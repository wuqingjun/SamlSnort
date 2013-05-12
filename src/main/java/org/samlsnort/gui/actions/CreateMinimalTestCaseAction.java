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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.samlsnort.gui.MainFrame;
import org.samlsnort.logic.TestRunner;
import org.samlsnort.util.TestCaseUtil;
import org.samlsnort.vo.TestCase;
import org.samlsnort.vo.TestSuite;

public class CreateMinimalTestCaseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private MainFrame mainFrame;
		private static final Logger LOGGER = Logger
		.getLogger(CreateReportAction.class.getName());
	
		public CreateMinimalTestCaseAction(MainFrame mainFrame) {
			putValue(NAME, "Create Minimal Test Case...");
			putValue(SHORT_DESCRIPTION, "Create Minimal Test Case...");
			this.mainFrame = mainFrame;
		}

		public void actionPerformed(ActionEvent e) {
			LOGGER.entering(CreateMinimalTestCaseAction.class.getName(),
					"actionPerformed", e);

			if (mainFrame.getTestSuitePanel().getTestSuite().getServiceProviderURL() == null
					|| mainFrame.getTestSuitePanel().getTestSuite().getServiceProviderURL()
							.length() == 0) {
				JOptionPane.showMessageDialog(null,
						"The Service Provider URL is required to run a test.");
			} else {
				new Thread() {
					@Override
					public void run() {

						try {
							mainFrame.switchPanel(null);
							
							TestSuite temp = mainFrame.getTestSuitePanel().getTestSuite();

							TestSuite newTestSuite = new TestSuite(temp
									.getServiceProviderURL(), temp
									.getTestMode());
							newTestSuite.setHost(temp.getHost());
							newTestSuite.setTestCases(TestCaseUtil
									.createVariations(temp.getTestCases()
											.get(0)));
							newTestSuite.getTestCases().add(0,
									temp.getTestCases().get(0));

							
							TestRunner testRunner = new TestRunner(mainFrame.getLoadPanel());
							
							List<TestCase> result = 
								testRunner.runTests(newTestSuite).getTestCases();

							TestCase correct = result.remove(0);
							temp.getTestCases().add(
									TestCaseUtil.createMinimalTestCase(correct,
											result));
							mainFrame.switchPanel(temp);
						} catch (Exception e) {
							LOGGER.log(Level.WARNING, "Create minimal test case failed:", e);
						}
					}
				}.start();
			}

			LOGGER.exiting(CreateMinimalTestCaseAction.class.getName(),
					"actionPerformed");
		}
	}
