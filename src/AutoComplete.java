import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

public class AutoComplete {

    public class SuggestionPanel {
        private JList list;
        private JPopupMenu popupMenu;
        private String subWord;
        private final int insertionPosition;

        public SuggestionPanel(JTextPane textarea, int position, String subWord, Point location) {
            this.insertionPosition = position;
            this.subWord = subWord;
            popupMenu = new JPopupMenu();
            popupMenu.removeAll();
            popupMenu.setOpaque(false);
            popupMenu.setBorder(null);
            popupMenu.add(list = createSuggestionList(position, subWord), BorderLayout.CENTER);
            popupMenu.show(textarea, location.x, textarea.getBaseline(0, 0) + location.y);
        }

        public void hide() {
            popupMenu.setVisible(false);
            if (suggestion == this) {
                suggestion = null;
            }
        }

        private JList createSuggestionList(final int position, final String subWord) {
            Object[] data = new Object[10];
            for (int i = 0; i < data.length; i++) {
                data[i] = subWord + i;
            }
            JList list = new JList(data);
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

        public void printval()
        {
        	System.out.println("area "+textarea);
        	System.out.println("list "+list);
        	System.out.println("popup "+popupMenu);
        	System.out.println();
        }

        public boolean insertSelection() {
            if (list.getSelectedValue() != null) {
                try {
                    final String selectedSuggestion = ((String) list.getSelectedValue()).substring(subWord.length());
                    textarea.getDocument().insertString(insertionPosition, selectedSuggestion, null);
                    return true;
                } catch (BadLocationException e1) {
                    System.out.println("at insert selectoion");
                    e1.printStackTrace();
                }
                hideSuggestion();
            }
            return false;
        }

        public void moveUp() {
            int index = Math.min(list.getSelectedIndex() - 1, 0);
            printval();
            selectIndex(index);
        }

        public void moveDown() {
            int index = Math.min(list.getSelectedIndex() + 1, list.getModel().getSize() - 1);
            printval();
            selectIndex(index);
        }

        private void selectIndex(int index) {
            final int position = textarea.getCaretPosition();
            list.setSelectedIndex(index);
            textarea.setCaretPosition(position);
        }
    }

    private SuggestionPanel suggestion;
    private JTextPane textarea;


    protected void showSuggestion() {
        hideSuggestion();
        suggestion.printval();
        System.out.println("asd");
        final int position = textarea.getCaretPosition();
        Point location;
        try {
            location = textarea.modelToView(position).getLocation();
        } catch (BadLocationException e2) {
            e2.printStackTrace();
            System.out.println("at location");
            return;
        }
        String text = textarea.getText();
        System.out.println("typed "+text);
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
        if (subWord.length() < 3) {
            return;
        }
        suggestion = new SuggestionPanel(textarea, position, subWord, location);
        textarea.requestFocusInWindow();
    }

    private void hideSuggestion() {
        if (suggestion != null) {
            suggestion.hide();
        }
    }
    protected void initUI(JTextPane area) {
    	
        final JFrame frame = new JFrame();
        frame.setTitle("Test frame on two screens");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        textarea = new JTextPane();
        textarea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        textarea.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (suggestion != null) {
                        if (suggestion.insertSelection()) {
                            e.consume();
                            final int position = textarea.getCaretPosition();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        textarea.getDocument().remove(position - 1, 1);
                                    } catch (BadLocationException e) {
                                        e.printStackTrace();
                                        System.out.println("keytyped");
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_DOWN && suggestion != null) {
                    suggestion.moveDown();}
                if (e.getKeyCode() == KeyEvent.VK_UP && suggestion != null) {
                    suggestion.moveUp();
                } else if (Character.isLetterOrDigit(e.getKeyChar())) {
                    showSuggestion();
                } else if (Character.isWhitespace(e.getKeyChar())) {
                    hideSuggestion();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        panel.add(textarea, BorderLayout.CENTER);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    AutoComplete(JTextPane area, KeyEvent e)
    {
    	textarea=area;
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
             if (suggestion != null) {
                  if (suggestion.insertSelection()) {
                      e.consume();
                      final int position = textarea.getCaretPosition();
                       SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    textarea.getDocument().remove(position - 1, 1);
                                } catch (BadLocationException e) {
                                    e.printStackTrace();
                                    System.out.println("at autocomplete");
                                }
                            }
                        });
                    }
                }
            }
    	
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("at 1");
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println("at 2");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("at 3");
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            System.out.println("at 4");
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Test().initUI();
            }
        });
    }

}