package com.rubus.event_hub;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class create_event extends AppCompatActivity {


    ImageView banner_thumbnail;
    Button btn_upload,btn_create_event ;
    EditText event_title,college,description,venue,phone_number,website;
    TextView date_1,date_2,time_1;
    ImageButton btn_calender_1,btn_calender_2,btn_time_1;
    AwesomeSpinner my_spinner;
    ProgressBar progressBar;


    protected String  amPm,userUid,email;
    private static int RESULT_LOAD_IMG = 71;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("eventhub/student_profiles");


    DatabaseReference eventRef = database.getReference("eventhub/events");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

         my_spinner= (AwesomeSpinner) findViewById(R.id.category);
        findViewById(R.id.dummyfocus).setFocusableInTouchMode(true);
        findViewById(R.id.dummyfocus).requestFocus();
       progressBar =findViewById(R.id.progressbar);
        btn_upload=findViewById(R.id.btn_upload);
        banner_thumbnail=findViewById(R.id.banner_thumbnail);
        event_title=findViewById(R.id.event_title);
        college=findViewById(R.id.college);
        description=findViewById(R.id.description);
        venue=findViewById(R.id.venue);
        date_1=findViewById(R.id.date1);
        date_2=findViewById(R.id.date2);
        time_1=findViewById(R.id.time1);
        btn_calender_1=findViewById(R.id.calender1);
        btn_calender_2=findViewById(R.id.calender2);
        btn_time_1=findViewById(R.id.clock1);
        phone_number=findViewById(R.id.phone_number);
        website=findViewById(R.id.website);
        btn_create_event=findViewById(R.id.btn_create_event);

        Query query;
       userUid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                query = myRef.child(userUid);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_profile profile = dataSnapshot.getValue(user_profile.class);
                        college.setText(profile.getCollege());
                        email=profile.getEmail();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        List<String> categories = new ArrayList<String>();
        categories.add("Arts");
        categories.add("Tech Fest");
        categories.add("Workshop");
        categories.add("Placement");
        categories.add("Admission");


        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        my_spinner.setAdapter(categoriesAdapter);


        my_spinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                //TODO YOUR ACTIONS

                /*
                if(my_spinner.isSelected()){
                   category_spinner(my_spinner.getSelectedItem());
                }

*/

            }
        });



  btn_upload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


          Intent intent = new Intent();
          intent.setType("image/*");
          intent.setAction(Intent.ACTION_GET_CONTENT);
          startActivityForResult(Intent.createChooser(intent, "Select Banner"), RESULT_LOAD_IMG);

      }
  });


       // date_from=(TextView)findViewById(R.id.date_from);
        //date_to=(TextView)findViewById(R.id.date_to);
        //displayDate=(ImageButton) findViewById(R.id.imageButton);
        //displayTime=(ImageButton)findViewById(R.id.button2);

btn_calender_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

showcalender("from");


    }
});

        btn_calender_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcalender("to");


            }
        });


        btn_time_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTime();

            }
        });







        btn_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_create_event.setEnabled(false);
                btn_create_event.setClickable(false);

              validate_fields();

            }
        });



}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            Picasso.with(this).load(filePath).into(banner_thumbnail);
        }
    }

    private void uploadImage(final String key) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Banner Image .....");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            final StorageReference ref = storageReference.child("BannerImages/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> imgurl = new HashMap<>();
                                    imgurl.put("bannerUrl", uri.toString());

                                    eventRef.child(key).updateChildren(imgurl);

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(create_event.this, "Event created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(create_event.this,MainActivity.class));
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(create_event.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading ...... "+(int)progress+"%");
                        }
                    });
        }
        else {
            Toast.makeText(create_event.this, "Event posted Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(create_event.this,MainActivity.class));
            finish();
        }
    }

    void showcalender(final String cal){



    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {


                    if (cal.equals("from"))
                          date_1.setText(dayOfMonth + "-" + (MONTHS[monthOfYear]) + "-" + year);

                    else
                        date_2.setText(dayOfMonth + "-" + (MONTHS[monthOfYear]) + "-" + year);


                }
            }, mYear, mMonth, mDay);

    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
    datePickerDialog.show();





}

void showTime(){



    final Calendar c = Calendar.getInstance();
   int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);

    // Launch Time Picker Dialog
    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {


                    if (hourOfDay > 12) {
                        hourOfDay -= 12;
                        amPm = " PM";
                    } else if (hourOfDay == 0) {
                        hourOfDay += 12;
                        amPm = " AM";
                    } else if (hourOfDay == 12)
                        amPm = " PM";
                    else
                        amPm = " AM";
                    time_1.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);

                }
            }, mHour, mMinute, false);
    timePickerDialog.show();
}


void validate_fields(){

String event_title,college,description,category,venue,date1,date2,time1,phone_number,website="www.xyz.com";

event_title=this.event_title.getText().toString();
college=this.college.getText().toString();
description=this.description.getText().toString();
venue=this.venue.getText().toString();
date1=this.date_1.getText().toString();
date2=this.date_2.getText().toString();
time1=this.time_1.getText().toString();
phone_number=this.phone_number.getText().toString();
website=this.website.getText().toString();

if(website.isEmpty())
    website="----";

if(this.my_spinner.isSelected()){

    category=my_spinner.getSelectedItem();
}

else {

    category="";
}

if(!event_title.isEmpty() && !description.isEmpty() && !category.equals("") && !venue.isEmpty() && !phone_number.isEmpty() && phone_number.length()==10){


    if(date1.isEmpty() || date2.isEmpty() || time1.isEmpty())
    {
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Oops...")
                .setContentText("Set Date And Time Correctly")
                .show();
        btn_create_event.setEnabled(true);
        btn_create_event.setClickable(true);
    }

    else
        publish_event(event_title,college,description,category,venue,date1,date2,time1,phone_number,email,website);

}

else {

    btn_create_event.setEnabled(true);
    btn_create_event.setClickable(true);

  if(event_title.isEmpty())
  {
      this.event_title.setError("Title required");
      this.event_title.requestFocus();
  }

    if(description.isEmpty())
    {
        this.description.setError("Description required");
        this.description.requestFocus();
    }


    if (venue.isEmpty())
    {
        this.venue.setError("Venue required");
        this.venue.requestFocus();
    }

    if(category.equals("")){

        this.my_spinner.setHintTextColor(Color.RED);
    }




    if (phone_number.isEmpty()) {

        this.phone_number.setError("Phone number required");
        this.phone_number.requestFocus();
    }

    if (phone_number.length()!=10)
    {

       this.phone_number.setError("invalid phone number");
        this.phone_number.requestFocus();
    }

}

  }




    void publish_event(String event_title,String college_name,String description,String category,String venue,String date1,String date2,String time1,String phone_number,String email,String website) {

        progressBar.setVisibility(View.VISIBLE);
        boolean status = false;
        String publisher = userUid;
        String bannerUrl="0";
        events event_data = new events(
                event_title,
                college_name,
                description,
                category,
                venue,
                date1,
                date2,
                time1,
                phone_number,
                status,
                publisher,
                bannerUrl,
                email,
                website,
                null

        );



         final String key=eventRef.push().getKey();
        eventRef.child(key)
                .setValue(event_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {



                HashMap<String,Object> postId=new HashMap<>();
                postId.put("postId",key);

                eventRef.child(key).updateChildren(postId);
                progressBar.setVisibility(View.INVISIBLE);

                    uploadImage(key);



            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btn_create_event.setEnabled(true);
                btn_create_event.setClickable(true);
            }
        });
    }

    }









