import java.io.*;
import java.net.*;
import java.util.*;

class StagServer
{

    Location startLocation;
    Player currentPlayer;
    ArrayList<Player> players = new ArrayList<Player>();
    JsonReader jsonReader;
    DotReader dotReader;

    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {

        dotReader = new DotReader();
        dotReader.myReader(entityFilename);
        startLocation = dotReader.getStartLocation();

        jsonReader = new JsonReader();
        jsonReader.myReader(actionFilename);

        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }



    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        String line = in.readLine();

        String playerName = getFirstWordofString(line);
        setCurrentPlayer(playerName);

        if(line.contains("look")){
            out.write("Your current location is: " + currentPlayer.getCurrentLocation().getName() + "\n");
            out.write("In the location are: " + makeIDString(currentPlayer.getCurrentLocation().getEntities()) + "\n");
        }

        if(line.contains("health")){
            out.write("Your current health is: " + currentPlayer.getHealth() + "\n");
        }

        if(line.contains("inv")||line.contains("inventory")){
            out.write("You have the following artefacts: " + makeIDString(currentPlayer.getArtefacts()) + "\n");
        }

        if(line.contains("get")){
            Iterator<Artefact> it = currentPlayer.getCurrentLocation().getArtefacts().iterator();
            while (it.hasNext()) {
                Artefact artefact = it.next();
                if(line.contains(artefact.getId())){
                    currentPlayer.addArtefact(artefact);
                    it.remove();
                }
            }
        }

        if(line.contains("drop")){
            Iterator<Artefact> it = currentPlayer.getArtefacts().iterator();
            while (it.hasNext()) {
                Artefact artefact = it.next();
                if(line.contains(artefact.getId())){
                    currentPlayer.getCurrentLocation().addArtefact(artefact);
                    it.remove();
                }
            }
        }

        if(line.contains("goto")){
            for(Location location : currentPlayer.getCurrentLocation().getPaths()) {
                if(line.contains(location.getName())){
                    currentPlayer.getCurrentLocation().getCharacters().remove(currentPlayer);
                    currentPlayer.setCurrentLocation(location);
                    currentPlayer.getCurrentLocation().addCharacter(currentPlayer);

                }
            }
        }

