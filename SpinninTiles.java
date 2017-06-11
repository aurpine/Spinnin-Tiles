/*  Justin Pu
    Spinnin Tiles
    December 22, 2014
    Ms. Dyke

    Description:

    The main purpose of this program is to provide some entertainment in the form of a strategic matching game (Mahjong Solitaire).

    When the program first loads, an animation will run. Then, the main menu pops up and you are given 5 choices:
	1. Instructions     - You can view all the instructions of the program. It shows you how to properly use the program.
			    - It also displays the game 
	2. Select Level     - Choose the difficulty of the game
			    - Play the game. There is a timer at the top of the screen and your current score below it.
			    - If you win, you will be prompted for a username if you didn't enter one already.
			    - View your ranking on the highscores for that level, otherwise, you're taken to the main menu.
	3. Highscores       - Choose a level to view highscores for.
			    - The highscores are displayed.
	4. Exit             - A message from the maker of the game is displayed
			    - The program closes.

    Variable Dictionary
     _______________________________________________________________________________________________________________________
    | Type      | Name      | Description                                                                                   |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Console   | c         | The reference to the output Console. Has a text size of 30 by 100. Also allows access to      |
    |           |           | the built-in methods.                                                                         |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Mahjong   | m         | The reference variable to Mahjong. Allows access to the Mahjong methods and allows access to  |
    |           |           | its methods.                                                                                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | String    | choice    | Holds the value of the menu choice chosen by the user.                                        |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | String    | username  | Holds the username inputted by the user.                                                      |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | String    | HEADER    | Holds the headers of the highscore files.                                                     |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | String[]  | HSFILE    | Holds the filenames of the highscore files.                                                   |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | MAX       | Holds the maximum number of highscores to be displayed and written to the file                |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | int       | level     | Holds the level that the use has chose to play or display high scores for.                    |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | BG        | Holds the background colour.                                                                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | FG        | Holds the foreground colour.                                                                  |
    |-----------|-----------|-----------------------------------------------------------------------------------------------|
    | Color     | FGAC      | Holds the foreground accent colour.                                                           |
    |___________|___________|_______________________________________________________________________________________________|

*/

import hsa.Console;
import java.awt.*;
import java.io.*;
import javax.swing.JOptionPane;

class SpinninTiles
{
    private Console c;
    private Mahjong m;
    private String choice;
    private String username;
    private static final String HEADER = "[HEADER] Spinnin' Tiles highscores file";
    private static final String[] HSFILE = {"Easy.pmh", "Medium.pmh", "Hard.pmh"};
    private static final int MAX = 10;
    private int level;
    private static final Color BG = new Color (191, 223, 255);
    private static final Color FG = new Color (0, 102, 204);
    private static final Color FGAC = new Color (0, 128, 255);

