/**
* 	This class shows the result of stuffing or unstuffing in the ByteStuff class. 
*	It uses a GUI interface to show the result.
*/ 

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ByteStuffOutput
{
	private String inputData;
	private String outputData;
	int selection;
  
	public ByteStuffOutput (String inputData, String outputData, int selection)
	{     
		this.inputData = inputData;
		this.outputData = outputData;
		this.selection = selection;             
		display();
	} // End constructor

	private void display ()
	{
		JPanel mainPanel = new JPanel ();
		mainPanel.setLayout (new GridLayout (2, 1, 2, 2));
		JPanel [] panels = new JPanel [2];
		JLabel labels[] = new JLabel [2];
		JTextField []  fields = new JTextField [2];
		String [] labelStrs= {"  Original string", "Changed string"};
		String message = "Result of operation";                     
		for (int i = 0; i < 2; i++)
		{
			panels [i] = new JPanel ();
			labels [i]  = new JLabel (labelStrs[i], SwingConstants.RIGHT);
			labels[i].setForeground (Color.red); 
			fields[i] = new JTextField (40);
			fields[i].setBorder (new BevelBorder (BevelBorder.LOWERED));
			fields[i].setBackground (Color.yellow);
			fields[i].setEditable (false);
			panels[i].add (labels[i]);
			panels[i].add (fields[i]);
			mainPanel.add (panels [i]);
		} // End for-loop
		fields[0].setText (inputData);
		fields[1].setText (outputData);
		JOptionPane.showMessageDialog (null, mainPanel, message, 1); 
	} // End Display

 } // End class 