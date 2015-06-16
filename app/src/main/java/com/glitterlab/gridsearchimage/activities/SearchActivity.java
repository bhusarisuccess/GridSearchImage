package com.glitterlab.gridsearchimage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

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


public class SearchActivity extends ActionBarActivity {
private EditText etQuery;
    private GridView gvresult;
    private Button btnSearch;
    private ArrayList<ImageSearchResult> resultlist;
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
    }

    public void setUpViews(){
        etQuery= (EditText)findViewById(R.id.etSearch);
        gvresult= (GridView)findViewById(R.id.gdResult);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        gvresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch the item display activity

                //creating an intent
                Intent i= new Intent(getApplicationContext(),ImageDisplayActivity.class);
                //get the image to display
             ImageSearchResult result=resultlist.get(position);

                //pass the image result into intent
              i.putExtra("url",result.fullurl);
                //launch the new activity
                startActivity(i);

            }
        });


    }
//fired whenever button is pressed (android:onClick property)
 public void onImageSearch(View v){
String query= etQuery.getText().toString();
     AsyncHttpClient client= new AsyncHttpClient();
     //https://ajax.googleapis.com/ajax/services/search/images?v=1&q=android&size=8
     String searchUrl="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ query +"&rsz=8";
     client.get(searchUrl, new JsonHttpResponseHandler(){
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
             Log.e("Debug",response.toString());
             JSONArray imageResultJson=null;
             try {
                 imageResultJson= response.getJSONObject("responseData").getJSONArray("results");
                 resultlist.clear();// clear the existing images from the array
                 //then u make to the adapter ,it does modify underlyn data

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
}