    /*  This method saves the new highscore to the file.

	The try block allows the file to be opened and read. If any Exceptions occur, a new file is written.
	    The if structure checks if the file is made by this program by checking the header.
	    If it isn't then a new file is written.
	    Otherwise, the variables are read.
		The for loop reads all the names and scores by running a specified number of times.
		 _______________________________________________________________________________________________
		| Type  | Name  | Purpose                                                                       |
		|-------|-------|-------------------------------------------------------------------------------|
		| int   | x     | Holds the array index of the names and scores that the current line is of.    |
		|_______|_______|_______________________________________________________________________________|
	If an IOException occurs, a new file is written.
	If a NullPointerException occurs, a new file is written.
	If a NumberFormatException occurs, a new file is written.

	The for loop starts from the last entry in the high scores and makes it way towards the top.
	 _______________________________________________________________________________________________
	| Type  | Name  | Purpose                                                                       |
	|-------|-------|-------------------------------------------------------------------------------|
	| int   | x     | Holds the index of the scores and names array to check whether the new        |
	|       |       | highscore fits there or not.                                                  |
	|_______|_______|_______________________________________________________________________________|
	    The if structure checks if the score (or name if the score is equal) in front of the current index is higher, if it is, then it writes the new highscore.
	    If the current index is not the first, then it moves the score in front back an index to make space for the current player's high score.


	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________
	| Type          | Name      | Description                                                                                   |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| String[]      | names     | Holds the all the usernames in the file and later holds the current user's name.              |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| int[]         | scores    | Holds the all the scores in the file and later holds the recent user's score.                 |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| BufferedReader| in        | The input highscores file to read the old highscores.                                         |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| IOException   | e         | In case the file doesn't exist or is corrupt.                                                 |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| NullPointer-  | e         | In case the line read is blank.                                                               |
	| Exception     |           |                                                                                               |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| NumberFormat- | e         | In case the line that is trying to be parsed into a number contain non-digit characters.      |
	| Exception     |           |                                                                                               |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| PrintWriter   | out       | The output highscores file to write the new, updated highscores.                              |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| IOException   | e         | In case the file can't be written to.                                                         |
	|_______________|___________|_______________________________________________________________________________________________|
    */
    private void saveHighscore (int userScore)
    {
	String[] names = new String [1];
	int[] scores = new int [1];
	// Loading highscores file and names
	try
	{
	    BufferedReader in = new BufferedReader (new FileReader (HSFILE [level]));

	    if (!in.readLine ().equals (HEADER))
	    {
		createNewHighscoresFile ();
	    }
	    else
	    {
		names = new String [Integer.parseInt (in.readLine ()) + 1];
		scores = new int [names.length];

		for (int x = 0 ; x < names.length ; x++)
		{
		    names [x] = in.readLine ();
		    scores [x] = Integer.parseInt (in.readLine ());
		}
	    }
	}
	catch (IOException e)
	{
	    createNewHighscoresFile ();
	}
	catch (NullPointerException e)
	{
	    createNewHighscoresFile ();
	}
	catch (NumberFormatException e)
	{
	    createNewHighscoresFile ();
	}

	// Enters the current user's score into the highscores
	for (int x = names.length - 1 ; x >= 0 ; x--)
	{
	    if (x == 0 || scores [x - 1] > userScore || scores [x - 1] == userScore && names [x - 1].compareTo (username) >= 0)
	    {
		names [x] = username;
		scores [x] = userScore;
		break;
	    }
	    if (x != 0)
	    {
		names [x] = names [x - 1];
		scores [x] = scores [x - 1];
	    }
	}

	// Updating highscores file
	try
	{
	    PrintWriter out = new PrintWriter (new FileWriter (HSFILE [level]));
	    out.println (HEADER);
	    out.println ((names.length > MAX) ? MAX:
	    names.length);
	    for (int x = 0 ; x < names.length && x < MAX ; x++)
	    {
		out.println (names [x]);
		out.println (scores [x]);
	    }
	    out.close ();
	}
	catch (IOException e)
	{
	}
    }


    /*  This method asks the user for the level they wish to display high scores for or the level they want to play the game on.

	The if structure displays the highscore message is the level is for highscores, otherwise, it displays the message for playing the game.
	The while loop is to error trap the input so that it keeps prompting the user for input until they give valid input.
	    The if structure checks if the input provided matches one of the options, if it does, then the level is set and the loop is broken.

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________
	| Type          | Name      | Description                                                                                   |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| String        | temp      | Holds the input entered by the user to check if it is valid input.                            |
	|_______________|___________|_______________________________________________________________________________________________|
    */
    public void askLevel (boolean forHighscores)
    {
	title ();
	if (forHighscores)
	{
	    c.println ("For which level would you like to display high scores for? ");
	}
	else
	{
	    c.println ("On which level would you like to play? ");
	}
	c.println ();
	c.println ("1. Easy");
	c.println ("2. Medium");
	c.println ("3. Hard");
	while (true)
	{
	    c.setCursor (3, (forHighscores) ? 60:
	    40);
	    c.setTextColour (FGAC);
	    String temp = c.readLine ();
	    c.setTextColour (FG);
	    if (temp.matches ("[1-3]"))
	    {
		level = Integer.parseInt (temp) - 1;
		break;
	    }
	    JOptionPane.showMessageDialog (null, "That is not one of the choices!", "Bad File", JOptionPane.WARNING_MESSAGE);
	    c.setCursor (3, (forHighscores) ? 60:
	    40);
	    c.println ();
	}
    }


