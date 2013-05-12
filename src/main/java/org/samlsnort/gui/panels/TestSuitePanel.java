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
package org.samlsnort.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.samlsnort.gui.listener.TestCaseNameListener;
import org.samlsnort.vo.SamlResponseData;
import org.samlsnort.vo.TestCase;
import org.samlsnort.vo.TestMode;
import org.samlsnort.vo.TestSuite;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TestSuitePanel extends JPanel {
	private static final Logger LOGGER = Logger.getLogger(TestSuitePanel.class
			.getName());

	private static final long serialVersionUID = 1L;
	private JTextField serviceProviderURLTextField;
	private JComboBox modeComboBox;
	private JTabbedPane testCasesTabbedPane;
	private TestCasePanel correctTestCasePanel;
	private TestSuite testSuite;
	private TestCaseNameListener testCaseNameListener;
	private JTextField hostTextField;

	public TestSuitePanel() {
		init();
	}

	public TestSuitePanel(TestSuite t) {
		init();
		setTestSuite(t);
	}

	private void init() {
		setLayout(new BorderLayout());

		testCaseNameListener = new TestCaseNameListener() {
			@Override
			public void nameChanged(String newName, TestCasePanel panel) {
				testCasesTabbedPane.setTitleAt(testCasesTabbedPane
						.indexOfComponent(panel), newName);
			}
		};

		JPanel top = new JPanel();
		top
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.GLUE_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(203dlu;default)"), },
						new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC,
								FormFactory.RELATED_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC,
								FormFactory.RELATED_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC,
								FormFactory.RELATED_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblServiceProviderUrl = new JLabel("Service Provider URL");
		top.add(lblServiceProviderUrl, "2, 2, left, default");

		serviceProviderURLTextField = new JTextField();
		top.add(serviceProviderURLTextField, "4, 2, 3, 1, fill, default");
		serviceProviderURLTextField.setColumns(10);

		JLabel lblMode = new JLabel("Mode");
		top.add(lblMode, "2, 4, left, default");

		modeComboBox = new JComboBox();
		modeComboBox.setModel(new DefaultComboBoxModel(TestMode.values()));
		modeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (modeComboBox.getSelectedItem().equals(
						TestMode.SP_HTTP_REDIRECT)
						|| modeComboBox.getSelectedItem().equals(
								TestMode.SP_HTTP_POST))
					hostTextField.setEnabled(true);
				else
					hostTextField.setEnabled(false);
			}
		});
		top.add(modeComboBox, "4, 4, 3, 1, fill, default");

		add(top, BorderLayout.NORTH);

		JLabel lblHost = new JLabel("Host");
		top.add(lblHost, "2, 6, left, default");

		hostTextField = new JTextField();
		top.add(hostTextField, "4, 6, 3, 1, fill, default");
		modeComboBox.setSelectedIndex(0);

		testCasesTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(testCasesTabbedPane, BorderLayout.CENTER);

		SamlResponseData wrongData = new SamlResponseData();
		wrongData.setSign(false);
		correctTestCasePanel = new TestCasePanel(new TestCase(
				"Correct Test Case", "Correct Test Case Description"), false);
		testCasesTabbedPane.addTab("Correct Test Case", new ImageIcon(
				TestSuitePanel.class.getResource("ledblue.png")),
				correctTestCasePanel, null);

	}

	public TestSuite getTestSuite() {
		LOGGER.entering(TestSuitePanel.class.getName(), "getTestSuite");

		if (testSuite == null) {
			testSuite = new TestSuite(serviceProviderURLTextField.getText(),
					(TestMode) modeComboBox.getSelectedItem());
		} else {
			testSuite.setServiceProviderURL(serviceProviderURLTextField
					.getText());
			testSuite.setTestMode((TestMode) modeComboBox.getSelectedItem());
		}

		if (hostTextField.getText() != null
				&& hostTextField.getText().length() > 0) {
			testSuite.setHost(hostTextField.getText());
		}

		testSuite.setTestCases(getTestCases());

		LOGGER.exiting(TestSuitePanel.class.getName(), "getTestSuite",
				testSuite);
		return testSuite;
	}

	public void setTestSuite(TestSuite t) {
		LOGGER.entering(TestSuitePanel.class.getName(), "setTestSuite", t);
		this.testSuite = t;

		serviceProviderURLTextField.setText(testSuite.getServiceProviderURL());
		modeComboBox.setSelectedItem(testSuite.getTestMode());
		hostTextField.setText(testSuite.getHost());

		List<TestCase> testCases = testSuite.getTestCases();

		correctTestCasePanel.setTestCase(testCases.get(0));

		for (int i = 1; i < testSuite.getTestCases().size(); i++) {
			if (testCasesTabbedPane.getTabCount() == i) {
				testCasesTabbedPane.addTab(testCases.get(i).getName(),
						new TestCasePanel(testSuite.getTestCases().get(i),
								testCaseNameListener));
			} else {
				((TestCasePanel) testCasesTabbedPane.getComponentAt(i))
						.setTestCase(testCases.get(i));
			}
		}

		for (int i = testCasesTabbedPane.getTabCount(); i > testCases.size(); i--) {
			testCasesTabbedPane.remove(i - 1);
		}

		for (int i = 0; i < testCases.size(); i++) {

			if (testCases.get(i).getTestSuccessful() != null
					&& testCases.get(i).getTestSuccessful() == true) {
				testCasesTabbedPane.setIconAt(i, new ImageIcon(
						TestSuitePanel.class.getResource("ledgreen.png")));
			} else if (testCases.get(i).getTestSuccessful() != null
					&& testCases.get(i).getTestSuccessful() == false) {
				testCasesTabbedPane.setIconAt(i, new ImageIcon(
						TestSuitePanel.class.getResource("ledred.png")));
			} else {
				testCasesTabbedPane.setIconAt(i, new ImageIcon(
						TestSuitePanel.class.getResource("ledblue.png")));
			}

			if (testCases.get(i).getTestSuccessful() != null
					&& testCases.get(i).getExpectSuccess() != null
					&& testCases.get(i).getExpectSuccess().booleanValue() != testCases
							.get(i).getTestSuccessful().booleanValue()) {
				testCasesTabbedPane
						.setBackgroundAt(i, new Color(255, 236, 213));
			} else {
				testCasesTabbedPane.setBackgroundAt(i, testCasesTabbedPane
						.getBackground());
			}
		}

		LOGGER.exiting(TestSuitePanel.class.getName(), "setTestSuite");
	}

	/**
	 * Create a copy of the selected test case.
	 */
	public void addTestCase() {
		try {
			TestCase old = ((TestCasePanel) testCasesTabbedPane
					.getSelectedComponent()).getTestCase();
			TestCase copy = new TestCase((SamlResponseData) old
					.getSamlResponseData().clone());
			copy.setName(old.getName() + " copy");
			testCasesTabbedPane.addTab(copy.getName(), new ImageIcon(
					TestSuitePanel.class.getResource("ledblue.png")),
					new TestCasePanel(copy, testCaseNameListener));
		} catch (CloneNotSupportedException e) {
		}
	}

	public void addEmptyTestCase() {
		testCasesTabbedPane.addTab("Empty Test Case", new ImageIcon(
				TestSuitePanel.class.getResource("ledblue.png")),
				new TestCasePanel(new TestCase("Empty Test Case"),
						testCaseNameListener));
	}

	public void removeSelectedTestCase() {
		if (testCasesTabbedPane.getSelectedIndex() > 0)
			testCasesTabbedPane.removeTabAt(testCasesTabbedPane
					.getSelectedIndex());
	}

	private List<TestCase> getTestCases() {

		List<TestCase> testCases = new LinkedList<TestCase>();
		testCases.add(correctTestCasePanel.getTestCase());
		for (int i = 1; i < testCasesTabbedPane.getTabCount(); i++) {
			TestCasePanel panel = (TestCasePanel) testCasesTabbedPane
					.getComponentAt(i);
			testCases.add(panel.getTestCase());
		}

		return testCases;
	}

}
