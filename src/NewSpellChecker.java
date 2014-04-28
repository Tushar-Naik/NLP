
import static java.util.Arrays.asList;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;
public class NewSpellChecker {

	int wordcase=0;		//0:all lower case,  1:1st caps  2: All caps
	public final static HashMap<String, Long> nWords = new HashMap<String, Long>();
	static float wordCount;		// to keep track of probability, frequency
	public NewSpellChecker(String file) throws IOException 
	{
		wordCount=0;
		BufferedReader in = new BufferedReader(new FileReader(file));
		//take in word by word, put it into hash map, with the count
		for(String temp = ""; temp != null; temp = in.readLine())
		{
			String a[]=temp.split("\t");
			if(a.length>1)
			{
				wordCount+=Long.parseLong(a[1]);;
				nWords.put(a[0], Long.parseLong(a[1]));
			}
		}
		in.close();
	}
	String caseHandler(String w)
	{
		System.out.println("WORD:"+w+" wordcase="+wordcase);
	
		switch(wordcase)
		{
		case 0: return w;
		case 1: return makeCamelcase(w);
		case 2: return makeUppercase(w);
		default: System.out.println("Case error");
		return w;
		}
	}
	String makeCamelcase(String w)
	{
		System.out.println("Camel case entered. Word will be:"+Character.toUpperCase(w.charAt(0))+w.substring(1));
		return Character.toUpperCase(w.charAt(0))+w.substring(1);
	}
	String makeUppercase(String w)
	{
		String ans="";
		for(int i=0;i<w.length();i++)
		{
			ans+=Character.toUpperCase(w.charAt(i));
		}
			
		return ans;
	}
	private final ArrayList<String> edits(String word) 
	{
		ArrayList<String> result = new ArrayList<String>();  //list of all words within edit distance of 1
		// deletion
		for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
		// transposition
		for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		// substitution
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
		// addition
		for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
		return result;
		
	}
	public final ArrayList<String> getCandidates(String word)
	{
		return null;
	}
	HashMap<Long, String> candidates=null;
	public final String correct(String word) 
	{
		int c;
		wordcase=0;
		for(c=0;c<word.length();c++)
		{
			char ch=word.charAt(c);
			if(	Character.isLowerCase(ch))
				break;
			wordcase=1;
		}
		if(c==word.length())
			wordcase=2;
	
		System.out.println("WORD:"+word+" wordcase="+wordcase);
		String word1=word.toLowerCase();			//*
		if(nWords.containsKey(word1)) return word;			//* check if word is in the dict
		ArrayList<String> list = edits(word1);
		candidates = new HashMap<Long, String>();
		System.out.println("1:"+caseHandler(word));
		candidates.put((long) 0, caseHandler(word));
		// edit distance 1
		for(String s : list) if(nWords.containsKey(s)) candidates.put(nWords.get(s),caseHandler(s));
		if(candidates.size() > 1)
		{
			//System.out.println(word+" : "+candidates);			//*
			return candidates.get(Collections.max(candidates.keySet()));
		}
		//edit distance 2
		for(String s : list) for(String w : edits(s)) if(nWords.containsKey(w))
			candidates.put(nWords.get(w),caseHandler(w));
		//System.out.println(candidates.size());
		//return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
		if(candidates.size()>1)                     
			return candidates.get(Collections.max(candidates.keySet()));
		
		// lastly check for segmentation
		String splitString="";
		List<String> endResult=segment(word);
		for(String s:endResult)
			splitString=splitString+s+" ";
		splitString=splitString.trim();
		return splitString;
	}
//----------------------------   word segmentation logic  ---------------------------
	// Eg. "ilovindia"  to "i love india"
	
	public static ArrayList<ArrayList<String>> split(String word)//split function, as in python code
	{
		ArrayList<ArrayList<String>> splits=new ArrayList<ArrayList<String>>() ;
		for(int i=1;i<=word.length();i++)
			splits.add(new ArrayList<String>(asList(word.substring(0,i),word.substring(i))));
		return splits;
	}
	public static double getProbability(String word)
	{
		if(nWords.containsKey(word))
			return (nWords.get(word)/wordCount);
		else
			return(1/(wordCount*(Math.pow(10,word.length()-2))));			// ??
	}	
	public static double wordSequenceFitness(List<String> words)
	{
		double sum=0;
		for(String s:words)
			sum=sum+Math.log10(getProbability(s));
		return sum;
	}
	//returns the list of all words after segmentation 
	public static List<String> segment(String input)
	{
		ArrayList<ArrayList<String>> candidates=new ArrayList<ArrayList<String>>() ;
		HashMap<List<String>,Double>fitnessValues=new HashMap<List<String>,Double>();
		if("".equals(input))
			return(new ArrayList<String>(asList(""))); 
		input=input.toLowerCase();
		for(List<String>l:split(input))
		{	
			ArrayList<String>firstAndRem=new ArrayList<String>(asList(l.get(0)));
			firstAndRem.addAll(segment(l.get(1)));
			candidates.add(firstAndRem);
		}
		for(List<String> c:candidates)
			fitnessValues.put(c,wordSequenceFitness(c));
		Map.Entry<List<String>,Double> maxEntry = null;

		for (Entry<List<String>, Double> entry : fitnessValues.entrySet())
		 if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		       maxEntry = entry;
		return maxEntry.getKey();
	}
	public static void main(String args[]) throws IOException {
		
		NewSpellChecker obj=new NewSpellChecker("count_big.txt");
		System.out.println("Enter the string:");
		String s="ilovfrance";
		BufferedReader BR=new BufferedReader(new InputStreamReader(System.in));
		/*String stmt=BR.readLine();
		String seg[]=stmt.split(" ");
		for(String s:seg)*/
		System.out.println((s+" : ")+obj.correct(s)+"\nLIST");
		List<String> endResult=segment("ilovefrance");
		for(String s1:endResult)
			System.out.print(s1+' ');
		
	}
}