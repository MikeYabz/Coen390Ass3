package com.example.ass3;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView courseList = null;
    private TextView courseAverage = null;
    DBHelperCourse dbHandler = null;

    List<Course> Courses = null;
    protected FloatingActionButton fab = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();   //get the Action Bar object
        //ab.setDisplayHomeAsUpEnabled(true); //enable UP button, parent is declared in the manifest
        ab.setTitle("Course Viewer");
        ab.setSubtitle("By Michael Yablonovitch");

            //Init Layout Elements
        fab = findViewById(R.id.floatingActionButton);
        courseAverage = (TextView) findViewById(R.id.courseAverage);
        courseList = (ListView) findViewById(R.id.CourseList);
            //End

            //Init Database
        dbHandler = new DBHelperCourse(this,null,null,1);
        //dbHandler.deleteProduct("mike");
        printList();
            //End

            //Button Listeners
                //Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                floatingActionButtonClicked();
            }
        });
                //End

                //List View Click
        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                listCourseClicked(position);
            }
        });
                //End
            //End
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        printList();
    }

    //Creates ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_database,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handles action button
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                dbHandler.clearDatabase();  //delete the courses database
                printList();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    private void floatingActionButtonClicked(){
        /*InsertCourseDialogFragment dialog = new InsertCourseDialogFragment();
        dialog.show(getSupportFragmentManager(),TAG);*/
        Log.d(TAG, "floating button onclick");
        InsertCourseDialogFragment dialog = new InsertCourseDialogFragment();
        dialog.show(getSupportFragmentManager(), "Insert Course");
    }

    private void listCourseClicked(int index){
        Intent intent = new Intent(MainActivity.this, assignmentActivity.class);
        Bundle b = new Bundle();
        b.putInt("courseId", index); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setOutput(){
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void printList(){
        Courses = dbHandler.getCourseList();
        DBHelperAssignment dbhelperAssignments = new DBHelperAssignment(this);
        List<Assignment> Assignments;


        ArrayList<String> listCourses = new ArrayList<>();
        //arrayAdapter.add(dbHandler.databaseToString());
        int overallAverage = 0;
        int counter_for_how_many_courses_actually_have_assignments = 0;
        int i = 0;
        for(i = 0 ; i< Courses.size();i++){
            Assignments = dbhelperAssignments.getAssignments(i);
            boolean notApplicableFlag = false;
            int average = 0;
            for(int j=0;j<Assignments.size();j++){
                average += Assignments.get(j).getGrade();
            }
            if (average>0){
                average /= Assignments.size();
            }
            if(Assignments.size()>0){
                counter_for_how_many_courses_actually_have_assignments++;
            }
            else{
                notApplicableFlag = true;
            }
            overallAverage+=average;
            if(notApplicableFlag == false){
                listCourses.add(Courses.get(i).getTitle() + "\n" + Courses.get(i).getCode() + "\n\nAssignment Average: " + Integer.toString(average));
            }
            else{
                listCourses.add(Courses.get(i).getTitle() + "\n" + Courses.get(i).getCode() + "\n\nAssignment Average: " + "N/A");
            }
        }
        if (overallAverage>0){
            overallAverage/= counter_for_how_many_courses_actually_have_assignments;
        }
        if(counter_for_how_many_courses_actually_have_assignments > 0){
            courseAverage.setText("Course Average: " + overallAverage);
        }
        else{
            courseAverage.setText("Course Average: " + "N/A");
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listCourses);
        courseList.setAdapter(arrayAdapter);
    }
}
