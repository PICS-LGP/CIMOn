/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 16 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecolabel.knowledgebase.module.LocalOWLModule;
import ecolabel.protege.plugin.component.BinaryMappingComponent;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 16 juin 2017
 * 
 */

/********************
 * xd valid 
 *	<SubClassOf>
			<Class IRI="Mother"/>
			<Class IRI="Woman"/>
		</SubClassOf>
 */
public class SubClassOfOWLImpl extends MappingOWLImpl{

	/* 
	 * @see ecolabel.knowledgebase.context.mapping.MappingImpl#renderMapping(org.w3c.dom.NodeList)
	 */
	@Override
	public void renderMappingImpl(Node nl, BinaryMappingComponent bmc) {
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		
		String tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
		bmc.tfHead.setToolTipText(tempString);//xd set the tool tip
		bmc.tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
		bmc.mappingType.setSelectedItem("SubClassOf");//xd set the selected mapping type 
		bmc.lbMappingType.setText("SubClassOf");
		bmc.mapping.setVisible(false);
		
		tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
		bmc.tfTail.setToolTipText(tempString);
		bmc.tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
		
	}

	
	/* 
	 * @see ecolabel.knowledgebase.context.mapping.MappingImpl#parse(org.w3c.dom.NodeList, org.semanticweb.owlapi.model.OWLOntologyManager, ecolabel.knowledgebase.module.LocalOWLModule)
	 */
	@Override
	public void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom) {
		
		OWLDataFactory df = OWLManager.getOWLDataFactory(); 
		
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		OWLClass tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
		OWLClass tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
		OWLSubClassOfAxiom tempOSCOA = df.getOWLSubClassOfAxiom(tempClassA, tempClassB);
		owlmgr.addAxiom(lom.getOwlOntology(), tempOSCOA);
		
		
	}

	
	/* 
	 * @see ecolabel.knowledgebase.context.mapping.MappingImpl#buildRealBinaryMapping(ecolabel.protege.plugin.component.BinaryMappingComponent, org.w3c.dom.Document)
	 */
	@Override
	public Element buildRealBinaryMappingImpl(BinaryMappingComponent bmc, Document doc) {
		
		String type = bmc.lbMappingType.getText();
		Element realMapping = doc.createElement(type);
		
		Element eleClassA = doc.createElement("Class");
		eleClassA.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));
		Element eleClassB = doc.createElement("Class");
		eleClassB.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfTail.getToolTipText()));
		realMapping.appendChild(eleClassA);
		realMapping.appendChild(eleClassB);
		
		return realMapping;
	}

}
