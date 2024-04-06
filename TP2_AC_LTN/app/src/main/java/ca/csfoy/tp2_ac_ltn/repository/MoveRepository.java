package ca.csfoy.tp2_ac_ltn.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ca.csfoy.tp2_ac_ltn.data.MoveData;
import ca.csfoy.tp2_ac_ltn.database.GameGoTable;
import ca.csfoy.tp2_ac_ltn.exceptions.DatabaseExceptions;

public class MoveRepository implements IMoveRepository{
    //PS: MANQUE PEUT ETRE LE FIND ALL SI BESOIN
    private SQLiteDatabase database = null;
    public MoveRepository(SQLiteDatabase database)
    {
        this.database = database;
    }
    @Override
    public void delete(MoveData myObject) throws DatabaseExceptions {
        delete(myObject.getNo_play());
    }

    @Override
    public MoveData findRandomMove() throws DatabaseExceptions {
        Cursor cursor = null;
        MoveData move = null;
        try{
            this.database.beginTransaction();
            cursor = this.database.rawQuery(GameGoTable.SELECT_SQL_WITH_RANDOM_ID, new String[]{});

            if(cursor.moveToNext())
            {
                if(cursor.getLong(0)>0)
                {
                    int moveId = cursor.getInt(0);
                    String color = cursor.getString(1);
                    String stone = cursor.getString(2);
                    move = new MoveData(moveId,color,stone);
                }
            }
            this.database.setTransactionSuccessful();
            return move;
        }
        catch(RuntimeException e){
            throw new DatabaseExceptions();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
            this.database.endTransaction();
        }
    }

    @Override
    public void delete(long id) throws DatabaseExceptions
    {
        if( id > 0){
            try{
                this.database.beginTransaction();
                this.database.execSQL(GameGoTable.DELETE_SQL, new String[]{String.valueOf(id)});
                this.database.setTransactionSuccessful();
            }
            catch(RuntimeException e){
                throw new DatabaseExceptions();
            }
            finally {
                this.database.endTransaction();
            }
        }
    }

    @Override
    public void insert(MoveData myObject) throws DatabaseExceptions {
        if(myObject != null){
            try{
                this.database.beginTransaction();
                this.database.execSQL(GameGoTable.INSERT_SQL, new String[]{
                        myObject.getColor(),
                        myObject.getStone()
                });
                this.database.setTransactionSuccessful();
            }
            catch(RuntimeException e){
                throw new DatabaseExceptions();
            }
            finally {
                this.database.endTransaction();
            }
        }
    }

    @Override
    public MoveData find(long id) throws DatabaseExceptions{
        Cursor cursor = null;
        MoveData move = null;
        try{
            this.database.beginTransaction();
            cursor = this.database.rawQuery(GameGoTable.SELECT_ONE_OF_SQL, new String[]{String.valueOf(id)});

            if(cursor.moveToNext()){
                if(cursor.getLong(0)>0){
                    int moveId = cursor.getInt(0);
                    String color = cursor.getString(1);
                    String stone = cursor.getString(2);
                    move = new MoveData(moveId,color,stone);
                }
            }
            this.database.setTransactionSuccessful();
            return move;
        }
        catch(RuntimeException e){
            throw new DatabaseExceptions();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
            this.database.endTransaction();
        }
    }

    @Override
    public void save(MoveData myObject) throws DatabaseExceptions {
        if(myObject != null){
            try{
                this.database.beginTransaction();
                this.database.execSQL(GameGoTable.UPDATE_SQL, new String[]{
                        myObject.getColor(),
                        myObject.getStone(),
                        String.valueOf(myObject.getNo_play())
                });
                this.database.setTransactionSuccessful();
            }
            catch(RuntimeException e){
                throw new DatabaseExceptions();
            }
            finally {
                this.database.endTransaction();
            }
        }
    }



}
