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
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.samlsnort.gui.MainFrame;
import org.samlsnort.util.Configuration;

public class ExitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private MainFrame mainFrame;
		private static final Logger LOGGER = Logger.getLogger(ExitAction.class
				.getName());


		public ExitAction(MainFrame mainFrame) {
			putValue(NAME, "Exit");
			putValue(MNEMONIC_KEY, (int) 'x');
			putValue(SMALL_ICON, new ImageIcon(ExitAction.class
					.getResource("exit_small.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(ExitAction.class
					.getResource("exit.png")));
			putValue(SHORT_DESCRIPTION, "Exit");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E,
					Event.CTRL_MASK));
			this.mainFrame = mainFrame;
		}

		public void actionPerformed(ActionEvent e) {
			LOGGER.entering(ExitAction.class.getName(), "actionPerformed",e);
			int returnValue = JOptionPane.showConfirmDialog(null,
					"Do you want to save the changes?");
			if (returnValue == JOptionPane.YES_OPTION) {
				new SaveAsAction(mainFrame).actionPerformed(e);
				LOGGER.info("Exit Application");
				Configuration.getInstance().exit();
			} else if (returnValue == JOptionPane.NO_OPTION) {
				LOGGER.info("Exit Application");
				Configuration.getInstance().exit();
			}
			LOGGER.exiting(ExitAction.class.getName(), "actionPerformed");
		}
	}
