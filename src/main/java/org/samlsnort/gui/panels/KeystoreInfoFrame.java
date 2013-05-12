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
/**
 * 
 */
package org.samlsnort.gui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.samlsnort.util.KeyStoreTool;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author srzrbh
 * 
 */
public class KeystoreInfoFrame extends JFrame {
	private static final Logger LOGGER = Logger
			.getLogger(CreateNewAliasFrame.class.getName());

	private static final long serialVersionUID = 1L;
	private KeystoreInfoFrame instance;
	private JComboBox aliasComboBox;
	private JTextField privateKeyAlgorithmTextField;
	private JTextField privateKeyFormatTextField;

	private JTextField publicKeyAlgorithmTextField;

	private JTextField publicKeyFormatTextField;
	private JTextField cerTypeTextField;
	private JTextField certSigAlgTextField;
	private JTextField certIssuerDNTextField;
	private JTextField certSubjectDNTextField;
	private JTextField certNotBeforeTextField;
	private JTextField certNotAfterTextField;

	public KeystoreInfoFrame() {
		this.instance = this;
		if (KeyStoreTool.getAliases().isEmpty()) {
			JOptionPane.showMessageDialog(instance, "No aliases found.");
			instance.dispose();
		}
		initialize();
	}

	private void initialize() {
		setTitle("Keystore Informations");

		setSize(394, 423);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel bottom = new JPanel();
		getContentPane().add(bottom, BorderLayout.SOUTH);
		bottom.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.dispose();
			}
		});

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue = JOptionPane.showConfirmDialog(null,
						"Do you want to delete the selected alias?");
				if (returnValue == JOptionPane.YES_OPTION) {
					try {
						KeyStoreTool.deleteAlias(aliasComboBox
								.getSelectedItem().toString());
						aliasComboBox.setModel(new DefaultComboBoxModel(
								KeyStoreTool.getAliases().toArray()));
						if (aliasComboBox.getItemCount() == 0) {
							JOptionPane.showMessageDialog(instance,
									"No aliases found.");
							instance.dispose();
						} else {
							aliasComboBox.setSelectedIndex(0);
						}
					} catch (Exception e1) {
						LOGGER.log(Level.WARNING, "Deleting failed.", e1);
						JOptionPane.showMessageDialog(instance,
								"Delete failed.");
					}
				}
			}
		});
		bottom.add(btnDelete);
		bottom.add(btnClose);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("75px"), ColumnSpec.decode("46px:grow"), },
				new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblNewLabel = new JLabel("Alias");
		panel.add(lblNewLabel, "1, 2, left, top");

		aliasComboBox = new JComboBox();
		aliasComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PrivateKeyEntry entry = (PrivateKeyEntry) KeyStoreTool
							.getEntry(aliasComboBox.getSelectedItem()
									.toString());
					privateKeyAlgorithmTextField.setText(entry.getPrivateKey()
							.getAlgorithm());
					privateKeyFormatTextField.setText(entry.getPrivateKey()
							.getFormat());

					publicKeyAlgorithmTextField.setText(entry.getCertificate()
							.getPublicKey().getAlgorithm());
					publicKeyFormatTextField.setText(entry.getCertificate()
							.getPublicKey().getFormat());

					X509Certificate cert = (X509Certificate) entry
							.getCertificate();
					certIssuerDNTextField
							.setText(cert.getIssuerDN().toString());
					certNotAfterTextField
							.setText(cert.getNotAfter().toString());
					certNotBeforeTextField.setText(cert.getNotBefore()
							.toString());
					certSigAlgTextField.setText(cert.getSigAlgName());
					certSubjectDNTextField.setText(cert.getSubjectDN()
							.toString());
					cerTypeTextField.setText(cert.getType());
				} catch (Exception e1) {
					LOGGER.log(Level.WARNING, "Error while reading entry.", e1);
					JOptionPane.showMessageDialog(instance,
							"Error while reading entry.");
				}
			}
		});
		aliasComboBox.setModel(new DefaultComboBoxModel(KeyStoreTool
				.getAliases().toArray()));
		panel.add(aliasComboBox, "2, 2, fill, default");

		JPanel center = new JPanel();

		getContentPane().add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));

		JPanel pkPanel = new JPanel();
		pkPanel.setBorder(BorderFactory.createTitledBorder("Priavte Key"));
		center.add(pkPanel, BorderLayout.NORTH);
		pkPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("107px"), ColumnSpec.decode("45px:grow"), },
				new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblAlgorithm = new JLabel("Algorithm");
		pkPanel.add(lblAlgorithm, "1, 2, left, top");

		privateKeyAlgorithmTextField = new JTextField();
		privateKeyAlgorithmTextField.setEditable(false);
		pkPanel.add(privateKeyAlgorithmTextField, "2, 2, fill, default");
		privateKeyAlgorithmTextField.setColumns(10);

		JLabel lblFormat = new JLabel("Format");
		pkPanel.add(lblFormat, "1, 4, left, default");

		privateKeyFormatTextField = new JTextField();
		privateKeyFormatTextField.setEditable(false);
		pkPanel.add(privateKeyFormatTextField, "2, 4, fill, default");
		privateKeyFormatTextField.setColumns(10);

		JPanel pubkPanel = new JPanel();
		pubkPanel.setBorder(BorderFactory.createTitledBorder("Public Key"));
		center.add(pubkPanel, BorderLayout.CENTER);
		pubkPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("107px"), ColumnSpec.decode("45px:grow"), },
				new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblAlgorithm_1 = new JLabel("Algorithm");
		pubkPanel.add(lblAlgorithm_1, "1, 2, left, top");

		publicKeyAlgorithmTextField = new JTextField();
		publicKeyAlgorithmTextField.setEditable(false);
		pubkPanel.add(publicKeyAlgorithmTextField, "2, 2, fill, default");
		publicKeyAlgorithmTextField.setColumns(10);

		JLabel lblFormat_1 = new JLabel("Format");
		pubkPanel.add(lblFormat_1, "1, 4, left, default");

		publicKeyFormatTextField = new JTextField();
		publicKeyFormatTextField.setEditable(false);
		pubkPanel.add(publicKeyFormatTextField, "2, 4, fill, default");
		publicKeyFormatTextField.setColumns(10);

		JPanel certPanel = new JPanel();
		certPanel.setBorder(BorderFactory.createTitledBorder("Certificate"));
		center.add(certPanel, BorderLayout.SOUTH);
		certPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("107px"), ColumnSpec.decode("45px:grow"), },
				new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC,
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
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblType = new JLabel("Type");
		certPanel.add(lblType, "1, 2, left, top");

		cerTypeTextField = new JTextField();
		cerTypeTextField.setEditable(false);
		certPanel.add(cerTypeTextField, "2, 2, fill, default");
		cerTypeTextField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Signature Algorithm");
		certPanel.add(lblNewLabel_1, "1, 4, left, default");

		certSigAlgTextField = new JTextField();
		certSigAlgTextField.setEditable(false);
		certPanel.add(certSigAlgTextField, "2, 4, fill, default");
		certSigAlgTextField.setColumns(10);

		JLabel lblIssuerDn = new JLabel("Issuer DN");
		certPanel.add(lblIssuerDn, "1, 6, left, default");

		certIssuerDNTextField = new JTextField();
		certIssuerDNTextField.setEditable(false);
		certPanel.add(certIssuerDNTextField, "2, 6, fill, default");
		certIssuerDNTextField.setColumns(10);

		JLabel lblSubjectDn = new JLabel("Subject DN");
		certPanel.add(lblSubjectDn, "1, 8, left, default");

		certSubjectDNTextField = new JTextField();
		certSubjectDNTextField.setEditable(false);
		certPanel.add(certSubjectDNTextField, "2, 8, fill, default");
		certSubjectDNTextField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Not Before");
		certPanel.add(lblNewLabel_2, "1, 10, left, default");

		certNotBeforeTextField = new JTextField();
		certNotBeforeTextField.setEditable(false);
		certPanel.add(certNotBeforeTextField, "2, 10, fill, default");
		certNotBeforeTextField.setColumns(10);

		JLabel lblNotAfter = new JLabel("Not After");
		certPanel.add(lblNotAfter, "1, 12, left, default");

		certNotAfterTextField = new JTextField();
		certNotAfterTextField.setEditable(false);
		certPanel.add(certNotAfterTextField, "2, 12, fill, default");
		certNotAfterTextField.setColumns(10);

		aliasComboBox.setSelectedIndex(0);

		setVisible(true);
	}

	public static void main(String[] args) {
		new KeystoreInfoFrame();
	}
}
