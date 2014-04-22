import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;

import org.omg.CORBA.portable.InputStream;

public class ContextRec {
	int sentenceCount;
	JTextPane textarea;
	boolean contextRecUsed=false;		// variable, to remove the popup when he has clicked it.
	boolean popupBeingShown=false;		// variable to let the popup persist on the screen
	ArrayList<Character> separator=null;
	JPopupMenu popupMenu;
/*0*/	String letter[]={"to","from","dear","sir","madam","sub","subject","thank you","yours","faithfully","sincerely"};
/*1*/	String resume[]={"objective","education","institute","school","engineering","bachelor","skill","achievements","experience","projects","academics","intern","declaration"};
/*2*/	String code[]={"int","float","class","main","public","<","</"};
	String essay[]={"the","of"};
	String poem[]={"",""};
	String story[]={"",""};
	String tagArray[][]={letter,resume,code,essay,poem,story};			
	ArrayList<String> tags[]=new ArrayList[10];					//a list of all tags as an array of arraylists
	ArrayList<String> alreadyMatched = new ArrayList<String>();
	int n=6;
	int hitCount[]=new int[n];
	String query="";
	GoogleSearch gs;
	
	public ContextRec(JTextPane area)			//  constructor to initialize all variables
	{
		char sep[]={' ',',','\n','\t',';','.','?','(',')','!','@','#','-','_','[','{',']','}',':'};
		separator=new ArrayList<Character>(); for(char c:sep)separator.add(c);
		sentenceCount=0;
		textarea=area;
		popupMenu = new JPopupMenu();
		for(int i=0;i<n;i++)					//  add the tags to the arrayList array.
		{
			tags[i]=new ArrayList<String>();
			for(String s:tagArray[i])
			{
				tags[i].add(s);
				hitCount[i]=0;
			}
		}
	}

