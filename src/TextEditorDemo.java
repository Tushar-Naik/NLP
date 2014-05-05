import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class TextEditorDemo implements ActionListener,KeyListener{
	int []ar=new int[1000000];
	int j,n,i=0;
	JTextArea t;
	JFrame f;
	JTextField t1,t2,t3;
	JPanel p;
	JPanel p1;
	JButton b,b1;
	String str;
	Highlighter h;
public static void main(String[] args){
	TextEditorDemo d=new TextEditorDemo();
	d.go();
}
public void go()
{
  f=new JFrame();	
  p=new JPanel();
  p1=new JPanel();
  p.setVisible(true);
  p1.setVisible(true);
  b=new JButton("Advanced..");
  b.addActionListener(new ActionListener(){

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showConfirmDialog(f, p,"Advanced options",JOptionPane.DEFAULT_OPTION);
		
	}});
  b1=new JButton("Replace");
  t1=new JTextField(10);
  t2=new JTextField(10);
  t3=new JTextField(10);
  t1.addKeyListener(this);
  b1.addActionListener(this);
 f.setSize(1000,500);
 f.setVisible(true);
 t=new JTextArea(500,500);
 t.setLineWrap(true);
 t.setWrapStyleWord(true);
 h=t.getHighlighter();
 f.add(t);
 p.add(new JLabel("Find"));
	p.add(BorderLayout.NORTH,t1);
	p.add(new JLabel("Replace"));
	p.add(BorderLayout.CENTER,t2);
	p.add(b1);
	//p.add(new JLabel("Enter word to be replaced"),BoxLayout.X_AXIS);
	p1.add(new JLabel("Find"));
	p1.add(t3);
	p1.add(b);
	//p1.add(BorderLayout.CENTER,new JLabel("For replacing use advanced options"));
	t3.addKeyListener(new KeyListener(){

		@Override
		public void keyPressed(KeyEvent e){}
		@Override
		public void keyReleased(KeyEvent e) {
			str=(String)t3.getText();
			 h.removeAllHighlights();
			  if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
			    	if(str.isEmpty()){
			           h.removeHighlight(t);
			       	}
			}
				boolean b=capitalTest();
				if(b==true){
					highlight();
					str=str.toLowerCase();
					highlight();
					char c=Character.toUpperCase(str.charAt(0));
					str=c+str.substring(1);
					highlight();
				}else{
					highlight();
					char s=str.charAt(0);
					if(Character.isUpperCase(s))
					{
						char c=Character.toLowerCase(s);
						str=c+str.substring(1);
						highlight();
					}
					else{
					char c=Character.toUpperCase(s);
					str=c+str.substring(1);
					highlight();
					}
					str=str.toUpperCase();
					highlight();
					
				}
			}

		@Override
		public void keyTyped(KeyEvent e) {}});
f.add(BorderLayout.EAST,p1);
}

@Override
public void actionPerformed(ActionEvent e) {
		    str=(String)t1.getText();
			h.removeAllHighlights();
			Pattern p=Pattern.compile(str);
			String typo=t.getText();
			Matcher m=p.matcher(typo);
			while(m.find()){				
				ar[i]=m.start();
				System.out.println(ar[i]);
				j=i+1;
				i++;
			}	
			System.out.println(j);
			for(i=0;i<j;i++){
			
				String before=t.getText().substring(0,ar[i]+i*(t2.getText().length()-t1.getText().length()));
				String after=t.getText().substring(ar[i]+i*(t2.getText().length()-t1.getText().length())+t1.getText().length());
				t.setText(before+t2.getText()+after);
	
			}
			
	t1.setText("");
	t2.setText("");

}

public void highlight(){
	Pattern p=Pattern.compile(str);
	String typo=t.getText();
	Matcher m=p.matcher(typo);
	h=t.getHighlighter();
while(m.find()){
try {
	h.addHighlight(m.start(),m.start()+m.group().length(),DefaultHighlighter.DefaultPainter);
} catch (BadLocationException e1) {
	e1.printStackTrace();
}
}
}
public boolean capitalTest()
{
	for(char c:str.toCharArray()){
		if(Character.isLowerCase(c))
			return false;
	}
	return true;	
}
@Override
public void keyReleased(KeyEvent e) {
	 str=(String)t1.getText();
	 h.removeAllHighlights();
	  if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
	    	if(str.isEmpty()){
	           h.removeHighlight(t);
	       	}
	}
		boolean b=capitalTest();
		if(b==true){
			highlight();
			str=str.toLowerCase();
			highlight();
			char c=Character.toUpperCase(str.charAt(0));
			str=c+str.substring(1);
			highlight();
		}else{
			highlight();
			char s=str.charAt(0);
			if(Character.isUpperCase(s))
			{
				char c=Character.toLowerCase(s);
				str=c+str.substring(1);
				highlight();
			}
			else{
			char c=Character.toUpperCase(s);
			str=c+str.substring(1);
			highlight();
			}
			str=str.toUpperCase();
			highlight();
			
		}
		
}
@Override
public void keyTyped(KeyEvent e) {
}
@Override
public void keyPressed(KeyEvent e) {
}
}

