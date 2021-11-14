/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 26 avr. 2017
 * 
 */
package ecolabel.knowledgebase.context.history;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecolabel.knowledgebase.module.LocalOWLModule;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 26 avr. 2017
 * xd in the first realization of this prototype, let's say the OWLParser can read OWL axioms (OWL/XML Syntax) and translate them into OWL API operations
 */
public class OWLParser extends Parser {

	/**********************
	 * xd the concrete parsing job is supposed to be done in different parsers
	 * @param nl
	 * @param owlmgr
	 * @param owlo
	 * @return parsing result
 	 */
	public String parse(NodeList nl, OWLOntologyManager owlmgr, LocalOWLModule lom){
		String parsingResult = "Parsing result:";
		String appending = "";
		OWLDataFactory df = OWLManager.getOWLDataFactory(); 
		
		System.out.println("In OWLParser...");
		try{
			for(int q = 0; q < nl.getLength(); q ++){
				if(nl.item(q).getNodeType() != Node.COMMENT_NODE && nl.item(q).getNodeType() != Node.TEXT_NODE){//xd trim unnecessary comments and text
					appending += nodeToString(nl.item(q));
					String nodeName = nl.item(q).getNodeName();
					Node currentNode = (Node) nl.item(q); 
					System.out.println("Mapping type: " + nodeName);
					
					
//					System.out.println("Before triming node function test...");
//					NodeList oldChildren = currentNode.getChildNodes();
//					for(int i = 0; i < oldChildren.getLength(); i ++){
//						System.out.println(nodeToString(oldChildren.item(i)));
//					}
					
					currentNode = trimNode(currentNode);//xd trim off unnecessary '\t', '\n' or #comment
					
//					System.out.println("After triming node function test...");
//					NodeList newChildren = currentNode.getChildNodes();
//					for(int i = 0; i < newChildren.getLength(); i ++){
//						System.out.println(nodeToString(newChildren.item(i)));
//					}
					
					/*xd some temp variables used to construct axioms*/
					Element tempEleA, tempEleB, tempEleC, tempEleD, tempEleE;
					OWLClass tempClassA, tempClassB, tempClassC, tempClassD;
					OWLObjectProperty tempObjectPropertyA, tempObjectPropertyB, tempObjectPropertyC;
					OWLNamedIndividual tempNamedIndividualA, tempNamedIndividualB, tempNamedIndividualC;
					OWLDataProperty tempDataPropertyA, tempDataPropertyB; 
					OWLDatatype tempDatatypeA;
					OWLLiteral tempLiteralA, tempLiteralB;
					
					
					
					
					
					/***************
					 * According to different owl fragment, different mapping type, organize different axioms
					 * As wide support as possible should be provided
					 * Please refer to https://www.w3.org/TR/owl2-primer/ (show OWL/XML syntax; starts from Section 4: Classes, Properties, and Individuals – And Basic Modeling With Them )
					 */
					switch (nodeName){
					/********************
					 * xd valid 
					 *	<SubClassOf>
   							<Class IRI="Mother"/>
 							<Class IRI="Woman"/>
 						</SubClassOf>
					 */
					case "SubClassOf":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
						tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
						OWLSubClassOfAxiom tempOSCOA = df.getOWLSubClassOfAxiom(tempClassA, tempClassB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOSCOA);
						
						/****************
						 * xd the same axiom, we can also render it in Manchester syntax, the reverse must works too. 
						 */
						//ManchesterOWLSyntaxOWLObjectRendererImpl  manchesterRenderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
						//System.out.println("TEST: Changing OWL/XML to Manchester syntax: " + manchesterRenderer.render(tempOSCOA));
						break;
						
					/***************
					 * 
					 * <ClassAssertion>
					 * 		<Class IRI="Person"/>
    						<NamedIndividual IRI="Mary"/>
 						</ClassAssertion>
					 */
					case "ClassAssertion":
						Element tempEleClass = (Element) currentNode.getFirstChild();//xd first node
						Element tempEleIndividual = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						OWLClass tempClass = df.getOWLClass(IRI.create(tempEleClass.getAttribute("IRI")));
						OWLIndividual tempIndividual = df.getOWLNamedIndividual(IRI.create(tempEleIndividual.getAttribute("IRI")));
						OWLClassAssertionAxiom tempOCAA = df.getOWLClassAssertionAxiom(tempClass,tempIndividual);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOCAA);
						break;
						
					/**********
					 *
					 * 	<EquivalentClasses>
   							<Class IRI="Person"/>
   							<Class IRI="Human"/>
 						</EquivalentClasses>
					 */
					case "EquivalentClasses"://xd coming soon
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
						tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
						OWLEquivalentClassesAxiom tempOECA = df.getOWLEquivalentClassesAxiom(tempClassA,tempClassB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOECA);
						break;
						
						
					/******************
					 *
					 * 	<DisjointClasses>
 							<Class IRI="Woman"/>
 							<Class IRI="Man"/>
						</DisjointClasses>
					 */
					case "DisjointClasses":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
						tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
						OWLDisjointClassesAxiom tempODCA = df.getOWLDisjointClassesAxiom(tempClassA, tempClassB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempODCA);
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
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
						tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
						tempNamedIndividualC = df.getOWLNamedIndividual(IRI.create(tempEleC.getAttribute("IRI")));
						OWLObjectPropertyAssertionAxiom tempOOPAA = df.getOWLObjectPropertyAssertionAxiom(tempObjectPropertyA, tempNamedIndividualB, tempNamedIndividualC);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOOPAA);
						break;
						
						
					/******************
					 * 	<NegativeObjectPropertyAssertion>
							<ObjectProperty IRI="hasWife"/>
							<NamedIndividual IRI="Bill"/>
							<NamedIndividual IRI="Mary"/>
						</NegativeObjectPropertyAssertion>
					 */
					case "NegativeObjectPropertyAssertion":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
						tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
						tempNamedIndividualC = df.getOWLNamedIndividual(IRI.create(tempEleC.getAttribute("IRI")));
						OWLNegativeObjectPropertyAssertionAxiom tempONOPAA = df.getOWLNegativeObjectPropertyAssertionAxiom(tempObjectPropertyA, tempNamedIndividualB, tempNamedIndividualC);
						owlmgr.addAxiom(lom.getOwlOntology(), tempONOPAA);
						break;
						
					/***********************
					 * 	<SubObjectPropertyOf>
							<ObjectProperty IRI="hasWife"/>
							<ObjectProperty IRI="hasSpouse"/>
						</SubObjectPropertyOf>
					 */
					case "SubObjectPropertyOf":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempObjectPropertyB = df.getOWLObjectProperty(IRI.create(tempEleB.getAttribute("IRI")));
						OWLSubObjectPropertyOfAxiom tempOSPOA = df.getOWLSubObjectPropertyOfAxiom(tempObjectPropertyA, tempObjectPropertyB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOSPOA);
						break;
						
					/****************
					 * 	<ObjectPropertyDomain>
   							<ObjectProperty IRI="hasWife"/>
   							<Class IRI="Man"/>
 						</ObjectPropertyDomain>
					 */
					case "ObjectPropertyDomain":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
						OWLObjectPropertyDomainAxiom tempOOPDA = df.getOWLObjectPropertyDomainAxiom(tempObjectPropertyA, tempClassB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOOPDA);
						break;
						
						
					/******************
					 * 	<ObjectPropertyRange>
   							<ObjectProperty IRI="hasWife"/>
   							<Class IRI="Woman"/>
 						</ObjectPropertyRange>
					 */
					case "ObjectPropertyRange":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
						OWLObjectPropertyRangeAxiom tempOOPRA = df.getOWLObjectPropertyRangeAxiom(tempObjectPropertyA, tempClassB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOOPRA);
						break;
						
						
					/*****************
					 * 	<DifferentIndividuals>
   							<NamedIndividual IRI="John"/>
   							<NamedIndividual IRI="Bill"/>
 						</DifferentIndividuals>
					 */
					case "DifferentIndividuals":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempNamedIndividualA = df.getOWLNamedIndividual(IRI.create(tempEleA.getAttribute("IRI")));
						tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
						OWLDifferentIndividualsAxiom tempODIA = df.getOWLDifferentIndividualsAxiom(tempNamedIndividualA, tempNamedIndividualB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempODIA);
						break;
						
					
					/*****************
					 *
					 * 	<SameIndividual>
   							<NamedIndividual IRI="James"/>
   							<NamedIndividual IRI="Jim"/>
 						</SameIndividual>
					 */
					case "SameIndividual":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempNamedIndividualA = df.getOWLNamedIndividual(IRI.create(tempEleA.getAttribute("IRI")));
						tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
						OWLSameIndividualAxiom tempOSIA = df.getOWLSameIndividualAxiom(tempNamedIndividualA, tempNamedIndividualB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempOSIA);
						break;
						
						
						
					/***********
					 * 
					 * 	<DataPropertyAssertion>
   							<DataProperty IRI="hasAge"/>
   							<NamedIndividual IRI="John"/>
   							<Literal datatypeIRI="http://www.w3.org/2001/XMLSchema#integer">51</Literal>
 						</DataPropertyAssertion>
					 */
					case "DataPropertyAssertion"://xd should recognize the type of this literal maybe? 
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
						tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
						/*xd be careful that here the value of this literal is not sure*/
						tempLiteralA = getOWLLiteral(df, tempEleC);//xd the value of this node could be String, double, integer or anything.
						OWLDataPropertyAssertionAxiom tempODPAA = df.getOWLDataPropertyAssertionAxiom(tempDataPropertyA, tempNamedIndividualB, tempLiteralA);
						owlmgr.addAxiom(lom.getOwlOntology(), tempODPAA);
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
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempEleC = (Element) currentNode.getFirstChild().getNextSibling().getNextSibling();//xd third node
						tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
						/*xd be careful that here the value of this literal is not sure*/
						tempLiteralA = getOWLLiteral(df, tempEleC);//xd the value of this node could be String, double, integer or anything.
						OWLNegativeDataPropertyAssertionAxiom tempONDPAA = df.getOWLNegativeDataPropertyAssertionAxiom(tempDataPropertyA, tempNamedIndividualB, tempLiteralA);
						owlmgr.addAxiom(lom.getOwlOntology(), tempONDPAA);
						break;
						
						
					/**************
					 *
					 * 	<DataPropertyDomain>
   							<DataProperty IRI="hasAge"/>
   							<Class IRI="Person"/>
						</DataPropertyDomain>
					 */
					case "DataPropertyDomain":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
						OWLDataPropertyDomainAxiom tempODPDA = df.getOWLDataPropertyDomainAxiom(tempDataPropertyA, tempClassB);
						owlmgr.addAxiom(lom.getOwlOntology(), tempODPDA);
						break;
						
						
					/******************
					 *
					 * 	<DataPropertyRange>
   							<DataProperty IRI="hasAge"/>
   							<Datatype IRI="http://www.w3.org/2001/XMLSchema#nonNegativeInteger"/>
						</DataPropertyRange>
					 */
					case "DataPropertyRange":
						tempEleA = (Element) currentNode.getFirstChild();//xd first node
						tempEleB = (Element) currentNode.getFirstChild().getNextSibling();//xd second node
						tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
						tempDatatypeA = df.getOWLDatatype(IRI.create(tempEleB.getAttribute("IRI")));
						OWLDataPropertyRangeAxiom tempODPRA = df.getOWLDataPropertyRangeAxiom(tempDataPropertyA, tempDatatypeA);
						owlmgr.addAxiom(lom.getOwlOntology(), tempODPRA);
						break;
						
						
						
					//xd from here on, we reach section 5 Advanced Class Relationships
						
//					case "":
//						break;
//						
//					case "":
//						break;
//						
//					case "":
//						break;
//						
//					case "":
//						break;
//						
//					case "":
//						break;
						
						
						
						
						
					}
					
				}
			}
			
			System.out.println("Parsed content: " + appending); 
			return parsingResult += "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return parsingResult += "failure";
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
	
}
