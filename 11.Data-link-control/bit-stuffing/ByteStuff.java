/**
* 	This is the mother class in the program that uses two other classes to
*	simulate the byte-stuffing protocol. The program is also the driver for
*	for this project. It uses the stuff and unstuff methods to do its job. We
*	have created a limited version of this protocol to be able to use GUI
*	for input and output.  
*/ 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ByteStuff
{   
	private String inputData;
	private String outputData;
	private ByteStuffInput input;
	private ByteStuffOutput output;
	private int selection;

	public ByteStuff()
	{
		getInput ();
	}// End constructor

	public void getInput ()
	{             
		input = new ByteStuffInput( this); 
	} // End getInput
      
	public void stuff (String inData)
	{
		inputData = inData;
		outputData = inputData.replaceAll ("E", "EE");
		outputData = outputData.replaceAll ("F", "EF");
		selection = 0;
		showOutput ();
	}//End stuff
	
	public void unstuff (String inData)
	{
		inputData = inData;
		outputData = inputData.replaceAll ("EF", "F"); 
		outputData = outputData.replaceAll ("EE", "E");
		selection = 1;
		showOutput ();
	}//End unstuff
        
	public void showOutput ()
	{
		output = new ByteStuffOutput (inputData, outputData, selection);
	}// End showOutput

	public static void main (String [] args)
	{
		ByteStuff   bytestuff  = new ByteStuff ();
	} // End main
 
} // End class



 
            
            
