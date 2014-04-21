
import java.io.*;
import java.util.*;
public class SpaceError {


	private final static HashMap<String, Long> nWords = new HashMap<String, Long>();
	static ArrayList<String> Split = new ArrayList<String>();

	
	public SpaceError(String file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		for(String temp = ""; temp != null; temp = in.readLine()){
			String a[]=temp.split("\t");
			if(a.length>1)
			nWords.put(a[0], Long.parseLong(a[1]));
		}
		in.close();
		/*
		nWords.put("a",1l);
		nWords.put("i",1l);
		nWords.put("have",1l);
		nWords.put("new",1l);
		nWords.put("hello",1l);
		nWords.put("and",1l);
		nWords.put("anda",1l);*/
	}
	public boolean wordBreak(String str)
	{
		System.out.println(str);
		int size=str.length(),i;
		if(size==0) return true;
		if(size==1)
		{
			if(str=="a"||str=="i")
				return true;
			else return false;
		}
		for(i=1;i<=size;i++)
			if(nWords.containsKey(str.substring(0, i))&&wordBreak(str.substring(i,size)) )
			{
				Split.add(str.substring(0,i));
				return true;
			}
		return false;
		
	}

	public static void main(String args[]) throws IOException {
		
		SpaceError obj=new SpaceError("count_big.txt");
		System.out.println("Enter the string:");
		System.out.println("asd"+nWords.containsKey("asd")+"  "+("tushar".substring(0,3))+ " "+("tushar".substring(3,6)));
		BufferedReader BR=new BufferedReader(new InputStreamReader(System.in));
		String stmt=BR.readLine();
		String seg[]=stmt.split(" ");
		for(String s:seg)
		System.out.println((s+" : ")+obj.wordBreak(s));
		System.out.println(Split);
	}

}