    /*  This method displays the high scores from the high scores file.

	The try structure allows the highscores file to be opened. It also prevents the program from crashing in case of an exception.
	    The if structure checks if file header is valid and then reads the file and displays it, otherwise, an error message is shown and a new file is created.
		The if structure checks how many highscores are in the file, if there are 0, then it displays that there are no highscores.
		Otherwise, the number of highscores is displayed.
		    The ternary operator checks if the number of highscores is singular and uses one highscore instead of n highscores if it isn't.
	    The for loop goes through all the highscores and displays the highscores and the usernames of who got them.
	An EOFException is caught in case the file ended earlier than expected.
	An IOException is caught in case the file doesn't exist.
	A NumberFormatException is caught in case the line can't be parsed into an integer.

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________
	| Type          | Name      | Description                                                                                   |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| int           | n         | The number of highscores that there are in the file.                                          |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| int           | x         | Holds which highscore index is to be displayed in the loop. Starts at 0 and increases by 1 up |
	|               |           | to the number of highscore files in the file or the maximum number (10).                      |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| IOException   | e         | Catches an exception in case the highscores file cannot be read.                              |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| NumberFormat- | e         | Catches an exception in case there are other symbols where there should be only numbers.      |
	| Exception     |           |                                                                                               |
	|_______________|___________|_______________________________________________________________________________________________|
    */
    public void displayHighscores ()
    {
	title ();
	try
	{
	    BufferedReader in = new BufferedReader (new FileReader (HSFILE [level]));
	    if (in.readLine ().equals (HEADER))
	    {
		int n = Integer.parseInt (in.readLine ());
		if (n == 0)
		{
		    c.println ("There are no highscores in this file!");
		}
		else
		{
		    c.println ("There " + ((n == 1) ? ("is one highscore "):
		    ("are " + n + " highscores ")) + "in this file.");
		}
		c.println ();
		for (int x = 0 ; x < n && x < MAX ; x++)
		{
		    c.print (x + 1, 8);
		    c.print (". " + in.readLine (), 42);
		    c.println (Integer.parseInt (in.readLine ()), 40);
		}
		pauseProgram ();
		return;
	    }
	}
	catch (EOFException e)
	{
	}
	catch (IOException e)
	{
	}
	catch (NumberFormatException e)
	{
	}
	JOptionPane.showMessageDialog (null, "The highscores file \"" + HSFILE [level] + "\" is corrupted or does not exist. A new file will be created.", "Bad File", JOptionPane.WARNING_MESSAGE);
	createNewHighscoresFile ();
	displayHighscores ();
    }


    /*  This method created a new, blank highscores file.
	The try block allows the new file to be written and allows the program to continue without crashing in case the file can't be written.

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________
	| Type          | Name      | Description                                                                                   |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| PrintWriter   | out       | Holds the output file reference.                                                              |
	|---------------|-----------|-----------------------------------------------------------------------------------------------|
	| IOException   | e         | Catches in case the output file can't be written properly.                                    |
	|_______________|___________|_______________________________________________________________________________________________|
    */
    private void createNewHighscoresFile ()
    {
	try
	{
	    PrintWriter out = new PrintWriter (new FileWriter (HSFILE [level]));
	    out.println (HEADER);
	    out.println (0);
	    out.close ();
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (null, "Creating the new file was unsuccessful. ", "Couldn't write", JOptionPane.WARNING_MESSAGE);
	}
    }


    //  This method clears the screen and draws a title
    private void title ()
    {
	c.setTextBackgroundColour (BG);
	c.clear ();
	c.setTextBackgroundColour (FG);
	c.print (' ', 43);
	c.setTextColour (BG);
	c.println ("Spinnin' Tiles");
	c.setTextBackgroundColour (BG);
	c.setTextColour (FG);
	c.println ();
    }


