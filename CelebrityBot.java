/**
    CelebrityBot class - This file MUST NOT be changed
    KXO151 Programming & Problem Solving
    purpose:resources for assignment 3, 2019

    Name and UTAS ID of student 1:
    Name and UTAS ID of student 2:
*/


import java.util.Random;

public class CelebrityBot
{
    // final instance variables
    public final int IN_LOVE = 1;  // celebrity falls in love
    public final int SUCCESS = 0;  // operation successful
    public final int IMPOSSIBLE = -1; // operation unsuccessful because it was not allowed
    public final int DAZED = -2;  // player is dazed
    public final int FAILURE = -3;  // player encounters security
    public final int DISTANT=0;   // celebrity is neither here nor an adjacent street
    public final int FAINT=5;   // celebrity is in an adjacent street
    
    private final String[][] CELEBRITIES={{"Ryan Gosling","Tom Cruise","Henry Cavill","Johnny Depp","Leonardo DiCaprio",
        "Christoph Waltz","James Franco","Robert Downey Jr.","Bradley Cooper","Chris Hemsworth"},
        {"Jennifer Lawrence","Emma Watson","Emilia Clarke","Lena Headey","Stephanie Leonidas",
            "Olga Kurylenko","Julie Benz","Rebel Wilson","Anna Kendrick","Emma Stone"}};
    private final int MALE=0;  // index into above array for male celebrities
    private final int FEMALE=1;  // ditto but for female celebrities
    
    private final int MAX_DAZED_MOVES=2;  // turns to take when dazed
    private final int MAX_SHOUTING_ATTEMPTS=3; // turns to take to seduce celebrity
    
    private int numStreets=9;     // number of streets in the vicinity
    private int[] right={1,2,0,4,5,3,7,8,6}; // streets to right of current location (index)
    private int[] left={2,0,1,5,3,4,8,6,7};  // streets to left of current location (index)
    private int[] ahead={6,7,8,0,1,2,3,4,5}; // streets to front of current location (index)
    private int[] behind={3,4,5,6,7,8,0,1,2}; // streets to rear of current location (index)
    
    //non-final instance variables
    private int currentStreet;   // current location of player
    private int celebPos;    // street where the celebrity can be found
    private String celebrity;   // name of celebrity being stalked
    private boolean male;    // gender of celebrity being stalked
    private int dazedRemaining;   // number of turns remaining when dazed
    private int shoutsRemaining;  // number of shouts needed to seduce
    
