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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
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

/***************
 * 
 * <ClassAssertion>
 * 		<Class IRI="Person"/>
		<NamedIndividual IRI="Mary"/>
		</ClassAssertion>
 */
public class ClassAssertionOWLImpl extends MappingOWLImpl {

	
	
	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#parseImpl(org.w3c.dom.Node, org.semanticweb.owlapi.model.OWLOntologyManager, ecolabel.knowledgebase.module.LocalOWLModule)
	 */
	@Override
	public void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom) {
		
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		Element tempEleClass = (Element) nl.getFirstChild();//xd first node
		Element tempEleIndividual = (Element) nl.getFirstChild().getNextSibling();//xd second node
		OWLClass tempClass = df.getOWLClass(IRI.create(tempEleClass.getAttribute("IRI")));
		OWLIndividual tempIndividual = df.getOWLNamedIndividual(IRI.create(tempEleIndividual.getAttribute("IRI")));
		OWLClassAssertionAxiom tempOCAA = df.getOWLClassAssertionAxiom(tempClass,tempIndividual);
		owlmgr.addAxiom(lom.getOwlOntology(), tempOCAA);
		
		
		
	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#renderMappingImpl(org.w3c.dom.Node, ecolabel.protege.plugin.component.BinaryMappingComponent)
	 */
	@Override
	public void renderMappingImpl(Node nl, BinaryMappingComponent bmc) {
		Element tempEleClass = (Element) nl.getFirstChild();//xd first node
		Element tempEleIndividual = (Element) nl.getFirstChild().getNextSibling();//xd second node
		
		String tempString = IRI.create(tempEleClass.getAttribute("IRI")).toString();
		bmc.tfHead.setToolTipText(tempString);//xd set the tool tip, no label info loaded from context
		bmc.tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
		bmc.mappingType.setSelectedItem("ClassAssertion");//xd set the selected mapping type 
		bmc.lbMappingType.setText("ClassAssertion");
		bmc.mapping.setVisible(false);
		tempString = IRI.create(tempEleIndividual.getAttribute("IRI")).toString();
		bmc.tfTail.setToolTipText(tempString);//xd set tool tip, no label info from context
		bmc.tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
		

	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#buildRealBinaryMappingImpl(ecolabel.protege.plugin.component.BinaryMappingComponent, org.w3c.dom.Document)
	 */
	@Override
	public Element buildRealBinaryMappingImpl(BinaryMappingComponent bmc, Document doc) {
		String type = bmc.lbMappingType.getText();
		Element realMapping = doc.createElement(type);
		Element eleClass = doc.createElement("Class");
		eleClass.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));//xd may contain label info from ontology, trim the label
		Element eleNamedIndividual = doc.createElement("NamedIndividual");
		eleNamedIndividual.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfTail.getToolTipText()));//xd may contain label info from ontology, trim the label
		realMapping.appendChild(eleClass);
		realMapping.appendChild(eleNamedIndividual);
		return realMapping;
	}

}
