import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * the worldGen class generates the world of the game.
 * it initialises all the players, NPCs, and items.
 *
 * @author Syraj Alkhalil
 * @version 28.11.2021
 */
public class WorldGen
{
    private People player;                // main player
    private Enemy bandit;
    private Mother mother;
    private NPCJoe joe;
    private NPCLeo Leo;

    private ArrayList<Room> rooms;
    private ArrayList<Enemy> enemies;
    private Room outside, house, forest1, forest2, forest3, river, pub, lab, cell, leoHouse, tower;

    private Items mead, sword, table;     // in the pub.
    private Items note, machete;          // with the bandit.
    private Items note2, portalGun;       // in the lab.
    private Items cure;                   // with leo.

    private HashMap<Items, String> notes; // display the correct note description

    /**
     * generate the world.
     * create the rooms, the people, and the items.
     */
    public WorldGen() {
        createRooms();
        rooms   = new ArrayList<>();
        enemies = new ArrayList<>();    // to store all the enemies in the game.
        notes   = new HashMap<>();
        
        populate();                     // add rooms to the ArrayList
        createPeople();
        addExistsForJoe();
        initialiseNotes();
        enemies.add(bandit);
    }

    /**
     * add the notes to the HashMap
     */
    private void initialiseNotes(){
        notes.put(note, "Thank you for capturing Leonardo Da Vinci... \nI really appreciate you placing him in the cell as well!! \nYou can collect your reward soon...");
        notes.put(note2, "I managed to cast a spell of protection over the lab... \nNow nobody can teleport into my lab. \nI have manged to make a portal gun to teleport to places. \nAlso I have managed to make a teleporter.. it is broken though (it teleports me to random places) \n To use the broken teleporter just type teleport \n To use the portal gun type teleport [place] the names of places are on the map :)");
    }

    public String getNote(Items note){
        return notes.get(note);
    }

    public Room getLab(){
        return lab;
    }

    /**
     * This method checks who is attacking the player and returns the enemies attack the player.
     *
     * @return the arrayList of all the enemies currently attacking the player.
     */
    public ArrayList<Enemy> attackingPlayer() {
        ArrayList<Enemy> attackingPlayer = new ArrayList<>();
        
        for(Enemy enemy : enemies){
            if(enemy.getIsFollowing() && enemy.getHealth() > 0){
                attackingPlayer.add(enemy);
            }
        }
        return attackingPlayer;
    }

    /**
     * accessor method that returns joe.
     * @return the NPC joe. NPC joe inherits from People.
     */
    public NPCJoe getJoe() {
        return joe;
    }

    /**
     * accessor method that returns Leo.
     * @return the NPC Leo. NPC Leo inherits from People.
     */
    public NPCLeo getLeo() {
        return Leo;
    }

    /**
     * accessor method that returns the current player.
     * @return the current player.
     */
    public People getPlayer() {
        return player;
    }

    /**
     * create a list of all people in the game.
     * and add them to the rooms and give them the items they need.
     */
    private void createPeople() {
        bandit = new Enemy("bandit", forest1);    // set the starting position of the player to the starting position.
        mother = new Mother("mom", house);
        Leo    = new NPCLeo("leonardo da vinci", cell);
        joe    = new NPCJoe("traveller joe", pub);
        player = new People("you", outside);

        addPeopleToRooms();    // add all the people to the rooms
        givePeopleItems();     // give the people the items they need.
    }

    /**
     * give all the people the items they need.
     */
    private void givePeopleItems() {
        bandit.pickItem(note);
        bandit.pickItem(machete);
        Leo.pickItem(cure);
    }

    /**
     * add the people to the rooms in which they belong.
     */
    private void addPeopleToRooms() {
        bandit.getPosition().addPerson(bandit);
        mother.getPosition().addPerson(mother);
        Leo.getPosition().addPerson(Leo);
        joe.getPosition().addPerson(joe);
        joe.addRoom(joe.getPosition());         // since joe moves around then this is just initialising the stack of
                                                // rooms that joe visits with the first room.
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        // create the rooms
        outside  = new Room("start", "at the start of the game!");
        house    = new Room("your house", "at your house...", true);
        pub      = new Room("the local pub", "in the local pub where they serve the best mead :O", true);
        forest1  = new Room("the start of the forest", "at the start of the forest... it is kinda dim in here. There are trees \ncovering the entire forest from which you can barely see the sun \nTo the east you see insurmountable mountains");
        forest2  = new Room("forest", "in the middle of the forest... right in front of you, you see more and more trees that path definitely won't lead anywhere.");
        forest3  = new Room("the edge of the forest", "at the edge of the forest.. the trees are clearing. You can see a house in front of you.");
        lab      = new Room("leonardo's lab", "in Leonardo's lab... some things might be helpful... \nYou see a giant thing that you don't understand.");
        leoHouse = new Room("leonardo's house", "at Leonardo's house!", true);
        river    = new Room("river", "swimming... the currents are unbelievable");
        tower    = new Room("tower", "inside a tower, you notice a narrow staircase going down...", true);
        cell     = new Room("cell" , "inside the cell", true);

        setExists();                // initialise room exits
        creatItems();               // creat all the items in the entire game
        addItemsToRooms();          // add the items to specific rooms
    }
    
