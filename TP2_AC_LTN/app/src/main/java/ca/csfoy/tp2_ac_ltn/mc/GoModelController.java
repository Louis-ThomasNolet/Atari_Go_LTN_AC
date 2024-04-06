package ca.csfoy.tp2_ac_ltn.mc;

import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.csfoy.tp2_ac_ltn.R;
import ca.csfoy.tp2_ac_ltn.data.Serializer;
import ca.csfoy.tp2_ac_ltn.database.GameGoTable;

public class GoModelController {
    private final int SIZE = 9;
    private static int tokenState = 0;
    private static int currentBoard = -1;
    private static int whitePoints = 0;
    private static int blackPoints = 0;
    private  static int lastPlayedX = -1;
    private  static int lastPlayedY = -1;
    private static int lastClickedX = -1;
    private static int lastClickedY = -1;

    private SQLiteDatabase database;
    private final ImageButton[][] board;
    public static final List<int[][]> allTurns = new ArrayList<int[][]>(0);


    public GoModelController(ImageButton[][] board, SQLiteDatabase database){
        this.board = board;
        this.database = database;
    }
    public static int getTokenState() {
        return tokenState;
    }
    public static int getCurrentBoard(){
        return currentBoard;
    }
    public  int getLastPlayedX() {
        return lastPlayedX;
    }
    public  int getLastPlayedY() {
        return lastPlayedY;
    }
    public void initTable(){//Initialize the board with their default image resources
        for(int x = 0; x < SIZE; x++){
            for (int y = 0; y < SIZE; y++) {
                board[x][y].setImageResource(getDefaultTile(x,y));
                board[x][y].setTag(getDefaultTile(x,y));
            }
        }
    }
    public void drawDot(int X , int Y){//Draw a dot on the correct spot
        if(lastClickedX != -1)//Makes sure only one new dot is there at a time
            board[lastClickedX][lastClickedY].setTag(getDefaultTile(lastClickedX,lastClickedY));

        if (tokenState == 1 || tokenState == 0) {//Draw the correct color
            board[X][Y].setTag(R.drawable.blackdot);
        } else {
            board[X][Y].setTag(R.drawable.whitedot);
        }
        lastClickedY = Y;
        lastClickedX = X;
    }
    public int getDefaultTile(int x, int y) {//Get the default value of each tile with their x and y coordinates
        if(x == 0 && y == 0){
            return R.drawable.ul;
        }
        else if(x == 0 && y == SIZE - 1){
            return R.drawable.dl;
        }
        else if(x == SIZE - 1 && y == 0){
            return R.drawable.ur;
        }
        else if(x == SIZE - 1 && y == SIZE - 1){
            return R.drawable.dr;
        }
        else if(x == 0 && y < SIZE && y > 0){
            return R.drawable.l;
        }
        else if(x == SIZE - 1 && y < SIZE && y > 0){
            return R.drawable.r;
        }
        else if(y == 0 && x < SIZE && x > 0){
            return R.drawable.u;
        }
        else if(y == SIZE -1 && x < SIZE && x > 0){
            return R.drawable.d;
        }
        else{
            return R.drawable.m;
        }
    }

