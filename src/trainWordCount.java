import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;


public class trainWordCount {
	public  HashMap<String,Integer>wordCount=new HashMap<String,Integer>();
	public  HashMap<String,Integer>posCount=new HashMap<String,Integer>();
	public  trainWordCount() throws FileNotFoundException
	{
		Scanner s=new Scanner(new File("merged"));
		while(s.hasNext())
		{
			String obj=s.next();
			String currentWord=obj.split("/")[0].toLowerCase();
			
			
			if(wordCount.containsKey(currentWord))
			{
				int count=wordCount.get(currentWord);
				count++;
				wordCount.remove(currentWord);
				wordCount.put(currentWord, count);
			}
			else
				wordCount.put(currentWord, 1);
			
			String currentPos;
			try
			{
				 currentPos=obj.split("/")[1].toLowerCase();
			}
			catch(Exception e)
			{
				currentPos="UNK";
				
			}
			
			
			
			if(posCount.containsKey(currentPos))
			{
				int count=posCount.get(currentPos);
				count++;
				posCount.remove(currentPos);
				posCount.put(currentPos, count);
			}
			else
				posCount.put(currentPos, 1);
			
			
		}
		s.close();
	}
	/*public  void populatePosCount() throws FileNotFoundException
	{
		Scanner x=new Scanner(new File("merged"));
		while(x.hasNext())
		{
			String currentPos;
			try
			{
				 currentPos=x.next().split("/")[1].toLowerCase();
			}
			catch(Exception e)
			{
				currentPos="UNK";
				
			}
			
			
			
			if(posCount.containsKey(currentPos))
			{
				int count=posCount.get(currentPos);
				count++;
				posCount.remove(currentPos);
				posCount.put(currentPos, count);
			}
			else
				posCount.put(currentPos, 1);
			
			
		}
		
	}*/
	public static void main(String args[])
	{
		trainWordCount t = null;
		try {
			t = new trainWordCount();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(Entry<String,Integer>e:t.posCount.entrySet())
			System.out.println(e.getKey()+"="+e.getValue());
		//System.out.println(wordCount.get("forensic"));
		String x="a/b";
		String y=x.split("/")[1];
		System.out.println(y);
				
	}

}
