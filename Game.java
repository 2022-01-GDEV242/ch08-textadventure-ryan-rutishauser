import java.util.ArrayList;
import java.util.Random;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Ryan Rutishauser
 * @version 2022.03.19
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Random randomizer;
    private ArrayList<Item> inventory;
    private int health;
    private ArrayList<Room> rooms;
    private Room locker;
    
    /**
     * Main method so that game can be run outside of Bluej.
     */
    public static void main(String[] args){
        Game game = new Game();
        game.play();
    }
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        inventory = new ArrayList<>();
        health = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     * Also creates all items and links them to rooms.
     */
    private void createRooms()
    {
        rooms = new ArrayList<>();
        
        Room outside, theater, pub, lab, office, cafeteria, water_closet,
        janitor_closet, kitchen, hallway, parking_lot, gymnasium, 
        auditorium, locker_room;
        
        Item apple, orange, banana;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        rooms.add(outside);
        theater = new Room("in a lecture theater");
        rooms.add(theater);
        pub = new Room("in the campus pub");
        rooms.add(pub);
        lab = new Room("in a computing lab");
        rooms.add(lab);
        office = new Room("in the computing admin office");
        rooms.add(office);
        cafeteria = new Room("in the cafeteria");
        rooms.add(cafeteria);
        water_closet = new Room("in the water closet");
        rooms.add(water_closet);
        janitor_closet = new Room("in the janitor closet");
        rooms.add(janitor_closet);
        kitchen = new Room("in the kitchen");
        rooms.add(kitchen);
        parking_lot = new Room("in the parking lot");
        rooms.add(parking_lot);
        hallway = new Room("in the hallway");
        rooms.add(hallway);
        gymnasium = new Room("in the gymnasium");
        rooms.add(gymnasium);
        auditorium = new Room("in the auditorium");
        rooms.add(auditorium);
        locker_room = new Room("in the locker room");
        rooms.add(locker_room);
        locker = new Room("locked in a locker");
        rooms.add(locker);
        
        apple = new Item("This is an apple. This can be eaten to improve your health", 1);
        orange = new Item("This is an orange. This can be eaten to improve your health", 1);
        banana = new Item("This is a banana. This can be eaten to improve your health", 1);
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", parking_lot);
        
        parking_lot.setExit("south", outside);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("west", cafeteria);
        lab.setExit("south", hallway);
        
        hallway.setExit("north", lab);
        hallway.setExit("west", water_closet);
        hallway.setExit("south", gymnasium);
        hallway.setExit("east", auditorium);

        office.setExit("west", lab);
        
        cafeteria.setExit("west", kitchen);
        cafeteria.setExit("south", water_closet);
        cafeteria.setExit("east", lab);
        cafeteria.setItem(orange);
        cafeteria.setItem(banana);
        
        kitchen.setExit("east", cafeteria);
        kitchen.setExit("south", janitor_closet);
        
        janitor_closet.setExit("north", kitchen);
        janitor_closet.setExit("east", water_closet);
        janitor_closet.setItem(apple);
        
        water_closet.setExit("west", janitor_closet);
        water_closet.setExit("north", cafeteria);
        water_closet.setExit("east", hallway);
        
        gymnasium.setExit("north", hallway);
        gymnasium.setExit("east", locker_room);
        
        auditorium.setExit("west", hallway);
        
        locker_room.setExit("west", gymnasium);
        locker_room.setExit("east", locker);

        currentRoom = outside;  // start game outside
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case LOOK:
                look(command);
                break;
                
            case GET:
                get(command);
                break;
            
            case EAT:
                eat(command);
                break;
                
            case TRANSPORT:
                transport(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            moveRoom(nextRoom);
        }
    }
    /**
     * moves user from current room to next room.
     * @param nextRoom room user will go next.
     */
    private void moveRoom(Room nextRoom)
    {
        currentRoom = nextRoom;
        System.out.println(currentRoom.getLongDescription());
        currentRoom.printItem();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    /** 
     * "Look" was entered. This prints the exits of the room for the player.
     */
    private void look(Command command) 
    {
        System.out.println(currentRoom.getLongDescription());
    }
    /** 
     * "Transport" was entered. This sends user to random room.
     */
    private void transport(Command command) 
    {
        Room room;
        if(currentRoom == locker){
            System.out.println("You cannot transport out of a locker.");
            return;
        }
        randomizer = new Random();
        while(true){
            room = rooms.get(randomizer.nextInt(rooms.size()));
            if (room != currentRoom){
                moveRoom(room);
                return;
            }
        }
    }
    /** 
     * "GET" was entered. This gets the item in the room.
     */
    private void get(Command command) 
    {
        if(currentRoom.numberItem() == 0){
            System.out.println("There are no items in this room.");
        }
        else{
            inventory.addAll(currentRoom.getItems());
            
            currentRoom.removeItems();
            System.out.println("You have gotten the items in this room.");
        }
    }
    /** 
     * "Eat" was entered. This command allows the player to eat food and 
     * satisfy his hunger. It increases health by 10 each time a food is
     * eaten and removes the food from inventory.
     */
    private void eat(Command command) 
    {
        if (inventory.size() == 0){
            System.out.println("You do not have any food.");
        }
        else{ 
            System.out.println("Your health was " + health);
            health += 10;
            System.out.println("Your health is now " + health);
            inventory.remove(0);
        }
    }
    }
