package ca.cmpt276.parentapp;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

public class SaveLoadData {
    /*
    String childFilePath = getFilesDir().getPath().toString() + "/SaveChildInfo3.json";
    File inputChildList = new File(childFilePath);

     */
    private SaveLoadData(){
    }
    private final static File file = new File(" ");
    //private final static String currentWorkingDirectory = file.getAbsolutePath();
    //private final static String childFilePath = System.getProperty("user.dir") + "SaveChildInfo3.json";
    //private final static File inputChildList = new File(System.getProperty("user.dir") + "SaveChildInfo3.json");
    private static ChildManager childManager = ChildManager.getInstance();

    private static Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
            new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter jsonWriter,
                                  LocalDateTime localDateTime) throws IOException {
                    jsonWriter.value(localDateTime.toString());
                }
                @Override
                public LocalDateTime read(JsonReader jsonReader) throws IOException {
                    return LocalDateTime.parse(jsonReader.nextString());
                }
            }).create();

    public static void saveChildList(String childFilePath, ArrayList<Child> childrenList){
        try {
            String jsonChildName = myGson.toJson(childrenList);
            FileWriter fileWriter = new FileWriter(childFilePath);
            fileWriter.write(jsonChildName);
            fileWriter.close();
        } catch (IOException exception){
            System.out.println("Exception " + exception.getMessage());
        }
    }

    public static ArrayList<Child> loadChildList(String childFilePath){
        File inputChildList = new File(childFilePath);
        try{
            JsonElement childElement = JsonParser.parseReader(new FileReader(inputChildList));
            JsonArray jsonArrayChild = childElement.getAsJsonArray();
            for (JsonElement child : jsonArrayChild){
                JsonObject childObject = child.getAsJsonObject();
                String name = childObject.get("name").getAsString();
                Child newChild = new Child(name);
                childManager.addChild(newChild);
            }
        } catch (FileNotFoundException e) {
            //do nothing if no file found
            Log.e("TAG", "CHILD FILE NOT FOUND");
        }
        return childManager.getChildList();
    }
}
