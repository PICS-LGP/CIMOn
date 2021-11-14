/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 19 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
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
*
* 	<DataPropertyRange>
			<DataProperty IRI="hasAge"/>
			<Datatype IRI="http://www.w3.org/2001/XMLSchema#nonNegativeInteger"/>
	</DataPropertyRange>
*/
public class DataPropertyRangeOWLImpl extends MappingOWLImpl {

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#parseImpl(org.w3c.dom.Node, org.semanticweb.owlapi.model.OWLOntologyManager, ecolabel.knowledgebase.module.LocalOWLModule)
	 */
	@Override
	public void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom) {
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		OWLDataProperty tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
		OWLDatatype tempDatatypeA = df.getOWLDatatype(IRI.create(tempEleB.getAttribute("IRI")));
		OWLDataPropertyRangeAxiom tempODPRA = df.getOWLDataPropertyRangeAxiom(tempDataPropertyA, tempDatatypeA);
		owlmgr.addAxiom(lom.getOwlOntology(), tempODPRA);
	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#renderMappingImpl(org.w3c.dom.Node, ecolabel.protege.plugin.component.BinaryMappingComponent)
	 */
	@Override
	public void renderMappingImpl(Node nl, BinaryMappingComponent bmc) {
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		
//		tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
//		mapping.setToolTipText(tempString);//xd set the tool tip
//		mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
		
		bmc.mappingType.setSelectedItem("DataPropertyRange");//xd set the selected mapping type 
		bmc.lbMappingType.setText("DataPropertyRange");
		bmc.mapping.setVisible(false);
		String tempString = IRI.create(tempEleA.getAttribute("IRI")).toString();
		bmc.tfHead.setToolTipText(tempString);//xd set the tool tip
		bmc.tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
		
		tempString = IRI.create(tempEleB.getAttribute("datatypeIRI")).toString();
		//tfTail.setToolTipText(tempEleB.getNodeValue());
		//tfTail.setText(tempEleB.getNodeValue());
		bmc.tfTail.setVisible(false);
		bmc.literalDatatype.setSelectedItem((tempString.substring(tempString.lastIndexOf("#") + 1)));//xd add extra datatype chooser to this mapping editor
		bmc.literalDatatype.setVisible(true);
		

	}

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#buildRealBinaryMappingImpl(ecolabel.protege.plugin.component.BinaryMappingComponent, org.w3c.dom.Document)
	 */
	@Override
	public Element buildRealBinaryMappingImpl(BinaryMappingComponent bmc, Document doc) {
		String type = bmc.lbMappingType.getText();
		Element realMapping = doc.createElement(type);
		
		Element eleDataProperty3 = doc.createElement("DataProperty");
		eleDataProperty3.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));
		Element eleDatatype = doc.createElement("Datatype");
		eleDatatype.setAttribute("IRI", "http://www.w3.org/2001/XMLSchema#"+bmc.literalDatatype.getSelectedItem().toString());
		realMapping.appendChild(eleDataProperty3);
		realMapping.appendChild(eleDatatype);
		
		return realMapping;
	}

}
