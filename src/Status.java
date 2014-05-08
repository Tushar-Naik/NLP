import javax.swing.event.*;
import javax.swing.text.BadLocationException;

import java.awt.event.*;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class Status extends SwingWorker<Integer, Integer>
{
	JTextArea area;
	JFrame frame;
	NewSpellChecker obj;
	ArrayList<Character> separator = null; // for listening to every word
	ContextRec CR;
	public Status( JTextArea a, JFrame f) {
		// TODO Auto-generated constructor stub
		area=a;
		frame=f;
	}
    protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.
    	StatusBar statusBar=new StatusBar(area);
    	frame.add(BorderLayout.SOUTH, statusBar);
		return null;
    }

    protected void done()
    {
        System.out.println("Showed Status bar");
    }
}

class StatusBar extends JPanel {
	private JLabel statusLabel;

	public StatusBar(JTextArea area) {
		setLayout(new BorderLayout(2, 2));
		statusLabel = new JLabel("Wrods:"+area.getText().length());
		statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusLabel.setForeground(Color.black);
		add(BorderLayout.CENTER, statusLabel);
		JLabel dummyLabel = new JLabel(" ");
		dummyLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		add(BorderLayout.EAST, dummyLabel);
	}

}