import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;


public class trainWordWithPos {
	public static HashMap<String,Integer>wordWithPosCount=new HashMap<String,Integer>();
	public trainWordWithPos() throws FileNotFoundException
	{
		Scanner s=new Scanner(new File("merged"));
		while(s.hasNext())
		{
			String wordAndPos;
			
			String x=s.next();
			try
			{
				 wordAndPos=x.split("/")[0].toLowerCase()+"/"+x.split("/")[1];	
			}
			catch(Exception e)
			{
				wordAndPos=x.split("/")[0].toLowerCase()+"/"+"UNK";
			}
			
			if(wordWithPosCount.containsKey(wordAndPos))
			{
				int count=wordWithPosCount.get(wordAndPos);
				count++;
				wordWithPosCount.remove(wordAndPos);
				wordWithPosCount.put(wordAndPos, count);
			}
			else
				wordWithPosCount.put(wordAndPos, 1);
		}
	}/*
	public static void main(String args[])
	{
		trainWordWithPos t= null;
		try {
			t= new trainWordWithPos();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Entry<String,Integer>e:wordWithPosCount.entrySet())
			System.out.println(e.getKey()+"="+e.getValue());
	}

*/
}
