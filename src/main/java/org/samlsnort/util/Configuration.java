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
package org.samlsnort.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.samlsnort.vo.SamlResponseData;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;

public class Configuration {
	private static final Logger LOGGER = Logger.getLogger(Configuration.class
			.getName());
	private static Configuration instance = new Configuration();
	private static final Properties CONFIG;
	private ServerSocket socket;
	
	static {
		CONFIG = new Properties();
		try {
			CONFIG.load(new FileInputStream(
					new File("config/config.properties")));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Property '"
					+ new File("config/config.properties").getAbsolutePath()
					+ " not found", e);
			System.exit(1);
		}
	}

	public static Configuration getInstance() {
		return instance;
	}

	public void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "UIManger:", e);
		}

		try {
			    socket = new ServerSocket(getSingleInstantPort(),0,InetAddress.getByAddress(new byte[] {127,0,0,1}));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Only one application instant is allowed.");
			throw new RuntimeException(
					"Only one application instant is allowed.");
		}

		if (!getTempFolder().exists())
			getTempFolder().mkdir();

		if (getHttpProxyHost().length() > 0 && getHttpProxyPort().length() > 0) {
			System.setProperty("http.proxyPort", getHttpProxyPort());
			System.setProperty("http.proxyHost", getHttpProxyHost());
			System.setProperty("https.proxyPort", getHttpProxyPort());
			System.setProperty("https.proxyHost", getHttpProxyHost());
		}

		UIUtils.setPreferredLookAndFeel();
		NativeInterface.open();

		CookieManager manager = new CookieManager();
		CookieHandler.setDefault(manager);

	}

	public void exit() {
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.throwing(Configuration.class.getName(), "exit", e);
		}
		try {
			for (File file : getTempFolder().listFiles()) {
				file.delete();
			}
			getTempFolder().delete();
		} catch (Exception e) {
			LOGGER.throwing(Configuration.class.getName(), "exit", e);
		} finally {
			System.exit(0);
		}
	}

	public String getHttpProxyHost() {
		return CONFIG.getProperty("httpProxyHost");
	}

	public String getHttpProxyPort() {
		return CONFIG.getProperty("httpProxyPort");
	}

	public String getHttpsProxyHost() {
		return CONFIG.getProperty("httpsProxyHost");
	}

	public String getHttpsProxyPort() {
		return CONFIG.getProperty("httpsProxyPort");
	}

	public String getKeystorePassword() {
		return CONFIG.getProperty("keystorePassword");
	}

	public String getKeystoreLocationPath() {
		return new File(CONFIG.getProperty("keystoreLocation"))
				.getAbsolutePath();
	}

	public File getKeystoreLocation() {
		return new File(getKeystoreLocationPath());
	}

	public File getTempFolder() {
		return new File(getTempFolderPath());
	}

	public String getTempFolderPath() {
		return new File(CONFIG.getProperty("tempFolder")).getAbsolutePath();
	}

	public int getSingleInstantPort() {
		return Integer.parseInt(CONFIG.getProperty("singleInstantPort"));
	}

	public int getPort() {
		return Integer.parseInt(CONFIG.getProperty("port"));
	}

	public String getHost() {
		return CONFIG.getProperty("host");
	}
	
	
	public String getResultExpositionMethod() {
		return CONFIG.getProperty("resultExpositionMethod");
	}
	
	
	
	public List<String> getDestinationOptions() {
		List<String> result = new LinkedList<String>();
		result.add(SamlResponseData.REPLACE_WITH_AUTHN_REQUEST);
		result.addAll(getSplitedProperty("destinationOptions"));
		return result;
	}

	public List<String> getInResponseToOptions() {
		List<String> result = new LinkedList<String>();
		result.add(SamlResponseData.REPLACE_WITH_AUTHN_REQUEST);
		result.addAll(getSplitedProperty("inResponseToOptions"));
		return result;
	}

	public List<String> getTimeOptions() {
		return getSplitedProperty("timeOptions");
	}

	public List<String> getStatusCodeValueOptions() {
		return getSplitedProperty("statusCodeValueOptions");
	}

	public List<String> getCanocializationMethodAlgorithmOptions() {
		return getSplitedProperty("canocializationMethodAlgorithmOptions");
	}

	public List<String> getSignatureMethodAlgorithmOptions() {
		return getSplitedProperty("signatureMethodAlgorithmOptions");
	}

	public List<String> getSubjectFormatOptions() {
		return getSplitedProperty("subjectFormatOptions");
	}

	public List<String> getSubjectRecipientOptions() {
		List<String> result = new LinkedList<String>();
		result.add(SamlResponseData.REPLACE_WITH_AUTHN_REQUEST);
		result.addAll(getSplitedProperty("subjectRecipientOptions"));
		return result;
	}

	public List<String> getSubjectInResponseToOptions() {
		List<String> result = new LinkedList<String>();
		result.add(SamlResponseData.REPLACE_WITH_AUTHN_REQUEST);
		result.addAll(getSplitedProperty("subjectInResponseToOptions"));
		return result;
	}

	public List<String> getAuthnContextClassRefOptions() {
		List<String> result = new LinkedList<String>();
		result.add(SamlResponseData.REPLACE_WITH_AUTHN_REQUEST);
		result.addAll(getSplitedProperty("authnContextClassRefOptions"));
		return result;
	}

	public List<String> getSubjectLocalityAddress() {
		List<String> result = new LinkedList<String>();
		try {
			result.add(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
		}
		result.addAll(getSplitedProperty("subjectLocalityAddressOptions"));
		return result;
	}

	public List<String> getAudienceOptions() {
		List<String> result = new LinkedList<String>();
		result.add(SamlResponseData.REPLACE_WITH_AUTHN_REQUEST);
		result.addAll(getSplitedProperty("audienceOptions"));
		return result;
	}

	public List<String> getIssuerOptions() {
		List<String> result = new LinkedList<String>();
		result.addAll(getSplitedProperty("issuerOptions"));
		return result;
	}

	public List<String> getSubjectValueOptions() {
		List<String> result = new LinkedList<String>();
		result.addAll(getSplitedProperty("subjectValueOptions"));
		return result;
	}

	public List<String> getSubjectLocalityDNSNameOptions() {
		List<String> result = new LinkedList<String>();
		result.addAll(getSplitedProperty("subjectLocalityDNSNameOptions"));
		return result;
	}

	private List<String> getSplitedProperty(String property) {
		List<String> result = new LinkedList<String>();
		String[] splited = CONFIG.getProperty(property).split(";", -1);

		if (splited.length == 1 && splited[0].length() == 0) {
			// do nothing
		} else {
			result.addAll(Arrays.asList(splited));
		}
		return result;

	}

}
