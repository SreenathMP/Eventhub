package com.rubus.event_hub;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import java.util.HashMap;
import java.util.List;


public class register_student extends AppCompatActivity {

    private EditText textInputName,textInputEmail,textInputPassword,textInputConfirmPassword;
    protected AutoCompleteTextView textInputCollege;
    protected Button register_button;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);


        textInputName=findViewById(R.id.name);
        textInputCollege = findViewById(R.id.college);
        textInputEmail = findViewById(R.id.email);
        textInputPassword=findViewById(R.id.password);
        textInputConfirmPassword=findViewById(R.id.confirm_password);


        register_button=findViewById(R.id.btn_register_student);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        final ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.colleges));
        textInputCollege.setAdapter(adapter);
        textInputCollege.setThreshold(1);


        textInputCollege.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    validateCollege();

                }
            }
        });

        textInputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {


                      validateEmail();
                }
            }
        });


        textInputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {


                    validate_password();
                }
            }
        });

        textInputConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {


                    validate_confirmPassword();
                }
            }
        });


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


   if(validateName() && validateEmail() && validateCollege() && validate_password() && validate_confirmPassword()){

       withEditText(null);

   }

            }
        });

    }

    private boolean validateName(){

        String name=textInputName.getText().toString().trim();

        if (name.isEmpty()){

            textInputName.setError("Name required");

            return false;
        }

        return true;

    }

    private boolean validateCollege(){
        final String[] collArr = getResources().getStringArray(R.array.colleges);
        final List<String> collegeList = Arrays.asList(collArr);
        String collegeInput=textInputCollege.getText() + "";
        if(!collegeInput.equals("")) {
            boolean code = collegeList.contains(collegeInput);
            if(!code) {

                textInputCollege.setError("Select College From The List");
                return false;

            }
        }

        else {
            textInputCollege.setError("College Name Required");
        }

return true;
    }


    private boolean validateEmail() {
        String emailInput = textInputEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validate_password(){

        String password=textInputPassword.getText().toString().trim();

        if (password.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (password.length()<6) {
            textInputPassword.setError("password min length 6");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }


    }

    private boolean validate_confirmPassword(){

        String password=textInputPassword.getText().toString().trim();

        String confirm_password=textInputConfirmPassword.getText().toString().trim();


        if(confirm_password.isEmpty()) {
            textInputConfirmPassword.setError("Field can't be empty");
            return false;
        }

        else if(!confirm_password.equals(password)){

            textInputConfirmPassword.setError("password doesn't match");
            return false;
        }
        else
        {
            textInputConfirmPassword.setError(null);
            return true;
        }

    }




    public void withEditText(View view) {

        // AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Complete your Profile ");

        final EditText department = new EditText(this);
        final EditText course_end = new EditText(this);
        final EditText phone      = new EditText(this);

        department.setHint("Department");
        course_end.setHint("Course end(Year)");
        phone.setHint("Phone number");
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(department);
        lay.addView(course_end);
        lay.addView(phone);


        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(lay)
                .setTitle("Complete Your Profile")
                .setPositiveButton("SUBMIT", null) //Set to null. We override the onclick
                .create();



        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button submit = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                submit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        if(!department.getText().toString().equals("")&&!course_end.getText().toString().equals("") && !phone.getText().toString().equals("") && phone.getText().length()==10) {
                            dialog.dismiss();

                            register_button.setEnabled(false);
                            register_button.setClickable(false);
                            register_user(department.getText().toString(),course_end.getText().toString(),phone.getText().toString());

                        }
                     else {
                            if (department.getText().toString().equals("")) {

                                department.setError("Required Field");
                                department.requestFocus();
                            }

                            if (course_end.getText().toString().equals("")) {

                                course_end.setError("Required Field");
                                course_end.requestFocus();
                            }
                             if (phone.getText().toString().equals("")) {

                                phone.setError("Required Field");
                                phone.requestFocus();
                             }

                             if (phone.getText().toString().length()!=10)
                             {

                                 phone.setError("invalid phone number");
                                 phone.requestFocus();
                             }


                        }

                    }

                });
            }


        });

dialog.show();

    }

protected void register_user(final String department,final String course_end,final String phone_number){

        final String name=textInputName.getText().toString();
        final String college=textInputCollege.getText().toString();
        final String email=textInputEmail.getText().toString();
        final String password=textInputPassword.getText().toString();



    progressBar.setVisibility(View.VISIBLE);
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(register_student.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                   // progressBar.setVisibility(View.GONE);
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(register_student.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();

                        register_button.setEnabled(true);
                        register_button.setClickable(true);

                    } else {



                        user_student_profile users = new user_student_profile(
                                name,
                                college,
                                email,
                                department,
                                course_end,
                                phone_number
                        );

                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        HashMap<String,String> user_type=new HashMap<>();
                        user_type.put("name",name);
                        user_type.put("account","student");

                        FirebaseDatabase.getInstance().getReference("eventhub/student_profiles")
                                .child(uid)
                                .setValue(users);
                        FirebaseDatabase.getInstance().getReference("eventhub/users")
                                .child(uid)
                                .setValue(user_type);

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(register_student.this, "Registration Successfull",
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(register_student.this,login_student.class));

                        finish();
                    }
                }
            });


    }

}

