import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;


public class FindImplementation {
	JTextField find, findInD, replaceInD;
	JButton adv, replaceB;
	JTextArea area;
	Highlighter h;
	String textTyped;
	JPanel p;
	int []ar=new int[1000000];
	int j,n,i=0;
	
	public FindImplementation(JTextField fi, JButton jb, JTextArea a)
	{
		find=fi;
		findInD=new JTextField(10);
		replaceInD=new JTextField(10);
		replaceB=new JButton("Replace");
		adv=jb;
		area=a;
		h=area.getHighlighter();
		h.removeAllHighlights();
		p=new JPanel();
		p.add(new JLabel("Find"));
		p.add(BorderLayout.NORTH,findInD);
		p.add(new JLabel("Replace"));
		p.add(BorderLayout.CENTER,replaceInD);
		p.add(replaceB);
		find.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

				 textTyped=find.getText().toString();
				 h.removeAllHighlights();
				  if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE || find.getText().length()==0){
				    	if(textTyped.isEmpty()){
				           h.removeHighlight(area);
				       	}
				  }
					boolean b=capitalTest();
					if(b==true)
					{
						highlight();
						textTyped=textTyped.toLowerCase();
						highlight();
						char c=Character.toUpperCase(textTyped.charAt(0));
						textTyped=c+textTyped.substring(1);
						highlight();
					}
					else
					{
						highlight();
						char s=textTyped.charAt(0);
						if(Character.isUpperCase(s))
						{
							char c=Character.toLowerCase(s);
							textTyped=c+textTyped.substring(1);
							highlight();
						}
						else
						{
							char c=Character.toUpperCase(s);
							textTyped=c+textTyped.substring(1);
							highlight();
						}
						textTyped=textTyped.toUpperCase();
						highlight();
						
					}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		adv.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				JOptionPane.showConfirmDialog(area, p,"Advanced options",JOptionPane.DEFAULT_OPTION);
			}
		});
		
		findInD.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

				 textTyped=findInD.getText().toString();
				 h.removeAllHighlights();
				  if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
				    	if(textTyped.isEmpty()){
				           h.removeHighlight(area);
				       	}
				  }
					boolean b=capitalTest();
					if(b==true)
					{
						highlight();
						textTyped=textTyped.toLowerCase();
						highlight();
						char c=Character.toUpperCase(textTyped.charAt(0));
						textTyped=c+textTyped.substring(1);
						highlight();
					}
					else
					{
						highlight();
						char s=textTyped.charAt(0);
						if(Character.isUpperCase(s))
						{
							char c=Character.toLowerCase(s);
							textTyped=c+textTyped.substring(1);
							highlight();
						}
						else{
						char c=Character.toUpperCase(s);
						textTyped=c+textTyped.substring(1);
						highlight();
						}
						textTyped=textTyped.toUpperCase();
						highlight();
					}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});;
		replaceB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				textTyped=findInD.getText();
				h.removeAllHighlights();
				boolean b= capitalTest();
				if(b==true){
					replace();
					textTyped=textTyped.toLowerCase();
					replace();
					char c=Character.toUpperCase(textTyped.charAt(0));
					textTyped=c+textTyped.substring(1);
					replace();
				}
				else{
					replace();
					char s=textTyped.charAt(0);
					if(Character.isUpperCase(s))
					{
						char c=Character.toLowerCase(s);
						textTyped=c+textTyped.substring(1);
						replace();
					}
					else{
					char c=Character.toUpperCase(s);
					textTyped=c+textTyped.substring(1);
					replace();
					textTyped=textTyped.toUpperCase();
					replace();
					}
					
				}
				findInD.setText("");
				replaceInD.setText("");
				/*Pattern p=Pattern.compile(textTyped);
				String typo=area.getText();
				Matcher m=p.matcher(typo);
				while(m.find()){				
					ar[i]=m.start();
					System.out.println(ar[i]);
					j=i+1;
					i++;
				}	
				System.out.println(j);
				for(i=0;i<j;i++){
					
					String before=area.getText().substring(0,ar[i]+i*(replaceInD.getText().length()-findInD.getText().length()));
					String after=area.getText().substring(ar[i]+i*(replaceInD.getText().length()-findInD.getText().length())+findInD.getText().length());
					area.setText(before+replaceInD.getText()+after);
		
				}
						for(i=0;i<j;i++){
					ar[i]=0;
				}
				
				findInD.setText("");
				replaceInD.setText("");
				*/
				
			}
		});
		
	}
	public void replace()
	{
		i=0;
		Pattern p=Pattern.compile(textTyped);
		String typo=area.getText();
		Matcher m=p.matcher(typo);
		while(m.find()){				
			ar[i]=m.start();
			System.out.println(ar[i]);
			j=i+1;
			i++;
		}	
		System.out.println(j);
		for(i=0;i<j;i++){
			
			String before=area.getText().substring(0,ar[i]+i*(replaceInD.getText().length()-findInD.getText().length()));
			String after=area.getText().substring(ar[i]+i*(replaceInD.getText().length()-findInD.getText().length())+findInD.getText().length());
			area.setText(before+replaceInD.getText()+after);

		}
				for(i=0;i<j;i++){
			ar[i]=0;
		}
		
	}

	public void highlight()
	{
		Pattern p=Pattern.compile(textTyped);
		String typo=area.getText();
		Matcher m=p.matcher(typo);
		h=area.getHighlighter();
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
		for(char c:textTyped.toCharArray()){
			if(Character.isLowerCase(c))
				return false;
		}
		return true;	
	}
	
}
	