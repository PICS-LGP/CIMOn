/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 21 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 21 juin 2017
 * xd reuse code from http://www.java2s.com/Code/Java/Swing-JFC/CheckBoxNodeTreeSample.htm
 * 
 */
public class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

	  CheckBoxNodeRenderer renderer/* = new CheckBoxNodeRenderer()*/;

	  ChangeEvent changeEvent = null;

	  JTree tree;

	  public CheckBoxNodeEditor(JTree tree, CheckBoxNodeRenderer r) {
	    this.tree = tree;
	    this.renderer = r;
	  }

	  @Override
	  public Object getCellEditorValue() {
		  EntityNameSelectable checkbox = renderer.getLeafRenderer();
		  SimpleTreeNodeWrapper checkBoxNode = new SimpleTreeNodeWrapper(
				  checkbox.entityId.getText(), 
				  checkbox.entityIRI,
				  checkbox.entityLabel, 
				  checkbox.getToolTipText(), 
				  checkbox.reserveEntityOrNot.isSelected(),
				  checkbox.entityType,
				  checkbox.entityOntology);
		  return checkBoxNode;
	  }

	  public boolean isCellEditable(EventObject event) {
		  
	    boolean returnValue = false;
	    //System.out.println("CHECK BOX TREE: isCellEditable: " + returnValue);
	    if (event instanceof MouseEvent) {
	      MouseEvent mouseEvent = (MouseEvent) event;
	      //if(mouseEvent.getClickCount() == 2){//xd double click then we can edit tree node
	      TreePath path = tree.getPathForLocation(mouseEvent.getX(),
	          mouseEvent.getY());
	      if (path != null) {
	        Object node = path.getLastPathComponent();
	        if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
	          DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
	          Object userObject = treeNode.getUserObject();
	          returnValue = ((treeNode.isLeaf()) && (userObject instanceof SimpleTreeNodeWrapper));
	        }
	      }
	      //}
	    }
	    
	    return returnValue;
	  }

	  public Component getTreeCellEditorComponent(JTree tree, Object value,
	      boolean selected, boolean expanded, boolean leaf, int row) {

	    Component editor = renderer.getTreeCellRendererComponent(tree, value,
	        true, expanded, leaf, row, true);
	    
	    // editor always selected / focused
	    //xd 
	    ItemListener itemListener = new ItemListener() {
	      public void itemStateChanged(ItemEvent itemEvent) {
	        if (stopCellEditing()) {
	        	//((EntityNameSelectable) editor).reserveEntityOrNot.setSelected( renderer.getLeafRenderer().reserveEntityOrNot.isSelected());
	        	
	          fireEditingStopped();
	          
	        }
	      }
	    };
	    
	    
	    
	    MouseAdapter mouseAdapter = new MouseAdapter(){
			public void mouseClicked(MouseEvent  e) {
				if (stopCellEditing()) {
		        	//((EntityNameSelectable) editor).reserveEntityOrNot.setSelected( renderer.getLeafRenderer().reserveEntityOrNot.isSelected());
		          fireEditingStopped();
		        }
			}
	    };
	    
	    MouseListener mouseListener = new MouseListener(){
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
//	    		if (stopCellEditing()) {
//		        	fireEditingStopped();
//		        	}
	    	    };
	    	    @Override
	    	    public void mouseExited(MouseEvent e) {
//	    	    	if (stopCellEditing()) {
//			        	fireEditingStopped();
//			        }
	    	    }

				@Override
				public void mouseClicked(MouseEvent arg0) {
//					if (stopCellEditing()) {
//			        	fireEditingStopped();
//			        }
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					if (stopCellEditing()) {
			        	fireEditingStopped();
			        }
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
	    };
	    
	    if (editor instanceof EntityNameSelectable) {
	      ((EntityNameSelectable) editor).reserveEntityOrNot.addItemListener(itemListener);
	      ((EntityNameSelectable) editor).entityId.addMouseListener(mouseListener);
	    }

	    return editor;
	  }
	  
	  
	}