    //  This method temporarily pauses the program until a key is pressed.
    private void pauseProgram ()
    {
	c.setCursor (29, 1);
	c.print (' ', 36);
	c.println ("Press any key to continue...");
	c.getChar ();
    }


    /*  Delays the program execution by an amount given.

	The try structure sleeps the Thread so that the program can be paused. Exception e catches an Exception in case the program cannot be paused.

	Local Variable Dictionary
	 __________________________________________________________________________________________________________________________________
	| Type                 | Name          | Description                                                                               |
	|----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                  | milliseconds  | The amount in milliseconds to pause the program for.                                      |
	|----------------------|---------------|-------------------------------------------------------------------------------------------|
	| InterruptedException | e             | Catches an InterruptedException in case the program can not be paused.                    |
	|______________________|_______________|___________________________________________________________________________________________|
    */
    private void delay (int milliseconds)
    {
	try
	{
	    Thread.sleep (milliseconds);
	}
	catch (InterruptedException e)
	{
	}
    }


    /*  Plays an animation to show the program to the user.

	The for loop draws the filling of the tile slowly from the inside
	 _______________________)___________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | h             | The height of the tile to be drawn. Starts as 0, and increases by 1 until 119.            |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the top border line of the tile from left to right.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | The x coordinate of the pixel to be drawn. Starts as 355 and increases by 1 until 425.    |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the right border line of the tile from top to bottom.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | y             | The y coordinate of the pixel to be drawn. Starts as 235 and increases by 1 until 345.    |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the bottom border line of the tile from right to left.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | The x coordinate of the pixel to be drawn. Starts as 425 and decreases by 1 until 355.    |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the left border line of the tile from the bottom up.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | y             | The x coordinate of the pixel to be drawn. Starts as 345 and decreases by 1 until 235.    |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the maple leaf from outside and fills in.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | h             | The length and height of the maple leaf. Starts as 60 and decreases by 1 until it's 0.    |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the shadow of the tile from outside in.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | h             | Holds the height of the rounded rectangle to be drawn. It starts as 0 and slowly increases|
	|                       |               | by 1 until it is 120.                                                                     |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop moves the whole for loop up from its current position.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | y             | The center y coordinate of the object. Starts as 300 and decreases by 5 until it's -70.   |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop Moves the rotating tile into the screen from the right.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | The x value of the tile so that it can be set. It starts as 850 and decreases by 2 until  |
	|                       |               | it's 150.                                                                                 |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the word letter by letter with a delay of 75 millisecond between each letter.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | It is where the substring of the first line to be drawn. x starts as 0 and increases by   |
	|                       |               | 1 until the length of the line (8).                                                       |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws the second line letter by letter with the same delay.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | It is where the substring of the string to be drawwn ends. x starts as 0 and increases by |
	|                       |               | 1 until it's the length of the line (5).                                                  |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop moves the first rotating tile out of the screen.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | It is the center x coordinate of the tile. x starts as 150 and decreases by 1 until it's  |
	|                       |               | -50.                                                                                      |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop draws a curtain coming down on the screen slowly. The delay increases as it goes.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | y             | The y value of the line to draw. It starts as 0 and increases by 1 until it's 600.        |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop moves the two new tiles into the screen and down or up depending on their random starting value.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | It is the x coordinate of the center of the first tile and the inverse of that of the     |
	|                       |               | second. It starts as -50 and increases by 5 until it's 850.                               |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop slowly raises the curtain up.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | y             | The y value of the line to be drawn. It starts as 599 and decreases by 1 until it's 0.    |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop moves the last rotating tile into the screen.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | The x value of the center coordinate of the rotation tile. It starts as -50 and increases |
	|                       |               | by 1 until it's 599.                                                                      |
	|_______________________|_______________|___________________________________________________________________________________________|

	The for loop that moves the last tile out of the screen.
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| int                   | x             | The x value of the center coordinate of the tile. Starts as 600 and increases until it's  |
	|                       |               | 850.                                                                                      |
	|_______________________|_______________|___________________________________________________________________________________________|

	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| SplashScreen          | s1            | The reference to the first SplashScreen instance. It animates a moving tile.              |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| String                | line1         | Holds the first line of text to be drawn on the screen.                                   |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| String                | line2         | Holds the second line of text to be drawn on the screen.                                  |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| SplashScreen          | s2            | The reference to the second SplashScreen instance. It animates the second moving tile.    |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| SplashScreen          | s3            | The reference to the third SplashScreen instance. It animates a moving tile.              |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| SplashScreen          | s4            | The reference to the fourth SplashScreen instance. It animates a moving tile.             |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    public void splashScreen ()
    {
	// Draws the background colour
	c.setColour (FGAC);
	c.setTextBackgroundColour (BG);
	c.fillRect (0, 0, 800, 600);
	// Draws the inside of the tile
	c.setColour (Color.lightGray);
	for (int h = 0 ; h < 120 ; h++)
	{
	    c.drawRoundRect (390 - h / 3, 290 - h / 2, 2 * h / 3, h, h / 24, h / 24);
	    delay (3);
	}
	c.setColour (Color.black);
	// Draws the top line
	for (int x = 355 ; x <= 425 ; x++)
	{
	    c.drawLine (x, 230, x, 230);
	    delay (3);
	}
	c.drawArc (420, 230, 10, 10, 0, 90);
	// Draws the right line
	for (int y = 235 ; y <= 345 ; y++)
	{
	    c.drawLine (430, y, 430, y);
	    delay (3);
	}
	c.drawArc (420, 340, 10, 10, 270, 90);
	// Draws the bottom line
	for (int x = 425 ; x >= 355 ; x--)
	{
	    c.drawLine (x, 350, x, 350);
	    delay (3);
	}
	// Draws the left line
	c.drawArc (350, 340, 10, 10, 180, 90);
	for (int y = 345 ; y >= 235 ; y--)
	{
	    c.drawLine (350, y, 350, y);
	    delay (3);
	}
	c.drawArc (350, 230, 10, 10, 90, 90);
	// Draws the maple leaf
	c.setColour (Color.red);
	for (int h = 60 ; h >= 0 ; h--)
	{
	    c.drawMapleLeaf (390 - h / 2, 290 - h / 2, h, h);
	    delay (3);
	}
	// Draws the shadow
	for (int h = 0 ; h <= 120 ; h++)
	{
	    c.setColour (Color.black);
	    c.drawRoundRect (370 + h / 3, 250 + h / 2, 80 - h * 2 / 3, 120 - h, 10 - h / 24, 10 - h / 24);
	    // Draws the front back
	    c.setColour (Color.lightGray);
	    c.fillRoundRect (350, 230, 80, 120, 10, 10);
	    c.setColour (Color.black);
	    c.drawRoundRect (350, 230, 80, 120, 10, 10);
	    c.setColour (Color.red);
	    c.fillMapleLeaf (360, 260, 60, 60);
	    delay (3);
	}

	// Moves it up
	for (int y = 300 ; y > -70 ; y -= 8)
	{
	    c.setColour (Color.black);
	    c.fillRoundRect (370, y - 50, 80, 120, 10, 10);
	    c.setColour (Color.lightGray);
	    c.fillRoundRect (350, y - 70, 80, 120, 10, 10);
	    c.setColour (Color.black);
	    c.drawRoundRect (350, y - 70, 80, 120, 10, 10);
	    c.setColour (Color.red);
	    c.fillMapleLeaf (360, y - 40, 60, 60);
	    delay (10);
	    c.setColour (FGAC);
	    c.fillRect (350, y - 70, 101, 141);
	}

	SplashScreen s1 = new SplashScreen (c, 850, 300, FGAC);
	s1.start ();
	for (int x = 850 ; x >= 150 ; x -= 2)
	{
	    s1.setX (x);
	    delay (4);
	}
	c.setFont (new Font ("Comic Sans MS", Font.ITALIC, 150));
	String line1 = "Spinnin'";
	// Draws the word letter by letter
	for (int x = 0 ; x < line1.length () ; x++)
	{
	    c.setColour (Color.black);
	    c.drawString (line1.substring (0, x + 1), 100, 200);
	    delay (75);
	}
	// Draws the second line letter by letter
	String line2 = "Tiles";
	for (int x = 0 ; x < line2.length () ; x++)
	{
	    c.setColour (Color.black);
	    c.drawString (line2.substring (0, x + 1), 300, 400);
	    delay (75);
	}
	delay (1000);
	for (int x = 150 ; x > -50 ; x--)
	{
	    s1.setX (x);
	    delay (5);
	}
	s1.stopDrawing ();

	delay (100);

	// Clears the screen
	c.setColour (FG);
	for (int y = 0 ; y < 600 ; y++)
	{
	    c.drawLine (0, y, 799, y);
	    delay (y / 120);
	}

	int s2Y = (int) (601 * Math.random ());
	int s3Y = (int) (601 * Math.random ());
	SplashScreen s2 = new SplashScreen (c, -50, s2Y, FG);
	SplashScreen s3 = new SplashScreen (c, 850, s3Y, FG);
	s2.start ();
	s3.start ();

	for (int x = -50 ; x <= 850 ; x += 5)
	{
	    s2.setX (x);
	    s2.setY (s2Y + (300 - s2Y) * (x + 50) / 900);
	    s3.setX (800 - x);
	    s3.setY (s3Y + (300 - s3Y) * (x + 50) / 900);
	    delay (10);
	}
	s2.stopDrawing ();
	s3.stopDrawing ();

	delay (100);

	c.setColour (BG);
	for (int y = 599 ; y >= 0 ; y--)
	{
	    c.drawLine (0, y, 799, y);
	    delay ((599 - y) / 120);
	}

	SplashScreen s4 = new SplashScreen (c, -50, 400, BG);
	s4.start ();

	for (int x = -50 ; x < 600 ; x++)
	{
	    s4.setX (x + 1);

	    delay (5);
	}
	askUsername ();
	for (int x = 600 ; x < 850 ; x++)
	{
	    s4.setX (x + 1);
	    delay (7);
	}
	s4.stopDrawing ();
    }


    /*  This method asks the user for their username.

	The while loop error traps the user's input for the username. It keeps on running until it breaks when the user enters a valid username.
	    The if structure checks if the username is valid or not, then it breaks.
    */
    private void askUsername ()
    {
	title ();
	c.println ("What username would you like to use for this session? ");
	while (true)
	{
	    c.setCursor (3, 55);
	    c.setTextColour (FGAC);
	    username = c.readLine ();
	    c.setTextColour (FG);
	    if (username.length () < 40 && username.matches ("\\w*"))
	    {
		break;
	    }
	    JOptionPane.showMessageDialog (null, "Your username has to be alpha-numeric (a-z, A-Z, 0-9 and _) and under 40 characters long.", "Bad username", JOptionPane.WARNING_MESSAGE);
	    c.setCursor (3, 55);
	    c.println ();
	}
    }


