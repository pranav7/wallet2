package com.example.wallet2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends FragmentActivity
{
        
        EditText searchBox;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                searchBox = (EditText)findViewById(R.id.search_box);
        }
        
        /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }
    
    @Override
    protected void onResume()
    {
            super.onResume();
//            Toast.makeText(this, "Resumed", Toast.LENGTH_LONG).show();
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }
        
        
        
        
        public void showMap(View v)
        {
                Intent intent = new Intent(this, DisplayMapActivity.class);
                startActivity(intent);
        }
        
        public void performSearch(View v)
        {
                Intent intent = new Intent(this, SearchResultActivity.class);
                String searchQuery = searchBox.getText().toString();
                intent.putExtra(Constants.SEARCH_QUERY, searchQuery);
                startActivity(intent);
        }
}