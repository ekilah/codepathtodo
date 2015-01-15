package com.mekilah.codepath.todo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditActivity extends ActionBarActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText et = (EditText) findViewById(R.id.etEditItemText);
        et.setText(getIntent().getStringExtra("text"));
        et.requestFocus();
        et.setSelection(et.getText().toString().length());
        this.position = getIntent().getIntExtra("pos", -1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

    public void onSave(View v){
        EditText et = (EditText) findViewById(R.id.etEditItemText);
        Intent i = new Intent();
        i.putExtra("text", et.getText().toString());
        i.putExtra("pos", this.position);
        setResult(RESULT_OK, i);
        finish();
    }
}
