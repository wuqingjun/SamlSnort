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
package org.samlsnort.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.samlsnort.gui.actions.AddEmptyTestCaseAction;
import org.samlsnort.gui.actions.AddTestCaseAction;
import org.samlsnort.gui.actions.CreateMinimalTestCaseAction;
import org.samlsnort.gui.actions.CreateNewAliasAction;
import org.samlsnort.gui.actions.CreateOptionalTestCaseAction;
import org.samlsnort.gui.actions.CreateReportAction;
import org.samlsnort.gui.actions.CreateRequiredTestCaseAction;
import org.samlsnort.gui.actions.ExitAction;
import org.samlsnort.gui.actions.KeystoreInformationAction;
import org.samlsnort.gui.actions.OpenFileAction;
import org.samlsnort.gui.actions.RemoveTestCaseAction;
import org.samlsnort.gui.actions.RunTestSuiteAction;
import org.samlsnort.gui.actions.SaveAsAction;
import org.samlsnort.gui.panels.LoadPanel;
import org.samlsnort.gui.panels.TestSuitePanel;
import org.samlsnort.vo.TestSuite;

/**
 * @author Michael Gerber
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final ResourceBundle RESORUCE = ResourceBundle
			.getBundle(MainFrame.class.getName());
	private static final Logger LOGGER = Logger.getLogger(MainFrame.class
			.getName());

	private JPanel center;
	private TestSuitePanel testSuitePanel;
	private LoadPanel loadPanel;

	private final Action exitAction = new ExitAction(this);
	private final Action addTestCaseAction = new AddTestCaseAction(this);
	private final Action addEmptyTestCaseAction = new AddEmptyTestCaseAction(
			this);
	private final Action removeTestCaseAction = new RemoveTestCaseAction(this);
	private final Action runTestSuiteAciton = new RunTestSuiteAction(this);
	private final Action saveAsAction = new SaveAsAction(this);
	private final Action openFileAction = new OpenFileAction(this);
	private final Action createReportAction = new CreateReportAction(this);
	private final Action createMinimalTestCase = new CreateMinimalTestCaseAction(
			this);
	private final Action createRequiredTestCaseAction = new CreateRequiredTestCaseAction(this);
	private final Action createOptionalTestCaseAction = new CreateOptionalTestCaseAction(this);
	
	private final Action createNewAliasAction = new CreateNewAliasAction();
	private final Action keystoreInformationAction = new KeystoreInformationAction();
	

	/**
	 * Create the application.
	 */
	public MainFrame() {
		LOGGER.entering(MainFrame.class.getName(), MainFrame.class
				.getSimpleName());
		init();

		LOGGER.exiting(MainFrame.class.getName(), MainFrame.class
				.getSimpleName());

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void init() {
		setTitle(RESORUCE.getString("applicationName"));

		setIconImage(new ImageIcon(MainFrame.class.getResource("logo.png")).getImage());
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
		});

		initMenu();
		initToolbar();

		testSuitePanel = new TestSuitePanel();
		loadPanel = new LoadPanel();

		center = new JPanel(new BorderLayout());
		center.add(testSuitePanel);
		getContentPane().add(center, BorderLayout.CENTER);

		setMinimumSize(new Dimension(650, 700));
		setLocationRelativeTo(null);

	}

	private void initToolbar() {
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar.add(openFileAction);
		toolBar.add(saveAsAction);
		toolBar.add(createReportAction);
		toolBar.add(exitAction);
		toolBar.addSeparator(new Dimension(10, 40));
		toolBar.add(addEmptyTestCaseAction);
		toolBar.add(addTestCaseAction);
		toolBar.add(removeTestCaseAction);
		toolBar.addSeparator(new Dimension(10, 40));
		toolBar.add(runTestSuiteAciton);

	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		mnFile.add(openFileAction);
		mnFile.addSeparator();
		mnFile.add(saveAsAction);
		mnFile.add(createReportAction);
		mnFile.addSeparator();
		mnFile.add(exitAction);

		JMenu mnTest = new JMenu("Test Case");
		mnTest.setMnemonic('T');
		menuBar.add(mnTest);
		mnTest.add(addTestCaseAction);
		mnTest.add(addEmptyTestCaseAction);
		mnTest.addSeparator();
		JMenu testCaseCreator = new JMenu("Create Test Cases");
		testCaseCreator.add(createRequiredTestCaseAction);
		testCaseCreator.add(createOptionalTestCaseAction);
		testCaseCreator.add(createMinimalTestCase);
		mnTest.add(testCaseCreator);
		mnTest.addSeparator();
		mnTest.add(removeTestCaseAction);

		JMenu mnRun = new JMenu("Run");
		mnRun.setMnemonic('R');
		menuBar.add(mnRun);
		mnRun.add(runTestSuiteAciton);

		JMenu mnKs = new JMenu("Keystore");
		mnKs.setMnemonic('K');
		menuBar.add(mnKs);
		mnKs.add(createNewAliasAction);
		mnKs.add(keystoreInformationAction);
	}

	public TestSuitePanel getTestSuitePanel() {
		return testSuitePanel;
	}

	public void switchPanel(final TestSuite testSuite) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Component c = center.getComponent(0);
				center.removeAll();
				if (c == loadPanel) {
					enableActions(true);
					testSuitePanel = new TestSuitePanel(testSuite);
					center.add(testSuitePanel);
				} else {
					enableActions(false);
					loadPanel.clear();
					center.add(loadPanel);
				}
				center.updateUI();
				System.gc();
			}
		});

	}

	private void enableActions(boolean enabled) {
		exitAction.setEnabled(enabled);
		addTestCaseAction.setEnabled(enabled);
		addEmptyTestCaseAction.setEnabled(enabled);
		removeTestCaseAction.setEnabled(enabled);
		runTestSuiteAciton.setEnabled(enabled);
		saveAsAction.setEnabled(enabled);
		openFileAction.setEnabled(enabled);
		createReportAction.setEnabled(enabled);
		createMinimalTestCase.setEnabled(enabled);
		createOptionalTestCaseAction.setEnabled(enabled);
		createRequiredTestCaseAction.setEnabled(enabled);
		createNewAliasAction.setEnabled(enabled);
		keystoreInformationAction.setEnabled(enabled);
	}

	public LoadPanel getLoadPanel() {
		return loadPanel;
	}
}
