import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import java.util.Random;
import java.util.*;
import java.util.Arrays;
import java.util.List;

/** class that implements the Model of Domestic Robot application */
public class HouseModel extends GridWorldModel {

    // constants for the grid objects
    public static final int FRIDGE = 16;
    public static final int OWNER  = 32;
    public static final int DOOR  = 64;
    // public static final int CLEANINGAGENT = 8;
    public static final int DIRT = 8;


    // the grid size
    public static final int GSize = 7;

    boolean fridgeOpen   = false; // whether the fridge is open
    boolean carryingBeer = false; // whether the robot is carrying beer
    int sipCount        = 0; // how many sip the owner did
    int availableBeers  = 2; // how many beers are available
    int dirtCount      = 0; // how many dirt are on the floor
    int maxDirt = 10; // how many dirt are on the floor
    double dirtSpawnProbability = 0.3; // how many dirt are on the floor

    Location lFridge = new Location(0,0);
    Location lOwner  = new Location(GSize-1,GSize-1);
    Location lDoor  = new Location(0,GSize-1);

    Location lRobotCleaner = new Location(GSize-1,0);
    Location lRobotConcierge = new Location(GSize/2,0);

    List<Location> dirtLoc = new ArrayList<Location>();
    // Location lCleaningAgent = new Location(GSize-1,0);
    Location lDirt = super.getFreePos();

    public HouseModel() {
        // create a 7x7 grid with three mobile agent
        super(GSize, GSize, 3);

        // initial location of robot (column 3, line 3)
        // ag code 0 means the robot
        setAgPos(0, GSize/2, GSize/2);
        // ag code 1 means the cleaner
        setAgPos(1, lRobotCleaner);
        // ag code 2 means the concierge
        setAgPos(2, lRobotConcierge);

        // initial location of fridge and owner and cleaning agent
        add(FRIDGE, lFridge);
        add(OWNER, lOwner);
        add(DOOR, lDoor);
        // add(CLEANINGAGENT, lCleaningAgent);

        // initial location of dirt randomly generated with low probability
        add(DIRT, lDirt);
        dirtLoc.add(lDirt);
        // add(DIRT, GSize/2,GSize/2);
    }

    void spawnDirt() {
        Location lDirt = super.getFreePos();

        while(lDirt.equals(lFridge) || lDirt.equals(lOwner) || lDirt.equals(lOwner) || lDirt.equals(getAgPos(0)) || lDirt.equals(getAgPos(1)) || lDirt.equals(getAgPos(2)) || lDirt.equals(lDoor)) {
            lDirt = super.getFreePos();
        }
        // Spawn dirt only if it's less than the maximum amount of dirt aloowed and if it has a low probability
        if (dirtCount <= maxDirt && new Random().nextFloat() < dirtSpawnProbability) {
            add(DIRT, lDirt);
            dirtLoc.add(lDirt);
            dirtCount++;
        }
    }

    boolean openFridge() {
        spawnDirt();
        if (!fridgeOpen) {
            fridgeOpen = true;
            return true;
        } else {
            return false;
        }
    }

    boolean closeFridge() {
        spawnDirt();
        if (fridgeOpen) {
            fridgeOpen = false;
            return true;
        } else {
            return false;
        }
    }

    boolean moveRobot(Location dest) {
        // spawnDirt();
        Location r1 = getAgPos(0);
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
        setAgPos(0, r1); // move the robot in the grid

        // repaint the fridge and owner locations
        if (view != null) {
            view.update(lFridge.x,lFridge.y);
            view.update(lOwner.x,lOwner.y);
        }
        return true;
    }

    boolean moveRobotCleaner(Location dest) {
        // spawnDirt();
        Location r1 = getAgPos(1);
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
        setAgPos(1, r1); // move the robot in the grid

        // clean dirt
        if (view != null) {
            remove(DIRT, r1);
            dirtCount--;
            dirtLoc.remove(r1);

            view.update();
        }
        return true;
    }


    boolean moveRobotConcierge(Location dest) {
        // spawnDirt();
        Location r1 = getAgPos(2);
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
        setAgPos(2, r1); // move the robot in the grid
        
        // repaint the fridge and owner locations
        if (view != null) {
            view.update();
        }
        return true;
    }

    boolean getBeer() {
        spawnDirt();
        Random rn = new Random();
        if (fridgeOpen && availableBeers > 0 && !carryingBeer) {
            availableBeers--;
            carryingBeer = true;
            if (view != null)
                view.update(lFridge.x,lFridge.y);
            return true;
        } else {
            return false;
        }
    }

    boolean addBeer(int n) {
        spawnDirt();
        availableBeers += n;
        if (view != null)
            view.update(lFridge.x,lFridge.y);
        return true;
    }

    boolean handInBeer() {
        spawnDirt();
        if (carryingBeer) {
            sipCount = 10;
            carryingBeer = false;
            if (view != null)
                view.update(lOwner.x,lOwner.y);
            return true;
        } else {
            return false;
        }
    }

    boolean sipBeer() {
        // spawnDirt();
        if (sipCount > 0) {
            sipCount--;
            if (view != null)
                view.update(lOwner.x,lOwner.y);
            return true;
        } else {
            return false;
        }
    }
}
