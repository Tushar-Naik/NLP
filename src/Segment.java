import java.io.*;

import static java.util.Arrays.asList;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;
class unigramDistribution
{
	float wordCount;
	HashMap<String,Long> dict=new HashMap<String,Long>();
	unigramDistribution (String file)throws IOException
	{
		wordCount=0;
		BufferedReader in = new BufferedReader(new FileReader(file));
		for(String temp = ""; temp != null; temp = in.readLine())
		{
			String a[]=temp.split("\t");
			if(a.length>1)
			{
				//because of retarded error.Try without this line sometime.
				dict.put(a[0], Long.parseLong(a[1]));
				wordCount+=Long.parseLong(a[1]);
			}		
		}	
	}
	double getProbability(String word)
	{
		if(dict.containsKey(word))
			return (dict.get(word)/wordCount);
		else
			return(1/(wordCount*(Math.pow(10,word.length()-2))));			// ??
	}	
}
public   class Segment
{
	public static ArrayList<ArrayList<String>> split(String word)//split function, as in python code
	{
		ArrayList<ArrayList<String>> splits=new ArrayList<ArrayList<String>>() ;
		for(int i=1;i<=word.length();i++)
			splits.add(new ArrayList<String>(asList(word.substring(0,i),word.substring(i))));
		return splits;
	}
	public static double wordSequenceFitness(List<String> words,unigramDistribution o)
	{
		double sum=0;
		for(String s:words)
			sum=sum+Math.log10(o.getProbability(s));
		return sum;
	}
	public static List<String> segment(String input,unigramDistribution o)
	{
		ArrayList<ArrayList<String>> candidates=new ArrayList<ArrayList<String>>() ;
		HashMap<List<String>,Double>fitnessValues=new HashMap<List<String>,Double>();
		if("".equals(input))
			return(new ArrayList<String>(asList(""))); 
		input=input.toLowerCase();
		for(List<String>l:split(input))
		{	
			ArrayList<String>firstAndRem=new ArrayList<String>(asList(l.get(0)));
			firstAndRem.addAll(segment(l.get(1),o));
			candidates.add(firstAndRem);
		}
		for(List<String> c:candidates)
		{
			fitnessValues.put(c,wordSequenceFitness(c,o));
			System.out.println(fitnessValues);
		}
		Map.Entry<List<String>,Double> maxEntry = null;

		for (Entry<List<String>, Double> entry : fitnessValues.entrySet())
		 if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		       maxEntry = entry;
		return maxEntry.getKey();
	}
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		unigramDistribution singleWordProb=new unigramDistribution("count_big.txt");
		String word="word";
		split(word);
		List<String> a=new ArrayList<String>();
		for(String s:asList("i am a boy".split(" ")))
				a.add(s);
		System.out.println(wordSequenceFitness(a,singleWordProb));
		List<String> endResult=segment("ilovefrance",singleWordProb);
		for(String s:endResult)
			System.out.print(s+' ');
	}
}
	