    //  The instructions page of the program where the game and how to use the program is explained to the user. The program continues once the user presses a button.
    public void instructions ()
    {
	title ();
	c.println ("This is a Mahjong Solitaire game.");
	c.println ();
	c.println ("The Objective:");
	c.println ("The objective of the game is to clear all the tiles from the board. You do this by matching tiles");
	c.println ("and removing them from the board. ");
	c.println ();
	c.println ("The Rules:");
	c.println ("You may only remove tiles that are \"free\". This means that there must be no tile to the left, the");
	c.println ("right, or on both sides of the tile. Free tiles do not have a tile above them. ");
	c.println ();
	c.println ("Controls:");
	c.println ("The cursor is on the tile that is currently selected. To move the cursor, use the WASD arrow keys.");
	c.println ("Press W to move up, A to move left, S to move down and D to move right. Press the Enter key to");
	c.println ("select the tile that the cursor is on. If the tile is already selected, then it will be deselected.");
	c.println ("When a tile is selected, it will be highlighted in blue. To quit the game, press the backspace key");
	c.println ("anytime. ");
	c.println ();
	c.println ("Matching:");
	c.println ("Any two tiles with the same design on them will match. Additionally, any of the seasons tiles");
	c.println ("(purple) will match with each other and any of the flower tiles (green) will match with each other");
	c.println ();
	c.println ("Scoring:");
	c.println ("Each tile has a value from 1 to 42. When you match two tiles, their combined value is added to your");
	c.println ("score. Furthurmore, if you match the same tile as previously, an exponential multiplier will be");
	c.println ("applied. The seasons and flower tiles will give a two times greater multiplier. ");
	c.println ();
	pauseProgram ();
	title ();
	c.println ("Game Flow:");
	c.println ("When you play the game, you will be prompted to select a level to play on. You must choose one of");
	c.println ("options. If you win, your score will be shown and you will be shown the highscores file. If you");
	c.println ("lose, you will be taken to the main menu. You may also see the highscores from the main menu.");
	c.println ("You may choose to exit from the main menu.");
	pauseProgram ();
    }


