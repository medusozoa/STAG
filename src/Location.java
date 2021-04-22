import java.util.ArrayList;

public class Location {
    String id;
    String name;

    ArrayList<Artefact> artefacts = new ArrayList<Artefact>();
    ArrayList<Character> characters = new ArrayList<Character>();
    ArrayList<Furniture> furnitures = new ArrayList<Furniture>();
    ArrayList<Entity> entities = new ArrayList<Entity>();
    ArrayList<Location> paths = new ArrayList<Location>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Location(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addPath(Location location) {
        paths.add(location);
    }

    public void addArtefact(Artefact artefact) {
        artefacts.add(artefact);
        setEntities();
    }

    public void addCharacter(Character character) {
        characters.add(character);
        setEntities();
    }

    public void addFurniture(Furniture furniture) {
        furnitures.add(furniture);
        setEntities();
    }

    public ArrayList<Artefact> getArtefacts() {
        setEntities();
        return artefacts;
    }

    public ArrayList<Character> getCharacters() {
        setEntities();
        return characters;
    }

    public ArrayList<Furniture> getFurnitures() {
        setEntities();
        return furnitures;
    }

    public ArrayList<Location> getPaths() {
        return paths;
    }

    public void setEntities() {
        entities.clear();
        entities.addAll(artefacts);
        entities.addAll(characters);
        entities.addAll(furnitures);
    }
    public ArrayList<Entity> getEntities(){
        setEntities();
        return entities;
    }

}
