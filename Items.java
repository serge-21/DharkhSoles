/**
 * This class is responsible for
 * representing all the items in the game.
 *
 * @author Syraj Alkahlil
 * @version 28.11.2021
 */
public class Items
{
    private int weight;
    private String name;
    private String description;

    /**
     * set the item a name, a description, and a weight.
     */
    public Items(String name, String description, int weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * @return the item's weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return the item's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the item's name
     */
    public String getItemName() {
        return name;
    }
}
