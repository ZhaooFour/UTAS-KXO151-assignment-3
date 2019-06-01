/**
    CelebStalker - You must complete this file
    purpose: KXO151 assignment 3, 2019

    Name and UTAS ID of student 1: Chen Wei, 505625
    Name and UTAS ID of student 2: Jiaqing Wang, 505570
*/


import java.util.Scanner;
import java.util.ArrayList;


public class CelebStalker
{
    // final instance variables
	 private final int MAX_DAZED_TIMES = 2;	//The final int variable to declare the max dazed times
	 
    // non-final instance variables
	 private boolean tracing = false;	//The boolean variable to decide whether turn on the tracing. Now it has been turned off. It isn't related to the game itself.
	 private boolean startGame;	//The boolean variable to decide whether start the game
	 private boolean endSignal = false;	//The boolean variable to decide whether the game goes to the ending part
	 private int operationResult;	//The int variable to deliver the operation result
	 private int dazedRemaining = 0;	//The int variable to store the remaining dazed times
	 private int endingType = 0;	//The int variable to deliver different endings
	 private int shoutToEmptyNums = 0;	//The int variable to store the times of shouting to the empty street
	 private int moveSteps = 0;	//The int variable to store total move steps
	 private String celebrityName = null;	//The String variable to deliver the name of the celebrity
	 CelebrityBot game = new CelebrityBot(tracing);	//The instance of CelebrityBot class. Make it possible to call the non-static functions and variables of CelebrityBot class.
	 ArrayList<Integer> moveList = new ArrayList<Integer>();	//The instance of ArrayList class. Make it possible to call the non-static functions and variables of ArrayList<E>. Here <E> is <Integer> and it can only store int numbers.
	 
	   
	 /*The constructor of this class. Execute explain() method when instantiation*/
	 
    public CelebStalker()
    {
		this.explain();
    }
    
    
	 /*explain() method. Make an introduction of the game and receive the feedback of player before the game.*/
	 
    public void explain()
    {
		System.out.println("Stalk a Celebrity!");
		System.out.println("You have to find and seduce your ideal celebrity now.");
		System.out.println();
		System.out.println("Type the word 'true' or 'false' to make selections.");
		System.out.println();
		System.out.println("Would you like to play Stalk a Celeb?");
		
		Scanner startSelection = new Scanner(System.in);
		startGame = startSelection.nextBoolean();
    }
    
    
	 /*play() method. Organize the procedure of the game.*/
	 
    public void play()
    {
	 	this.gameStart();
	 	this.gameProgress();
		this.gameEnding(endingType);
	 }
	 
	 
	 /*gameStart() method. Start the game and finish game initialization.*/
	 
	 private void gameStart()
	 {
	 	boolean isMale;	//The boolean variable to decide the gender of celebrity
		
		if(startGame)
		{
	 		System.out.println("Do you want to stalk a male celebrity?");
			
			Scanner genderSelector = new Scanner(System.in);
	 		isMale = genderSelector.nextBoolean();
	 	
	 		game.newGame(isMale);
			celebrityName = game.getCelebrity();
		}
		
		else
		{
			this.gameEnding(endingType);
		}
	 }
	
	
	/*gameProgress() method. Organize the game progress.*/
	
	 private int gameProgress()
	 {
	 	final char WALK = 'w';	//The final char variable to declare different operations
		final char SHOUT = 's';	//The final char variable to declare different operations
		final char RESET = 'r';	//The final char variable to declare different operations
		final char QUIT = 'q';	//The final char variable to declare different operations
	 	char operation;	//The char variable to deliver the operation of the game player
	 	int inputTarget;	//The int variable to deliver the operation of the game player
		
	 	while(endSignal != true)
		{
			this.dazedEffect();
			System.out.println("Please choose from (w)alk, (s)hout, (r)eset, or (q)uit:");
			
			Scanner input = new Scanner(System.in);
			operation = (input.nextLine()).charAt(0);
			
			switch(operation)
			{
				case 'w':
				System.out.println("Which street would you like to walk into?");
				inputTarget = input.nextInt();
				operationResult = game.tryWalk(inputTarget);
				this.recordMovement(inputTarget);
				this.walkResultChecker();
				break;
				
				case 's':
				System.out.println("Which street would you like to shout into?");
				inputTarget = input.nextInt();
				this.operationResult = game.tryShout(inputTarget);
				this.shoutResultChecker();
				break;
				
				case 'r':
				this.gameReset();
				break;
				
				case 'q':
				this.gameQuit();
				break;
			}
		}
		
		return endingType;
	 }
	 
	 
	 /*gameEnding(int) method. Receive an int variable and organize different endings of the game.*/
	 
	 private void gameEnding(int endingType)
	 {
	 	boolean restart;	//The boolean variable to decide whether restart the game
		
	 	switch(endingType)
		{
			case 0:
			this.showMoveRecords();
			System.exit(-1);
			break;
			
			case 1:
			System.out.println("You scream 'I love you'! Finally you're seen and you're instantly soulmates.");
			System.out.println();
			break;
			
			case 2:
			System.out.println("Oops, security detail front and centre. This won't end well...");
			System.out.println();
			break;
			
			case 3:
			System.out.println("You scream 'I love you' to a policeman. Enjoy the asylum...");
			System.out.println();
			break;
			
			case 4:
			System.out.println("Quit selected.");
			System.out.println();
			break;
		}
	 	System.out.println("Would you like to play Stalk a Celeb again?");
		
		Scanner restarter = new Scanner(System.in);
		restart = restarter.nextBoolean();
		
		if(restart)
		{
			celebStalkerReset();
			game.reset();
			
			this.play();
		}
		
		else
		{
			this.showMoveRecords();
		}
		
		celebStalkerReset();
	 }
	 
	 
	 /*dazedEffect() method. Organize the effect of the status dazed.*/
	 	 	 
