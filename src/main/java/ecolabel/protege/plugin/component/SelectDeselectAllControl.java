/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 22 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.semanticweb.owlapi.model.IRI;

import ecolabel.protege.plugin.view.ContextSkeletonView;

import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 22 juin 2017
 * 
 */
public class SelectDeselectAllControl extends JPanel {

	
	public JButton btnSelectAll;
	public JButton btnDeselectAll;
	
	public ContextSkeletonView contextSkeletonView;
	
	public JTree targetJTree;
	
	

	public SelectDeselectAllControl() {
		initialise();

	}
	
	
//	public SelectDeselectAllControl(JTree target){
//		initialise();
//		this.targetJTree = target;
//	}
	
	public SelectDeselectAllControl(JTree target, ContextSkeletonView contextSkeletonView){
		initialise();
		this.targetJTree = target;
		this.contextSkeletonView = contextSkeletonView;
	}

	
	public void initialise(){
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		btnSelectAll = new JButton("Select all");
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(targetJTree.getModel() != null){
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) targetJTree.getModel().getRoot();
					//xd according to SourceOntologyView.updateClassList(), the model is an liner array. only one layer
					
					System.out.println("\n Select all!");
					int entityCount = root.getChildCount();
					if(entityCount > 0 && contextSkeletonView.getContextEditorView().getContext() != null){
							
						for(int i = 0; i < entityCount; i ++){
							DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode) root.getChildAt(i);
							SimpleTreeNodeWrapper object = (SimpleTreeNodeWrapper) currentChild.getUserObject();
							contextSkeletonView.getContextEditorView().getContext().updateItemInContextMemberFilterCatalog(true, IRI.create(object.getIri()), object.getEntityType(), object.getOntology());
						}
//							DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode) root.getChildAt(i);
//							SimpleTreeNodeWrapper object = (SimpleTreeNodeWrapper) currentChild.getUserObject();
//							//object.setReserveOrNot(true);
//							System.out.println("SELECT: " + object.getId() + " " + object.getIri() + " " + object.isReserveOrNot());
//							CheckBoxNodeRenderer renderer = (CheckBoxNodeRenderer) targetJTree.getCellRenderer();
//							EntityNameSelectable componentRenderer = (EntityNameSelectable) renderer.getTreeCellRendererComponent(
//									targetJTree, object, true, true, true, targetJTree.getRowForPath(new TreePath(currentChild.getPath())), false);
//							CheckBoxNodeEditor editor = (CheckBoxNodeEditor) targetJTree.getCellEditor();
//							EntityNameSelectable componentEditor = (EntityNameSelectable) editor.getTreeCellEditorComponent(
//									targetJTree, object, true, true, true, targetJTree.getRowForPath(new TreePath(currentChild.getPath())));
//							
////							editor.stopCellEditing();
//							targetJTree.startEditingAtPath(new TreePath(currentChild.getPath()));
//							if(!componentEditor.reserveEntityOrNot.isSelected() /*|| !componentRenderer.reserveEntityOrNot.isSelected()*/){
////								componentEditor.reserveEntityOrNot.setSelected(true);
//								componentEditor.reserveEntityOrNot.doClick();
////								object.setReserveOrNot(true);
//								System.out.println("select click!");
//							}
//							targetJTree.stopEditing();
						//targetJTree.updateUI();
						updateEntityListHandler();
					}
				}
			}});
		add(btnSelectAll);
		//xd the method using itemListener is not working, try to modify the catalog directly
		btnDeselectAll = new JButton("Deselect all");
		btnDeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(targetJTree.getModel() != null){
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) targetJTree.getModel().getRoot();
					//xd according to SourceOntologyView.updateClassList(), the model is an liner array. only one layer
					System.out.println("\n Deselect all!");
					int entityCount = root.getChildCount();
					if(entityCount > 0 && contextSkeletonView.getContextEditorView().getContext() != null){
						
						for(int i = 0; i < entityCount; i ++){
							DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode) root.getChildAt(i);
							SimpleTreeNodeWrapper object = (SimpleTreeNodeWrapper) currentChild.getUserObject();
							contextSkeletonView.getContextEditorView().getContext().updateItemInContextMemberFilterCatalog(false, IRI.create(object.getIri()), object.getEntityType(), object.getOntology());
						}
//							DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode) root.getChildAt(i);
//							SimpleTreeNodeWrapper object = (SimpleTreeNodeWrapper) currentChild.getUserObject();
//							//object.setReserveOrNot(true);
//							System.out.println("DESELECT: " + object.getId() + " " + object.getIri() + " " + object.isReserveOrNot());
//							CheckBoxNodeRenderer renderer = (CheckBoxNodeRenderer) targetJTree.getCellRenderer();
//							EntityNameSelectable componentRenderer = (EntityNameSelectable) renderer.getTreeCellRendererComponent(
//									targetJTree, object, true, true, true, targetJTree.getRowForPath(new TreePath(currentChild.getPath())), false);
//							CheckBoxNodeEditor editor = (CheckBoxNodeEditor) targetJTree.getCellEditor();
//							EntityNameSelectable componentEditor = (EntityNameSelectable) editor.getTreeCellEditorComponent(
//									targetJTree, object, true, true, true, targetJTree.getRowForPath(new TreePath(currentChild.getPath())));
//							
////							editor.stopCellEditing();
//							targetJTree.startEditingAtPath(new TreePath(currentChild.getPath()));
//							if(componentEditor.reserveEntityOrNot.isSelected() /*|| componentRenderer.reserveEntityOrNot.isSelected()*/){
////								componentEditor.reserveEntityOrNot.setSelected(true);
//								componentEditor.reserveEntityOrNot.doClick();
//								System.out.println("deselect click!");
////								object.setReserveOrNot(true);
//								
//							}
//							targetJTree.stopEditing();
							
							
						
						//targetJTree.updateUI();
						updateEntityListHandler();
					}
				}
				
			}
		});
		add(btnDeselectAll);
	}

	public void updateEntityListHandler(){
		contextSkeletonView.updateEntityListHandler();
	}
}
