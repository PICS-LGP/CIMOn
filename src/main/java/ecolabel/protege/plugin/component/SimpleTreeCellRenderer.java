/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 27 mai 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 27 mai 2017
 * 
 */
public class SimpleTreeCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
		
		super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		if(!node.isRoot()){
			SimpleTreeNodeWrapper wrapper = (SimpleTreeNodeWrapper) (node.getUserObject());
			setText(wrapper.getId());
			setToolTipText(/*wrapper.getId()+ "  " + */wrapper.getTooltip());
		}
		
		
		return this;
	}
}
