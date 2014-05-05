import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;


class WordToPosMap
{

	HashMap<String , ArrayList<String> > POSdict=new HashMap<String,ArrayList<String>>();
	 void populatePOSdict() throws FileNotFoundException
	{
		Scanner s=new Scanner(new File("merged"));
		while(s.hasNext())
		{
			String r=s.next();
			String key="";String val="";
			ArrayList<String> POSvalues=new ArrayList<String>();
			try
			{
				key=r.split("/")[0].toLowerCase();
				val=r.split("/")[1].toLowerCase();
			}
			catch(Exception e)
			{
				val="UNK";
			}
			
			if(POSdict.containsKey(key))
			{
				POSvalues=POSdict.get(key);
				POSvalues.add(val);
				
				
			}
			else
				POSvalues.add(val);
				
			POSdict.put(key,POSvalues);
				
		}
		
		s.close();
	}
	
	void printPOSdict()
	{
		for(Entry<String,ArrayList<String>> e:POSdict.entrySet())
		{
			System.out.println(e.getKey()+"\t\t\t"+e.getValue());
		}
	}
}

class trainPOSfromBrown
{
	public static void main(String args[])
	{
		WordToPosMap w=new WordToPosMap();
		try {
			w.populatePOSdict();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		w.printPOSdict();
		
		
	}
}
