import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
public class UnderlineBkground extends SwingWorker<Integer, Integer>
{
    protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.
        Thread.sleep(1000);
        return 42;
    }

    protected void done()
    {
        
    }
}