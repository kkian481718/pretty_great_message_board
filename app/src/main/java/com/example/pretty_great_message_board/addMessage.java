package com.example.pretty_great_message_board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class addMessage extends AppCompatActivity {

    // TextView xxxname;
    ImageView userPicture;
    EditText editTextTextPersonName, input_title, input_content;
    Button button_send_msg, button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmessage);

        userPicture = findViewById(R.id.userPicture);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        input_title = findViewById(R.id.input_title);
        input_content = findViewById(R.id.input_content);
        button_send_msg = findViewById(R.id.button_send_msg);
        button_cancel = findViewById(R.id.button_cancel);

        /*
        // https://message-board-4181d-default-rtdb.firebaseio.com
        // Write a message to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        */


        // btn: Back to main page
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addMessage.this, MainActivity.class));
            }
        });

    }

    private void sendMsg() {

        String userName = editTextTextPersonName.getText().toString();
        String title = input_title.getText().toString();
        String msg = input_content.getText().toString();
        long Time = new Date().getTime();

        // String key = reference.push().getkey();


    }
}