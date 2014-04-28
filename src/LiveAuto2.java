import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
public class LiveAuto2 extends JFrame {

	/** A text editor using java that implements, (1) a live contextual Spell Checker-Corrector using that
	 * Bayesian model, n-grams and the noisy channel model, (2) a Context Recognizer using probabilistic
	 * tagging, (3) an Auto Complete feature using a hash map, and (4) a Regex Find option.
	 */
	
	private static final long serialVersionUID = 1L;
	//Editor Declarations
	private JTextPane area = new JTextPane();
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir")); // used to provide GUI to navigate FileSystem
	JScrollPane jlsc= new JScrollPane();
	private String currentFile = "Untitled";
	JToolBar tool;
	//button variables
	boolean spellCheckOn;
	boolean autoCompleteOn;
	String beforeWordText="";
	String afterWordText="";
	private boolean changed = false;	// to differentiate between save and saveAs
	char prevChar;
	
	ArrayList<Character> separator=null;	//for listening to every word
	char separatorUsed;						// to print back the separator after correction
	boolean suggestion=false;	//variable to make sure that ......................
	ArrayList<String> Completable=null;		//list of all words, that have been typed by user, of length >7
	final JList<String> candidateList = new JList<String>();	//list of all words, that can fix the wrong one.
	/*Create all objects to save time*/
	NewSpellChecker obj;
	ContextRec CR;

	public LiveAuto2() throws IOException {
		spellCheckOn = true;
		autoCompleteOn = true;
		prevChar=' ';
		Completable=new ArrayList<String>();
		char sep[]={' ',',','\n','\t',';','.'};
		separator=new ArrayList<Character>(); for(char c:sep)separator.add(c);
		obj = new NewSpellChecker("count_big.txt");
		initPanel();
		area.setFont(new Font("Segoe UI", 0, 14));
		// add(scroll, BorderLayout.CENTER);

		setLayout(new FlowLayout());
		JMenuBar JMB = new JMenuBar();
		setJMenuBar(JMB);
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMB.add(file);
		JMB.add(edit);
		file.add(New);
		file.add(Open);
		file.add(Save);
		file.add(SaveAs);
		file.add(Quit);
		file.addSeparator();
		for (int i = 0; i < 4; i++)
			file.getItem(i).setIcon(null);
		edit.add(Cut);
		edit.add(Copy);
		edit.add(Paste);
		edit.getItem(0).setText("Cut");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");
		tool = new JToolBar();
		add(tool, BorderLayout.NORTH);
		tool.add(New);
		tool.add(Open);
		tool.add(Save);
		tool.addSeparator();
		JButton cut = tool.add(Cut), cop = tool.add(Copy), pas = tool
				.add(Paste);
		tool.addSeparator();
		final JToggleButton spellOn = new JToggleButton("Spell Check");
		spellOn.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					spellCheckOn = true;
					spellOn.setText("Spell Checker ON");
				} else {
					spellCheckOn = false;
					spellOn.setText("Spell Checker OFF");
				}
				area.requestFocus();
			}
		});
		final JToggleButton completeOn = new JToggleButton("Auto Complete");
		completeOn.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					autoCompleteOn = true;
					completeOn.setText("Auto Complete ON");
				} else {
					autoCompleteOn = false;
					completeOn.setText("Auto Complete OFF");
				}
				area.requestFocus();
			}
		});
		cut.setText(null);
		cut.setIcon(new ImageIcon("cut.gif"));
		cop.setText(null);
		cop.setIcon(new ImageIcon("copy.gif"));
		pas.setText(null);
		pas.setIcon(new ImageIcon("paste.gif"));

		spellOn.setSelected(true);
		completeOn.setSelected(true);
		tool.add(spellOn);
		tool.add(completeOn);
		tool.addSeparator(new Dimension(50,50));
		jlsc.setPreferredSize(new Dimension(120,60));
		tool.add(jlsc);
		Save.setEnabled(false);
		SaveAs.setEnabled(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		area.addKeyListener(k1);
		setTitle(currentFile);
		// area.setSize(300, 300);
		area.setPreferredSize(new Dimension(500, 500));
		area.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
		// area.setBounds(0,0,200,200);
		JScrollPane scroll = new JScrollPane(area);
		add(scroll, BorderLayout.CENTER);
		area.requestFocus();
		CR=new ContextRec(area);
		area.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e))
				{
					//get the caret position of the right click
					JEditorPane editor = (JEditorPane) e.getSource();
				    Point pt = new Point(e.getX(), e.getY());
				    int pos = editor.viewToModel(pt);
					String typedText=area.getText();
					String word="";
					//extract the word at the caret position
					if(!separator.contains(typedText.charAt(pos)))  // check if a word exists in that position
					{
						int i=pos;
						while(i<typedText.length()&&!separator.contains(typedText.charAt(i)))
							word=word+typedText.charAt(i++);
						afterWordText=typedText.substring(i);
						System.out.println("after word= "+afterWordText);
						i=pos-1;
						while(i>=0&&!separator.contains(typedText.charAt(i)))
							 word=typedText.charAt(i--)+word;
						beforeWordText=typedText.substring(0,i+1);
						System.out.println("before word= "+beforeWordText);
						System.out.println("WORD on right click: "+word);
					}
					// if the word is error free, "nothing doing" else, show candidates
					if(!obj.nWords.containsKey(word))
					{
						String correctWord=obj.correct(word);
						if (obj.candidates != null && obj.candidates.size() > 1) { // Concept to show candidate words as list

							ArrayList<String> cand = new ArrayList<String>(
									obj.candidates.values());
							String allCandidates[] = new String[cand.size()];
							allCandidates = cand.toArray(allCandidates); // convert ArrayLIst to String[]
							candidateList.setListData(allCandidates);
							jlsc.setViewportView(candidateList);
							jlsc.setVisible(true);
							// Add listener to JList
							candidateList.addListSelectionListener(new ListSelectionListener() {

										@Override
										public void valueChanged(ListSelectionEvent arg0) {
											// change the previous word
											if (candidateList.getSelectedValue() != null) {
												 
												String wordSelected = candidateList.getSelectedValue();
												area.setText(beforeWordText + wordSelected	+ afterWordText);
												indicateErrors();
												area.requestFocus();
											}
										}
									});
							
						}
						else
						{
							JLabel jl=new JLabel("No suggestions");
							jlsc.setViewportView(jl);
							jlsc.setVisible(true);
							
						}
					}
			     }
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		setSize(800, 700);
		setVisible(true);
	}

	Action New = new AbstractAction("New", new ImageIcon("new.gif")) {
		public void actionPerformed(ActionEvent e) {
			// saveOld();
			try {
				new LiveAuto();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};
	Action Open = new AbstractAction("Open", new ImageIcon("open.gif")) {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};
	Action Save = new AbstractAction("Save", new ImageIcon("save.gif")) {
		public void actionPerformed(ActionEvent e) {
			if (!currentFile.equals("Untitled"))
				saveFile(currentFile);
			else
				saveFileAs();
		}
	};
	Action SaveAs = new AbstractAction("Save as...") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	Action Quit = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};
	ActionMap m = area.getActionMap();
	Action Cut = m.get(DefaultEditorKit.cutAction);
	Action Copy = m.get(DefaultEditorKit.copyAction);
	Action Paste = m.get(DefaultEditorKit.pasteAction);

	private void saveFileAs() {
		if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}

	private void saveOld() {
		if (changed) {
			if (JOptionPane.showConfirmDialog(this, "Would you like to save "
					+ currentFile + " ?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				saveFile(currentFile);
		}
	}

	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			area.read(r, null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,
					"Editor can't find the file called " + fileName);
		}
	}

	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			area.write(w);
			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
		} catch (IOException e) {
		}
	}
