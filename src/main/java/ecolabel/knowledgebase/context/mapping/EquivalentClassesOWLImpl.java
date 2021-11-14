/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 19 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
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

/**********
*
* 	<EquivalentClasses>
			<Class IRI="Person"/>
			<Class IRI="Human"/>
	</EquivalentClasses>
*/
public class EquivalentClassesOWLImpl extends MappingOWLImpl {

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#parseImpl(org.w3c.dom.Node, org.semanticweb.owlapi.model.OWLOntologyManager, ecolabel.knowledgebase.module.LocalOWLModule)
	 */
	@Override
	public void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom) {
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		OWLClass tempClassA = df.getOWLClass(IRI.create(tempEleA.getAttribute("IRI")));
		OWLClass tempClassB = df.getOWLClass(IRI.create(tempEleB.getAttribute("IRI")));
		OWLEquivalentClassesAxiom tempOECA = df.getOWLEquivalentClassesAxiom(tempClassA,tempClassB);
		owlmgr.addAxiom(lom.getOwlOntology(), tempOECA);
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
		bmc.mappingType.setSelectedItem("EquivalentClasses");//xd set the selected mapping type 
		bmc.lbMappingType.setText("EquivalentClasses");
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
		
		Element eleClassA1 = doc.createElement("Class");
		eleClassA1.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));
		Element eleClassB1 = doc.createElement("Class");
		eleClassB1.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfTail.getToolTipText()));
		realMapping.appendChild(eleClassA1);
		realMapping.appendChild(eleClassB1);
		
		return realMapping;
	}

}
