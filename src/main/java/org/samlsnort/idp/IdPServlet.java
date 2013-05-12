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
package org.samlsnort.idp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.saml2.core.AuthnRequest;
import org.samlsnort.util.EncodingTool;
import org.samlsnort.util.SamlTool;
import org.samlsnort.util.XmlUtil;
import org.samlsnort.util.velocity.VelocityUtil;
import org.samlsnort.vo.SamlResponseData;
import org.samlsnort.vo.TestCase;
import org.samlsnort.vo.TestMode;

public class IdPServlet extends HttpServlet {

	private static final Charset CHARSET = Charset.forName("UTF-8");

	private static final long serialVersionUID = -1358533366802703279L;

	private static final Logger LOGGER = Logger.getLogger(IdPServlet.class
			.getName());

	private TestCase testCase;

	private TestMode testMode;

	public IdPServlet(TestMode testMode) {
		this.testMode = testMode;
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOGGER.entering(IdPServlet.class.getName(), "service", new Object[] {
				request, response });
		processRedirectBinding(request, response);

		LOGGER.exiting(IdPServlet.class.getName(), "service");
	}

	private void processRedirectBinding(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LOGGER.entering(IdPServlet.class.getName(), "processRedirectBinding",
				new Object[] { request, response });

		String samlRequest = request.getParameter("SAMLRequest");
		if (samlRequest != null) {

			validateSAMLRequest(samlRequest);

			String authnRequestString = null;
			try {
				if (testMode.equals(TestMode.SP_HTTP_POST)) {
					authnRequestString = EncodingTool.fromBase64(samlRequest);
				} else if (testMode.equals(TestMode.SP_HTTP_REDIRECT)) {
					authnRequestString = EncodingTool
							.inflateFromBase64(samlRequest);
				}
			} catch (Exception e) {
				LOGGER.throwing(IdPServlet.class.getName(),
						"processRedirectBinding", e);
			}

			if (authnRequestString != null) {

				testCase.setSamlAuthnRequest(XmlUtil
						.prettyFormat(authnRequestString));

				try {

					AuthnRequest authnRequest = (AuthnRequest) SamlTool
							.parseSamlObject(authnRequestString);

					String resp = SamlTool.generateResponse(authnRequest,testCase);

					testCase.setSamlResponse(XmlUtil.prettyFormat(resp));

					response.setHeader("Cache-Control", "no-cache, no-store");
					response.setHeader("Pragma", "no-cache");
					response.setCharacterEncoding("UTF-8");
					response.setContentType("text/html");

					String destination;

					if (SamlResponseData.REPLACE_WITH_AUTHN_REQUEST
							.equals(testCase.getSamlResponseData()
									.getDestination()))
						destination = authnRequest
								.getAssertionConsumerServiceURL();
					else
						destination = testCase.getSamlResponseData()
								.getDestination();

					VelocityUtil.writeForm(response.getWriter(), destination,
							request.getParameter("RelayState"), EncodingTool
									.toBase64(resp));
				} catch (Exception e) {
					LOGGER.throwing(IdPServlet.class.getName(),
							"processRedirectBinding", e);
					response.sendError(400, e.getMessage());
				}
			} else {
				LOGGER.info("SAMLRequest isn't valid");
				response.sendError(400, "SAMLRequest isn't valid");
			}
		} else {
			LOGGER.info("Missing required parameter: SAMLRequest");
			response.sendError(400, "Missing required parameter: SAMLRequest");
		}
		LOGGER.exiting(IdPServlet.class.getName(), "processRedirectBinding");

	}

	private boolean validateSAMLRequest(String samlRequest) {

		// Base64 check

		byte[] decodedSAMLRequest = null;
		try {
			decodedSAMLRequest = Base64.decodeBase64(samlRequest
					.getBytes(CHARSET));

			testCase.setBase64Valid(true);
		} catch (RuntimeException e) {
			testCase.setBase64Valid(false);
			return false;
		}

		// deflat check
		if (testMode.equals(TestMode.SP_HTTP_REDIRECT)) {
			try {
				EncodingTool.inflate(decodedSAMLRequest);

				testCase.setDeflatedValid(true);
			} catch (DataFormatException e) {
				testCase.setDeflatedValid(false);
				return false;
			}
		} else
			testCase.setDeflatedValid(null);
		return true;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}
	

}
