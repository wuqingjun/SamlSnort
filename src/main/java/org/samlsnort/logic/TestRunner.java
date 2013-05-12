/*******************************************************************************
 * Copyright (C) 2011 by Michael Gerber
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package org.samlsnort.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.joda.time.DateTime;
import org.samlsnort.gui.listener.TestListener;
import org.samlsnort.idp.IdPServlet;
import org.samlsnort.util.Configuration;
import org.samlsnort.util.EncodingTool;
import org.samlsnort.util.SamlTool;
import org.samlsnort.util.XmlUtil;
import org.samlsnort.util.velocity.VelocityUtil;
import org.samlsnort.vo.TestCase;
import org.samlsnort.vo.TestMode;
import org.samlsnort.vo.TestSuite;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultPageCreator;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHead;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class TestRunner {

	private static final Logger LOGGER = Logger.getLogger(TestRunner.class
			.getName());
	boolean cheat = true;
	private Server server;
	private TestListener listener;

	private IdPServlet servlet;

	public TestRunner(TestListener listener) {
		this.listener = listener;
	}

	public TestSuite runTests(TestSuite testSuite) throws Exception {
		LOGGER.entering(TestRunner.class.getName(), "runTests", testSuite);

		JWebBrowser.clearSessionCookies();
		startServer(testSuite.getTestMode());
		cleanTestSuite(testSuite);

		BrowserVersion browserVersion = BrowserVersion.INTERNET_EXPLORER_8;
		browserVersion
				.setUserAgent("Saml Test Tool Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)");

		WebClient webClient;

		if (Configuration.getInstance().getHttpProxyHost().length() > 0
				&& Configuration.getInstance().getHttpProxyPort().length() > 0) {
			webClient = new WebClient(browserVersion, Configuration
					.getInstance().getHttpProxyHost(), Integer
					.parseInt(Configuration.getInstance().getHttpProxyPort()));
		} else {
			webClient = new WebClient(browserVersion);
		}
		if (TestMode.SP_HTTP_REDIRECT.equals(testSuite.getTestMode())
				|| TestMode.SP_HTTP_POST.equals(testSuite.getTestMode())) {
			webClient.setPageCreator(new LocalPageCreator(testSuite.getHost()));
		}

		int testCaseCount = 1;
		testSuite.setStartTime(new DateTime());
		LOGGER.fine("Run tests for test suite: " + testSuite);
		for (TestCase testCase : testSuite.getTestCases()) {
			listener.startTestCase(testCaseCount, testSuite.getTestCases()
					.size(), testCase);
			testCase.setStartTime(new DateTime());

			LOGGER.fine("Run test for test case: " + testCase);
			HtmlPage page = null;

			if (TestMode.SP_HTTP_REDIRECT.equals(testSuite.getTestMode())
					|| TestMode.SP_HTTP_POST.equals(testSuite.getTestMode())) {
				servlet.setTestCase(testCase);
				try {
					boolean complete = false;
					String url = testSuite.getServiceProviderURL();
					while (!complete) {
						try {
							System.out.println(url);
							page = webClient.getPage(url);
							complete = true;
						} catch (FailingHttpStatusCodeException e) {

							if (e.getResponse().getWebRequest().getUrl()
									.toString().indexOf(testSuite.getHost()) >= 0) {
								url = e.getResponse().getWebRequest().getUrl()
										.toString().replaceAll(
												testSuite.getHost(),
												InetAddress.getLocalHost()
														.getHostAddress()
														+ ":"
														+ Configuration
																.getInstance()
																.getPort());
							} else {
								throw e;
							}
						} catch (Exception e) {
							throw e;
						}
					}

					updateResult(testCase, webClient, page);
					if (testCase == testSuite.getTestCases().get(0)
							&& testCase.getSamlAuthnRequest() != null
							&& testCase.getSamlResponse() != null
							&& testCase.getResultPageText() != null
							&& testCase.getResultPageHTML() != null
							&& testCase.getResultURL() != null) {
						testCase.setTestSuccessful(true);
						LOGGER.fine("Test successful");
					} else if (testCase.getResultURL() != null
							&& testCase.getResultURL().equals(
									testSuite.getTestCases().get(0)
											.getResultURL())
							&& testSuite.getTestCases().get(0)
									.getTestSuccessful()) {
						testCase.setTestSuccessful(true);
						LOGGER.fine("Test successful");
					} else {
						testCase.setTestSuccessful(false);
						LOGGER.fine("Test failed");
					}
				} catch (Exception e) {
					testCase.setTestSuccessful(false);
					testCase.setResultPageText(e.getMessage());
					LOGGER.fine("Test failed");
					LOGGER.throwing(TestRunner.class.getName(), "runTests", e);
				}
			} else if (TestMode.IDP_INITIATED.equals(testSuite.getTestMode())) {

				String resp = SamlTool.generateResponse(null, testCase);
				testCase.setSamlResponse(XmlUtil.prettyFormat(resp));

				File htmlFile = null;
				FileWriter wr = null;
				try {
					htmlFile = new File(Configuration.getInstance()
							.getTempFolder(), System.currentTimeMillis()
							+ ".html");
					wr = new FileWriter(htmlFile);
					VelocityUtil.writeForm(wr, testSuite
							.getServiceProviderURL(), null, EncodingTool
							.toBase64(resp));
					page = webClient.getPage(htmlFile.toURI().toURL());

					updateResult(testCase, webClient, page);
					if (testCase == testSuite.getTestCases().get(0)) {
						testCase.setTestSuccessful(true);
						LOGGER.fine("Test successful");
					} else if (testCase.getResultURL().equals(
							testSuite.getTestCases().get(0).getResultURL())
							&& testSuite.getTestCases().get(0)
									.getTestSuccessful()) {
						testCase.setTestSuccessful(true);
						LOGGER.fine("Test successful");
					} else {
						testCase.setTestSuccessful(false);
						LOGGER.fine("Test failed");
					}

				} catch (Exception e) {
					testCase.setTestSuccessful(false);
					testCase.setResultPageText(e.getMessage());
					LOGGER.fine("Test failed");
					LOGGER.throwing(TestRunner.class.getName(), "runTests", e);
				} finally {
					if (wr != null) {
						try {
							wr.close();
						} catch (Exception e) {
						}
					}
					if (htmlFile != null) {
						try {
							if (!htmlFile.delete()) {
								htmlFile.deleteOnExit();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}
			testCase.setEndTime(new DateTime());
			listener.endTestCase(testCaseCount++, testSuite.getTestCases()
					.size(), testCase);
		}
		testSuite.setEndTime(new DateTime());
		stopServer();

		LOGGER.exiting(TestRunner.class.getName(), "runTests", testSuite);
		return testSuite;
	}

	private void updateResult(TestCase testCase, WebClient webClient,
			HtmlPage page) {
		if (page != null) {
			HtmlHead head = (HtmlHead) page.getElementsByTagName("head").get(0);
			HtmlElement base = head.appendChildIfNoneExists("base");
			base.setAttribute("href", page.getUrl().toString());
			head.removeChild(base);
			head.insertBefore(base);

			testCase.setResultPageText(page.asText());
			testCase.setResultPageHTML(page.asXml());
			testCase.setResultURL(page.getUrl());
			for (Cookie cookie : webClient.getCookieManager().getCookies()) {
				JWebBrowser.setCookie(page.getUrl().toString(), cookie
						.toString());
			}

		}
		webClient.closeAllWindows();
		webClient.getCookieManager().clearCookies();
		webClient.getCache().clear();
	}

	private void cleanTestSuite(TestSuite testSuite) {
		testSuite.setStartTime(null);
		testSuite.setEndTime(null);
		for (TestCase testCase : testSuite.getTestCases()) {
			testCase.setBase64Valid(null);
			testCase.setDeflatedValid(null);
			testCase.setEndTime(null);
			testCase.setResultPageHTML(null);
			testCase.setResultPageText(null);
			testCase.setResultURL(null);
			testCase.setSamlAuthnRequest(null);
			testCase.setSamlResponse(null);
			testCase.setStartTime(null);
			testCase.setTestSuccessful(null);
		}
	}

	public void stopServer() throws Exception {
		LOGGER.entering(TestRunner.class.getName(), "stopServer");
		if (server != null && server.isRunning())
			server.stop();
		LOGGER.exiting(TestRunner.class.getName(), "stopServer");
	}

	public void startServer(TestMode testMode) throws Exception {
		LOGGER.entering(TestRunner.class.getName(), "startServer");

		if (testMode != TestMode.IDP_INITIATED) {
			int port = 0;
			try {
				port = Configuration.getInstance().getPort();
			} catch (Exception e) {
				throw new RuntimeException(
						"Port (port) must be defined in 'config.properties'");
			}
			server = new Server(port);
			servlet = new IdPServlet(testMode);
			ServletHolder servletHolder = new ServletHolder(servlet);
			ServletContextHandler servletContextHandler = new ServletContextHandler();
			servletContextHandler.setContextPath("/");
			servletContextHandler.addServlet(servletHolder, "/*");
			server.setHandler(servletContextHandler);
			server.start();

			LOGGER.fine("Server started on: localhost:" + port);
		} else {
			LOGGER.fine("No server started");
		}

		LOGGER.exiting(TestRunner.class.getName(), "startServer");
	}

	class LocalPageCreator extends DefaultPageCreator {
		private static final long serialVersionUID = 1L;

		private String host;

		public LocalPageCreator(String host) {
			this.host = host;
		}

		@Override
		public Page createPage(WebResponse webResponse, WebWindow webWindow)
				throws IOException {
			String content = webResponse.getContentAsString();
			content = content.replaceAll(host, InetAddress.getLocalHost()
					.getHostAddress()
					+ ":" + Configuration.getInstance().getPort());
			return super.createPage(new WebResponse(new WebResponseData(content
					.getBytes(), webResponse.getStatusCode(), webResponse
					.getStatusMessage(), webResponse.getResponseHeaders()),
					webResponse.getWebRequest(), webResponse.getLoadTime()),
					webWindow);
		}
	}

}
