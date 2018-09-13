package com.google.williamsun.mydemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.google.williamsun.mydemo.MESSAGE";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            String[] myDataset = new String[]{"Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard", "Bulbasaur", "Ivysaur", "Venasaur", "Charizard"};
            mAdapter = new MyAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);
        }

        /** Called when the user taps the Send button */
        public void sendMessage(View view) {
//            Intent intent = new Intent(this, DisplayMessageActivity.class);
////            EditText editText = (EditText) findViewById(R.id.editText);
//            String message = editText.getText().toString();
//            intent.putExtra(EXTRA_MESSAGE, message);
//            startActivity(intent);
    }
}
