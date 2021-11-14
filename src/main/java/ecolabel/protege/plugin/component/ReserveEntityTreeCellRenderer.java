/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 20 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 20 juin 2017
 * 
 */




public class ReserveEntityTreeCellRenderer extends EntityNameSelectable implements TreeCellRenderer{

	/**
	 * @param il
	 */
	public ReserveEntityTreeCellRenderer(ItemListener il) {
		super(il);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param il
	 */
	

	public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
		
//		super.getTreeCellRendererComponent(
//                tree, value, sel,
//                expanded, leaf, row,
//                hasFocus);
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		//panel = new EntityNameSelectable();
		
		if(!node.isRoot()){
			SimpleTreeNodeWrapper wrapper = (SimpleTreeNodeWrapper) (node.getUserObject());
			this.entityId.setText(wrapper.getId());
			setEnabled(true);
			setToolTipText(wrapper.getLabel() + "  " + wrapper.getTooltip());
		}else{
			this.entityId.setText("owl:thing");
			setEnabled(false);
			
			this.reserveEntityOrNot.setVisible(false);
			//panel.setToolTipText(wrapper.getLabel() + "  " + wrapper.getTooltip());
		}
		
		
		return this;
	}
}
