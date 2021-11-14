/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 26 avr. 2017
 * 
 */
package ecolabel.knowledgebase.context.history;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecolabel.knowledgebase.module.LocalOWLModule;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 26 avr. 2017
 * xd translate XML elements into OWL API's ontology manipulation 
 */
public class Parser {

	protected static String nodeToString(Node node) throws TransformerException
	{
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return(buf.toString());
	}
	
	/**********************
	 * xd the concrete parsing job is supposed to be done in different parsers
	 * @param nl
	 * @param owlmgr
	 * @param lom
	 * @return parsing result
 	 */
	public String parse(NodeList nl, OWLOntologyManager owlmgr, LocalOWLModule lom){
		String parsingResult = "Parsing result:";
		return parsingResult;
	}
	
	
	
	/******************
	 * delete all unnecessary #text or #comment
	 * @param node
	 */
	public Node trimNode(Node node)
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
