/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 19 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
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

/*****************
*
* 	<SameIndividual>
			<NamedIndividual IRI="James"/>
			<NamedIndividual IRI="Jim"/>
	</SameIndividual>
*/
public class SameIndividualOWLImpl extends MappingOWLImpl {

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#parseImpl(org.w3c.dom.Node, org.semanticweb.owlapi.model.OWLOntologyManager, ecolabel.knowledgebase.module.LocalOWLModule)
	 */
	@Override
	public void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom) {
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		OWLNamedIndividual tempNamedIndividualA = df.getOWLNamedIndividual(IRI.create(tempEleA.getAttribute("IRI")));
		OWLNamedIndividual tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
		OWLSameIndividualAxiom tempOSIA = df.getOWLSameIndividualAxiom(tempNamedIndividualA, tempNamedIndividualB);
		owlmgr.addAxiom(lom.getOwlOntology(), tempOSIA);
		
	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#renderMappingImpl(org.w3c.dom.Node, ecolabel.protege.plugin.component.BinaryMappingComponent)
	 */
	@Override
	public void renderMappingImpl(Node nl, BinaryMappingComponent bmc) {
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		
		String tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
		bmc.tfHead.setToolTipText(tempString);//xd set the tool tip
		bmc.tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping head field value
		
		bmc.mappingType.setSelectedItem("SameIndividual");//xd set the selected mapping type 
		bmc.lbMappingType.setText("SameIndividual");
		bmc.mapping.setVisible(false);
		tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
		bmc.tfTail.setToolTipText(tempString);
		bmc.tfTail.setText(tempString.substring(tempString.lastIndexOf("#") + 1));//xd set the mapping tail value
		

	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#buildRealBinaryMappingImpl(ecolabel.protege.plugin.component.BinaryMappingComponent, org.w3c.dom.Document)
	 */
	@Override
	public Element buildRealBinaryMappingImpl(BinaryMappingComponent bmc, Document doc) {
		String type = bmc.lbMappingType.getText();
		Element realMapping = doc.createElement(type);
		
		Element eleIndividual7 = doc.createElement("NamedIndividual");
		eleIndividual7.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));
		Element eleIndividual8 = doc.createElement("NamedIndividual");
		eleIndividual8.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfTail.getToolTipText()));
		realMapping.appendChild(eleIndividual7);
		realMapping.appendChild(eleIndividual8);
		
		return realMapping;
	}

}
