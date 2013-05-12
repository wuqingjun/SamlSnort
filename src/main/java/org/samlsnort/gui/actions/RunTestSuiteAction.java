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

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.samlsnort.gui.MainFrame;
import org.samlsnort.logic.TestRunner;
import org.samlsnort.vo.TestMode;
import org.samlsnort.vo.TestSuite;

public class RunTestSuiteAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger
			.getLogger(RunTestSuiteAction.class.getName());

	private MainFrame mainFrame;

	public RunTestSuiteAction(MainFrame mainFrame) {
		putValue(NAME, "Run Test Suite");
		putValue(SHORT_DESCRIPTION, "Run Test Suite");
		putValue(MNEMONIC_KEY, (int) 'u');
		putValue(SMALL_ICON, new ImageIcon(RunTestSuiteAction.class
				.getResource("run_small.png")));
		putValue(LARGE_ICON_KEY, new ImageIcon(RunTestSuiteAction.class
				.getResource("run.png")));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R,
				Event.CTRL_MASK));
		this.mainFrame = mainFrame;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		LOGGER.entering(RunTestSuiteAction.class.getName(), "actionPerformed",
				actionEvent);

		final TestSuite testSuite = mainFrame.getTestSuitePanel()
				.getTestSuite();
		if (testSuite.getServiceProviderURL() == null
				|| testSuite.getServiceProviderURL().length() == 0) {
			JOptionPane.showMessageDialog(null,
					"The Service Provider URL is required to run a test.");
		} else if ((testSuite.getTestMode().equals(TestMode.SP_HTTP_POST) || testSuite
				.getTestMode().equals(TestMode.SP_HTTP_REDIRECT))
				&& (testSuite.getHost() == null || testSuite.getHost().length() == 0)) {
			JOptionPane.showMessageDialog(null,
					"The Host is required to run a test.");
		} else {
			new Thread() {
				@Override
				public void run() {
					try {
						mainFrame.switchPanel(null);
						TestRunner testRunner = new TestRunner(mainFrame
								.getLoadPanel());
						TestSuite temp = testRunner.runTests(testSuite);
						mainFrame.switchPanel(temp);
					} catch (Exception e1) {
						LOGGER.log(Level.WARNING, "Error while running tests:",
								e1);
						mainFrame.switchPanel(testSuite);
						JOptionPane.showMessageDialog(null,"Error while running tests","Error while running tests",JOptionPane.ERROR_MESSAGE);
					}
				}
			}.start();
		}

		LOGGER.exiting(RunTestSuiteAction.class.getName(), "actionPerformed");

	}
}
