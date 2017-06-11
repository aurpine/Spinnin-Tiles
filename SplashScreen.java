/*  Justin Pu
    Spinnin' Tiles
    January 4, 2015
    Ms. Dyke

    Description:

    This thread creates a simultaneous animation in the splash screen.

    Variable Dictionary
     _______________________________________________________________________________________________________________________
    | Type      | Name      | Description                                                                                   |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Console   | c         | The reference to the output Console. Has a text size of 30 by 100. Also allows access to      |
    |           |           | the built-in methods.                                                                         |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | ang       | The angle of orientation of the 3D drawing.                                                   |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | x         | Holds the start x coordinate of the drawing.                                                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | String    | y         | Holds the start y coordinate of the drawing.                                                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | bgCol     | Holds the background colour so that redrawing is smooth.                                      |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | boolean   | draw      | Holds the boolean to continue drawing or not.                                                 |
    |___________|___________|_______________________________________________________________________________________________|

*/

import hsa.Console;
import java.awt.*;
import java.lang.*;

public class SplashScreen extends Thread
{
    private Console c;
    private int ang;
    private int x, y;
    private int prevX, prevY;
    private Color bgCol;
    private boolean draw;
    private static final int DELAY = 40;


    /*  This method converts a length to how long it looks like when rotated a certain degree of angles.

	Local Variable Dictionary
	 _______________________________________________________________________________________________________________________
	| Type      | Name      | Description                                                                                   |
	|-----------|-----------|-----------------------------------------------------------------------------------------------|
	| int       | num       | The value of the length to be drawn 3D.                                                       |
	|___________|___________|_______________________________________________________________________________________________|
    */
    private int to2D (int num)
    {
	return (int) (num * Math.cos (Math.toRadians (ang)));
    }


    /*  This method allows another program to set the x value (the x of the center of the rotating tile).

	Local Variable Dictionary
	 _______________________________________________________________________________________________________________________
	| Type      | Name      | Description                                                                                   |
	|-----------|-----------|-----------------------------------------------------------------------------------------------|
	| int       | newX      | The new value of x to be assigned.                                                            |
	|___________|___________|_______________________________________________________________________________________________|
    */
    public void setX (int newX)
    {
	x = newX;
    }


    /*  This method allows another program to set the y value (the y of the center of the rotating tile).

	Local Variable Dictionary
	 _______________________________________________________________________________________________________________________
	| Type      | Name      | Description                                                                                   |
	|-----------|-----------|-----------------------------------------------------------------------------------------------|
	| int       | newY      | The new value of y to be assigned.                                                            |
	|___________|___________|_______________________________________________________________________________________________|
    */
    public void setY (int newY)
    {
	y = newY;
    }


    //  This method stops the animation and ends the Thread.
    public void stopDrawing ()
    {
	draw = false;
    }


    /*  This constructor creates the new Thread and assigns the output console, the initial x coordinate, the initial y coordinate and the background colour.
    
	Local Variable Dictionary
	
	 _______________________________________________________________________________________________________________________
	| Type      | Name      | Description                                                                                   |
	|-----------|-----------|-----------------------------------------------------------------------------------------------|
	| Console   | con       | The output console passed to the SplashScreen Thread so that they are on the same window.     |
	|-----------|-----------|-----------------------------------------------------------------------------------------------|
	| int       | iniX      | The starting x center coordinate of the spinning tile.                                        |
	|-----------|-----------|-----------------------------------------------------------------------------------------------|
	| int       | iniY      | The starting y center coordinate of the spinning tile.                                        |
	|-----------|-----------|-----------------------------------------------------------------------------------------------|
	| Color     | col       | The background colour so that erasing blends in.                                              |
	|___________|___________|_______________________________________________________________________________________________|
    */
    public SplashScreen (Console con, int iniX, int iniY, Color col)
    {
	c = con;
	x = iniX;
	y = iniY;
	draw = true;
	bgCol = col;
    }


    /*  This method is the Thread that rotates the tile constantly. 
	
	The while loop runs as long as the draw is true. 
	    The for loop rotates the tile based on the angle. 
		The try block delays the program by a set amount of time. 
		An InterruptedException is caught in case the program couldn't delay the program.
	
	Local Variable Dictionary
	
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name      | Description                                                                                   |
	|-----------------------|-----------|-----------------------------------------------------------------------------------------------|
	| InterruptedException  | e         | Catches an InterruptedException in case the delay couldn't have been executed.                |
	|_______________________|___________|_______________________________________________________________________________________________|
    
    */
    public void run ()
    {
	while (draw)
	{
	    for (ang = 0 ; ang < 360 && draw ; ang += 5)
	    {
		int[] xc = {x + to2D (45), x + to2D (50), x + to2D (50), x + to2D (45), x - to2D (25), x - to2D (30), x - to2D (30), x - to2D (25) };
		int[] yc = {y + 20, y + 25, y + 135, y + 140, y + 140, y + 135, y + 25, y + 20};
		c.setColour (Color.black);
		c.fillPolygon (xc, yc, 8);


		xc = new int[]
		{
		    x + to2D (25), x + to2D (30), x + to2D (30), x + to2D (25), x - to2D (45), x - to2D (50), x - to2D (50), x - to2D (45)
		}
		;
		yc = new int[]
		{
		    y, y + 5, y + 115, y + 120, y + 120, y + 115, y + 5, y
		}
		;
		c.setColour (Color.lightGray);
		c.fillPolygon (xc, yc, 8);
		c.setColour (Color.black);
		c.drawPolygon (xc, yc, 8);

		c.setColour (Color.red);
		if (ang < 90 || ang > 270)
		{
		    c.fillMapleLeaf (x - to2D (40), y + 30, to2D (60), 60);
		}
		prevX = x;
		prevY = y;

		// Delay
		try
		{
		    sleep (DELAY);
		}
		catch (InterruptedException e)
		{
		}

		// Erase
		c.setColour (bgCol);
		c.fillRect (prevX - Math.abs (to2D (50)) - 5, prevY - 5, Math.abs (to2D (100)) + 10, 150);
	    }
	}
    }
}

