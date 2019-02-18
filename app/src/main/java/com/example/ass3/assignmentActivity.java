package com.example.ass3;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class assignmentActivity extends AppCompatActivity {

    protected Button insertCourseButton = null;
    Course course = null;
    private TextView tempInput = null;
    protected FloatingActionButton fab = null;
    private static final String TAG = "MainActivity";
    public int courseId;
    List<Assignment> Assignments = null;
    DBHelperAssignment dbHandler = null;
    private ListView assignmentList = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ActionBar ab = getSupportActionBar();   //get the Action Bar object
        ab.setDisplayHomeAsUpEnabled(true); //enable UP button, parent is declared in the manifest
        fab = findViewById(R.id.floatingActionButton);
        Bundle b = getIntent().getExtras();
        if (b != null){
            courseId = b.getInt("courseId");
            DBHelperCourse dbHandler = new DBHelperCourse(this,null,null,1);
            course = dbHandler.getCourseList().get(courseId);
            ab.setTitle(course.getTitle());   //set the title
            tempInput = (TextView) findViewById(R.id.textTest);
            tempInput.setText(course.getTitle());
        }
        assignmentList=(ListView)findViewById(R.id.AssignmentListView);
        dbHandler = new DBHelperAssignment(this,null,null,1);
        printList();
        //Button Listeners
        //Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                floatingActionButtonClicked();
            }
        });
        //End
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
                dbHandler.deleteProduct(Integer.toString(courseId));
                DBHelperCourse dbHelperCourse = new DBHelperCourse(this,null,null,1);
                dbHelperCourse.deleteProduct(course.getCode());
                //printList();
                Intent intent = new Intent(assignmentActivity.this, MainActivity.class);
                startActivity(intent);
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
        InsertAssignmentDialogFragment dialog = new InsertAssignmentDialogFragment();
        dialog.show(getSupportFragmentManager(), "Insert Assignment");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void printList(){
        Assignments = dbHandler.getAssignments(courseId);
        ArrayList<String> listCourses = new ArrayList<>();
        int i;
        for(i = 0 ; i< Assignments.size();i++){
            listCourses.add("Assignment Title: " + Assignments.get(i).getAssignmentTitle() + "\nGrade: " + Assignments.get(i).getGrade());
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listCourses);
        assignmentList.setAdapter(arrayAdapter);    }
}
