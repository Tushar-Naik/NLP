import java.io.FileNotFoundException;
import java.util.ArrayList;

//This class goes through Brown Corpus and populates a dictionary of words and their POS.
public class getPOS 
{
	static WordToPosMap w=new WordToPosMap();
	static ArrayList<String>posArray=new ArrayList<String>();
	
public static String getMaxPOS(String word)
{
	ArrayList<String> list=w.POSdict.get(word);
	String currentMax = null; 
	int maxCount = 0;
	String current = null;
	int count = 0;
	for(int i = 0; i < list.size(); i++) 
	{
	    String item = list.get(i);
	    if(item.equals(current)) 
	    {
	        count++;
	    } 
	    else
	    { 
	        if(count > maxCount) 
	        { 
	            maxCount = count; 
	            currentMax = current; 
	        }
	        count = 1;
	        current = item;
	    }
	}
	
	if(currentMax==null)
		return current;
	return currentMax;
}
	
	
	public static void outputPOS(String s) throws FileNotFoundException
	{
		posArray.clear();
		s=s.toLowerCase();
		w.populatePOSdict();
		for(String x:s.split(" "))
		{	
			
			posArray.add(getMaxPOS(x));
			
			/*posArray=w.POSdict.get(x);
			System.out.println(x+"====="+posArray);*/
			
		}
		
				
	}
	
	public static void main(String args[])
	{
		try {
			System.out.println("you're");
			outputPOS("you're");
			for(String x:posArray)
				System.out.println(x);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("your");
			outputPOS("your");
			for(String x:posArray)
				System.out.println(x);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
