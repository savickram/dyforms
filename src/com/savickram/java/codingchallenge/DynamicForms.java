package com.savickram.java.codingchallenge;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class represents a GUI Form which is dynamic and activates components based on our rules / values of other components
 * 
 * @author Vickram
 *
 */
public class DynamicForms extends JFrame implements ActionListener {

	public JPanel jPanel;
	public ArrayList<Element> eList;
	public ArrayList<JComponent> componentList = new ArrayList<JComponent>();
	public HashMap<String, ArrayList<ChildElement>> childListMap = new HashMap<String, ArrayList<ChildElement>>();
	public HashMap<String, String> submittedForm = new HashMap<String, String>();

	/**
	 * Default Constructor
	 */
	public DynamicForms() {

	}

	/**
	 *
	 * Constructor to initialize the Dynamic Form from element list
	 * @param eList - List of Elements parsed from JSON
	 */
	public DynamicForms(ArrayList<Element> eList) {

		super("Dynamic Form");
		init(eList);
	}

	/**
	 * initialize the Dynamic Form from eList
	 * @param eList - List of Elements parsed from JSON
	 */
	public void init(ArrayList<Element> eList) {

		// set layout manager
		setLayout(new FlowLayout());

		// Instantiate a Container
		jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		this.eList = eList;

		
		// Iterates through all Elements
		for (Element element : eList) {

			JPanel formRow = new JPanel();

			// Create a label & textbox if ElementType is TEXT
			if (element.getType().equalsIgnoreCase("text")) {

				//Label for TextBox
				JLabel nameLabel = new JLabel();
				nameLabel.setText(element.getName());
				formRow.add(nameLabel);

				//TextBox with Listener
				JTextField jTextField = new JTextField("", 30);
				jTextField.setName(element.getName());
				jTextField.setEnabled(!element.isHasParent());

				// Add child Element details if element has any child
				if (element.isHasChild()) {

					childListMap.put(jTextField.getName(),
							element.getChildList());

					// Adding Listener for necessary dependent child Elements
					jTextField.addFocusListener(new FocusListener() {

						public void focusLost(FocusEvent focusEvent) {

							// Iterating through all the child elements of current element
							for (ChildElement childElement : childListMap
									.get(((JTextField) focusEvent.getSource())
											.getName())) {

								JComponent cElement = getChildElement(childElement);
								
								// Check if the current value matches Trigger Value
								if (((JTextField) focusEvent.getSource())
										.getText()
										.equalsIgnoreCase(
												childElement
														.getParentElementTriggerValue())) {

									// Enables the element
									
									if (cElement instanceof JTextField) {

										((JTextField) cElement)
												.setEnabled(true);

									} else if (cElement instanceof JComboBox) {

										((JComboBox) cElement).setEnabled(true);

									}

								} else {

									// Check if the current value matches Trigger Value, diables the element
									
									if (cElement instanceof JTextField) {

										((JTextField) cElement)
												.setEnabled(false);

									} else if (cElement instanceof JComboBox) {

										((JComboBox) cElement)
												.setEnabled(false);

									}

								}
							}
						}

						public void focusGained(FocusEvent focusEvent) {
						}
					});

				}
				
				// Adding the textfield to ComponentList
				componentList.add(jTextField);
				formRow.add(jTextField);

			} 
			
			// Create a label & Combo box if ElementType is NUMBER or ENUM
			else if (element.getType().equalsIgnoreCase("number")
					|| element.getType().equalsIgnoreCase("Enum")) {

				String[] dropDownValues = element.getValues();

				// Label for Combo Box
				JLabel nameLabel = new JLabel(element.getName());
				formRow.add(nameLabel);

				// Combo Box with values
				JComboBox jComboBox = new JComboBox(dropDownValues);
				jComboBox.setName(element.getName());
				jComboBox.setSize(130, 30);
				jComboBox.setEnabled(!element.isHasParent());

				// If any child is present, add necesary listeners
				if (element.isHasChild()) {

					childListMap.put(jComboBox.getName(),
							element.getChildList());

					jComboBox.addItemListener(new ItemListener() {

						public void itemStateChanged(ItemEvent itemEvent) {

							// Iterating through all the child elements of current element
							for (ChildElement childElement : childListMap
									.get(((JComboBox) itemEvent.getSource())
											.getName())) {

								JComponent cElement = getChildElement(childElement);

								// Enable the element if the current value matches Trigger Value
								if (((JComboBox) itemEvent.getSource())
										.getSelectedItem()
										.toString()
										.equalsIgnoreCase(
												childElement
														.getParentElementTriggerValue())) {

									if (cElement instanceof JTextField) {

										((JTextField) cElement)
												.setEnabled(true);

									} else if (cElement instanceof JComboBox) {

										((JComboBox) cElement).setEnabled(true);

									}
								} else {

									// Disable the element
									if (cElement instanceof JTextField) {

										((JTextField) cElement)
												.setEnabled(false);

									} else if (cElement instanceof JComboBox) {

										((JComboBox) cElement)
												.setEnabled(false);

									}

								}

							}
						}

					});

				}

				componentList.add(jComboBox);
				formRow.add(jComboBox);

			} else if (element.getType().equalsIgnoreCase("button")) {

				// Submission and Clear buttons
				JButton button = new JButton();
				button.setText(element.getName());
				button.addActionListener(this);
				formRow.add(button);

			}

			jPanel.add(formRow);

		}

		// Adding the Panel to JFrame
		add(jPanel, BorderLayout.CENTER);

		// adds window event listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				int reply = JOptionPane
						.showConfirmDialog(DynamicForms.this,
								"Are you sure you want to quit?", "Exit",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (reply == JOptionPane.YES_OPTION) {
					dispose();
				} else {
					return;
				}
			}
		});

		// Packs the JFrme and setting relevant properties
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);

	}

	
	/**
	 * Finds and returns the JComponent based on the given Child Element
	 * @param eType - Type of Child Element
	 * @param childElement - 
	 * @return
	 */
	public JComponent getChildElement(ChildElement childElement) {

		for (JComponent component : componentList) {

			if (component instanceof JTextField
					&& ((JTextField) component).getName().equals(
							childElement.getChildElementName())) {

				return component;

			} else if (component instanceof JComboBox
					&& ((JComboBox) component).getName().equals(
							childElement.getChildElementName())) {

				return component;
			}

		}

		return null;
	}

	/**
	 * Submit & Clear button logic
	 */
	public void actionPerformed(ActionEvent actionEvent) {

		JButton clickedButton = (JButton) actionEvent.getSource();

		if (clickedButton.getText().equals("submit")) {

			// Iterate through all the Components and fetch the form data in a HashMap
			for (JComponent component : componentList) {

				if(component instanceof JTextField && component.isEnabled()){
					
					submittedForm.put(component.getName(), ((JTextField) component).getText());
					
				} else if(component instanceof JComboBox && component.isEnabled()){
					
					submittedForm.put(component.getName(), ((JComboBox) component).getSelectedItem().toString());
					
				}
				
				
			}
			
		} else if (clickedButton.getText().equals("clear")) {

			// Iterate through all the Components and clear the content
			for (JComponent component : componentList) {

				if(component instanceof JTextField && component.isEnabled()){
					
					((JTextField) component).setText("");
					
				} else if(component instanceof JComboBox && component.isEnabled()){
					
					((JComboBox) component).setSelectedIndex(0);
					
				}
				
				
			}
			
		}
	}

}