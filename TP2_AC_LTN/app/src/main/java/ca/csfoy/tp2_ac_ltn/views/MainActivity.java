package ca.csfoy.tp2_ac_ltn.views;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ca.csfoy.tp2_ac_ltn.R;
import ca.csfoy.tp2_ac_ltn.data.Serializer;
import ca.csfoy.tp2_ac_ltn.database.MyDatabaseFactory;

public class MainActivity extends AppCompatActivity {

    //private String DATABASE_NAME = "go.sqlite"; // Pas encore de Nom il faut mettre le nom de la BD mais je suis pas encore sur quel nom faut mettre
    private MyDatabaseFactory databaseFactory;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        System.out.println("Test");

        Button btnNewGame = findViewById(R.id.btnNewGame);
        Button btnReplay = findViewById(R.id.btnReplay);
        //Fait la connexion de la BD
        databaseFactory = new MyDatabaseFactory(this);
        database = databaseFactory.getWritableDatabase();
        btnNewGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                databaseFactory.clearPlayTable();
                startGameActivity();
            }
        });
        btnReplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor cursor = databaseFactory.loadGame();

                // Check if there is a saved game
                if (cursor != null && cursor.moveToFirst()) {
                    // Start the GameActivity with the loaded data
                    restartGameActivity(cursor);
                } else {
                    // Handle the case when there is no saved game
                    Log.d("Replay", "No saved game found");
                }

                // Don't forget to close the cursor when done
                if (cursor != null) {
                    cursor.close();
                }
            }

        });
    }
    //On doit mettre la BD dans la MainActivity car il faut l'utiliser avec le bouton newGame et Replay et on doit arreter la BD lorsque l'app arrete
    @Override
    protected void onStop(){
        super.onStop();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    private void startGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    private void restartGameActivity(Cursor cursor) {
        Intent intent = new Intent(this, GameActivity.class);

        if(cursor != null && cursor.moveToFirst()){
            ArrayList<String> colors = new ArrayList<>();
            ArrayList<String> stones = new ArrayList<>();

            do{
                int colorIndex = cursor.getColumnIndex("color");
                int stoneIndex = cursor.getColumnIndex("stone");
               // int noPlay = cursor.getColumnIndex("no_play");
                if(colorIndex != -1 && stoneIndex != -1) {
                    String color = cursor.getString(colorIndex);
                    String stone = cursor.getString(stoneIndex);
                    colors.add(color);
                    stones.add(stone);
                }
            }
            while(cursor.moveToNext());

                    intent.putStringArrayListExtra("EXTRA_COLORS", colors);
                    intent.putStringArrayListExtra("EXTRA_STONES", stones);
                    startActivity(intent);

        } else
        {
            Log.e("RestartGameActivity", "Invalid column indices");
        }

        if(cursor == null){
            cursor.close();
        }
    }
}