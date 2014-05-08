import java.io.*;
import java.util.*;
import java.util.regex.*;
class Segmentation
{
	/**
	 * @author Tushar Naik, Vamanan TS, Suhas
	 * 
	 * basically, implement segmentation recursively, which takes in a text without space, and tries to split it.
	 */
	static String dictionary[] = {"mobile","samsung","sam","sung","man","mango",
		    "icecream","and","go","i","like","ice","cream","a"};	

	private final static HashMap<String, Integer> nWords = new HashMap<String, Integer>();
	static ArrayList<String> Split = new ArrayList<String>();


	NewSpellChecker obj;
	public Segmentation() throws NumberFormatException, IOException
	{
		obj=new NewSpellChecker("count_big.txt");
		BufferedReader in = new BufferedReader(new FileReader("corpus5000.txt"));
		int rank=1;
		for(String temp = ""; temp != null; temp = in.readLine()){
			String a="";
			for(int i=0;i<temp.length();i++)
			{
				char c=temp.charAt(i);
				if(Character.isAlphabetic(c))
				 a=a+c;
			}
			nWords.put(a, rank++);
			
		}
	}
	public void display(String s,int n,String result)
	{
		String prefix="";
		//System.out.println("result:"+result);
		//System.out.println("n="+n);
		/*This function recursively goes through s, checks prefixes in the dictionary
		 stores the prefixes in result seperated by space and prints the end result upon reaching
		 the end of s*/
		
		for(int i=1;i<=n;i++)
		{			
			prefix=s.substring(0,i);
			if(i==n){
				if(!nWords.containsKey(prefix))
				{
					String prefix2=obj.correct(prefix,null);
					if(prefix!=prefix2)
					{
						result+=prefix2;
						System.out.println(result);
					}
				}
				else
				{
					result+=prefix;
					System.out.println(result);
				}
			}
			if(nWords.containsKey(prefix))
			{
				//System.out.println("prefix:"+prefix);
				//System.out.println(prefix);
				
				//System.out.println("i="+i+" n="+n);
				//System.out.println("substr:"+s.substring(i,n));
				display(s.substring(i,n),n-i,result+prefix+" ");				
			}
		}			
	}
	public static void main(String[] args) throws NumberFormatException, IOException
	{		
		String s="lawntenis";
		Segmentation obj=new Segmentation();
		
		obj.display(s,s.length(),"");	
	}
}
	


