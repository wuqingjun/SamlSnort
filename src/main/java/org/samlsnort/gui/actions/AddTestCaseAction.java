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
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.samlsnort.gui.MainFrame;

public class AddTestCaseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private static final Logger LOGGER = Logger.getLogger(AddTestCaseAction.class
				.getName());

		private MainFrame mainFrame;
		
		public AddTestCaseAction(MainFrame mainFrame) {
			putValue(NAME, "Add Test Case");
			putValue(SHORT_DESCRIPTION, "Add a copy of the selected Test Case");
			putValue(MNEMONIC_KEY, (int) 'a');
			putValue(SMALL_ICON, new ImageIcon(AddTestCaseAction.class
					.getResource("tab_duplicate_small.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(AddTestCaseAction.class
					.getResource("tab_duplicate.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T,
					Event.CTRL_MASK));
			this.mainFrame = mainFrame;
		}

		public void actionPerformed(ActionEvent e) {
			LOGGER.exiting(AddTestCaseAction.class.getName(), "actionPerformed");
			mainFrame.getTestSuitePanel().addTestCase();
			LOGGER
				.entering(AddTestCaseAction.class.getName(), "actionPerformed",
						e);
		}

	}
