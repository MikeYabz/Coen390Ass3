package com.example.ass3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelperAssignment extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "assignments.db";
    public static final String TABLE_PRODUCTS = "assignment";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GRADE = "grade";
    public static final String COLUMN_COURSE_ID = "idCourse";
    public static final String COLUMN_ASSIGNMENT_ID = "idAss";

    private static final String TAG = "DatabaseHelper";
    private Context context = null;

    static private int idIndex;


    public DBHelperAssignment(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
        this.context = context;
    }
    public DBHelperAssignment(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_COURSE_ID + " INTEGER NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_GRADE + " INTEGER NOT NULL"
                + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
    }

    //Add new row to the database
    public  void addProduct(Assignment assignment){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, assignment.getAssignmentTitle());
        values.put(COLUMN_COURSE_ID, assignment.getCourseId());
        values.put(COLUMN_GRADE, assignment.getGrade());
        //values.put(COLUMN_ASSIGNMENT_ID, assignment.getAssignmentId());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;
        try {
            id = db.insertOrThrow(TABLE_PRODUCTS, null, values);
            Toast.makeText(context, "Operation Success" , Toast.LENGTH_LONG).show();
        } catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    public void deleteProduct(String courseID){
        long deletedRowCount = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            deletedRowCount = db.delete(TABLE_PRODUCTS,
                    COLUMN_COURSE_ID + " = ? ",
                    new String[]{ courseID });
        }catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public List<Assignment> getAssignmentList(){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = null;

        try{
            cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null, null);
            //String query = "SELECT * FROM" + TABLE_PRODUCTS + "WHERE 1";
            //cursor = db.rawQuery(query,null);

            if (cursor != null){
                if (cursor.moveToFirst()){
                    List<Assignment> assignmentList = new ArrayList<>();
                    do{
                        int courseId = cursor.getInt(cursor.getColumnIndex(COLUMN_COURSE_ID));
                        int assignmentId = cursor.getInt(cursor.getColumnIndex(COLUMN_ASSIGNMENT_ID));
                        String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                        int grade = cursor.getInt(cursor.getColumnIndex(COLUMN_GRADE));
                        assignmentList.add(new Assignment(assignmentId,courseId,title,grade));
                        /*
                        dbString += cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                        dbString += "\n";
                        */
                    } while (cursor.moveToNext());
                    return assignmentList;
                }
            }
        } catch (Exception e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return new ArrayList<>();
    }

    public void clearDatabase(){
        long deletedRowCount = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            deletedRowCount = db.delete(TABLE_PRODUCTS,
                    null,null);
        }catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public List<Assignment> getAssignments(int courseId){
        List<Assignment> assignmentsInput = getAssignmentList();
        List<Assignment> assignmentsOutput = new ArrayList<>();
        for(int i=0;i<assignmentsInput.size();i++){
            if(assignmentsInput.get(i).getCourseId() == courseId){
                assignmentsOutput.add(assignmentsInput.get(i));
            }
        }
        return assignmentsOutput;
    }
}
