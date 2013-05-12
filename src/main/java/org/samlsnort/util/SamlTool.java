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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.utils.ElementProxy;
import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.Signer;
import org.samlsnort.vo.AttributeData;
import org.samlsnort.vo.SamlResponseData;
import org.samlsnort.vo.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SamlTool {

	private static final Logger LOGGER = Logger.getLogger(SamlTool.class
			.getName());

	private static BasicParserPool basicParserPool;
	private static SecureRandomIdentifierGenerator GENERATOR;
	private static SamlResponseData samlResponseData;
	private static AuthnRequest authnRequest;
	private static DateTime startTime;
	static {

		try {
			// Initialize the library
			DefaultBootstrap.bootstrap();
			GENERATOR = new SecureRandomIdentifierGenerator();

			// Get parser pool manager
			basicParserPool = new BasicParserPool();
			basicParserPool.setNamespaceAware(true);
			basicParserPool.setXincludeAware(true);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error while initializing Saml Tool.", e);
		}
	}

	public static XMLObject parseSamlObject(String xmlInput)
			throws XMLParserException, UnmarshallingException {
		LOGGER.entering(SamlTool.class.getName(), "parseSamlObject", xmlInput);

		StringReader reader = new StringReader(xmlInput);

		// Parse metadata file
		Document inCommonMDDoc = basicParserPool.parse(reader);
		Element metadataRoot = inCommonMDDoc.getDocumentElement();

		// Get apropriate unmarshaller
		UnmarshallerFactory unmarshallerFactory = Configuration
				.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory
				.getUnmarshaller(metadataRoot);

		// Unmarshall using the document root element, an EntitiesDescriptor in
		// this case
		XMLObject result = unmarshaller.unmarshall(metadataRoot);
		LOGGER.exiting(SamlTool.class.getName(), "parseSamlObject", result);
		return result;
	}

	public static String generateResponse(AuthnRequest authnRequest,
			TestCase testCase) throws Exception {
		LOGGER.entering(SamlTool.class.getName(), "generateResponse");

		SamlTool.samlResponseData = testCase.getSamlResponseData();
		SamlTool.startTime = testCase.getStartTime();
		SamlTool.authnRequest = authnRequest;

		String result = writeXML(createResponse());
		LOGGER.exiting(SamlTool.class.getName(), "generateResponse", result);
		return result;
	}

	public static String writeXML(XMLObject object)
			throws MarshallingException, TransformerException {
		LOGGER.entering(SamlTool.class.getName(), "writeXML", object);

		// Get the marshaller factory

		MarshallerFactory marshallerFactory = Configuration
				.getMarshallerFactory();

		Marshaller marshaller = marshallerFactory.getMarshaller(object);

		// Marshall the Subject
		Element element = marshaller.marshall(object);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(element);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);

		LOGGER.exiting(SamlTool.class.getName(), "writeXML", writer.getBuffer()
				.toString());
		return writer.getBuffer().toString();
	}

	public static String writeFormatedXML(XMLObject object)
			throws MarshallingException, TransformerException {
		LOGGER.entering(SamlTool.class.getName(), "writeFormatedXML", object);

		// Get the marshaller factory

		MarshallerFactory marshallerFactory = Configuration
				.getMarshallerFactory();

		Marshaller marshaller = marshallerFactory.getMarshaller(object);

		// Marshall the Subject
		Element element = marshaller.marshall(object);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(element);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);

		LOGGER.exiting(SamlTool.class.getName(), "writeFormatedXML", writer
				.getBuffer().toString());
		return writer.getBuffer().toString();
	}

	private static Response createResponse() throws Exception {

		Response response = create(Response.DEFAULT_ELEMENT_NAME);

		response.setID(GENERATOR.generateIdentifier());

		if (SamlResponseData.REPLACE_WITH_AUTHN_REQUEST.equals(samlResponseData
				.getInResponseTo())) {
			response.setInResponseTo(authnRequest.getID());
		} else
			response.setInResponseTo(samlResponseData.getInResponseTo());

		if (SamlResponseData.REPLACE_WITH_AUTHN_REQUEST.equals(samlResponseData
				.getDestination()))
			response.setDestination(authnRequest
					.getAssertionConsumerServiceURL());
		else
			response.setDestination(samlResponseData.getDestination());

		if (samlResponseData.getIssueInstant() != null) {
			response.setIssueInstant(startTime.plusMinutes(samlResponseData
					.getIssueInstant()));
		}

		response.setIssuer(createIssuer(samlResponseData.getIssuer()));
		response.setStatus(createStatus());
		response.getAssertions().add(createAssertion());

		return response;
	}

	private static Issuer createIssuer(String value) {
		if (value != null && value.length() > 0) {
			Issuer issuer = create(Issuer.DEFAULT_ELEMENT_NAME);
			issuer.setValue(value);
			return issuer;

		} else {
			return null;
		}
	}

	private static Status createStatus() {
		Status status = null;

		if (samlResponseData.getStatusCodeValue() != null
				&& samlResponseData.getStatusCodeValue().length() > 0) {
			StatusCode statusCodeElement = create(StatusCode.DEFAULT_ELEMENT_NAME);
			statusCodeElement.setValue(samlResponseData.getStatusCodeValue());
			status = create(Status.DEFAULT_ELEMENT_NAME);
			status.setStatusCode(statusCodeElement);
		}
		return status;
	}

	private static Assertion createAssertion() throws Exception {

		Assertion assertion = create(Assertion.DEFAULT_ELEMENT_NAME);
		assertion.setID(GENERATOR.generateIdentifier());
		assertion.setIssuer(createIssuer(samlResponseData.getAssertionIssuer()));
		if (samlResponseData.getIssueInstant() != null) {
			assertion.setIssueInstant(startTime.plusMinutes(samlResponseData
					.getIssueInstant()));
		}

		Signature signature = createSignature();
		assertion.setSignature(signature);
		assertion.setSubject(createSubject());
		assertion.setConditions(createCondition());
		assertion.getAuthnStatements().add(createAuthnStatement());
		if (samlResponseData.getAttributes() != null
				&& !samlResponseData.getAttributes().isEmpty()) {
			assertion.getAttributeStatements().add(createAttributeStatement());
		}
		if (samlResponseData.getSign() != null
				&& samlResponseData.getSign() == true
				&& samlResponseData.getCertificateAlias() != null
				&& signature.getCanonicalizationAlgorithm() != null
				&& signature.getSignatureAlgorithm() != null) {
			Configuration.getMarshallerFactory().getMarshaller(assertion)
					.marshall(assertion);
			try {
				Signer.signObject(signature);
			} catch (Exception e) {
			}
		}
		return assertion;
	}

	private static AttributeStatement createAttributeStatement() {

		AttributeStatement attributeStatement = create(AttributeStatement.DEFAULT_ELEMENT_NAME);

		for (AttributeData attributeData : samlResponseData.getAttributes()) {
			Attribute attribute = create(Attribute.DEFAULT_ELEMENT_NAME);
			attribute.setFriendlyName(attributeData.getFriendlyName());
			attribute.setName(attributeData.getName());
			attribute.setNameFormat(attributeData.getNameFormat());

			XMLObjectBuilder<XSAny> builder = getXMLObjectBuilder(XSAny.TYPE_NAME);

			for (String values : attributeData.getValue().split(";", -1)) {
				XSAny value = builder
						.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);

				value.setTextContent(values);

				attribute.getAttributeValues().add(value);
			}

			attributeStatement.getAttributes().add(attribute);
		}
		return attributeStatement;
	}

	private static AuthnStatement createAuthnStatement() {
		AuthnStatement authnStatement = null;
		AuthnContext authnContext = null;
		SubjectLocality subjectLocality = null;
		if (samlResponseData.getAuthnContextClassRef() != null
				&& samlResponseData.getAuthnContextClassRef().length() > 0) {
			authnContext = create(AuthnContext.DEFAULT_ELEMENT_NAME);

			AuthnContextClassRef authnContextClassRef = create(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
			if (samlResponseData.getAuthnContextClassRef().equals(
					SamlResponseData.REPLACE_WITH_AUTHN_REQUEST)) {
				authnContextClassRef.setAuthnContextClassRef(authnRequest
						.getRequestedAuthnContext().getAuthnContextClassRefs()
						.get(0).getAuthnContextClassRef());
			} else {
				authnContextClassRef.setAuthnContextClassRef(samlResponseData
						.getAuthnContextClassRef());
			}
			authnContext.setAuthnContextClassRef(authnContextClassRef);
		}

		if (samlResponseData.getSubjectLocalityAddress() != null
				&& samlResponseData.getSubjectLocalityAddress().length() > 0) {
			subjectLocality = create(SubjectLocality.DEFAULT_ELEMENT_NAME);
			subjectLocality.setAddress(samlResponseData
					.getSubjectLocalityAddress());
		}

		if (samlResponseData.getSubjectLocalityDNSName() != null
				&& samlResponseData.getSubjectLocalityDNSName().length() > 0) {

			if (subjectLocality == null)
				subjectLocality = create(SubjectLocality.DEFAULT_ELEMENT_NAME);
			subjectLocality.setDNSName(samlResponseData
					.getSubjectLocalityDNSName());
		}

		if (authnContext != null || subjectLocality != null
				|| samlResponseData.getAuthnInstant() != null) {
			authnStatement = create(AuthnStatement.DEFAULT_ELEMENT_NAME);
			if (samlResponseData.getAuthnInstant() != null) {
				authnStatement.setAuthnInstant(startTime
						.plusMinutes(samlResponseData.getAuthnInstant()));
			}
			authnStatement.setSessionIndex(GENERATOR.generateIdentifier());
			authnStatement.setAuthnContext(authnContext);
			authnStatement.setSubjectLocality(subjectLocality);
		}

		return authnStatement;
	}

	private static Conditions createCondition() {
		Conditions conditions = null;
		AudienceRestriction audienceRestriction = null;
		if (samlResponseData.getAudience() != null
				&& samlResponseData.getAudience().length() > 0) {
			Audience audience = create(Audience.DEFAULT_ELEMENT_NAME);

			if (SamlResponseData.REPLACE_WITH_AUTHN_REQUEST
					.equals(samlResponseData.getAudience())) {
				audience.setAudienceURI(authnRequest.getIssuer().getValue());
			} else {
				audience.setAudienceURI(samlResponseData.getAudience());
			}

			audienceRestriction = create(AudienceRestriction.DEFAULT_ELEMENT_NAME);
			audienceRestriction.getAudiences().add(audience);
		}

		if (samlResponseData.getConditionNotBefore() != null
				|| samlResponseData.getConditionNotOnOrAfter() != null
				|| audienceRestriction != null) {
			conditions = create(Conditions.DEFAULT_ELEMENT_NAME);
			if (samlResponseData.getConditionNotBefore() != null)
				conditions.setNotBefore(startTime.plusMinutes(samlResponseData
						.getConditionNotBefore()));
			if (samlResponseData.getConditionNotOnOrAfter() != null)
				conditions.setNotOnOrAfter(startTime
						.plusMinutes(samlResponseData
								.getConditionNotOnOrAfter()));
			conditions.getAudienceRestrictions().add(audienceRestriction);
		}

		return conditions;
	}

	private static Signature createSignature() throws Exception {
		ElementProxy.setDefaultPrefix("http://www.w3.org/2000/09/xmldsig#", "");

		Signature signature = null;
		if ((samlResponseData.getSignatureMethodAlgorithm() != null && samlResponseData
				.getSignatureMethodAlgorithm().length() > 0)
				|| (samlResponseData.getCanocializationMethodAlgorithm() != null && samlResponseData
						.getCanocializationMethodAlgorithm().length() > 0)
				|| samlResponseData.getCertificateAlias() != null) {
			signature = create(Signature.DEFAULT_ELEMENT_NAME, "");

			signature.setSignatureAlgorithm(samlResponseData
					.getSignatureMethodAlgorithm());
			signature.setCanonicalizationAlgorithm(samlResponseData
					.getCanocializationMethodAlgorithm());
			if (samlResponseData.getCertificateAlias() != null) {

				Credential credential = KeyStoreTool
						.getCredential(samlResponseData.getCertificateAlias());
				if (credential != null) {
					signature.setSigningCredential(credential);

					KeyInfo keyInfo = Configuration
							.getGlobalSecurityConfiguration()
							.getKeyInfoGeneratorManager().getDefaultManager()
							.getFactory(credential).newInstance().generate(
									credential);

					signature.setKeyInfo(keyInfo);
				}

			}
		}

		return signature;
	}

	private static NameID createNameID(String value, String format) {
		if (value != null && value.length() > 0 && format != null
				&& format.length() > 0) {
			NameID nameID = create(NameID.DEFAULT_ELEMENT_NAME);
			nameID.setValue(value);
			nameID.setFormat(format);
			return nameID;
		} else
			return null;
	}

	private static Subject createSubject() {
		Subject subject = null;

		NameID nameID = createNameID(samlResponseData.getSubjectValue(),
				samlResponseData.getSubjectFormat());

		if (nameID != null
				|| samlResponseData.getSubjectNotOnOrAfter() != null
				|| samlResponseData.getSubjectNotBefore() != null
				|| (samlResponseData.getSubjectInResponseTo() != null && samlResponseData
						.getSubjectInResponseTo().length() > 0)
				|| (samlResponseData.getSubjectRecipient() != null && samlResponseData
						.getSubjectRecipient().length() > 0)) {
			subject = create(Subject.DEFAULT_ELEMENT_NAME);
			subject.setNameID(nameID);
			SubjectConfirmationData subjectConfirmationData = create(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);

			if (SamlResponseData.REPLACE_WITH_AUTHN_REQUEST
					.equals(samlResponseData.getSubjectInResponseTo()))
				subjectConfirmationData.setInResponseTo(authnRequest.getID());
			else
				subjectConfirmationData.setInResponseTo(samlResponseData
						.getSubjectInResponseTo());

			if (samlResponseData.getSubjectNotOnOrAfter() != null)
				subjectConfirmationData
						.setNotOnOrAfter(startTime.plusMinutes(samlResponseData
								.getSubjectNotOnOrAfter()));
			if (samlResponseData.getSubjectNotBefore() != null)
				subjectConfirmationData.setNotBefore(startTime
						.plusMinutes(samlResponseData.getSubjectNotBefore()));

			if (SamlResponseData.REPLACE_WITH_AUTHN_REQUEST
					.equals(samlResponseData.getSubjectRecipient()))
				subjectConfirmationData.setRecipient(authnRequest
						.getAssertionConsumerServiceURL());
			else
				subjectConfirmationData.setRecipient(samlResponseData
						.getSubjectRecipient());

			SubjectConfirmation subjectConfirmation = create(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
			subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);
			subjectConfirmation
					.setSubjectConfirmationData(subjectConfirmationData);
			subject.getSubjectConfirmations().add(subjectConfirmation);
		}

		return subject;
	}

	@SuppressWarnings("unchecked")
	private static <T extends XMLObjectBuilder> T getXMLObjectBuilder(
			QName typeName) {
		return (T) Configuration.getBuilderFactory().getBuilder(typeName);
	}

	@SuppressWarnings("unchecked")
	private static <T extends XMLObject> T create(QName qname) {
		return (T) getXMLObjectBuilder(qname).buildObject(qname);
	}

	@SuppressWarnings("unchecked")
	private static <T extends XMLObject> T create(QName qname, String prefix) {
		return (T) getXMLObjectBuilder(qname).buildObject(
				qname.getNamespaceURI(), qname.getLocalPart(), prefix);
	}
}
