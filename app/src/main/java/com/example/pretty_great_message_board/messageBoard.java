package com.example.pretty_great_message_board;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class messageBoard extends AppCompatActivity {

    List<Card> card_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_board);

        // Btn: go to "addMessage"
        Button button_add = findViewById(R.id.button);
        button_add.setOnClickListener(v -> startActivity(new Intent(messageBoard.this, addMessage.class)));

        // initialize the recycleView
        initData();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

        // initialize the recycleView
        initData();
    }

    private void initData() {
        new Async().execute(); // update RecycleView.
        try {
            Thread.sleep(2000);
            // TODO: 等待兩秒讓資料載入，如果在Thread裡面呼叫initView，都會報錯
            initView();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.rv_card);
        CardAdapter cardAdapter = new CardAdapter(card_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cardAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    class Async extends AsyncTask<Void, Void, Void> {

        String error = "";

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                /** INFO of message_board(table)
                 * [variables]
                 *  Timestamp,  type = timestamp
                 *  Username,   type = char
                 *  Content,    type = text
                 *
                 * [How did I create it?]
                 *  String sql = "CREATE TABLE message_board (Timestamp timestamp, Username char(50), Content text)";
                 *  stmt.executeUpdate(sql);
                 */

                // 1. open MySql
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://tww.sytes.net:3306/users", "ptest", "usertest00");

                // 2. Download data
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM message_board");

                // 3. create list
                card_list = new ArrayList<>();
                int card_counter = 0;

                while (resultSet.next()) {
                    Card card = new Card();
                    card_counter++;

                    card.setId("# " + card_counter);
                    card.setTimestamp("Time |  " + resultSet.getString(1));
                    card.setUsername("@" + resultSet.getString(2));
                    card.setContent(resultSet.getString(3));

                    card_list.add(card);
                }
            } catch (Exception e) {
                error = e.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (error != "") {
                System.out.println(error);
            }
            super.onPostExecute(aVoid);
        }
    }

}