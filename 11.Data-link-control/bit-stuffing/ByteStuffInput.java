/**
* 	This class gets the input data for ByteStuf class using a GUI interface.
*	If first gets the user selection (stuffing or unstuffing). It then allows the 
* 	the user to enter the data. 
*/ 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;
import javax.swing.border.*;
import java.util.regex.*;

public class ByteStuffInput
{
	ByteStuff  caller;
	int selection;
	String inputData;
     
	public ByteStuffInput (ByteStuff caller)
	{
		this.caller = caller;
		selection = getSelection();
		inputData = getChars();
		if (selection == 0)
		{
			caller.stuff (inputData);   
		}
		else 
		{
			caller.unstuff (inputData); 
		}
	} // End constructor

	private int getSelection ()
	{
		String selectMessage = "Select the action you need to perform.";
		String[] options = {"Stuff", "Unstuff"};
		int selection = JOptionPane.showOptionDialog (null, selectMessage, "Action Selection", -1, 3, null, options, options [0]);
		return selection;
	} // End selection
       
	private String getChars ()
	{
		String strInput;
		String message;
		String warning;
		boolean validInput = false;
		if (selection == 0)
			message = "Enter the string of characters, made of D (data), E(escape), or F (flag), to be stuffed.";
		else
			message = "Enter the string of characters, made of D (data), E(escape), or F (flag), to be unstuffed.";
			warning = "The string should be made only of D, E, or F. " + message;      
			strInput  = (JOptionPane.showInputDialog (null, message).trim()).toUpperCase();    
			do
			{   
				try
				{           
					if (!Pattern.matches ("[DEF]*", strInput))             
						throw new FormatException ("Wrong Format");
						validInput = true;
				}        
				catch (FormatException fe)
				{
					strInput  = JOptionPane.showInputDialog (null, warning).trim();    
				} 
			} while (!validInput);
			return strInput;   
	}// End getChars

	class FormatException extends Exception
	{  
		public FormatException (String message)
		{
			super (message); 
		}
	} // End FormatException

} // End class