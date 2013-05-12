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
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.samlsnort.gui.listener.TestCaseNameListener;
import org.samlsnort.util.Configuration;
import org.samlsnort.vo.TestCase;

import chrriis.dj.nativeswing.swtimpl.components.JSyntaxHighlighter;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.JSyntaxHighlighter.ContentLanguage;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TestCasePanel extends JPanel {
	private static final Logger LOGGER = Logger.getLogger(TestCasePanel.class
			.getName());

	private static final long serialVersionUID = 1L;
	private SamlResponseDataPanel samlResponseDataPanel;
	private JSyntaxHighlighter authnRequestTextArea;
	private JSyntaxHighlighter samlResponseTextArea;
	private JWebBrowser resultPageEditorPane;
	private TestCase testCase;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JLabel base64Encoding;
	private JLabel deflated;
	private JPanel overviewPanel;
	private JRadioButton successButton;
	private JRadioButton failureButton;
	private JTextArea descriptionArea;
	private JLabel lblName;
	private JTextField nameTextField;
	private TestCaseNameListener testCaseNameListener;
	private TestCasePanel instance;

	public TestCasePanel() {
		this.instance = this;
		init();
	}

	/**
	 * This constructor is used to initialize the 'correct test case panel'
	 * 
	 * @param testCase
	 */
	public TestCasePanel(TestCase testCase, boolean editableHeaderInfo) {
		this();
		setTestCase(testCase);
		if (!editableHeaderInfo) {
			nameTextField.setEditable(false);
			descriptionArea.setEditable(false);
			successButton.setEnabled(false);
			failureButton.setEnabled(false);
		}
	}

	public TestCasePanel(TestCaseNameListener testCaseNameListener) {
		this();
		this.testCaseNameListener = testCaseNameListener;
		initListener();
	}

	public TestCasePanel(TestCase testCase,
			TestCaseNameListener testCaseNameListener) {
		this(testCase, true);
		this.testCaseNameListener = testCaseNameListener;
		initListener();
	}

	private void init() {
		resultPageEditorPane = new JWebBrowser();
		authnRequestTextArea = new JSyntaxHighlighter();
		samlResponseTextArea = new JSyntaxHighlighter();

		setLayout(new BorderLayout(0, 0));

		overviewPanel = new JPanel();
		overviewPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(overviewPanel, BorderLayout.NORTH);
		overviewPanel
				.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("79px"), FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(47dlu;default):grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		lblName = new JLabel("Name");
		overviewPanel.add(lblName, "1, 2, left, default");

		nameTextField = new JTextField();
		nameTextField.setText("Test Case Name");

		overviewPanel.add(nameTextField, "2, 2, 3, 1, fill, default");
		nameTextField.setColumns(10);

		overviewPanel.add(new JLabel("Expect Result"), "1, 4, left, center");
		ButtonGroup buttonGroup = new ButtonGroup();
		successButton = new JRadioButton("Success");
		successButton.setSelected(true);
		buttonGroup.add(successButton);
		overviewPanel.add(successButton, "2, 4, left, default");

		failureButton = new JRadioButton("Failure");
		buttonGroup.add(failureButton);
		overviewPanel.add(failureButton, "4, 4, left, fill");

		overviewPanel.add(new JLabel("Description"), "1, 6, default, top");

		descriptionArea = new JTextArea();
		descriptionArea.setRows(3);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setLineWrap(true);
		descriptionArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		overviewPanel.add(new JScrollPane(descriptionArea),
				"2, 6, 3, 1, fill, fill");

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		samlResponseDataPanel = new SamlResponseDataPanel();
		tabbedPane.addTab("Saml Response Data", null,
				wrap(samlResponseDataPanel), null);

		tabbedPane.addTab("Result Page", null, resultPageEditorPane, null);

		tabbedPane.addTab("SamlResponse XML", null, samlResponseTextArea, null);

		JPanel authnRequestPanel = new JPanel();
		authnRequestPanel.setLayout(new BorderLayout());

		panel = new JPanel();
		authnRequestPanel.add(panel, BorderLayout.NORTH);
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblBaseEncoding = new JLabel("Base64 Encoding");
		panel.add(lblBaseEncoding, "1, 1");

		base64Encoding = new JLabel("");
		panel.add(base64Encoding, "3, 1");

		JLabel lblDeflated = new JLabel("Deflated");
		panel.add(lblDeflated, "1, 3");

		deflated = new JLabel("");
		panel.add(deflated, "3, 3");
		authnRequestPanel.add(authnRequestTextArea, BorderLayout.CENTER);
		tabbedPane.addTab("AuthnRequest XML", null, authnRequestPanel, null);

		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, false);

	}

	private void initListener() {
		nameTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					testCaseNameListener.nameChanged(e.getDocument().getText(0,
							e.getDocument().getLength()), instance);
				} catch (BadLocationException e1) {
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					testCaseNameListener.nameChanged(e.getDocument().getText(0,
							e.getDocument().getLength()), instance);
				} catch (BadLocationException e1) {
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					testCaseNameListener.nameChanged(e.getDocument().getText(0,
							e.getDocument().getLength()), instance);
				} catch (BadLocationException e1) {
				}
			}

		});
	}

	public void setTestCase(TestCase t) {
		LOGGER.entering(TestCasePanel.class.getName(), "setTestCase", t);

		this.testCase = t;
		samlResponseDataPanel.setSamlResponseData(testCase
				.getSamlResponseData());

		if (testCase.getResultPageHTML() != null
				&& testCase.getResultPageHTML().length() > 0
				&& "html".equals(Configuration.getInstance()
						.getResultExpositionMethod())) {
			resultPageEditorPane.setHTMLContent(testCase.getResultPageHTML());
			tabbedPane.setEnabledAt(1, true);
		} else if (testCase.getResultURL() != null
				&& "url".equals(Configuration.getInstance()
						.getResultExpositionMethod())) {
			resultPageEditorPane.navigate(testCase.getResultURL().toString());
			tabbedPane.setEnabledAt(1, true);
		} else if (testCase.getResultPageText() != null
				&& testCase.getResultPageText().length() > 0) {
			resultPageEditorPane.setHTMLContent("<html><body><pre>"
					+ testCase.getResultPageText() + "</pre></body></html>");
			tabbedPane.setEnabledAt(1, true);
		} else {
			tabbedPane.setEnabledAt(1, false);
		}

		if (testCase.getSamlResponse() != null) {
			samlResponseTextArea.setContent(testCase.getSamlResponse(),
					ContentLanguage.XML);
			tabbedPane.setEnabledAt(2, true);
		} else {
			tabbedPane.setEnabledAt(2, false);
		}

		if (testCase.getSamlAuthnRequest() != null) {
			authnRequestTextArea.setContent(testCase.getSamlAuthnRequest(),
					ContentLanguage.XML);
			if (testCase.getBase64Valid() != null) {
				base64Encoding.setText(testCase.getBase64Valid() ? "Valid"
						: "Invalid");
			} else {
				base64Encoding.setText("Not tested");
			}

			if (testCase.getDeflatedValid() != null) {
				deflated.setText(testCase.getDeflatedValid() ? "Valid"
						: "Invalid");
			} else {
				deflated.setText("Not tested");
			}
			tabbedPane.setEnabledAt(3, true);
		} else {
			tabbedPane.setEnabledAt(3, false);
		}

		if (testCase.getExpectSuccess() != null) {
			successButton.setSelected(testCase.getExpectSuccess());
			failureButton.setSelected(!testCase.getExpectSuccess());
		}

		if (testCase.getDescription() != null) {
			descriptionArea.setText(testCase.getDescription());
		}

		if (testCase.getName() != null) {
			nameTextField.setText(testCase.getName());
		}

		LOGGER.exiting(TestCasePanel.class.getName(), "setTestCase");
	}

	public TestCase getTestCase() {
		LOGGER.entering(TestCasePanel.class.getName(), "getTestCase");

		if (testCase == null)
			testCase = new TestCase(samlResponseDataPanel.getSamlResponseData());
		else
			testCase.setSamlResponseData(samlResponseDataPanel
					.getSamlResponseData());

		testCase.setExpectSuccess(successButton.isSelected());
		testCase.setDescription(descriptionArea.getText());
		testCase.setName(nameTextField.getText());

		LOGGER.exiting(TestCasePanel.class.getName(), "getTestCase", testCase);
		return testCase;
	}

	private JScrollPane wrap(JComponent component) {
		JScrollPane result = new JScrollPane(component);
		result.getVerticalScrollBar().setUnitIncrement(18);
		result.getHorizontalScrollBar().setUnitIncrement(18);

		return result;
	}
}
