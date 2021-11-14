/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 8 juin 2017
 * 
 */
package ecolabel.knowledgebase.context.history;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 8 juin 2017
 * 
 */
public class BMBuilderFactory {

public BMBuilder getBuilder(String type){
		
		BMBuilder temp = null;
		
		if(type.equals("OWL/XML")){
			//System.out.println("Getting new OWL/XML parser... by using parameter " + type);
			temp = new BMOWLBuilder();
			return temp;
		}
		
		//xd imagine that we have mapping of other format
		
		return temp;
		
	}
}
