/**
* This is the input class in the CSMA/CD project. 
* It gets some parameters that need to set from the user and pass them to the CSMA/CD class.   
*/ 

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CSMACDInput
{
	private CSMACD caller;
	private int limit, intInput;
	private String strInput, message, warning;
	boolean validInput;

	public CSMACDInput (CSMACD caller)
	{ 
		this.caller = caller;  
    // limit = getInput ("Enter the maximum number of attemps, between 2 and 15", 2, 15);
		limit = getInput ("Enter the maximum number of attemps, between 5 and 15", 5, 15);
		caller.simulate (limit);       
	}// End Constructor
    
	private int getInput (String message, int lower, int upper)
	{
		strInput = JOptionPane.showInputDialog (null, message).trim();    
		do
		{   
			try
			{           
				intInput =  Integer.parseInt (strInput);              
				if (intInput < lower || intInput > upper)
				throw new RangeException ("Out of range.");
				validInput = true;
			}         
			catch (RangeException re)
			{
				warning =  "You entered, " + strInput + ", which is out of range.  "+ message;
				strInput  = JOptionPane.showInputDialog (null, warning).trim();    
			}
			catch (NumberFormatException nf)
			{
				if (strInput.length() == 0) 
				{
					warning  =  "You did not enter any input! " + message;
				}
				else
				{
					warning =  "You entered, " + strInput+  ", which is not a valid input. "+ message;
				}
				strInput  = JOptionPane.showInputDialog (null, warning).trim();
			}
		} while (!validInput);
		return intInput;   
	}// End getLimit

	class RangeException extends ArithmeticException
	{  
		public RangeException (String message)
		{
			super (message); 
		}
	} // End RangeException

} // End class


