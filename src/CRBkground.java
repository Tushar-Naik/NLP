import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
public class CRBkground extends SwingWorker<Integer, Integer>
{
	JTextArea area;
	NewSpellChecker obj;
	ArrayList<Character> separator = null; // for listening to every word
	ContextRec CR;
	public CRBkground( JTextArea a, ContextRec c) {
		// TODO Auto-generated constructor stub
		area=a;
		CR = new ContextRec(area);
	}
    protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.
    	
		if (CR.getSentenceCount() > 1) // If he has typed more than 2 sentences
		{
			try {
				CR.recognizer();

			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else
			CR.hidePopup();
		return null;
    }

    protected void done()
    {
        System.out.println("TRIED TO RECOGNIZE-----------");
    }
}