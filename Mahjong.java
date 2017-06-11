/*  Justin Pu
    Spinnin' Tiles
    December 19, 2014
    Ms. Dyke


    Variable Dictionary
     _______________________________________________________________________________________________________________________
    | Type      | Name      | Description                                                                                   |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Console   | c         | The reference to the same output Console as the one created in the Mahjong class.             |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Timer-    | t         | The reference variable the TimerThread object instance. It allows access to the TimerThread   |
    | Thread    |           | methods. It implements a timer into the game.                                                 |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int[] []  | tiles     | Holds the values of all the tiles of the board. Anything from 1 - 42 denotes a specific       |
    | []        |           | pattern. A value of -1 means no tile exists. A value of 0 is temporary for a tile that        |
    |           |           | currently has no pattern.                                                                     |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Image[]   | pattern   | The image textures of the tiles. They all have unique patterns.                               |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Image[]   | shadow    | Tbe image textures of shadows from 8 different directions.                                    |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Image     | selImg    | The texture to be overlapped a tile to indicate that it is currently selected.                |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Image     | background| The background image to be drawn to clear the screen.                                         |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Image     | curImg    | The image to be overlapped a tile to indicate that it is where the cursor is currently at.    |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | String[]  | HEADER    | The String array that holds the board file headers.                                           |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int[]     | cur       | The x and y location of the cursor.                                                           |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int[]     | sel       | The x and y location of the selected tile.                                                    |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | totPat    | The total number of different patterns available to be generated as instructed by the board   |
    |           |           | file.                                                                                         |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | sX        | The x start position of the board drawing. Used for drawing the tiles.                        |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | sY        | The y start position of the board drawing.                                                    |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | score     | The score earned by the user in the game.                                                     |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | prevTile  | The pattern of the previous tile pair matched. This is used to determine the multiplier.      |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | multiplier| The current amount of multipliers. (The multiplier is stored in powers of 2)                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | numTiles  | The total number of tiles in the whole board.                                                 |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | level     | The level difficulty of the game. (1 = easy, 2 = medium, 3 = hard)                            |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | boolean   | quit      | The boolean to store whether the user has chosen to quit or not.                              |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | bg        | Holds the background colour.                                                                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | fg        | Holds the foreground colour.                                                                  |
    |___________|___________|_______________________________________________________________________________________________|


*/

import java.io.*;
import hsa.Console;
import java.awt.*;
import javax.swing.JOptionPane;

class Mahjong
{
    private Console c;
    private TimerThread t;
    private int[] [] [] tiles; // 0 = none assigned, -1 = doesn't exist
    private static Image[] pattern = new Image [45], shadow = new Image [8];
    private static Image selImg, background, curImg;
    private static final String HEADER = "[HEADER] Spinnin' Tiles board file";
    private static final String[] BFILE = {"Easy.pmb", "Medium.pmb", "Hard.pmb"};
    private int totPat, sX, sY, score, prevTile, multiplier, numTiles;
    private int[] cur = new int [2], sel = new int [2]; // 0 - x, 1 - y, 2 - z.
    private int level;
    private boolean quit;
    private Color bg;
    private Color fg;


