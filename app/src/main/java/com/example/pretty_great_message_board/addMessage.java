package com.example.pretty_great_message_board;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class addMessage extends AppCompatActivity {
    
    ImageView userPicture;
    EditText editTextTextPersonName, input_content;
    Button button_send_msg, button_cancel;
    SwitchCompat switch_anonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmessage);

        userPicture = findViewById(R.id.userPicture);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        input_content = findViewById(R.id.input_content);
        button_send_msg = findViewById(R.id.button_send_msg);
        button_cancel = findViewById(R.id.button_cancel);

        // btn: Back to main page
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addMessage.this, MainActivity.class));
            }
        });

        // btn: Sent new message
        button_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_Input();
            }
        });

    }

    private void validate_Input() {

        String _userName = editTextTextPersonName.getText().toString();
        String _content = input_content.getText().toString();

        // Avoid empty value
        if ( TextUtils.isEmpty(_userName) && !(switch_anonymous.isChecked()) )
            Toast.makeText(addMessage.this, "請輸入一個名稱！", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(_content))
            Toast.makeText(addMessage.this, "請輸入內容！", Toast.LENGTH_SHORT).show();
        else
            sendMsg(_userName, _content);

    }

    private void sendMsg(String sent_userName, String sent_content) {

        long Time = new Date().getTime();

        Card card = new Card();


        /*
        card.setId("# " + (i + 1));

        if (i % 2 == 1){
            card.setState("線上");
        }else{
            card.setState("離線");
        }
        */

        // > Initial the location of data in Firebase
        // https://message-board-4181d-default-rtdb.firebaseio.com
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReferenceFromUrl("https://message-board-4181d-default-rtdb.firebaseio.com/message"); //.getReference("message");
        DatabaseReference newRef = myRef.child("Person").push();
        // newRef.setValue(Card);


        // > Write a message to the database
        // myRef.setValue("Hello, World!")

    }
}