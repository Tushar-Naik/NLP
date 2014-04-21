
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

public class HelloSwing {

	HelloSwing()
	{
		JFrame jf =new JFrame("Text Editor");
		jf.setSize(300, 100);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel jl=new JLabel("My 1st Swing prog");
		jf.add(jl);
		jf.setVisible(true);

	}
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				new HelloSwing();
			}
		});
	}
}