    /*  This method sets the level with the new level parameter passed through.

	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | newLevel      | The new value of the level int variable.                                                  |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    public void setLevel (int newLevel)
    {
	level = newLevel;
    }


    //  Returns the value of the score variable so that the classes accessing this class can display it.
    public int getScore ()
    {
	return score;
    }


    //  This method resets the variables of the class so that a new game can be played.
    private void reset ()
    {
	t = new TimerThread (c, bg, fg);
	cur [0] = tiles.length / 2 - 1;
	cur [1] = tiles [0].length / 2 - 1;
	moveCursor ();
	sel [0] = -1;
	sel [1] = -1;
	score = 0;
	prevTile = 0;
	multiplier = 0;
	quit = false;
    }


    /*  This method shuffles an array passed randomly so that the tiles can be randomized.

	The for loop goes through all of the values in the array. It keeps track of the index of the array to be switched into the random index of the new array.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | Holds the index of the value in the original array to be switched into the second. Starts |
	|                       |               | as 0 and increases by 1 until it's the value of the array's length.                       |
	|_______________________|_______________|___________________________________________________________________________________________|

	    The nested for loop counts and finds the randomized index of where the value of the second array goes.
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | y             | Holds the index of the current value in the array it is at. It starts at 0 and increases  |
	    |                       |               | by 1 and has a maximum value of the length of the array - 1.                              |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | z             | Starts as -1. It increases by 1 when the array index it is at has no value. The loop ends |
	    |                       |               | when the z reaches the randomized number n.                                               |
	    |_______________________|_______________|___________________________________________________________________________________________|

		The if structure checks if the newArr array's value at the current y index is blank (0). If it is, then the the z counter goes up.
		The if structure checks if y is equal to z, meaning that the array has counted to it's objective and will assign the value to the new array and break.

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[]                 | original      | The original array to be shuffled.                                                        |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[]                 | newArr        | The new array to be returned.                                                             |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | n             | Holds the randomized index of where the old value goes in the new.                        |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private int[] shuffle (int[] original)
    {
	int[] newArr = new int [original.length];

	for (int x = 0 ; x < original.length ; x++)
	{
	    int n = (int) (Math.random () * (original.length - x));

	    for (int y = 0, z = -1 ; y < original.length ; y++)
	    {
		if (newArr [y] == 0)
		{
		    z++;
		}
		if (z == n)
		{
		    newArr [y] = original [x];
		    break;
		}
	    }
	}
	return newArr;
    }


    /*  This method checks the number of free tiles on the board

	The for loop goes through all the x indexes of the board array.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | This variable keeps track of the x index to search. It starts as 0 and increases by 1     |
	|                       |               | until it reaches the maximum index of the array.                                          |
	|_______________________|_______________|___________________________________________________________________________________________|
	    The for loop goes through all the y indexes of the board array.

	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | y             | This variable keeps track of the y index to search. It starts as 0 and increases by 1     |
	    |                       |               | until it reaches the maximum index of the array.                                          |
	    |_______________________|_______________|___________________________________________________________________________________________|

		The for loop goes through all the z indexes of the board array.

		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | z             | This variable keeps track of the y index to search. It starts as 0 and increases by 1     |
		|                       |               | until it reaches the maximum index of the array.                                          |
		|_______________________|_______________|___________________________________________________________________________________________|

		    The if structure checks the the tile is free, then it increases the counter.


	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | num           | This variable holds the number of free tiles that remain on the board.                    |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[] [] []           | tiles         | This passed variable holds the board pattern that the method checks.                      |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private int numFree (int[] [] [] tiles)
    {
	int num = 0;
	for (int x = 0 ; x < tiles.length ; x++)
	{
	    for (int y = 0 ; y < tiles [0].length ; y++)
	    {
		for (int z = 0 ; z < tiles [0] [0].length ; z++)
		{
		    if (free (tiles, x, y, z))
		    {
			num++;
		    }
		}
	    }
	}
	return num;
    }


    /*  This method checks if the tile specified is 'free' or not

	The if structure checks if it is free, if it is, then it returns true, otherwise it returns false.

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[] [] []           | tiles         | This passed variable holds the board pattern that the method checks.                      |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | The x index of the array to check.                                                        |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | y             | The y index of the board to check.                                                        |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | z             | The z index of the board to check.                                                        |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private boolean free (int[] [] [] tiles, int x, int y, int z)    // Checks if the tile is free
    {
	if (tiles [x] [y] [z] != -1 && (z == tiles [0] [0].length - 1 || tiles [x] [y] [z + 1] == -1) && (x == 0 || x == tiles.length - 1 || tiles [x - 1] [y] [z] == -1 || tiles [x + 1] [y] [z] == -1))
	{
	    return true;
	}
	return false;
    }


    /*  Checks whether the tile given is free or not based on just two variables. It checks the default board, tiles. It also finds the top-most tile.

	The for loop goes through all of the z values until it finds the top-most tile. It starts from the top.

	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | z             | This variable holds the current index of z to check on the board. Starts at the highest   |
	|                       |               | possible value of z. Then it decreases by 1 until it finds the tile or it reaches 0.      |
	|_______________________|_______________|___________________________________________________________________________________________|

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | The x index of the array to check.                                                        |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | y             | The y index of the board to check.                                                        |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private boolean free (int x, int y)
    {
	for (int z = tiles [0] [0].length - 1 ; z >= 0 ; z--)
	{
	    if (tiles [x] [y] [z] != -1)
	    {
		return free (tiles, x, y, z);
	    }
	}
	return false;
    }


    /*  Tries to read the pattern from the board.

	The try block allows a file to be read without crashing the program.
	    The if structure checks the header of the file, if it doesn't match, then the method returns false, meaning reading the file wasn't successful.
	    The if structure checks whether the board exceeds the maximum dimensions. If it does, then the method returns false.

	    The for loop reads the board from the file by layers (z).
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | z             | Holds which layer is currently being read. Starts at 0 and increases by 1 until it reaches|
	    |                       |               | the maximum length.                                                                       |
	    |_______________________|_______________|___________________________________________________________________________________________|
		The for loop reads the board by rows (lines).
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | y             | Holds which row is currently being read. Starts at 0 and increases by 1 until it reaches  |
		|                       |               | the maximum length.                                                                       |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The for loop reads the board by columns (per character).
		     ___________________________________________________________________________________________________________________________________
		    | Type                  | Name          | Description                                                                               |
		    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
		    | int                   | x             | Holds which column is currently being read. Starts at 0 and increases by 1 until it       |
		    |                       |               | reaches the maximum length.                                                               |
		    |_______________________|_______________|___________________________________________________________________________________________|

			The if structure checks if the character is a '+' then it sets that tile's value to 0 (kept as is). Otherwise, the tile is set to -1.
			    The if structure checks if there is a tile under the current tile, if there isn't then the board is set as bad and false is returned.
	    If the total amount of tiles is odd, then false is returned.
	IOException is caught in case the file doesn't exist.
	NullPointerException is caught in case a line is empty when it's not supposed to be.
	StringIndexOutOfBoundsException is caught in case a line is shorter than it is supposed to be in characters.

	The for loop goes through all the layers and checks how many tiles are at the left to add offset.

	int     z       Holds the value of the z layer to be checked. Starts as 0, increases by 1 until it reaches the max length.

	    The for loop goes through all the x values to check if the layer needs offset.

	    int     x       Holds the value of the x tile to be checked. Starts at 0 and increases by 1 until the maximum.

		The if structure checks if there is a tile there and adds offset if there is and breaks the loop for that layer.

	The for loop goes through all the layers and checks for many tiles are the the top to add offset.

	int     z       Holds the value of the z layer to be checked. Starts as 0, increases by 1 until it reaches the max length.

	    The for loop goes thrgouh all the y tiles to check if the layer needs offset.

	    int     y       Holds the value of the y tile to be checked. Starts at 0 and increases by 1 until the maximum.

		The if structure checks if there is a tile there and adds offset if there is and breaks the loop for that layer.

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| BufferedReader        | in            | The reference to the input file to read from the board file.                              |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| String                | temp          | The temporary string read from the file to be used to check for the placement of tiles.   |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| IOException           | e             | Catches an IOException in case the file cannot be found or read.                          |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| NullPointerException  | e             | Catches a NullPointerException in case a blank line is empty                              |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| NumberFormatException | e             | Catches a NumberFormatException in case the line cannot be parsed into a number           |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| StringIndexOutOf-     | e             | Catches a StringIndexOutOfBoundsException in case the line is shorter than expected.      |
	| BoundsException       |               |                                                                                           |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private boolean getPattern ()    // True if successful, false if not
    {
	try
	{
	    BufferedReader in = new BufferedReader (new FileReader (BFILE [level]));
	    if (!in.readLine ().equals (HEADER))
	    {
		return false;
	    }
	    totPat = Integer.parseInt (in.readLine ());
	    tiles = new int [Integer.parseInt (in.readLine ())] [Integer.parseInt (in.readLine ())] [Integer.parseInt (in.readLine ())]; // Initialization

	    if (tiles.length > 16 || tiles.length == 0 || tiles [0].length > 8 || tiles [0].length == 0 || tiles [0] [0].length > 5 || tiles [0] [0].length == 0 || totPat > 42 || totPat < 1) // Invalid number of unique tiles or invalid board size
	    {
		return false;
	    }

	    numTiles = 0;
	    for (int z = 0 ; z < tiles [0] [0].length ; z++)
	    {
		in.readLine ();
		for (int y = 0 ; y < tiles [0].length ; y++)
		{
		    String temp = in.readLine ();

		    for (int x = 0 ; x < tiles.length ; x++)
		    {
			if (temp.charAt (x) == '+')
			{
			    if (z != 0 && tiles [x] [y] [z - 1] == -1)
			    {
				return false;
			    }
			    numTiles++;
			}
			else
			{
			    tiles [x] [y] [z] = -1;
			}
		    }
		}
	    }
	    if (numTiles % 2 == 1)
	    {
		return false;
	    }
	}
	catch (IOException e)
	{
	    return false;
	}
	catch (NullPointerException e)
	{
	    return false;
	}
	catch (NumberFormatException e)
	{
	    return false;
	}
	catch (StringIndexOutOfBoundsException e)
	{
	    return false;
	}


	// Sets the game board start coordinates
	sX = (800 - 45 * tiles.length) / 2;
	sY = (640 - 65 * tiles [0].length) / 2;

	int xOff = 0;
	int yOff = 0;
	// Offset for x coordinate based on number of layers
	for (int z = 0 ; z < tiles [0] [0].length && z == xOff ; z++)
	{
	    for (int x = 0 ; x < tiles.length ; x++)
	    {
		if (tiles [x] [0] [z] == 0)
		{
		    xOff++;
		    break;
		}
	    }
	}
	// Offset for the y coordinate based on the number of layers
	for (int z = 0 ; z < tiles [0] [0].length && z == yOff ; z++)
	{
	    for (int y = 0 ; y < tiles [0].length ; y++)
	    {
		if (tiles [0] [y] [z] == 0)
		{
		    yOff++;
		    break;
		}
	    }
	}
	sX += (xOff * 5) / 3;
	sY += (yOff * 5) / 3;

	return true;
    }


    /*  Checks if the user lost the game or not. Returns true if they lost, otherwise, it returns false.

	The for loop goes through all the x values.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | Holds the x coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
	|                       |               | reaches the maximum length.                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | num           | Holds the index of the array of the patterns of the previous tiles. Starts at 0 and       |
	|                       |               | increases each time a free tile is checked. Loop stops once num reaches the numFree - 1.  |
	|_______________________|_______________|___________________________________________________________________________________________|
	    The for loop reads the board by rows (lines).
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | y             | Holds the y coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
	    |                       |               | reaches the maximum length.                                                               |
	    |_______________________|_______________|___________________________________________________________________________________________|
		The for loop reads the board by columns (per character).
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | z             | Holds the z coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
		|                       |               | reaches the maximum length.                                                               |
		|_______________________|_______________|___________________________________________________________________________________________|

		    If the tile being checked if free, then it adds its pattern to the array and checks the other patterns to see if there's a matching one.

			The for loop goes through all the previous patterns in the array and checks for a pair.
			 ___________________________________________________________________________________________________________________________________
			| Type                  | Name          | Description                                                                               |
			|-----------------------|---------------|-------------------------------------------------------------------------------------------|
			| int                   | n             | Holds the index of the array it is currently checking a match with. Starts at 0 and       |
			|                       |               | increases by 1 until it reaches num.                                                      |
			|_______________________|_______________|___________________________________________________________________________________________|

			    The if structure checks if the current indexed pattern is the same as the tile being checked (a match can be made).

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[]                 | pat           | An array that holds the values of the tile patterns that are free.                        |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private boolean lost ()  // Determines if the user lost or not
    {
	int[] pat = new int [numFree (tiles)];

	for (int x = 0, num = 0 ; x < tiles.length && num < pat.length ; x++)
	{
	    for (int y = 0 ; y < tiles [0].length ; y++)
	    {
		for (int z = 0 ; z < tiles [0] [0].length ; z++)
		{
		    if (free (tiles, x, y, z))
		    {
			pat [num] = tiles [x] [y] [z];
			for (int n = 0 ; n < num ; n++)
			{
			    if (pat [n] == pat [num] || ("" + pat [n]).matches ("26|27|39|40") && ("" + pat [num]).matches ("26|27|39|40") || ("" + pat [n]).matches ("28|29|41|42") && ("" + pat [num]).matches ("28|29|41|42"))
			    {
				return false;
			    }
			}
			num++;
		    }
		}
	    }
	}
	t.stopCounter ();
	JOptionPane.showMessageDialog (null, "You lost the game because there are no more moves available.", "Game Over!", JOptionPane.PLAIN_MESSAGE);
	return true;
    }


    /*  This method loads all the pictures needed by the game.

	The for loop loads all the tile pictures.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | Holds the x tile pattern picture that is to be loaded. x starts at 0 and increases by 1   |
	|                       |               | until it reaches 42.                                                                      |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop loads all the shadow pictures.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | Holds the x tile pattern picture that is to be loaded. x starts at 0 and increases by 1   |
	|                       |               | until it reaches 8.                                                                       |
	|_______________________|_______________|___________________________________________________________________________________________|

	The try block loads all the images without the program crashing in case it doesn't work.

	Local Variable Declaration
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| InterruptedException  | e             | Catches an InterruptedException in case the pictures could not be loaded.                 |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| MediaTracker          | tracker       | The tracker reference to load the pictures.                                               |
	|_______________________|_______________|___________________________________________________________________________________________|

    */
    private void load ()
    {
	MediaTracker tracker = new MediaTracker (new Frame ());

	for (int x = 0 ; x <= 42 ; x++)
	{
	    pattern [x] = Toolkit.getDefaultToolkit ().getImage ("textures\\tile" + (x) + ".gif");
	    tracker.addImage (pattern [x], 0);
	}
	for (int x = 0 ; x < 8 ; x++)
	{
	    shadow [x] = Toolkit.getDefaultToolkit ().getImage ("textures\\shadow" + (x) + ".png");
	    tracker.addImage (shadow [x], 0);
	}
	curImg = Toolkit.getDefaultToolkit ().getImage ("textures\\cursor.png");
	tracker.addImage (curImg, 0);
	selImg = Toolkit.getDefaultToolkit ().getImage ("textures\\selected.png");
	tracker.addImage (selImg, 0);
	background = Toolkit.getDefaultToolkit ().getImage ("textures\\background.gif");
	tracker.addImage (background, 0);
	try
	{
	    tracker.waitForAll ();
	}
	catch (InterruptedException e)
	{
	}
    }


