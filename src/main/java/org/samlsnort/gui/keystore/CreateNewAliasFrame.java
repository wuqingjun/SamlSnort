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
package org.samlsnort.gui.keystore;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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

public class CreateNewAliasFrame extends JFrame {
	private static final Logger LOGGER = Logger
			.getLogger(CreateNewAliasFrame.class.getName());

	private static final long serialVersionUID = 1L;
	private JTextField aliasTextField;
	private JTextField privateKeyLabel;
	private JTextField certificateChainLabel;
	private CreateNewAliasFrame instance;;

	/**
	 * Create the panel.
	 */
	public CreateNewAliasFrame() {
		setTitle("Create New Alias");
		this.instance = this;
		initialize();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(117dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		JLabel lblAlias = new JLabel("Alias");
		panel.add(lblAlias, "2, 2, left, default");

		aliasTextField = new JTextField();
		panel.add(aliasTextField, "4, 2, 3, 1, fill, default");
		aliasTextField.setColumns(10);

		JLabel lblPrivateKey = new JLabel("Private Key");
		panel.add(lblPrivateKey, "2, 4, left, default");

		privateKeyLabel = new JTextField("");
		privateKeyLabel.setEditable(false);
		panel.add(privateKeyLabel, "4, 4");

		JButton btnNewButton = new JButton("Browse ...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File("")
						.getAbsoluteFile());
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					privateKeyLabel.setText(chooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		panel.add(btnNewButton, "6, 4");

		JLabel lblCertificateChain = new JLabel("Certificate Chain");
		panel.add(lblCertificateChain, "2, 6, left, default");

		certificateChainLabel = new JTextField("");
		certificateChainLabel.setEditable(false);
		panel.add(certificateChainLabel, "4, 6");

		JButton btnNewButton_1 = new JButton("Browse ...");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File("")
						.getAbsoluteFile());
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					certificateChainLabel.setText(chooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		panel.add(btnNewButton_1, "6, 6");

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(bottom,BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(KeyStoreTool.getAliases().contains(aliasTextField.getText()))
				{
					JOptionPane.showMessageDialog(instance, "Alias already used.");
				}
				else if(aliasTextField.getText().length()==0)
				{
					JOptionPane.showMessageDialog(instance, "Alias can't be empty .");
				}
				else if(certificateChainLabel.getText().length()==0)
				{
					JOptionPane.showMessageDialog(instance, "Certificate chain can't be empty.");
				}
				else if(privateKeyLabel.getText().length()==0){
					JOptionPane.showMessageDialog(instance, "Private key can't be emtpy.");
				}
				else
				{
					
					File chain = new File(certificateChainLabel.getText());
					if(!chain.exists())
					{
						JOptionPane.showMessageDialog(instance, "Certificate chain file must exist.");
					}
					File pk = new File(privateKeyLabel.getText());
					if(!pk.exists())
					{
						JOptionPane.showMessageDialog(instance, "Private key file must exist.");
					}
					
					try {
						KeyStoreTool.importIntoKeystore(chain, pk, aliasTextField.getText());
						instance.dispose();
					} catch (Exception e) {
						LOGGER.log(Level.WARNING, "Error while creating alias.", e);
						JOptionPane.showMessageDialog(instance, "Alias creating failed.");
					}
				}
			}
		});
		bottom.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.dispose();
			}
		});
		bottom.add(cancelButton);
		setSize(372, 155);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
