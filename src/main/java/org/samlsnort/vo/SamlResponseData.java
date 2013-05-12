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
package org.samlsnort.vo;

import java.io.Serializable;
import java.util.List;

public class SamlResponseData implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	public final static String REPLACE_WITH_AUTHN_REQUEST = "$AuthnRequest";

	private String destination;
	private String inResponseTo;
	private Integer issueInstant;
	private String issuer;
	private String statusCodeValue;

	private String assertionIssuer;

	private Boolean sign;
	private String canocializationMethodAlgorithm;
	private String signatureMethodAlgorithm;
	private String certificateAlias;

	private String subjectFormat;
	private String subjectValue;
	private Integer subjectNotOnOrAfter;
	private Integer subjectNotBefore;
	private String subjectRecipient;
	private String subjectInResponseTo;

	private Integer conditionNotBefore;
	private Integer conditionNotOnOrAfter;
	private String audience;

	private Integer authnInstant;
	private String authnContextClassRef;
	private String subjectLocalityAddress;
	private String subjectLocalityDNSName;

	private List<AttributeData> attributes;

	
	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the inResponseTo
	 */
	public String getInResponseTo() {
		return inResponseTo;
	}

	/**
	 * @param inResponseTo the inResponseTo to set
	 */
	public void setInResponseTo(String inResponseTo) {
		this.inResponseTo = inResponseTo;
	}

	/**
	 * @return the issueInstant
	 */
	public Integer getIssueInstant() {
		return issueInstant;
	}

	/**
	 * @param issueInstant the issueInstant to set
	 */
	public void setIssueInstant(Integer issueInstant) {
		this.issueInstant = issueInstant;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * @return the statusCodeValue
	 */
	public String getStatusCodeValue() {
		return statusCodeValue;
	}

	/**
	 * @param statusCodeValue the statusCodeValue to set
	 */
	public void setStatusCodeValue(String statusCodeValue) {
		this.statusCodeValue = statusCodeValue;
	}

	/**
	 * @return the assertionIssuer
	 */
	public String getAssertionIssuer() {
		return assertionIssuer;
	}

	/**
	 * @param assertionIssuer the assertionIssuer to set
	 */
	public void setAssertionIssuer(String assertionIssuer) {
		this.assertionIssuer = assertionIssuer;
	}

	/**
	 * @return the sign
	 */
	public Boolean getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(Boolean sign) {
		this.sign = sign;
	}

	/**
	 * @return the canocializationMethodAlgorithm
	 */
	public String getCanocializationMethodAlgorithm() {
		return canocializationMethodAlgorithm;
	}

	/**
	 * @param canocializationMethodAlgorithm the canocializationMethodAlgorithm to set
	 */
	public void setCanocializationMethodAlgorithm(
			String canocializationMethodAlgorithm) {
		this.canocializationMethodAlgorithm = canocializationMethodAlgorithm;
	}

	/**
	 * @return the signatureMethodAlgorithm
	 */
	public String getSignatureMethodAlgorithm() {
		return signatureMethodAlgorithm;
	}

	/**
	 * @param signatureMethodAlgorithm the signatureMethodAlgorithm to set
	 */
	public void setSignatureMethodAlgorithm(String signatureMethodAlgorithm) {
		this.signatureMethodAlgorithm = signatureMethodAlgorithm;
	}

	/**
	 * @return the certificateAlias
	 */
	public String getCertificateAlias() {
		return certificateAlias;
	}

	/**
	 * @param certificateAlias the certificateAlias to set
	 */
	public void setCertificateAlias(String certificateAlias) {
		this.certificateAlias = certificateAlias;
	}

	/**
	 * @return the subjectFormat
	 */
	public String getSubjectFormat() {
		return subjectFormat;
	}

	/**
	 * @param subjectFormat the subjectFormat to set
	 */
	public void setSubjectFormat(String subjectFormat) {
		this.subjectFormat = subjectFormat;
	}

	/**
	 * @return the subjectValue
	 */
	public String getSubjectValue() {
		return subjectValue;
	}

	/**
	 * @param subjectValue the subjectValue to set
	 */
	public void setSubjectValue(String subjectValue) {
		this.subjectValue = subjectValue;
	}

	/**
	 * @return the subjectNotOnOrAfter
	 */
	public Integer getSubjectNotOnOrAfter() {
		return subjectNotOnOrAfter;
	}

	/**
	 * @param subjectNotOnOrAfter the subjectNotOnOrAfter to set
	 */
	public void setSubjectNotOnOrAfter(Integer subjectNotOnOrAfter) {
		this.subjectNotOnOrAfter = subjectNotOnOrAfter;
	}

	/**
	 * @return the subjectNotBefore
	 */
	public Integer getSubjectNotBefore() {
		return subjectNotBefore;
	}

	/**
	 * @param subjectNotBefore the subjectNotBefore to set
	 */
	public void setSubjectNotBefore(Integer subjectNotBefore) {
		this.subjectNotBefore = subjectNotBefore;
	}

	/**
	 * @return the subjectRecipient
	 */
	public String getSubjectRecipient() {
		return subjectRecipient;
	}

	/**
	 * @param subjectRecipient the subjectRecipient to set
	 */
	public void setSubjectRecipient(String subjectRecipient) {
		this.subjectRecipient = subjectRecipient;
	}

	/**
	 * @return the subjectInResponseTo
	 */
	public String getSubjectInResponseTo() {
		return subjectInResponseTo;
	}

	/**
	 * @param subjectInResponseTo the subjectInResponseTo to set
	 */
	public void setSubjectInResponseTo(String subjectInResponseTo) {
		this.subjectInResponseTo = subjectInResponseTo;
	}

	/**
	 * @return the conditionNotBefore
	 */
	public Integer getConditionNotBefore() {
		return conditionNotBefore;
	}

	/**
	 * @param conditionNotBefore the conditionNotBefore to set
	 */
	public void setConditionNotBefore(Integer conditionNotBefore) {
		this.conditionNotBefore = conditionNotBefore;
	}

	/**
	 * @return the conditionNotOnOrAfter
	 */
	public Integer getConditionNotOnOrAfter() {
		return conditionNotOnOrAfter;
	}

	/**
	 * @param conditionNotOnOrAfter the conditionNotOnOrAfter to set
	 */
	public void setConditionNotOnOrAfter(Integer conditionNotOnOrAfter) {
		this.conditionNotOnOrAfter = conditionNotOnOrAfter;
	}

	/**
	 * @return the audience
	 */
	public String getAudience() {
		return audience;
	}

	/**
	 * @param audience the audience to set
	 */
	public void setAudience(String audience) {
		this.audience = audience;
	}

	/**
	 * @return the authnInstant
	 */
	public Integer getAuthnInstant() {
		return authnInstant;
	}

	/**
	 * @param authnInstant the authnInstant to set
	 */
	public void setAuthnInstant(Integer authnInstant) {
		this.authnInstant = authnInstant;
	}

	/**
	 * @return the authnContextClassRef
	 */
	public String getAuthnContextClassRef() {
		return authnContextClassRef;
	}

	/**
	 * @param authnContextClassRef the authnContextClassRef to set
	 */
	public void setAuthnContextClassRef(String authnContextClassRef) {
		this.authnContextClassRef = authnContextClassRef;
	}

	/**
	 * @return the subjectLocalityAddress
	 */
	public String getSubjectLocalityAddress() {
		return subjectLocalityAddress;
	}

	/**
	 * @param subjectLocalityAddress the subjectLocalityAddress to set
	 */
	public void setSubjectLocalityAddress(String subjectLocalityAddress) {
		this.subjectLocalityAddress = subjectLocalityAddress;
	}

	/**
	 * @return the subjectLocalityDNSName
	 */
	public String getSubjectLocalityDNSName() {
		return subjectLocalityDNSName;
	}

	/**
	 * @param subjectLocalityDNSName the subjectLocalityDNSName to set
	 */
	public void setSubjectLocalityDNSName(String subjectLocalityDNSName) {
		this.subjectLocalityDNSName = subjectLocalityDNSName;
	}

	/**
	 * @return the attributes
	 */
	public List<AttributeData> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<AttributeData> attributes) {
		this.attributes = attributes;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value
	 * format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() {
		final String TAB = "    ";

		String retValue = "";

		retValue = "SamlResponseData ( " + super.toString() + TAB
				+ "certificateAlias = " + this.certificateAlias + TAB
				+ "destination = " + this.destination + TAB + "inResponseTo = "
				+ this.inResponseTo + TAB + "issueInstant = "
				+ this.issueInstant + TAB + "issuer = " + this.issuer + TAB
				+ "statusCodeValue = " + this.statusCodeValue + TAB
				+ "canocializationMethodAlgorithm = "
				+ this.canocializationMethodAlgorithm + TAB
				+ "signatureMethodAlgorithm = " + this.signatureMethodAlgorithm
				+ TAB + "sign = " + this.sign + TAB + "subjectFormat = "
				+ this.subjectFormat + TAB + "subjectValue = "
				+ this.subjectValue + TAB + "subjectNotOnOrAfter = "
				+ this.subjectNotOnOrAfter + TAB + "subjectNotBefore = "
				+ this.subjectNotBefore + TAB + "subjectRecipient = "
				+ this.subjectRecipient + TAB + "subjectInResponseTo = "
				+ this.subjectInResponseTo + TAB + "conditionNotBefore = "
				+ this.conditionNotBefore + TAB + "conditionNotOnOrAfter = "
				+ this.conditionNotOnOrAfter + TAB + "audience = "
				+ this.audience + TAB + "authnInstant = " + this.authnInstant
				+ TAB + "authnContextClassRef = " + this.authnContextClassRef
				+ TAB + "subjectLocalityAddress = "
				+ this.subjectLocalityAddress + TAB
				+ "subjectLocalityDNSName = " + this.subjectLocalityDNSName
				+ TAB + "attributes = " + this.attributes + TAB + " )";

		return retValue;
	}

}
