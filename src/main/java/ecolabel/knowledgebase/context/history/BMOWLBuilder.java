/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 8 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ecolabel.protege.plugin.component.BinaryMappingComponent;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 8 juin 2017
 * 
 */

/******
 * xd this class is old design. Abandoned. please refer to MappingOWLAdapter
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 19 juin 2017
 *
 */
public class BMOWLBuilder extends BMBuilder {

	public Element buildRealBinaryMapping(BinaryMappingComponent bmc, Document doc){
		//xd the behavier of this function is somehow like the reverse of Parser
		String type = bmc.lbMappingType.getText();
		Element realMapping = doc.createElement(type);
		
		switch(type){
			
			/********************
			 * xd valid 
			 *	<SubClassOf>
					<Class IRI="Mother"/>
					<Class IRI="Woman"/>
				</SubClassOf>
			 */
			case "SubClassOf":
				Element eleClassA = doc.createElement("Class");
				eleClassA.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleClassB = doc.createElement("Class");
				eleClassB.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleClassA);
				realMapping.appendChild(eleClassB);
				break;
		
			
			/***************
			 * 
			 * <ClassAssertion>
			 * 		<Class IRI="Person"/>
					<NamedIndividual IRI="Mary"/>
				</ClassAssertion>
			 */
			case "ClassAssertion":
				Element eleClass = doc.createElement("Class");
				eleClass.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleNamedIndividual = doc.createElement("NamedIndividual");
				eleNamedIndividual.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleClass);
				realMapping.appendChild(eleNamedIndividual);
				break;
				
