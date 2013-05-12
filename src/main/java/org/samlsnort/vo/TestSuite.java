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

import org.joda.time.DateTime;

public class TestSuite implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<TestCase> testCases;
	private String serviceProviderURL;
	private String host;
	private DateTime startTime;
	private DateTime endTime;
	private TestMode testMode;

	public TestSuite(String serviceProviderURL, TestMode testMode) {
		this.serviceProviderURL=serviceProviderURL;
		this.testMode = testMode;
	}

	/**
	 * @return the testCases
	 */
	public List<TestCase> getTestCases() {
		return testCases;
	}

	/**
	 * @param testCases
	 *            the testCases to set
	 */
	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	/**
	 * @return the serviceProviderURL
	 */
	public String getServiceProviderURL() {
		return serviceProviderURL;
	}

	/**
	 * @param serviceProviderURL
	 *            the serviceProviderURL to set
	 */
	public void setServiceProviderURL(String serviceProviderURL) {
		this.serviceProviderURL = serviceProviderURL;
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
	 * @return the testMode
	 */
	public TestMode getTestMode() {
		return testMode;
	}

	/**
	 * @param testMode
	 *            the testMode to set
	 */
	public void setTestMode(TestMode testMode) {
		this.testMode = testMode;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
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
	    
	    retValue = "TestSuite ( "
	        + super.toString() + TAB
	        + "testCases = " + this.testCases + TAB
	        + "serviceProviderURL = " + this.serviceProviderURL + TAB
	        + "host = " + this.host + TAB
	        + "startTime = " + this.startTime + TAB
	        + "endTime = " + this.endTime + TAB
	        + "testMode = " + this.testMode + TAB
	        + " )";
	
	    return retValue;
	}

}
