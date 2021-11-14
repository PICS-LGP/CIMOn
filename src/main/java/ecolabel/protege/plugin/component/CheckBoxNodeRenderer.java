/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 21 juin 2017
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

import ecolabel.protege.plugin.view.ContextSkeletonView;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 21 juin 2017
 * xd reuse code from http://www.java2s.com/Code/Java/Swing-JFC/CheckBoxNodeTreeSample.htm
 * 
 */
public class CheckBoxNodeRenderer implements TreeCellRenderer {
	  private EntityNameSelectable leafRenderer /*= new EntityNameSelectable()*/;
	  private ContextSkeletonView csv;
	  private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

	  Color selectionBorderColor, selectionForeground, selectionBackground,
	      textForeground, textBackground;

	  protected EntityNameSelectable getLeafRenderer() {
	    return leafRenderer;
	  }

	  public CheckBoxNodeRenderer(ItemListener il) {
		  leafRenderer = new EntityNameSelectable(il);
	    Font fontValue;
	    fontValue = UIManager.getFont("Tree.font");
	    if (fontValue != null) {
	      leafRenderer.setFont(fontValue);
	    }
	    /*Boolean booleanValue = (Boolean) UIManager
	        .get("Tree.drawsFocusBorderAroundIcon");
	    leafRenderer.setFocusable((booleanValue != null)//xd old code is setFocusPainted
	        && (booleanValue.booleanValue()));*/
	    leafRenderer.setFocusable(false);

	    selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
	    selectionForeground = UIManager.getColor("Tree.selectionForeground");
	    selectionBackground = UIManager.getColor("Tree.selectionBackground");
	    textForeground = UIManager.getColor("Tree.textForeground");
	    textBackground = UIManager.getColor("Tree.textBackground");
	  }
	  
//	  public CheckBoxNodeRenderer(ContextSkeletonView csv) {
//		  leafRenderer = new EntityNameSelectable(csv);
//	    Font fontValue;
//	    fontValue = UIManager.getFont("Tree.font");
//	    if (fontValue != null) {
//	      leafRenderer.setFont(fontValue);
//	    }
//	    /*Boolean booleanValue = (Boolean) UIManager
//	        .get("Tree.drawsFocusBorderAroundIcon");
//	    leafRenderer.setFocusable((booleanValue != null)//xd old code is setFocusPainted
//	        && (booleanValue.booleanValue()));*/
//	    leafRenderer.setFocusable(false);
//
//	    selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
//	    selectionForeground = UIManager.getColor("Tree.selectionForeground");
//	    selectionBackground = UIManager.getColor("Tree.selectionBackground");
//	    textForeground = UIManager.getColor("Tree.textForeground");
//	    textBackground = UIManager.getColor("Tree.textBackground");
//	  }

	  public Component getTreeCellRendererComponent(JTree tree, Object value,
	      boolean selected, boolean expanded, boolean leaf, int row,
	      boolean hasFocus) {

	    Component returnValue;
	    if (leaf) {
	    	//System.out.println("CHECK BOX TREE: leaf");
	      //String stringValue = tree.convertValueToText(value, selected,
	          //expanded, leaf, row, false);
	      
	      //leafRenderer.entityId.setText(stringValue);
	      
	      //leafRenderer.reserveEntityOrNot.setVisible(true);
	      //leafRenderer.reserveEntityOrNot.setEnabled(true);
	      //leafRenderer.setEnabled(true/*tree.isEnabled()*/);

	      if (selected) {
	    	  leafRenderer.entityId.setForeground(Color.white);
	        leafRenderer.setForeground(selectionForeground);
	        leafRenderer.setBackground(selectionBackground);
	      } else {
	    	  leafRenderer.entityId.setForeground(Color.black);
	        leafRenderer.setForeground(textForeground);
	        leafRenderer.setBackground(textBackground);
	      }

	      if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
	    	  //System.out.println("CHECK BOX TREE: (value != null) && (value instanceof DefaultMutableTreeNode)");
	        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
	        if (userObject instanceof SimpleTreeNodeWrapper) {
	        	//System.out.println("userObject instanceof SimpleTreeNodeWrapper");
	        	SimpleTreeNodeWrapper node = (SimpleTreeNodeWrapper) userObject;
	        	
	          leafRenderer.entityId.setText(node.getId());
	          leafRenderer.reserveEntityOrNot.setSelected(node.isReserveOrNot());
	          leafRenderer.setToolTipText(node.getTooltip());//xd label (if any) plus the complete IRI
	          leafRenderer.entityIRI = node.getIri();
	          leafRenderer.entityType = node.getEntityType();
	          leafRenderer.entityOntology = node.getOntology();
	        }
	      }
	      returnValue = leafRenderer;
	    } else {
	    	//System.out.println("CHECK BOX TREE: not leaf");
	      returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,
	          value, selected, expanded, leaf, row, hasFocus);
	    }
	    return returnValue;
	  }
	}
