package com.example.ass3;

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

public class InsertCourseDialogFragment extends DialogFragment {

    private static final String TAG = "InsertCourseDialog";
    EditText titleEditText;
    EditText codeEditText;
    Button cancelButton;
    Button saveButton;
    //DBHelperCourse dbHandler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflator.inflate(R.layout.fragment_insert_course_dialog, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        codeEditText = view.findViewById(R.id.gradeEditText);
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
                String code = codeEditText.getText().toString();
                if(title.equals("")) {
                    Toast.makeText(getActivity(), "No Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(code.equals("")) {
                    Toast.makeText(getActivity(), "No Course Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHelperCourse dbhelper = new DBHelperCourse(getActivity());
                dbhelper.addProduct(new Course(-1,title,code)); //(tempInput.getText().toString());
                //((MainActivity)getActivity()).setOutput();
                ((MainActivity)getActivity()).printList();
                //(MainActivity)getActivity()).loadListView();
                getDialog().dismiss();
                getDialog().dismiss();

            }
        });

        return view;
    }

}
