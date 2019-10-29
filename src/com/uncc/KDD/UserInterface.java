package com.uncc.KDD;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.SystemColor;

public class UserInterface implements Observer{

	private JFrame frmKddProjectGroup;
	private JTextArea textArea;
	private File dataFile;
	private File headerFile;
	private JTextField dataFileField;
	private JTextField headerFileField;
	private JTextField minSupportTextField;
	private JTextField minConfidenceTextField;
	private Logic actionFinder;
	private JList<String> stableAttributesList;
	private JComboBox<String> decisionAttributeComboBox;
	private JComboBox<String> dToValueComboBox;
	private JComboBox<String> dInitialValueComboBox;
	private static final String SEPARATOR = System.getProperty("line.separator");

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface window = new UserInterface();
					window.frmKddProjectGroup.setVisible(true);
					
					UIManager.setLookAndFeel(
				            UIManager.getSystemLookAndFeelClassName());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	/**
	 * Create the application.
	 */
	public UserInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		actionFinder = new Logic();
		actionFinder.addObserver(this);
		
		frmKddProjectGroup = new JFrame();
		frmKddProjectGroup.setResizable(false);
		frmKddProjectGroup.setTitle("KDD Project- Group 10");
		frmKddProjectGroup.getContentPane().setBackground(new Color(105, 105, 105));
		frmKddProjectGroup.setBounds(100, 100, 603, 585);
		frmKddProjectGroup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmKddProjectGroup.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 321, 585, 217);
		frmKddProjectGroup.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(textArea);
		
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.background"));
		panel.setBounds(10, 203, 563, 107);
		frmKddProjectGroup.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblMinimumSupport = new JLabel("Min Support:");
		lblMinimumSupport.setBounds(10, 14, 88, 14);
		lblMinimumSupport.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblMinimumSupport);
		
		minConfidenceTextField = new JTextField();
		minConfidenceTextField.setBounds(108, 11, 96, 20);
		panel.add(minConfidenceTextField);
		minConfidenceTextField.setToolTipText("Enter as a percentage value");
		minConfidenceTextField.setColumns(10);
		
		JLabel lblMinimumConfidence = new JLabel("Min Confidence:");
		lblMinimumConfidence.setBounds(10, 49, 88, 14);
		panel.add(lblMinimumConfidence);
		
		minSupportTextField = new JTextField();
		minSupportTextField.setBounds(108, 46, 96, 20);
		panel.add(minSupportTextField);
		minSupportTextField.setColumns(10);
		
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(465, 71, 88, 25);
		panel.add(btnRun);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(310, 11, 127, 85);
		panel.add(scrollPane_1);
		
		stableAttributesList = new JList<String>();
		stableAttributesList.setVisibleRowCount(10);
		scrollPane_1.setViewportView(stableAttributesList);
		
		JLabel lblStableAttributes = new JLabel("Stable attributes:");
		lblStableAttributes.setHorizontalAlignment(SwingConstants.CENTER);
		lblStableAttributes.setBounds(204, 11, 105, 20);
		panel.add(lblStableAttributes);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean correctInput = true;
				textArea.append("Verifying Input...\n");
				
				HashSet<String> stable = new HashSet<String>();
				stable.addAll(stableAttributesList.getSelectedValuesList());		
				actionFinder.setStableFlexible(stable);
				
				if(stable.contains((String)decisionAttributeComboBox.getSelectedItem())){
					JOptionPane.showMessageDialog(null, "Decision attribute cannot be stable.", 
							"Decision attribute error", JOptionPane.ERROR_MESSAGE);
					correctInput = false;
				}
				
				try {
					if(Integer.parseInt(minSupportTextField.getText()) <= 0 || 
							Integer.parseInt(minConfidenceTextField.getText()) < 0) {
						correctInput = false;
						JOptionPane.showMessageDialog(null, "Support and confidence values must be greater than 0", 
								"Value error", JOptionPane.ERROR_MESSAGE);
					}
				}catch(NullPointerException err) {
					correctInput = false;
					JOptionPane.showMessageDialog(null, "Must enter support and confidence values", 
							"Value missing", JOptionPane.ERROR_MESSAGE);
				}catch(NumberFormatException err) {
					correctInput = false;
					JOptionPane.showMessageDialog(null, "Support and confidence values must be integers", 
							"Value error", JOptionPane.ERROR_MESSAGE);
				}
					
				if(correctInput) {
					actionFinder.setMinSupportConfidence(Integer.parseInt(minSupportTextField.getText()),
							Integer.parseInt(minConfidenceTextField.getText()));
					
					String decisionName = (String)decisionAttributeComboBox.getSelectedItem();
					
					actionFinder.setDecisionAttributes(decisionName + ((String)dInitialValueComboBox.getSelectedItem()),
							decisionName + (String)dToValueComboBox.getSelectedItem());
					
					(new Thread(actionFinder)).start();
				}				
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 130, 563, 62);
		frmKddProjectGroup.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblChooseDecisionAttribute = new JLabel("Choose decision attribute: ");
		lblChooseDecisionAttribute.setBounds(10, 11, 153, 14);
		lblChooseDecisionAttribute.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblChooseDecisionAttribute);
		
		decisionAttributeComboBox = new JComboBox<String>();
		decisionAttributeComboBox.setBackground(Color.WHITE);
		decisionAttributeComboBox.setBounds(10, 26, 153, 22);
		panel_1.add(decisionAttributeComboBox);
		
		JLabel lblInitialValue = new JLabel("Initial Value:");
		lblInitialValue.setBounds(173, 11, 88, 16);
		panel_1.add(lblInitialValue);
		
		dInitialValueComboBox = new JComboBox<String>();
		dInitialValueComboBox.setBackground(Color.WHITE);
		dInitialValueComboBox.setBounds(173, 26, 88, 22);
		panel_1.add(dInitialValueComboBox);
		
		JLabel lblEndValue = new JLabel("End Value:");
		lblEndValue.setBounds(271, 11, 109, 16);
		panel_1.add(lblEndValue);
		
		dToValueComboBox = new JComboBox<String>();
		dToValueComboBox.setBackground(Color.WHITE);
		dToValueComboBox.setBounds(271, 26, 109, 22);
		panel_1.add(dToValueComboBox);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 11, 563, 108);
		frmKddProjectGroup.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		dataFileField = new JTextField();
		dataFileField.setBounds(10, 11, 348, 22);
		panel_2.add(dataFileField);
		dataFileField.setBackground(Color.WHITE);
		dataFileField.setEditable(false);
		dataFileField.setColumns(10);
		
		headerFileField = new JTextField();
		headerFileField.setBounds(10, 40, 348, 22);
		panel_2.add(headerFileField);
		headerFileField.setBackground(Color.WHITE);
		headerFileField.setEditable(false);
		headerFileField.setColumns(10);
		
		JButton btnChooseDataFile = new JButton("Choose data file");
		btnChooseDataFile.setBounds(368, 10, 185, 25);
		panel_2.add(btnChooseDataFile);
		
		JButton btnChooseAttributeName = new JButton("Choose attribute name file");
		btnChooseAttributeName.setBounds(368, 39, 185, 25);
		panel_2.add(btnChooseAttributeName);
		
		JButton btnLoadFiles = new JButton("Load files...");
		btnLoadFiles.setBounds(10, 72, 109, 25);
		panel_2.add(btnLoadFiles);
		btnLoadFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!(dataFile == null) && !(headerFile == null)){
					if(dataFile.isFile() && headerFile.isFile()){
						textArea.append("Reading files..." + SEPARATOR);
						actionFinder.readFile(headerFile, dataFile);
						textArea.append("Files read" + SEPARATOR);
						
						//set decision attribute choices
						initDecisionAttributes();
						//set stable attribute choices
						initStableAttributes();
					}else{
						JOptionPane.showMessageDialog(null, "Files could not be read. Please check files chosen.", 
								"File error", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null, "Please choose a file for the attribute names and the data values.", 
							"File error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnChooseAttributeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileFind = new JFileChooser();
				int returnVal = fileFind.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					headerFile = fileFind.getSelectedFile();
					headerFileField.setText(headerFile.getPath());
				}
				headerFileField.setText(headerFile.getPath());
			}
		});
		btnChooseDataFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileFind = new JFileChooser();
				int returnVal = fileFind.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					dataFile = fileFind.getSelectedFile();
					dataFileField.setText(dataFile.getPath());
				}	
			}
		});
		decisionAttributeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
					dInitialValueComboBox.removeAllItems();
					dToValueComboBox.removeAllItems();
					
					HashSet<String> distinctValues = actionFinder.getDistinctAttributeValues((String)arg0.getItem());
					
					for(String value : distinctValues) {
						dInitialValueComboBox.addItem(value);
						dToValueComboBox.addItem(value);
					}
					dInitialValueComboBox.setEnabled(true);
					dToValueComboBox.setEnabled(true);
				}else {
					dInitialValueComboBox.setEnabled(false);
					dToValueComboBox.setEnabled(false);
				}
			}
		});
		frmKddProjectGroup.setVisible(true);
	}

	/**
	 * Initialize all potential stable attributes in the list
	 */
	protected void initStableAttributes() {
		String[] attributeNames = (actionFinder.getAttributeNames().toArray(new String[0]));

		stableAttributesList.setListData(attributeNames);
		
	}

	/**
	 * Initializes all of the potential decision attribute values
	 */
	protected void initDecisionAttributes() {
		List<String> attributeNames = actionFinder.getAttributeNames();
		
		decisionAttributeComboBox.removeAllItems();
		
		for(String name : attributeNames) {
			decisionAttributeComboBox.addItem(name);
		}	
	}

	@Override
	public void update(Observable arg0, Object lineEnd) {		
		textArea.append((String)lineEnd);
	}
}
