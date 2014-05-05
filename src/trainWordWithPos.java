import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;


public class trainWordWithPos {
	public static HashMap<String,Integer>wordCount=new HashMap<String,Integer>();
	public static void populateWordCount() throws FileNotFoundException
	{
		Scanner s=new Scanner(new File("ca01"));
		while(s.hasNext())
		{
			String x=s.next();
			String wordAndPos=x.split("/")[0].toLowerCase()+"/"+x.split("/")[1];
			if(wordCount.containsKey(wordAndPos))
			{
				int count=wordCount.get(wordAndPos);
				count++;
				wordCount.remove(wordAndPos);
				wordCount.put(wordAndPos, count);
			}
			else
				wordCount.put(wordAndPos, 1);
		}
	}
	public static void main(String args[])
	{
		try {
			populateWordCount();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Entry<String,Integer>e:wordCount.entrySet())
			System.out.println(e.getKey()+"="+e.getValue());
	}


}