    /*  Draws the whole board.

	The if structure draws the background if the background needs to be drawn.

	The for loop checks all the layers on the board.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | z             | Holds the value of which layer to check. Starts at 0, increases by 1 until max value.     |
	|_______________________|_______________|___________________________________________________________________________________________|
	    The for loop checks all the columns.
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | x             | Holds the value of which column to check. Starts at 0, increases by 1 until max value.    |
	    |_______________________|_______________|___________________________________________________________________________________________|
		The for loop checks all the rows.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | y             | Holds the value of which row to check. Starts at 0, increases by 1 until max value.       |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The if structure checks if a tile needs to be drawn depending on if the background is drawn too.
			The ternary operator checks if partly visible tiles need to be drawn depending on whether the background is being drawn or not.
			The if structure checks if the cursor image needs to be drawn.
			The if structure checks if the selected image needs to be drawn.
			The if structure checks if a shadow may possibly need to be drawn.
			    The if structure checks if a shadow from the top needs to be drawn.
			    The if structure checks if a shadow from the top right needs to be drawn.
			    The if structure checks if a shadow from the right needs to be drawn.
			    The if structure checks if a shadow from the bottom right needs to be drawn.
			    The if structure checks if a shadow from the bottom needs to be drawn.
			    The if structure checks if a shadow from the bottom left needs to be drawn.
			    The if structure checks if a shadow from the left needs to be drawn.
			    The if structure checks if a shadow from the top left needs to be drawn.
	If the background needs to be drawn, it also updates the score and number of free tiles.

	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| boolean               | drawBackground| Whether the background needs to be drawn or not.                                          |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private void draw (boolean drawBackground)
    {
	if (drawBackground)
	{
	    c.drawImage (background, 0, 0, null);
	}
	for (int z = 0 ; z < tiles [0] [0].length ; z++)
	{
	    for (int x = 0 ; x < tiles.length ; x++)
	    {
		for (int y = 0 ; y < tiles [0].length ; y++)
		{
		    if (((!drawBackground) ? (z == tiles [0] [0].length - 1 || tiles [x] [y] [z + 1] == -1):
		    true) && (tiles [x] [y] [z] != -1 && ((x == tiles.length - 1 || tiles [x + 1] [y] [z] == -1) || (y == tiles [0].length - 1 || tiles [x] [y + 1] [z] == -1) || (z == tiles [0] [0].length - 1 || tiles [x] [y] [z + 1] == -1) || ((drawBackground) ? (tiles [x + 1] [y + 1] [z + 1] == -1):
		    false))))
		    {
			c.drawImage (pattern [tiles [x] [y] [z]], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);  // Background

			if (cur [0] == x && cur [1] == y && (z == tiles [0] [0].length - 1 || tiles [x] [y] [z + 1] == -1))
			{
			    c.drawImage (curImg, sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			}
			if (sel [0] == x && sel [1] == y && (z == tiles [0] [0].length - 1 || tiles [x] [y] [z + 1] == -1))
			{
			    c.drawImage (selImg, sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			}

			// Draws the shadows
			if (z != tiles [0] [0].length - 1 && tiles [x] [y] [z + 1] == -1)
			{
			    if (y != 0 && tiles [x] [y - 1] [z + 1] != -1)
			    {
				c.drawImage (shadow [0], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			    if (x != tiles.length - 1 && y != 0 && tiles [x + 1] [y - 1] [z + 1] != -1 && tiles [x] [y - 1] [z + 1] == -1 && tiles [x + 1] [y] [z + 1] == -1)
			    {
				c.drawImage (shadow [1], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			    if (x != tiles.length - 1 && tiles [x + 1] [y] [z + 1] != -1)
			    {
				c.drawImage (shadow [2], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			    if (x != tiles.length - 1 && y != tiles [0].length - 1 && tiles [x + 1] [y + 1] [z + 1] != -1 && tiles [x + 1] [y] [z + 1] == -1 && tiles [x] [y + 1] [z + 1] == -1)
			    {
				c.drawImage (shadow [3], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			    if (y != tiles [0].length - 1 && tiles [x] [y + 1] [z + 1] != -1)
			    {
				c.drawImage (shadow [4], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			    if (x != 0 && y != tiles [0].length - 1 && tiles [x - 1] [y + 1] [z + 1] != -1 && tiles [x] [y + 1] [z + 1] == -1 && tiles [x - 1] [y] [z + 1] == -1)
			    {
				c.drawImage (shadow [5], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			    if (x != 0 && tiles [x - 1] [y] [z + 1] != -1)
			    {
				c.drawImage (shadow [6], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			    if (x != 0 && y != 0 && tiles [x - 1] [y - 1] [z + 1] != -1 && tiles [x - 1] [y] [z + 1] == -1 && tiles [x] [y - 1] [z + 1] == -1)
			    {
				c.drawImage (shadow [7], sX + x * 45 - z * 5, sY + y * 65 - z * 5, null);
			    }
			}
		    }
		}
	    }
	}
	if (drawBackground)
	{
	    t.draw ();
	    c.setTextBackgroundColour (fg);
	    c.setTextColour (bg);
	    c.setCursor (2, 1);
	    c.print (' ', (100 - (numFree (tiles) + "" + score).length () - 21) / 2);
	    c.println (numFree (tiles) + " tiles free | " + score + " points");
	    c.setTextBackgroundColour (bg);
	    c.setTextColour (fg);
	}
    }


    /*  Gets the time bonus that will be added to the score.

	If the amount of time taken to solve the puzzle is greater than the maximum time to get time bonus, then no time bonus is given.
    */
    private int getTimeBonus ()
    {
	int maxTime = totPat * numTiles / 4;
	if (t.getSeconds () > maxTime)
	{
	    return 0;
	}
	return (maxTime - t.getSeconds ()) * 42 * 142 / 2 / maxTime;
    }


