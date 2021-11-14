/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 9 juin 2017
 * 
 */
package ecolabel.protege.plugin.transferHandler;

import java.awt.Component;
import java.awt.datatransfer.Transferable;

import javax.swing.JTextField;
import javax.swing.TransferHandler;

import ecolabel.protege.plugin.component.SimpleTreeNodeWrapper;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 9 juin 2017
 * 
 */
public class TextFieldTransferHandler extends TransferHandler{
	
	
	@Override
    public boolean canImport(TransferSupport source) {
        return /*(source.getComponent() instanceof JTree)*/ source.isDataFlavorSupported(TreeNodeTransferable.TREE_NODE_DATA_FLAVOR);
    }
	
	
	public boolean importData(TransferSupport info){
		if (!info.isDrop()) {
            return false;
        }
		
		
		
		boolean accept = false;
        if (canImport(info)) {
            try {
                Transferable t = info.getTransferable();
                Object value = t.getTransferData(TreeNodeTransferable.TREE_NODE_DATA_FLAVOR);
                if (value instanceof SimpleTreeNodeWrapper) {
                	Component component = info.getComponent();
                    if (component instanceof JTextField) {
                        ((JTextField)component).setText(((SimpleTreeNodeWrapper)value).getId());
                        ((JTextField)component).setToolTipText(((SimpleTreeNodeWrapper)value).getTooltip());
                        accept = true;
                    }
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return accept;
	}

}
