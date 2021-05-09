package com.example.sqlite_virgiawa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private DatabaseHelper dbHelper;

    final String[] from = new String[] {dbHelper._ID, dbHelper.TITLE, dbHelper.DESC};
    final int[] to = new int[]{R.id.id, R.id.listTitle, R.id.listDesc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DatabaseManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.myListView);

        adapter = new SimpleCursorAdapter(this, R.layout.adapter, cursor, from, to, 0);
        listView.setAdapter(adapter);

        try {
            Intent intent = getIntent();
            Boolean ItemDeleted = intent.getExtras().getBoolean("Item Deleted");
            ModifyActivity modifyActivity = new ModifyActivity();
        } catch (Exception e) {
            if (adapter.isEmpty()) {
                Snackbar.make(listView, "Click on fab to add list", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(listView, "Hold on item to modify", Snackbar.LENGTH_LONG).show();
            }
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView itemID = (TextView) view.findViewById(R.id.id);
                TextView itemTitle = (TextView) view.findViewById(R.id.listTitle);
                TextView itemDesc = (TextView) view.findViewById(R.id.listDesc);

                String myId = itemID.getText().toString();
                String myTitle = itemTitle.getText().toString();
                String myDesc = itemDesc.getText().toString();

                Intent intent = new Intent(getApplicationContext(), ModifyActivity.class);
                intent.putExtra("Id", myId);
                intent.putExtra("Title", myTitle);
                intent.putExtra("Desc", myDesc);
                startActivity(intent);
                return false;
            }
        });
    }

    public void onClickAddItem(View view) {
        Intent intent = new Intent(getApplicationContext(), AddItem.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_all) {
            dbManager.dropTable();
            dbManager.open();
            Cursor cursor = dbManager.fetch();
            adapter = new SimpleCursorAdapter(this, R.layout.adapter, cursor, from, to ,0);
            listView.setAdapter(adapter);
            Snackbar.make(listView, "Database Deleted!", Snackbar.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}