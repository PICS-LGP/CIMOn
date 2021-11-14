/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 19 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ecolabel.knowledgebase.module.LocalOWLModule;
import ecolabel.protege.plugin.component.BinaryMappingComponent;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 19 juin 2017
 * 
 */

/******************
 * 	<NegativeObjectPropertyAssertion>
		<ObjectProperty IRI="hasWife"/>
		<NamedIndividual IRI="Bill"/>
		<NamedIndividual IRI="Mary"/>
	</NegativeObjectPropertyAssertion>
 */	

public class NegativeObjectPropertyAssertionOWLImpl extends MappingOWLImpl {

	protected SemanticCacheOBORO semanticCacheOBORO = new SemanticCacheOBORO();//xd extra OBO RO semantics are needed
	
	
	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#parseImpl(org.w3c.dom.Node, org.semanticweb.owlapi.model.OWLOntologyManager, ecolabel.knowledgebase.module.LocalOWLModule)
	 */
	@Override
	public void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom) {
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		Element tempEleC = (Element) nl.getFirstChild().getNextSibling().getNextSibling();//xd third node
		OWLObjectProperty tempObjectPropertyA = df.getOWLObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
		OWLNamedIndividual tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
		OWLNamedIndividual tempNamedIndividualC = df.getOWLNamedIndividual(IRI.create(tempEleC.getAttribute("IRI")));
		OWLNegativeObjectPropertyAssertionAxiom tempONOPAA = df.getOWLNegativeObjectPropertyAssertionAxiom(tempObjectPropertyA, tempNamedIndividualB, tempNamedIndividualC);
		owlmgr.addAxiom(lom.getOwlOntology(), tempONOPAA);
		
		if(semanticCacheOBORO.isOBOROProperty(IRI.create(tempEleA.getAttribute("IRI")))){//xd find out that this property is an extra objectProperty
			//xd get the label axiom from semanticCacheOBORO
			String objectPropertyLabel = semanticCacheOBORO.getLabelStringForObjectProperty(IRI.create(tempEleA.getAttribute("IRI")));
			OWLAnnotation commentAnno = df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(objectPropertyLabel, "en"));//xd default the language is English
			OWLAxiom axiomLabel = df.getOWLAnnotationAssertionAxiom(tempObjectPropertyA.getIRI(), commentAnno);
			owlmgr.addAxiom(lom.getOwlOntology(), axiomLabel);
		}
		
	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#renderMappingImpl(org.w3c.dom.Node, ecolabel.protege.plugin.component.BinaryMappingComponent)
	 */
	@Override
	public void renderMappingImpl(Node nl, BinaryMappingComponent bmc) {
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		Element tempEleC = (Element) nl.getFirstChild().getNextSibling().getNextSibling();//xd third node
		
		String tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
		
		if(semanticCacheOBORO.isOBOROProperty(IRI.create(tempEleA.getAttribute("IRI")))){
			bmc.mapping.setToolTipText(tempString + " " + "\"" +semanticCacheOBORO.getLabelStringForObjectProperty(IRI.create(tempEleA.getAttribute("IRI"))) + "\"");//xd set the tool tip with label
		}else{
			bmc.mapping.setToolTipText(tempString);//xd set the tool tip
		}
		
		bmc.mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
		bmc.mapping.setVisible(true);
		bmc.mappingType.setSelectedItem("NegativeObjectPropertyAssertion");//xd set the selected mapping type 
		bmc.lbMappingType.setText("NegativeObjectPropertyAssertion");
		
		tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
		bmc.tfHead.setToolTipText(tempString);//xd set the tool tip
		bmc.tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
		
		tempString = IRI.create(tempEleC.getAttribute("IRI")).toString();
		bmc.tfTail.setToolTipText(tempString);
		bmc.tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
		

	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#buildRealBinaryMappingImpl(ecolabel.protege.plugin.component.BinaryMappingComponent, org.w3c.dom.Document)
	 */
	@Override
	public Element buildRealBinaryMappingImpl(BinaryMappingComponent bmc, Document doc) {
		String type = bmc.lbMappingType.getText();
		Element realMapping = doc.createElement(type);
		
		Element eleObjectProperty1 = doc.createElement("ObjectProperty");
		eleObjectProperty1.setAttribute("IRI", getEntityIRIFromTooltip(bmc.mapping.getToolTipText()));
		Element eleIndividual3 = doc.createElement("NamedIndividual");
		eleIndividual3.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));
		Element eleIndividual4 = doc.createElement("NamedIndividual");
		eleIndividual4.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfTail.getToolTipText()));
		realMapping.appendChild(eleObjectProperty1);
		realMapping.appendChild(eleIndividual3);
		realMapping.appendChild(eleIndividual4);
		
		return realMapping;
	}

}
