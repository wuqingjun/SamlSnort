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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.KeyStore.Entry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.EnumerationUtils;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.KeyStoreCredentialResolver;
import org.opensaml.xml.security.criteria.EntityIDCriteria;

/**
 * @author srzrbh
 * 
 */
public class KeyStoreTool {
	private static final Logger LOGGER = Logger.getLogger(KeyStoreTool.class
			.getName());
	private static KeyStore keyStore;

	static {
		try {
			keyStore = KeyStore.getInstance("jks");
			keyStore.load(new FileInputStream(Configuration.getInstance()
					.getKeystoreLocation()), Configuration.getInstance()
					.getKeystorePassword().toCharArray());
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error while loading keystore.", e);
		}
	}

	/**
	 * @param certName
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static Credential getCredential(String certName)
			throws NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, SecurityException {
		LOGGER
				.entering(KeyStoreTool.class.getName(), "getCredential",
						certName);
		Credential credential = null;
		if (certName != null && certName.length() > 0) {
			Map<String, String> passwords = new HashMap<String, String>();
			passwords.put(certName, Configuration.getInstance()
					.getKeystorePassword());
			KeyStoreCredentialResolver keyStoreCredentialResolver = new KeyStoreCredentialResolver(
					keyStore, passwords);
		
			try {
				credential = keyStoreCredentialResolver
						.resolveSingle(new CriteriaSet(new EntityIDCriteria(
								certName)));
			} catch (Exception e) {
				LOGGER.throwing(KeyStoreTool.class.getName(), "getCredential", e);
			}
		}
		LOGGER.exiting(KeyStoreTool.class.getName(), "getCredential",
				credential);

		return credential;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getAliases() {
		LOGGER.entering(KeyStoreTool.class.getName(), "getAliases");

		List<String> result = new LinkedList<String>();

		try {
			result.addAll(EnumerationUtils.toList(keyStore.aliases()));
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error while reading aliases.", e);
		}

		LOGGER.exiting(KeyStoreTool.class.getName(), "getAliases", result);

		return result;
	}

	public static Certificate getCertificat(String alias)
			throws KeyStoreException {
		LOGGER.entering(KeyStoreTool.class.getName(), "getCertificat", alias);

		Certificate result = keyStore.getCertificate(alias);

		LOGGER.exiting(KeyStoreTool.class.getName(), "getCertificat", result);

		return result;
	}

	public static Key getKey(String alias) throws KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException {
		LOGGER.entering(KeyStoreTool.class.getName(), "getKey", alias);

		Key result = keyStore.getKey(alias, Configuration.getInstance()
				.getKeystorePassword().toCharArray());

		LOGGER.exiting(KeyStoreTool.class.getName(), "getKey", result);

		return result;
	}

	public static Entry getEntry(String alias) throws KeyStoreException,
			NoSuchAlgorithmException, UnrecoverableEntryException {
		LOGGER.entering(KeyStoreTool.class.getName(), "getEntry", alias);

		Entry result = keyStore.getEntry(alias,
				new KeyStore.PasswordProtection(Configuration.getInstance()
						.getKeystorePassword().toCharArray()));

		LOGGER.exiting(KeyStoreTool.class.getName(), "getEntry", result);

		return result;
	}

	public static Certificate[] getCertificateChain(String alias)
			throws KeyStoreException, NoSuchAlgorithmException,
			UnrecoverableEntryException {
		LOGGER.entering(KeyStoreTool.class.getName(), "getCertificateChain",
				alias);

		Certificate[] result = keyStore.getCertificateChain(alias);

		LOGGER.exiting(KeyStoreTool.class.getName(), "getCertificateChain",
				result);

		return result;
	}

	public static void deleteAlias(String alias) throws KeyStoreException,
			NoSuchAlgorithmException, UnrecoverableEntryException,
			CertificateException, IOException {
		LOGGER.entering(KeyStoreTool.class.getName(), "deleteAlias", alias);

		keyStore.deleteEntry(alias);
		saveKeystore();

		LOGGER.exiting(KeyStoreTool.class.getName(), "deleteAlias");

	}

	public static void importIntoKeystore(File certificateChainFile,
			File privateKeyFile, String alias) throws Exception {

		FileInputStream certificateStream = new FileInputStream(
				certificateChainFile);
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance("X.509");
		Certificate[] chain = {};
		chain = certificateFactory.generateCertificates(certificateStream)
				.toArray(chain);
		certificateStream.close();

		byte[] encodedKey = new byte[(int) privateKeyFile.length()];
		FileInputStream keyInputStream = new FileInputStream(privateKeyFile);
		keyInputStream.read(encodedKey);
		keyInputStream.close();
		KeyFactory rSAKeyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = rSAKeyFactory
				.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));

		keyStore.setEntry(alias,
				new KeyStore.PrivateKeyEntry(privateKey, chain),
				new KeyStore.PasswordProtection(Configuration.getInstance()
						.getKeystorePassword().toCharArray()));

		


		
		saveKeystore();
	}

	private static void saveKeystore() throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		FileOutputStream keyStoreOutputStream = new FileOutputStream(
				Configuration.getInstance().getKeystoreLocation());
		keyStore.store(keyStoreOutputStream, Configuration.getInstance()
				.getKeystorePassword().toCharArray());
		keyStoreOutputStream.close();
	}
}
