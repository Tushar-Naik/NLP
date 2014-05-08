import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
public class UnderlineBkground extends SwingWorker<Integer, Integer>
{
	JTextArea area;
	NewSpellChecker obj;
	ArrayList<Character> separator = null; // for listening to every word
	ContextRec CR;
	public UnderlineBkground( JTextArea a, NewSpellChecker ob, ArrayList<Character> sep) {
		// TODO Auto-generated constructor stub
		area=a;
		obj=ob;
		separator=sep;
		CR = new ContextRec(area);
	}
	public boolean isNumber(String w)
	{
		int a;
		try{
			a=Integer.parseInt(w);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
    protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.
    	int i;
    	area.getHighlighter().removeAllHighlights();
		String textTyped=area.getText().toLowerCase();
		String word="";
		//int caretAddidtion=0;			// to equate caret position and area.getText().length() (new line takes 2 caret positions)
		System.out.println("------------------IN INDICATE---------------");
		//System.out.println("CARET:"+area.getCaretPosition()+" LENGHT:"+textTyped.length()+textTyped);
		//System.out.println("TYPEDTEXT:"+textTyped);
		for(i=0;i<textTyped.length();i++)
	       {
			char c=textTyped.charAt(i);
			if(separator.contains(c)||c=='\n'||Character.isWhitespace(c)||Character.isSpace(c))
	       	{
	       		//System.out.println("IN SEPARATOR TRUE:"+c+"|");
	       		if (word.length() > 1 && !isNumber(word))
	       		{
	       		//	System.out.println("WORD:"+word+"|");
	    				
	    				if(!obj.nWords.containsKey(word))
	    				{
	    				//	System.out.println("WORD:"+word+" NOT PRESENT");   					
	    			//		System.out.println("TO HIGHLIGHT:  i="+i+" len="+word.length()+" word="+word);
	    					//Highlight from indexOfWord to indexOfWord+word.length
	    					Highlighter.HighlightPainter painter = new UnderlineHighlighter.UnderlineHighlightPainter(Color.red);
	    					Highlighter highlighter = area.getHighlighter();
	    					try {
								//highlighter.addHighlight(i-caretAddidtion-word.length(),i-caretAddidtion, painter);
	    						highlighter.addHighlight(i-word.length(),i, painter);
							} catch (BadLocationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	    				}
	    		}
	       		word="";
	       	}
	       	else 
	       		word=word+c;
	       }
		return null;
    }

    protected void done()
    {
        System.out.println("DONE UNDERLINING-----------");
    }
}