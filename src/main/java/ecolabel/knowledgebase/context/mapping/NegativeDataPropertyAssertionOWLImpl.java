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
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
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

/*************
*
* 	<NegativeDataPropertyAssertion>
			<DataProperty IRI="hasAge"/>
			<NamedIndividual IRI="Jack"/>
			<Literal datatypeIRI="http://www.w3.org/2001/XMLSchema#integer">53</Literal>
	</NegativeDataPropertyAssertion>
*/
public class NegativeDataPropertyAssertionOWLImpl extends MappingOWLImpl {

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
		OWLNegativeDataPropertyAssertionAxiom tempONDPAA = df.getOWLNegativeDataPropertyAssertionAxiom(tempDataPropertyA, tempNamedIndividualB, tempLiteralA);
		owlmgr.addAxiom(lom.getOwlOntology(), tempONDPAA);
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
		bmc.mappingType.setSelectedItem("NegativeDataPropertyAssertion");//xd set the selected mapping type 
		bmc.lbMappingType.setText("NegativeDataPropertyAssertion");
		
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
		
		Element eleDataProperty1 = doc.createElement("DataProperty");
		eleDataProperty1.setAttribute("IRI", getEntityIRIFromTooltip(bmc.mapping.getToolTipText()));
		Element eleIndividual10 = doc.createElement("NamedIndividual");
		eleIndividual10.setAttribute("IRI", getEntityIRIFromTooltip(bmc.tfHead.getToolTipText()));
		Element eleLiteral1 = doc.createElement("Literal");
		eleLiteral1.setAttribute("datatypeIRI", "http://www.w3.org/2001/XMLSchema#"+bmc.literalDatatype.getSelectedItem().toString());
		eleLiteral1.setNodeValue(getEntityIRIFromTooltip(bmc.tfTail.getToolTipText()));
		realMapping.appendChild(eleDataProperty1);
		realMapping.appendChild(eleIndividual10);
		realMapping.appendChild(eleLiteral1);
		
		return realMapping;
	}

}
