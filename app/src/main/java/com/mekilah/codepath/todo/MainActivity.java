package com.mekilah.codepath.todo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private final int MY_RESULT_CODE = 42;

    ListView lvItems;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        readFile();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);

        this.setupListViewListeners();
    }

    private void setupListViewListeners(){
        this.lvItems.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeFile();
                return true;
            }
        });

        this.lvItems.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("pos", position);
                i.putExtra("text", items.get(position));
                startActivityForResult(i, MY_RESULT_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == this.MY_RESULT_CODE){
            if(resultCode == RESULT_OK){
                int position = data.getExtras().getInt("pos", -1);
                items.set(position, data.getExtras().getString("text").toString());
                itemsAdapter.notifyDataSetChanged();
                writeFile();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v){
        EditText et = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(et.getText().toString());
        et.setText("");
        writeFile();
    }

    private void readFile(){
        File file = new File(getFilesDir(), "todo.txt");

        try{
            items = new ArrayList<String>(FileUtils.readLines(file));
        }catch(Exception e){
            items = new ArrayList<String>();
        }
    }

    private void writeFile(){
        File file = new File(getFilesDir(), "todo.txt");

        try {
            FileUtils.writeLines(file, items);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