	public void hidePopup()
	{
		popupMenu.setVisible(false);
		popupBeingShown=false;
	}
	public void createPopup(JLabel jLab)			//  the contents of jlabel is shown as popup
	{
        popupBeingShown=true;
        
        popupMenu.removeAll();
        popupMenu.setOpaque(false);
        popupMenu.setBorder(null);
        popupMenu.add(jLab, BorderLayout.CENTER);
        popupMenu.addSeparator();
        popupMenu.add(new JLabel("Search Results"));
        final JTextField jt=new JTextField();
        jt.setToolTipText(query);
        JButton jb=new JButton("Quick Search");
        JSplitPane jsp=new JSplitPane();
        
        
        popupMenu.addSeparator();
        popupMenu.add(jt);
        popupMenu.add(jb);
        
        jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				query=jt.getText();
			}
		});
        try {
        	thid:
			gs=new GoogleSearch(query);
			JMenuItem links[] = new JMenuItem[gs.total];
			System.out.println("TOTAL SEARCH RESULTS"+gs.total);
			for(int i=0;i<gs.total;i++){
				String title=gs.results.getResponseData().getResults().get(i).getTitle();
				String a[]=title.split("<b>|</b>");
				title=title.replace("<b>","");
				title=title.replace("</b>","");
				final String hyperlink=gs.results.getResponseData().getResults().get(i).getUrl();
				System.out.println("Title: " + title);
				System.out.println("URL: " + hyperlink + "\n");
				links[i]=new JMenuItem(title);
				links[i].setToolTipText(hyperlink);
				links[i].addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						if (Desktop.isDesktopSupported()) {
		    				   Desktop desktop = Desktop.getDesktop();
		    				   try {
		    					       URI uri = new URI(hyperlink);
		    					       desktop.browse(uri);
		    					       } catch (IOException ex) {
		    					       ex.printStackTrace();
		    					       } catch (URISyntaxException ex) {
		    					       ex.printStackTrace();
		    					   }
		    				   }
						contextRecUsed=true;
					}
				});
				popupMenu.add(links[i]);
			}
		
        }
        catch (Exception e) {
			// TODO Auto-generated catch block
        	popupMenu.add(new JMenuItem("No internet connection found, to perform search"));
			e.printStackTrace();
		}
        
        popupMenu.show(textarea, 500, 300);							//  Right bottom of screen
        textarea.requestFocus();
	}
	public int getSentenceCount()		//  returns the number of sentences typed
	{
		String typedText=textarea.getText();
		sentenceCount=0;
		for(int i=0;i<typedText.length();i++)
			if(typedText.charAt(i)=='.')//||typedText.charAt(i)=='\n')&&typedText.charAt(i+1)==' ')
				sentenceCount++;
		return sentenceCount;
	}
	public int getMax()					//   return the index of the tag with the maximum hits
	{
		int i,max=hitCount[0],index=0;
		boolean hitsExist=false;
		for(i=0;i<n;i++)
		{
			if(hitCount[i]>1)				// 0 or 1 or 2 ----- CHECK THIS
			{
				hitsExist=true;
				break;
			}
		}
		if(!hitsExist)
			return -1;
		for(i=1;i<n;i++)
		{
			if(hitCount[i]>max)							//------------------------- NEED TO TAKE CARE OF TIES
			{
				max=hitCount[i];
				index=i;
			}
		}
		return index;
	}
	
	public void recognizer() throws BadLocationException		//   the main concept of recognizing the context
	{
		alreadyMatched.removeAll(alreadyMatched);
		if(contextRecUsed)
		{
			hidePopup();
			return;
		}		
		System.out.println("Sentence count= "+sentenceCount);
		//textarea.getDocument().insertString(caretPos, "!SENT", null);
        String textTyped=textarea.getText().toLowerCase();
        String word="";
        int i;
        // empty the hitcount for all of the context types
        for(i=0;i<n;i++) hitCount[i]=0;
        // extract each word and increment hitCount if the word is a tag
        for(i=0;i<textTyped.length();i++)
        {
        	char c=textTyped.charAt(i);
        	if(separator.contains(c))
        	{
        		for(int j=0;j<n;j++)
        		{
        			if(tags[j].contains(word)&&word.length()>1 && ! alreadyMatched.contains(word))
        			{
        				hitCount[j]++;
        				System.out.println("HITS:"+word+hitCount[j]);
        				alreadyMatched.add(word);
        			}
        		}
        		word="";
        	}
        	else word=word+c;
        }
        int index=getMax();		// to know which had the context type had the most hits
        System.out.println("index selected="+index);
        switch(index)
        {
        	case -1:System.out.println("No hits");
        			hidePopup();
        			return;
        	case 0: letterTheme();        			
        			break;
        	case 1: resumeTheme();        			
					break;
        	case 2: codeTheme();        			
					break;
					/*
        	case 3: Theme();        			
					break;
        	case 4: Theme();        			
					break;
        	case 5: Theme();        			
					break;
					*/
        	default: hidePopup();
        }
	}
	public void letterTheme()
	{
		JLabel jLab;
		String s="<html>Are you typing a Letter?<br>Do you need some help?</html>";
		jLab=new JLabel(s);
		jLab.setFont(new Font("Segoe UI", 0, 16));
		query="help to type a letter";
		createPopup(jLab);
	}
	public void resumeTheme()
	{
		JLabel jLab;
		String s="<html>Are you typing a resume?<br>Do you need some help?</html>";
		jLab=new JLabel(s);
		jLab.setFont(new Font("Segoe UI", 0, 16));
		query="help to type a resume";
		createPopup(jLab);
	}
	public void codeTheme()
	{
		JLabel jLab;
		String s="<html>Are you typing a code?<br>Do you need some help?</html>";
		jLab=new JLabel(s);
		jLab.setFont(new Font("Segoe UI", 0, 16));
		query="good coding techniques";
		createPopup(jLab);
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new LiveAuto();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
