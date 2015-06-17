package com.glitterlab.gridsearchimage.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.glitterlab.gridsearchimage.R;
import com.glitterlab.gridsearchimage.adapter.ImageSearchAdapter;
import com.glitterlab.gridsearchimage.models.ImageSearchResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;


public class SearchActivity extends AppCompatActivity {
private EditText etQuery;
    private GridView gvresult;
    private Button btnSearch;

   private  ArrayList<ImageSearchResult> resultlist;
    private ImageSearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

             setUpViews();
        //create the data source
        resultlist= new ArrayList<ImageSearchResult>();
        //attaches data source to an adapter
        adapter= new ImageSearchAdapter(this,resultlist);
        //set adapter to gridview
        gvresult.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
    }

    public void setUpViews(){
       // etQuery= (EditText)findViewById(R.id.etSearch);
        gvresult= (GridView)findViewById(R.id.gdResult);
       /* btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()){
                    onImageSearch(v);
                }else
                    Toast.makeText(getApplicationContext(), "Check your Network Connection and try again.", LENGTH_LONG).show();
            }
        });*/
        gvresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch the item display activity

                //creating an intent
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                //get the image to display
                ImageSearchResult result = resultlist.get(position);

                //pass the image result into intent
                i.putExtra("url", result.fullurl);
                //launch the new activity
                startActivity(i);

            }
        });


    }
//fired whenever button is pressed (android:onClick property)
 public void onImageSearch(View v){
String query= etQuery.getText().toString().trim();
     AsyncHttpClient client= new AsyncHttpClient();
     // https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
     String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query  + "&rsz=8";
     client.get(searchUrl, new JsonHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
             Log.e("Debug", response.toString());
             JSONArray imageResultJson = null;
             try {
                 imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
                 resultlist.clear();// clear the existing images from the array
                 //imageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                 // aImageResults.notifyDataSetChanged();
                 // alternatively we can use

                 adapter.addAll(ImageSearchResult.fromJSONarray(imageResultJson));


             } catch (JSONException e) {
                 e.printStackTrace();

             }

         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
             super.onFailure(statusCode, headers, responseString, throwable);
         }
     });


 }
    public void onImageSearchActionbar(String query){

        AsyncHttpClient client= new AsyncHttpClient();
        // https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query  + "&rsz=8";
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("Debug", response.toString());
                JSONArray imageResultJson = null;
                try {
                    imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
                    resultlist.clear();// clear the existing images from the array
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                    // aImageResults.notifyDataSetChanged();
                    // alternatively we can use

                    adapter.addAll(ImageSearchResult.fromJSONarray(imageResultJson));


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isNetworkAvailable()){
                    onImageSearchActionbar(query);
                }else
                    Toast.makeText(getApplicationContext(), "Check your Network Connection and try again.", LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
Intent i =new Intent(this,SettingActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
