/**
* 	This is the event class that is used in conjunction with the CSMA class
*	to simulate the occurrence of events   
*/ 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;
import java.util.regex.*;

public class CSMACAEvent
{     
	private JDialog  dialog;
	private JTextField [] fields;
	private int next;
	private Font font1, font2;
     
public CSMACAEvent ()
	{
		dialog = new JDialog ();
		dialog.setSize (700, 400);
		dialog.setLayout (new GridLayout(10, 1));
		fields = new JTextField [10];
		font1 = new Font ("monospaced", Font.BOLD, 12);
		font2 = new Font ("monospaced", Font.BOLD, 20);
		next = 0;
	} // End constructor

	public void add (String message)
	{
		fields[next] = new JTextField (120);
		fields[next].setFont (font1);
		fields[next].setText ("   " + message);
		fields[next].setHorizontalAlignment (SwingConstants.LEFT);
		if (next == 0)
		{
			fields[next].setText (message);
			fields[next].setBackground (Color.YELLOW);
			fields[next].setForeground (Color.RED);
			fields[next].setFont (font2);
			fields[next].setHorizontalAlignment (SwingConstants.CENTER);
		}
		dialog.add (fields[next]);
		dialog.setVisible (true);
		next++;
	} // End add

	public void end ()
	{
		dialog.dispose();
	} // End end

} // End class