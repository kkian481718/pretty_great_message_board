package com.example.pretty_great_message_board;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Card> card_list;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the recycleView
        initData();
        initView();

        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, addMessage.class));
            }
        });

        // Read from the database
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

    private void initData() {

        // TODO: 2022/10/14 之後要在這裡加上 firebase .getData() 之類的東西

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        // TODO: 2022/10/15 fake data
        card_list = new ArrayList<>();
        long Time = new Date().getTime();
        for (int i = 0; i <= 20; i++) {

            Card card = new Card();

            card.setId("# " + (i+1));
            card.setTimestamp(Time + "");
            card.setContent("：）");

            card_list.add(card);
        }


        /*
        // Check if it's null
        for (int newRefIndex = 0; true; newRefIndex++) {

            DatabaseReference newRef = myRef.child(newRefIndex + "");
            if (!newRef.exists())
                break;
            else {
                Card card = new Card();

                card.setId("# " + newRef.getValue().toString());
                card.setTimestamp(newRef.child("timestamp").getValue().toString());

                card_list.add(card);
            }
        }
        */
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.rv_card);
        CardAdapter cardAdapter = new CardAdapter(card_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cardAdapter);
    }
}