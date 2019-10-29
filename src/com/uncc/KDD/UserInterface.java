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
        
        /**
	 * Initialization
	 */
    
	private JFrame lersFramedUI;
	private JTextArea textArea;
        private JTextField dataField;
	private JTextField headerField;
	private JTextField minimumSupportField;
	private JTextField minimumConfidenceField;
        
        private JComboBox<String> decisionAttributeField;
	private JComboBox<String> dicisionToValue;
	private JComboBox<String> dicisionInitialValue;
        
	private File data;
	private File headerFile;
	
        private JList<String> stableAttributes;
	private Logic actionFinder;
	
	
	private static final String SEPARATOR = System.getProperty("line.separator");

	
	/*
        
        Main Function : Staing the Program
        
        */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface window = new UserInterface();
					window.lersFramedUI.setVisible(true);
					
					UIManager.setLookAndFeel(
				            UIManager.getSystemLookAndFeelClassName());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	/**
	 * Populate the User Interface
	 */
	public UserInterface() {
		initialize();
	}

	private void initialize() {
		actionFinder = new Logic();
		actionFinder.addObserver(this);
		
		lersFramedUI = new JFrame();
		lersFramedUI.setResizable(false);
		lersFramedUI.setTitle("KDD Project- Group 10");
		lersFramedUI.getContentPane().setBackground(new Color(105, 105, 105));
		lersFramedUI.setBounds(100, 100, 603, 585);
		lersFramedUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lersFramedUI.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 321, 585, 217);
		lersFramedUI.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(textArea);
		
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.background"));
		panel.setBounds(10, 203, 563, 107);
		lersFramedUI.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblMinimumSupport = new JLabel("Min Support:");
		lblMinimumSupport.setBounds(10, 14, 88, 14);
		lblMinimumSupport.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblMinimumSupport);
		
		minimumConfidenceField = new JTextField();
		minimumConfidenceField.setBounds(108, 11, 96, 20);
		panel.add(minimumConfidenceField);
		minimumConfidenceField.setToolTipText("Enter as a percentage value");
		minimumConfidenceField.setColumns(10);
		
		JLabel lblMinimumConfidence = new JLabel("Min Confidence:");
		lblMinimumConfidence.setBounds(10, 49, 88, 14);
		panel.add(lblMinimumConfidence);
		
		minimumSupportField = new JTextField();
		minimumSupportField.setBounds(108, 46, 96, 20);
		panel.add(minimumSupportField);
		minimumSupportField.setColumns(10);
		
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(465, 71, 88, 25);
		panel.add(btnRun);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(310, 11, 127, 85);
		panel.add(scrollPane_1);
		
		stableAttributes = new JList<String>();
		stableAttributes.setVisibleRowCount(10);
		scrollPane_1.setViewportView(stableAttributes);
		
		JLabel lblStableAttributes = new JLabel("Stable attributes:");
		lblStableAttributes.setHorizontalAlignment(SwingConstants.CENTER);
		lblStableAttributes.setBounds(204, 11, 105, 20);
		panel.add(lblStableAttributes);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean correctInput = true;
				textArea.append("Verifying Input...\n");
				
				HashSet<String> stable = new HashSet<String>();
				stable.addAll(stableAttributes.getSelectedValuesList());		
				actionFinder.setStableFlexible(stable);
				
				if(stable.contains((String)decisionAttributeField.getSelectedItem())){
					JOptionPane.showMessageDialog(null, "Decision attribute cannot be stable.", 
							"Decision attribute error", JOptionPane.ERROR_MESSAGE);
					correctInput = false;
				}
				
				try {
					if(Integer.parseInt(minimumSupportField.getText()) <= 0 || 
							Integer.parseInt(minimumConfidenceField.getText()) < 0) {
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
					actionFinder.setMinSupportConfidence(Integer.parseInt(minimumSupportField.getText()),
							Integer.parseInt(minimumConfidenceField.getText()));
					
					String decisionName = (String)decisionAttributeField.getSelectedItem();
					
					actionFinder.setDecisionAttributes(decisionName + ((String)dicisionInitialValue.getSelectedItem()),
							decisionName + (String)dicisionToValue.getSelectedItem());
					
					(new Thread(actionFinder)).start();
				}				
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 130, 563, 62);
		lersFramedUI.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblChooseDecisionAttribute = new JLabel("Choose decision attribute: ");
		lblChooseDecisionAttribute.setBounds(10, 11, 153, 14);
		lblChooseDecisionAttribute.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblChooseDecisionAttribute);
		
		decisionAttributeField = new JComboBox<String>();
		decisionAttributeField.setBackground(Color.WHITE);
		decisionAttributeField.setBounds(10, 26, 153, 22);
		panel_1.add(decisionAttributeField);
		
		JLabel lblInitialValue = new JLabel("Initial Value:");
		lblInitialValue.setBounds(173, 11, 88, 16);
		panel_1.add(lblInitialValue);
		
		dicisionInitialValue = new JComboBox<String>();
		dicisionInitialValue.setBackground(Color.WHITE);
		dicisionInitialValue.setBounds(173, 26, 88, 22);
		panel_1.add(dicisionInitialValue);
		
		JLabel lblEndValue = new JLabel("End Value:");
		lblEndValue.setBounds(271, 11, 109, 16);
		panel_1.add(lblEndValue);
		
		dicisionToValue = new JComboBox<String>();
		dicisionToValue.setBackground(Color.WHITE);
		dicisionToValue.setBounds(271, 26, 109, 22);
		panel_1.add(dicisionToValue);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 11, 563, 108);
		lersFramedUI.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		dataField = new JTextField();
		dataField.setBounds(10, 11, 348, 22);
		panel_2.add(dataField);
		dataField.setBackground(Color.WHITE);
		dataField.setEditable(false);
		dataField.setColumns(10);
		
		headerField = new JTextField();
		headerField.setBounds(10, 40, 348, 22);
		panel_2.add(headerField);
		headerField.setBackground(Color.WHITE);
		headerField.setEditable(false);
		headerField.setColumns(10);
		
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
				if(!(data == null) && !(headerFile == null)){
					if(data.isFile() && headerFile.isFile()){
						textArea.append("Reading files..." + SEPARATOR);
						actionFinder.readFile(headerFile, data);
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
					headerField.setText(headerFile.getPath());
				}
				headerField.setText(headerFile.getPath());
			}
		});
		btnChooseDataFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileFind = new JFileChooser();
				int returnVal = fileFind.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					data = fileFind.getSelectedFile();
					dataField.setText(data.getPath());
				}	
			}
		});
		decisionAttributeField.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
					dicisionInitialValue.removeAllItems();
					dicisionToValue.removeAllItems();
					
					HashSet<String> distinctValues = actionFinder.getDistinctAttributeValues((String)arg0.getItem());
					
					for(String value : distinctValues) {
						dicisionInitialValue.addItem(value);
						dicisionToValue.addItem(value);
					}
					dicisionInitialValue.setEnabled(true);
					dicisionToValue.setEnabled(true);
				}else {
					dicisionInitialValue.setEnabled(false);
					dicisionToValue.setEnabled(false);
				}
			}
		});
		lersFramedUI.setVisible(true);
	}

	/**
	 * Initializing all the available stable attributes
	 */
	protected void initStableAttributes() {
		String[] attributeNames = (actionFinder.getAttributeNames().toArray(new String[0]));

		stableAttributes.setListData(attributeNames);
		
	}

	/**
	 * Initializing all the available decision attributes
	 */
	protected void initDecisionAttributes() {
		List<String> attributeNames = actionFinder.getAttributeNames();
		
		decisionAttributeField.removeAllItems();
		
		for(String name : attributeNames) {
			decisionAttributeField.addItem(name);
		}	
	}

	@Override
	public void update(Observable arg0, Object lineEnd) {		
		textArea.append((String)lineEnd);
	}
}
