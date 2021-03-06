package com.example.sqlite_virgiawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ModifyActivity extends AppCompatActivity {
    private EditText modTitle;
    private EditText modDesc;
    private long id;
    private boolean isItemDeleted = false;

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbManager = new DatabaseManager(this);
        dbManager.open();

        modTitle = (EditText) findViewById(R.id.modTitle);
        modDesc = (EditText) findViewById(R.id.modDesc);

        Bundle intentData = getIntent().getExtras();

        final String myID = intentData.getString("Id");
        final String myTitle = intentData.getString("Title");
        final String myDesc = intentData.getString("Desc");

        modTitle.setText(myTitle);
        modDesc.setText(myDesc);
        id = Long.parseLong(myID);

        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        FloatingActionButton fabUpdate = (FloatingActionButton) findViewById(R.id.fabUpdate);

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.delete(Integer.parseInt(myID));
                setItemDeleted(true);
                returnHome();
            }
        });

        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = modTitle.getText().toString();
                String newDesc = modDesc.getText().toString();
                dbManager.update(Integer.parseInt(myID), newTitle, newDesc);
                returnHome();
            }
        });
    }

    public void returnHome() {
        Intent intent = new Intent (getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (isItemDeleted) {
            intent.putExtra("ItemDeleted", true);
        }
        startActivity(intent);
    }

    public void setItemDeleted(boolean itemDeleted) {
        isItemDeleted = itemDeleted;
    }
}