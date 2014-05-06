import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
public class LoadingBkground extends SwingWorker<Integer, Integer>
{
	//JTextArea area;
	//NewSpellChecker obj;
	int wordcase=0;		//0:all lower case,  1:1st caps  2: All caps
	public HashMap<String, Long> nWords;// = new HashMap<String, Long>();
	public HashMap<String, Long> nWords2;// = new HashMap<String, Long>();
	float wordCount;		// to keep track of probability, frequency
	String file;
	public LoadingBkground( HashMap<String, Long> nw,HashMap<String, Long> nw2, float wc, String f) {
		// TODO Auto-generated constructor stub
		nWords=nw;
		nWords2=nw2;
		file=f;
		wordCount=wc;
	}
    protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.
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


		return null;
    }

    protected void done()
    {
        System.out.println("DONE LAODING ALL DICTS----------------");
        System.out.println("Count"+wordCount+" "+nWords2.size()+" "+nWords.size());
        
    }
}