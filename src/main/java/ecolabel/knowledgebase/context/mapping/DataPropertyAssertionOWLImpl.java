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
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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
public class DataPropertyAssertionOWLImpl extends MappingOWLImpl {

	/* (non-Javadoc)
	 * @see ecolabel.knowledgebase.context.mapping.MappingOWLImpl#parseImpl(org.w3c.dom.Node, org.semanticweb.owlapi.model.OWLOntologyManager, ecolabel.knowledgebase.module.LocalOWLModule)
	 */
	@Override
	public void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom) {
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		Element tempEleA = (Element) nl.getFirstChild();//xd first node
		Element tempEleB = (Element) nl.getFirstChild().getNextSibling();//xd second node
		Element tempEleC = (Element) nl.getFirstChild().getNextSibling().getNextSibling();//xd third node
		OWLDataProperty tempDataPropertyA = df.getOWLDataProperty(IRI.create(tempEleA.getAttribute("IRI")));
		OWLNamedIndividual tempNamedIndividualB = df.getOWLNamedIndividual(IRI.create(tempEleB.getAttribute("IRI")));
		/*xd be careful that here the value of this literal is not sure*/
		OWLLiteral tempLiteralA = getOWLLiteral(df, tempEleC);//xd the value of this node could be String, double, integer or anything.
		OWLDataPropertyAssertionAxiom tempODPAA = df.getOWLDataPropertyAssertionAxiom(tempDataPropertyA, tempNamedIndividualB, tempLiteralA);
		owlmgr.addAxiom(lom.getOwlOntology(), tempODPAA);
		
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
		bmc.mapping.setToolTipText(tempString);//xd set the tool tip
		bmc.mapping.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
		bmc.mapping.setVisible(true);
		bmc.mappingType.setSelectedItem("DataPropertyAssertion");//xd set the selected mapping type 
		bmc.lbMappingType.setText("DataPropertyAssertion");
		
		tempString = IRI.create(tempEleB.getAttribute("IRI")).toString();
		bmc.tfHead.setToolTipText(tempString);//xd set the tool tip
		bmc.tfHead.setText(tempString.substring(tempString.lastIndexOf("#") + 1));
		
		tempString = IRI.create(tempEleC.getAttribute("datatypeIRI")).toString();
		bmc.tfTail.setToolTipText(tempEleC.getNodeValue());
		bmc.tfTail.setText(tempEleC.getNodeValue());
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
		
		Element eleDataProperty = doc.createElement("DataProperty");
		eleDataProperty.setAttribute("IRI", getEntityIRIFromTooltip(bmc.mapping.getToolTipText()));
		Element eleIndividual9 = doc.createElement("NamedIndividual");
		eleIndividual9.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));
		Element eleLiteral = doc.createElement("Literal");
		eleLiteral.setAttribute("datatypeIRI", "http://www.w3.org/2001/XMLSchema#"+bmc.literalDatatype.getSelectedItem().toString());
		eleLiteral.setNodeValue(getEntityIRIFromTooltip(bmc.tfTail.getToolTipText()));
		realMapping.appendChild(eleDataProperty);
		realMapping.appendChild(eleIndividual9);
		realMapping.appendChild(eleLiteral);
		
		return realMapping;
	}

}
