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

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.samlsnort.vo.SamlResponseData;
import org.samlsnort.vo.TestCase;

public class TestCaseUtil {
	private static final Logger LOGGER = Logger.getLogger(TestCaseUtil.class
			.getName());

	public static List<TestCase> createVariations(TestCase testCase)
			throws Exception {

		LOGGER.entering(TestCaseUtil.class.getName(), "createVariations",
				testCase);

		List<TestCase> result = new LinkedList<TestCase>();

		SamlResponseData correct = testCase.getSamlResponseData();
		for (Method method : SamlResponseData.class.getMethods()) {

			if (method.getName().startsWith("set")) {
				Object o = invokeGetMethodWithSameName(correct, method);
				if (o != null) {
					if (o instanceof String && ((String) o).length() == 0) {
						continue;
					}
					TestCase temp = new TestCase((SamlResponseData) correct
							.clone());
					temp.setName("Empty " + method.getName().substring(3)
							+ " Test Case");
					Object n = null;
					method.invoke(temp.getSamlResponseData(), n);
					result.add(temp);
				}

			}
		}

		LOGGER
				.exiting(TestCaseUtil.class.getName(), "createVariations",
						result);

		return result;
	}

	private static Object invokeGetMethodWithSameName(Object object,
			Method method) throws Exception {
		String methodName = "get" + method.getName().substring(3);
		Method getMethod = object.getClass()
				.getMethod(methodName, new Class[0]);
		return getMethod.invoke(object, new Object[0]);
	}

	public static TestCase createMinimalTestCase(TestCase correct,
			List<TestCase> testCases) throws Exception {
		LOGGER.entering(TestCaseUtil.class.getName(), "createMinimalTestCase",
				new Object[] { correct, testCases });

		Object n = null;
		TestCase result = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		result.setName("Minimal Test Case");
		for (TestCase testCase : testCases) {
			if (testCase.getTestSuccessful() != null
					&& testCase.getTestSuccessful()) {
				for (Method method : SamlResponseData.class.getMethods()) {
					if (method.getName().startsWith("set")) {
						Object o = invokeGetMethodWithSameName(testCase
								.getSamlResponseData(), method);
						if (o == null
								|| (o instanceof String && ((String) o)
										.length() == 0)) {
							method.invoke(result.getSamlResponseData(), n);
						}
					}
				}
			}
		}
		LOGGER.exiting(TestCaseUtil.class.getName(), "createMinimalTestCase",
				result);
		return result;
	}

	public static List<TestCase> createRequiredTestCases(TestCase correct)
			throws CloneNotSupportedException {
		List<TestCase> testCases = new LinkedList<TestCase>();
		// No IssueInstant
		TestCase noIssueInstant = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		noIssueInstant.setName("No Issue Instant");
		noIssueInstant
				.setDescription("IssueInstant [Required] The time instant of issue of the response. The time value is encoded in UTC, as described in Section 1.3.3.");
		noIssueInstant.setExpectSuccess(Boolean.FALSE);
		noIssueInstant.getSamlResponseData().setIssueInstant(null);
		testCases.add(noIssueInstant);

		// No Status Code Value
		TestCase wrongStatusCodeValue = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		wrongStatusCodeValue.setName("No Status Code Value");
		wrongStatusCodeValue
		.setDescription("<Status> [Required] A code representing the status of the corresponding request.");
		wrongStatusCodeValue.setExpectSuccess(Boolean.FALSE);
		wrongStatusCodeValue.getSamlResponseData().setStatusCodeValue(null);
		testCases.add(wrongStatusCodeValue);

		// No Assertion Issuer
		TestCase noIssuer = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		noIssuer.setName("No Assertion Issuer");
		noIssuer
				.setDescription("<Issuer> [Required] The SAML authority that is making the claim(s) in the assertion. The issuer SHOULD be unambiguous to the intended relying parties. This specification defines no particular relationship between the entity represented by this element and the signer of the assertion (if any). Any such requirements imposed by a relying party that consumes the assertion or by specific profiles are application-specific.");
		noIssuer.setExpectSuccess(Boolean.FALSE);
		noIssuer.getSamlResponseData().setAssertionIssuer(null);
		testCases.add(noIssuer);

		// No AuthnInstant
		TestCase noAuthnInstant = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		noAuthnInstant.setName("No AuthnInstant");
		noAuthnInstant
				.setDescription("AuthnInstant [Required] Specifies the time at which the authentication took place. The time value is encoded in UTC, as described in Section 1.3.3.");
		noAuthnInstant.setExpectSuccess(Boolean.FALSE);
		noAuthnInstant.getSamlResponseData().setAuthnInstant(null);
		testCases.add(noAuthnInstant);
		
		return testCases;
	}
	
