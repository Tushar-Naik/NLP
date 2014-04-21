import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

public class LiveTextEditor extends JFrame {

	private JTextPane area = new JTextPane();
	private JFileChooser dialog = new JFileChooser(
			System.getProperty("user.dir")); // used to provide GUI to navigate FileSystem
	final JList<String> candidateList = new JList<String>();
	JScrollPane jlsc= new JScrollPane();
	
	private String currentFile = "Untitled";
	private boolean changed = false;
	boolean spellCheckOn;
	JToolBar tool;
	/*Create all objects to save time*/
	NewSpellChecker obj;
	SuggestionPanel suggestion;

	public LiveTextEditor() throws IOException {
		spellCheckOn = true;
		obj = new NewSpellChecker("count_big.txt");
		suggestion=new SuggestionPanel(area);
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(area,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
		cut.setText(null);
		cut.setIcon(new ImageIcon("cut.gif"));
		cop.setText(null);
		cop.setIcon(new ImageIcon("copy.gif"));
		pas.setText(null);
		pas.setIcon(new ImageIcon("paste.gif"));

		spellOn.setSelected(true);
		tool.add(spellOn);
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
		add(area, BorderLayout.CENTER);
		area.requestFocus();
		setSize(600, 700);
		setVisible(true);
	}

	Action New = new AbstractAction("New", new ImageIcon("new.gif")) {
		public void actionPerformed(ActionEvent e) {
			// saveOld();
			try {
				new LiveTextEditor();
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

	private void print(String[] buf) {
		// TODO Auto-generated method stub
		for (String s : buf)
			System.out.print(s + " ");
	}

	private KeyListener k1 = new KeyAdapter() {
        
        public void keyPressed(KeyEvent e) {
			changed = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);

			// LIVE SPELL CHECKER CORRECTER
			if (spellCheckOn) {

				if (e.getKeyChar() == ' ' || e.getKeyChar() == '.'
						|| e.getKeyChar() == '\n' || e.getKeyChar() == ','
						|| e.getKeyChar() == ';') {
					String typedText;
					String word, remText;
					int i = 0;
					word = "";
					remText = "";
					typedText = area.getText(); // http://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html
					i = typedText.length() - 1;
					while (i >= 0 && typedText.charAt(i) != ' '
							&& typedText.charAt(i) != '.'
							&& typedText.charAt(i) != '\n')
						word = typedText.charAt(i--) + word;
					while (i >= 0)
						remText = typedText.charAt(i--) + remText;
					if (word.length() > 1) {
						String actualWord=word;
						word = obj.correct(word);
						area.setText(remText + word);
						if (obj.candidates != null) {// Concept to show candidate words as list

							ArrayList<String> cand = new ArrayList<String>(obj.candidates.values());
							cand.add(0, actualWord);
							String allCandidates[] = new String[cand.size()];
							allCandidates = cand.toArray(allCandidates); // convert ArrayLIst to String[]
							candidateList.setListData(allCandidates);
							jlsc.setViewportView(candidateList);
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
											
											while (i >= 0
													&& typedText.charAt(i) != ' '
													&& typedText.charAt(i) != '.'
													&& typedText.charAt(i) != '\n')
												word = typedText.charAt(i--)+ word;
											while (i >= 0)
												remText = typedText.charAt(i--)+ remText;
											word = candidateList.getSelectedValue();
											System.out.println("you selected: "+ word);
											area.setText(remText + word+" ");
											area.requestFocus();
										}
										}
									});
						}
					}
				}
			}
		}
	};

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new LiveTextEditor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
