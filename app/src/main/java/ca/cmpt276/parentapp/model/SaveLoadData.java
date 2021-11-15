package ca.cmpt276.parentapp.model;

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

/**
 * SaveLoadData class:
 * <p>
 * class used to save/load configured children and coin flip history
 */
public class SaveLoadData {

    private SaveLoadData() {
    }

    private final static File file = new File(" ");
    private static ChildManager childManager = ChildManager.getInstance();
    private static WhosTurnManager whosTurnManager = WhosTurnManager.getInstance();

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

    public static void saveFlipHistoryList(String flipFilePath, ArrayList<CoinFlipData> coinFlipData) {
        try {
            String jsonString = myGson.toJson(coinFlipData);
            FileWriter fileWriter = new FileWriter(flipFilePath);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception " + exception.getMessage());
        }
    }

    public static ArrayList<CoinFlipData> loadFlipHistoryList(String flipHistoryPath) {
        File inputFlipHistory = new File(flipHistoryPath);
        try {
            JsonElement flipHistoryElement = JsonParser.parseReader(new FileReader(inputFlipHistory));
            JsonArray jsonArrayFlip = flipHistoryElement.getAsJsonArray();
            for (JsonElement flip : jsonArrayFlip) {
                JsonObject flipObject = flip.getAsJsonObject();
                String dateAsString = flipObject.get("timeOfFlip").getAsString();
                LocalDateTime timeOfFlip = LocalDateTime.parse(dateAsString);
                String nameOfPicker = flipObject.get("whoPicked").getAsString();
                boolean isHeads = flipObject.get("isHeads").getAsBoolean();
                boolean pickerPickedHeads = flipObject.get("pickerPickedHeads").getAsBoolean();
                boolean pickerWon = flipObject.get("pickerWon").getAsBoolean();
                CoinFlipData coinFlip = new CoinFlipData(timeOfFlip, nameOfPicker,
                        isHeads, pickerPickedHeads, pickerWon);
                childManager.addCoinFlip(coinFlip);
            }
        } catch (FileNotFoundException e) {
            //do nothing if no file found
            Log.e("TAG", "history Json not found");
        }
        return childManager.getCoinFlipHistory();
    }

    public static void saveChildList(String childFilePath, ArrayList<Child> childrenList) {
        try {
            String jsonChildName = myGson.toJson(childrenList);
            FileWriter fileWriter = new FileWriter(childFilePath);
            fileWriter.write(jsonChildName);
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception " + exception.getMessage());
        }
    }

    public static ArrayList<Child> loadChildList(String childFilePath) {
        File inputChildList = new File(childFilePath);
        try {
            JsonElement childElement = JsonParser.parseReader(new FileReader(inputChildList));
            JsonArray jsonArrayChild = childElement.getAsJsonArray();
            for (JsonElement child : jsonArrayChild) {
                JsonObject childObject = child.getAsJsonObject();
                String name = childObject.get("name").getAsString();
                Child newChild = new Child(name);
                childManager.addChild(newChild);
            }
        } catch (FileNotFoundException e) {
            //do nothing if no file found
            //Log.e("TAG", "CHILD FILE NOT FOUND");
        }
        return childManager.getChildList();
    }

    public static void saveTaskList(String taskFilePath, ArrayList<Task> tasks) {
        try {
            String jsonString = myGson.toJson(tasks);
            FileWriter fileWriter = new FileWriter(taskFilePath);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception " + exception.getMessage());
        }
    }

    public static ArrayList<Task> loadTaskList(String taskFilePath) {
        File inputTaskList = new File(taskFilePath);
        try {
            JsonElement taskElement = JsonParser.parseReader(new FileReader(inputTaskList));
            JsonArray jsonArrayTask = taskElement.getAsJsonArray();
            for (JsonElement task: jsonArrayTask) {
                JsonObject taskObject = task.getAsJsonObject();
                String taskName = taskObject.get("taskName").getAsString();
                String childName = taskObject.get("childName").getAsString();
                int childID = taskObject.get("currentChildID").getAsInt();
                Task newTask = new Task(taskName, childName, childID);
                whosTurnManager.addTask(newTask);
            }

        } catch (FileNotFoundException e) {
            //do nothing if no file found
            Log.e("TAG", "TASK FILE NOT FOUND");
        }
        return whosTurnManager.getTasks();
    }
}
