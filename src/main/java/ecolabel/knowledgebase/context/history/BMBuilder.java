/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 8 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ecolabel.protege.plugin.component.BinaryMappingComponent;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 8 juin 2017
 * 
 */
public abstract class BMBuilder {
	
	public abstract Element buildRealBinaryMapping(BinaryMappingComponent bmc, Document doc);

}
