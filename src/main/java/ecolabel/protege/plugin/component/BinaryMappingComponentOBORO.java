/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 21 juil. 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import ecolabel.protege.plugin.transferHandler.TextFieldTransferHandler;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 21 juil. 2017
 * we tried to use this class to inherit BinaryMappingComponent, but failed, this class's layout is strange, mappingType is totally out of position
 * please refer to BinaryMappingComponent.java to get more details
 */


public class BinaryMappingComponentOBORO extends JPanel{

	protected boolean editFlag = false;//xd by default the the item is not under editing
	public JTextField tfHead;//xd mapping head
	public JTextField tfTail;//xd mapping tail
	public JTextField mapping;//xd arbitray 
	public JComboBox mappingType;//xd owl official mapping predefined
	public JLabel lbMappingType;//xd 
	public JComboBox literalDatatype;//xd 
	public JButton btnNewButton;
	public JButton btnNewButton_1;
	protected JLabel lblNewLabel;
	private JComboBox mappingTypeOBORO;//xd some predefined object properties from OBO ro.owl
	
	
	public BinaryMappingComponentOBORO() {
		
		setMaximumSize(new Dimension(1500, 40));
		setSize(new Dimension(1500, 40));
		setPreferredSize(new Dimension(1000, 40));
		setBackground(SystemColor.info);
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		setLayout(flowLayout);
		
		
		tfHead = new JTextField();
		tfHead.setEditable(false);//xd by default this component is not editable
		
		add(tfHead);
		tfHead.setColumns(10);
		
		lbMappingType = new JLabel();
		lbMappingType.setPreferredSize(new Dimension(270, 26));
		lbMappingType.setText("SubClassOf");//xd by default, the mapping type is SubClassOf
		add(lbMappingType);
		
		mappingType = new JComboBox();
		mappingType.setVisible(false);//xd by default this component is not visible, only visible when editing
		mappingType.setModel(new DefaultComboBoxModel(new Object[] {
				"SubClassOf",
				"ClassAssertion",
				"EquivalentClasses",
				"DisjointClasses",
				new JSeparator(JSeparator.HORIZONTAL),
				"ObjectPropertyAssertion",//xd 3 sockets
				"ObjectPropertyAssertion-OBO-RO",//xd 3 sockets //xd need mappingOBORO chooser
				"NegativeObjectPropertyAssertion",//xd 3 sockets
				"SubObjectPropertyOf",
				"ObjectPropertyDomain",
				"ObjectPropertyRange",
				new JSeparator(JSeparator.HORIZONTAL),
				"DifferentIndividuals",
				"SameIndividual",
				new JSeparator(JSeparator.HORIZONTAL),
				"DataPropertyAssertion",//xd 3 sockets //xd need literal datatype chooser
				"NegativeDataPropertyAssertion",//xd 3 sockets //xd need literal datatype chooser
				"DataPropertyDomain",
				"DataPropertyRange"//xd need literal datatype chooser
				
				

		}));
		mappingType.setSelectedIndex(0);//xd the first item (SubClassOf) selected by default
		mappingType.setPreferredSize(new Dimension(270, 26));
		mappingType.setRenderer(new SeparatorComboBoxRenderer());
		mappingType.addActionListener(new SeparatorComboBoxListener(mappingType));
		add(mappingType,flowLayout);
		mappingType.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					lbMappingType.setText(e.getItem().toString());//xd set the hidden mapping type label
				}
				
				//xd for some mapping type, we need a third textfield
				if(mappingType.getSelectedItem().equals("ObjectPropertyAssertion") 
						|| mappingType.getSelectedItem().equals("NegativeObjectPropertyAssertion")
						|| mappingType.getSelectedItem().equals("DataPropertyAssertion")
						|| mappingType.getSelectedItem().equals("NegativeDataPropertyAssertion")){
					mapping.setVisible(true);
				}else{
					mapping.setVisible(false);//xd set mapping textfield to be invisible
				}
				
