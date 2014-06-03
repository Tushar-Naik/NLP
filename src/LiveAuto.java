import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

public class LiveAuto extends JFrame {

	/**
	 * @author Tushar Naik, Vamanan T S, Suhas V
	 * 
	 * A text editor using java that implements, 
	 * (1) a live contextual Spell  Checker-Corrector using that Bayesian model, n-grams and the noisy
	 * channel model, (2) a Context Recognizer using probabilistic tagging, 
	 * (3) an Auto Complete feature using a hash map, and 
	 * (4) a Regex Find option.TO
	 */

	private static final long serialVersionUID = 1L;
	// Editor Declarations
	private JFrame frame = new JFrame();
	public JTextArea area = new JTextArea(25,45);
	public JTextField find= new JTextField(10);
	public JButton advancedFind = new JButton("Advanced Find");
	public JButton optionButton = new JButton("Options");
	private JFileChooser dialog = new JFileChooser(
			System.getProperty("user.dir")); // used to provide GUI to navigate FileSystem
	private String currentFile = "Untitled";
	JToolBar tool;
	// button variables- to keep track of toggle buttons
	//boolean spellCheckOn;
	//boolean autoCompleteOn;
	String beforeWordText="";
	String afterWordText="";
	private boolean changed = false; // to differentiate between save and saveAs
	char prevChar;

	ArrayList<Character> separator = null; // for listening to every word
	char separatorUsed; // to print back the separator after correction
	boolean acsuggestion = false; // variable to make sure that the autocomplete suggestion popup vanishes wen not used by user
	//boolean wordBeingTyped= false; // variable to make sure that the candidatesuggestion popup vanishes wen not being used..
	boolean enterSeparatorpressed=false;
	boolean spellSuggestion = false;
	boolean rightClickListOn = false;
	boolean addToDict = false; //to check if the word typed, was wrong, or even if no suggestions exist
	ArrayList<String> Completable = null; // list of all words, that have been
											// typed by user, of length >7
	final JList<String> candidateList = new JList<String>(); // list of all words, that can fix the wrong one.
	/* Create all objects to save time */
	NewSpellChecker obj;
	ContextRec CR;
	FindImplementation FI;
	Options O;
	JPanel statusPanel=new JPanel();

