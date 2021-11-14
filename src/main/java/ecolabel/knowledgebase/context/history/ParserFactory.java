/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 27 avr. 2017
 * 
 */
package ecolabel.knowledgebase.context.history;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 27 avr. 2017
 * 
 */
public class ParserFactory {
	
	public Parser getParser(String type){
		
		Parser temp = null;
		
		if(type.equals("OWL/XML")){
			//System.out.println("Getting new OWL/XML parser... by using parameter " + type);
			temp = new OWLParser();
			return temp;
		}
		
		//xd imagin that we have mapping of other format
		
		return temp;
		
	}

}
