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

/**
 * @author Michael Gerber
 *
 */
public class AddEmptyTestCaseAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private static final Logger LOGGER = Logger
			.getLogger(AddEmptyTestCaseAction.class.getName());

	public AddEmptyTestCaseAction(MainFrame mainFrame) {
		putValue(NAME, "Add Empty Test Case");
		putValue(SHORT_DESCRIPTION, "Add a empty Test Case");
		putValue(MNEMONIC_KEY, (int) 'n');
		putValue(LARGE_ICON_KEY, new ImageIcon(AddEmptyTestCaseAction.class
				.getResource("tab_new_raised.png")));
		putValue(SMALL_ICON, new ImageIcon(AddEmptyTestCaseAction.class
				.getResource("tab_new_raised_small.png")));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Event.CTRL_MASK));
		this.mainFrame = mainFrame;
	}

	public void actionPerformed(ActionEvent e) {
		LOGGER.entering(AddEmptyTestCaseAction.class.getName(),
				"actionPerformed", e);

		mainFrame.getTestSuitePanel().addEmptyTestCase();

		LOGGER.exiting(AddEmptyTestCaseAction.class.getName(),
				"actionPerformed");
	}

}
