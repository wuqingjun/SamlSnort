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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.jfree.ui.ExtensionFileFilter;
import org.samlsnort.gui.MainFrame;
import org.samlsnort.vo.TestSuite;

import com.thoughtworks.xstream.XStream;

public class OpenFileAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private static final Logger LOGGER = Logger.getLogger(OpenFileAction.class
				.getName());
		private MainFrame mainFrame;

		private static final ResourceBundle RESORUCE = ResourceBundle
		.getBundle(OpenFileAction.class.getName());
		
		public OpenFileAction(MainFrame mainFrame) {
			putValue(NAME, "Open File...");
			putValue(MNEMONIC_KEY, (int) 'O');
			putValue(SMALL_ICON, new ImageIcon(OpenFileAction.class
					.getResource("fileopen_small.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(OpenFileAction.class
					.getResource("fileopen.png")));
			putValue(SHORT_DESCRIPTION, "Open File...");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
					Event.CTRL_MASK));
			this.mainFrame = mainFrame;

		}

		public void actionPerformed(ActionEvent e) {
			LOGGER.entering(OpenFileAction.class.getName(), "actionPerformed",
					e);
			JFileChooser chooser = new JFileChooser(new File("")
					.getAbsoluteFile());
			chooser.setFileFilter(new ExtensionFileFilter(
					RESORUCE.getString("description"), RESORUCE.getString("extension")));
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();

				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					TestSuite testSuite = (TestSuite) new XStream()
							.fromXML(fis);

					mainFrame.getTestSuitePanel().setTestSuite(testSuite);

				} catch (Exception exception) {
					LOGGER.log(Level.WARNING, "Open failed:", exception);
					JOptionPane.showMessageDialog(null,
							"Open failed, please try again.", "Save Failed",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					try {
						if (fis != null)
							fis.close();
					} catch (IOException exception) {
						LOGGER.log(Level.WARNING, "Open failed:", exception);
					}
				}
			}
			LOGGER.exiting(OpenFileAction.class.getName(), "actionPerformed");
		}
	}