    /*  The board is randomly generated here.

	The for loop goes through all the x values.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | Holds the x coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
	|                       |               | reaches the maximum length.                                                               |
	|_______________________|_______________|___________________________________________________________________________________________|
	    The for loop reads the board by rows (lines).
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | y             | Holds the y coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
	    |                       |               | reaches the maximum length.                                                               |
	    |_______________________|_______________|___________________________________________________________________________________________|
		The for loop reads the board by columns (per character).
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | z             | Holds the z coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
		|                       |               | reaches the maximum length.                                                               |
		|_______________________|_______________|___________________________________________________________________________________________|

	The while loop keeps running until all the tiles have been cleared from the generation board.
	    The for loop goes through all the x values.
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | x             | Holds the x coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
	    |                       |               | reaches the maximum length.                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | num           | Holds the index of the array of the patterns of the previous tiles. Starts at 0 and       |
	    |                       |               | increases each time a free tile is checked. Loop stops once num reaches the numFree - 1.  |
	    |_______________________|_______________|___________________________________________________________________________________________|
		The for loop reads the board by rows (lines).
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | y             | Holds the y coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
		|                       |               | reaches the maximum length.                                                               |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The for loop reads the board by columns (per character).
		     ___________________________________________________________________________________________________________________________________
		    | Type                  | Name          | Description                                                                               |
		    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
		    | int                   | z             | Holds the z coordinate of the tile being checked. Starts at 0 and increases by 1 until it |
		    |                       |               | reaches the maximum length.                                                               |
		    |_______________________|_______________|___________________________________________________________________________________________|
			If the tile is free, then it checks if it's the one that needs to be omitted.
			    If it is the one that needs to be omitted, then the omitted value is set to -1.
			    Otherwise, the tile's locations are stored.
	    The for loop assigns random values to the pairs of tiles.
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | x             | Holds the first index of the pair to be assigned. x starts as 0 and increases by 1 until  |
	    |                       |               | the maximum number divided by 2.
	    |_______________________|_______________|___________________________________________________________________________________________|
	    If the number of tiles free is 1 (meaning impossible to generate anymore), then the loop is broken out of.


	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[] [] []           | gen           | Holds the new virtual board used for generation purposes.                                 |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[]                 | xLoc          | Holds the x locations of the tiles. Has a length of the number of free tiles rounded by 2.|
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[]                 | yLoc          | Holds the y locations of the tiles. Has same length as xLoc.                              |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int[]                 | zLoc          | Holds the z locations of the tiles. Has same length as xLoc.                              |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | omit          | Contains the index of the tile to omit. It is random.                                     |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    private boolean generate ()
    {
	int[] [] [] gen = new int [tiles.length] [tiles [0].length] [tiles [0] [0].length]; // Second board for generation

	// Assigns the new board
	for (int x = 0 ; x < tiles.length ; x++)
	{
	    for (int y = 0 ; y < tiles [0].length ; y++)
	    {
		for (int z = 0 ; z < tiles [0] [0].length ; z++)
		{
		    gen [x] [y] [z] = tiles [x] [y] [z];
		}
	    }
	}

	while (numFree (gen) != 0)
	{
	    int[] xLoc = new int [numFree (gen) / 2 * 2];
	    int[] yLoc = new int [xLoc.length];
	    int[] zLoc = new int [xLoc.length];

	    int omit = (numFree (gen) % 2 == 1) ? (int) (Math.random () * numFree (gen)):
	    - 1;                                                                                    // The index to omit

	    for (int x = 0, num = 0 ; x < gen.length && num < xLoc.length ; x++)
	    {
		for (int y = 0 ; y < gen [0].length ; y++)
		{
		    for (int z = 0 ; z < gen [0] [0].length ; z++)
		    {
			if (free (gen, x, y, z))
			{
			    if (num == omit)
			    {
				omit = -1;
			    }
			    else
			    {
				xLoc [num] = x;
				yLoc [num] = y;
				zLoc [num] = z;
				num++;
			    }
			}
		    }
		}
	    }


	    int[] tilePat = new int [xLoc.length]; // Holds the tile designs

	    for (int x = 0 ; x < tilePat.length / 2 ; x++)    // Assigns the pattern designs to the tiles
	    {
		tilePat [x * 2] = (int) (Math.random () * totPat + 1);
		tilePat [x * 2 + 1] = tilePat [x * 2];
	    }

	    tilePat = shuffle (tilePat);    // Randomizes the tiles

	    // Assigns the tiles
	    for (int x = 0 ; x < xLoc.length ; x++)
	    {
		gen [xLoc [x]] [yLoc [x]] [zLoc [x]] = -1;
		tiles [xLoc [x]] [yLoc [x]] [zLoc [x]] = tilePat [x];
	    }

	    if (numFree (gen) == 1)
	    {
		return false;
	    }
	}
	return true;
    }


    /*  Gets the pattern of the selected tile.

	The for loop checks the layers (z) from top to bottom.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | z             | Holds the z coordinate of the tile being checked. Starts at the maximum value and         |
	|                       |               | decreases by 1 until it reaches 0 or returns.                                             |
	|_______________________|_______________|___________________________________________________________________________________________|
	    The if structure checks if there is a tile there. If there is, then it's value/pattern is returned.
    */
    private int getSelectedPat ()
    {
	for (int z = tiles [0] [0].length - 1 ; z >= 0 ; z--)
	{
	    if (tiles [sel [0]] [sel [1]] [z] != -1)
	    {
		return tiles [sel [0]] [sel [1]] [z];
	    }
	}
	return -1;
    }


