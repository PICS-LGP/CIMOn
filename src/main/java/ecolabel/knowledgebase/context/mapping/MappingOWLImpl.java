/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 16 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
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
 * xd this is the abstract of different mapping implementation. each mapping (for example SubClassOf ObjectPropertyAssertion... ) must support 
 * parsing(loading from context file), component renderring(self-showing on BinaryMappingComponent GUI, depends on BMC component), and mapping building(saving back to context file)
 */
public abstract class MappingOWLImpl {
	
	public abstract void parseImpl(Node nl, OWLOntologyManager owlmgr, LocalOWLModule lom);/*{
		return "In MappingOWLImpl";//xd do nothing
	}*/
	
	public abstract void renderMappingImpl(Node nl, BinaryMappingComponent bmc );/*{
		//xd do nothing
	}*/
	
	public abstract Element buildRealBinaryMappingImpl(BinaryMappingComponent bmc, Document doc);/*{
		return null;//xd do nothing
	}*/
	
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
	
	
	protected String nodeToString(Node node) throws TransformerException
	{
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return(buf.toString());
	}
	
	//xd by default the first part of toolTip is the IRI
	protected String getEntityIRIFromTooltip(String toolTip){
		if(!toolTip.equals("") && toolTip.indexOf(" ") != -1){
			return toolTip.substring(0, toolTip.indexOf(" "));
		}else{
			return toolTip;
		}
	}
	
	/******************
	 * delete all unnecessary #text or #comment
	 * @param node
	 */
//	public Node trimNode(Node node)
//	{
//	    NodeList children = node.getChildNodes();
//	    for(int i = 0; i < children.getLength(); ++i) {
//	        Node child = children.item(i);
//	        if(child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == node.COMMENT_NODE) {
//	            node.removeChild(child);
//	        }
//	        child = trimNode(child);
//	    }
//		return node;
//	}
	
}
