import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;


@SuppressWarnings("serial")
public class BuildUnixWordsDict implements java.io.Serializable
{
	HashMap<String, Long>UnixWordsDict=new HashMap<String,Long>();
	HashMap<String, Long> BrownWordsDict=new HashMap<String,Long>();
	
	@SuppressWarnings("unchecked")
	public BuildUnixWordsDict() throws FileNotFoundException
	{
		try
	      {
	         FileInputStream fileIn = new FileInputStream("nwords.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         BrownWordsDict = (HashMap<String, Long>) in.readObject();
	         in.close();
	         fileIn.close();
	         
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("nwords not found");
	         c.printStackTrace();
	         return;
	      }
		
		Scanner s=new Scanner(new File("unixwords.txt"));
		while(s.hasNext())
		{
			String currword=s.next();
			//System.out.println(currword);
			if(BrownWordsDict.containsKey(currword))
			{
				UnixWordsDict.put(currword, BrownWordsDict.get(currword));
			}
		}
		try
	      {
			System.out.println("asassa");
	         FileOutputStream fileOut = new FileOutputStream("unixwords.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(UnixWordsDict);
	         out.close();
	         fileOut.close();
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }

	
	}
	public static void main(String args[])
	{
		BuildUnixWordsDict b=null;
		try {
			b=new BuildUnixWordsDict();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	System.out.println(b.UnixWordsDict);	
	}
}