    /*  Gets the pattern of the ctile that the cursor is on.

	The for loop checks the layers (z) from top to bottom.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | z             | Holds the z coordinate of the tile being checked. Starts at the maximum value and         |
	|                       |               | decreases by 1 until it reaches 0 or returns.                                             |
	|_______________________|_______________|___________________________________________________________________________________________|
	    The if structure checks if there is a tile there. If there is, then it's value/pattern is returned.
    */
    private int getCursorPat ()
    {
	for (int z = tiles [0] [0].length - 1 ; z >= 0 ; z--)
	{
	    if (tiles [cur [0]] [cur [1]] [z] != -1)
	    {
		return tiles [cur [0]] [cur [1]] [z];
	    }
	}
	return -1;
    }


    /*  Moves the cursor after a move to the closest tile to it's current location.

	The if structure checks if the tile that it is on exists, if it does, then it breaks.

	The for loop counts how far to expand away from the cursor.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | a             | Holds the value of a, how far to go away from the cursor to check. Starts at zero and     |
	|                       |               | keeps on increasing by 1.                                                                 |
	|_______________________|_______________|___________________________________________________________________________________________|
	    The for loop counts how far to expand away from the cursor in the other direction.
	     ___________________________________________________________________________________________________________________________________
	    | Type                  | Name          | Description                                                                               |
	    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
	    | int                   | b             | Holds the value of b, how far to go away from the cursor the other way to check. Starts at|
	    |                       |               | zero and keeps on increasing by 1 until it reaches the farthest length to a border.       |
	    |_______________________|_______________|___________________________________________________________________________________________|
		The if structure checks the closest tile to the left.
		The if structure checks the closest tile to the up.
		The if structure checks the closest tile to the right.
		The if structure checks the closest tile to the down.
    */
    private void moveCursor ()
    {
	if (tiles [cur [0]] [cur [1]] [0] != -1)
	{
	    return;
	}

	for (int a = 0 ; true ; a++)
	{
	    for (int b = 1 ; b < Math.max (tiles.length - cur [0] - 1, Math.max (cur [0], Math.max (tiles [0].length - cur [1] - 1, cur [1]))) ; b++)
	    {
		if (cur [0] - b >= 0 && cur [1] - a >= 0 && tiles [cur [0] - b] [cur [1] - a] [0] != -1)
		{
		    cur [0] -= b;
		    cur [1] -= a;
		    return;
		}
		if (cur [0] + a < tiles.length && cur [1] - b >= 0 && tiles [cur [0] + a] [cur [1] - b] [0] != -1)
		{
		    cur [0] += a;
		    cur [1] -= b;
		    return;
		}
		if (cur [0] + b < tiles.length && cur [1] + a < tiles [0].length && tiles [cur [0] + b] [cur [1] + a] [0] != -1)
		{
		    cur [0] += b;
		    cur [1] += a;
		    return;
		}
		if (cur [0] - a >= 0 && cur [1] + b < tiles [0].length && tiles [cur [0] - a] [cur [1] + b] [0] != -1)
		{
		    cur [0] -= a;
		    cur [1] += b;
		    return;
		}
	    }
	}
    }


