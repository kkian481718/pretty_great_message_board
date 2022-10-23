package com.example.pretty_great_message_board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


public class addMessage extends AppCompatActivity {
    
    ImageView userPicture;
    EditText input_username, input_content;
    Button button_send_msg, button_cancel;
    SwitchCompat switch_anonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmessage);

        userPicture = findViewById(R.id.userPicture);
        input_username = findViewById(R.id.input_username);
        input_content = findViewById(R.id.input_content);
        button_send_msg = findViewById(R.id.button_send_msg);
        button_cancel = findViewById(R.id.button_cancel);
        switch_anonymous = findViewById(R.id.switch_anonymous);

        // btn: Back to main page
        button_cancel.setOnClickListener(v -> startActivity(new Intent(addMessage.this, messageBoard.class)));

        // btn: Sent new message
        button_send_msg.setOnClickListener(v -> {
            try {
                validate_Input();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });

    }

    private void validate_Input() throws IOException, JSONException {

        String _userName = input_username.getText().toString();
        String _content = input_content.getText().toString();

        // Avoid empty value
        if (TextUtils.isEmpty(_content)) {
            Toast.makeText(addMessage.this, "請輸入內容！", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(_userName)) {
            if (switch_anonymous.isChecked()) {
                Toast.makeText(addMessage.this, "匿名留言！", Toast.LENGTH_SHORT).show();
                save_msg_into_jsonFile(_userName, _content);
            }
            if (!(switch_anonymous.isChecked())) {
                Toast.makeText(addMessage.this, "請輸入一個名稱！", Toast.LENGTH_SHORT).show();
            }
        }

        else {
            save_msg_into_jsonFile(_userName, _content);
        }
    }

    private void save_msg_into_jsonFile(String sent_userName, String sent_content) throws JSONException {

        /** 1. create a new obj */
        Date dt = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String Time = sdf.format(dt);

        JSONObject new_card_object = new JSONObject();
        try {
            new_card_object.put("Timestamp", Time);
            if(TextUtils.isEmpty(sent_userName)) {
                new_card_object.put("Username", "匿名");
            }
            if(!TextUtils.isEmpty(sent_userName)) {
                new_card_object.put("Username", sent_userName);
            }
            new_card_object.put("Content", sent_content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // System.out.println(new_card_object);


        /** 2. save it into "card_data.json" */
        // get original data
        InputStream in = getResources().openRawResource(R.raw.card_data);
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        String line = "";
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray card_JSONarray = new JSONArray(sb.toString());
        card_JSONarray.put(new_card_object);

        // Convert JsonObject to String Format
        String card_string = card_JSONarray.toString();

        // TODO: directly upload "card_string" onto MySQL


        /** 3. return to messageBoard page */
        startActivity(new Intent(addMessage.this, messageBoard.class));

        /** 4. update online database */
        // push_data_onto_Firebase();
        // push_data_onto_MySQL();
    }

    private void push_data_onto_Firebase() {

        // TODO: push_data_onto_Firebase

        /*
        // Initial the location of data in Firebase
        // https://message-board-4181d-default-rtdb.firebaseio.com
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReferenceFromUrl("https://message-board-4181d-default-rtdb.firebaseio.com/message"); //.getReference("message");
        DatabaseReference newRef = myRef.child("Person").push();
        newRef.setValue(Card);

        // Write a message to the database
        myRef.setValue("Hello, World!")
        */
    }
    private void push_data_onto_MySQL() {}
}