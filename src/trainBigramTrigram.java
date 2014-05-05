import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;


public class trainBigramTrigram {
	
	public static HashMap<String,Integer> bigramCount=new HashMap<String,Integer>();
	public static HashMap<String,Integer> trigramCount=new HashMap<String,Integer>();
	public trainBigramTrigram() throws FileNotFoundException 
	{
		Scanner s=new Scanner(new File("merged"));
		ArrayList<String> currentbigram=new ArrayList<String>();
		ArrayList<String> currenttrigram=new ArrayList<String>();
		
		while(s.hasNext())
		{
			String x;
			try
			{
				x=s.next().split("/")[1];
			}
			catch(Exception e)
			{
				x="UNK";
			}
			//train bigram counts in brown corpus
			if(currentbigram.size()==2)
			{
				if(bigramCount.containsKey(currentbigram.toString()))
				{
					
					int i=bigramCount.get(currentbigram.toString());
					
					i++;
					bigramCount.remove(currentbigram.toString());
					bigramCount.put(currentbigram.toString(), i);
					
				}
				else
				{
					bigramCount.put(currentbigram.toString(), 1);
				}
				currentbigram.set(0, currentbigram.get(1));
				currentbigram.remove(1);
			}
			else
			{
				currentbigram.add(x);
			}
			
			//now train trigram counts
			if(currenttrigram.size()==3)
			{
				if(trigramCount.containsKey(currenttrigram.toString()))
				{
					
					int i=trigramCount.get(currenttrigram.toString());
					i++;
				
					
					trigramCount.remove(currenttrigram.toString());
					trigramCount.put(currenttrigram.toString(), i);
					
				}
				else
				{
					
					
					trigramCount.put(currenttrigram.toString(), 1);
					
		
				}
				
				currenttrigram.set(0, currenttrigram.get(1));
				currenttrigram.set(1, currenttrigram.get(2));
				currenttrigram.remove(2);
			}
			else
			{
				
				currenttrigram.add(x);
				
			}
					
		}
	}
	public static void main(String args[])
	{
		try {
			new trainBigramTrigram();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*System.out.println("BIGRAMS:");
		for(Entry<String,Integer>e:trigramCount.entrySet())
		{
			System.out.println(e.getKey()+"=>"+e.getValue());
		}*/
		/*ArrayList<String>a=new ArrayList<String>();
		a.add("dt");a.add("bez");a.add("pps");*/
		
		System.out.println(trigramCount.get("[dt, bez, pps]"));
		
	}

	
}