    /*  This method processes a key press

	The while loop keep running so that it only updates when the user enters a valid key.
	    If the key pressed is backspace, then the program prompts the user if they want to quit or not.
	    Otherwise, if the D key is pressed, then the board is checked.
		The for loop checks if the file is directly to the right.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | x             | The index of the tile to be checked. Starts as the current index and increases until max. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The if structure checks if a tile is there.
		The for loop holds on which side beside the tile to check for tiles.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | x             | The index of the tile to be checked. Starts as the current index and increases until max. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The for loop holds how far up to check tiles so that all tiles are checked.
		     ___________________________________________________________________________________________________________________________________
		    | Type                  | Name          | Description                                                                               |
		    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
		    | int                   | y             | The index of the tile above to check. Starts as the current index and increases until max.|
		    |_______________________|_______________|___________________________________________________________________________________________|
			The if structure checks upper-right.
			The if structure checks lower-right.

	    Otherwise, if the A key is pressed, then the board is checked.
		The for loop checks if the file is directly to the left.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | x             | The index of the tile to be checked. Starts as the current index and decreases until min. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The if structure checks if a tile is there.
		The for loop holds on which side beside the tile to check for tiles.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | x             | The index of the tile to be checked. Starts as the current index and increases until max. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The for loop holds how far up to check tiles so that all tiles are checked.
		     ___________________________________________________________________________________________________________________________________
		    | Type                  | Name          | Description                                                                               |
		    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
		    | int                   | y             | The index of the tile above to check. Starts as the current index and increases until max.|
		    |_______________________|_______________|___________________________________________________________________________________________|
			The if structure checks upper-left.
			The if structure checks lower-left.

	    Otherwise, if the S key is pressed, then the board is checked.
		The for loop checks if the file is directly to the bottom.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | y             | The index of the tile to be checked. Starts as the current index and increases until max. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The if structure checks if a tile is there.
		The for loop holds on which side beside the tile to check for tiles.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | y             | The index of the tile to be checked. Starts as the current index and increases until max. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The for loop holds how far up to check tiles so that all tiles are checked.
		     ___________________________________________________________________________________________________________________________________
		    | Type                  | Name          | Description                                                                               |
		    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
		    | int                   | x             | The index of the tile above to check. Starts as the current index and decreases until min.|
		    |_______________________|_______________|___________________________________________________________________________________________|
			The if structure checks left-down.
			The if structure checks right-down.

	    Otherwise, if the W key is pressed, then a tile above is checked for.
		The for loop checks if the file is directly to the bottom.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | y             | The index of the tile to be checked. Starts as the current index and decreases until min. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The if structure checks if a tile is there.
		The for loop holds on which side beside the tile to check for tiles.
		 ___________________________________________________________________________________________________________________________________
		| Type                  | Name          | Description                                                                               |
		|-----------------------|---------------|-------------------------------------------------------------------------------------------|
		| int                   | y             | The index of the tile to be checked. Starts as the current index and decreases until min. |
		|_______________________|_______________|___________________________________________________________________________________________|
		    The for loop holds how far up to check tiles so that all tiles are checked.
		     ___________________________________________________________________________________________________________________________________
		    | Type                  | Name          | Description                                                                               |
		    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
		    | int                   | x             | The index of the tile above to check. Starts as the current index and increases until max.|
		    |_______________________|_______________|___________________________________________________________________________________________|
			The if structure checks left-upper.
			The if structure checks right-upper.

	    Otherwise, if the user pressed the enter key, then it checks if the tile can be selected.
		It checks if the user is trying to select the tile.
		It checks if the user is trying to make a match.
		    It checks if a match is possible to be made
			If a match is made and checks if a multiplier could be applied.
			    The for loop clears the tile that the cursor is on.
			     ___________________________________________________________________________________________________________________________________
			    | Type                  | Name          | Description                                                                               |
			    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
			    | int                   | z             | The index of the layer to check for a tile. Starts at the top and decreases by 1 until min|
			    |_______________________|_______________|___________________________________________________________________________________________|
			    The for loop chlears the tile that the selection is on.
			     ___________________________________________________________________________________________________________________________________
			    | Type                  | Name          | Description                                                                               |
			    |-----------------------|---------------|-------------------------------------------------------------------------------------------|
			    | int                   | z             | The index of the layer to check for a tile. Starts at the top and decreases by 1 until min|
			    |_______________________|_______________|___________________________________________________________________________________________|
			The cursor is then changed to selected if nothing else.

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| char                  | key           | The key that the user pressed.                                                            |
	|_______________________|_______________|___________________________________________________________________________________________|

    */
    private boolean processKey ()
    {
	while (true)
	{
	    char key = c.getChar ();    // Input

	    if (key == '\b' && JOptionPane.showConfirmDialog (null, "Are you sure you want to quit your current game?", "Confirm Quit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	    {
		quit = true;
		return false;
	    }
	    else if ((key == 'd' || key == 'D') && cur [0] < tiles.length)
	    {
		// Searching for tile directly to the right.
		for (int x = cur [0] + 1 ; x < tiles.length ; x++)
		{
		    if (tiles [x] [cur [1]] [0] != -1)
		    {
			cur [0] = x;
			return false;
		    }
		}
		// Searching for tile indirectly right.
		for (int x = cur [0] + 1 ; x < tiles.length ; x++)
		{
		    for (int y = 1 ; y < Math.max (cur [1], tiles [0].length - cur [1] - 1) ; y++)
		    {
			if (cur [1] - y >= 0 && tiles [x] [cur [1] - y] [0] != -1)
			{
			    cur [0] = x;
			    cur [1] -= y;
			    return false;
			}
			if (cur [1] + y < tiles [0].length && tiles [x] [cur [1] + y] [0] != -1)
			{
			    cur [0] = x;
			    cur [1] += y;
			    return false;
			}
		    }
		}
	    }
	    else if ((key == 'a' || key == 'A') && cur [0] > 0)
	    {
		// Searching for tile directly to the left.
		for (int x = cur [0] - 1 ; x >= 0 ; x--)
		{
		    if (tiles [x] [cur [1]] [0] != -1)
		    {
			cur [0] = x;
			return false;
		    }
		}
		// Searching for tile indirectly left.
		for (int x = cur [0] - 1 ; x >= 0 ; x--)
		{
		    for (int y = 1 ; y < Math.max (cur [1], tiles [0].length - cur [1] - 1) ; y++)
		    {
			if (cur [1] - y >= 0 && tiles [x] [cur [1] - y] [0] != -1)
			{
			    cur [0] = x;
			    cur [1] -= y;
			    return false;
			}
			if (cur [1] + y < tiles [0].length && tiles [x] [cur [1] + y] [0] != -1)
			{
			    cur [0] = x;
			    cur [1] += y;
			    return false;
			}
		    }
		}
	    }
	    else if ((key == 's' || key == 'S') && cur [1] < tiles [0].length)
	    {
		// Searching for a tile directly below.
		for (int y = cur [1] + 1 ; y < tiles [0].length ; y++)
		{
		    if (tiles [cur [0]] [y] [0] != -1)
		    {
			cur [1] = y;
			return false;
		    }
		}
		// Searching for tile indirectly below.
		for (int y = cur [1] + 1 ; y < tiles [0].length ; y++)
		{
		    for (int x = 1 ; x < Math.max (cur [0], tiles.length - cur [0] - 1) ; x++)
		    {
			if (cur [0] - x >= 0 && tiles [cur [0] - x] [y] [0] != -1)
			{
			    cur [0] -= x;
			    cur [1] = y;
			    return false;
			}
			if (cur [0] + x < tiles.length && tiles [cur [0] + x] [y] [0] != -1)
			{
			    cur [0] += x;
			    cur [1] = y;
			    return false;
			}
		    }
		}
	    }
	    else if ((key == 'w' || key == 'W') && cur [1] > 0)
	    {
		// Searching for a tile directly above
		for (int y = cur [1] - 1 ; y >= 0 ; y--)
		{
		    if (tiles [cur [0]] [y] [0] != -1)
		    {
			cur [1] = y;
			return false;
		    }
		}
		// Searching for tile indirectly above.
		for (int y = cur [1] - 1 ; y >= 0 ; y--)
		{
		    for (int x = 1 ; x < Math.max (cur [0], tiles.length - cur [0] - 1) ; x++)
		    {
			if (cur [0] - x >= 0 && tiles [cur [0] - x] [y] [0] != -1)
			{
			    cur [0] -= x;
			    cur [1] = y;
			    return false;
			}
			if (cur [0] + x < tiles.length && tiles [cur [0] + x] [y] [0] != -1)
			{
			    cur [0] += x;
			    cur [1] = y;
			    return false;
			}
		    }
		}
	    }
	    else if (key == '\n')
	    {
		if (sel [0] == -1 && free (cur [0], cur [1]))
		{
		    sel [0] = cur [0];
		    sel [1] = cur [1];
		    return false;
		}
		else if (sel [0] == cur [0] && sel [1] == cur [1])
		{
		    sel [0] = -1;
		    return false;
		}
		else if (free (cur [0], cur [1]) && free (sel [0], sel [1]) && (getCursorPat () == getSelectedPat () || ("" + getCursorPat ()).matches ("26|27|39|40") && ("" + getSelectedPat ()).matches ("26|27|39|40") || ("" + getCursorPat ()).matches ("28|29|41|42") && ("" + getSelectedPat ()).matches ("28|29|41|42")))
		{
		    int currentTile = getCursorPat ();
		    if (currentTile == prevTile || ("" + prevTile).matches ("2[6-9]|39|4[0-2]") && ("" + currentTile).matches ("2[6-9]|39|4[0-2]"))
		    {
			multiplier += (("" + prevTile).matches ("2[6-9]|39|4[0-2]")) ? 2:
			1;
			if (multiplier > 4) // Limit
			{
			    multiplier = 4;
			}
		    }
		    else
		    {
			multiplier = 0;
		    }
		    prevTile = currentTile;
		    score += (getCursorPat () + getSelectedPat ()) * Math.pow (2, multiplier);

		    // Changing the tile to non-existent
		    for (int z = tiles [0] [0].length - 1 ; z >= 0 ; z--)
		    {
			if (tiles [cur [0]] [cur [1]] [z] != -1)
			{
			    tiles [cur [0]] [cur [1]] [z] = -1;
			    break;
			}
		    }

		    // Changing the other tile to non-existent
		    for (int z = tiles [0] [0].length - 1 ; z >= 0 ; z--)
		    {
			if (tiles [sel [0]] [sel [1]] [z] != -1)
			{
			    tiles [sel [0]] [sel [1]] [z] = -1;
			    break;
			}
		    }

		    sel [0] = -1;
		    return true;
		}
		else
		{
		    if (free (cur [0], cur [1]))
		    {
			sel [0] = cur [0];
			sel [1] = cur [1];
			return false;
		    }
		}
	    }
	}
    }


    /*  The constructor passes the Console reference to this file. It then loads the pictures needed for the game.

	 ___________________________________________________________________________________________________
	| Type      | Name  | Purpose                                                                       |
	|-----------|-------|-------------------------------------------------------------------------------|
	| Console   | con   | Holds the Console object reference to allow this class to use the same Console|
	|           |       | window.                                                                       |
	|-----------|-------|-------------------------------------------------------------------------------|
	| Color     | BG    | Contains the background colour to draw the scores.                            |
	|-----------|-------|-------------------------------------------------------------------------------|
	| Color     | FG    | Contains the forground colour to draw the score text.                         |
	|___________|_______|_______________________________________________________________________________|
    */
    public Mahjong (Console con, Color BG, Color FG)
    {
	c = con;
	bg = BG;
	fg = FG;
	load ();
    }


    /*  This method implements the game and allows the user to play it.
	The first if structure checks if the pattern has been properly loaded. If not, it returns and displays an error message.
	The for loop allows the generator to try 10 times to generate a file until it tells the user that it is too hard (or impossible) to generate a game on the given board.
	 ___________________________________________________________________________________________________
	| Type      | Name  | Purpose                                                                       |
	|-----------|-------|-------------------------------------------------------------------------------|
	| int       | x     | Holds the number of attempts that the generator has been trying for. Starts   |
	|           |       | at 0 and increases by 1 until 9.                                              |
	|___________|_______|_______________________________________________________________________________|
	    If the generator is on it's last attempt, then it displays to the user that it can't generate it.

	The while loop allows the game to continue until it ends.
	    The first if structure checks if you have made a move or not.
		It then checks if you have finished the game, if you did, then it ends the game and displays your score and the highscores.
		Otherwise, if you lost, then it ends the game.
		Otherwise, it just draws the updated board.
	    If you didn't make a move, then it checks if you've quit and draws the board.
		If you chose to quit the game, then it closes the game.

    */
    public boolean play ()
    {
	if (getPattern () == false)
	{
	    JOptionPane.showMessageDialog (null, "The board pattern (file) cannot be properly loaded. \nThis could mean that the file: \n - Doesn't exist\n - Is corrupt\n - Is in the wrong format, or\n - The pattern has an odd number of tiles. ", "Bad Board File", JOptionPane.PLAIN_MESSAGE);
	    return false;
	}
	for (int x = 0 ; x < 10 && !generate () ; x++)
	{
	    if (x == 9)
	    {
		JOptionPane.showMessageDialog (null, "It may be impossible to generate a pattern with this board and the algorithm of this generator.", "Bad Board", JOptionPane.PLAIN_MESSAGE);
		return false;
	    }
	}
	reset ();
	t.start ();

	draw (true);
	while (true)
	{

	    if (processKey ())
	    {
		if (numFree (tiles) == 0)
		{
		    t.stopCounter ();
		    JOptionPane.showMessageDialog (null, "You won the game! You got a score of " + score + " + " + getTimeBonus () + " time bonus = " + (score + getTimeBonus ()) + " points!", "You Won!", JOptionPane.PLAIN_MESSAGE);
		    score += getTimeBonus ();
		    return true;
		}
		else if (lost ())
		{
		    t.stopCounter ();
		    return false;
		}
		else
		{
		    moveCursor ();
		    draw (true);
		}
	    }
	    else
	    {
		if (quit)
		{
		    t.stopCounter ();
		    return false;
		}
		moveCursor ();
		draw (false);
	    }
	}
    }
}


