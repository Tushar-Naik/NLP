import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

class frequentCollocation
{
	//this class contains collocation and its no of occurences before a word in the corpus 
	ArrayList<String> collocation;
	int count;
}
public class trainCollocation 
{
	public static HashMap<String,frequentCollocation> wordAndCollocation=new HashMap<String,frequentCollocation>();
	public static ArrayList<String>returnIntersection(ArrayList<String> a,ArrayList<String> b)
	{
		ArrayList<String> c=new ArrayList<String>();
		for(String i:a)
		{
			for(String j:b)
			{
				if(i==j)
				{
					c.add(i);
				}
			}
			
		}
		return c;
	}
	
	public static void train() throws FileNotFoundException
	{
		ArrayList<String>prevk=new ArrayList<String>();
		Scanner s=new Scanner(new File("ca01"));
		while(s.hasNext())
		{
			String current=s.next().split("/")[0].toLowerCase();
			if(wordAndCollocation.containsKey(current))
			{
				frequentCollocation obj=new frequentCollocation();
				obj=wordAndCollocation.get(current);
				wordAndCollocation.remove(current);
				obj.collocation=returnIntersection(prevk, obj.collocation);
				obj.count++;
			}
			else
			{
				frequentCollocation obj=new frequentCollocation();
				obj.collocation=prevk;
				obj.count=1;
				wordAndCollocation.put(current, obj);
			}
			if(prevk.size()==10)
			{
				//here we add 10 words occurring to the right of prevk(0) to its collocation
				//then we delete prevk(0) from prevk
				int i;
				for(i=1;i<prevk.size();i++)
				{
					wordAndCollocation.get(prevk.get(0)).collocation.add(prevk.get(i));
				}
				//we now remove prevk(0) from prevk and shift everything in prevk 1 place to the left
				for(i=0;i<prevk.size()-1;i++)
				{
					prevk.set(i, prevk.get(i+1));
					
				}
				prevk.set(i,current);
			}
			else
				prevk.add(current);
			
		}
	}
	public static void main(String args[])
	{
		
		try {
			train();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Entry<String,frequentCollocation>e:wordAndCollocation.entrySet())
		{
			System.out.println(e.getKey()+"=>"+e.getValue().collocation);
			
		}
	}
	

}
