/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 9 juin 2017
 * 
 */
package ecolabel.protege.plugin.transferHandler;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;

import ecolabel.protege.plugin.component.SimpleTreeNodeWrapper;


/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 9 juin 2017
 * 
 */
public class TreeTransferHandler extends TransferHandler{
	
	@Override
	protected Transferable createTransferable(JComponent c){
		TreeNodeTransferable tnTransferable = null;
		if(c instanceof JTree){
			JTree tree = (JTree) c;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			
			SimpleTreeNodeWrapper wrapper = (SimpleTreeNodeWrapper) node.getUserObject();
			tnTransferable = new TreeNodeTransferable(wrapper);
			
		}
		return tnTransferable;
		
	}
	
	@Override
    public int getSourceActions(JComponent c) {
        return DnDConstants.ACTION_COPY;//xd support only copy
    }
	
	@Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        System.out.println("Drag & Copy from JTree complete");
        
    }
	
}
