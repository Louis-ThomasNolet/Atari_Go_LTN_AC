package ca.csfoy.tp2_ac_ltn.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import ca.csfoy.tp2_ac_ltn.R;

import ca.csfoy.tp2_ac_ltn.data.Serializer;
import ca.csfoy.tp2_ac_ltn.database.MyDatabaseFactory;
import ca.csfoy.tp2_ac_ltn.mc.GoModelController;
import ca.csfoy.tp2_ac_ltn.parcelable.ParcelableImageButtonArray;


public class GameActivity extends AppCompatActivity {
    private final int SIZE = 9;
    private static int tokenState = 0;
    private static int lastPlayedX = -1;
    private static int lastPlayedY = -1;

    private int lastClickedX = -1;
    private int lastClickedY = -1;
    private boolean hasCancel = false;
    private boolean hasResign = false;
    GoModelController controller;
    ImageButton[][] board;
    Button btnPlay;
    Button btnResign;
    Button btnCancel;
    Button btnSave;
    ImageView leftArrow;
    ImageView rightArrow;

    private MyDatabaseFactory databaseFactory;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        btnCancel = findViewById(R.id.btnCancel);
        btnResign = findViewById(R.id.btnResign);
        btnPlay = findViewById(R.id.btnPlay);
        btnSave = findViewById(R.id.btnSave);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        board = new ImageButton[SIZE][SIZE];
        databaseFactory = new MyDatabaseFactory(this);
        database = databaseFactory.getDatabase();
        controller = new GoModelController(board, database);


        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCancelTurn();
            }
        });
        btnResign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!checkIfGameOver() && !hasResign){
                    if(controller.getTokenState() == 2){
                        controller.decideWinner("black");
                    } else{
                        controller.decideWinner("white");
                    }
                    leftArrow.setClickable(false);
                    rightArrow.setClickable(false);
                    btnCancel.setClickable(false);
                    btnPlay.setClickable(false);
                    showWinner();
                    btnResign.setText("New Game");
                }
                else{
                    btnResign.setText("Resign");
                    controller.resetGame();
                    controller.initTable();
                    hasResign = false;
                    leftArrow.setClickable(true);
                    rightArrow.setClickable(true);
                    btnCancel.setClickable(true);
                    btnPlay.setClickable(true);
                    startGameActivity();
                }
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setText("Back to Play");
                updateArrowButton(controller.leftArrowClick());
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateArrowButton(controller.rightArrowClick());
                if(controller.getCurrentBoard() == controller.allTurns.size() - 1){
                    btnPlay.setText("Play");
                    resetBoard(controller.allTurns.get(controller.getCurrentBoard()));
                    controller.resetClickable();
                    update();
                }
            }
        });
        for (int y = 1; y <= SIZE; y++) {
            for (int x = 1; x <= SIZE; x++) {
                int resID = getResources().getIdentifier(controller.getLetter(y) + controller.getLetter(x), "id", getPackageName());
                board[x - 1][y - 1] = findViewById(resID);
                final int X = x -1;
                final int Y = y -1;
                board[X][Y].setOnClickListener(v -> {
                    onClickBoard(X, Y);
                });
            }
        }
        btnPlay.setOnClickListener(v -> {
            btnPlay.setText("Play");
            resetBoard(controller.playTurn());//make sure the board showed is the current one
            checkDead();
            update();
        });

        btnSave.setOnClickListener(v -> {
            controller.save();
            Serializer.writeToFile(this);


            Serializer.deserialize(this);//Print the data in logs
        });
        controller.initTable();
        int x = -1;
        int y = -1;
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<String> colors = intent.getStringArrayListExtra("EXTRA_COLORS");
            ArrayList<String> stones = intent.getStringArrayListExtra("EXTRA_STONES");
            if (colors != null && stones != null) {
                for (int i = 0; i < colors.size(); i++) {
                    String color = colors.get(i);
                    String stone = stones.get(i);
                    x = controller.getCoordinate(stone.charAt(1)) - 1;
                    y = controller.getCoordinate(stone.charAt(0)) - 1;
                    controller.getColorFromString(color);
                    controller.drawDot(x, y);
                    controller.playTurn();
                    update();
                }
            }
        }
    }
    private void resetBoard(int[][] tags) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j].setImageResource(tags[i][j]);
                board[i][j].setTag(tags[i][j]);
            }

        }
    }
    private void updateArrowButton(int[][] temp) {
        for (int i = 0; i < SIZE; i++){
            for( int j = 0; j < SIZE; j++){
                board[i][j].setImageResource(temp[i][j]);
                board[i][j].setTag(temp[i][j]);
                board[i][j].setClickable(false);
            }
        }
    }
    private void checkDead() {
        controller.isDead();
    }
    private void update() {
        for (int i = 0; i < SIZE; i++){
            for( int j = 0; j < SIZE; j++){
                board[i][j] = controller.getBoard()[i][j];
                board[i][j].setImageResource((int)controller.getBoard()[i][j].getTag());
                if(checkIfGameOver()){
                    showWinner();
                }
            }
        }
    }

    private void showWinner() {
        String winnerMessage = controller.getWinner();
        //Snackbar to show winner
        Snackbar.make(this.findViewById(android.R.id.content), winnerMessage, Snackbar.LENGTH_LONG).show();
        btnCancel.setClickable(false);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j].setClickable(false);
            }
        }
        controller.resetGame();
        hasResign = true;
        btnResign.setText("New Game");
    }


    private boolean checkIfGameOver() {
        return controller.isGameOver();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("tokenState", tokenState);
        outState.putInt("lastPlayedX", lastPlayedX);
        outState.putInt("lastPlayedY", lastPlayedY);
        outState.putInt("lastClickedX", lastClickedX);
        outState.putInt("lastClickedY", lastClickedY);

        int[][] imageResources = new int[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                imageResources[x][y] = (Integer) board[x][y].getTag(); // Cast to Integer
            }
        }

        int x = 0;
        int y = 0;
        int[] flatArray = new int[SIZE * SIZE];
        for (int i = 0; i < SIZE * SIZE; i++) {
            flatArray[i] = imageResources[x][y];
            if (x == SIZE - 1) {
                x = 0;
                y++;
            } else {
                x++;
            }
        }
        outState.putIntArray("imageResources", flatArray);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tokenState = savedInstanceState.getInt("tokenState");
        lastPlayedX = savedInstanceState.getInt("lastPlayedX");
        lastPlayedY = savedInstanceState.getInt("lastPlayedY");
        lastClickedX = savedInstanceState.getInt("lastClickedX");
        lastClickedY = savedInstanceState.getInt("lastClickedY");

        int[] flatArray = savedInstanceState.getIntArray("imageResources");
        int x = 0;
        int y = 0;
        int[][] imageResources = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE * SIZE; i++) {
            imageResources[x][y] = flatArray[i];
            if (x == SIZE - 1) {
                x = 0;
                y++;
            } else {
                x++;
            }
        }

        for (x = 0; x < SIZE; x++) {
            for (y = 0; y < SIZE; y++) {
                board[x][y].setTag(imageResources[x][y]);
                board[x][y].setImageDrawable(controller.getBoard()[x][y].getDrawable());
            }
        }
        update();
    }
    private void startGameActivity() {

        lastPlayedX = -1;
        lastPlayedY = -1;
        tokenState = 0;
        Intent intent = new Intent(this, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    public void onClickBoard(int x , int y ) {
        controller.drawDot(x,y);
        update();
    }
    public void onClickCancelTurn(){
        controller.cancelTurn();
        update();
    }

}


