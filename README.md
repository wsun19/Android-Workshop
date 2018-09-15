# Android Workshop
Google's Android Workshop for HopHacks Fall 2018

# Introduction
A workshop for learning Android by doing :)

## Contacting me
Please feel free to raise your hand, PM me for help on Slack (my username is william@), or ask in the #google slack channel. I'm here to help!

# Setup

Please go through the setup before the workshop begins - it takes quite a while to download everything!

1) [Download Android Studio](https://developer.android.com/studio/)

2a) If you plan on running your app on your computer, choose to have a "custom" instead of "standard" setup at this screen:

![Install Type](https://i.imgur.com/Qxj36uN.png)

Then, select to install all components.

![SDK Components Setup](https://i.imgur.com/5yG1Lb5.png)

2b) If you plan on running your app on your phone, install Android Studio with default settings, and then make sure you [enable developer options and USB debugging](https://developer.android.com/studio/debug/dev-options#enable).

In addition, I highly recommend that you enable these settings to automatically import any necessary Java packages we will be using. Please make sure you check "Add unambiguous imports on the fly" and "Optimize imports on the fly (for current project)".

![Import Settings](https://i.imgur.com/uEUTHtU.png)

# Starting the Project

For the base of this project, we're going to be using the [official introduction for building an app](https://developer.android.com/training/basics/firstapp/creating-project), from the Android Developer's website.

When you're done with this tutorial, we'll continue to add a few new features!

# Creating a List

Now, we're going to base this app around a list of content.

In your build.gradle (Module: app) file, add this line to your dependencies section
```
implementation 'com.android.support:recyclerview-v7:28.0.0-alpha1'
```

In activity_main.xml, replace everything inside the ConstraintLayout with:

```
<android.support.v7.widget.RecyclerView
    android:id="@+id/my_recycler_view"
    android:scrollbars="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

(So, there will no longer be the text and button from the end of the last tutorial.)

Now, we will make a new Java file MyAdapter.java. This is a lot to paste in at once, but a lot of this is just boilerplate code, and as we make changes to it we can hopefully gain a more intuitive understanding of how it works! For now, the most important thing to understand is that the "mDataset" variable is a String array. That is how the core data in our list of text will be structured.

```
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

 public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
     // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public MyViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }
     // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }
     // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_entry, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
     // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
     }
     // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
```


Now, we will change the beginning of the MainActivity file to this. This creates a few variables needed for our RecyclerView, and also sets up some boilerplate code for using it.
```
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
         String[] myDataset = new String[]{"Bulbasaur", "Ivysaur", "Venasaur"};
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }	        
```

Under res/layout, we can also make a pokemon_entry.xml file
```
<?xml version="1.0" encoding="utf-8"?>
<TextView android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text=""
    android:textSize="32dp"
    xmlns:android="http://schemas.android.com/apk/res/android" />
```

We should also comment out or delete the sendMessage method for now - it's not going to be used for the rest of this workshop, though it's related to concepts you may want to keep working on.

Right now, if you run the app, you should see a short list of Pokémon names - you can add extra data to confirm that it scrolls!

# Accessing the Internet
It's kind of boring to have an app with only hardcoded data - let's make it more dynamic! To demonstrate how to do this, we will use a Pokemon API.

To check out the API, you can look at the main link [https://pokeapi.co/api/v1/sprite/?limit=40&offset=1](https://pokeapi.co/api/v1/sprite/?limit=40&offset=1). It should look like a mess, so you can use this [JSON formatting website](https://jsonformatter.curiousconcept.com/) to have a better idea of what's going on.

![Formatted JSON](https://i.imgur.com/xIurtSa.png)

So, we can see that the rough structure is that there is an array of objects that represent information about separate Pokémon, including their name, Pokédex ID, and URL to their sprite image.

To allow the app to access the Internet, we will add the Internet permission to our AndroidManifest.xml file by adding this line right after the "manifest" line:

```
<uses-permission android:name="android.permission.INTERNET" />
```

Add this field to the top of MainActivity
```
public static final String POKEAPI_BASE_URL = "https://pokeapi.co";
```

And add this invocation near the end of your onCreate method
```
new PokemonDataTask().execute(POKEAPI_BASE_URL + "/api/v1/sprite/?limit=40&offset=1");
```

Add the AsyncTask to below the onCreate method.

```

private class PokemonDataTask extends AsyncTask<String, Void, String[]> {
     protected void onPreExecute() {
        super.onPreExecute();
    }
     protected String[] doInBackground(String... params) {
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
                String[] pokemonNames = new String[objects.length()];
                for(int i = 0; i < objects.length(); i++) {
                    JSONObject entry = objects.getJSONObject(i);
                    pokemonNames[i] = entry.getJSONObject("pokemon").getString("name");
                }
                return pokemonNames;
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
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        mAdapter = new MyAdapter(result);
        mRecyclerView.setAdapter(mAdapter);
    }
}

```

# Making the list better

Now, let's make it a little more interesting - to show an example of how you can make an item in a list more complicated, we'll also add a small image of the Pokémon next to the name.

As before, add this to your build.gradle (Module: app) file

```
implementation 'com.squareup.picasso:picasso:2.71828'
```

Since each list item will now have an image in addition to text, we'll change the pokemon_entry.xml file to:
```
<LinearLayout android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text=""	 
    android:textSize="32dp"
    xmlns:android="http://schemas.android.com/apk/res/android" />
        android:id="@+id/entry_sprite"
        android:layout_width="wrap_content"
        android:layout_height="match_parent" />
     <TextView
        android:id="@+id/entry_name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="28sp"/>
 </LinearLayout>
```

Remember that the mAdapter's current data is just a String array - that was all right when we just needed to have names in the list, but we now need two pieces of data - the name of the Pokémon, and the URL of the image of the Pokémon. So, let's make a new Java file PokemonEntryData.java with a relatively simple Java data object.
```
 public class PokemonEntryData {
    public String name;
    public String spriteUrl;
    public PokemonEntryData(String name, String spriteUrl) {
        this.name = name;
        this.spriteUrl = spriteUrl;
    }
}
```

Now, in the AsyncTask, we should change every instance of "String[]" to "PokemonEntryData[]", since the adapter needs data in this format now.

Also, in the try block of the AsyncTask, we need to update the logic to also pass along the sprite url

```
JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
JSONArray objects = jsonObject.getJSONArray("objects");	           
String[] pokemonNames = new String[objects.length()];	                    
PokemonEntryData[] entryData = new PokemonEntryData[objects.length()];
for(int i = 0; i < objects.length(); i++) {	          
    JSONObject entry = objects.getJSONObject(i);	                      
    pokemonNames[i] = entry.getJSONObject("pokemon").getString("name");
                        entryData[i] = new PokemonEntryData(entry.getJSONObject("pokemon").getString("name"), entry.getString("image"));
}
return entryData;
```

In MyAdapter.java, we should also change every instance of "String[]" to "PokemonEntryData[]". Then, the contents of the MyViewHolder class should change to:

```
public TextView mTextView;
public ImageView mImageView;
public MyViewHolder(View v) {
    super(v);
    mTextView = (TextView) v.findViewById(R.id.entry_name);
    mImageView = (ImageView) v.findViewById(R.id.entry_sprite);
}
```

This reflects the fact that pokemon_entry.xml now has an additional ImageView.

Then, we change the type of the variable v in the onCreateViewHolder to just a View, since the pokemon_entry is no longer just a textview.
```
View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.pokemon_entry, parent, false);
```

Finally, we change the contents of onBindViewHolder to
```
holder.mTextView.setText(mDataset[position].name);
Picasso.get().load(MainActivity.POKEAPI_BASE_URL + mDataset[position].spriteUrl).into(holder.mImageView);
```

Note, the second line handles all of the logic for downloading and resizing the image! Picasso is a great library :)

# What next?
## For this project, try to look up and figure out how to...
- Have the list refresh and add more Pokémon when you scroll to the bottom of the list.
- Polish the UI
  - Right now, all the sprites are different sizes - try making them the same size so that the list looks nicer.
- Make it so that when you click on an entry in the list, you open up a new Activity with more information about the Pokémon - remember that we did something similar in the basic tutorial at the beginning, with the textview and button.

# In General
- Follow more guides. There are [two more](https://developer.android.com/courses/) suggested on the Android Developers website, and I personally liked the Udacity courses/track.
- Use different APIs for your app. For example, [The OpenWeatherMap API](https://openweathermap.org/api).
- Check out some resources for [developing Material Design](https://material.io/develop/) to make your app look beautiful in the standard way.
- Check out [Firebase](https://firebase.google.com/) and [Google Cloud](https://cloud.google.com/) for more tools to make your app better, like messaging.

# Common Issues
- If you have used Android Studio before, you may have issues with rendering a preview of your app. In that case, refer to the help [linked here](https://stackoverflow.com/a/51709049).
- If you have issues with running the app on your device, you may need to [disable Instant Run](https://stackoverflow.com/a/34817880).

# General Troubleshooting
- Google is your friend! :)
  - Stack Overflow often shows up in Google results to help.
- I'm glad to try to help during the entire weekend! You can always reach me as william@ on Slack.
