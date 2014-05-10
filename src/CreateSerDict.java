import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

class WordPopulate implements java.io.Serializable {

	public final static HashMap<String, Long> nWords = new HashMap<String, Long>();
	public final static HashMap<String, Long> nWords2 = new HashMap<String, Long>();
	static float wordCount=0;		// to keep track of probability, frequency
	public WordPopulate(String file) throws IOException 
	{
		//new LoadingBkground(nWords, nWords2, wordCount, file).execute();
		
		wordCount=0;
		BufferedReader in = new BufferedReader(new FileReader(file));
		//take in word by word, put it into hash map, with the count
		for(String temp = ""; temp != null; temp = in.readLine())
		{
			String a[]=temp.split("\t");
			if(a.length>1)
			{
				wordCount+=Long.parseLong(a[1]);;
				nWords2.put(a[0], Long.parseLong(a[1]));
			}
		}
		in.close();
		Scanner s=new Scanner(new File("merged"));
		while(s.hasNext())
		{
			String obj=s.next();
			String currentWord=obj.split("/")[0].toLowerCase();
			
			
			if(nWords.containsKey(currentWord))
			{
				Long count=nWords.get(currentWord);
				count++;
				nWords.remove(currentWord);
				nWords.put(currentWord, count);
			}
			else
				nWords.put(currentWord, (long) 1);
		}
	}
}

public class CreateSerDict  {

	
	public CreateSerDict() throws IOException
	{

		WordPopulate obj=new WordPopulate("count_big.txt");
		
		try
	      {
	         FileOutputStream fileOut = new FileOutputStream("nwords.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(obj.nWords);
	         out.close();
	         fileOut.close();

	         obj.nWords2.put("andromen",(long) obj.wordCount);
	         FileOutputStream fileOut2 = new FileOutputStream("nwords2.ser");
	         ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
	         out2.writeObject(obj.nWords2);
	         out2.close();
	         fileOut2.close();
	         
	         System.out.printf("Serialized data is saved in nwords.ser and nwords2.ser");
	        /* ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
	         out = new ObjectOutputStream(bos) ;
	         out.writeObject(obj);
	         out.close();

	         // Get the bytes of the serialized object
	         byte[] buf = bos.toByteArray();*/
	         
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
		
	}
	public CreateSerDict(NewSpellChecker obj) throws IOException
	{
		try
	      {
	         FileOutputStream fileOut = new FileOutputStream("nwords.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(obj.nWords);
	         out.close();
	         fileOut.close();

	         obj.nWords2.put("andromen",(long) obj.wordCount);		//total word count saved in a unique key
	         FileOutputStream fileOut2 = new FileOutputStream("nwords2.ser");
	         ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
	         out2.writeObject(obj.nWords2);
	         out2.close();
	         fileOut2.close();
	         
	         System.out.printf("Serialized data (edited dictionary) is saved in nwords.ser and nwords2.ser");
	        /* ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
	         out = new ObjectOutputStream(bos) ;
	         out.writeObject(obj);
	         out.close();

	         // Get the bytes of the serialized object
	         byte[] buf = bos.toByteArray();*/
	         
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			CreateSerDict o=new CreateSerDict();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