//-----------------------------   Suggestion Panel   ------------------------------------------
	public JList list;
    public JPopupMenu popupMenu;
    public String subWord;
    public int insertionPosition;
    public JTextPane textarea;

    public void initPanel()
    {
    	textarea=area;
    	insertionPosition=0;
    	popupMenu=null;
    }/*
    public void init(JTextPane area)
    {
    	textarea=area;
    	insertionPosition=0;
    	popupMenu=null;
    }*/
    public void SuggestionPanels(JTextPane textarea, int position, String subWord, Point location) {
        this.insertionPosition = position;
        this.subWord = subWord;
        popupMenu = new JPopupMenu();
        popupMenu.removeAll();
        popupMenu.setOpaque(false);
        popupMenu.setBorder(null); 
        if(createSuggestionList(position, subWord))
        {popupMenu.add(list, BorderLayout.CENTER);
        popupMenu.show(textarea, location.x, textarea.getBaseline(0, 0) + location.y+20);
        }
       // printval();
    }

    public void hide() {
    	suggestion=false;
    	if(popupMenu!=null)
        popupMenu.setVisible(false);
    }
//------------------ Actual Logic of Autocomplete ------------------------
    public boolean createSuggestionList(final int position, final String subWord) {
        ArrayList<String> data=new ArrayList();
        for (int i = 0; i < Completable.size(); i++) {
            if(Completable.get(i).startsWith(subWord)&& !Completable.get(i).equals(subWord)&&subWord.length()<Completable.get(i).length())
        	data.add(Completable.get(i));
        }
        System.out.println("IN CREATLIST=="+subWord+Completable+data+data.size());
        if(data.size()==0){

        	suggestion=false;
        	hide();
        	return false;
        }
        suggestion=true;
        list=new JList(data.toArray());
        //list.setListData(data);
        list.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    insertSelection();
                }
            }
        });
        return true;
    }

    public boolean insertSelection() {
        if (list.getSelectedValue() != null) {
            try {
                final String selectedSuggestion = ((String) list.getSelectedValue()).substring(subWord.length());		// add only the remaining part of the word to the text area.
                textarea.getDocument().insertString(insertionPosition, selectedSuggestion+" ", null);
                return true;
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
            hide();
        }
        return false;
    }

    public void moveUp() {
    
    	if(list!=null)
    	{int index = Math.max(list.getSelectedIndex() - 1, 0);
    	System.out.println("index selected "+index);
        selectIndex(index);
    	}
    	else
    		System.out.println("LIST IS NULL");
    }

    public void moveDown() {
    	if(list==null)
    		System.out.println("LIST IS NULL");
    	else
    	{
    		int index = Math.min(list.getSelectedIndex() + 1, list.getModel().getSize() - 1);
    		System.out.println("index selected "+index);
    		selectIndex(index);
    	}
    }

    private void selectIndex(int index) {
        final int position = textarea.getCaretPosition();
        list.setSelectedIndex(index);
        textarea.setCaretPosition(position);
    }
    public  void showSuggestion() {
        hide();
        final int position=textarea.getCaretPosition();
        int caret=position-1;
        Point location;			//location is collected to display the popup at that location
        try {
            location = textarea.modelToView(position).getLocation();
        } catch (BadLocationException e2) {
            e2.printStackTrace();
            return;
        }
		String typed=textarea.getText();
		String lastWord="";
		while(caret>=0&&typed.charAt(caret)!=' ')
		{
			lastWord=typed.charAt(caret)+lastWord;
			caret--;
		}
		if(caret>=position) return;
        final String subWord = lastWord;
        if (subWord.length() < 3) {
            return;
        }
        SuggestionPanels(textarea, position, subWord, location);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textarea.requestFocusInWindow();
            }
        });
    }
	
	private KeyListener k1 = new KeyAdapter() {
        
        @Override
        public void keyTyped(KeyEvent e) {
        	if (e.getKeyChar() == KeyEvent.VK_ENTER && suggestion ) {
                    if (insertSelection()) {
                        e.consume();
                        final int position = textarea.getCaretPosition();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    textarea.getDocument().remove(position - 1, 1);
                                } catch (BadLocationException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                }
            }
        }
        public void keyReleased(KeyEvent e)
        {
			System.out.println("typed test in rell="+area.getText()+"|");	
			if(autoCompleteOn)
        	{
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					moveDown();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP ) {
					moveUp();
				}
				else if (Character.isWhitespace(e.getKeyChar())) {
					hide();
				} 
				else if(Completable!=null&&Completable.size()>0){
						showSuggestion();
				}
        	}

        	if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_V)
        	{
        		//area.setCaretPosition(area.getText().length()-getCaretAddidtion()-1);
				tryToRecognize();
				indicateErrors();
				// Spell error detection
				
        	}
        	if(separator.contains(e.getKeyChar()))
			 indicateErrors();
        }
		private int getCaretAddidtion() {
			// TODO Auto-generated method stub
			String textTyped=area.getText().toLowerCase();
			int caretAddidtion=0,i;			// to equate caret position and area.getText().length() (new line takes 2 caret positions)
			for(i=0;i<textTyped.length();i++)
	        {
				char c=textTyped.charAt(i);
	        	if(c=='\n') caretAddidtion++;
	        }
			return caretAddidtion;
		}
		public void keyPressed(KeyEvent e) {
			changed = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
			//System.out.println("typed text in pres="+area.getText()+"|");	
        	
        	// LIVE SPELL CHECKER CORRECTER
			if (spellCheckOn && !suggestion) {

				if (separator.contains(e.getKeyChar()))//e.getKeyChar() == ' ' || e.getKeyChar() == '.'|| e.getKeyChar() == '\n' || e.getKeyChar() == ','|| e.getKeyChar() == ';')
				{
					separatorUsed=e.getKeyChar();
					String typedText;
					String word, correctedWord;
					int i = 0;
					word = "";
					typedText = area.getText(); // http://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html
					i = typedText.length() - 1;
					while (i >= 0&& !separator.contains(typedText.charAt(i)))//&& typedText.charAt(i) != ' '&& typedText.charAt(i) != '.'&& typedText.charAt(i) != '\n')
						word = typedText.charAt(i--) + word;/*
					while (i >= 0)
						remText = typedText.charAt(i--) + remText;*/
					if(obj.nWords.containsKey(word.toLowerCase()))
					{
						jlsc.setVisible(false);
					}
					else
					{						
					if (word.length() > 1) {
						correctedWord = obj.correct(word);
						try {
							area.getDocument().remove(area.getCaretPosition()-(word.length()), (word.length()));
							area.getDocument().insertString(area.getCaretPosition(), correctedWord,null);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//area.setText(remText + correctedWord );
						if (obj.candidates != null && obj.candidates.size()>1) {			// Concept to show candidate words as list

							ArrayList<String> cand = new ArrayList<String>(			
									obj.candidates.values());
							String allCandidates[] = new String[cand.size()];
							allCandidates = cand.toArray(allCandidates); // convert ArrayLIst to String[]
							candidateList.setListData(allCandidates);
							jlsc.setViewportView(candidateList);
							jlsc.setVisible(true);
							// Add listener to JList
							candidateList.addListSelectionListener(new ListSelectionListener() {

										@Override
										public void valueChanged(ListSelectionEvent arg0) {
											// change the previous word
										if(candidateList.getSelectedValue()!=null)
										{
											String typedText;
											String word, remText;
											int i = 0;
											word = "";
											remText = "";
											typedText = area.getText(); // http://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html
											i = typedText.length() - 2;
											
											while (i >= 0&& typedText.charAt(i) != ' '&& typedText.charAt(i) != '.'	&& typedText.charAt(i) != '\n')
												word = typedText.charAt(i--)+ word;
											while (i >= 0)
												remText = typedText.charAt(i--)+ remText;
											word = candidateList.getSelectedValue();
											System.out.println("you selected: "+ word);
											area.setText(remText + word+" ");
											area.requestFocus();
											indicateErrors();
										}
										}
									});
						}
					}
					}
				}
				else
					jlsc.setVisible(false);
				
			}
	//-------------------- Add Words for Autocomplete ------------------------------------------
			
			if(separator.contains(e.getKeyChar()))//e.getKeyChar() == ' ' || e.getKeyChar() == '.' || e.getKeyChar() == '\n' || e.getKeyChar() == ',' || e.getKeyChar() == ';')
			{
				tryToRecognize();
				int caret = area.getCaretPosition() - 1;
				String typed = area.getText();
				String lastWord = "";
				while (caret >= 0 && !separator.contains(typed.charAt(caret)))// typed.charAt(caret)!=' ')
				{
					lastWord = typed.charAt(caret) + lastWord;
					caret--;
				}
				// System.out.println("LastWord= "+lastWord);
				if (lastWord.length() > 7 && !Completable.contains(lastWord)
						&& obj.nWords.containsKey(lastWord))
					Completable.add(lastWord);
			}
			prevChar=e.getKeyChar();
		}
	};
	public void tryToRecognize()  //-- Context Recognizer
	{
		if(CR.getSentenceCount()>1)	// If he has typed more than 2 sentences
		{
			try {
				CR.recognizer();
				
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
			CR.hidePopup();
	}
	public void indicateErrors()
	{
		if (spellCheckOn && !suggestion) {
			int i;
			String textTyped=area.getText().toLowerCase();
			String word="";
			int caretAddidtion=0;			// to equate caret position and area.getText().length() (new line takes 2 caret positions)
			System.out.println("------------------IN INDICATE---------------");
			System.out.println("CARET:"+area.getCaretPosition()+" LENGHT:"+textTyped.length()+textTyped);
			for(i=0;i<textTyped.length();i++)
	        {
				char c=textTyped.charAt(i);
	        	if(separator.contains(c)||c=='\n'||Character.isWhitespace(c)||Character.isSpace(c))
	        	{
	        		if (word.length() > 1)
	        		{
	        			System.out.println("WORD:"+word+"|");
	    					
	    					if(!obj.nWords.containsKey(word))
	    					{
	    						System.out.println("WORD:"+word+" NOT PRESENT");   					
	    			
	    						//Highlight from indexOfWord to indexOfWord+word.length
	    						Highlighter.HighlightPainter painter = new UnderlineHighlighter.UnderlineHighlightPainter(Color.red);
	    						Highlighter highlighter = area.getHighlighter();
	    						try {
									highlighter.addHighlight(i-caretAddidtion-word.length(),i-caretAddidtion, painter);
								} catch (BadLocationException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	    					}
	    			}
	        		if(c=='\n') caretAddidtion++;
	        		word="";
	        	}
	        	else 
	        		word=word+c;
	        }
		
		}
	}
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new LiveAuto2();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
