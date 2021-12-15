import java.util.Iterator;

/**
 * this class is responsible for all the enemies in the game
 * This class extends the class people.
 * the enemy in the game can attack the player and therefore kill the player
 * if the player dies then they lose.
 *
 * @author Syraj Alkhalil
 * @version 28.11.2021
 */
public class Enemy extends People
{
    private boolean isFollowing;        // is the enemy following the player

    /**
     * make the enemy object. the exact same as a People object,
     * but it has one extra field that determines if the enemy is following the player.
     */
    public Enemy(String name, Room position) {
        super(name, position);
        isFollowing = false;
    }

    /**
     * makes the enemy talk. nothing fancy... a print statement
     */
    public void talk() {
        super.talk();
        if (getHealth() > 0) {
            System.out.println("knights... what do u want ? just leave me alone... I didn't steal anything I swear");
        }
    }

    /**
     * if the player attacks the enemy then subtract 50hp points from the enemy.
     * the player has the advantage here.
     * the player can damage the enemy more than the enemy can damage the player.
     */
    public void attacked() {
        setHealth(getHealth() - 50);
    }

    /**
     * check if the enemy is dead if they are then we make them
     * drop everything they have in the inventory as loot.
     * @return if the enemy is dead or not as a boolean value
     */
    public boolean isDead() {
        if(getHealth() <= 0){
            if(getName().startsWith("dead")){
                return true;
            }
            if(getInventory().size() == 0){
                System.out.println("Ahhh too bad this person didn't drop any items fot you to loot");
            }else{
                System.out.println("Ohhh this person dropped some items for you to loot!!");
                System.out.println();
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
     * change the position of the enemy to match the
     * position of the person we want to follow at the
     * start of every turn
     * @param person the person we want to follow
     */
    public void follow(People person) {
        this.getPosition().removePerson(this);
        this.changePosition(person.getPosition());
        this.getPosition().addPerson(this);
    }

    /**
     * @return if this object is following anyone as a boolean value
     */
    public boolean getIsFollowing() {
        return isFollowing;
    }

    /**
     * change the fact that this object is following someone or not.
     * @param following a boolean value. true if this person is following someone.
     */
    public void following(boolean following) {
        isFollowing = following;
    }

    /**
     * attack a person and output their health.
     * @param person the person we wish to attack.
     */
    public void attack(People person) {
        person.attacked();
        System.out.println(getName() + " attacked you! your health now: " + person.getHealth()+"/100");
    }
}