    /**
     * set all the exists for all the rooms
     */
    private void setExists() {
        // outside exists...
        outside.setExit("east", house);
        outside.setExit("south", forest1);
        outside.setExit("west", pub);
        
        // pub exists...
        pub.setExit("east", outside);
        
        // house exists...
        house.setExit("west", outside);
        
        // forest exists...
        forest1.setExit("south", forest2);
        forest1.setExit("west" , forest3);
        forest1.setExit("north", outside);

        forest2.setExit("north", forest1);
        forest2.setExit("east" , river);

        forest3.setExit("south", leoHouse);
        forest3.setExit("east" , forest1);
        
        // leonardo's house complex...
        leoHouse.setExit("down", lab);
        leoHouse.setExit("north", forest3);
        lab.setExit("up", leoHouse);

        // river
        river.setExit("west", forest2);
        river.setExit("east", tower);

        //tower
        tower.setExit("west", river);
        tower.setExit("down", cell);

        //cell
        cell.setExit("up", tower);
    }

    /**
     * set all the exists for all the rooms for the character joe specifically
     * this needed to be done again despite being done above because a hashmap
     * has been used to store all the exists. since hashmaps aren't indexed
     * I can't access a hashmap to get a random exist. therefore, all the
     * exists are going to be stored in an arraylist which I am going to access
     * randomly.
     *
     * NOTE: I could have redesigned the entire way exists are stored, but I don't have time.
     */
    private void addExistsForJoe() {
        // outside exists...
        outside.addAnExistForJoe("east");
        outside.addAnExistForJoe("south");
        outside.addAnExistForJoe("west");

        // pub exists...
        pub.addAnExistForJoe("east");

        // house exists...
        house.addAnExistForJoe("west");

        // forest"
        forest1.addAnExistForJoe("south");
        forest1.addAnExistForJoe("west" );
        forest1.addAnExistForJoe("north");

        forest2.addAnExistForJoe("north");
        forest2.addAnExistForJoe("east");

        forest3.addAnExistForJoe("south");
        forest3.addAnExistForJoe("east");

        // leonardo's house complex...
        leoHouse.addAnExistForJoe("down");
        leoHouse.addAnExistForJoe("north");
        lab.addAnExistForJoe("up");

        // river
        river.addAnExistForJoe("west");
        river.addAnExistForJoe("east");

        //tower
        tower.addAnExistForJoe("west");
        tower.addAnExistForJoe("down");

        //cell
        cell.addAnExistForJoe("up");
    }
    
    /**
     * Create all items in all the rooms
     */
    private void creatItems() {
        mead      = new Items("mead", "alcoholic beverage", 1);
        sword     = new Items("sword", "weapon", 1);
        table     = new Items("table", "weapon", 10);

        machete   = new Items("machete", "weapon", 2);
        note      = new Items("note", "a sheet of paper.. it appears it has some writing on it.", 0);

        cure      = new Items("cure", "the cure", 1);
        note2     = new Items("note", "a sheet of paper.. it appears it has some writing on it.", 0);
        portalGun = new Items("portal gun", "teleporter", 2);

    }
    
    /**
     * Add all the items to all the rooms
     */
    private void addItemsToRooms() {
        pub.addItem(mead);
        pub.addItem(table);
        pub.addItem(sword);

        // lab items
        lab.addItem(portalGun);
        lab.addItem(note2);
    }

    /**
     * add the rooms to the ArrayList rooms.
     */
    private void populate() {
        rooms.add(outside);
        rooms.add(house);
        rooms.add(forest1);
        rooms.add(forest2);
        rooms.add(forest3);
        rooms.add(pub);
        rooms.add(leoHouse);
        rooms.add(river);
        rooms.add(tower);
    }
    
    /**
     * pick a room at random from the rooms created...
     */
    public Room pickRandomRoom() {
        Random rand = new Random();
        int index = rand.nextInt(rooms.size()-2);
        return rooms.get(index);
    }

    /**
     * check if a room is inside the ArrayList rooms
     * can't use the contains method because rooms is of type Room
     * and the user is inputting a string
     * @return the room given a string
     */
    public Room getRoom(String room) {
        for (Room givenRoom : rooms) {
            if (room.equals(givenRoom.roomName())) {
                return givenRoom;
            }
        }  
        return null;
    }
    
    /**
     * same logic as getRoom() method. just returns a boolean value
     * instead of the object Room.
     * @return if a string is a room
     */
    public boolean isRoom(String room) {
        for (Room givenRoom : rooms) {
            if (givenRoom.roomName().equals(room)) {
                return true;
            }
        }
        return false;
    }
}