    /*  This method runs the game.
	The if structure checks if the user won the game or not and saves and displays highscores if they did win.
    */
    public void play ()
    {
	askLevel (false);
	m.setLevel (level);
	if (m.play ())
	{
	    saveHighscore (m.getScore ());
	    displayHighscores ();
	}
    }


    //  The method gives a brief thank you to the user for playing the game and closes the window.
    public void goodbye ()
    {
	title ();
	c.println ("Thank you for playing my game. ");
	c.println ();
	c.println ("If you want to contact me, Justin Pu, the supreme creator of the game, then email me at");
	c.println ("justinpu@rocketmail.com");
	c.println ();
	c.println ("-Innovative Pu Technology Inc.");
	pauseProgram ();
	c.close ();
    }


    /*  This method displays a list of menu options to the user and asks them to choose one.
	The while loop runs to error trap the user's input on the menu option to select.
	    The if structure checks if the user has entered a valid choice, then it breaks out of the loop.
    */
    public void mainMenu ()
    {
	title ();
	c.println ("1. Instructions");
	c.println ("2. Play (level selection)");
	c.println ("3. High Scores");
	c.println ("4. Exit");
	c.println ();
	c.println ("Choose a menu option: ");
	while (true)
	{
	    c.setCursor (8, 23);
	    c.setTextColour (FGAC);
	    choice = c.readLine ();
	    c.setTextColour (FG);
	    if (choice.matches ("[1-4]"))
	    {
		break;
	    }
	    JOptionPane.showMessageDialog (null, "That is not a choice!", "Invalid Choice", JOptionPane.PLAIN_MESSAGE);
	    c.setCursor (8, 23);
	    c.println ();
	}
    }


