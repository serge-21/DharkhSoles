import java.util.HashMap;

/**
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling, David J. Barnes and Syraj Alkhalil
 * @version 28.11.2021
 */

public class CommandWords
{
    // a constant array that holds all valid command words
    private final HashMap<String, String> validCommands;

    public CommandWords(){
        validCommands = new HashMap<>();
        populate();
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString) {
        for (String validCommand : validCommands.keySet()) {
            if (validCommand.equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }
    
    /**
     * fill the hashmap with all the possible commands.
     */
    private void populate(){
        validCommands.put("go", "go [direction]: go in a certain direction");
        validCommands.put("help", "help: displays this message :)");
        validCommands.put("back", "back: goes back, not if you teleport though...");
        validCommands.put("open", "open [door]: opens the door");
        validCommands.put("break", "break [door]: breaks the door");
        validCommands.put("pick", "pick [item]: pick up an item and place it in the inventory");
        validCommands.put("drop", "drop [item]: drop an item from the inventory");
        validCommands.put("inventory", "inventory: shows you the inventory");
        validCommands.put("location", "location: displays the current location");
        validCommands.put("give", "give [item] [character]: give a certain character an item");
        validCommands.put("teleport", "teleport OR teleport [place]: just teleport on its own will teleport to a random location...");
        validCommands.put("attack", "attack [character]: attacks a certain character");
        validCommands.put("map", "map: displays the map");
        validCommands.put("talk", "talk [character]: talks to a certain character");
        validCommands.put("read", "read [note]: reads the note");
        validCommands.put("quit", "quit: quits the game");
    }

    /**
     * Print all valid commands and their descriptions
     */
    public void showAll() {
        for(String command : validCommands.keySet()){
            System.out.println(validCommands.get(command));
        }
    }
}
