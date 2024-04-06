package ca.csfoy.tp2_ac_ltn.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Serializer {

    private static final String TAG = "Seri";
    private static String fileName = "MoveData.json";
    private static String filePath = "/data/data/ca.csfoy.tp2_ac_ltn/files/MoveData.json";
    private static Calendar calendar = Calendar.getInstance();
    private static String name;
    private static String date;
    private static List<String[]> game = new ArrayList<String[]>(0);

    public static void addToList(String color, String stone){
        game.add(new String[]{color,stone});
    }
    public static void addName(){
        int currentMonth = calendar.get(Calendar.MONTH)+ 1;
        name = "Ma joute du mois de " + getMonthString(currentMonth);
    }

    private static String getMonthString(int month){
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Invalid month";
        }
    }
    public static void addDate(){
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        date = String.format("%02d/%02d/%04d",day,month,year);
    }
    public static void eraseFile(){
        game.clear();
        File file = new File(filePath);
        if (file.exists()) {
            // Attempt to delete the file
            if (file.delete()) {
                // File deleted successfully
                Log.d("DeleteFile", "File deleted successfully");
            } else {
                // Failed to delete the file
                Log.e("DeleteFile", "Failed to delete file");
            }
        } else {
            // File does not exist
            Log.e("DeleteFile", "File does not exist");
        }
    }
    public static void writeToFile(Context context){
        File file = new File(filePath);
        if (file.exists()) {
            // Attempt to delete the file
            if (file.delete()) {
                // File deleted successfully
                Log.d("DeleteFile", "File deleted successfully");
            } else {
                // Failed to delete the file
                Log.e("DeleteFile", "Failed to delete file");
            }
        } else {
            // File does not exist
            Log.e("DeleteFile", "File does not exist");
        }


        JSONObject object = new JSONObject();
        JSONArray gameArray = new JSONArray();
        Log.d("SERIALIZE", "hasBeenCalled : writeToFile");
        try {
            object.put("name", name);
            object.put("date", date);

            for (String[] item : game) {
                JSONObject gameObject = new JSONObject();
                gameObject.put(item[0], item[1]);
                gameArray.put(gameObject);
            }
            object.put("game", gameArray);

            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                String content = object.toString();
                fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
                Log.d("SERIALIZE", "hasWrittenToFile");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void deserialize(Context context) {
        try {
            if(!new File(filePath).exists()){
                File file = new File(filePath);
            }
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            parseJson(stringBuilder.toString());
        } catch (IOException e) {
            Log.e(TAG, "Error reading file", e);
        }
    }

    private static void parseJson(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            name = jsonObject.getString("name");
            date = jsonObject.getString("date");

            JSONArray gameArray = jsonObject.getJSONArray("game");
            game.clear(); // Clear existing game list
            for (int i = 0; i < gameArray.length(); i++) {
                JSONObject gameObj = gameArray.getJSONObject(i);
                String color = gameObj.keys().next(); // Get the key
                String stone = gameObj.getString(color); // Get the value
                game.add(new String[]{color, stone});
            }

            // Print the data in Log.d("Serializer", ...)
            Log.d(TAG, "Name: " + name);
            Log.d(TAG, "Date: " + date);
            for (String[] item : game) {
                Log.d(TAG, "Color: " + item[0] + ", Stone: " + item[1]);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
    }

    public static void removeFromList(String colorFromInt, String stone) {
        game.remove(new String[]{colorFromInt, stone});
    }
}