				//xd for the external OBO RO relationship
				if(mappingType.getSelectedItem().equals("ObjectPropertyAssertion-OBO-RO")){
					mappingTypeOBORO.setVisible(true);
					mapping.setVisible(false);
					mapping.setText("RO_0002001");//xd by default the mapping is the first option in the combox of mappingTypeOBORO
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002001 \"aligned with\"");
				}else{
					mappingTypeOBORO.setVisible(false);//xd set OBO-RO mapping chooser to be invisible
					//mapping.setVisible(false);
				}
				
				//xd for some mapping type, we need the literal datatype chooser
				if(mappingType.getSelectedItem().equals("DataPropertyAssertion")
						|| mappingType.getSelectedItem().equals("NegativeDataPropertyAssertion")
						|| mappingType.getSelectedItem().equals("DataPropertyRange")){
					literalDatatype.setVisible(true);
				}else{
					literalDatatype.setVisible(false);
				}
				
				//xd DataPropertyRange doesn't have the tfTail
				if(mappingType.getSelectedItem().equals("DataPropertyRange")){
					tfTail.setVisible(false);
				}else{
					tfTail.setVisible(true);
				}
			}
			
		});
		
		
		
		mapping = new JTextField();
		mapping.setBackground(SystemColor.control);
		mapping.setEditable(false);//xd by default this component is not editable
		mapping.setVisible(false);//xd since the default mapping type is SubClassOf, then this mapping textfield shoule be invisible
		add(mapping,flowLayout);
		mapping.setColumns(10);
		
		mappingTypeOBORO = new JComboBox();
		mappingTypeOBORO.setVisible(false);//xd by default the OBORO relations are not visible
		add(mappingTypeOBORO,flowLayout);
		//xd interesting relationships identified from ro.owl
		mappingTypeOBORO.setModel(new DefaultComboBoxModel(new Object[] {
			"aligned with",
			"depends on",
			"differs in",
			"has function",
			"contains",
			"derives from",
			"develops from",
			"has host",
			"visits",
			"input of",
			"output of",
			"interacts with",
			"located in",
			"has part",
			"precedes"
		}));
		
		mappingTypeOBORO.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(mappingTypeOBORO.getSelectedItem().equals("aligned with")){
					mapping.setText("RO_0002001");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002001 \"aligned with\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("depends on")){
					mapping.setText("RO_0002502");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002502 \"depends on\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("differs in")){
					mapping.setText("RO_0002424");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002424 \"differs in\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("has function")){
					mapping.setText("RO_0000085");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0000085 \"has function\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("contains")){
					mapping.setText("RO_0001019");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0001019 \"contains\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("derives from")){
					mapping.setText("RO_0001000");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0001000 \"derives from\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("develops from")){
					mapping.setText("RO_0002202");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002202 \"develops from\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("has host")){
					mapping.setText("RO_0002454");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002454 \"has host\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("visits")){
					mapping.setText("RO_0002618");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002618 \"visits\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("input of")){
					mapping.setText("RO_0002352");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002352 \"input of\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("output of")){
					mapping.setText("RO_0002353");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002353 \"output of\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("interacts with")){
					mapping.setText("RO_0002434");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002434 \"interacts with\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("located in")){
					mapping.setText("RO_0001025");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0001025 \"located in\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("has part")){
					mapping.setText("BFO_0000051");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/BFO_0000051 \"has part\"");
				}else if(mappingTypeOBORO.getSelectedItem().equals("precedes")){
					mapping.setText("BFO_0000063");
					mapping.setToolTipText("http://purl.obolibrary.org/obo/BFO_0000063 \"precedes\"");
				}
				
				}
			});
		
