/*  Justin Pu
    Spinnin' Tiles
    December 30, 2014
    Ms. Dyke

    Description:

    This class implements a timer that counts how long the user has been playing the game for. 

    Variable Dictionary
     _______________________________________________________________________________________________________________________
    | Type      | Name      | Description                                                                                   |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Console   | c         | The reference to the output Console. Has a text size of 30 by 100. Also allows access to      |
    |           |           | the built-in methods.                                                                         |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | time      | The total amount of time in seconds that the user has been playing the game for.              |                                                                                     |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | boolean   | count     | Holds whether to continue counting or not in case the game has ended.                         |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | bg        | Holds the background colour.                                                                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | fg        | Holds the foreground colour.                                                                  |
    |___________|___________|_______________________________________________________________________________________________|

*/

import java.lang.*;
import java.awt.*;
import hsa.Console;

public class TimerThread extends Thread
{
    private Console c;
    private int time;
    private boolean count;
    private Color bg;
    private Color fg;

    //  This method draws the timer on the top bar of the game window. 
    public void draw ()
    {
	int seconds = time % 60;
	int minutes = (time / 60) % 60;
	int hours = time / 60 / 60;
	
	String temp = ((hours / 10 > 0) ? ( "" + hours) : ("0" + hours)) + ":" + ((minutes / 10 > 0)? ("" +minutes) : ("0" + minutes)) + ":" + ((seconds / 10 > 0) ? ("" + seconds) : ("0" + seconds));
	c.setTextBackgroundColour (fg);
	c.setTextColour (bg);
	c.setCursor (1, 1);
	c.print (' ', 50 - temp.length () / 2);
	c.println (temp);
	c.setTextBackgroundColour (bg);
	c.setTextColour (fg);
    }


    /*  This method runs the Thread and counts the time. 
	
	The try block delays the Thread for one second.
	An InterruptedException is caught in case it can't be slept. 
	The while loop continues as long as the Thread is instructed to count up (count is true).
	    The try block in the Thread delays the program so that one second can pass before a second is added to the time.
	    An InterruptedException is caught in case the program can't be delayed. 
	
	Local Variable Declaration
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| InterruptedException  | e             | Catches an InterruptedException in case the program can't be delayed properly.            |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| InterruptedException  | e             | Catches an InterruptedException in case the program can't be delayed properly.            |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    public void run ()
    {
	try
	{
	    sleep (1000);
	}
	catch (InterruptedException e)
	{
	}
	count = true;
	while (count)
	{
	    time++;
	    draw ();
	    
	    try
	    {
		sleep (1000);
	    }
	    catch (InterruptedException e)
	    {
	    }
	}
    }
    
    
    //  Stops the counter from running.
    public void stopCounter ()
    {
	count = false;
    }
    
    
    //  Returns the amount of seconds that the user has been playing the game for. 
    public int getSeconds ()
    {
	return time;
    }


    /*  The constructor creates the instance of the program. It also sets the Console window and the background and forground colours. 
    
	Local Variable Declaration
	
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| Console               | Con           | Gives the Thread the same Console window to draw the time.                                |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| Color                 | BG            | Passes the same background colour as in the other classes.                                |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| Color                 | FG            | Passes the same forground colour as in the other classes.                                 |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    public TimerThread (Console con, Color BG, Color FG)
    {
	c = con;
	bg = BG;
	fg = FG;
    }
}


