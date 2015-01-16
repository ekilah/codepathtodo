package com.mekilah.codepath.todo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.zip.Inflater;


public class MainActivity extends ActionBarActivity implements AddOrEditFragment.AddOrEditFragmentAcceptedListener {
    private final int MY_RESULT_CODE = 42;

    ListView lvItems;
    ArrayList<AddOrEditFragment.AddOrEditFragmentData> items;
    TodoListDataAdapter itemsAdapter;

    public class TodoListDataAdapter extends ArrayAdapter<AddOrEditFragment.AddOrEditFragmentData>{

        private class ViewHolder{
            TextView tvMaintext;
            TextView tvSubtext;
        }

        public TodoListDataAdapter(Context context, List<AddOrEditFragment.AddOrEditFragmentData> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AddOrEditFragment.AddOrEditFragmentData data = getItem(position);
            ViewHolder viewHolder;

            if(convertView==null){
                convertView =  LayoutInflater.from(getContext()).inflate(R.layout.item_add_or_edit, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvMaintext = (TextView) convertView.findViewById(R.id.tvItemMaintext);
                viewHolder.tvSubtext = (TextView) convertView.findViewById(R.id.tvItemSubtext);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvSubtext.setText(data.dateString);
            viewHolder.tvMaintext.setText(data.name);

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        readFile();
        itemsAdapter = new TodoListDataAdapter(this, items);

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
                FragmentManager fragmentManager = getSupportFragmentManager();
                AddOrEditFragment addFragment = AddOrEditFragment.newInstance("Edit Todo item", items.get(position), position);
                addFragment.show(fragmentManager, "fragment_edit");
            }
        });
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddOrEditFragment addFragment = AddOrEditFragment.newInstance("Add new Todo item");
        addFragment.show(fragmentManager, "fragment_add");
    }

    private void readFile(){
        File file = new File(getFilesDir(), "todo.txt");

        try{
            ArrayList<String> strings = new ArrayList<String>(FileUtils.readLines(file));
            items = new ArrayList<AddOrEditFragment.AddOrEditFragmentData>();
            for(String s : strings){
                int pos = s.lastIndexOf(" ");
                AddOrEditFragment.AddOrEditFragmentData data = new AddOrEditFragment.AddOrEditFragmentData();
                boolean success = data.setDateFromString(s.substring(pos+1 /*don't include space*/));

                if(!success){
                    throw new InputMismatchException("String did not parse properly: " + s);
                }

                data.name = s.substring(0, pos);
                items.add(data);
            }

        }catch(Exception e){
            e.printStackTrace();
            Log.e("err", "file read failed.", e);
            items = new ArrayList<AddOrEditFragment.AddOrEditFragmentData>();
            writeFile(); //delete bad file contents on bad read
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

    @Override
    public void onAcceptPressedInAddOrEditFragment(AddOrEditFragment.AddOrEditFragmentData data, int position) {
        if(position > -1 && position < items.size()){
            items.set(position, data);
            itemsAdapter.notifyDataSetChanged();
        }else{
            if(position != -1){
                Log.e("mainactivity", "bad position passed back from AddOrEditFragment: " + position + ", writing new item instead.");
            }
            itemsAdapter.add(data);
        }
        writeFile();
    }
}
