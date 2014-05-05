import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;


public class genCandidateSent {
	trainWordCount t;
	public genCandidateSent()
	{
		try {
			t=new trainWordCount();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String>replaceInSent(ArrayList<String> input,int pos,String word)
	{
		input.set(pos, word);
		return input;
		
	}
	public ArrayList<String> genCandWord(String word)
	{
		ArrayList<String> validCandidates=new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();  //list of all words within edit distance of 1
		// deletion
		for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
		// transposition
		for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		// substitution
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
		// addition
		for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
		for(String x:result)
			if(t.wordCount.containsKey(x)&&!validCandidates.contains(x)&&t.wordCount.get(x)>1)
			{
				validCandidates.add(x);
			}
		return validCandidates;
	}
	public String toSentence(ArrayList<String>input)
	{
		String sent="";
		for(String s:input)
			sent=sent+s+" ";
		sent=sent.trim();
		return sent;
	}
	public ArrayList<String> genCandidates(String sent)
	{
		ArrayList<String>candidateForWord=new ArrayList<String>();
		ArrayList<String>candidateSents=new ArrayList<String>();
		String s1[]=sent.split(" ");
		for(int i=0;i<s1.length;i++)
		{
			ArrayList<String> input=new ArrayList<String>();
			for(String s:s1) input.add(s);
			candidateForWord=genCandWord(s1[i]);
			for(String x:candidateForWord)
			{
				input.set(i, x);
				
				//System.out.println(input);
				String temp=toSentence(input);
				//System.out.println(temp);
				candidateSents.add(temp);
			}
		}
		return candidateSents;
	}
	public static void main(String args[])
	{
		String s="this is not it";
		genCandidateSent x=new genCandidateSent();
		System.out.println(x.genCandidates(s));
		
		
	}
}
