package ca.cmpt276.parentapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * SaveLoadData class:
 *
 * class used to save/load configured children and coin flip history
 */
public class SaveLoadData {

    private SaveLoadData(){
    }
    private final static File file = new File(" ");
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

    public static void saveFlipHistoryList(String flipFilePath, ArrayList<CoinFlipData> coinFlipData){
        try{
            String jsonString = myGson.toJson(coinFlipData);
            FileWriter fileWriter = new FileWriter(flipFilePath);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception " + exception.getMessage());
        }
    }

    public static ArrayList<CoinFlipData> loadFlipHistoryList(String flipHistoryPath){
        File inputFlipHistory = new File(flipHistoryPath);
        try{
            JsonElement flipHistoryElement = JsonParser.parseReader(new FileReader(inputFlipHistory));
            JsonArray jsonArrayFlip = flipHistoryElement.getAsJsonArray();
            for (JsonElement flip : jsonArrayFlip){
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
                String photo = childObject.get("photo").getAsString();
                Child newChild = new Child(name, photo);
                childManager.addChild(newChild);
            }
        } catch (FileNotFoundException e) {
            //do nothing if no file found
            Log.e("TAG", "CHILD FILE NOT FOUND");
        }
        return childManager.getChildList();
    }

    /*Adapted from https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa */
    public static String encode(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap decode(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


}
