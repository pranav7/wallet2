package com.example.wallet2;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;


public class SearchResultActivity extends ListActivity {


        ArrayList<String> offersList;
        String[] offers;
//        private final int MAX_OFFERS = 20;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                
//                offers = new String[MAX_OFFERS];
                offersList = new ArrayList<String>();
                processQuery();
        }
        
        public void processQuery()
        {
                try
                {
                        String searchQuery = getIntent().getStringExtra(Constants.SEARCH_QUERY);
                        searchQuery = URLEncoder.encode(searchQuery, "utf-8");
                        String queryURL = "http://vast-woodland-7582.herokuapp.com/creditAPI/search/?q=" + searchQuery;
                        Log.d(getResources().getString(R.string.app_name), queryURL);
                        new GetOffers().execute(queryURL);
                }
                catch(Exception e)
                {
                        System.out.println(e);
                }
        }
        
        private class GetOffers extends AsyncTask<String, Void, String>
        {


                @Override
                protected String doInBackground(String... params) 
                {
                        // TODO Auto-generated method stub
                        StringBuilder offersBuilder = new StringBuilder();
                        
                        for (String offerSearchURL : params)
                        {
                                HttpClient offersClient = new DefaultHttpClient();
                                try 
                                {
                                    //try to fetch the data
                                        HttpGet offersGet = new HttpGet(offerSearchURL);
                                        HttpResponse offersResponse = offersClient.execute(offersGet);
                                        StatusLine offerSearchStatus = offersResponse.getStatusLine();
                                        if (offerSearchStatus.getStatusCode() == 200) 
                                        {
                                                //we have an OK response
                                                HttpEntity offersEntity = offersResponse.getEntity();
                                                InputStream offersContent = offersEntity.getContent();
                                                InputStreamReader offersInput = new InputStreamReader(offersContent);
                                                BufferedReader offersReader = new BufferedReader(offersInput);
                                                
                                                String lineIn;
                                                while ((lineIn = offersReader.readLine()) != null) 
                                                {
                                                    offersBuilder.append(lineIn);
                                                }
                                        }
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                        }
                        return offersBuilder.toString();
                }
                
                protected void onPostExecute(String result)
                {
                        try
                        {
                                //parse JSON
                                JSONObject resultObject = new JSONObject(result);
                                JSONArray offersArray = resultObject.getJSONArray("offers");
                                
                                if(!offersList.isEmpty())
                                        offersList.clear();
                                
                                int len = offersArray.length();
                                for(int i=0;i<len;i++)
                                {
                                        String offer = offersArray.get(i).toString();
                                        offersList.add(offer);
                                }
                                offers = new String[offersList.size()];
                                setContentView(R.layout.activity_search_result);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchResultActivity.this,android.R.layout.simple_list_item_1, offersList.toArray(offers));
                                setListAdapter(adapter);
                        }
                        catch(Exception e)
                        {
                                e.printStackTrace();
                        }
                }
        }
}