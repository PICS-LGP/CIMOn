/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 9 juin 2017
 * 
 */
package ecolabel.protege.plugin.transferHandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import ecolabel.protege.plugin.component.SimpleTreeNodeWrapper;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 9 juin 2017
 * 
 * xd refer to this blog to let this wrapper to be transferable for Drag and Drop (DnD)
 * https://stackoverflow.com/questions/13855184/drag-and-drop-custom-object-from-jlist-into-jlabel
 *
 */
 
public class TreeNodeTransferable implements Transferable{

	public static DataFlavor TREE_NODE_DATA_FLAVOR = new DataFlavor(SimpleTreeNodeWrapper.class,"SimpleTreeNodeWrapper");//xd??? not so sure the function of this variable...
	private SimpleTreeNodeWrapper treeNodeWrapper;
	
	public TreeNodeTransferable(SimpleTreeNodeWrapper w){
		this.treeNodeWrapper = w;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		return treeNodeWrapper;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return new DataFlavor[]{TREE_NODE_DATA_FLAVOR};
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		return flavor.equals(TREE_NODE_DATA_FLAVOR);
	}

}