    //  This is the constructor. It creates the new Console window and initializes the instance of Mahjong.
    public SpinninTiles ()
    {
	c = new Console (30, 100, "Spinnin' Tiles");   // 100 by 30 screen size
	m = new Mahjong (c, BG, FG);
    }


    /*  The main method controls the program flow.

	The while loop allows the user to use the program until they choose to exit.
	    The if structure selects the method(s) to run for the user depending on the menu option that they select. If the user chooses to quit, it breaks out of the loop.
	
	Local Variable Dictionary
	 ___________________________________________________________________________________________________________________________________
	| Type                  | Name          | Description                                                                               |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| SpinninTiles          | st            | Holds the pointer to the instance of the class and allows access of the class methods.    |
	|-----------------------|---------------|-------------------------------------------------------------------------------------------|
	| String[]              | args          | Holds the java virtual machine run parameters. It is set by the java run settings.        |
	|_______________________|_______________|___________________________________________________________________________________________|
    */
    public static void main (String[] args)
    {
	SpinninTiles st = new SpinninTiles ();

	st.splashScreen ();
	while (true)
	{
	    st.mainMenu ();
	    if (st.choice.equals ("1"))
	    {
		st.instructions ();
	    }
	    else if (st.choice.equals ("2"))
	    {
		st.play ();
	    }
	    else if (st.choice.equals ("3"))
	    {
		st.askLevel (true);
		st.displayHighscores ();
	    }
	    else
	    {
		break;
	    }
	}
	st.goodbye ();

    }
}