//		mappingOBORO = new JTextField();
//		mappingOBORO.setVisible(false);
//		add(mappingOBORO);
//		mappingOBORO.setColumns(10);
		
		tfTail = new JTextField();
		tfTail.setEditable(false);//xd by default this component is not editable
		
		add(tfTail,flowLayout);
		tfTail.setColumns(10);
		
		
		literalDatatype = new JComboBox();
		literalDatatype.setVisible(false);//xd by default this component is not visible, only visible when editing
		setliteralDatatypeModel();
		literalDatatype.setSelectedItem("string");
		literalDatatype.setPreferredSize(new Dimension(130, 26));
		add(literalDatatype,flowLayout);//xd add this combobox right before the edit and remove button
		
		btnNewButton = new JButton("Edit");
		btnNewButton.addActionListener(new ActionListener() {//xd when the user wants to edit this item
			public void actionPerformed(ActionEvent e) {
				if(editFlag == true){
					editFlag = false;//xd close the edit flag
					lbMappingType.setVisible(true);
					mappingType.setVisible(false);
					mapping.setEditable(false);
					if(mappingType.getSelectedItem().equals("ObjectPropertyAssertion-OBO-RO")){
						mapping.setVisible(true);
						
						mappingTypeOBORO.setVisible(false);
					}
					//mappingOBORO.setEditable(false);
					tfHead.setEditable(false);
					tfTail.setEditable(false);
					literalDatatype.setEditable(false);
					tfHead.setTransferHandler(null);//xd remove drag & drop support
					tfTail.setTransferHandler(null);//xd remove drag & drop support
					mapping.setTransferHandler(null);//xd remove drag & drop support
					
					btnNewButton.setText("Edit");
					setBackground(SystemColor.info);
					
				}else{
					editFlag = true;//xd open the edit flag
					lbMappingType.setVisible(false);
					mappingType.setVisible(true);
					mapping.setEditable(true);
					if(mappingType.getSelectedItem().equals("ObjectPropertyAssertion-OBO-RO")){
						mappingToOBOROMapping();
						mappingTypeOBORO.setVisible(true);
						mapping.setVisible(false);
					}
					tfHead.setEditable(true);
					tfTail.setEditable(true);
					literalDatatype.setEditable(true);
					tfHead.setTransferHandler(new TextFieldTransferHandler());//xd drag & drop support 
					mapping.setTransferHandler(new TextFieldTransferHandler());//xd drag & drop support 
					tfTail.setTransferHandler(new TextFieldTransferHandler());//xd drag & drop support
					
					btnNewButton.setText("Done");
					setBackground(SystemColor.textHighlight);
					
					
				}
				
				
			}
		});
		btnNewButton.setPreferredSize(new Dimension(90, 29));
		add(btnNewButton,flowLayout);
		
		btnNewButton_1 = new JButton("Remove");
		btnNewButton_1.addActionListener(new ActionListener() {//xd whenn the user delete this item
			public void actionPerformed(ActionEvent e) {
				BinaryMappingComponent temp = (BinaryMappingComponent) ((JButton) e.getSource()).getParent();
				Container tempParent = temp.getParent();
				tempParent.remove(temp);
				tempParent.revalidate();
				tempParent.repaint();
			}
		});
		
		btnNewButton_1.setPreferredSize(new Dimension(90, 29));
		add(btnNewButton_1,flowLayout);
		
		lblNewLabel = new JLabel("*");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setVisible(false);//xd by default the error indicator is off
		add(lblNewLabel,flowLayout);
		

	}
	
	protected void mappingToOBOROMapping(){
		if(mapping.getText().equals("RO_0002001")){
			mappingTypeOBORO.setSelectedItem("aligned with");
		}else if(mapping.getText().equals("RO_0002502")){
			mappingTypeOBORO.setSelectedItem("depends on");
		}else if(mapping.getText().equals("RO_0002424")){
			mappingTypeOBORO.setSelectedItem("differs in");
		}else if(mapping.getText().equals("RO_0000085")){
			mappingTypeOBORO.setSelectedItem("has function");
		}else if(mapping.getText().equals("RO_0001019")){
			mappingTypeOBORO.setSelectedItem("contains");
		}else if(mapping.getText().equals("RO_0001000")){
			mappingTypeOBORO.setSelectedItem("derives from");
		}else if(mapping.getText().equals("RO_0002202")){
			mappingTypeOBORO.setSelectedItem("develops from");
		}else if(mapping.getText().equals("RO_0002454")){
			mappingTypeOBORO.setSelectedItem("has host");
		}else if(mapping.getText().equals("RO_0002618")){
			mappingTypeOBORO.setSelectedItem("visits");
		}else if(mapping.getText().equals("RO_0002352")){
			mappingTypeOBORO.setSelectedItem("input of");
		}else if(mapping.getText().equals("RO_0002353")){
			mappingTypeOBORO.setSelectedItem("output of");
		}else if(mapping.getText().equals("RO_0002434")){
			mappingTypeOBORO.setSelectedItem("interacts with");
		}else if(mapping.getText().equals("RO_0001025")){
			mappingTypeOBORO.setSelectedItem("located in");
		}else if(mapping.getText().equals("BFO_0000051")){
			mappingTypeOBORO.setSelectedItem("has part");
		}else if(mapping.getText().equals("BFO_0000063")){
			mappingTypeOBORO.setSelectedItem("precedes");
		}
	}
	
	protected void setliteralDatatypeModel(){
		literalDatatype.setModel(new DefaultComboBoxModel(new String[] {
				"string",
				"boolean",
				"float",
				"double",
				"integer",
				"int",
				"long",
				"short",
				"nonNegativeInteger"
				/*********
				 * xd types are not exhausted yet!!!!!!
				 */
		}));
	}
}
