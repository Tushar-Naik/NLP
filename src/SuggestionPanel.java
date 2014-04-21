import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;


public class SuggestionPanel {
        public JList list;
        public JPopupMenu popupMenu;
        public String subWord;
        public int insertionPosition;
        public JTextPane textarea;

        public SuggestionPanel(JTextPane area)
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
        public SuggestionPanel(JTextPane textarea, int position, String subWord, Point location) {
            this.insertionPosition = position;
            this.subWord = subWord;
            popupMenu = new JPopupMenu();
            popupMenu.removeAll();
            popupMenu.setOpaque(false);
            popupMenu.setBorder(null); 
            createSuggestionList(position, subWord);
            popupMenu.add(list, BorderLayout.CENTER);
            popupMenu.show(textarea, location.x, textarea.getBaseline(0, 0) + location.y);
           // printval();
        }

        public void hide() {
        	if(popupMenu!=null)
            popupMenu.setVisible(false);
        }

        public JList createSuggestionList(final int position, final String subWord) {
            Object[] data = new Object[10];
            for (int i = 0; i < data.length; i++) {
                data[i] = subWord + i;
            }
            list=new JList(data);
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
            return list;
        }

        public boolean insertSelection() {
            if (list.getSelectedValue() != null) {
                try {
                    final String selectedSuggestion = ((String) list.getSelectedValue()).substring(subWord.length());
                    textarea.getDocument().insertString(insertionPosition, selectedSuggestion, null);
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
        	{int index = Math.min(list.getSelectedIndex() - 1, 0);
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
        public void printval()
        {
        	System.out.println("area "+textarea);
        	System.out.println("list "+list);
        	System.out.println("popup "+popupMenu);
        	System.out.println();
        }
        public  void showSuggestion() {
            hide();
            //printval();
            final int position = textarea.getCaretPosition();
            System.out.println(position);
            Point location;			//location is collected to display the popup at that location
            try {
                location = textarea.modelToView(position).getLocation();
            } catch (BadLocationException e2) {
                e2.printStackTrace();
                return;
            }
            String text = textarea.getText();
            System.out.println(text);
            int start = Math.max(0, position - 1);
            while (start > 0) {
                if (!Character.isWhitespace(text.charAt(start))) {
                    start--;
                } else {
                    start++;
                    break;
                }
            }
            if (start > position) {
                return;
            }
            final String subWord = text.substring(start, position);
            if (subWord.length() < 2) {
                return;
            }
            new SuggestionPanel(textarea, position, subWord, location);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textarea.requestFocusInWindow();
                }
            });
        }

    }