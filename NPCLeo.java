import java.util.Iterator;
/**
 * this class is responsible for the behaviour of NPC Joe.
 */
public class NPCLeo extends People{

    /**
     * making an NPCLeo object is the same as
     * making a People object.
     */
    public NPCLeo(String name, Room position) {
        super(name, position);
    }

    /**
     * making Leo "talk" to the player
     * nothing fancy just a bunch of print statements.
     */
    public void talk() {
        super.talk();
        if(this.getPosition().roomName().equals("cell")){
            System.out.println("thank you for freeing me nobel knight...");
            System.out.println("to repay my debt I will give you a cure that will cure all ailments");
            System.out.println("please don't abuse its power");
            System.out.println("if you need me I will be in my lab!");
        }else{
            System.out.println("hello nobel knight...");
            System.out.println("thank you for freeing me again...");
            System.out.println("take whatever you want from my lab");
        }
    }

    /**
     * checks if a player or a character is dead
     * if they are then we check the inventory,
     * if they have anything in the inventory then they drop
     * the items in the inventory.
     *
     * @return if leo is dead
     */
    public boolean isDead() {
        if(getHealth() <= 0){
            System.out.println("why would you kill Leonardo Da Vinci ?????");
            if(getInventory().size() != 0){
                System.out.println("I mean u can loot him I guess... :(");
                System.out.println("just so that you know... he was willing to give you the cure...");
                inventory();

                dropAll();
            }
            return true;
        }
        return false;
    }

    /**
     * drop all the items that leo has
     */
    private void dropAll(){
        Iterator<Items> it = getInventory().iterator();
        while(it.hasNext()){
            getPosition().addItem(getItem(it.next().getItemName()));
            it.remove();
        }
    }

    /**
     * change leonardo's position and add him to the new room.
     * @param  newPosition  the position we wish to go to.
     */
    public void changePosition(Room newPosition){
        super.changePosition(newPosition);
        this.getPosition().addPerson(this);
    }
}
