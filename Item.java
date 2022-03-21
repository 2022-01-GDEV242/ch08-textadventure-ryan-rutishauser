

/**
 * Class Item - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Item" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Ryan Ruitshauser
 * @version 2022.03.21
 */

public class Item 
{
    private String description;
    private int weight;

    /**
     * Create a item described "description". Initially, it has
     * @param description The item's description.
     * @param sets room item is associated with.
     */
    public Item(String description, int weight) 
    {
        this.description = description;
        this.weight = weight;
    }
    
    /**
     * @return The weight of the item.
     */
    public int getWeight() 
    {
        return weight;
    }

    /**
     * @return The short description of the item
     * (the one that was defined in the constructor).
     */
    public String getDescription()
    {
        return description;
    }

}

