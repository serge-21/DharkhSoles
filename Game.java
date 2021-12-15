import java.util.Iterator;
import java.util.Random;

/**
 *  This class is the main class of the "Dharkh Soles" application.
 *  "Dharkh Soles" is a very simple, text based adventure game.  Users
 *  can walk around some scenery, pick and drop items, check their inventory
 *  use a map to see their location, attack AND be attacked by NPCs, give NPCs items,
 *  break OR open doors, talk to NPCs, inspect certain items, teleport to a specific location
 *  if the player has the portal gun OR teleport randomly if they are in the lab.
 *  The player wins if they find the cure AND give the cure to their mother.
 *  The player is vulnerable to dying in the game, as they can be attacked by NPCs.
 * 
 *  This main class creates and initialises all the others: it
 *  creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling, David J. Barnes, and Syraj ALkhalil
 * @version 28.11.2021
 */

public class Game
{
    private MapGUI map;
    private final People player;                // main player
    private final Parser parser;
    private final WorldGen worldGeneration;

    /**
     * the main method. this is what runs the game.
     * @param args the main arguments in the game.
     */
    public static void main(String[] args){
        Game game = new Game();
        game.play();
    }

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        parser          = new Parser();
        worldGeneration = new WorldGen();                 // first we generate the world
        player          = worldGeneration.getPlayer();    // player is generated with the world
    }

    /**
     *  Main play routine.  Loops until end of play.
     *  Enter the main command loop.  Here we repeatedly read commands and
     *  execute them until the game is over.
     */
    public void play() {
        printWelcome();
        boolean finished = false;

        while (!finished) {
            boolean didJoeMove = moveJoe();
            if (didJoeMove && (worldGeneration.getJoe().peek() == player.getPosition())) { // move joe randomly if he isn't dead
                System.out.println();
                System.out.println("you see joe leave");
            }
            Command command = parser.getCommand();
            finished = processCommand(command);

            followPlayer();
            if(player.isDead()){
                finished =  true;
            }
        }
        System.out.println("Thank you for playing!!!  Good bye :(");
        System.exit(0);
    }

    /**
     * this method decides (randomly) to either move the character Joe or not.
     * if it decides to move joe, the method will check if joe is dead first
     * if joe is not dead then joe will move to a room that is NEXT TO
     * the current room that joe is in.
     * @return if NPC joe moved or not.
     */
    private boolean moveJoe() {
        Random random = new Random();
        if(random.nextInt(2) == 1 && worldGeneration.getJoe().getHealth() > 0){
            worldGeneration.getJoe().moveRandomly();
            return true;
        }
        return false;
    }
    
    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Dharkh Soles"); // might change the name later.
        System.out.println("Dharkh Soles is a new, incredibly cool adventure game...:)");
        System.out.println("You are a nobel knight in the late 15th century");
        System.out.println("One day you come home only to discover that your mother has fallen a victim to a new disease :(");
        System.out.println("Your only hope is to find Leonardo Da Vinci as he is the best physician in this era...");
        System.out.println("If you don't find him... who knows? maybe your mother will die. Are you willing to chance it ?");
        System.out.println();
        System.out.println("Just to get you started here is the map... (type 'map' if to see the map again)");
        System.out.println("Type 'help' if you need help.");
        map();
        System.out.println("to teleport please enter the name of the place AS SEEN ON the map. don't worry about upper or lower case! :)");
        System.out.println("if you want to interact with something that has more than one word for a name you could try:");
        System.out.println("pick the \"portal gun\" so you should place \" around the name!");
        System.out.println("Quest: find Leonardo and get the cure");
        System.out.println();
        System.out.println(player.getPosition().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't recognise the command you used?");
            return false;
        }

        switch (command.getCommandWord()) {
            case "map"      : map();             break;
            case "help"     : printHelp();       break;
            case "pick"     : pick(command);     break;
            case "talk"     : talk(command);     break;
            case "read"     : read(command);     break;
            case "drop"     : drop(command);     break;
            case "back"     : back();            break;
            case "open"     : openDoor(command); break;
            case "break"    : breakDoor(command);break;
            case "location" : location();        break;
            case "teleport" : teleport(command); break;
            case "inventory": inventory();       break;
            case "go"       : goRoom(command);   break;

            // or we quit the game we beat the game when we give the cure
            // to the mother therefore we update wantToQuit regardless...
            case "quit"     : wantToQuit = quit(command);     break;
            case "attack"   : wantToQuit = attack(command);   break;
            case "give"     : wantToQuit = give(command);     break;
        }

        // else command not recognised.
        return wantToQuit;
    }

    //  ----------------------- implementations of user commands: ------------------------
    /**
     * Print out some help information.
     * Here we print the position of the player, the inventory and show the 
     * command words.
     */
    private void printHelp() {
        System.out.println("fineeeeeee I will help you... well let's start with your location.. ");
        System.out.println(player.getPosition().getLongDescription());
        System.out.println();
        System.out.println("Don't forget your inventory!!!");
        inventory();
        System.out.println();
        System.out.println("Alsooooo you can have the list of commands you can use :)");
        parser.showCommands();
        System.out.println("Quest: find Leonardo and get the cure");
        System.out.println("Good luck adventurer!!");
    }

    /**
     * Allow the player to go back to the last visited room
     * If a room is visited more than once then back will use go to this room more than once
     */
    private void back() {
        if(!player.getAllVisitedRooms().isEmpty()){
            if(player.peekVisitedPosition().roomName().equals("river")){
                if(player.numberOfItems() != 0){
                    System.out.println("you can't swim in the river because you are carrying items");
                    System.out.println("the currents are insane... you will drown... please drop all the items you have...");
                    return;
                }
            }
            player.changePosition(player.getLastVisitedPosition());
            System.out.println(player.getPosition().getLongDescription());
            player.changeNextPosition(player.getPosition());
        }else{
            System.out.println("go back where?"); 
        }
    }

    /**
     * make the characters talk. if the player wants to talk to them.
     * @param command of type Command. What the player enters.
     */
    private void talk(Command command){
        if(!command.hasSecondWord()){
            System.out.println("talk to whom exactly ?");
            return;
        }
        if(player.getPosition().hasPerson(command.getSecondWord())){
            player.getPosition().getPerson(command.getSecondWord()).talk();

            if(player.getPosition().getPerson(command.getSecondWord()) instanceof NPCLeo && worldGeneration.getLeo().getPosition().roomName().equals("cell")){
                worldGeneration.getLeo().getPosition().removePerson(worldGeneration.getLeo());
                worldGeneration.getLeo().changePosition(worldGeneration.getLab());
                Items cure = worldGeneration.getLeo().getItem("cure");
                worldGeneration.getLeo().dropItem("cure");
                player.pickItem(cure);
                System.out.println("the cure has been acquired");
            }
        }else{
            System.out.println("this person doesn't exist here!");
        }
    }

    /**
     * allow the player to read the note and nothing else.
     * @param command of type Command. What the player enters.
     */
    private void read(Command command) {
        if(!command.hasSecondWord()){
            System.out.println("What do u want to read?");
            return;
        }else if(!command.getSecondWord().equals("note")){
            System.out.println("You can't read that :(");
            return;
        }
        if (player.hasItem("note")){
            System.out.println("you can only read notes once be careful...");
            Items note = player.getItem("note");
            player.dropItem("note");
            System.out.println(worldGeneration.getNote(note));
        }else{
            System.out.println("You don't have a note to read...");
        }
    }

    /**
     * attack the player by checking who is following the player
     * whoever is following the player is following the player to attack them.
     */
    private void attackPlayer() {
        Iterator<Enemy> it = worldGeneration.attackingPlayer().iterator();
        while(it.hasNext()){
            it.next().attack(player);
            //this does nothing
        }
    }

    /**
     * check who needs to be following the player and make them
     * follow the player.
     * then calls the method attackPlayer()
     */
    private void followPlayer() {
        Iterator<Enemy> it = worldGeneration.attackingPlayer().iterator();
        while(it.hasNext()){
            it.next().follow(player);
        }
        attackPlayer();
    }
    
    /**
     * allow the player to attack other NPCs IF they have a weapon of
     * some sorts.
     * @param command of type Command. What the player enters.
     */
    private boolean attack(Command command) {
        if(!command.hasSecondWord()){
            System.out.println("who do you want to attack ?");
            return false;
        }
        if(player.itemType("weapon")){
            if(player.getPosition().hasPerson(command.getSecondWord())){
                People personToBeAttacked = player.getPosition().getPerson(command.getSecondWord());
                player.attack(personToBeAttacked);
                
                if(personToBeAttacked instanceof Enemy){
                    personToBeAttacked.following(true);
                }
                if(personToBeAttacked instanceof Mother){
                    player.setHealth(player.getHealth() - 5);
                    System.out.println("your health now is " + player.getHealth() + "/100");
                }
            }else{
                System.out.println(command.getSecondWord() + " doesn't exist here!");
            }
        }else{
            System.out.println("you don't have a weapon");
        }
        return player.isDead();
    }
    
    /**
     * This will allow the player to teleport to any room they want IF they have a portal gun. 
     * Obviously, the challenge task could still be achieved by not specifying a room
     * AND being inside leonardo's lab. Otherwise, the teleportation wouldn't work.
     * @param command of type Command. What the player enters.
     */
    private void teleport(Command command) {
        if(!command.hasSecondWord()){
            if(player.getPosition().roomName().equals("leonardo's lab")){
                player.clearStack();
                player.changePosition(worldGeneration.pickRandomRoom());
                player.changeNextPosition(player.getPosition());
                System.out.println(player.getPosition().getLongDescription());
            }else{
                System.out.println("you can't teleport here... :( go to the lab or use the portal gun!!!");
            }
        }else{
            if(player.hasItem("portal gun")){
                if(worldGeneration.isRoom(command.getSecondWord())){
                    player.clearStack();
                    player.changePosition(worldGeneration.getRoom(command.getSecondWord()));
                    player.changeNextPosition(player.getPosition());
                    System.out.println(player.getPosition().getLongDescription());
                    if(worldGeneration.getRoom(command.getSecondWord()).roomName().equals("river")){
                        if(player.numberOfItems() != 0){
                            player.setHealth(0);
                            System.out.println("You drowned... because you are carrying items with you whilst swimming in the river.");
                        }
                    }
                }else{
                    System.out.println("This is not a room :( you can't teleport there");
                }
            }else
                System.out.println("you don't have a portal gun");
        }
    }
    
    /**
     * This method needs 3 words the command word give
     * an item to give and a person to give to
     * @param command of type Command. What the player enters.
     */
    private boolean give(Command command) {
        if(!command.hasSecondWord()){
            System.out.println("give what?");
            return false;
        }else if(!command.hasThirdWord()){
            System.out.println("give it to whom ?");
            return false;
        }
        
        if(player.hasItem(command.getSecondWord())){
            if(player.getPosition().hasPerson(command.getThirdWord())){
                People otherPerson = player.getPosition().getPerson(command.getThirdWord());
                
                if(otherPerson instanceof Enemy){
                    System.out.println("why would u give your enemy anything ?");
                    System.out.println("I guess I have to be the voice of reason and stop you...");
                    System.out.println("you don't give the enemy anything...");
                }else{
                    Items item = player.getItem(command.getSecondWord());
                    player.dropItem(command.getSecondWord());
                    otherPerson.pickItem(item);
                    System.out.println(otherPerson.getName() + " thanks you for giving them: " + item.getItemName());
                }
                
                // here we should check if the mother has the cure... 
                // if she does the game ends.
                if(otherPerson.getName().equals("mom")){
                    if(otherPerson.hasItem("cure")){
                        System.out.println("Congrats.. you beat the game and saved your mother!");
                        return true;
                    }
                }
            }else{
                System.out.println(command.getThirdWord() + " This person doesn't exist here.");
            }
        }else{
            System.out.println("You don't have that item");
        }
        return false;
    }
    
    /**
     * This method will display the current position of the player
     * it will also display the items present in the room they are in.
     */
    private void location() {
        System.out.println(player.getPosition().getLongDescription());
    }
    
    /**
     * This method is responsible for displaying the player's inventory
     */
    private void inventory() {
        player.inventory();
    }
    
    /**
     * This method is responsible for dropping an item out of the inventory 
     * when this method is used the item specified will be dropped out 
     * of the inventory and added to the room.
     * @param command of type Command. What the player enters.
     */
    private void drop(Command command) {
        if(!command.hasSecondWord()){
            System.out.println("drop what?");
            return;
        }

        if(player.numberOfItems() == 0){
            System.out.println("Your inventory is empty :(");
            System.out.println("try to get some item by using the pick command!!!");
            return;
        }

        if(player.hasItem(command.getSecondWord())){
            Items item = player.getItem(command.getSecondWord());

            player.dropItem(command.getSecondWord());
            player.getPosition().addItem(item);
            System.out.println(command.getSecondWord() + " dropped");
        }else{
            System.out.println(command.getSecondWord() + " doesn't exist in your inventory :(");
        }
    }
    
    /**
     * This method is responsible for picking an item out of the room 
     * when this method is used the item specified will be picked from the
     * room and added to the inventory.
     * @param command of type Command. What the player enters.
     */
    private void pick(Command command) {
        if(!command.hasSecondWord()){
            System.out.println("pick what?");
            return;
        }

        if(player.getPosition().numberOfItems() == 0){
            System.out.println("no items exist");
            return;
        }

        if(player.getPosition().hasItem(command.getSecondWord())){
            Items item = player.getPosition().getItem(command.getSecondWord());
            
            // check the player's total weight before adding the item
            if(player.getTotalWeight() + item.getWeight() <= 5){
                player.getPosition().removeItem(command.getSecondWord());
                player.pickItem(item);
                System.out.println(command.getSecondWord() + " picked");
            }else{
                System.out.println("You don't have enough place for this item :(");
            }
        }else{
            System.out.println(command.getSecondWord() + " doesn't exist here :(");
        }
    }

    /**
     * Push the door open so that you can enter the room
     * doors will close in this game if the player just "open's" them
     * @param command of type Command. What the player enters.
     */
    private void openDoor(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Open what?");
            return;
        }else if (!command.getSecondWord().equalsIgnoreCase("door")){
            System.out.println("You can't open that");
            return;
        }
        if(player.getNextPosition() != null) {
            if (player.getNextPosition().getHasDoor() == null) {
                System.out.println("this area doesn't have a door...");
            } else if (player.getNextPosition().hasDoor()) {
                player.getNextPosition().changeStateOfDoor(false);
                player.changePosition(player.getNextPosition());
                player.getPosition().changeStateOfDoor(true);
                System.out.println(player.getPosition().getLongDescription());
            } else {
                System.out.println("You broke the door...");
                System.out.println("And nowwww you want to be polite? Nah too late");
            }
        }else{
            System.out.println("what door?");
        }
    }

    /**
     * To keep a door open the player has to break the door.
     * This has the same functionality as the openDoor method, but it
     * keeps the door open instead of closing the door.
     * Why push a door open when you can break it open?
     * @param command of type Command. What the player enters.
     */
    private void breakDoor(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("break what exactly ?");
            return;
        }else if (!command.getSecondWord().equals("door")){
            System.out.println("You can't break that :(");
            return;
        }

        if(player.getNextPosition() != null){
            if (player.getNextPosition().getHasDoor() == null) {
                System.out.println("this area doesn't have a door...");
            } else if(player.getNextPosition().hasDoor()){
                player.getNextPosition().changeStateOfDoor(false);
                player.changePosition(player.getNextPosition());
                System.out.println("Damn, very violent but I guess you must be in hurry...");
                System.out.println("it's ok though... I forgive you :)");
                System.out.println();
                System.out.println(player.getPosition().getLongDescription());
            }else{
                System.out.println("You broke the door...");
                System.out.println("What else do want to break ? ");
            }

        }else{
            System.out.println("what door ?");
        }
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * @param command of type Command. What the player enters.
     */
    private void goRoom(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        player.changeNextPosition(player.getPosition().getExit(direction));

        if (player.getNextPosition() == null) {
            System.out.println("You can't go there :(");
        }
        else {
            player.addRoom(player.getPosition()); // before we change the current room we push this room onto the stack.

            if(player.getNextPosition().getHasDoor() != null){
                if(player.getNextPosition().hasDoor()){
                    System.out.println("You are outside " + player.getNextPosition().roomName() + "\n");
                    System.out.println("There is a door... so you can't just barge into the place like this XD");
                    System.out.println("Be careful... the doors close on their own...");
                    System.out.println("What did you expect? it's the 15th century... no door stops yet :( ");
                    System.out.println("Butttttt maybe you can break doors as well as open them?? Who knows give it a try... :3 ");
                }else{
                    player.changePosition(player.getNextPosition());
                    System.out.println(player.getPosition().getLongDescription());
                }

            }else{
                if(player.getNextPosition().roomName().equals("river")){
                    if(player.numberOfItems() != 0){
                        System.out.println("you can't swim in the river because you are carrying items");
                        System.out.println("the currents are insane... you will drown... please drop all the items you have...");
                        return;
                    }
                }
                player.changePosition(player.getNextPosition());
                System.out.println(player.getPosition().getLongDescription());
            }
        }
    }
    
    /**
     * make a new instance of class MapGUI to show the map. 
     */
    private void map() {
        if(map == null || !map.getFrame().isVisible()){
            map = new MapGUI();
            if(map.getFrame().isVisible()){
                System.out.println("Here is a map!");
            }
            return;
        }
        System.out.println("you have a map");
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}