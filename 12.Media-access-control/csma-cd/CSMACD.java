/**
* This is the mother class in the program that uses three other classes to
*	simulate the CSMA/CD. The program is also the driver for this project. 
* It is the software implemenation of the flow diagram for this protocol.
*	We are using some timers to simulate the sending and receving
*	packets in this implementation.   
*/ 

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CSMACD
{
	private int limit, k, r, tbSetting;
	private final int csmaSetting = 2;
  private final int tfrSetting = 2;
  private boolean isTransDone;
  private boolean isColliDetected;
	private boolean success;
	private boolean abort;
	private Random random;
	private String [][] statistics;
	CSMACDEvent event;
	CSMACDInput input;
	CSMACDOutput output;

	public CSMACD ()
	{ 
		random = new Random (); 
		input = new CSMACDInput (this);
	} // End 

	public void simulate (int limit)
	{
		this.limit = limit;
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

    // To check statistics
    // for(int i = 0;i<k;i++){
    //   for(int j = 0;j<6;j++){
    //     System.out.println(statistics[i][j]);
    //   }
    // }
    
		output = new CSMACDOutput (statistics, k); 
	}// End Simulate

	private void attempt ()
	{
		event = new CSMACDEvent ();           
		event.add ("Attempt " + (k + 1) + ":");

    if(csma()){
      event.add ("Channel is busy, waiting a ramdom amount of time ...");
      wait (csmaSetting);
      while(csma()){
        wait (csmaSetting);
      }
    }
    event.add ("Channel is idle, ready to transmit ...");
    
    isTransDone = isTransDone();
    isColliDetected = isColliDetected();
    if(!isTransDone && !isColliDetected){
      event.add ("Transmit and Receive ...");
      while(!isTransDone && !isColliDetected){
        isTransDone = isTransDone();
        isColliDetected = isColliDetected();
      }
    }

    if(isTransDone){
      event.add ("Transmission done ...");
      statistics [k][0] = "Done";
    }else{
      event.add ("Transmission not done ...");
      statistics [k][0] = "Not done";
    }

    if(isColliDetected){
      event.add ("Collision detected, sending jamming signal ...");
      statistics [k][1] = "Detected";
      statistics [k][2] = "X";
      success = false;
    }else{
      event.add ("Collision not detected, Success");
      statistics [k][1] = "Not detected";
      statistics [k][2] = "O";
      statistics [k][3] = "X";
      statistics [k][4] = "NA";
      success = true;
    }
    
		if (!success)
		{
			k++;
			if (k >= limit)
			{
				event.add ("The whole process was aborted. We need to try another time.");
        statistics [k-1][3] = "O";
        statistics [k-1][4] = "NA";           
				abort = true;
			} 
			else 
			{
        event.add ("Waiting the TB timer to expire and to start a new attempt ...");
        // Random number R between 0 and 2^k-1
        // TB = R * Tfr
        tbSetting = tfrSetting * random.nextInt ((int) Math.pow (2, k));             
				wait(tbSetting);
        statistics [k-1][3] = "X";
        statistics [k-1][4] = String.valueOf (tbSetting);
        abort = false;
			}
		}
    // To check event
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

  // CSMA : non-Persistent
  // True : Channel busy , False : Channel idle
  public boolean csma ()
	{
		return (random.nextBoolean ());
	} // End csma

  // Check transmission done
  // True : Transmission done , False : Transmission not done
  public boolean isTransDone ()
	{
		return (random.nextBoolean ());
	} // End isTransDone

  // Check collision detected
  // True : Collision detected , False : Collision not detected
  public boolean isColliDetected ()
	{
		return (random.nextBoolean ());
	} // End isColliDetected

  // public boolean send (int timeoutSeconds)
	// {   
	// 	try
	// 	{
	// 		Thread.sleep (1000 * timeoutSeconds);
	// 	} 
	// 	catch (InterruptedException e){} 
	// 	return (random.nextBoolean ());
	// } // End send

	// public int getFreeChannel ()
	// {
	// 	return (random.nextInt ((int) Math.pow (2, k)));
	// } // End getFreeChannel

	public static void main (String args[])
	{      
		CSMACD csmacd = new CSMACD ();
	} // End main
	
} // End class