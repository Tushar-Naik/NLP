import java.io.FileNotFoundException;
import java.util.ArrayList;


public class CalcSentScore {

	trainBigramTrigram tbt;
	trainWordCount twc;
	trainWordWithPos twwp;
	genCandidateSent g;
	getPOS gpos;
	ArrayList<String> candSent=new ArrayList<String>();
    public CalcSentScore()
    {
    	try {
			tbt=new trainBigramTrigram();
	    	twc= new trainWordCount();
	    	twwp=new trainWordWithPos();
	    	g=new genCandidateSent();
	    	gpos=new getPOS();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    float BestSentence(String sent)
    {
    	/*ArrayList<String> prev3=new ArrayList<String>();
    	for(String word:sent)
    	{
    		
    	}*/
    	boolean isTriGram=true;
    	String s[]=sent.split(" ");
    	float totalScore=(float) 0.0;
    	float pWGivenT=1;
    	float pTtriGram=1;
    	for(int i=0;i<s.length;i++)
    	{
    		System.out.println(s[i]);
    		String currentPos=gpos.getMaxPOS(s[i]);
    		
    		pWGivenT=pWGivenT*(twwp.wordWithPosCount.get(s[i]+"/"+currentPos))/twc.posCount.get(currentPos);
    		isTriGram=true;
    		if(i<2)
    		{
    			continue;
    		}
    		ArrayList<String> posTriGram = new ArrayList<String>();
    		posTriGram.add(gpos.getMaxPOS(s[i-2]));
    		posTriGram.add(gpos.getMaxPOS(s[i-1]));
    		posTriGram.add(gpos.getMaxPOS(s[i]));
    		ArrayList<String> posBiGram = new ArrayList<String>();
    		posBiGram.add(gpos.getMaxPOS(s[i-2]));
    		posBiGram.add(gpos.getMaxPOS(s[i-1]));
    		
    		pTtriGram=pTtriGram*(tbt.trigramCount.get(posTriGram.toString()))/(tbt.bigramCount.get(posBiGram.toString()));

    		System.out.println(pTtriGram+" "+pWGivenT);
    	}
    	totalScore=pWGivenT*pTtriGram;
    	return totalScore;
    }
    public static void main(String args[])
    {
    	String a="this is there";
    	String b="this is it";
    	CalcSentScore c=new CalcSentScore();
    	//System.out.println(c.BestSentence(a));
    	System.out.println(c.BestSentence(b));
    }
    
}