	 private void dazedEffect()
	 {
	 	System.out.println("You are stalking " + celebrityName + ".");
	 	if(dazedRemaining == 0)
			{
				this.showRelatedInfor();
			}
			
			else
			{
				System.out.println("You are still dazed and don't know where you are.");
			}
	 }
	 
	 
	 /*recordMovement(int) method. Receive an int variable and store the streets into the instance variable moveList.*/
	  	 
	 private void recordMovement(int inputNum)
	 {
	 	if(operationResult != game.IMPOSSIBLE)
		{
	 		moveList.add(inputNum);
		}
	 }
	 
	 
	 /*walkResultChecker() method. Process the result of the operation WALK.*/
	 
	 private void walkResultChecker()
	 {
	 	if(operationResult == game.SUCCESS)
		{
			dazedRemaining--;
			if(dazedRemaining < 0)
			{
				dazedRemaining = 0;
			}
			
			System.out.println("Walk successful but you're still alone.");
			System.out.println();
		}
		
		if(operationResult == game.DAZED)
		{
			dazedRemaining--;
			if(dazedRemaining < 0)
			{
				dazedRemaining = 0;
			}
			
			dazedRemaining = MAX_DAZED_TIMES;
			System.out.println("You're dazed and don't know where you are.");
			System.out.println();
		}
		
		if(operationResult == game.IMPOSSIBLE)
		{
			System.out.println("To where? Sorry buddy, that's not possible...");
			System.out.println();
		}
		
		if(operationResult == game.FAILURE)
		{
			dazedRemaining--;
			if(dazedRemaining < 0)
			{
				dazedRemaining = 0;
			}
			
			endingType = 2;
			endSignal = true;
		}
	 }
	 
	 
	 /*shoutResultChecker() method. Process the result of the operation SHOUT.*/
	 
	 private void shoutResultChecker()
	 {
	 	if(operationResult == game.SUCCESS)
		{
			System.out.println("The celebrity has heard your sound. Carry on!");
			System.out.println();
		}
				
		if(operationResult == game.IMPOSSIBLE)
		{
			System.out.println("To where? Sorry buddy, that's not possible...");
			System.out.println();
		}
		
		if(operationResult == game.IN_LOVE)
		{
			endingType = 1;
			endSignal = true;
		}
		
		if(operationResult == game.FAILURE)
		{
			shoutToEmptyNums++;
			if(shoutToEmptyNums == 3)
			{
				endingType = 3;
				endSignal = true;
			}
			
			else
			{
				System.out.println("You scream 'I love you'! But no one is around to hear...");
				System.out.println();
			}
		}
	 }
	 
	 
	 /*gameReset() method. Reset the whole game, including the parts which are declared in both this class and CelebrityBot class.*/
	 	 	
	 private void gameReset()
	 {
		System.out.println("Whole game reset!");
		System.out.println();
		
		this.celebStalkerReset();
		game.reset();
	 }
	 
	 
	 /*gameQuit() method. Quit the game.*/
	 
	 private void gameQuit()
	 {
		endingType = 4;
		endSignal = true;
	 }
	 
	 
	 /*showMoveRecords() method. Show the movement information stored in the instance variable moveList.*/
	 
	 private void showMoveRecords()
	 {
	 	moveSteps = moveList.size();
		if(moveSteps != 0)
		{
			System.out.print(moveSteps + " moves: ");
		}
		
		else
		{
			System.out.print("0 moves.");
		}
		
	 	for(int i = 0; i < moveSteps; i++)
		{
			System.out.print(moveList.get(i));
			
			if(i == moveSteps - 1)
			{
				System.out.println(".");
			}
			
			else
			{
				System.out.print(", ");
			}
		}
	 }
	 
	 
	 /*celebStalkerReset() method. Reset parts which are declared only in this class.*/
	 private void celebStalkerReset()
	 {
	 	endSignal = false;
		dazedRemaining = 0;
		endingType = 0;
		shoutToEmptyNums = 0;
		moveSteps = 0;
		moveList.clear();
	 }
	 
	 
	 /*showRelatedInfor() method. Show the related information which is useful to the player.*/
	 
	 private void showRelatedInfor()
	 {
	 	String shoutNums = null;	//The String variable to deliver the progress of the game goal
	 	int lStreet, aStreet, rStreet, bStreet;	//The int variables to store the connected four streets
	 	int closeness;	//The int variable to deliver accessibility of celebrity
		
	 	System.out.println("You are in street #" + game.getCurrentStreet() + ".");	
		
		lStreet = game.nextStreet('l');
		aStreet = game.nextStreet('a');
		rStreet = game.nextStreet('r');
		bStreet = game.nextStreet('b');
		System.out.println("To your left is #" + lStreet + ", to your ahead is #" + aStreet + ", to your right is #" + rStreet + ", to your behind is #" + bStreet + ".");
		
		shoutNums = game.getShoutCount();
		System.out.println("You have declared your love " + shoutNums + " times and shouted in empty streets " + shoutToEmptyNums + " times.");
		
		closeness = game.celebNear();
		if(closeness == game.FAINT)
		{
			System.out.println("You can hear the paparazzi.");
		}
	 }
	   
	 
	 /*setTracing(boolean) method. Receive a boolean variable and deliver it to the boolean variable tracing. This method isn't related to the game itself.*/
	 
    public void setTracing(boolean onOff)
    {
	 	tracing = onOff;
    }
    
    
	 /*trace(String) method. Receive a String variable and decide whether show it as a tracing message or hide it. This method isn't related to the game itself.*/
    public void trace(String message)
    {
	 	if(tracing)
		{
			System.out.println("CelebStalker: " + message);
		}
    }
}
