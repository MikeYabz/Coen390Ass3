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

public class DBHelperCourse extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "courses.db";
    public static final String TABLE_PRODUCTS = "courses";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_ID = "id";

    private static final String TAG = "DatabaseHelper";
    private Context context = null;

    static private int idIndex;


    public DBHelperCourse(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
        this.context = context;
    }
    public DBHelperCourse(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_CODE + " TEXT NOT NULL"
                + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
    }

    //Add new row to the database
    public  void addProduct(Course course){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, course.getTitle());
        values.put(COLUMN_CODE, course.getCode());
        //values.put(COLUMN_ID, idIndex++);
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

        /*
        db.insert("TABLE_PRODUCTS",null,values);
        db.close();
        */
    }

    public void deleteProduct(String courseCode){
        long deletedRowCount = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            deletedRowCount = db.delete(TABLE_PRODUCTS,
                    COLUMN_CODE + " = ? ",
                    new String[]{ courseCode });
        }catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        //db.execSQL("DELETE FROM" + TABLE_PRODUCTS + "WHERE" + COLUMN_PRODUCTNAME + "=\"" + name + "\";");
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public List<Course> getCourseList(){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = null;

        try{
            cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null, null);
            //String query = "SELECT * FROM" + TABLE_PRODUCTS + "WHERE 1";
            //cursor = db.rawQuery(query,null);

            if (cursor != null){
                if (cursor.moveToFirst()){
                    List<Course> courseList = new ArrayList<>();
                    do{
                        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                        String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                        String code = cursor.getString(cursor.getColumnIndex(COLUMN_CODE));
                        courseList.add(new Course(id, title, code));
                        /*
                        dbString += cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                        dbString += "\n";
                        */
                    } while (cursor.moveToNext());
                    return courseList;
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

        /*
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME))!=null){
                dbString += c.getString(c.getColumnIndex("productName"));
                dbString += "\n";
            }
        }
        */
        //c.close();
        //db.close();
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
}