    private Random generator;    // to use for random placement in streets
    private boolean tracing;   // switch for tracing messages
    
    
    /**
     *  creates a bot for a Stalk a Celeb game
     *  @param traceOnOff whether or not tracing output should be shown
     */
    public CelebrityBot(boolean traceOnOff)
    {
        tracing = traceOnOff;
        trace("CelebrityBot() starts...");
        generator = new Random();
        generator.setSeed(101);
        trace("...CelebrityBot() ends");
    }
    
    
    /**
     *  provide the number of the current street
     *  @return which street number player is within
     */
    public int getCurrentStreet()
    {
        trace("getCurrentStreet() starts... ...and ends with value " + currentStreet);
        return currentStreet;
    }
    
    
    /**
     *  provide the name of the current celebrity
     *  @return which celebrity player is stalking
     */
    public String getCelebrity()
    {
        trace("getCelebrity() starts... ...and ends with value " + celebrity);
        return celebrity;
    }
    
    
    /**
     *  provide the count of celebrity seduction attempts
     *  @return the fraction of love messages received
     */
    public String getShoutCount()
    {
        String count; // formatted total
        
        trace("getShoutCount() starts...");
        count=""+(MAX_SHOUTING_ATTEMPTS-shoutsRemaining)+"/"+MAX_SHOUTING_ATTEMPTS;
        trace("getShoutCount() ...ends with value " + count);
        return count;
    }
    
    
    /**
     *  randomly determines unique location (currentStreet) and celebrity position (celebPos)
     * @param male whether or not the celebrity to be stalked is male
     *  @return the starting location (street)
     */
    public int newGame(boolean isMale)
    {
        int pos; // player's position
        
        trace("newGame() starts...");
        
        // determine celebrity
        male=isMale;
        if (male)
        {
            celebrity=CELEBRITIES[MALE][generator.nextInt(CELEBRITIES[MALE].length)];
        }
        else
        {
            celebrity=CELEBRITIES[FEMALE][generator.nextInt(CELEBRITIES[FEMALE].length)];
        }
        trace("celebrity is " + celebrity);
        
        // determine player's position
        pos = generator.nextInt(numStreets);
        currentStreet = pos;
        trace("player starts at " + currentStreet);
        
        // determine celeb's position
        trace("calling setCelebPosition()");
        setCelebPosition();
        
        // define player's status
        trace("player is not dazed");
        dazedRemaining=0;
        trace("celebrity is not influenced");
        shoutsRemaining=MAX_SHOUTING_ATTEMPTS;
        
        trace("...newGame() ends with value " + currentStreet);
        return currentStreet;
    }
    
    
    /**
     *  randomly determines unique location for celebrity (celebPos)
     *  @return the celebrity location (street)
     */
    private void setCelebPosition()
    {
        int pos; // celebrity position
        
        trace("setCelebPosition() starts...");
        
        pos = generator.nextInt(numStreets);
        while (pos == currentStreet)
        {
            trace("clash detected");
            // avoid clash with current location
            pos = generator.nextInt(numStreets);
        }
        celebPos = pos;
        trace("celebrity position is " + celebPos);
        
        trace("...setCelebPosition() ends");
    }
    
    
    /**
     *  determine if celebrity is nearby
     *  @return status of celebrity location
     *  DAZED: if player is currently dazed
     *  FAINT: if in connected street
     *  DISTANT: if elsewhere
     */
    public int celebNear()
    {
        int closeness; // closeness of celebrity
        
        trace("celebNear() starts...");
        
        if (dazedRemaining != 0)
        {
            // the celebrity is here
            closeness = DAZED;
            trace("player is still dazed (" + dazedRemaining + " moves to go)");
        }
        else
        {
            if ((right[currentStreet ] == celebPos) ||
                (left[currentStreet ] == celebPos) ||
                (ahead[currentStreet ] == celebPos) ||
                (behind[currentStreet] == celebPos))
            {
                // the celebrity is nearby
                closeness = FAINT;
                trace("celebrity is close");
            }
            else
            {
                // the celebrity is elsewhere
                closeness = DISTANT;
                trace("celebrity is distant");
            }
        }
        
        trace("...celebNear() ends with value " + closeness);
        
        return closeness;
    }
    
    
    /**
     *  try to move the player to another street
     *  @param into indicates the street to try to move into
     *  @return status of movement
     *      SUCCESS: move was successful (current position changed)
     *  DAZED: move was successful but player encounted security
     *  FAILURE: move was successful but dazed player encountered security again
     *      IMPOSSIBLE: move was impossible (current position not changed)
     */
    public int tryWalk(int into)
    {
        int result; // outcome of walk attempt
        
        trace("tryWalk() starts...");
        
        if ((right[currentStreet] == into) ||
            (ahead[currentStreet] == into) ||
            (left[currentStreet] == into) ||
            (behind[currentStreet] == into))
        {
            // street connected
            trace("move into street " + into );
            currentStreet = into;
            if (currentStreet != celebPos)
            {
                // celebrity not found
                trace("celebrity not found");
                result = SUCCESS;
                
                if (dazedRemaining != 0)
                {
                    dazedRemaining--;
                }
            }
            else
            {
                // celebrity found
                if (dazedRemaining == 0)
                {
                    // not dazed
                    trace("celebrity encountered");
                    result = DAZED;
                    dazedRemaining = MAX_DAZED_MOVES;
                    setCelebPosition();
                }
                else
                {
                    // already dazed
                    trace("celebrity encountered again");
                    result = FAILURE;
                }
            }
        }
        else
        {
            // street not connected
            trace("move not possible");
            result = IMPOSSIBLE;
        }
        
        trace("...tryWalk() ends with value " + result);
        
        return result;
    }
    
    
    /**
     *  try to shout to celebrity from the current street
     *  @return status of attempt
     *  IN_LOVE: celebrity seduced
     *      SUCCESS: celebrity was present but not seduced yet
     *      IMPOSSIBLE: street not connected
     *  FAILURE: celebrity not present
     */
    public int tryShout(int into)
    {
        int result; // outcome of shout attempt
        int cost; // cost of shout attempt
        
        trace("tryShout() starts...");
        
        if ((right[currentStreet] == into) ||
            (ahead[currentStreet] == into) ||
            (left[currentStreet] == into) ||
            (behind[currentStreet] == into))
        {
            // street connected
            trace("shout into street " + into );
            if (into != celebPos)
            {
                // not at celebrity location
                result = FAILURE;
                trace("celebrity not present");
            }
            else
            {
                // at celebrity location
                shoutsRemaining--;
                if (shoutsRemaining == 0)
                {
                    // last shout needed for seduction
                    trace("celebrity seduced");
                    result = IN_LOVE;
                }
                else
                {
                    // not the last shout
                    result = SUCCESS;
                    trace("celebrity found but not yet seduced");
                }
            }
        }
        else
        {
            // not at valid location
            result = IMPOSSIBLE;
            trace("street not present");
        }
        
        trace("...tryShout() ends with value " + result);
        
        return result;
    }
    
    
    /**
     *  determine number of adjacent street given its direction
     *  @param char indicates the direction required (l - left, r - right, a - ahead, b - behind)
     *  @return number of the street in that direction or IMPOSSIBLE if invalid parameter
     */
    public int nextStreet(char direction)
    {
        final char AHEAD = 'a';  // ahead
        final char BEHIND = 'b'; // behind
        final char RIGHT = 'r';  // right
        final char LEFT = 'l';  // left
        
        int nextIs; // street number of street in indicated direction
        
        trace("nextStreet() starts...");
        
        // examine adjacent streets
        switch (direction)
        {
            case LEFT:  trace("determining number of street to the left");
                        nextIs = left[currentStreet];
                        break;
            case AHEAD: trace("determining number of street ahead");
                        nextIs = ahead[currentStreet];
                        break;
            case RIGHT: trace("determining number of street to the right");
                        nextIs = right[currentStreet];
                        break;
            case BEHIND:trace("determining number of street behind");
                        nextIs = behind[currentStreet];
                        break;
            default:    nextIs = IMPOSSIBLE;
        }
        
        trace("...nextStreet() ends with value for '" + direction + "' of " + nextIs);
        
        return nextIs;
    }
    
    
    /**
     *  reset all game values
     */
    public void reset()
    {
        trace("reset() starts...");
        
        // reset all game values
        trace("resetting all game values");
        newGame(male);
        
        trace("...reset() ends");
    }
    
    
    /**
     *  turn tracing messages on or off (if off it is assumed that debugging is not occurring and so a new (unseeded) RNG is provided
     *  @param onOff indicates the required state of messages (true on, false off)
     */
    public void setTracing(boolean onOff)
    {
        if (! onOff) // not tracing so get an unseeded RNG
        {
            generator=new Random();
        }
        
        tracing = onOff;
    }
    
    
    /*
     *  displays tracing messages
     *  @param message the message to be displayed if instance variable tracing is true
     */
    public void trace(String message)
    {
        if (tracing)
        {
            System.out.println("celebBot: " + message);
        }
    }
}