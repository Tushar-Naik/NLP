import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Options {

	JTextArea area;
	public boolean spellCheckOn;
	public boolean autoCompleteOn;
	public boolean underLineOn;
	public boolean contextRecOn;
	public boolean segmentationOn;
	JTextField dialogText;
	JLabel settings;
	JPanel p;
	JRadioButton j1,j2;
	JCheckBox auto,spell,context,segment,under;
	ButtonGroup bg1;
	JButton restore;
	
	
	
	public Options(JTextArea a)
	{
		spellCheckOn=true;
		autoCompleteOn=true;
		underLineOn=true;
		contextRecOn=true;
		segmentationOn=true;
		area =a;
		restore=new JButton("Restore to defaults");
	}
	public void makeDialog()
	{
		
		settings=new JLabel("General settings");
		p=new JPanel(new GridLayout(0,2));
		p.setVisible(true);
	    p.add(new JLabel("Select Features for the editor"));
		p.add(new JLabel());p.add(new JLabel());p.add(new JLabel());
		
	    spell=new JCheckBox("Spell Check",spellCheckOn);
		p.add(spell);
		p.add(new JLabel("Checks for spelling mistakes and corrects"));
		
		auto=new JCheckBox("Auto Complete",autoCompleteOn);
		p.add(auto);
		p.add(new JLabel("Helps to complete words automatically"));
		
		under=new JCheckBox("Underline Errors",underLineOn);
		p.add(under);
		p.add(new JLabel("Underlines the spelling errors in the document"));
		
		context=new JCheckBox("Context Recognition",contextRecOn);
		p.add(context);
		p.add(new JLabel("Recognizes what your typing"));
		
		segment=new JCheckBox("Word Segmentation",segmentationOn);
		p.add(segment);
		p.add(new JLabel("Segments the word automatically in case you forget to put spaces"));
		/*
		bg1=new ButtonGroup();
		j1=new JRadioButton("",true);
		bg1.add(j1);
		j2=new JRadioButton();
		bg1.add(j2);
		dialogText=new JTextField(10);
		p.add(new JLabel());p.add(new JLabel());
		p.add(settings);
		p.add(new JLabel());
		p.add(new JLabel());
		p.add(new JLabel());
		p.add(new JLabel("Enter"));
		dialogText.setText("5");
		p.add(dialogText);
		p.add(new JLabel("SUHAS"));
		p.add(j1);
		p.add(new JLabel("TUSHAR"));
		p.add(j2);*/
		restore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				auto.setSelected(true);
				spell.setSelected(true);
				under.setSelected(true);
				context.setSelected(true);
				segment.setSelected(true);
				
			}
		});
		//restore.setBorderPainted(false); 
        //restore.setContentAreaFilled(false); 
		p.add(new JLabel());p.add(new JLabel());p.add(new JLabel());
		p.add(new JLabel());p.add(new JLabel());p.add(new JLabel());
		p.add(restore);
		p.add(new JLabel());p.add(new JLabel());p.add(new JLabel());
		p.add(new JLabel());p.add(new JLabel());p.add(new JLabel());
		p.add(new JLabel());p.add(new JLabel());p.add(new JLabel());
		int cancel=JOptionPane.showConfirmDialog(area, p,"Options",JOptionPane.OK_CANCEL_OPTION);
		if(cancel == JOptionPane.OK_OPTION){
					if(auto.isSelected())autoCompleteOn=true; else autoCompleteOn=false;
					if(spell.isSelected())spellCheckOn=true; else spellCheckOn=false;
					if(context.isSelected())contextRecOn=true; else contextRecOn=false;
					if(segment.isSelected())segmentationOn=true; else segmentationOn=false;
					if(under.isSelected())underLineOn=true; else underLineOn=false;
					area.requestFocus();
			}
	}
	
	public static void main(String args[])
	{
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
