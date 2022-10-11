package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //Declare and Define all object type references
    EditText et_id, et_name, et_address, et_cno;
    Button btn_save, btn_show, btn_update, btn_delete;
    DatabaseReference dbRef;
    Student std;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Define all object type references
        et_id = findViewById(R.id.et_id);
        et_name = findViewById(R.id.et_name);
        et_address = findViewById(R.id.et_address);
        et_cno = findViewById(R.id.et_cno);

        btn_save = findViewById(R.id.btn_save);
        btn_show = findViewById(R.id.btn_show);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);

        std = new Student();

        //Set on click listener to the “SAVE” button
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Student");

                try {
                    if (TextUtils.isEmpty(et_id.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter an ID", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(et_name.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter a Name", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(et_address.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter an address", Toast.LENGTH_SHORT).show();
                    else{
                        //Take inputs from the user and assign them to the instance(Std5) of the Student
                        std.setId(et_id.getText().toString().trim());
                        std.setName(et_name.getText().toString().trim());
                        std.setAddress(et_address.getText().toString().trim());
                        std.setConNo(Integer.parseInt(et_cno.getText().toString().trim()));

                        //dbRef.push().setValue(std); .//Insert into the database
                        dbRef.child("std5").setValue(std); //Student object -std5 with our own key

                        //Successful feedback to the user
                        Toast.makeText(getApplicationContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                }catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_SHORT).show();
                }
            }
        });




        //Show
        //Set onclick listener to the “SHOW” button
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("Student").child("std5");
                readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            et_id.setText(snapshot.child("id").getValue().toString());
                            et_name.setText(snapshot.child("name").getValue().toString());
                            et_address.setText(snapshot.child("address").getValue().toString());
                            et_cno.setText(snapshot.child("conNo").getValue().toString());


                        }else{
                            Toast.makeText(getApplicationContext(), "No source to display", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        //update
        //Set onclick listener to the “UPDATE” button
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference updRef = FirebaseDatabase.getInstance().getReference().child("Student");
                updRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("std5")){
                            try {
                                std.setId(et_id.getText().toString().trim());
                                std.setName(et_name.getText().toString().trim());
                                std.setAddress(et_address.getText().toString().trim());
                                std.setConNo(Integer.parseInt(et_cno.getText().toString().trim()));

                                dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("std5");
                                dbRef.setValue(std);
                                clearControls();
                                Toast.makeText(getApplicationContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();


                            }catch(NumberFormatException e){
                                Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "No source to update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });




        //Delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Student");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("std5")){
                            dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("std5");
                            dbRef.removeValue();
                            clearControls();
                            Toast.makeText(getApplicationContext(), "Data deleted successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "No source to delete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    //Method to clear user inputs
    private void clearControls(){
        et_id.setText("");
        et_name.setText("");
        et_address.setText("");
        et_cno.setText("");

    }
}