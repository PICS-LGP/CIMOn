/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 24 avr. 2017
 * 
 */
package ecolabel.knowledgebase.context;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 24 avr. 2017
 * xd entry to context layer, application from upper layer instantialize this class to control any context instance
 */
public class ContextManager {

	/***************
	 * xd load (create) new context from file system
	 * @param contextPath
	 * @return
	 */
	public Context loadContext(String contextPath){
		
		Context context = new Context(contextPath);
		
		return context;
	}
	
	public Context getNewContext(){
		Context context = new Context();
		return context;
	}
	
	
	
	
}
