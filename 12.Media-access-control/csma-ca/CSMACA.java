/**
* 	This is the mother class in the program that uses three other classes to
*	simulate the CSMA/CA. The program is also the driver for
*	this project. It is the software implemenation of the flow diagram for
*	this protocol. We are using some timers to simulate the sending and receving
*	packets in this implementation.   
*/ 

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CSMACA
{
	private int limit, k, r, ctsTimeSetting, ackTimeSetting;
	private final int ifsSetting = 1;
	private final int tbSetting = 1;
	private boolean ctsReceived;
	private boolean ackReceived;
	private boolean success;
	private boolean abort;
	private boolean status;
	private Random random;
	private String [][] statistics;
	CSMACAEvent event;
	CSMACAInput input;
	CSMACAOutput output;

	public CSMACA ()
	{ 
		random = new Random (); 
		input = new CSMACAInput (this);
	} // End 

	public void simulate (int limit, int ctsTimeSetting, int ackTimeSetting)
	{
		this.limit = limit; 
		this.ctsTimeSetting = ctsTimeSetting;
		this.ackTimeSetting = ackTimeSetting;
		statistics = new String [limit][6];
		k = 0;
		success = false;
		abort = false;
		do
		{   
			attempt ();                    
		} while ((!success) && (!abort));
		if (abort)
		{
			k--;
		}
		output = new CSMACAOutput (statistics, k); 
	}// End Simulate

	private void attempt ()
	{
		event = new CSMACAEvent ();           
		event.add ("Attempt " + (k + 1) + ":"); 
		r =  getFreeChannel () + 1;
		event.add ("Slot " + r + " is selected, waiting to reach this slot in the contention window ...");
		statistics [k][0] = String.valueOf (r + 1);
		event.add ("Waiting for IFS timer to expire ...");
		wait (ifsSetting);
		event.add ("RTS were sent, waiting to receive CTS from the receiver ...");
		statistics [k][1]= "Sent";
		ctsReceived = send (ctsTimeSetting);
		if (ctsReceived)
		{
			event.add ("CTS received. The target is ready.");
			statistics [k][2] = "Received";
			event.add ("Waiting for IFS timer to expire ...");
			wait (ifsSetting);
			event.add ("Data packet was sent, waiting for acknowlegement ...");
			statistics [k][3] = "Sent";
			ackReceived = send (ackTimeSetting);
			if (ackReceived)
			{
				event.add ("Acknowledgement was received. The attempt was successful.");
				statistics [k][4] = "Received";
				success = true;
			}  
			else 
			{
				event.add ("Acknowledgment did not received!");
				statistics [k][4] = "Timeout";
				success = false;    
			}
		}
		else
		{
			event.add ("CTS did not received!");
			statistics [k][2] = "Timeout";
			statistics [k][3] = "NA";
			statistics [k][4] = "NA";
		}
		if (!success)
		{
			k++;
			if (k < limit)
			{
				event.add ("Waiting the TB timer to expire and to start a new attempt ...");                
				wait(tbSetting); 
			} 
			else 
			{
				event.add ("The whole process was aborted. We need to try another time.");                
				abort = true;
			}
		}
		wait (5);
		event.end ();
	} // End attempt    
          
	public void wait (int seconds)
	{
		try
		{
			Thread.sleep (1000 * seconds);
		} 
		catch (InterruptedException e){}
	} // End wait  

	public boolean send (int timeoutSeconds)
	{   
		try
		{
			Thread.sleep (1000 * timeoutSeconds);
		} 
		catch (InterruptedException e){} 
		return (random.nextBoolean ());
	}  //End send   

	public int getFreeChannel ()
	{
		return (random.nextInt ((int) Math.pow (2, k)));
	} // End getFreeChannel 

	public static void main (String args[])
	{      
		CSMACA csmaca = new CSMACA ();
	}// End main
	
}// End class