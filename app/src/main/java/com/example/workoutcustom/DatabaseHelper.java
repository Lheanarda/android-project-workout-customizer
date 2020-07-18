package com.example.workoutcustom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLInput;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "workout.db";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE workouts (workoutid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "workoutname TEXT,totalTime INTEGER,sets INTEGER)");

        db.execSQL("CREATE TABLE byreps (listid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, reps INTEGER, listnumber INTEGER, workoutid INTEGER," +
                "FOREIGN KEY (workoutid) REFERENCES workouts(workoutid))");

        db.execSQL("CREATE TABLE bytime (listid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name text, seconds INTEGER, listnumber INTEGER,workoutid INTEGER," +
                "FOREIGN KEY (workoutid) REFERENCES workouts(workoutid))");

        db.execSQL("CREATE TABLE rest(listid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "seconds INTEGER, listnumber INTEGER, workoutid INTEGER," +
                "FOREIGN KEY (workoutid) REFERENCES workouts(workoutid))");

        db.execSQL("CREATE TABLE break(breakid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "seconds INTEGER, workoutid INTEGER," +
                "FOREIGN KEY (workoutid) REFERENCES workouts(workoutid))");

        //SAMPLE TABATA
        insertWorkoutsDefault(db,"Sample Tabata Timer",240,1);

        insertbytimeDefault(db,"Work",20,1,1);
        insertrestDefault(db,10,2,1);

        insertbytimeDefault(db,"Work",20,3,1);
        insertrestDefault(db,10,4,1);

        insertbytimeDefault(db,"Work",20,5,1);
        insertrestDefault(db,10,6,1);

        insertbytimeDefault(db,"Work",20,7,1);
        insertrestDefault(db,10,8,1);

        insertbytimeDefault(db,"Work",20,9,1);
        insertrestDefault(db,10,10,1);

        insertbytimeDefault(db,"Work",20,11,1);
        insertrestDefault(db,10,12,1);

        insertbytimeDefault(db,"Work",20,13,1);
        insertrestDefault(db,10,14,1);

        insertbytimeDefault(db,"Work",20,15,1);
        insertrestDefault(db,10,16,1);
        //END SAMLPLE TABATA

        //SAMPLE 7 Minute
        insertWorkoutsDefault(db,"Sample 7 Minutes Workout",470,1);

        insertbytimeDefault(db,"Jumping Jack",30,1,2);
        insertrestDefault(db,10,2,2);

        insertbytimeDefault(db,"Wall Sit",30,3,2);
        insertrestDefault(db,10,4,2);

        insertbytimeDefault(db,"Push Up",30,5,2);
        insertrestDefault(db,10,6,2);

        insertbytimeDefault(db,"Crunch",30,7,2);
        insertrestDefault(db,10,8,2);

        insertbytimeDefault(db,"Step On To Chair",30,9,2);
        insertrestDefault(db,10,10,2);

        insertbytimeDefault(db,"Squat",30,11,2);
        insertrestDefault(db,10,12,2);

        insertbytimeDefault(db,"Tricep Dip",30,13,2);
        insertrestDefault(db,10,14,2);

        insertbytimeDefault(db,"Plank",30,15,2);
        insertrestDefault(db,10,16,2);

        insertbytimeDefault(db,"High Knees",30,17,2);
        insertrestDefault(db,10,18,2);

        insertbytimeDefault(db,"Lunges",30,19,2);
        insertrestDefault(db,10,20,2);

        insertbytimeDefault(db,"Pushup + Rotation",30,21,2);
        insertrestDefault(db,10,22,2);

        insertbytimeDefault(db,"Side Plank One Side",15,23,2);
        insertbytimeDefault(db,"Side Plank Other Side",15,24,2);
        //END SAMPLE 7 MINUTE

        //SAMPLE TEST
        insertWorkoutsDefault(db,"1 Minutes Workout",80,3);
        insertbytimeDefault(db,"Jumping Jack",4,1,3);
        insertrestDefault(db,3,2,3);

        insertbytimeDefault(db,"Squad",8,3,3);
        insertrestDefault(db,7,4,3);

        insertbreakDefault(db,5,3);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS workouts");
        onCreate(db);
    }

    //INSERT
    public boolean insertWorkoutsDefault(SQLiteDatabase db,String workoutname, int totalTime, int sets) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("workoutname", workoutname);
        contentValues.put("totalTime", totalTime);
        contentValues.put("sets", sets);
        long result = db.insert("workouts", null, contentValues);
        if (result == 1) {
            return false;
        } else return true;
    }

    public boolean insertbyrepsDefault(SQLiteDatabase db,String name, int reps,int listnumber,int workoutid){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("reps",reps);
        contentValues.put("listnumber",listnumber);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("byreps",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertbytimeDefault(SQLiteDatabase db, String name, int seconds, int listnumber,int workoutid ){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("seconds",seconds);
        contentValues.put("listnumber",listnumber);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("bytime",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertrestDefault(SQLiteDatabase db, int seconds,int listnumber,int workoutid){
        ContentValues contentValues = new ContentValues();
        contentValues.put("seconds",seconds);
        contentValues.put("listnumber",listnumber);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("rest",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertbreakDefault(SQLiteDatabase db, int seconds,int workoutid){
        ContentValues contentValues= new ContentValues();
        contentValues.put("seconds",seconds);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("break",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertbreak( int seconds,int workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("seconds",seconds);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("break",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertrest(int seconds,int listnumber,int workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("seconds",seconds);
        contentValues.put("listnumber",listnumber);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("rest",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertbytime( String name, int seconds, int listnumber,int workoutid ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("seconds",seconds);
        contentValues.put("listnumber",listnumber);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("bytime",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertbyreps(String name, int reps,int listnumber,int workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("reps",reps);
        contentValues.put("listnumber",listnumber);
        contentValues.put("workoutid",workoutid);
        long result = db.insert("byreps",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    public boolean insertWorkouts(String workoutname, int totalTime, int sets){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("workoutname",workoutname);
        contentValues.put("totalTime",totalTime);
        contentValues.put("sets",sets);
        long result = db.insert("workouts",null,contentValues);
        if(result==1){
            return false;
        }else return true;
    }

    //END INSERT

    //SELECT
    public Cursor getAllWorkouts(){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res =db.rawQuery("SELECT*FROM workouts",null);
        return res;
    }

    public Cursor getWorkoutById(String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT*FROM workouts WHERE workoutid = ?",new String[]{workoutid});
        return res;
    }

    public Cursor getByRepsByWorkoutID(String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT name,reps AS result,listnumber,'reps' AS texts FROM byreps " +
                "WHERE workoutid=? ORDER BY listnumber",new String[]{workoutid});
        return res;
    }

    public Cursor getByTimeByWorkoutID(String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery
                ("SELECT name,seconds AS result,listnumber,'seconds' AS texts  FROM bytime " +
                                "WHERE workoutid=? ORDER BY listnumber"
                        ,new String[]{workoutid});
        return res;
    }

    public Cursor getRestByWorkoutID(String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT 'Rest' as name, seconds AS result,listnumber,'seconds' AS texts  FROM rest " +
                "WHERE workoutid=? ORDER BY listnumber",new String[]{workoutid});
        return res;
    }

    public Cursor getByRepsByWorkoutIDANDListNumber(String workoutid,String listnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT name,reps AS result,listnumber,'reps ' AS texts " +
                        "FROM byreps WHERE workoutid = ? AND listnumber = ?",
                new String[]{workoutid,listnumber});
        return res;
    }

    public Cursor getByTimeByWorkoutIDANDListNumber(String workoutid,String listnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT name,seconds AS result,listnumber,'seconds' AS texts " +
                        "FROM bytime WHERE workoutid = ? AND listnumber = ?",
                new String[]{workoutid,listnumber});
        return res;
    }

    public Cursor getRestByWorkoutIDANDListNumber(String workoutid,String listnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT 'Rest' as name, seconds AS result,listnumber,'seconds' AS texts " +
                        "FROM rest WHERE workoutid = ? AND listnumber = ?",
                new String[]{workoutid,listnumber});
        return res;
    }

    public Cursor getBreakSecond (String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT seconds FROM break WHERE workoutid = ? ",new String[]{workoutid});
        return res;
    }

    public Cursor getLastInserted (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT*FROM workouts WHERE workoutid = " +
                "(SELECT (MAX(workoutid)) FROM workouts)",null);
        return res;
    }

    public Cursor getBreakByWorkoutid(String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT*FROM break WHERE workoutid=?",new String[]{workoutid});
        return res;
    }

    public Cursor getTotalWorkoutByWorkoutID(String workoutid, String workoutid2, String workoutid3){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " +
                "(SELECT COUNT(*) FROM bytime WHERE workoutid = ?)+ " +
                "(SELECT COUNT(*) FROM rest WHERE workoutid=?)+ " +
                "(SELECT COUNT(*) FROM byreps WHERE workoutid=?) ",new String[]{workoutid,workoutid2,workoutid3});
        return res;
    }
    //END SELECT

    //UPDATE
    public boolean updateLastInsertedWorkout(String workoutid, String workoutname,int TotalTime,int sets){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("workoutname",workoutname);
        contentValues.put("totalTime",TotalTime);
        contentValues.put("sets",sets);
        db.update("workouts",contentValues,"workoutid=?",new String[]{workoutid});
        return true;
    }
    public boolean UpdateByRep(String workoutid, String listnumber, String name, int reps){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("reps",reps);
        db.update("byreps",contentValues,"workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
        return true;
    }

    public boolean UpdateRest(String workoutid,String listnumber,int seconds){
        SQLiteDatabase db = this.getWritableDatabase();;
        ContentValues contentValues = new ContentValues();
        contentValues.put("seconds",seconds);
        db.update("rest",contentValues,"workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
        return true;
    }
    public boolean UpdateByTime(String workoutid,String listnumber,String name, String seconds){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("seconds",seconds);
        db.update("bytime",contentValues,"workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
        return true;
    }
    public boolean UpdateBreak (String workoutid, String seconds){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("seconds",seconds);
        db.update("break",contentValues,"workoutid=?",new String[]{workoutid});
        return true;
    }

    public boolean UpdateByRepForDelete (String workoutid, String listnumber, String newListNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("listnumber",newListNumber);
        db.update("byreps",contentValues,"workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
        return true;
    }
    public boolean UpdateByTimeForDelete (String workoutid, String listnumber, String newListNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("listnumber",newListNumber);
        db.update("bytime",contentValues,"workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
        return true;
    }
    public boolean UpdateRestForDelete (String workoutid, String listnumber, String newListNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("listnumber",newListNumber);
        db.update("rest",contentValues,"workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
        return true;
    }
    //END UPDATE

    //DELETE
    public Integer deleteUnfinishedWorkouts(String workoutid){
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete("workouts","workoutid=?",new String[]{workoutid});
    }
    public Integer deleteUnfinishedByRep (String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete("byreps","workoutid=?",new String[]{workoutid});
    }
    public Integer deleteUnfinishedByTime (String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete("bytime","workoutid=?",new String[]{workoutid});
    }
    public Integer deleteUnfinishedRest(String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete("rest","workoutid=?",new String[]{workoutid});
    }
    public Integer deleteUnfinishedBreak (String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete("break","workoutid=?",new String[]{workoutid});
    }
    public Integer deleteByRepByWorkoutid(String workoutid, String listnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("byreps","workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
    }
    public Integer deleteByTimeByWorkoutid(String workoutid, String listnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("bytime","workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
    }
    public Integer deleteRestByWorkoutid(String workoutid, String listnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("rest","workoutid=? AND listnumber=?",new String[]{workoutid,listnumber});
    }
    public Integer deleteBreakByWorkoutid(String workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("break","workoutid=?",new String[]{workoutid});
    }
    //END DELETE
}
