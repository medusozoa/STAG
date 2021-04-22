import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


class DotReader {

    ArrayList<Location> locations = new ArrayList<Location>();

    public Location getStartLocation()
    {
        for(Location location : locations)
        {
            if(location.name.equals("start")){
                return location;
            }
        }
        return null;
    }


    public void myReader(String arg)
    {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(arg);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
            for(Graph g : subGraphs){


                ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                for (Graph g1 : subGraphs1){
                    //Locations
                    ArrayList<Node> nodesLoc = g1.getNodes(false);
                    Node nLoc = nodesLoc.get(0);

                    //make new location
                    Location location = new Location(g1.getId().getId(), nLoc.getId().getId());
                    locations.add(location);


                    ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
                    for (Graph g2 : subGraphs2) {
                        //Entity

                        ArrayList<Node> nodesEnt = g2.getNodes(false);
                        for (Node nEnt : nodesEnt) {
                            //Entity specifications

                            if(g2.getId().getId().equals("artefacts")){
                                Artefact artefact = new Artefact(nEnt.getId().getId(),nEnt.getAttribute("description"));
                                location.addArtefact(artefact);
                            }
                            if(g2.getId().getId().equals("furniture")){
                                Furniture furniture = new Furniture(nEnt.getId().getId(),nEnt.getAttribute("description"));
                                location.addFurniture(furniture);
                            }
                            if(g2.getId().getId().equals("characters")){
                                Character character = new Character(nEnt.getId().getId(),nEnt.getAttribute("description"));
                                location.addCharacter(character);
                            }

                        }
                    }
                }

                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges){
                    for (Location sourceLocation :locations){
                        if(sourceLocation.name.equals(e.getSource().getNode().getId().getId())){
                            for (Location targetLocation :locations){
                                if(targetLocation.name.equals(e.getTarget().getNode().getId().getId())){
                                    sourceLocation.addPath(targetLocation);
                                }
                            }
                        }
                    }
                }
            }


        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (com.alexmerz.graphviz.ParseException pe) {
            System.out.println(pe);
        }

        }

    public ArrayList<Location> getLocations() {
        return locations;
    }
}



