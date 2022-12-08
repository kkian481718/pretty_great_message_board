package com.example.pretty_great_message_board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import org.json.JSONException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;


public class addMessage extends AppCompatActivity {
    
    ImageView userPicture;
    EditText input_username, input_content;
    Button button_send_msg, button_cancel;
    SwitchCompat switch_anonymous;
    TextView errorText;

    String Username = "";
    String Content = "";
    String Timestamp = "";

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
        errorText = findViewById(R.id.errorText);

        // btn: Back to main page
        button_cancel.setOnClickListener(v -> startActivity(new Intent(addMessage.this, messageBoard.class)));

        // btn: Sent new message
        button_send_msg.setOnClickListener(v -> {
            try {
                validate_Input();
                // push_data_onto_MySQL();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });

    }

    private void validate_Input() throws IOException, JSONException {

        Username = input_username.getText().toString();
        Content = input_content.getText().toString();

        // Avoid empty value
        if (TextUtils.isEmpty(Content))
        {
            Toast.makeText(addMessage.this, "請輸入內容！", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Username) && !(switch_anonymous.isChecked()))
        {
            Toast.makeText(addMessage.this, "請輸入一個名稱！", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (switch_anonymous.isChecked()) Toast.makeText(addMessage.this, "匿名留言！", Toast.LENGTH_SHORT).show();

            // save_msg_into_jsonFile(_userName, _content);
            push_data_onto_MySQL(); /** sent data */
        }
    }

    private void push_data_onto_MySQL() {

        Date dt = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Timestamp = sdf.format(dt);

        new Async().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class Async extends AsyncTask<Void, Void, Void> {

        String records = "", error = "";

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

                // 2. Upload data
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO message_board VALUES(?, ?, ?)");
                stmt.setString(1, Timestamp);
                stmt.setString(2, Username);
                stmt.setString(3, Content);
                stmt.executeUpdate();
                stmt.clearParameters();

                // 3. Back to Main page
                startActivity(new Intent(addMessage.this, messageBoard.class));


            } catch (Exception e) {
                error = e.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (error != "") {
                errorText.setText(error);
                System.out.println(error);
                // Toast.makeText(addMessage.this, error, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }
    }

    /*
    private void save_msg_into_jsonFile(String sent_userName, String sent_content) throws JSONException {

        // 1. create a new obj
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


        // 2. save it into "card_data.json"
        // get original data
        InputStream in = getResources().openRawResource(R.raw.card_data);
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        String line;
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

        // 3. return to messageBoard page
        startActivity(new Intent(addMessage.this, messageBoard.class));

        // 4. update online database
        push_data_onto_MySQL();
    }
    */
}

