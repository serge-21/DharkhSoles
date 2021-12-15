import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class Room - a room in the game.
 *
 * This class is part of the "Dharkh Soles" application.
 * "Dharkh Soles" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room. Each room may or may not have
 * a door.
 * 
 * @author  Michael Kölling, David J. Barnes, and Syraj Alkhalil
 * @version 28.11.2021
 */

public class Room
{
    private String roomName;                        // the room name is not the description of the room
    private Boolean hasDoor;                        // does the room have a door?
    private String description;
    private ArrayList<Items> items;
    private ArrayList<People> people;               // people in the room.
    private ArrayList<String> existsForJoe;
    private HashMap<String, Room> exits;            // Store exits of this room.

    /**
     * Create a room described "description". Initially, it has
     * no exits. "roomName" is something like "a kitchen" or "pub"
     * "description" is something like "an open court yard".
     * hasDoor has to be Boolean to distinguish broken doors
     * from rooms with no doors.
     * @param roomName The room's name.
     * @param description The room's description.
     */
    public Room(String roomName, String description) {
        this.roomName = roomName;
        this.description = description;
        hasDoor = null;
        exits = new HashMap<>();
        items = new ArrayList<>();
        people = new ArrayList<>();
        existsForJoe = new ArrayList<>();
    }

    /**
     * Create a room described "description". Initially, it has
     * no exits. "hasDoor" is a boolean. The room either has a door or not.
     * @param roomName The room's name.
     * @param description The room's description.
     * @param hasDoor Boolean either true or false.
     */
    public Room(String roomName, String description, boolean hasDoor) {
        this(roomName, description);    // call the main constructor
        this.hasDoor = hasDoor;
    }

    /**
     * check how many people exist in a room and
     * gives a string o all their names.
     * @return the list of the people in a given room.
     */
    public String peopleInRoom() {
        String listOfPeople = "";
        for(People person : people){
            listOfPeople += person.getName() + "  ";
        }
        return listOfPeople;
    }

    /**
     * @return the total number of the people in a given room as an int.
     */
    public int numberOfPeopleInRoom() {
        return people.size();
    }

    /**
     * @return the items in the room as a string.
     */
    public String itemsInRoom() {
        String listOfItems = "";
        for(Items item : items){
            listOfItems += item.getItemName() + "  ";
        }
        return listOfItems;
    }

    /**
     * @return if the room has a given item as a boolean.
     */
    public boolean hasItem(String isItem) {
        for(Items item : items){
            if(isItem.equals(item.getItemName())){
                return true;
            }
        }
        return false;
    }

    /**
     * @return the item to be picked up by the player as an item
     */
    public Items getItem(String itemToBeGiven) {
        for(Items item : items){
            if(itemToBeGiven.equals(item.getItemName())){
                return item;
            }
        }
        return null;
    }

    /**
     * @return the name of the room
     */
    public String roomName() {
        return roomName;
    }

    /**
     * @return if this room has a door as a boolean.
     */
    public Boolean hasDoor() {
        return hasDoor.booleanValue();
    }
    
    /**
     * return the field hasDoor
     */
    public Boolean getHasDoor(){
        return hasDoor;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription() {
        if(items.size() == 0){
            if(numberOfPeopleInRoom() == 0){
                return "You are " + description + "\n" + getExitString();
            }else{
                if(this.hasPerson("bandit")){
                    if(this.getPerson("bandit").getIsFollowing()){
                        return "You are " + description + "\nThe people in the area: " + peopleInRoom()
                                + "\nThe bandit is following you... better keep attacking..."
                                +"\n" + getExitString();
                    }
                    return "You are " + description + "\nThe people in the area: " + peopleInRoom()
                            + "\nDon't worry... the bandit doesn't see you yet... try to attack them first.."
                            + "\n" + getExitString();
                }
                return "You are " + description + "\nThe people in the area: " + peopleInRoom() + "\n" + getExitString();
            }
        }

        if(numberOfPeopleInRoom() == 0){
            return "You are " + description + "\nItems in the area: " + itemsInRoom()
                    + "\n" + getExitString();
        }else{
            if(this.hasPerson("bandit")){
                if(this.getPerson("bandit").getIsFollowing()){
                    return "You are " + description + "\nItems in the area: " + itemsInRoom() +
                            "\nThe people in the area: " + peopleInRoom()
                            + "\nThe bandit is following you... better keep attacking..."
                            +"\n" + getExitString();
                }
                return "You are " + description + "\nItems in the area: " + itemsInRoom() +
                        "\nThe people in the area: " + peopleInRoom()
                        + "\nDon't worry... the bandit doesn't see you yet... try to attack them first.."
                        + "\n" + getExitString();
            }
            return "You are " + description + "\nItems in the area: " + itemsInRoom() +
                    "\nThe people in the area: " + peopleInRoom() + "\n" + getExitString();
        }
    }

    /**
     * @return the number of items in a room
     */
    public int numberOfItems() {
        return items.size();
    }

    /**
     * Add an item to the room.
     * @param item the item we want to add to the room
     */
    public void addItem(Items item) {
        items.add(item);
    }

    /**
     * remove an item from the room.
     * @param itemToRemove the item we want to remove from the room
     */
    public void removeItem(String itemToRemove) {
        for(Items item : items){
            if(itemToRemove.equals(item.getItemName())){
                items.remove(item);
                break;
            }
        }
    }

    /**
     * add a person to the room.
     * @param person the person we wish to add to the room
     */
    public void addPerson(People person) {
        people.add(person);
    }

    /**
     * remove a person from the room
     * @param person the person we wish to remove from the room
     */
    public void removePerson(People person) {
        people.remove(person);
    }

    /**
     * @return if the room has a given person as a boolean
     */
    public boolean hasPerson(String isPerson) {
        for(People person : people){
            if(isPerson.equals(person.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     * @return the person that is in the room.
     */
    public People getPerson(String personToBeFound) {
        for(People person : people){
            if(personToBeFound.equals(person.getName())){
                return person;
            }
        }
        return null;
    }
    
    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString() {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    /**
     * changes the state of a door from open to shut and vice versa
     * @param state of door, open = 1 , close = 0.
     */
    public void changeStateOfDoor(boolean state) {
        hasDoor = state;
    }

    /**
     * adds all the possible exits for a given room to Joe so that he can pick a random exit
     * and go in that direction
     * @param exist the exit we want to add for joe
     */
    public void addAnExistForJoe(String exist) {
        existsForJoe.add(exist);
    }

    /**
     * pick a random exit by generating a random number.
     * @return the exit as a string.
     */
    public String pickRandomExist() {
        Random index = new Random();
        return existsForJoe.get(index.nextInt(existsForJoe.size()));
    }
}