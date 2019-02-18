package com.example.ass3;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertAssignmentDialogFragment extends DialogFragment {

    private static final String TAG = "InsertCourseDialog";
    EditText titleEditText;
    EditText gradeEditText;
    Button cancelButton;
    Button saveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflator.inflate(R.layout.fragment_insert_assignment_dialog, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        gradeEditText = view.findViewById(R.id.gradeEditText);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick: cancel button");
                getDialog().dismiss();
            }
        });


        saveButton.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String gradeString = gradeEditText.getText().toString();
                int grade;
                if (gradeString.equals("")){
                    Toast.makeText(getActivity(), "No Grade", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(title.equals("")){
                    Toast.makeText(getActivity(), "No Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    grade = Integer.parseInt(gradeString);
                }
                if (grade>100)
                {
                    Toast.makeText(getActivity(), "Grade can't be over 100", Toast.LENGTH_SHORT).show();
                    return;
                }
                DBHelperAssignment dbhelper = new DBHelperAssignment(getActivity());
                dbhelper.addProduct(new Assignment(-1,((assignmentActivity)getActivity()).courseId,title,grade)); //(tempInput.getText().toString());
                //((MainActivity)getActivity()).setOutput();
                ((assignmentActivity)getActivity()).printList();
                getDialog().dismiss();


            }
        });

        return view;
    }


}
