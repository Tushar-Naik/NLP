import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

//This class populates a hashmap of words:prev k words from Brown corpus
//Here word includes its POS
 class wordAndPOS
{
	String word;
	String POS;
	
	
}
public class trainContext 
{
	public static HashMap<wordAndPOS,ArrayList<wordAndPOS>> contextDict=new HashMap<wordAndPOS,ArrayList<wordAndPOS>>();
	
	
	@SuppressWarnings("unchecked")
	public static void populatecontextDict() throws FileNotFoundException
	{
		Scanner s=new Scanner(new File("ca01"));
		//wordAndPOS current = new wordAndPOS();
		ArrayList<wordAndPOS> prevk=new ArrayList<wordAndPOS>();
		while(s.hasNext())
		{
			wordAndPOS current = new wordAndPOS();
			String x=s.next();
			//System.out.println(x);
			try
			{
				current.word=x.split("/")[0];
				current.POS=x.split("/")[1];
				//System.out.println(current.word);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			if(contextDict.containsKey(current))
			{
				ArrayList<wordAndPOS>temp=contextDict.get(current);
				for(wordAndPOS i:prevk)
				{
					temp.add(i);
				}
				contextDict.remove(current);
				contextDict.put(current, (ArrayList<wordAndPOS>) temp.clone());
			}
			else
			{
				contextDict.put(current, (ArrayList<wordAndPOS>) prevk.clone());
				
			}
				
			if(prevk.size()==10)
			{
				int i;
				for( i=0;i<prevk.size()-1;i++)
				{
					prevk.set(i, prevk.get(i+1));
				}
				prevk.set(i, current);
				
			}
			else
				prevk.add(current);
			
			
		}
		
	}
	
	public static void main(String args[])
	{
		
		try
		{
			populatecontextDict();
		}
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Entry<wordAndPOS,ArrayList<wordAndPOS>>e:contextDict.entrySet())
		{
			System.out.println(e.getKey().word+"="+e.getKey().POS);
			System.out.println("--------------------------------------------------------------");
			for(wordAndPOS x:e.getValue())
			{
				System.out.println(x.word+"="+x.POS);
			}
			System.out.println("--------------------------------------------------------------");

	
		}
		//System.out.println("asassa");
	}
}
