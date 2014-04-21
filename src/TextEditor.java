import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.*;

public class TextEditor extends JFrame {

	private JTextArea area = new JTextArea(20, 120);
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));	// used to provide GUI to navigate FileSystem
	private String currentFile = "Untitled";
	private boolean changed = false;

	public TextEditor() {
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scroll, BorderLayout.CENTER);

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
		JToolBar tool = new JToolBar();
		add(tool, BorderLayout.NORTH);
		tool.add(New);
		tool.add(Open);
		tool.add(Save);
		tool.addSeparator();

		JButton cut = tool.add(Cut), cop = tool.add(Copy), pas = tool.add(Paste);

		cut.setText(null);
		cut.setIcon(new ImageIcon("cut.gif"));
		cop.setText(null);
		cop.setIcon(new ImageIcon("copy.gif"));
		pas.setText(null);
		pas.setIcon(new ImageIcon("paste.gif"));

		Save.setEnabled(false);
		SaveAs.setEnabled(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		area.addKeyListener(k1);
		setTitle(currentFile);
		setVisible(true);
	}
	Action New = new AbstractAction("New", new ImageIcon("new.gif")) {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
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
		for(String s:buf)System.out.print(s+" ");
	}
	


	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true;
			Save.setEnabled(true);
			String typedText;
			SaveAs.setEnabled(true);
			if(e.getKeyChar()=='\n')
			{
				typedText =area.getText();  //http://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html
				String typedLines[]=typedText.split("\n");
				 System.out.println("typed lines: ");print(typedLines);
				String LineText=typedLines[area.getLineCount()-1];
				 System.out.println("line count"+area.getLineCount());
				String typedWords[]=LineText.split(" ");
				 System.out.println("typed words: ");print(typedWords);
				String output="";
				for(String s:typedWords)
					try {
						output+=(new SpellChecker("big.txt")).correct(s)+" ";
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				area.append("\n"+output+"\n");
			}
		}
	};

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TextEditor();
			}
		});
	}
}
