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
import java.io.FileOutputStream;
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

import com.thoughtworks.xstream.XStream;

public class SaveAsAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private static final Logger LOGGER = Logger.getLogger(SaveAsAction.class
				.getName());

		private static final ResourceBundle RESORUCE = ResourceBundle
				.getBundle(SaveAsAction.class.getName());

		private MainFrame mainFrame;

		public SaveAsAction(MainFrame mainFrame) {
			putValue(NAME, "Save As...");
			putValue(MNEMONIC_KEY, (int) 'A');
			putValue(SMALL_ICON, new ImageIcon(SaveAsAction.class
					.getResource("filesaveas_small.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(SaveAsAction.class
					.getResource("filesaveas.png")));
			putValue(SHORT_DESCRIPTION, "Save As...");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK));
			this.mainFrame = mainFrame;
		}

		public void actionPerformed(ActionEvent e) {
			LOGGER.entering(SaveAsAction.class.getName(), "actionPerformed", e);
			JFileChooser chooser = new JFileChooser(new File("")
					.getAbsoluteFile());
			chooser.setFileFilter(new ExtensionFileFilter(RESORUCE.getString("description"),RESORUCE.getString("extension")));
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getName().endsWith("."+RESORUCE.getString("extension")))
					file = new File(file.getAbsolutePath() + "."+RESORUCE.getString("extension"));

				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					new XStream().toXML(mainFrame.getTestSuitePanel().getTestSuite(), fos);
					JOptionPane.showMessageDialog(null, "Successful saved as '"
							+ file.getName() + "'.", "Save Successful",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exception) {
					LOGGER.log(Level.WARNING, "Save failed:", exception);
					JOptionPane.showMessageDialog(null,
							"Save failed, please try again.", "Save Failed",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					try {
						if (fos != null)
							fos.close();
					} catch (Exception exception) {
						LOGGER.log(Level.WARNING, "Save failed:", exception);
					}
				}
			}

			LOGGER.exiting(SaveAsAction.class.getName(), "actionPerformed");
		}

	}
