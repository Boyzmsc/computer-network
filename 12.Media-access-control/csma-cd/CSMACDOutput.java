/**
* 	This is the output class in the CSMA/CA project that shows the result of the program. 
*	The display method uses the GUI interface to show the output.   
*/ 

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CSMACDOutput
{
	private JTextField [][] fields;
	private int k; 
	private String [][] statistics;
	private String [][] data;
	private String message;
	JPanel panel;
  
	public CSMACDOutput(String [][] statistics, int k)
	{
		this.statistics = statistics;
		this.k= k;
		fields = new JTextField [k + 2][6];
		data = new String [k + 2][6];
		panel = new JPanel ();
		display ();
	} // End constructor
              
	private void display ()
	{
		String [] topStrs = {"Transmission", "Collision", "Success", "Abort", "TB"};
		panel.setLayout (new GridLayout (k + 2, 6 , 2, 2));
		data[0][0] = "";
		for (int j = 1; j < 6; j++)
		{
			data[0][j] = topStrs [j-1];
		}
		for (int i = 1; i < k+2; i ++)
		{
			data[i][0] = "Attempt " + i;
		}
           
		for (int i = 0; i < k + 1 ; i++)
		{
			for (int j = 0 ; j < 5 ; j++)
			{
				data[i+1][j+1] = statistics [i][j];
			}
		}
		for (int i = 0; i < k + 2 ; i++)
		{
			for (int j = 0 ; j < 6; j++)
			{       
				fields[i][j] = new JTextField (8);
				fields[i][j].setText (data[i][j]);
				if (j != 0)
				fields[i][j].setHorizontalAlignment (JTextField.CENTER); 
				fields[0][0].setBackground (Color.black);
				if ((i == 0) || (j == 0))
					fields[i][j].setBackground (Color.yellow);
					panel.add (fields[i][j]);
			}
		}
		message = "Statistics of attemps";     
		JOptionPane.showMessageDialog (null, panel, message, -1); 
	} // End Display

} // End class 