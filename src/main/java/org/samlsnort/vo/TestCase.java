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
import java.net.URL;

import org.joda.time.DateTime;

public class TestCase implements Serializable {
	private static final long serialVersionUID = 1L;
	private DateTime startTime;
	private DateTime endTime;
	private SamlResponseData samlResponseData;
	private String samlResponse;
	private String samlAuthnRequest;
	private transient URL resultURL;
	private String resultPageText;
	private transient String resultPageHTML;
	private Boolean testSuccessful;
	private Boolean base64Valid;
	private Boolean deflatedValid;
	private String name;
	private String description;
	private Boolean expectSuccess;

	/**
	 * @return the resultURL
	 */
	public URL getResultURL() {
		return resultURL;
	}

	/**
	 * @param resultURL the resultURL to set
	 */
	public void setResultURL(URL resultURL) {
		this.resultURL = resultURL;
	}

	/**
	 * @return the startTime
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the samlResponseData
	 */
	public SamlResponseData getSamlResponseData() {
		return samlResponseData;
	}

	/**
	 * @param samlResponseData
	 *            the samlResponseData to set
	 */
	public void setSamlResponseData(SamlResponseData samlResponseData) {
		this.samlResponseData = samlResponseData;
	}

	/**
	 * @return the samlResponse
	 */
	public String getSamlResponse() {
		return samlResponse;
	}

	/**
	 * @param samlResponse
	 *            the samlResponse to set
	 */
	public void setSamlResponse(String samlResponse) {
		this.samlResponse = samlResponse;
	}

	/**
	 * @return the samlAuthnRequest
	 */
	public String getSamlAuthnRequest() {
		return samlAuthnRequest;
	}

	/**
	 * @param samlAuthnRequest
	 *            the samlAuthnRequest to set
	 */
	public void setSamlAuthnRequest(String samlAuthnRequest) {
		this.samlAuthnRequest = samlAuthnRequest;
	}

	/**
	 * @return the resultPageText
	 */
	public String getResultPageText() {
		return resultPageText;
	}

	/**
	 * @param resultPageText
	 *            the resultPageText to set
	 */
	public void setResultPageText(String resultPageText) {
		this.resultPageText = resultPageText;
	}

	/**
	 * @return the testSuccessful
	 */
	public Boolean getTestSuccessful() {
		return testSuccessful;
	}

	/**
	 * @param testSuccessful
	 *            the testSuccessful to set
	 */
	public void setTestSuccessful(Boolean testSuccessful) {
		this.testSuccessful = testSuccessful;
	}

	/**
	 * @return the base64Valid
	 */
	public Boolean getBase64Valid() {
		return base64Valid;
	}

	/**
	 * @param base64Valid
	 *            the base64Valid to set
	 */
	public void setBase64Valid(Boolean base64Valid) {
		this.base64Valid = base64Valid;
	}

	/**
	 * @return the deflatedValid
	 */
	public Boolean getDeflatedValid() {
		return deflatedValid;
	}

	/**
	 * @param deflatedValid
	 *            the deflatedValid to set
	 */
	public void setDeflatedValid(Boolean deflatedValid) {
		this.deflatedValid = deflatedValid;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the expectSuccess
	 */
	public Boolean getExpectSuccess() {
		return expectSuccess;
	}

	/**
	 * @param expectSuccess
	 *            the expectSuccess to set
	 */
	public void setExpectSuccess(Boolean expectSuccess) {
		this.expectSuccess = expectSuccess;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @param samlResponseData
	 */
	public TestCase(SamlResponseData samlResponseData) {
		super();
		this.samlResponseData = samlResponseData;
	}

	public TestCase(String name) {
		this.name = name;
	}

	public TestCase(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the resultPageHTML
	 */
	public String getResultPageHTML() {
		return resultPageHTML;
	}

	/**
	 * @param resultPageHTML
	 *            the resultPageHTML to set
	 */
	public void setResultPageHTML(String resultPageHTML) {
		this.resultPageHTML = resultPageHTML;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "TestCase ( "
	        + super.toString() + TAB
	        + "samlResponseData = " + this.samlResponseData + TAB
	        + "testSuccessful = " + this.testSuccessful + TAB
	        + "name = " + this.name + TAB
	        + " )";
	
	    return retValue;
	}

	
}
