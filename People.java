import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;

/**
 * Class People - a person in the game.
 *
 * A "person" represents one player of the game. the player has health
 * weight that is gained due to picking items, a position, and an inventory
 * the player can be attacked and if the health of the player reaches 0
 * the player dies.
 *
 * @author Syraj Alkhalil
 * @version 28.11.2021
 */
public class People
{
    private int health;
    private String name;
    private int totalWeight;                        // the weight from the item in the inventory.
    private Room position;                          // current position of player 
    private Room nextPosition;                      // the next position the player is trying to go to.
    private Stack<Room> rooms;                      // all the VISITED rooms
    private ArrayList<Items> inventory;

    /**
     * Create the person and set the health points to 100, and the 
     * total weight to 0 (as the person is not holding anything in the inventory).
     */
    public People(String name, Room position) {
        totalWeight   = 0;
        health        = 100;
        this.name     = name;
        this.position = position;
        rooms         = new Stack<>();
        inventory     = new ArrayList<>();
    }

    /**
     * set the name of the player/character
     * @param newName the new name of the character or player
     */
    public void changeName(String newName) {
        name = newName;
    }

    /**
     * checks if a player or a character is dead
     * if they are then we check the inventory,
     * if they have anything in the inventory then they drop
     * the items in the inventory.
     *
     * @return if the player is dead or not
     */
    public boolean isDead() {
        if(health <= 0){
            System.out.println("you died!");
            if(inventory.size() != 0){
                System.out.println("you dropped all of those...");
                inventory();
                
                // we drop the items so that the player can actually pick them 
                Iterator<Items> it = inventory.iterator();
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
     * reduces the health of the player by 20hp
     */
    public void attacked() {
        health -= 20;
    }

    /**
     * @return the health of the player as an int
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return ArrayList of all the items. aka the inventory of the player or character
     */
    public ArrayList<Items> getInventory() {
        return inventory;
    }

    /**
     * set the health of the player or character to a specific value.
     * @param health the new health of the character or player.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * attack a certain character
     * and check if they are dead, if they are then change
     * the name of the character to "dead " + the character name
     * @param person the person we wish to attack.
     */
    public void attack(People person) {
        person.attacked();
        if(person.isDead()){
            // \this shit doesn't work now bruh
            if (!person.getName().startsWith("dead")){
                System.out.println("--------------------------");
                System.out.println(person.getName() + " is dead");
                person.changeName("dead " + person.getName());
                System.out.println("from now on they will be called : " + person.getName());
            }else{
                System.out.println("I don't know about you.. but where I am from we don't kill the dead... >:( ");
            }

        }else{
            if(person instanceof Mother){
                return;
            }
            System.out.println(person.getName() + " has been attacked the health now: " + person.getHealth()+"/100");
        }
    }

    /**
     * the main talk method... each character has their own
     * way of speech that will be specified in their own class.
     */
    public void talk() {
        if(this.getHealth() <= 0){
            System.out.println("you can't talk to dead people...");
            return;
        }
        System.out.println("you approach the " + getName());
    }

    /**
     * @return the name of the player/character
     */
    public String getName() {
        return name;
    }
    
    /**
     * Change the current position of the player.
     * @param  newPosition  the position we wish to go to.
     */
    public void changePosition(Room newPosition) {
        position = newPosition;
    }

    /**
     * clear the entire stack of visited rooms.
     * this is useful when we teleport to other places.
     */
    public void clearStack() {
        rooms.clear();
    }
    
    /**
     * Change the destination the player wants to go to.
     * @param  newNextPosition  the position we wish to go to.
     */
    public void changeNextPosition(Room newNextPosition) {
        nextPosition = newNextPosition;
    }
    
    /**
     * Push the newly visited room onto the stack.
     * @param  room  the position we just visited
     */
    public void addRoom(Room room) {
        rooms.push(room);
    }

    /**
     * Add an item to the inventory and increase the weight of the player
     * @param  item  the item we wish to add to the inventory
     */
    public void pickItem(Items item) {
        inventory.add(item);
        totalWeight += item.getWeight();
    }

    /**
     * the player is never really following anyone.
     * however, the bandits might.
     * I am not sure if this is the best implementation but this is
     * what I managed to come up with, because I don't 100% understand
     * inheritance
     *
     * @return always false
     */
    public boolean getIsFollowing() {
        return false;
    }

    /**
     * this method should be used from the Enemy class.
     * the same explanation for getIsFollowing().
     * @param following a boolean value. true for this person is following someone.
     */
    public void following(boolean following){}
    
    /**
     * Remove an item to the inventory and decrease the weight of the player
     * @param  itemToRemove  the item we wish to remove from the inventory
     */
    public void dropItem(String itemToRemove) {
        for(Items item : inventory){
            if(itemToRemove.equals(item.getItemName())){
                inventory.remove(item);
                totalWeight -= item.getWeight();
                break;
            }
        }
    }

    /**
     * check if an item exists in the inventory and return true or false.
     * @param type a string that is the name of a given item.
     * @return boolean. either true if we have the item or false if we don't
     */
    public boolean itemType(String type) {
        for(Items item : inventory){
            if(item.getDescription().equals(type)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return the current position of the player 
     */
    public Room getPosition() {
        return position;
    }
    
    /**
     * @return the position the player wants to go to. 
     */
    public Room getNextPosition() {
        return nextPosition;
    }
    
    /**
     * @return the last visited position.
     */
    public Room getLastVisitedPosition() {
        return rooms.pop();
    }

    /**
     * @return the last visited position.
     */
    public Room peekVisitedPosition() {
        return rooms.peek();
    }
    
    /**
     * @return ALL the visited positions in the game.
     */
    public Stack<Room> getAllVisitedRooms() {
        return rooms;
    }
    
    /**
     * @return the totalWeight of the player
     * reminder that the totalWeight refers to the weight gained from carrying items.
     */
    public int getTotalWeight() {
        return totalWeight;
    }
    
    /**
     * @return the number of items that the player has.
     */
    public int numberOfItems() {
        return inventory.size();
    }
    
    /**
     * @return if the player has a certain item.
     */
    public boolean hasItem(String item) {
        for(Items oneItem : inventory){
            if(item.equals(oneItem.getItemName())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return the item that is to be dropped.
     */
    public Items getItem(String itemToBeGiven) {
        for(Items item : inventory){
            if(itemToBeGiven.equals(item.getItemName())){
                return item;
            }
        }
        return null;
    }
    
    /**
     * print all the items the player has
     */
    public void inventory() {
        if(inventory.size() == 0){
            System.out.println("You don't have any items in the inventory yet... try to pick some using the command pick!");
        }else{
            for(Items item : inventory){
                System.out.println(item.getItemName() + ": " + item.getDescription());
            }
        }
    }
}