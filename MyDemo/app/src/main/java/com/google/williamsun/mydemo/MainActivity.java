package com.google.williamsun.mydemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.google.williamsun.mydemo.MESSAGE";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final String POKEAPI_BASE_URL = "https://pokeapi.co";
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

            new PokemonDataTask().execute(POKEAPI_BASE_URL + "/api/v1/sprite/?limit=40&offset=1");
        }

        /** Called when the user taps the Send button */
        public void sendMessage(View view) {
//            Intent intent = new Intent(this, DisplayMessageActivity.class);
////            EditText editText = (EditText) findViewById(R.id.editText);
//            String message = editText.getText().toString();
//            intent.putExtra(EXTRA_MESSAGE, message);
//            startActivity(intent);
    }

    private class PokemonDataTask extends AsyncTask<String, Void, PokemonEntryData[]> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected PokemonEntryData[] doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                String response = buffer.toString();

                try {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                    JSONArray objects = jsonObject.getJSONArray("objects");
                    PokemonEntryData[] entryData = new PokemonEntryData[objects.length()];
                    for(int i = 0; i < objects.length(); i++) {
                        JSONObject entry = objects.getJSONObject(i);
                        entryData[i] = new PokemonEntryData(entry.getJSONObject("pokemon").getString("name"), "/media/img/" + entry.getInt("id") + ".png");
                    }
                    return entryData;

                } catch (JSONException e) {
                    Log.e("Invalid JSON", response);
                }

                return null;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(PokemonEntryData[] result) {
            super.onPostExecute(result);
            mAdapter = new MyAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
