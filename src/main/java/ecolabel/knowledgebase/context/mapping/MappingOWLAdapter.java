/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 16 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import org.semanticweb.owlapi.model.OWLOntologyManager;
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

/***************
 * According to different owl fragment, different mapping type, organize different axioms
 * As wide support as possible should be provided
 * Please refer to https://www.w3.org/TR/owl2-primer/ (show OWL/XML syntax; starts from Section 4: Classes, Properties, and Individuals – And Basic Modeling With Them )
 */
public class MappingOWLAdapter {
	
	private MappingOWLImpl owlImpl;
	
	public MappingOWLAdapter(String type){
		
		
		
		switch (type){
		
		case "SubClassOf":
			owlImpl = new SubClassOfOWLImpl();
			break;
		case "ClassAssertion":
			owlImpl = new ClassAssertionOWLImpl();
			break;
		case "EquivalentClasses":
			owlImpl = new EquivalentClassesOWLImpl();
			break;
		case "DisjointClasses":
			owlImpl = new DisjointClassesOWLImpl();
			break;
		case "ObjectPropertyAssertion":
			owlImpl = new ObjectPropertyAssertionOWLImpl();
			break;
		case "ObjectPropertyAssertion-OBO-RO"://xd some extra relationship from ro.owl
			owlImpl = new ObjectPropertyAssertionOWLImpl();//xd they are still object properties, so use the same impl 
			break;
		case "NegativeObjectPropertyAssertion":
			owlImpl = new NegativeObjectPropertyAssertionOWLImpl();
			break;
		case "SubObjectPropertyOf":
			owlImpl = new SubObjectPropertyOfOWLImpl();
			break;
		case "ObjectPropertyDomain":
			owlImpl = new ObjectPropertyDomainOWLImpl();
			break;
		case "ObjectPropertyRange":
			owlImpl = new ObjectPropertyRangeOWLImpl();
			break;
		case "DifferentIndividuals":
			owlImpl = new DifferentIndividualsOWLImpl();
			break;
		case "SameIndividual":
			owlImpl = new SameIndividualOWLImpl();
			break;
		case "DataPropertyAssertion":
			owlImpl = new DataPropertyAssertionOWLImpl();
			break;
		case "NegativeDataPropertyAssertion":
			owlImpl = new NegativeDataPropertyAssertionOWLImpl();
			break;
		case "DataPropertyDomain":
			owlImpl = new DataPropertyDomainOWLImpl();
			break;
		case "DataPropertyRange":
			owlImpl = new DataPropertyRangeOWLImpl();
			break;
		//xd other cases to be done...
		}
		
		
	}
	
	
	//xd to do: for parse and readerMapping, clean the nodelist before passing inside as parameters, after the cleaning, the nodelist should become node instead
	public void parse(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom){//xd interface not sure yet
		if(owlImpl != null && owlImpl instanceof MappingOWLImpl){
			owlImpl.parseImpl(nl, owlmgr, lom);
		}
	}
	
	public void renderMapping(Node nl, BinaryMappingComponent bmc ){//xd interface not sure yet
		if(owlImpl != null && owlImpl instanceof MappingOWLImpl){
			owlImpl.renderMappingImpl(nl, bmc);
		}
	}
	
	public Element buildRealBinaryMapping(BinaryMappingComponent bmc, Document doc){//xd interface not sure yet
		if(owlImpl != null && owlImpl instanceof MappingOWLImpl){
			return owlImpl.buildRealBinaryMappingImpl(bmc, doc);
		}else{
			System.out.println("Adapter's implementation not initialised yet");
			return null;
		}
	}
	
	/******************
	 * delete all unnecessary #text or #comment
	 * @param node
	 */
	public static Node trimNode(Node node)
	{
		
		
	    NodeList children = node.getChildNodes();
	    for(int i = 0; i < children.getLength(); ++i) {
	        Node child = children.item(i);
	        if(child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == node.COMMENT_NODE) {
	            node.removeChild(child);
	        }
	        child = trimNode(child);
	    }
	    
		return node;
	}
}
