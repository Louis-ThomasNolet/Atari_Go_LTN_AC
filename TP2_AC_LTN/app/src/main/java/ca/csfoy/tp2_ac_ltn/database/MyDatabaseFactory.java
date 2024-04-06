package ca.csfoy.tp2_ac_ltn.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseFactory extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "go.sqlite";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;
    private static final String TABLE_PLAY = "Play";

    public MyDatabaseFactory(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GameGoTable.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(db != null) {
            db.execSQL(GameGoTable.DROP_TABLE_SQL);
        }
            onCreate(db);

    }

    public void clearPlayTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            // Delete all rows from the "Play" table
            db.delete(TABLE_PLAY, null, null);
            String resetAutoIncrementSql = "DELETE FROM sqlite_sequence WHERE name='" + TABLE_PLAY + "'";
            db.execSQL(resetAutoIncrementSql);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MyDatabaseFactory", "Error clearing Play table", e);
        } finally {
            db.endTransaction();
        }
    }

    public Cursor loadGame() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_PLAY, null, null, null, null, null, null);
        } catch (Exception e) {
            Log.e("MyDatabaseFactory", "Error loading game data", e);
        }

        return cursor;
    }
    public SQLiteDatabase getDatabase()
    {
        try{
            return getWritableDatabase();
        }catch(Exception e){
            e.printStackTrace();
            return null;
    }
    }
    //Pour les tests
    public void runBatchQueries(String[] queries){
        for(String query : queries){
            db.execSQL(query);
        }

    }
}