    public int[][] playTurn() {//Play a turn
        if(currentBoard != allTurns.size() - 1){//Return to current board if checking previous turns
            currentBoard = allTurns.size() - 1;
            resetClickable();
        }
        else{//Plays the correct token and changes the last token to normal dot
            if(lastClickedY != -1){
                if(tokenState == 2){
                    board[lastClickedX][lastClickedY].setTag(R.drawable.whitedotsquare);
                    board[lastClickedX][lastClickedY].setClickable(false);
                    tokenState = 1;
                }
                else{
                    board[lastClickedX][lastClickedY].setTag(R.drawable.blackdotsquare);
                    board[lastClickedX][lastClickedY].setClickable(false);
                    tokenState = 2;
                }

                if(lastPlayedX != -1){
                    if(tokenState == 2){
                        board[lastPlayedX][lastPlayedY].setTag(R.drawable.whitedot);
                    }
                    else{
                        board[lastPlayedX][lastPlayedY].setTag(R.drawable.blackdot);
                    }
                }
                insertMove(lastClickedX,lastClickedY);
                lastPlayedX = lastClickedX;
                lastPlayedY = lastClickedY;
                lastClickedX = -1;
                lastClickedY = -1;
            }
            if(!contains(board)){
                int[][] temp = new int[SIZE][SIZE];
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        if(determineTileTag(i,j) != 0)
                            temp[i][j] = (int)board[i][j].getTag();
                        else
                            temp[i][j] = getDefaultTile(i,j);
                    }
                }
                allTurns.add(temp);
                currentBoard++;
            }
        }
        return allTurns.get(currentBoard);
    }

    public void resetClickable() {//Make the board clickable after returning from looking at previous turns
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(allTurns.get(currentBoard)[i][j] == getDefaultTile(i,j)){
                    board[i][j].setClickable(true);
                }
            }
        }
    }

    public int[][] leftArrowClick() {//Check the previous turn
        if(currentBoard - 1 >= 0){
            currentBoard--;
        }
        return allTurns.get(currentBoard);
    }
    public int[][] rightArrowClick() {//Check the next turn if possible
        if(currentBoard + 1 < allTurns.size()){
            currentBoard++;
        }
        return allTurns.get(currentBoard);
    }
    private boolean contains(ImageButton[][] board) {//Checks if we are trying to add an existing board to the board list
        for (int[][] boards : allTurns) {
            boolean isIn = true; // Reset isIn for each board in allTurns
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (boards[i][j] != (int)board[i][j].getTag()) {
                        isIn = false; // If any element doesn't match, set isIn to false
                        break; // No need to continue checking this board
                    }
                }
                if (!isIn) {
                    break; // No need to continue checking this board
                }
            }
            if (isIn) {
                return true; // If the board matches, return true
            }
        }
        return false; // If no matching board was found, return false
    }
    public ImageButton[][] getBoard() {
        return board;
    }
    public void cancelTurn() {//Cancels a turn
        if(lastPlayedX != -1){
            board[lastPlayedX][lastPlayedY].setTag(getDefaultTile(lastPlayedX,lastPlayedY));
            board[lastPlayedX][lastPlayedY].setClickable(true);
            Serializer.removeFromList(getColorFromInt(lastPlayedX, lastPlayedY), getLetter(lastPlayedX) + getLetter(lastPlayedY));//Makes sure we do not save a canceled turn
            lastPlayedY = -1;
            lastPlayedX = -1;
            if(tokenState == 2){
                tokenState = 1;
            }
            else{
                tokenState = 2;
            }
            allTurns.remove(currentBoard);
            currentBoard--;
        }
    }
    public void isDead() {//Checks if cells are dead
        boolean[][] surroundedCells = new boolean[SIZE][SIZE];

        //Identify surrounded cells
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                surroundedCells[i][j] = isSurrounded(i, j);
            }
        }

        // List to store the "bigCells" (connected token of the same color)
        List<List<String[]>> allBigCells = new ArrayList<>();

        //Iterate to find grouped surrounded cells
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (surroundedCells[i][j]) {  // If this cell is surrounded
                    int currentTileTag = determineTileTag(i, j);
                    List<String[]> bigCell = new ArrayList<>();  // Create a list to store coordinates

                    // Check adjacent cells
                    checkAdjacentSurroundedCells(i, j, currentTileTag, surroundedCells, bigCell);

                    // Add the bigCell to the list of all bigCells
                    if (!bigCell.isEmpty()) {
                        allBigCells.add(bigCell);
                    }
                }
            }
        }

        for (List<String[]> bigCell : allBigCells) {
            if (isBigCellSurroundedByEnemies(bigCell)) {
                killBigCell(bigCell);// Kill the cells that are surrounded
            }
        }
    }
    private void killBigCell(List<String[]> bigCell) {//Kill cells that are connected and surrounded
        for (String[] coord : bigCell){
            int x = Integer.parseInt(coord[0]);
            int y = Integer.parseInt(coord[1]);
            if(determineTileTag(x,y) == R.drawable.blackdot)
                whitePoints++;
            else if (determineTileTag(x,y) == R.drawable.whitedot)
                blackPoints++;
            board[x][y].setTag(getDefaultTile(x,y));
        }
    }
    private boolean isBigCellSurroundedByEnemies(List<String[]> bigCell) {//Checks if a cell is surrounded
        for (String[] coord : bigCell) {
            int x = Integer.parseInt(coord[0]);
            int y = Integer.parseInt(coord[1]);
            int currentTag = determineTileTag(x,y);
            int left = x - 1;//cell to the left
            int right = x + 1;//cell to the right
            int top = y - 1;//cell on top
            int bottom = y + 1;//cell on bottom

            //get the coordinate of every adjacent cell
            String[] leftCoord = new String[2];
            leftCoord[0] = "" + left;
            leftCoord[1] = "" + y;
            String[] rightCoord = new String[2];
            rightCoord[0] = "" + right;
            rightCoord[1] = "" + y;
            String[] topCoord = new String[2];
            topCoord[0] = "" + x;
            topCoord[1] = "" + top;
            String[] bottomCoord = new String[2];
            bottomCoord[0] = "" + x;
            bottomCoord[1] = "" + bottom;

            //checks if adjacent cells are enemies or are in the same bigCell
            if(!isIn(bigCell, leftCoord)){
                if(!isEnemyCell(left, y, currentTag))
                    return false;
            }
            if(!isIn(bigCell, rightCoord)){
                if (!isEnemyCell(right, y, currentTag))
                    return false;
            }
            if(!isIn(bigCell, topCoord)){
                if(!isEnemyCell(x, top, currentTag))
                    return false;
            }
            if(!isIn(bigCell, bottomCoord)){
                if (!isEnemyCell(x, bottom, currentTag))
                    return false;
            }
        }
        return true;  // All boundary cells are enemies
    }
    private boolean isIn(List<String[]> bigCell, String[] coord) {//Checks if the adjacent cell is in the same bigCell
        for (String[] coords : bigCell){
            if(Objects.equals(coords[0], coord[0]) && Objects.equals(coords[1], coord[1])){
                return true;//Adjacent is in the same bigCell
            }
        }
        return false;//Adjacent is not in the same bigCell
    }
    private boolean isEnemyCell(int x, int y, int currentTag) {//Checks if a cell is an enemy to the current cell
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return true;  // Out of bounds are considered enemies
        }
        int newTileTag = determineTileTag(x, y);//status of the cell being checked
        return newTileTag != currentTag;//return true if checked cell status is not the same as our current cell, return false otherwise
    }
    private int determineTileTag(int x, int y) {//get the tag (we store drawables in the tag) of a cell ex: R.drawable.blackdot
        int currentTileTag = 0;
        int tagValue = (int) board[x][y].getTag();
        if (tagValue == R.drawable.whitedot || tagValue == R.drawable.whitedotsquare)
            currentTileTag = R.drawable.whitedot;
        else if (tagValue == R.drawable.blackdot || tagValue == R.drawable.blackdotsquare)
            currentTileTag = R.drawable.blackdot;
        return currentTileTag;
    }
    private void checkAdjacentSurroundedCells(int x, int y, int currentTileTag, boolean[][] surroundedCells, List<String[]> bigCell) {//Recursive function to check if a bigCell is surrounded
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE || !surroundedCells[x][y]) {
            return;  // Base condition for recursion
        }
        int newTileTag = 0;
        if((int)board[x][y].getTag() == R.drawable.whitedot || (int)board[x][y].getTag() == R.drawable.whitedotsquare)
            newTileTag = R.drawable.whitedot;
        else if ((int)board[x][y].getTag() == R.drawable.blackdot || (int)board[x][y].getTag() == R.drawable.blackdotsquare)
            newTileTag = R.drawable.blackdot;

        if (newTileTag == currentTileTag) {
            String[] coord = {String.valueOf(x), String.valueOf(y)};
            bigCell.add(coord);

            // Mark as visited to avoid infinite loop
            surroundedCells[x][y] = false;

            // Check all 4 adjacent cells recursively
            checkAdjacentSurroundedCells(x + 1, y, currentTileTag, surroundedCells, bigCell);
            checkAdjacentSurroundedCells(x - 1, y, currentTileTag, surroundedCells, bigCell);
            checkAdjacentSurroundedCells(x, y + 1, currentTileTag, surroundedCells, bigCell);
            checkAdjacentSurroundedCells(x, y - 1, currentTileTag, surroundedCells, bigCell);
        }
    }
    private boolean isSurrounded(int x, int y) {//Checks if the cell is surrounded, does not care if it is enemy or not
        boolean isToken = (int)board[x][y].getTag() == R.drawable.blackdotsquare || (int)board[x][y].getTag() == R.drawable.blackdot || (int)board[x][y].getTag() == R.drawable.whitedotsquare || (int)board[x][y].getTag() == R.drawable.whitedot;
        // Check if the cell is surrounded on each side
        // A cell is surrounded on a side if that side is next to an edge or to another cell
        boolean isSurroundedOnLeft = x == 0 || (((int) board[x - 1][y].getTag() == R.drawable.blackdot || (int) board[x - 1][y].getTag() == R.drawable.blackdotsquare)) || (((int) board[x - 1][y].getTag() == R.drawable.whitedot || (int) board[x - 1][y].getTag() == R.drawable.whitedotsquare));
        boolean isSurroundedOnRight = x == SIZE - 1 || (((int) board[x + 1][y].getTag() == R.drawable.blackdot || (int) board[x + 1][y].getTag() == R.drawable.blackdotsquare)) || (((int) board[x + 1][y].getTag() == R.drawable.whitedot || (int) board[x + 1][y].getTag() == R.drawable.whitedotsquare));
        boolean isSurroundedOnTop = y == 0 || (((int) board[x][y - 1].getTag() == R.drawable.blackdot || (int) board[x][y - 1].getTag() == R.drawable.blackdotsquare)) || (((int) board[x][y - 1].getTag() == R.drawable.whitedot || (int) board[x][y - 1].getTag() == R.drawable.whitedotsquare));
        boolean isSurroundedOnBottom = y == SIZE - 1 || (((int) board[x][y + 1].getTag() == R.drawable.blackdot || (int) board[x][y + 1].getTag() == R.drawable.blackdotsquare)) || (((int) board[x][y + 1].getTag() == R.drawable.whitedot || (int) board[x][y + 1].getTag() == R.drawable.whitedotsquare));

        return isToken && isSurroundedOnLeft && isSurroundedOnRight && isSurroundedOnBottom && isSurroundedOnTop;
    }
    public String getWinner(){//Get a winner message
        if(blackPoints > whitePoints)
            return "Les noirs gagnent";
        else
            return "Les blancs gagnent";
    }
    public boolean isGameOver() {
        return blackPoints > 0 || whitePoints > 0;
    }//checks if the game is over

    public void resetGame() {//ready every atributes for a new game
        lastPlayedX = -1;
        lastPlayedY = -1;
        lastClickedY = -1;
        lastClickedX = -1;
        currentBoard = -1;
        allTurns.clear();
        whitePoints = 0;
        blackPoints = 0;
        tokenState = 0;
    }

    public void decideWinner(String winner) {//determine the winner when someone resigns
        if(winner == "black")
            blackPoints++;
        else if (winner == "white") {
            whitePoints++;
        }
    }

    public String getLetter(int nb) {//get letter (row and collumn) from the coresponding coordinate
        switch(nb){
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            case 4:
                return "D";
            case 5:
                return "E";
            case 6:
                return "F";
            case 7:
                return "G";
            case 8:
                return "H";
            case 9:
                return "I";
            default:
                return "";
        }
    }

    public int getCoordinate(char coord) {//get the coordinate (row and collumn) from the coresponding letter
        switch(coord){
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;
            case 'E':
                return 5;
            case 'F':
                return 6;
            case 'G':
                return 7;
            case 'H':
                return 8;
            case 'I':
                return 9;
            default:
                return -1;
        }
    }
    public void getColorFromString(String value){//set the current color to play
        if(value.equals("W")){
            tokenState = 2;
        }
        else if(value.equals("B") ){
            tokenState = 1 ;
        }

    }
    private String getColorFromInt(int row, int column){//get the color from the cell being checked
        if((int) board[row][column].getTag() == R.drawable.whitedot || (int) board[row][column].getTag() == R.drawable.whitedotsquare){
            return "W";
        }
        else if((int) board[row][column].getTag() == R.drawable.blackdot ||(int) board[row][column].getTag() == R.drawable.blackdotsquare ){
            return "B";
        }
        return "NULL";
    }
    public void insertMove(int row, int column){//insert a move into the database
        if (database != null) {
        String stone = getLetter(column + 1) + getLetter(row + 1);
        String insertSql = GameGoTable.INSERT_SQL
                .replace("$color", "'" + getColorFromInt(row, column)+"'")
                .replace("$stone", "'" + stone +"'");
            database.execSQL(insertSql);
        }
    }

    public void save(){//save game as a json file when save button is pressed
        Serializer.eraseFile();
        Serializer.addDate();
        Serializer.addName();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(getColorFromInt(i,j) != "NULL"){
                    Serializer.addToList(getColorFromInt(i,j), getLetter(j + 1) + getLetter(i + 1));
                }
            }
        }
    }
}
