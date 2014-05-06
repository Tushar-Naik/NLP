import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;


public class TrainBigramTrigramWord {
	
	public static HashMap<String,Integer> bigramWordCount=new HashMap<String,Integer>();
	public static HashMap<String,Integer> trigramWordCount=new HashMap<String,Integer>();
	public TrainBigramTrigramWord() throws FileNotFoundException 
	{
		Scanner s=new Scanner(new File("merged"));
		ArrayList<String> currentbigram=new ArrayList<String>();
		ArrayList<String> currenttrigram=new ArrayList<String>();
		
		while(s.hasNext())
		{
			String x;
			try
			{
				x=s.next().split("[/,.;?*()-:`]")[0].toLowerCase();
			}
			catch(Exception e)
			{
				continue;
				//x="UNK";
			}
			//train bigram counts in brown corpus
			if(currentbigram.size()==2)
			{
				if(bigramWordCount.containsKey(currentbigram.toString()))
				{
					
					int i=bigramWordCount.get(currentbigram.toString());
					
					i++;
					bigramWordCount.remove(currentbigram.toString());
					bigramWordCount.put(currentbigram.toString(), i);
					
				}
				else
				{
					bigramWordCount.put(currentbigram.toString(), 1);
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
				if(trigramWordCount.containsKey(currenttrigram.toString()))
				{
					
					int i=trigramWordCount.get(currenttrigram.toString());
					i++;
				
					
					trigramWordCount.remove(currenttrigram.toString());
					trigramWordCount.put(currenttrigram.toString(), i);
					
				}
				else
				{
					
					
					trigramWordCount.put(currenttrigram.toString(), 1);
					
		
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
			new TrainBigramTrigramWord();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TRIGRAMS:");
		/*for(Entry<String,Integer>e:trigramWordCount.entrySet())
		{
			System.out.println(e.getKey()+"=>"+e.getValue());
		}*/
		ArrayList<String>a=new ArrayList<String>();
		a.add("ate");a.add("desert");
		ArrayList<String>b=new ArrayList<String>();
		b.add("ate");b.add("dessert");
		
		System.out.println(a.toString()+bigramWordCount.get(a.toString()));
		System.out.println(b.toString()+bigramWordCount.get(b.toString()));
		
	}

	
}

