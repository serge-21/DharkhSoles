import java.util.Iterator;

/**
 * this class is responsible for the behaviour of NPC Joe.
 *
 * @author Syraj Alkhalil
 * @version 28.11.2021
 */
public class NPCJoe extends People
{
    /**
     * making an NPCJoe object is the same as
     * making a People object.
     */
    public NPCJoe(String name, Room position) {
       super(name, position);
    }

    /**
     * making joe "talk" to the player
     * nothing fancy just a bunch of print statements.
     */
    public void talk() {
        super.talk();
        if(getHealth() > 0){
            System.out.println("I am just a friendly character please don't hurt me nobel knight :)");
        }
    }

    /**
     * joe is a weak character therefore if he gets attacked
     * he loses 80hp points.
     */
    public void attacked() {
        setHealth(getHealth() - 80);
    }

    /**
     * check if joe is dead or not. if he is then
     * check if he has any items. (maybe the player gave him some)
     * if so then the player can loot joe.
     * @return if joe is dead or not.
     */
    public boolean isDead() {
        if(getHealth() <= 0){
            System.out.println("why would you kill joe ?????");
            if(getInventory().size() != 0){
                System.out.println("I mean joe wasn't rich but I guess you can loot him :(");
                inventory();
                
                // we drop the items so that the player can actually pick them 
                Iterator<Items> it = getInventory().iterator();
                while(it.hasNext()){
                    getPosition().addItem(getItem(it.next().getItemName()));
                    it.remove();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return the room at the top of the stack of rooms that joe visited.
     */
    public Room peek() {
        return getAllVisitedRooms().peek();
    }

    /**
     * make joe move randomly.
     * by getting his current position. pick an exit at random.
     * and move joe to the exit chosen.
     */
    public void moveRandomly() {
        this.addRoom(this.getPosition());
        this.getPosition().removePerson(this);
        String randomExist = this.getPosition().pickRandomExist();
        Room randomRoom = this.getPosition().getExit(randomExist);
        this.changePosition(randomRoom);
        this.getPosition().addPerson(this);
    }
}
