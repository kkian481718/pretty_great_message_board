package com.example.pretty_great_message_board;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class messageBoard extends AppCompatActivity {

    private List<Card> card_list;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_board);

        // Btn: go to "addMessage"
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(messageBoard.this, addMessage.class));
            }
        });

        // initialize the recycleView
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

        //Do your code here
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData() throws IOException, JSONException {

        /** 0. get data from MySQL or Firebase */
        get_data_from_mySQL();
        //get_data_from_firebase();


        /** 1. read data from "card_data.json" */
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


        /** 2. transform the data into (list)card_list */
        card_list = new ArrayList<>();

        for (int i = (card_JSONarray.length() - 1); i >= 0; i--) {

            Card card = new Card();
            JSONObject card_new_obj = card_JSONarray.getJSONObject(i);

            card.setId("# " + (i+1));
            card.setTimestamp("Time |  " + card_new_obj.getString("Timestamp"));
            card.setUsername("@" + card_new_obj.getString("Username"));
            card.setContent(card_new_obj.getString("Content"));

            card_list.add(card);
        }


        /** 3. print it onto RecycleView. */
        initView();
    }

    // plan A：mySQL
    private void get_data_from_mySQL() {

        // TODO: 之後在這裡加上從mySQL抓資料的副程式

        /*
            [ what should this block do? ]
            1. download data from MySQL
            2. save data into "card_data.json"
        */
    }
    // plan B: Firebase (not finished yet)
    private void get_data_from_firebase() {

        // Read data from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // String value = (String) dataSnapshot.getValue();
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.rv_card);
        CardAdapter cardAdapter = new CardAdapter(card_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cardAdapter);
    }
}