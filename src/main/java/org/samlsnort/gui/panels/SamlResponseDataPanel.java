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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.samlsnort.util.Configuration;
import org.samlsnort.util.GuiUtil;
import org.samlsnort.util.KeyStoreTool;
import org.samlsnort.vo.AttributeData;
import org.samlsnort.vo.SamlResponseData;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SamlResponseDataPanel extends JPanel {
	private static final Logger LOGGER = Logger
			.getLogger(SamlResponseDataPanel.class.getName());
	private static ResourceBundle RESOURCE = ResourceBundle.getBundle(SamlResponseDataPanel.class.getName());
	private static final long serialVersionUID = 1L;
	private JComboBox destinationComboBox;
	private JComboBox inResponseToComboBox;
	private JComboBox issueInstantComboBox;
	private JComboBox issuerCombBox;
	private JComboBox statusCodeValueComboBox;
	private JComboBox canocializationMethodAlgorithmComboBox;
	private JComboBox signatureMethodAlgorithmComboBox;
	private JComboBox certificateAliasComboBox;
	private JComboBox subjectFormatComboBox;
	private JComboBox subjectNotOnOrAfterComboBox;
	private JComboBox subjectRecipientComboBox;
	private JComboBox subjectInResponseToComboBox;
	private JComboBox conditionNotBeforeComboBox;
	private JComboBox conditionNotOnOrAfterComboBox;
	private JComboBox audienceComboBox;
	private JComboBox authnInstantComboBox;
	private JComboBox authnContextClassRefComboBox;
	private JComboBox subjectLocalityAddressComboBox;
	private JComboBox subjectLocalityDNSNameTextField;
	private JTable attributesTable;
	private JCheckBox signCheckbox;
	private JComboBox subjectValueTextField;
	private JComboBox subjectNotBeforeComboBox;
	private JComboBox assertionIssuerComboBox;

	public SamlResponseDataPanel() {
		initialize();
	}

	public SamlResponseDataPanel(SamlResponseData data) {
		this();
		setSamlResponseData(data);
	}

	private void initialize() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(14dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GLUE_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("27dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(31dlu;default)"),}));

		
		
		JLabel lblDestination = new JLabel("Destination");
		lblDestination.setToolTipText(RESOURCE.getString("destination"));
		add(lblDestination, "2, 2, 3, 1, left, default");
		
		destinationComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getDestinationOptions());
		add(destinationComboBox, "6, 2, 3, 1, fill, default");

		JLabel lblInresponseto = new JLabel("InResponseTo");
		lblInresponseto.setToolTipText(RESOURCE.getString("inResponseTo"));
		add(lblInresponseto, "2, 4, 3, 1, left, default");

		inResponseToComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getInResponseToOptions());
		add(inResponseToComboBox, "6, 4, 3, 1, fill, default");

		JLabel lblIssueinstant = new JLabel("Issue Instant");
		lblIssueinstant.setFont(lblIssueinstant.getFont().deriveFont(lblIssueinstant.getFont().getStyle() | Font.BOLD));
		lblIssueinstant.setToolTipText(RESOURCE.getString("issueInstant"));
		add(lblIssueinstant, "2, 6, 3, 1, left, default");

		issueInstantComboBox = GuiUtil.createJCombobox(true, Configuration
				.getInstance().getTimeOptions());
		add(issueInstantComboBox, "6, 6, 3, 1, fill, default");

		JLabel lblIssuer = new JLabel("Issuer");
		lblIssuer.setToolTipText(RESOURCE.getString("issuer"));
		add(lblIssuer, "2, 8, 3, 1, left, default");

		issuerCombBox = GuiUtil.createJCombobox(false, Configuration.getInstance().getIssuerOptions());
		add(issuerCombBox, "6, 8, 3, 1, fill, default");

		JLabel lblStatusCodeValue = new JLabel("Status Code Value");
		lblStatusCodeValue.setFont(lblIssueinstant.getFont().deriveFont(lblIssueinstant.getFont().getStyle() | Font.BOLD));
		lblStatusCodeValue.setToolTipText(RESOURCE.getString("statusCodeValue"));
		add(lblStatusCodeValue, "2, 10, 3, 1, left, default");

		statusCodeValueComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getStatusCodeValueOptions());
		add(statusCodeValueComboBox, "6, 10, 3, 1, fill, default");
		
		add(DefaultComponentFactory.getInstance().createSeparator("Assertion",SwingConstants.CENTER), "2, 12, 7, 1, default, bottom");
		
		JLabel lblAssertionIssuer = new JLabel("Assertion Issuer");
		lblAssertionIssuer.setFont(lblIssueinstant.getFont().deriveFont(lblIssueinstant.getFont().getStyle() | Font.BOLD));
		lblAssertionIssuer.setToolTipText(RESOURCE.getString("assertionIssuer"));
		add(lblAssertionIssuer, "4, 14, left, default");
		
		assertionIssuerComboBox = GuiUtil.createJCombobox(false, Configuration.getInstance().getIssuerOptions());
		add(assertionIssuerComboBox, "6, 14, 3, 1, fill, default");
		
		add(DefaultComponentFactory.getInstance().createSeparator("Signature",SwingConstants.CENTER), "4, 16, 5, 1");

		JLabel lblCanocializationMethodAlgorithm = new JLabel(
				"Canonicalizationmethod Algorithm");
		lblCanocializationMethodAlgorithm.setToolTipText(RESOURCE.getString("canocializationMethodAlgorithm"));
		add(lblCanocializationMethodAlgorithm, "4, 18, left, default");

		canocializationMethodAlgorithmComboBox = GuiUtil.createJCombobox(false,
				Configuration.getInstance()
						.getCanocializationMethodAlgorithmOptions());
		add(canocializationMethodAlgorithmComboBox,
				"6, 18, 3, 1, fill, default");

		JLabel lblSignatureMethodAlgorithm = new JLabel(
				"Signature Method Algorithm");
		lblSignatureMethodAlgorithm.setToolTipText(RESOURCE.getString("signatureMethodAlgorithm"));
		add(lblSignatureMethodAlgorithm, "4, 20, left, default");

		signatureMethodAlgorithmComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getSignatureMethodAlgorithmOptions());
		add(signatureMethodAlgorithmComboBox, "6, 20, 3, 1, fill, default");

		JLabel lblSign = new JLabel("Sign");
		add(lblSign, "4, 22");

		signCheckbox = new JCheckBox("Sign");
		signCheckbox.setSelected(true);
		add(signCheckbox, "6, 22, 3, 1");

		JLabel lblCertificatealias = new JLabel("Certificate Alias");
		lblCertificatealias.setToolTipText(RESOURCE.getString("certificateAlias"));
		add(lblCertificatealias, "4, 24, left, default");

		certificateAliasComboBox = GuiUtil.createJCombobox(false, KeyStoreTool
				.getAliases());
		add(certificateAliasComboBox, "6, 24, 3, 1, fill, default");
		
		add(DefaultComponentFactory.getInstance().createSeparator("Subject",SwingConstants.CENTER), "4, 26, 5, 1");

		JLabel lblSubjectFormat = new JLabel("Subject Format");
		lblSubjectFormat.setToolTipText(RESOURCE.getString("subjectFormat"));
		add(lblSubjectFormat, "4, 28, left, default");

		subjectFormatComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getSubjectFormatOptions());
		add(subjectFormatComboBox, "6, 28, 3, 1, fill, default");

		JLabel lblSubjectValue = new JLabel("Subject Value");
		add(lblSubjectValue, "4, 30, left, default");

		subjectValueTextField = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getSubjectValueOptions());
		add(subjectValueTextField, "6, 30, 3, 1, fill, default");

		JLabel lblSubjectNotBefore = new JLabel("Not Before");
		lblSubjectNotBefore.setToolTipText(RESOURCE.getString("subjectNotBefore"));
		add(lblSubjectNotBefore, "4, 32, left, default");

		subjectNotBeforeComboBox = GuiUtil.createJCombobox(true, Configuration
				.getInstance().getTimeOptions());
		add(subjectNotBeforeComboBox, "6, 32, 3, 1, fill, default");

		JLabel lblSubjectNotOnOrAfter = new JLabel("Not On Or After");
		lblSubjectNotOnOrAfter.setToolTipText(RESOURCE.getString("subjectNotOnOrAfter"));
		add(lblSubjectNotOnOrAfter, "4, 34, left, default");

		subjectNotOnOrAfterComboBox = GuiUtil.createJCombobox(true, Configuration
				.getInstance().getTimeOptions());
		add(subjectNotOnOrAfterComboBox, "6, 34, 3, 1, fill, default");

		JLabel lblSubjectrecipient = new JLabel("Recipient");
		lblSubjectrecipient.setToolTipText(RESOURCE.getString("subjectRecipient"));
		add(lblSubjectrecipient, "4, 36, left, default");

		subjectRecipientComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getSubjectRecipientOptions());
		add(subjectRecipientComboBox, "6, 36, 3, 1, fill, default");

		JLabel lblSubjectInresponseto = new JLabel("InResponseTo");
		lblSubjectInresponseto.setToolTipText(RESOURCE.getString("subjectInResponseTo"));
		add(lblSubjectInresponseto, "4, 38, left, default");

		subjectInResponseToComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getSubjectInResponseToOptions());
		add(subjectInResponseToComboBox, "6, 38, 3, 1, fill, default");
		
		add(DefaultComponentFactory.getInstance().createSeparator("Condition",SwingConstants.CENTER), "4, 40, 5, 1");

		JLabel lblConditionNotBefore = new JLabel("Not Before");
		lblConditionNotBefore.setToolTipText(RESOURCE.getString("conditionNotBefore"));
		add(lblConditionNotBefore, "4, 42, left, default");

		conditionNotBeforeComboBox = GuiUtil.createJCombobox(true, Configuration
				.getInstance().getTimeOptions());
		add(conditionNotBeforeComboBox, "6, 42, 3, 1, fill, default");

		JLabel lblConditionNotOnOrAfter = new JLabel("Not On Or After");
		lblConditionNotOnOrAfter.setToolTipText(RESOURCE.getString("conditionNotOnOrAfter"));
		add(lblConditionNotOnOrAfter, "4, 44, left, default");

		conditionNotOnOrAfterComboBox = GuiUtil.createJCombobox(true, Configuration
				.getInstance().getTimeOptions());
		add(conditionNotOnOrAfterComboBox, "6, 44, 3, 1, fill, default");

		JLabel lblAudience = new JLabel("Audience");
		lblAudience.setToolTipText(RESOURCE.getString("audience"));
		add(lblAudience, "4, 46, left, default");

		audienceComboBox = GuiUtil.createJCombobox(false, Configuration.getInstance()
				.getAudienceOptions());
		add(audienceComboBox, "6, 46, 3, 1, fill, default");
		
		add(DefaultComponentFactory.getInstance().createSeparator("AuthnStatement",SwingConstants.CENTER), "4, 48, 5, 1");

		JLabel lblAuthninstant = new JLabel("Authn Instant");
		lblAuthninstant.setFont(lblIssueinstant.getFont().deriveFont(lblIssueinstant.getFont().getStyle() | Font.BOLD));
		lblAuthninstant.setToolTipText(RESOURCE.getString("authnInstant"));
		add(lblAuthninstant, "4, 50, left, default");

		authnInstantComboBox = GuiUtil.createJCombobox(true, Configuration
				.getInstance().getTimeOptions());
		add(authnInstantComboBox, "6, 50, 3, 1, fill, default");

		JLabel lblAuthnContextClass = new JLabel("Authn Context Class Ref");
		lblAuthnContextClass.setToolTipText(RESOURCE.getString("authnContextClassRef"));
		add(lblAuthnContextClass, "4, 52, left, default");

		authnContextClassRefComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getAuthnContextClassRefOptions());
		add(authnContextClassRefComboBox, "6, 52, 3, 1, fill, default");

		JLabel lblSubjectLocalityAddress = new JLabel(
				"Subject Locality Address");
		lblSubjectLocalityAddress.setToolTipText(RESOURCE.getString("subjectLocalityAddress"));
		add(lblSubjectLocalityAddress, "4, 54, left, default");

		subjectLocalityAddressComboBox = GuiUtil.createJCombobox(false, Configuration
				.getInstance().getSubjectLocalityAddress());

		add(subjectLocalityAddressComboBox, "6, 54, 3, 1, fill, default");

		JLabel lblSubjectLocalityDns = new JLabel("Subject Locality DNS Name");
		lblSubjectLocalityDns.setToolTipText(RESOURCE.getString("subjectLocalityDNSName"));
		add(lblSubjectLocalityDns, "4, 56, left, default");

		subjectLocalityDNSNameTextField = GuiUtil.createJCombobox(false,Configuration.getInstance().getSubjectLocalityDNSNameOptions());
		add(subjectLocalityDNSNameTextField, "6, 56, 3, 1, fill, default");
		
		add(DefaultComponentFactory.getInstance().createSeparator("Attributes",SwingConstants.CENTER), "4, 58, 5, 1");

		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, "4, 60, 3, 3, fill, fill");

		attributesTable = new JTable();
		attributesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(attributesTable);
		attributesTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Name", "Friendly Name", "Format", "Value" }));

		
		JButton btnAdd = new JButton(new ImageIcon(SamlResponseDataPanel.class
				.getResource("add.png")));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DefaultTableModel) attributesTable.getModel())
						.addRow(new Object[] { null, null, null, null });
			}
		});
		add(btnAdd, "8, 60");

		JButton btnRemove = new JButton(new ImageIcon(
				SamlResponseDataPanel.class.getResource("delete.png")));
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (attributesTable.getSelectedRowCount() == 1)
					((DefaultTableModel) attributesTable.getModel())
							.removeRow(attributesTable.getSelectedRow());
			}
		});
		add(btnRemove, "8, 62, default, top");
	}

	public SamlResponseData getSamlResponseData() {
		LOGGER.entering(SamlResponseDataPanel.class.getName(),
				"getSamlResponseData");
		SamlResponseData samlResponseData = new SamlResponseData();

		if (audienceComboBox.getSelectedItem() != null)
			samlResponseData.setAudience(audienceComboBox.getSelectedItem()
					.toString());
		else
			samlResponseData.setAudience(null);

		if (authnContextClassRefComboBox.getSelectedItem() != null)
			samlResponseData
					.setAuthnContextClassRef(authnContextClassRefComboBox
							.getSelectedItem().toString());
		else
			samlResponseData.setAuthnContextClassRef(null);
		if (authnInstantComboBox.getSelectedItem() != null) {
			try {
				samlResponseData.setAuthnInstant(Integer
						.parseInt(authnInstantComboBox.getSelectedItem()
								.toString()));
			} catch (NumberFormatException e) {
			}
		} else
			samlResponseData.setAuthnInstant(null);

		if (canocializationMethodAlgorithmComboBox.getSelectedItem() != null)
			samlResponseData
					.setCanocializationMethodAlgorithm(canocializationMethodAlgorithmComboBox
							.getSelectedItem().toString());
		else
			samlResponseData.setCanocializationMethodAlgorithm(null);

		if (conditionNotBeforeComboBox.getSelectedItem() != null) {
			try {
				samlResponseData.setConditionNotBefore(Integer
						.parseInt(conditionNotBeforeComboBox.getSelectedItem()
								.toString()));
			} catch (NumberFormatException e) {
			}
		} else
			samlResponseData.setConditionNotBefore(null);
		if (conditionNotOnOrAfterComboBox.getSelectedItem() != null) {
			try {
				samlResponseData.setConditionNotOnOrAfter(Integer
						.parseInt(conditionNotOnOrAfterComboBox
								.getSelectedItem().toString()));
			} catch (NumberFormatException e) {
			}
		} else
			samlResponseData.setConditionNotOnOrAfter(null);

		if (destinationComboBox.getSelectedItem() != null)
			samlResponseData.setDestination(destinationComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setDestination(null);

		if (inResponseToComboBox.getSelectedItem() != null)
			samlResponseData.setInResponseTo(inResponseToComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setInResponseTo(null);

		if (issueInstantComboBox.getSelectedItem() != null) {
			try {
				samlResponseData.setIssueInstant(Integer
						.parseInt(issueInstantComboBox.getSelectedItem()
								.toString()));
			} catch (NumberFormatException e) {
			}
		} else
			samlResponseData.setIssueInstant(null);

		if (issuerCombBox.getSelectedItem() != null)
			samlResponseData.setIssuer(issuerCombBox.getSelectedItem().toString());
		else
			samlResponseData.setIssuer(null);

		if (statusCodeValueComboBox.getSelectedItem() != null)
			samlResponseData.setStatusCodeValue(statusCodeValueComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setStatusCodeValue(null);
		
		if (assertionIssuerComboBox.getSelectedItem() != null)
			samlResponseData.setAssertionIssuer(assertionIssuerComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setAssertionIssuer(null);

		samlResponseData.setSign(signCheckbox.isSelected());

		if (signatureMethodAlgorithmComboBox.getSelectedItem() != null)
			samlResponseData
					.setSignatureMethodAlgorithm(signatureMethodAlgorithmComboBox
							.getSelectedItem().toString());
		else
			samlResponseData.setSignatureMethodAlgorithm(null);


		if (subjectFormatComboBox.getSelectedItem() != null)
			samlResponseData.setSubjectFormat(subjectFormatComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setSubjectFormat(null);

		if (subjectInResponseToComboBox.getSelectedItem() != null)
			samlResponseData.setSubjectInResponseTo(subjectInResponseToComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setSubjectInResponseTo(null);

		if (subjectLocalityAddressComboBox.getSelectedItem() != null)
			samlResponseData
					.setSubjectLocalityAddress(subjectLocalityAddressComboBox
							.getSelectedItem().toString());
		else
			samlResponseData.setSubjectLocalityAddress(null);

		if (subjectLocalityDNSNameTextField.getSelectedItem() != null)
			samlResponseData
					.setSubjectLocalityDNSName(subjectLocalityDNSNameTextField
							.getSelectedItem().toString());
		else
			samlResponseData.setSubjectLocalityDNSName(null);

		if (subjectNotOnOrAfterComboBox.getSelectedItem() != null) {
			try {
				samlResponseData.setSubjectNotOnOrAfter(Integer
						.parseInt(subjectNotOnOrAfterComboBox.getSelectedItem()
								.toString()));
			} catch (NumberFormatException e) {
			}
		} else
			samlResponseData.setSubjectNotOnOrAfter(null);

		if (subjectNotBeforeComboBox.getSelectedItem() != null) {
			try {
				samlResponseData.setSubjectNotBefore(Integer
						.parseInt(subjectNotBeforeComboBox.getSelectedItem()
								.toString()));
			} catch (NumberFormatException e) {
			}
		} else
			samlResponseData.setSubjectNotBefore(null);

		if (subjectRecipientComboBox.getSelectedItem() != null)
			samlResponseData.setSubjectRecipient(subjectRecipientComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setSubjectRecipient(null);

		if (subjectValueTextField.getSelectedItem() != null )
			samlResponseData.setSubjectValue(subjectValueTextField.getSelectedItem().toString());
		else
			samlResponseData.setSubjectValue(null);

		if (certificateAliasComboBox.getSelectedItem() != null)
			samlResponseData.setCertificateAlias(certificateAliasComboBox
					.getSelectedItem().toString());
		else
			samlResponseData.setCertificateAlias(null);

		List<AttributeData> attribues = new LinkedList<AttributeData>();
		for (int i = 0; i < attributesTable.getRowCount(); i++) {
			attribues.add(new AttributeData((String) attributesTable
					.getValueAt(i, 0), (String) attributesTable
					.getValueAt(i, 1), (String) attributesTable
					.getValueAt(i, 2), (String) attributesTable
					.getValueAt(i, 3)));
		}
		samlResponseData.setAttributes(attribues);
		LOGGER.exiting(SamlResponseDataPanel.class.getName(),
				"getSamlResponseData", samlResponseData);
		return samlResponseData;
	}

	public void setSamlResponseData(SamlResponseData samlResponseData) {
		LOGGER.entering(SamlResponseDataPanel.class.getName(),
				"setSamlResponseData", samlResponseData);

		if (samlResponseData == null)
			return;
		destinationComboBox.setSelectedItem(samlResponseData.getDestination());
		inResponseToComboBox
				.setSelectedItem(samlResponseData.getInResponseTo());
		if (samlResponseData.getIssueInstant() != null)
			issueInstantComboBox.setSelectedItem(samlResponseData
					.getIssueInstant().toString());
		else
			issueInstantComboBox.setSelectedItem("");
	
		issuerCombBox.setSelectedItem(samlResponseData.getIssuer());
		statusCodeValueComboBox.setSelectedItem(samlResponseData
				.getStatusCodeValue());
		assertionIssuerComboBox.setSelectedItem(samlResponseData
				.getAssertionIssuer());
		canocializationMethodAlgorithmComboBox.setSelectedItem(samlResponseData
				.getCanocializationMethodAlgorithm());
		signatureMethodAlgorithmComboBox.setSelectedItem(samlResponseData
				.getSignatureMethodAlgorithm());
		subjectFormatComboBox.setSelectedItem(samlResponseData
				.getSubjectFormat());
		if (samlResponseData.getSubjectNotOnOrAfter() != null)
			subjectNotOnOrAfterComboBox.setSelectedItem(samlResponseData
					.getSubjectNotOnOrAfter().toString());
		else
			subjectNotOnOrAfterComboBox.setSelectedItem("");
		if (samlResponseData.getSubjectNotBefore() != null)
			subjectNotBeforeComboBox.setSelectedItem(samlResponseData
					.getSubjectNotBefore().toString());
		else
			subjectNotBeforeComboBox.setSelectedItem("");
		subjectRecipientComboBox.setSelectedItem(samlResponseData
				.getSubjectRecipient());
		subjectInResponseToComboBox.setSelectedItem(samlResponseData
				.getSubjectInResponseTo());
		if (samlResponseData.getConditionNotBefore() != null)
			conditionNotBeforeComboBox.setSelectedItem(samlResponseData
					.getConditionNotBefore().toString());
		else
			conditionNotBeforeComboBox.setSelectedItem("");
		if (samlResponseData.getConditionNotOnOrAfter() != null)
			conditionNotOnOrAfterComboBox.setSelectedItem(samlResponseData
					.getConditionNotOnOrAfter().toString());
		else
			conditionNotOnOrAfterComboBox.setSelectedItem("");
		audienceComboBox.setSelectedItem(samlResponseData.getAudience());
		if (samlResponseData.getAuthnInstant() != null)
			authnInstantComboBox.setSelectedItem(samlResponseData
					.getAuthnInstant().toString());
		else
			authnInstantComboBox.setSelectedItem("");
		authnContextClassRefComboBox.setSelectedItem(samlResponseData
				.getAuthnContextClassRef());
		subjectLocalityAddressComboBox.setSelectedItem(samlResponseData
				.getSubjectLocalityAddress());
		subjectLocalityDNSNameTextField.setSelectedItem(samlResponseData
				.getSubjectLocalityDNSName());
		if (samlResponseData.getSign() != null)
			signCheckbox.setSelected(samlResponseData.getSign());
		subjectValueTextField.setSelectedItem(samlResponseData.getSubjectValue());
		certificateAliasComboBox.setSelectedItem(samlResponseData
				.getCertificateAlias());


		if (samlResponseData.getAttributes() != null) {
			DefaultTableModel model = (DefaultTableModel) attributesTable
					.getModel();
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			for (AttributeData attrData : samlResponseData.getAttributes()) {
				model.addRow(new Object[] { attrData.getName(),
						attrData.getFriendlyName(), attrData.getNameFormat(),
						attrData.getValue() });
			}
		}
		LOGGER.exiting(SamlResponseDataPanel.class.getName(),
				"setSamlResponseData");
	}
	

}
