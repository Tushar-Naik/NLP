import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightExample implements ActionListener, KeyListener {
	JTextPane t;
	JFrame f;
	JTextField j;
	JPanel p;
	JButton b;
	int i, len;
	String str;
	Highlighter h;

	public static void main(String[] args) {
		HighlightExample d = new HighlightExample();
		d.go();
	}

	public void go() {

		f = new JFrame();
		p = new JPanel();
		b = new JButton("Ok");
		j = new JTextField(10);
		j.addKeyListener(this);
		b.addActionListener(this);
		f.setSize(500, 500);
		f.setVisible(true);
		t = new JTextPane();
		h = t.getHighlighter();
		f.add(t);
		p.add(j);
		p.add(BorderLayout.SOUTH, b);
		f.add(BorderLayout.EAST, p);
		t.setSize(200, 200);
		t.requestFocus();
		t.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				String word="";
				String text=t.getText();
				int x=t.getCaretPosition();
				//convert point to 
				//Cursor x=t.getCursor();
				//then extract the particular word 
				int y=x-1;
				while(x<t.getText().length()&&text.charAt(x)!=' ') 
				{
					word=word+text.charAt(x);
					x++;
				}
				while (y>0&&text.charAt(y)!=' ')
				{
					word=text.charAt(y)+word;
					y--;
				}
				System.out.println(word+" x="+x+" "+arg0.getX()+" "+arg0.getY()+" "+arg0.getLocationOnScreen()+" "+arg0.getXOnScreen()+" "+arg0.getYOnScreen());
				
				JPopupMenu popupMenu = new JPopupMenu();
				JLabel jLab =new JLabel("Hi brother");
				popupMenu.removeAll();
		        popupMenu.setOpaque(false);
		        popupMenu.setBorder(null);
		        popupMenu.add(jLab, BorderLayout.CENTER);
		        popupMenu.show(t, 0,0);							//  Right bottom of screen
		        t.requestFocus();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				/*
				JPopupMenu popupMenu = new JPopupMenu();
				JLabel jLab =new JLabel("Hi brother");
				popupMenu.removeAll();
		        popupMenu.setOpaque(false);
		        popupMenu.setBorder(null);
		        popupMenu.add(jLab, BorderLayout.CENTER);
		        popupMenu.show(t, 0,0);							//  Right bottom of screen
		        t.requestFocus();
				*/
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		str = (String) j.getText();
		System.out.println(str.length()+"  "+t.getCaretPosition());
		boolean b = capitalTest();
		if (b == true) {
			highlight();
			str = str.toLowerCase();
			highlight();
		} else {
			highlight();
			str = str.toUpperCase();
			highlight();
		}
	}

	public void highlight() {
		Pattern p = Pattern.compile(str);
		String typo = t.getText();
		Matcher m = p.matcher(typo);
		h = t.getHighlighter();
		while (m.find()) {
			try {
				h.addHighlight(m.start(), m.start() + str.length(),
						DefaultHighlighter.DefaultPainter);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean capitalTest() {
		for (Character c : str.toCharArray()) {
			if (Character.isLowerCase(c))
				return false;
		}
		return true;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		str = (String) j.getText();

		System.out.println(t.getText().length()+"  "+t.getCaretPosition());
		h.removeAllHighlights();
		boolean b = capitalTest();
		if (b == true) {
			highlight();
			str = str.toLowerCase();
			highlight();
		} else {
			highlight();
			str = str.toUpperCase();
			highlight();
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}
}