			/**********
			 *
			 * 	<EquivalentClasses>
					<Class IRI="Person"/>
					<Class IRI="Human"/>
				</EquivalentClasses>
			 */
			case "EquivalentClasses":
				Element eleClassA1 = doc.createElement("Class");
				eleClassA1.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleClassB1 = doc.createElement("Class");
				eleClassB1.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleClassA1);
				realMapping.appendChild(eleClassB1);
				break;
				
			/******************
			 *
			 * 	<DisjointClasses>
					<Class IRI="Woman"/>
					<Class IRI="Man"/>
				</DisjointClasses>
			 */
			case "DisjointClasses":
				Element eleClassA2 = doc.createElement("Class");
				eleClassA2.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleClassB2 = doc.createElement("Class");
				eleClassB2.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleClassA2);
				realMapping.appendChild(eleClassB2);
				break;
				
			/**********************
			 *
			 * 	<ObjectPropertyAssertion>
					<ObjectProperty IRI="hasWife"/>
					<NamedIndividual IRI="John"/>
					<NamedIndividual IRI="Mary"/>
				</ObjectPropertyAssertion>
			 */
			case "ObjectPropertyAssertion":
				Element eleObjectProperty = doc.createElement("ObjectProperty");
				eleObjectProperty.setAttribute("IRI", bmc.mapping.getToolTipText());
				Element eleIndividual1 = doc.createElement("NamedIndividual");
				eleIndividual1.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleIndividual2 = doc.createElement("NamedIndividual");
				eleIndividual2.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleObjectProperty);
				realMapping.appendChild(eleIndividual1);
				realMapping.appendChild(eleIndividual2);
				break;
				
			/******************
			 * 	<NegativeObjectPropertyAssertion>
					<ObjectProperty IRI="hasWife"/>
					<NamedIndividual IRI="Bill"/>
					<NamedIndividual IRI="Mary"/>
				</NegativeObjectPropertyAssertion>
			 */	
			case "NegativeObjectPropertyAssertion":
				Element eleObjectProperty1 = doc.createElement("ObjectProperty");
				eleObjectProperty1.setAttribute("IRI", bmc.mapping.getToolTipText());
				Element eleIndividual3 = doc.createElement("NamedIndividual");
				eleIndividual3.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleIndividual4 = doc.createElement("NamedIndividual");
				eleIndividual4.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleObjectProperty1);
				realMapping.appendChild(eleIndividual3);
				realMapping.appendChild(eleIndividual4);
				break;
				
			/***********************
			 * 	<SubObjectPropertyOf>
					<ObjectProperty IRI="hasWife"/>
					<ObjectProperty IRI="hasSpouse"/>
				</SubObjectPropertyOf>
			 */
			case "SubObjectPropertyOf":
				Element eleObjectProperty2 = doc.createElement("ObjectProperty");
				eleObjectProperty2.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleObjectProperty3 = doc.createElement("ObjectProperty");
				eleObjectProperty3.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleObjectProperty2);
				realMapping.appendChild(eleObjectProperty3);
				break;
				
			/****************
			 * 	<ObjectPropertyDomain>
					<ObjectProperty IRI="hasWife"/>
					<Class IRI="Man"/>
				</ObjectPropertyDomain>
			 */
			case "ObjectPropertyDomain":
				Element eleObjectProperty4 = doc.createElement("ObjectProperty");
				eleObjectProperty4.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleClass3 = doc.createElement("Class");
				eleClass3.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleObjectProperty4);
				realMapping.appendChild(eleClass3);
				break;
				
			/******************
			 * 	<ObjectPropertyRange>
					<ObjectProperty IRI="hasWife"/>
					<Class IRI="Woman"/>
				</ObjectPropertyRange>
			 */
			case "ObjectPropertyRange":
				Element eleObjectProperty5 = doc.createElement("ObjectProperty");
				eleObjectProperty5.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleClass4 = doc.createElement("Class");
				eleClass4.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleObjectProperty5);
				realMapping.appendChild(eleClass4);
				break;
				
			/*****************
			 * 	<DifferentIndividuals>
					<NamedIndividual IRI="John"/>
					<NamedIndividual IRI="Bill"/>
				</DifferentIndividuals>
			 */
			case "DifferentIndividuals":
				Element eleIndividual5 = doc.createElement("NamedIndividual");
				eleIndividual5.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleIndividual6 = doc.createElement("NamedIndividual");
				eleIndividual6.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleIndividual5);
				realMapping.appendChild(eleIndividual6);
				break;
				
			/*****************
			 *
			 * 	<SameIndividual>
					<NamedIndividual IRI="James"/>
					<NamedIndividual IRI="Jim"/>
				</SameIndividual>
			 */
			case "SameIndividual":
				Element eleIndividual7 = doc.createElement("NamedIndividual");
				eleIndividual7.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleIndividual8 = doc.createElement("NamedIndividual");
				eleIndividual8.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleIndividual7);
				realMapping.appendChild(eleIndividual8);
				break;
				
			/***********
			 * 
			 * 	<DataPropertyAssertion>
					<DataProperty IRI="hasAge"/>
					<NamedIndividual IRI="John"/>
					<Literal datatypeIRI="http://www.w3.org/2001/XMLSchema#integer">51</Literal>
				</DataPropertyAssertion>
			 */
			case "DataPropertyAssertion":
				Element eleDataProperty = doc.createElement("DataProperty");
				eleDataProperty.setAttribute("IRI", bmc.mapping.getToolTipText());
				Element eleIndividual9 = doc.createElement("NamedIndividual");
				eleIndividual9.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleLiteral = doc.createElement("Literal");
				eleLiteral.setAttribute("datatypeIRI", "http://www.w3.org/2001/XMLSchema#"+bmc.literalDatatype.getSelectedItem().toString());
				eleLiteral.setNodeValue(bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleDataProperty);
				realMapping.appendChild(eleIndividual9);
				realMapping.appendChild(eleLiteral);
				break;
				
				
				
			/*************
			 *
			 * 	<NegativeDataPropertyAssertion>
					<DataProperty IRI="hasAge"/>
					<NamedIndividual IRI="Jack"/>
					<Literal datatypeIRI="http://www.w3.org/2001/XMLSchema#integer">53</Literal>
				</NegativeDataPropertyAssertion>
			 */
			case "NegativeDataPropertyAssertion":
				Element eleDataProperty1 = doc.createElement("DataProperty");
				eleDataProperty1.setAttribute("IRI", bmc.mapping.getToolTipText());
				Element eleIndividual10 = doc.createElement("NamedIndividual");
				eleIndividual10.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleLiteral1 = doc.createElement("Literal");
				eleLiteral1.setAttribute("datatypeIRI", "http://www.w3.org/2001/XMLSchema#"+bmc.literalDatatype.getSelectedItem().toString());
				eleLiteral1.setNodeValue(bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleDataProperty1);
				realMapping.appendChild(eleIndividual10);
				realMapping.appendChild(eleLiteral1);
				break;
				
			/**************
			 *
			 * 	<DataPropertyDomain>
					<DataProperty IRI="hasAge"/>
					<Class IRI="Person"/>
				</DataPropertyDomain>
			 */
			case "DataPropertyDomain":
				Element eleDataProperty2 = doc.createElement("DataProperty");
				eleDataProperty2.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleClass5 = doc.createElement("Class");
				eleClass5.setAttribute("IRI", bmc.tfTail.getToolTipText());
				realMapping.appendChild(eleDataProperty2);
				realMapping.appendChild(eleClass5);
				break;
				
			/******************
			 *
			 * 	<DataPropertyRange>
					<DataProperty IRI="hasAge"/>
					<Datatype IRI="http://www.w3.org/2001/XMLSchema#nonNegativeInteger"/>
				</DataPropertyRange>
			 */
			case "DataPropertyRange":
				Element eleDataProperty3 = doc.createElement("DataProperty");
				eleDataProperty3.setAttribute("IRI", bmc.tfHead.getToolTipText());
				Element eleDatatype = doc.createElement("Datatype");
				eleDatatype.setAttribute("IRI", "http://www.w3.org/2001/XMLSchema#"+bmc.literalDatatype.getSelectedItem().toString());
				realMapping.appendChild(eleDataProperty3);
				realMapping.appendChild(eleDatatype);
				break;
			}
		
		
		
		return realMapping;
	}
}
