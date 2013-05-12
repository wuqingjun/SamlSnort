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
package org.samlsnort.gui.actions;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import org.samlsnort.gui.MainFrame;
import org.samlsnort.util.TestCaseUtil;
import org.samlsnort.vo.TestSuite;

public class CreateOptionalTestCaseAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private MainFrame mainFrame;
	private static final Logger LOGGER = Logger
			.getLogger(CreateReportAction.class.getName());

	public CreateOptionalTestCaseAction(MainFrame mainFrame) {
		putValue(NAME, "Create Optional Test Case");
		putValue(SHORT_DESCRIPTION, "Create Optional Test Case...");
		this.mainFrame = mainFrame;
	}

	public void actionPerformed(ActionEvent e) {
		LOGGER.entering(CreateOptionalTestCaseAction.class.getName(),
				"actionPerformed", e);

		try {
			TestSuite testSuite = mainFrame.getTestSuitePanel().getTestSuite();
			testSuite.getTestCases().addAll(
					TestCaseUtil.createOptionalTestCases(testSuite
							.getTestCases().get(0)));
			mainFrame.getTestSuitePanel().setTestSuite(testSuite);
		} catch (CloneNotSupportedException e1) {
			LOGGER.throwing(CreateOptionalTestCaseAction.class.getName(),
					"actionPerformed", e1);
		}

		LOGGER.exiting(CreateOptionalTestCaseAction.class.getName(),
				"actionPerformed");
	}
}