	public LiveAuto() throws IOException {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//spellCheckOn = true;
		//autoCompleteOn = true;
		prevChar = ' ';//
		Completable = new ArrayList<String>();
		char sep[] = { ' ', ',', '\n', '\t', ';', '.', '?', '(', ')', '!', '@',
				'#', '-', '_', '[', '{', ']', '}', ':' };
		separator = new ArrayList<Character>();
		for (char c : sep)
			separator.add(c);
		obj = new NewSpellChecker("count_big.txt");
		O=new Options(area);
		area.setFont(new Font("Segoe UI", 0, 16));
		
		// add(scroll, BorderLayout.CENTER);

		frame.setLayout(new FlowLayout());
		JMenuBar JMB = new JMenuBar();
		frame.setJMenuBar(JMB);
		find = new JTextField (10);
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
		frame.add(tool, BorderLayout.NORTH);
		tool.add(New);
		tool.add(Open);
		tool.add(Save);
		tool.addSeparator();
		tool.addSeparator();
		JButton cut = tool.add(Cut), cop = tool.add(Copy), pas = tool
				.add(Paste);
		tool.addSeparator();
		optionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				O.makeDialog();
				indicateErrors();
				tryToRecognize();
				
			}
		});
		tool.add(optionButton);
		cut.setText(null);
		cut.setToolTipText("Cut");
		cut.setIcon(new ImageIcon("cut.gif"));
		cop.setText(null);
		cop.setToolTipText("Copy");
		cop.setIcon(new ImageIcon("copy.gif"));
		pas.setText(null);
		pas.setToolTipText("Paste");
		pas.setIcon(new ImageIcon("paste.gif"));

		//spellOn.setSelected(true);
		//completeOn.setSelected(true);
		//tool.add(spellOn);
		//tool.add(completeOn);
		//tool.addSeparator(new Dimension(50, 50));
		tool.addSeparator();
		tool.addSeparator();
		tool.add(find);
		tool.add(advancedFind);
		Save.setEnabled(false);
		SaveAs.setEnabled(false);

		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		area.addKeyListener(k1);
		frame.setTitle(currentFile);
		// area.setSize(300, 300);
		//area.setPreferredSize(new Dimension(500, 500));
		area.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		area.setLineWrap(true);
		//area.setBounds(0,0,200,200);
		JScrollPane scroll = new JScrollPane(area);
		//scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

 
		frame.add(scroll, BorderLayout.CENTER);

		/*statusPanel=new JPanel(new BorderLayout());
		statusPanel.add(new JLabel("hi"));
		frame.add(statusPanel);
		*/
		
		area.requestFocus();
		area.setWrapStyleWord(true) ;
		// option of right click on the misspelt word
		area.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e))
				{
					System.out.println("____________  IN RIGHT CLICK  _________");
					//get the caret position of the right click
				    Point pt = new Point(e.getX(), e.getY());
				    int pos = area.viewToModel(pt);
				    Point location; // location is collected to display the popup at that
					// location
				    try {
				    	location = textarea.modelToView(pos).getLocation();
				    } catch (BadLocationException e2) {
				    	e2.printStackTrace();
				    	return;
				    }
				   // System.out.println("POS="+pos);
					String typedText=area.getText();
					String word="";
					//extract the word at the caret position
					if(pos<typedText.length( )&& !separator.contains(typedText.charAt(pos)))  // check if a word exists in that position
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
						String correctWord=obj.correct(word,O);
						if (obj.candidates != null && obj.candidates.size() > 1) { // Concept to show candidate words as list

							System.out.println("Added----------");
							ArrayList<String> cand = new ArrayList<String>(obj.candidates.values());
							cand.add("Add to dictionary");
							String allCandidates[] = new String[cand.size()];
							allCandidates = cand.toArray(allCandidates); // convert ArrayLIst to String[]
							candidateList.setListData(allCandidates);
							//jlsc.setViewportView(candidateList);
							//jlsc.setVisible(true);
							// Add listener to JList
							final String wordAdd=word;
							candidateList.addListSelectionListener(new ListSelectionListener() {

										@Override
										public void valueChanged(ListSelectionEvent arg0) {
											// change the previous word
											if (candidateList.getSelectedValue() != null && rightClickListOn) {
												int caretPos=area.getCaretPosition(); 
												String wordSelected = candidateList.getSelectedValue();
												System.out.println("you selected: "+ wordSelected);
												System.out.println("Bef:"+beforeWordText+" after:"+afterWordText+" word:"+wordSelected);
												if(candidateList.getSelectedValue()=="Add to dictionary")
												{
													System.out.println("tried to add="+wordAdd);
													addToDict(wordAdd);
												}
										 		else
												 area.setText(beforeWordText + wordSelected	+ afterWordText);
												area.requestFocus();
												area.setCaretPosition(caretPos);
												//System.out.println("CARET+"+area.getCaretPosition()+" Len="+area.getText().length());
												indicateErrors();
												hidePopup();
												//System.out.println("Hide called:"+236);
											}
										}
									});
							SuggestionPanels(area, pos, word, location, 2, candidateList);
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									textarea.requestFocusInWindow();
								}
							});
						}
						else
						{
							SuggestionPanels(area, pos, "", location, 2, null);
							ArrayList<String> cand = new ArrayList<String>(obj.candidates.values());
							cand.add("Add to dictionary");
							String allCandidates[] = new String[cand.size()];
							allCandidates = cand.toArray(allCandidates); // convert ArrayLIst to String[]
							candidateList.setListData(allCandidates);
							//jlsc.setViewportView(candidateList);
							//jlsc.setVisible(true);
							// Add listener to JList
							final String wordAdd=word;
							candidateList.addListSelectionListener(new ListSelectionListener() {

										@Override
										public void valueChanged(ListSelectionEvent arg0) {
											// change the previous word
											if (candidateList.getSelectedValue() != null && rightClickListOn) {
												int caretPos=area.getCaretPosition(); 
												String wordSelected = candidateList.getSelectedValue();
												System.out.println("you selected: "+ wordSelected);
												System.out.println("Bef:"+beforeWordText+" after:"+afterWordText+" word:"+wordSelected);
												if(candidateList.getSelectedValue()=="Add to dictionary")
												{
													addToDict(wordAdd);
												}
												else
												 area.setText(beforeWordText + wordSelected	+ afterWordText);
												area.requestFocus();
												area.setCaretPosition(caretPos);
												//System.out.println("CARET+"+area.getCaretPosition()+" Len="+area.getText().length());
												indicateErrors();
												hidePopup();
												//System.out.println("Hide called:"+236);
											}
										}
									});
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

		FI= new FindImplementation(find, advancedFind, area);
		initPanel();
		CR = new ContextRec(area,obj,frame);
		frame.setSize(800, 700);
		//frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
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

	private void addToDict(String wordAdd) 
	{
		wordAdd=wordAdd.toLowerCase();
		obj.nWords.put(wordAdd, (long)1);
		obj.nWords2.put(wordAdd, (long)1);
		try {
			new CreateSerDict(obj);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			obj = new NewSpellChecker("count_big.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// ----------------------------- Suggestion Panel ------------------------------------------
	public JList list;
	public JPopupMenu popupMenu;
	public String subWord;
	public int insertionPosition;
	public JTextArea textarea;

	public void initPanel() {
		textarea = area;
		insertionPosition = 0;
		popupMenu = null;
	}/*
	 * public void init(JTextArea area) { textarea=area; insertionPosition=0;
	 * popupMenu=null; }
	 */

	public void hidePopup() {
		if (popupMenu != null && acsuggestion == true)
		{	acsuggestion = false;
			spellSuggestion=false;
			popupMenu.setVisible(false);
		}
		if (popupMenu != null && spellSuggestion == true)
		{	spellSuggestion=false;
			acsuggestion=false;
			popupMenu.setVisible(false);
		}
		
	}
	
	//show popup, if spell=0, for autocomplete, if spell=1, for spelling error candidates
	public void SuggestionPanels(JTextArea textarea, int position,
			String subWord, Point location, int spell, JList list2) {
		this.insertionPosition = position;
		this.subWord = subWord;
		this.list=list2;
		popupMenu = new JPopupMenu();
		popupMenu.removeAll();
		popupMenu.setOpaque(true);
		popupMenu.setBorder(null);
		if(spell==0)		// auto complete suggestions to be shown
		{	if (createCompletableList(position, subWord)) {
			JScrollPane jsp=new JScrollPane(list);
			jsp.setSize(100,200);
			popupMenu.add(jsp, BorderLayout.CENTER);
			popupMenu.show(textarea, location.x, textarea.getBaseline(0, 0)	+ location.y + 20);
			}
		}
		
		else 
		{
			if(list2 !=null && spell ==1)		// spell error candidates to be shown
			{
				
				spellSuggestion=true;
				createSuggestionList(list2);
				JScrollPane jsp=new JScrollPane(list);
				jsp.setSize(100,200);
				popupMenu.add(jsp, BorderLayout.CENTER);
				popupMenu.show(textarea, location.x-subWord.length(), textarea.getBaseline(0, 0)
						+ location.y + 20);
			}
			else if(list2 != null && spell==2)
			{
				spellSuggestion=true;
				rightClickListOn=true;
				JScrollPane jsp=new JScrollPane(list2);
				jsp.setSize(100,200);
				popupMenu.add(jsp, BorderLayout.CENTER);
				popupMenu.show(textarea, location.x-subWord.length(), textarea.getBaseline(0, 0)
						+ location.y + 20);
			}
			else
			{
				spellSuggestion=true;
				popupMenu.add(new JLabel("No suggestions"));
				popupMenu.show(textarea, location.x, textarea.getBaseline(0, 0)
						+ location.y + 20);
			}
		}
			area.requestFocus();
		// printval();
	}

	// ------------------------------ Actual Logic of Autocomplete -------------------------------

	public boolean createCompletableList(final int position,
			final String subword) {
		acsuggestion=true;
		ArrayList<String> data = new ArrayList<String>();
		for (int i = 0; i < Completable.size(); i++) {
			if (Completable.get(i).startsWith(subWord)
					&& !Completable.get(i).equals(subWord)
					&& subWord.length() < Completable.get(i).length())
				data.add(Completable.get(i));
		}
		// System.out.println("IN CREATLIST=="+subWord+Completable+data+data.size());
		if (data.size() == 0) {
			hidePopup();
			//System.out.println("Hide called:"+453);
			return false;
		}
		JList jl = new JList(data.toArray());
		this.list=jl;
		createSuggestionList(jl);
		return true;
	}

	public void createSuggestionList(JList jl) {

		list=jl;
		//System.out.println("LIST:"+list);
		list.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					insertSelection();
					acsuggestion=true;
					spellSuggestion=true;
					hidePopup();
					//System.out.println("Hide called:"+477);
				}
			}
		});
		list.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
				//System.out.println("in keyTyped="+area.getText()+"|");
				//System.out.println("INENTER:"+e.getKeyChar()+"|"+enterSeparatorpressed);
				if (e.getKeyChar() == KeyEvent.VK_ENTER && (acsuggestion||spellSuggestion))// && !enterSeparatorpressed) 
					{
					//System.out.println("IN Enter");
					if (insertSelection()) 
					 {
						/*e.consume();
						 
						final int position = textarea.getCaretPosition();
						SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
						try {
								//System.out.println("IN remove:pos="+position);
								textarea.getDocument().remove(position - 1, 1);
							} catch (BadLocationException e) {
							e.printStackTrace();
							}	
						}
						});*/
						hidePopup();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public boolean insertSelection() {
		if (list.getSelectedValue() != null) {
			System.out.println("----------------IN INSERT-----------");
			final String selectedSuggestion = ((String) list.getSelectedValue()); // add  only the remaining part of the word to text area.
			//System.out.println("selectedSugg:"+selectedSuggestion);
			int caretPos= textarea.getCaretPosition();
			int bp=textarea.getCaretPosition()-1;
			String TT=textarea.getText();
			System.out.println("caretPos="+caretPos+"TTlen="+TT.length());
			String afterText=TT.substring(bp+1);
			System.out.println("TT:"+TT+"|");
			System.out.println("after:"+afterText);
			while(bp>=0 && !Character.isAlphabetic(TT.charAt(bp))){separatorUsed=TT.charAt(bp); bp--;}
			while(bp>=0 && !separator.contains(TT.charAt(bp))) bp--;
			System.out.println("bp after:"+bp);
			String prevText=TT.substring(0,bp+1);
			System.out.println("prev Text:"+prevText);
			textarea.setText(prevText+selectedSuggestion+separatorUsed+afterText);		//extra space which will be consumed
			System.out.println("Settext="+textarea.getText());
			try{
				textarea.setCaretPosition(prevText.length()+selectedSuggestion.length()+1);
			}
			catch(Exception e)
			{
				textarea.setCaretPosition(textarea.getText().length());
			}
			acsuggestion=true;
			spellSuggestion=true;
			hidePopup();
			//System.out.println("Hide called:"+495);
			return true;
		}
		return false;
	}

	public void moveUp() {

		if (list != null) {
			list.requestFocus();
			int index = Math.max(list.getSelectedIndex() - 1, 0);
			System.out.println("index selected " + index);
			selectIndex(index);
		} else
			System.out.println("LIST IS NULL");
	}

	public void moveDown() {
		//System.out.println("LIST=="+list);
		if (list == null)
			System.out.println("LIST IS NULL");
		else {
			list.requestFocus();
			int index = Math.min(list.getSelectedIndex() + 1, list.getModel()
					.getSize() - 1);
			System.out.println("index selected " + index);
			selectIndex(index);
		}
	}

	private void selectIndex(int index) {
		final int position = textarea.getCaretPosition();
		list.setSelectedIndex(index);
		textarea.setCaretPosition(position);
	}

	public void showSuggestion() {
		hidePopup();
		//System.out.println("Hide called:"+530);
		final int position = textarea.getCaretPosition();
		int caret = position -1;
		Point location; // location is collected to display the popup at that location
		try {
			location = textarea.modelToView(position).getLocation();
		} catch (BadLocationException e2) {
			e2.printStackTrace();
			return;
		}
		acsuggestion=true;
		String typed = textarea.getText();
		//System.out.println("Typed="+typed+caret);
		String lastWord = "";
		while (caret >= 0 && !separator.contains(typed.charAt(caret))) 
		{
			lastWord = typed.charAt(caret) + lastWord;
			caret--;
		}
		if (caret >= position)
			return;

		//System.out.println("subword="+subWord);
		final String subWord = lastWord;	
		if (subWord.length() < 3) {
			return;
		}
		SuggestionPanels(textarea, position, subWord, location,0,null);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textarea.requestFocusInWindow();
			}
		});
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

	//------------------------- Key Listeners for all the editor features -----------------------
	
	private KeyListener k1 = new KeyAdapter() {

		@Override
		public void keyTyped(KeyEvent e) { 	//  executed 2nd
			//System.out.println("in keyTyped="+area.getText()+"|");
			
			/*{
				System.out.println("IN Enter");
				if (insertSelection()) {
					e.consume();
					final int position = textarea.getCaretPosition();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								System.out.println("IN remove:pos="+position);
								textarea.getDocument().remove(position - 1, 1);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}*/
		}

		public void keyReleased(KeyEvent e) { 	//  executed 3rd
			//System.out.println("in keyRel="+area.getText()+"|"+" spellsugges="+spellSuggestion);
			if (Character.isWhitespace(e.getKeyChar())) 
			{
				indicateErrors();
				if(acsuggestion)
				{	hidePopup();
					//System.out.println("Hide called:"+598);
				}
			} 
			else if (Completable != null && Completable.size() > 0 && O.autoCompleteOn) {
					//System.out.println("trying autocomplete..");
				showSuggestion();
			}
			else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE|| e.getKeyCode()== KeyEvent.VK_ESCAPE)
			{
				hidePopup();
				CR.hidePopup();
				CR.popupMenu.setVisible(false);
			}
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
				//area.setCaretPosition(area.getText().length()	- getCaretAddidtion() - 1);
				String textTyped=area.getText();
				//System.out.println("CARET:"+area.getCaretPosition()+" LENGHT:"+textTyped.length()+textTyped);
				tryToRecognize();
				indicateErrors();
				// Spell error detection
			}
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) 
			{
				find.requestFocus();
			}
			if (separator.contains(e.getKeyChar()))
				indicateErrors();
		}
		public void keyPressed(KeyEvent e) {	//  executed 1st
			
			changed = true;		//variable to enable save
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
			rightClickListOn=false;
			if (e.getKeyChar() == KeyEvent.VK_ENTER && (acsuggestion||spellSuggestion)&& list!=null)// &&!enterSeparatorpressed 
			 {
				e.consume();
				System.out.println("INENTER:"+e.getKeyChar()+"|");
				System.out.println("Enter pressed:"+acsuggestion+spellSuggestion+enterSeparatorpressed);
				
				list.requestFocus();
			 }
			else if (e.getKeyCode() == KeyEvent.VK_DOWN && (acsuggestion||spellSuggestion)&& list!=null) 
			{
				e.consume();
				list.requestFocus();
				//moveDown();
				System.out.println("LIST:"+list);
			} 
			else if (e.getKeyCode() == KeyEvent.VK_UP && (acsuggestion||spellSuggestion)&& list!=null) 
			{		
				e.consume();
				list.requestFocus();
				//moveUp();	
			} 
				
			//System.out.println("in keyPress="+area.getText()+"|");
			//System.out.println(obj.nWords2.size());
			// ------------------------------- LIVE SPELL CHECKER CORRECTER ------------------------------------
			else if (O.spellCheckOn && !acsuggestion) // !acsuggestion bcoz, type positive, then pos"\n" it autocorrects it to pus, but doesnt take the autocomplete suggestion into account..
			{
				System.out.println("Entered speell on with c="+e.getKeyChar()+"|");
				if(Character.isAlphabetic(e.getKeyChar()))
				{
					//wordBeingTyped=true;
					hidePopup();
					//System.out.println("Hide called:"+730);
				}
				else if (separator.contains(e.getKeyChar()))
				{
					area.revalidate();
					enterSeparatorpressed=false;
					//System.out.println("Entered spellcking mechanism");
					//wordBeingTyped=false;
					separatorUsed = e.getKeyChar();
					//System.out.println("Entered spellcking mechanism: sep="+separatorUsed+"|");;
					if(separatorUsed=='\n'&& spellSuggestion==false)
						enterSeparatorpressed=true;			// to solve the enter"\n" bug
					
					String typedText;
					String word, correctedWord;
					int i = 0;
					word = "";
					typedText = area.getText(); // http://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html
					//System.out.println("CARET:"+area.getCaretPosition()+" LENGHT:"+typedText.length());
					i = area.getCaretPosition()-1;		// ERRORMARK : for \t or \n. use ( till it reaches an alphabet, i--)
					//while (i >= 0 && separator.contains(typedText.charAt(i))) i--;
					while (i >= 0 && !separator.contains(typedText.charAt(i)))
						// && typedText.charAt(i) != ' '&& typedText.charAt(i)
						// != '.'&& typedText.charAt(i) != '\n')
						word = typedText.charAt(i--) + word;
					//System.out.println("word="+word);
					boolean autoCap=false;
					if(!isNumber(word))
					{
						//System.out.println("w"+word.length()+"t"+typedText.length());
						if(word.length()==typedText.length()||typedText.charAt(typedText.length()-1-word.length())=='\n'||(typedText.charAt(typedText.length()-1-word.length())==' '&&typedText.charAt(typedText.length()-2-word.length())=='.'))
						{
							if(word.length()>0)
							{
								word=Character.toUpperCase(word.charAt(0))+word.substring(1);
								autoCap=true;
							}
						}
						if(obj.nWords.containsKey(word.toLowerCase())&& !autoCap && spellSuggestion==false)
						{
							spellSuggestion=true;
							//acsuggestion=true;
							hidePopup();
							//System.out.println("Hide at 790");
						}
						if(!obj.nWords.containsKey(word.toLowerCase()) || autoCap )
						{
							if (word.length() > 1) {
								correctedWord = obj.correct(word,O);
								try {
									area.getDocument().remove(area.getCaretPosition() - (word.length()),(word.length()));
									area.getDocument().insertString(area.getCaretPosition(), correctedWord,null);
								} catch (BadLocationException e1) {
							// 	TODO Auto-generated catch block
									e1.printStackTrace();
								}
						// 	area.setText(remText + correctedWord );
								if (!word.equalsIgnoreCase(correctedWord) && obj.candidates != null && obj.candidates.size() > 1) { // Concept to show candidate words as list
									System.out.println("ITS COME HERE--- "+word+" "+correctedWord+(word.toLowerCase()==correctedWord.toLowerCase()));
									int pos = area.getCaretPosition();
									Point location; // location is collected to display the popup at that
							// 	location
									try {
										location = textarea.modelToView(pos).getLocation();
									} catch (BadLocationException e2) {
										e2.printStackTrace();
										return;
									}
									ArrayList<String> cand = new ArrayList<String>(
										obj.candidates.values());
									String allCandidates[] = new String[cand.size()];
									//System.out.println("candidates:"+cand);
									allCandidates = cand.toArray(allCandidates); // convert ArrayLIst to String[]
									candidateList.setListData(allCandidates);
									
									SuggestionPanels(area, pos, word, location, 1, candidateList);
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											textarea.requestFocusInWindow();
										}
									});
								}
								else
								{
									hidePopup();
								}
							}
						}
					}
				}
				else
					hidePopup();
			}
			// -------------------- Add Words for Autocomplete------------------------------------------

			if (separator.contains(e.getKeyChar()))// e.getKeyChar() == ' ' || e.getKeyChar() == '.' ||
													// e.getKeyChar() == '\n' ||
													// e.getKeyChar() == ',' ||
													// e.getKeyChar() == ';')
			{
				System.out.println("IN AUTOCOMPLETE");
				if(e.getKeyChar()=='.') tryToRecognize();
				indicateErrors();
				int caret = area.getCaretPosition() - 1;
				String typed = area.getText();
				String lastWord = "";
				while (caret >= 0 && !separator.contains(typed.charAt(caret)))// typed.charAt(caret)!=' ')
				{
					lastWord = typed.charAt(caret) + lastWord;
					caret--;
				}
				lastWord=lastWord.toLowerCase();
				//System.out.println("LastWord= "+lastWord);
				if (lastWord.length() > 7 && !Completable.contains(lastWord)
						&& obj.nWords.containsKey(lastWord))
					Completable.add(lastWord);
				//System.out.println("Completable List= "+Completable);
			}
			prevChar = e.getKeyChar();
		}
	};

	public void tryToRecognize() // -- Context Recognizer
	{
		System.out.print("Try to rec function"+popupMenu);
		if(CR.popupMenu!=null  && O.contextRecOn)		//to remove multiple links, and to show spellSuggestions and not override with this popup
		 {
			System.out.println("Came here");
			new CRBkground(area,CR).execute();
		 }
		indicateErrors();
	}

	public void indicateErrors() 
	{
		if (O.underLineOn && !acsuggestion)// && CR.popupBeingShown==false) 
		{	
			new UnderlineBkground(area, obj, separator).execute();	
		}
		else
	    	area.getHighlighter().removeAllHighlights();
	}

	public static void main(String args[]) {
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
