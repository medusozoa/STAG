import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class JsonReader {
    JSONArray actions;
    JSONObject currentJSONObject;

    public JsonReader() {
    }

    public void myReader(String arg) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(arg)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            actions = (JSONArray) jsonObject.get("actions");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean lineContainsTrigger(String line){
        for(Object actionObject : actions){
            JSONObject action = (JSONObject) actionObject;
            JSONArray triggers = (JSONArray) action.get("triggers");
            for(Object triggerObject: triggers){
                String trigger = (String) triggerObject;
                if(line.contains(trigger)){
                    currentJSONObject = action;
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<String> getSubjects(){
        JSONArray subjectsJSON = (JSONArray) currentJSONObject.get("subjects");
        ArrayList<String> subjects = (ArrayList<String>) subjectsJSON;
        return subjects;
    }
    public ArrayList<String> getConsumed(){
        JSONArray consumedJSON = (JSONArray) currentJSONObject.get("consumed");
        ArrayList<String> consumed = (ArrayList<String>) consumedJSON;
        return consumed;
    }
    public ArrayList<String> getProduced(){
        JSONArray producedJSON = (JSONArray) currentJSONObject.get("produced");
        ArrayList<String> produced = (ArrayList<String>) producedJSON;
        return produced;
    }

    public String getNarration(){
        String narration = (String) currentJSONObject.get("narration");
        return narration;
    }






}




