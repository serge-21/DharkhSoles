/**
 *  this class is responsible for the mother character in the game
 *  This class extends the class people.
 *  the mother in the game can attack the player IFF the player attacks her first
 *  if the player dies then they lose.
 *
 * @author Syraj Alkhalil
 * @version 28.11.2021
 */
public class Mother extends People
{
    /**
     * making a mother object is the same as
     * making a People object.
     */
    public Mother(String name, Room position) {
        super(name, position);
    }

    /**
     * making the mother "talk" to the player
     * nothing fancy just a bunch of print statements.
     */
    public void talk() {
        System.out.println("You approach your mother");
        System.out.println("help me get the cure I am about to die :(");
        System.out.println("I heard Leonardo Da Vinci is the best in town.. could you please talk to him ? ");
    }

    /**
     * the mother can't be attacked by the player.
     * so this class is being overridden
     */
    public void attacked() {
        System.out.println("you dare attack your own mother? have u no shame?");
        System.out.println("your mother slaps you!"); 
    }
}
