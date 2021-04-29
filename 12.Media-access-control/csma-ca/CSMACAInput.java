/**
* 	This is the input class in the CSMA/CA project. It gets some parameters that need to 
*	set from the user and pass them to the CSMACA class.   
*/ 

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CSMACAInput
{
	private CSMACA caller;
	private int limit,  cstTime, ackTime, intInput;
	private String strInput, message, warning;
	boolean validInput;

	public CSMACAInput (CSMACA caller)
	{ 
		this.caller = caller;  
		limit = getInput ("Enter the maximum number of attemps, between 5 and 15", 5, 15);  
		cstTime = getInput ("Enter the number of seconds for CTS timer, between 2 and 5", 2, 5);
		ackTime = getInput ("Enter the number of seconds for ACK timer, between 2 and 5", 2, 5);
		caller.simulate (limit, cstTime, ackTime);       
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


