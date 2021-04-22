import java.util.ArrayList;

public class Player extends Character {
    Location currentLocation;
    ArrayList<Artefact> artefacts = new ArrayList<Artefact>();
    int health;

    public Player(String id, String description, Location location) {
        super(id, description);
        currentLocation = location;
        health = 3;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public ArrayList<Artefact> getArtefacts() {
        return artefacts;
    }
    public void addArtefact(Artefact artefact) {
        artefacts.add(artefact);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
