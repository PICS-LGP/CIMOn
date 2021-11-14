/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 9 mai 2017
 * 
 */
package ecolabel.protege.plugin.test;

import java.util.Iterator;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 9 mai 2017
 * 
 */
public class ClassHierarchyConsolePrinter {

	/**
	 * Recursive printing of class hierarchy
	 * @param parent	The parent or root class of this hierarchy
	 * @param reasoner	Reasonr 
	 * @param depth		Indent depth
	 */
	public void printClassHierarchy(Node<OWLClass> parent, OWLReasoner reasoner,
            int depth) {
        // We don't want to print out the bottom node (containing owl:Nothing
        // and unsatisfiable classes) because this would appear as a leaf node
        // everywhere
        if (parent.isBottomNode()) {
            return;
        }
        // Print an indent to denote parent-child relationships
        printIndent(depth);
        // Now print the node (containing the child classes)
        printNode(parent);
        for (Node<OWLClass> child : reasoner.getSubClasses(
                parent.getRepresentativeElement(), true)) {
            // Recurse to do the children. Note that we don't have to worry
            // about cycles as there are non in the inferred class hierarchy
            // graph - a cycle gets collapsed into a single node since each
            // class in the cycle is equivalent.
        	printClassHierarchy(child, reasoner, depth + 1);
        }
    }

	
	/**
	 * Print indents
	 * @param depth	Indent depth
	 */
    public void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }
    }

    
    /**
     * Print node 
     * @param node OWLClass node to be printed
     */
    public void printNode(Node<OWLClass> node) {
        DefaultPrefixManager pm = new DefaultPrefixManager(
                "http://www.enit.fr/xuda/ontologies/ecolabel#");
        // Print out a node as a list of class names in curly brackets
        System.out.print("{");
        for (Iterator<OWLClass> it = node.getEntities().iterator(); it
                .hasNext();) {
            OWLClass cls = it.next();
            // User a prefix manager to provide a slightly nicer shorter name
            System.out.print(pm.getShortForm(cls));
            if (it.hasNext()) {
            	System.out.println(" ");
            }
        }
        System.out.println("}");
    }
}
