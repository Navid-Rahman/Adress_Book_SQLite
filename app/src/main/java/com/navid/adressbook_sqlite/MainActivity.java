package com.navid.adressbook_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;

    Button add_data;
    EditText add_name, add_email, add_number;

    ListView userList;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        listItem = new ArrayList<>();

        add_data = findViewById(R.id.add_data);
        add_name = findViewById(R.id.add_name);
        add_email = findViewById(R.id.add_email);
        add_number = findViewById(R.id.add_number);
        userList = findViewById(R.id.users_list);

        viewData();

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = userList.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, ""+text, Toast.LENGTH_SHORT).show();
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = add_name.getText().toString();
                String phone = add_number.getText().toString();
                String email = add_email.getText().toString();

                if(!name.equals("") && !email.equals("") && !phone.equals("") && db.insertData(name, email, phone)){
                    Toast.makeText(MainActivity.this, "Data added", Toast.LENGTH_SHORT).show();

                    add_name.setText("");
                    add_number.setText("");
                    add_email.setText("");

                    listItem.clear();
                    viewData();
                }
                else{
                    Toast.makeText(MainActivity.this, "Data not added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewData() {
        Cursor cursor = db.viewData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                listItem.add(cursor.getString(1));//index 1 is name, index 0 is id
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            userList.setAdapter(adapter);
        }
    }
}