	public static List<TestCase> createOptionalTestCases(TestCase correct) 	throws CloneNotSupportedException
	{
		List<TestCase> testCases = new LinkedList<TestCase>();
		// Wrong Destination
		TestCase wrongDestiantion = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		wrongDestiantion.setName("Wrong Destination");
		wrongDestiantion
				.setDescription("Destination [Optional] A URI reference indicating the address to which this response has been sent. This is useful to prevent malicious forwarding of responses to unintended recipients, a protection that is required by some protocol bindings. If it is present, the actual recipient MUST check that the URI reference identifies the location at which the message was received. If it does not, the response MUST");
		wrongDestiantion.setExpectSuccess(Boolean.FALSE);
		wrongDestiantion.getSamlResponseData().setDestination(
				"https://samlsnort.org/sp");
		testCases.add(wrongDestiantion);


		// Wrong InResponseTo
		TestCase wrongInResponseTo = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		wrongInResponseTo.setName("Wrong InResponseTo");
		wrongInResponseTo
		.setDescription("InResponseTo [Optional] A reference to the identifier of the request to which the response corresponds, if any. If the response is not generated in response to a request, or if the ID attribute value of a request cannot be determined (for example, the request is malformed), then this attribute MUST NOT be present. Otherwise, it MUST be present and its value MUST match the value of the corresponding request's ID attribute.");
		wrongInResponseTo.setExpectSuccess(Boolean.FALSE);
		wrongInResponseTo.getSamlResponseData().setInResponseTo(
		"s23c9e176eb9add30234d331d6371b9eb6ae73b234");
		testCases.add(wrongInResponseTo);
		
		// Wrong Status Code Value
		TestCase wrongStatusCodeValue = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		wrongStatusCodeValue.setName("Wrong Status Code Value");
		wrongStatusCodeValue
		.setDescription("<Status> [Required] A code representing the status of the corresponding request.");
		wrongStatusCodeValue.setExpectSuccess(Boolean.FALSE);
		wrongStatusCodeValue.getSamlResponseData().setStatusCodeValue("urn:oasis:names:tc:SAML:2.0:status:AuthnFailed");
		testCases.add(wrongStatusCodeValue);
		
		// Wrong Assertion Issuer
		TestCase wrongIssuer = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		wrongIssuer.setName("Wrong Assertion Issuer");
		wrongIssuer
		.setDescription("<Issuer> [Required] The SAML authority that is making the claim(s) in the assertion. The issuer SHOULD be unambiguous to the intended relying parties. This specification defines no particular relationship between the entity represented by this element and the signer of the assertion (if any). Any such requirements imposed by a relying party that consumes the assertion or by specific profiles are application-specific.");
		wrongIssuer.setExpectSuccess(Boolean.FALSE);
		wrongIssuer.getSamlResponseData().setIssuer("https://samlsnort.org/sp");
		testCases.add(wrongIssuer);

		// Not signed
		TestCase notSigned = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		notSigned.setName("Not Signed");
		notSigned
		.setDescription("An XML Signature that authenticates the responder and provides message integrity, as described below and in Section 5.");
		notSigned.setExpectSuccess(Boolean.FALSE);
		notSigned.getSamlResponseData().setSign(Boolean.FALSE);
		testCases.add(notSigned);
		
		// Wrong Certificate
		TestCase wrongCertificate = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		wrongCertificate.setName("Wrong Certificate");
		wrongCertificate.setDescription("");
		wrongCertificate.getSamlResponseData().setCertificateAlias("samlsnort");
		wrongCertificate.setExpectSuccess(Boolean.FALSE);
		testCases.add(wrongCertificate);

		// Wrong Subject Recipient
		TestCase wrongSubjectRecipient = new TestCase(
				(SamlResponseData) correct.getSamlResponseData().clone());
		wrongSubjectRecipient.setName("Wrong Subject Recipient");
		wrongSubjectRecipient.setDescription("Recipient [Optional] A URI specifying the entity or location to which an attesting entity can present the assertion. For example, this attribute might indicate that the assertion must be delivered to a particular network endpoint in order to prevent an intermediary from redirecting it someplace else.");
		wrongSubjectRecipient.getSamlResponseData().setSubjectRecipient(
				"https://samlsnort.org/sp");
		wrongSubjectRecipient.setExpectSuccess(Boolean.FALSE);
		testCases.add(wrongSubjectRecipient);

		// Wrong Subject InResponseTo
		TestCase wrongSubjectInResponseTo = new TestCase(
				(SamlResponseData) correct.getSamlResponseData().clone());
		wrongSubjectInResponseTo.setName("Wrong Subject InResponseTo");
		wrongSubjectInResponseTo.setDescription("InResponseTo [Optional] The ID of a SAML protocol message in response to which an attesting entity can present the assertion. For example, this attribute might be used to correlate the assertion to a SAML request that resulted in its presentation.");
		wrongSubjectInResponseTo.setExpectSuccess(Boolean.FALSE);
		wrongSubjectInResponseTo.getSamlResponseData().setInResponseTo(
				"s23c9e176eb9add30234d331d6371b9eb6ae73b234");
		testCases.add(wrongSubjectInResponseTo);

		// Correct Condition Times
		TestCase correctConditionTimes = new TestCase(
				(SamlResponseData) correct.getSamlResponseData().clone());
		correctConditionTimes.setName("Correct Condition Times");
		correctConditionTimes.setDescription("The NotBefore and NotOnOrAfter attributes specify time limits on the validity of the assertion within the context of its profile(s) of use. They do not guarantee that the statements in the assertion will be correct or accurate throughout the validity period. The NotBefore attribute specifies the time instant at which the validity interval begins. The NotOnOrAfter attribute specifies the time instant at which the validity interval has ended. If the value for either NotBefore or NotOnOrAfter is omitted, then it is considered unspecified. If the NotBefore attribute is unspecified (and if all other conditions that are supplied evaluate to Valid), then the assertion is Valid with respect to conditions at any time before the time instant specified by the NotOnOrAfter attribute. If the NotOnOrAfter attribute is unspecified (and if all other conditions that are supplied evaluate to Valid), the assertion is Valid with respect to conditions from the time instant specified by the NotBefore attribute with no expiry. If neither attribute is specified (and if any other conditions that are supplied evaluate to Valid), the assertion is Valid with respect to conditions at any time. If both attributes are present, the value for NotBefore MUST be less than (earlier than) the value for NotOnOrAfter.");
		correctConditionTimes.getSamlResponseData().setConditionNotBefore(0);
		correctConditionTimes.getSamlResponseData()
				.setConditionNotOnOrAfter(10);
		correctConditionTimes.setExpectSuccess(Boolean.TRUE);
		testCases.add(correctConditionTimes);

		// Wrong Condition Times
		TestCase wrongConditionTimes = new TestCase((SamlResponseData) correct
				.getSamlResponseData().clone());
		wrongConditionTimes.setName("Wrong Condition Times");
		wrongConditionTimes.setDescription("The NotBefore and NotOnOrAfter attributes specify time limits on the validity of the assertion within the context of its profile(s) of use. They do not guarantee that the statements in the assertion will be correct or accurate throughout the validity period. The NotBefore attribute specifies the time instant at which the validity interval begins. The NotOnOrAfter attribute specifies the time instant at which the validity interval has ended. If the value for either NotBefore or NotOnOrAfter is omitted, then it is considered unspecified. If the NotBefore attribute is unspecified (and if all other conditions that are supplied evaluate to Valid), then the assertion is Valid with respect to conditions at any time before the time instant specified by the NotOnOrAfter attribute. If the NotOnOrAfter attribute is unspecified (and if all other conditions that are supplied evaluate to Valid), the assertion is Valid with respect to conditions from the time instant specified by the NotBefore attribute with no expiry. If neither attribute is specified (and if any other conditions that are supplied evaluate to Valid), the assertion is Valid with respect to conditions at any time. If both attributes are present, the value for NotBefore MUST be less than (earlier than) the value for NotOnOrAfter.");
				wrongConditionTimes.getSamlResponseData().setConditionNotBefore(10);
		wrongConditionTimes.getSamlResponseData().setConditionNotOnOrAfter(5);
		wrongConditionTimes.setExpectSuccess(Boolean.FALSE);
		testCases.add(wrongConditionTimes);
		return testCases;
	}

}
