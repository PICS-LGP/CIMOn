/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 29 mai 2017
 * 
 */
package ecolabel.protege.plugin.component;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.io.StringWriter;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecolabel.knowledgebase.context.mapping.MappingOWLAdapter;
import ecolabel.knowledgebase.module.LocalOWLModule;
import ecolabel.protege.plugin.transferHandler.TextFieldTransferHandler;

import java.awt.Dimension;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.awt.Container;

import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.UIManager;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 29 mai 2017
 * xd notice that, this class is not a good design, because we have integrated some relationship from OBO Relationship Ontology(ro.owl) in this component
 * It will be difficult to apply other change and maintenance. I've come up with a solution that letting BinaryMappingComponentOBORO to inherit this class. In this
 * BinaryMappingComponentOBORO class the extra visual component "JComboBox mappingTypeOBORO" is defined, unfortunately, BinaryMappingComponentOBORO is wrongly visualized.
 * Normally, BinaryMappingComponentOBORO should have a flow layout, while it the components are out of position. It feels strange to me, and I didn't find solution for this inherit problem. 
 * As a result, this degenerated design and code is applied for now.
 */
public class BinaryMappingComponent extends JPanel {
	
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
	//private JTextField mappingOBORO;
	/**
	 * Create the panel.
	 */
	public BinaryMappingComponent() {
		setMaximumSize(new Dimension(1500, 40));
		setSize(new Dimension(1000, 40));
		setPreferredSize(new Dimension(1000, 40));
		setBackground(SystemColor.info);
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		
		
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
		add(mappingType);
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
		add(mapping);
		mapping.setColumns(10);
		
		mappingTypeOBORO = new JComboBox();
		mappingTypeOBORO.setVisible(false);//xd by default the OBORO relations are not visible
		add(mappingTypeOBORO);
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
		
		add(tfTail);
		tfTail.setColumns(10);
		
		
		literalDatatype = new JComboBox();
		literalDatatype.setVisible(false);//xd by default this component is not visible, only visible when editing
		setliteralDatatypeModel();
		literalDatatype.setSelectedItem("string");
		literalDatatype.setPreferredSize(new Dimension(130, 26));
		add(literalDatatype);//xd add this combobox right before the edit and remove button
		
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
		add(btnNewButton);
		
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
		add(btnNewButton_1);
		
		lblNewLabel = new JLabel("*");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setVisible(false);//xd by default the error indicator is off
		add(lblNewLabel);
		

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
	
	
	
	
	/******
	 * xd this function is old design. Abandoned. please refer to MappingOWLAdapter
	 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
	 * @time: 19 juin 2017
	 *
	 */
//	public void renderMapping(NodeList nl /*OWLOntologyManager owlmgr, LocalOWLModule lom*/){
//		
//		String appending = "";
//		OWLDataFactory df = OWLManager.getOWLDataFactory(); 
//		
//		System.out.println("In OWLParser...");
//		try{
//			for(int q = 0; q < nl.getLength(); q ++){
//				if(nl.item(q).getNodeType() != Node.COMMENT_NODE && nl.item(q).getNodeType() != Node.TEXT_NODE){//xd trim unnecessary comments and text
//					appending += nodeToString(nl.item(q));
//					String nodeName = nl.item(q).getNodeName();
//					Node currentNode = (Node) nl.item(q); 
//					System.out.println("Mapping type: " + nodeName);
//					
//					currentNode = MappingOWLAdapter.trimNode(currentNode);//xd trim off unnecessary '\t', '\n' or #comment
//
//					Element tempEleA, tempEleB, tempEleC, tempEleD, tempEleE;
//					OWLClass tempClassA, tempClassB, tempClassC, tempClassD;
//					OWLObjectProperty tempObjectPropertyA, tempObjectPropertyB, tempObjectPropertyC;
//					OWLNamedIndividual tempNamedIndividualA, tempNamedIndividualB, tempNamedIndividualC;
//					OWLDataProperty tempDataPropertyA, tempDataPropertyB;
//					OWLDatatype tempDatatypeA;
//					OWLLiteral tempLiteralA, tempLiteralB;
//					String tempString;
//
//					
//					switch (nodeName){
//					/********************
//					 * xd valid 
//					 *	<SubClassOf>
//   							<Class IRI="Mother"/>
// 							<Class IRI="Woman"/>
// 						</SubClassOf>
//					 */
//					case "SubClassOf":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						mappingType.setSelectedItem("SubClassOf");//xd set the selected mapping type 
//						lbMappingType.setText("SubClassOf");
//						mapping.setVisible(false);
//						
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//xd no need for mapping field(because the mapping type is SubClassOf, we don't need a third field here)
//						
//						//tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLSubClassOfAxiom tempOSCOA = df.getOWLSubClassOfAxiom(tempClassA, tempClassB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOSCOA);
//						
//						/****************
//						 * xd the same axiom, we can also render it in Manchester syntax, the reverse must works too. 
//						 */
//						//ManchesterOWLSyntaxOWLObjectRendererImpl  manchesterRenderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
//						//System.out.println("TEST: Changing OWL/XML to Manchester syntax: " + manchesterRenderer.render(tempOSCOA));
//						break;
//						
//					/***************
//					 * 
//					 * <ClassAssertion>
//					 * 		<Class IRI="Person"/>
//    						<NamedIndividual IRI="Mary"/>
// 						</ClassAssertion>
//					 */
//					case "ClassAssertion":
//						Element tempEleClass = (Element) currentNode.getFirstChild();//xd first node
//						Element tempEleIndividual = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleClass.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						mappingType.setSelectedItem("ClassAssertion");//xd set the selected mapping type 
//						lbMappingType.setText("ClassAssertion");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleIndividual.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						
//						//OWLClass tempClass = df.getOWLClass(IRI.create(tempEleClass.getAttribute("IRI")));
//						//OWLIndividual tempIndividual = df.getOWLNamedIndividual(IRI.create(tempEleIndividual.getAttribute("IRI")));
//						//OWLClassAssertionAxiom tempOCAA = df.getOWLClassAssertionAxiom(tempClass,tempIndividual);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOCAA);
//						break;
//						
//					/**********
//					 *
//					 * 	<EquivalentClasses>
//   							<Class IRI="Person"/>
//   							<Class IRI="Human"/>
// 						</EquivalentClasses>
//					 */
//					case "EquivalentClasses"://xd coming soon
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						mappingType.setSelectedItem("EquivalentClasses");//xd set the selected mapping type 
//						lbMappingType.setText("EquivalentClasses");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLEquivalentClassesAxiom tempOECA = df.getOWLEquivalentClassesAxiom(tempClassA,tempClassB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOECA);
//						break;
//						
//						
//					/******************
//					 *
//					 * 	<DisjointClasses>
// 							<Class IRI="Woman"/>
// 							<Class IRI="Man"/>
//						</DisjointClasses>
//					 */
//					case "DisjointClasses":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						
//						mappingType.setSelectedItem("DisjointClasses");//xd set the selected mapping type 
//						lbMappingType.setText("DisjointClasses");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLDisjointClassesAxiom tempODCA = df.getOWLDisjointClassesAxiom(tempClassA, tempClassB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempODCA);
//						break;
//						
//					/**********************
//					 *
//					 * 	<ObjectPropertyAssertion>
//							<ObjectProperty IRI="hasWife"/>
//							<NamedIndividual IRI="John"/>
//							<NamedIndividual IRI="Mary"/>
//						</ObjectPropertyAssertion>
//					 */
//					case "ObjectPropertyAssertion":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						mapping.setToolTipText(tempString);//xd set the tool tip
//						mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						mapping.setVisible(true);
//						mappingType.setSelectedItem("ObjectPropertyAssertion");//xd set the selected mapping type 
//						lbMappingType.setText("ObjectPropertyAssertion");
//						
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						tempString = IRI.create(tempEleC.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						//tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
//						//tempNamedIndividualC = df.getOWLNamedIndividual(IRI.create(tempEleC.getAttribute("IRI")));
//						//OWLObjectPropertyAssertionAxiom tempOOPAA = df.getOWLObjectPropertyAssertionAxiom(tempObjectPropertyA, tempNamedIndividualB, tempNamedIndividualC);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOOPAA);
//						break;
//						
//						
//					/******************
//					 * 	<NegativeObjectPropertyAssertion>
//							<ObjectProperty IRI="hasWife"/>
//							<NamedIndividual IRI="Bill"/>
//							<NamedIndividual IRI="Mary"/>
//						</NegativeObjectPropertyAssertion>
//					 */
//					case "NegativeObjectPropertyAssertion":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						mapping.setToolTipText(tempString);//xd set the tool tip
//						mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						mapping.setVisible(true);
//						mappingType.setSelectedItem("NegativeObjectPropertyAssertion");//xd set the selected mapping type 
//						lbMappingType.setText("NegativeObjectPropertyAssertion");
//						
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						tempString = IRI.create(tempEleC.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						//tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
//						//tempNamedIndividualC = df.getOWLNamedIndividual(IRI.create(tempEleC.getAttribute("IRI")));
//						//OWLNegativeObjectPropertyAssertionAxiom tempONOPAA = df.getOWLNegativeObjectPropertyAssertionAxiom(tempObjectPropertyA, tempNamedIndividualB, tempNamedIndividualC);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempONOPAA);
//						break;
//						
//					/***********************
//					 * 	<SubObjectPropertyOf>
//							<ObjectProperty IRI="hasWife"/>
//							<ObjectProperty IRI="hasSpouse"/>
//						</SubObjectPropertyOf>
//					 */
//					case "SubObjectPropertyOf":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						
//						mappingType.setSelectedItem("SubObjectPropertyOf");//xd set the selected mapping type 
//						lbMappingType.setText("SubObjectPropertyOf");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempObjectPropertyB = df.getOWLObjectProperty(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLSubObjectPropertyOfAxiom tempOSPOA = df.getOWLSubObjectPropertyOfAxiom(tempObjectPropertyA, tempObjectPropertyB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOSPOA);
//						break;
//						
//					/****************
//					 * 	<ObjectPropertyDomain>
//   							<ObjectProperty IRI="hasWife"/>
//   							<Class IRI="Man"/>
// 						</ObjectPropertyDomain>
//					 */
//					case "ObjectPropertyDomain":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						
//						mappingType.setSelectedItem("ObjectPropertyDomain");//xd set the selected mapping type 
//						lbMappingType.setText("ObjectPropertyDomain");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLObjectPropertyDomainAxiom tempOOPDA = df.getOWLObjectPropertyDomainAxiom(tempObjectPropertyA, tempClassB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOOPDA);
//						break;
//						
//						
//					/******************
//					 * 	<ObjectPropertyRange>
//   							<ObjectProperty IRI="hasWife"/>
//   							<Class IRI="Woman"/>
// 						</ObjectPropertyRange>
//					 */
//					case "ObjectPropertyRange":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						
//						mappingType.setSelectedItem("ObjectPropertyRange");//xd set the selected mapping type 
//						lbMappingType.setText("ObjectPropertyRange");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLObjectPropertyRangeAxiom tempOOPRA = df.getOWLObjectPropertyRangeAxiom(tempObjectPropertyA, tempClassB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOOPRA);
//						break;
//						
//						
//					/*****************
//					 * 	<DifferentIndividuals>
//   							<NamedIndividual IRI="John"/>
//   							<NamedIndividual IRI="Bill"/>
// 						</DifferentIndividuals>
//					 */
//					case "DifferentIndividuals":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						
//						mappingType.setSelectedItem("DifferentIndividuals");//xd set the selected mapping type 
//						lbMappingType.setText("DifferentIndividuals");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//tempNamedIndividualA = df.getOWLNamedIndividual(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLDifferentIndividualsAxiom tempODIA = df.getOWLDifferentIndividualsAxiom(tempNamedIndividualA, tempNamedIndividualB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempODIA);
//						break;
//						
//					
//					/*****************
//					 *
//					 * 	<SameIndividual>
//   							<NamedIndividual IRI="James"/>
//   							<NamedIndividual IRI="Jim"/>
// 						</SameIndividual>
//					 */
//					case "SameIndividual":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						
//						mappingType.setSelectedItem("SameIndividual");//xd set the selected mapping type 
//						lbMappingType.setText("SameIndividual");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						//tempNamedIndividualA = df.getOWLNamedIndividual(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLSameIndividualAxiom tempOSIA = df.getOWLSameIndividualAxiom(tempNamedIndividualA, tempNamedIndividualB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempOSIA);
//						break;
//						
//						
//						
//					/***********
//					 * 
//					 * 	<DataPropertyAssertion>
//   							<DataProperty IRI="hasAge"/>
//   							<NamedIndividual IRI="John"/>
//   							<Literal datatypeIRI="http://www.w3.org/2001/XMLSchema#integer">51</Literal>
// 						</DataPropertyAssertion>
//					 */
//					case "DataPropertyAssertion"://xd should recognize the type of this literal maybe? 
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
//						
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						mapping.setToolTipText(tempString);//xd set the tool tip
//						mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						mapping.setVisible(true);
//						mappingType.setSelectedItem("DataPropertyAssertion");//xd set the selected mapping type 
//						lbMappingType.setText("DataPropertyAssertion");
//						
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						tempString = IRI.create(tempEleC.getAttribute("datatypeIRI")).toString();
//						tfTail.setToolTipText(tempEleC.getNodeValue());
//						tfTail.setText(tempEleC.getNodeValue());
//						literalDatatype.setSelectedItem((tempString.substring(tempString.lastIndexOf("#") + 1)));//xd add extra datatype chooser to this mapping editor
//						literalDatatype.setVisible(true);
//						
//						
//						//tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
//						/*xd be careful that here the value of this literal is not sure*/
//						//tempLiteralA = getOWLLiteral(df, tempEleC);//xd the value of this node could be String, double, integer or anything.
//						//OWLDataPropertyAssertionAxiom tempODPAA = df.getOWLDataPropertyAssertionAxiom(tempDataPropertyA, tempNamedIndividualB, tempLiteralA);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempODPAA);
//						break;
//						
//						
//					/*************
//					 *
//					 * 	<NegativeDataPropertyAssertion>
//	   						<DataProperty IRI="hasAge"/>
//	   						<NamedIndividual IRI="Jack"/>
//	   						<Literal datatypeIRI="http://www.w3.org/2001/XMLSchema#integer">53</Literal>
// 						</NegativeDataPropertyAssertion>
//					 */
//					case "NegativeDataPropertyAssertion":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						mapping.setToolTipText(tempString);//xd set the tool tip
//						mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						mapping.setVisible(true);
//						mappingType.setSelectedItem("NegativeDataPropertyAssertion");//xd set the selected mapping type 
//						lbMappingType.setText("NegativeDataPropertyAssertion");
//						
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						tempString = IRI.create(tempEleC.getAttribute("datatypeIRI")).toString();
//						tfTail.setToolTipText(tempEleC.getNodeValue());
//						tfTail.setText(tempEleC.getNodeValue());
//						literalDatatype.setSelectedItem((tempString.substring(tempString.lastIndexOf("#") + 1)));//xd add extra datatype chooser to this mapping editor
//						literalDatatype.setVisible(true);
//						
//						
//						//tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
//						/*xd be careful that here the value of this literal is not sure*/
//						//tempLiteralA = getOWLLiteral(df, tempEleC);//xd the value of this node could be String, double, integer or anything.
//						//OWLNegativeDataPropertyAssertionAxiom tempONDPAA = df.getOWLNegativeDataPropertyAssertionAxiom(tempDataPropertyA, tempNamedIndividualB, tempLiteralA);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempONDPAA);
//						break;
//						
//						
//					/**************
//					 *
//					 * 	<DataPropertyDomain>
//   							<DataProperty IRI="hasAge"/>
//   							<Class IRI="Person"/>
//						</DataPropertyDomain>
//					 */
//					case "DataPropertyDomain":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
//						
//						mappingType.setSelectedItem("DataPropertyDomain");//xd set the selected mapping type 
//						lbMappingType.setText("DataPropertyDomain");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
//						tfTail.setToolTipText(tempString);
//						tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
//						
//						
//						//tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLDataPropertyDomainAxiom tempODPDA = df.getOWLDataPropertyDomainAxiom(tempDataPropertyA, tempClassB);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempODPDA);
//						break;
//						
//						
//					/******************
//					 *
//					 * 	<DataPropertyRange>
//   							<DataProperty IRI="hasAge"/>
//   							<Datatype IRI="http://www.w3.org/2001/XMLSchema#nonNegativeInteger"/>
//						</DataPropertyRange>
//					 */
//					case "DataPropertyRange":
//						tempEleA = (Element) currentNode.getFirstChild();//xd first node
//						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
//						
////						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
////						mapping.setToolTipText(tempString);//xd set the tool tip
////						mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						mappingType.setSelectedItem("DataPropertyRange");//xd set the selected mapping type 
//						lbMappingType.setText("DataPropertyRange");
//						mapping.setVisible(false);
//						tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//						tfHead.setToolTipText(tempString);//xd set the tool tip
//						tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
//						
//						tempString = IRI.create(tempEleB.getAttribute("datatypeIRI")).toString();
//						//tfTail.setToolTipText(tempEleB.getNodeValue());
//						//tfTail.setText(tempEleB.getNodeValue());
//						tfTail.setVisible(false);
//						literalDatatype.setSelectedItem((tempString.substring(tempString.lastIndexOf("#") + 1)));//xd add extra datatype chooser to this mapping editor
//						literalDatatype.setVisible(true);
//						
//						//tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
//						//tempDatatypeA = df.getOWLDatatype(IRI.create(tempEleB.getAttribute("IRI")));
//						//OWLDataPropertyRangeAxiom tempODPRA = df.getOWLDataPropertyRangeAxiom(tempDataPropertyA, tempDatatypeA);
//						//owlmgr.addAxiom(lom.getOwlOntology(), tempODPRA);
//						break;
//
//					//xd from here on, we reach section 5 Advanced Class Relationships
//						
//					}
//					
//				}
//			}
//			
//			System.out.println("Parsed content: " + appending); 
//			//return parsingResult += "success";
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		//return parsingResult += "failure";
//	}

	
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
	
	
	protected static String nodeToString(Node node) throws TransformerException
	{
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return(buf.toString());
	}
	
	/**********
	 * xd return different types of OWLLiterals, not finished yet!!!!!!
	 * We assume that the para Element ele is not empty, refer to https://www.w3.org/2001/XMLSchema-datatypes for all the supported types
	 * @param ele
	 * @return
	 */
	public OWLLiteral getOWLLiteral(OWLDataFactory df, Element ele){
		OWLLiteral ol = df.getOWLLiteral("");//xd initialization by null
		if(ele.getNodeType() == Node.ELEMENT_NODE && ele.hasAttribute("datatypeIRI") && ele.getNodeValue() != ""){
			String datatypeIRI = ele.getAttribute("datatypeIRI");
			
			String datatype = datatypeIRI.substring(datatypeIRI.lastIndexOf("#"));//xd get the datatype name
			if(datatype.equals("string")){
				ol = df.getOWLLiteral(ele.getNodeValue());
			}else if(datatype.equals("boolean")){
				ol = df.getOWLLiteral(Boolean.parseBoolean(ele.getNodeValue()));
			}else if(datatype.equals("float")){
				ol = df.getOWLLiteral(Float.parseFloat(ele.getNodeValue()));
			}else if(datatype.equals("double")){
				ol = df.getOWLLiteral(Double.parseDouble(ele.getNodeValue()));
			}/*else if(datatype.equals("decimal")){
				
			}*//*else if(datatype.equals("dateTime")){
				
			}*/
			//......
			else if(datatype.equals("integer")||datatype.equals("nonNegativeInteger")){
				ol = df.getOWLLiteral(Integer.parseInt(ele.getNodeValue()));
			}else if(datatype.equals("int")){
				ol = df.getOWLLiteral(Integer.parseInt(ele.getNodeValue()));
			}else if(datatype.equals("long")){
				ol = df.getOWLLiteral(Long.parseLong(ele.getNodeValue()));
			}else if(datatype.equals("short")){
				ol = df.getOWLLiteral(Short.parseShort(ele.getNodeValue()));
			}
			
			
			
			/*********
			 * xd types are not exhausted yet!!!!!!
			 */
			
			
		}
		return ol;
		
		
	}
	
	public void ErrorSwitchOn(String tooltip){
		lblNewLabel.setVisible(true);
		setToolTipText(tooltip);
		//xd set necessary tooltip in the ContextChecker
		
	}
	
	public void ErrorSwitchOff(){
		setToolTipText("");
		lblNewLabel.setVisible(false);
	}
}