        if(jsonReader.lineContainsTrigger(line)){
            ArrayList<String> subjects = jsonReader.getSubjects();
            if(allSubjectsAvailable(subjects)){
                consumeEntity(jsonReader.getConsumed());
                produceEntity(jsonReader.getProduced());


                out.write(jsonReader.getNarration());
            }
        }


    }

    private void produceEntity(ArrayList<String> elements){
        for (String element : elements) {

            if(element.equals("health")){
                currentPlayer.setHealth(currentPlayer.getHealth()+1);
            }

            for(Location location: dotReader.getLocations()){
                if(location.getName().equals(element)){
                    currentPlayer.getCurrentLocation().addPath(location);
                }
            }

            for(Location location: dotReader.getLocations()){
                if(location.getName().equals("unplaced")){
                    ArrayList<Artefact> artefacts = location.getArtefacts();
                    ArrayList<Furniture> furnitures = location.getFurnitures();
                    ArrayList<Character> characters = location.getCharacters();

                    Iterator<Artefact> it = artefacts.iterator();
                    while (it.hasNext()) {
                        Artefact artefact = it.next();
                        if (element.equals(artefact.getId())) {
                            currentPlayer.getCurrentLocation().addArtefact(artefact);
                        }
                    }
                    Iterator<Furniture> it1 = furnitures.iterator();
                    while (it1.hasNext()) {
                        Furniture furniture = it1.next();
                        if (element.equals(furniture.getId())) {
                            currentPlayer.getCurrentLocation().addFurniture(furniture);
                        }
                    }
                    Iterator<Character> it2 = characters.iterator();
                    while (it2.hasNext()) {
                        Character character = it2.next();
                        if (element.equals(character.getId())) {
                            currentPlayer.getCurrentLocation().addCharacter(character);
                        }
                    }

                }
            }


        }
    }



    private void consumeEntity(ArrayList<String> elements) {
        ArrayList<Artefact> inventory = currentPlayer.getArtefacts();
        ArrayList<Artefact> artefacts = currentPlayer.getCurrentLocation().getArtefacts();
        ArrayList<Furniture> furnitures = currentPlayer.getCurrentLocation().getFurnitures();
        ArrayList<Character> characters = currentPlayer.getCurrentLocation().getCharacters();
        ArrayList<Location> paths = currentPlayer.getCurrentLocation().getPaths();


        for (String element : elements) {
            Iterator<Artefact> it = artefacts.iterator();
            while (it.hasNext()) {
                Artefact artefact = it.next();
                if (element.equals(artefact.getId())) {
                    it.remove();
                }
            }
            Iterator<Artefact> it4 = inventory.iterator();
            while (it4.hasNext()) {
                Artefact artefact = it4.next();
                if (element.equals(artefact.getId())) {
                    it4.remove();
                }
            }
            Iterator<Furniture> it1 = furnitures.iterator();
            while (it1.hasNext()) {
                Furniture furniture = it1.next();
                if (element.equals(furniture.getId())) {
                    it1.remove();
                }
            }
            Iterator<Character> it2 = characters.iterator();
            while (it2.hasNext()) {
                Character character = it2.next();
                if (element.equals(character.getId())) {
                    it2.remove();
                }
            }
            Iterator<Location> it3 = paths.iterator();
            while (it3.hasNext()) {
                Location path = it3.next();
                if (element.equals(path.getName())) {
                    it3.remove();
                }
            }

            if(element.equals("health")){
                currentPlayer.setHealth(currentPlayer.getHealth()-1);
                if(currentPlayer.getHealth() == 0){
                    currentPlayer.getCurrentLocation().getCharacters().remove(currentPlayer);
                    currentPlayer.setCurrentLocation(startLocation);
                    currentPlayer.setHealth(3);
                }
            }
        }
    }
    private boolean allSubjectsAvailable(ArrayList<String> subjects){
        ArrayList<String> inv = makeIDStringArrayList(currentPlayer.getArtefacts());
        ArrayList<String> entities = makeIDStringArrayList(currentPlayer.getCurrentLocation().getEntities());
        for(String subject : subjects) {
            if(!inv.contains(subject) && !entities.contains(subject)){
                return false;
            }
        }
        return true;

    }

    private String makeIDString(ArrayList<? extends Entity> arrayList){
        return makeStringListToString(makeIDStringArrayList(arrayList));
    }

    private ArrayList<String> makeIDStringArrayList(ArrayList<? extends Entity> arrayList){
        ArrayList<String> stringArrayList = new ArrayList<>();
        for(Entity entity : arrayList){
            stringArrayList.add(entity.getId());
        }
        return stringArrayList;
    }

    private String makeStringListToString(ArrayList<String> list){
        String delim = ", ";

        return String.join(delim, list);
    }

    private void setCurrentPlayer(String playerName){
        if(isNewPlayer(playerName)) {
            Player newPlayer = new Player(playerName, playerName, startLocation);
            players.add(newPlayer);
            newPlayer.getCurrentLocation().addCharacter(newPlayer);
            newPlayer.getCurrentLocation().setEntities();
            currentPlayer = newPlayer;
        }
        else {
            for (Player player : players) {
                if (player.getId().equals(playerName)) {
                    currentPlayer = player;
                }
            }
        }

    }

    private boolean isNewPlayer(String newPlayer){
        for(Player player : players){
            if(player.getId().equals(newPlayer)) {
                return false;
            }
        }
        return true;
    }

    private String getFirstWordofString(String string){
        return string.substring(0, string.indexOf(':'));
    }
}
