package com.example.wallet2;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AuthCodeActivity extends Activity {
        
        EditText regcodeBox;
        Button validCodeButton;
        String mobNum;
        
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_authcode);
                
                regcodeBox = (EditText)findViewById(R.id.regcode_box);
                validCodeButton = (Button)findViewById(R.id.validcode_button);
                mobNum = getIntent().getStringExtra(Constants.mobNum_REG);
        }
        
        private class GetAuthResult extends AsyncTask<String, Void, String>
        {


                @Override
                protected String doInBackground(String... params) 
                {
                        // TODO Auto-generated method stub
                        StringBuilder regResultBuilder = new StringBuilder();
                        
                        for (String regCodeURL : params)
                        {
                                HttpClient regResultClient = new DefaultHttpClient();
                                try 
                                {
                                    //try to fetch the data
                                        HttpPost httppost = new HttpPost("http://vast-woodland-7582.herokuapp.com/creditAPI/register_result/");
                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                nameValuePairs.add(new BasicNameValuePair("code", regCodeURL));
                                nameValuePairs.add(new BasicNameValuePair("phn", mobNum));
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                        HttpResponse regResultResponse = regResultClient.execute(httppost);
                                        StatusLine regResultStatus = regResultResponse.getStatusLine();
                                        if (regResultStatus.getStatusCode() == 200) 
                                        {
                                                //we have an OK response
                                                HttpEntity regResultEntity = regResultResponse.getEntity();
                                                InputStream regResultContent = regResultEntity.getContent();
                                                InputStreamReader regResultInput = new InputStreamReader(regResultContent);
                                                BufferedReader regResultReader = new BufferedReader(regResultInput);
                                                
                                                String lineIn;
                                                while ((lineIn = regResultReader.readLine()) != null) 
                                                {
                                                    regResultBuilder.append(lineIn);
                                                }
                                                Log.d("Wallet 2","User Registered successfully");
                                        }
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                        }
                        return regResultBuilder.toString();
                }
                
                protected void onPostExecute(String result)
                {
                        try
                        {
                                //parse JSON
                                JSONObject resultObject = new JSONObject(result);
//                                JSONObject status = resultObject.getJSONObject("status");
//                                String match = status.toString();
                                String match = resultObject.getString("status");
                                Log.d("Wallet 2","Status: "+match);
                                if(match.equals("Registered"))
                                {
                                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putBoolean("authenticated", true);


                                    // Commit the edits!
                                    editor.commit();
                                        
                                        Intent intent = new Intent(AuthCodeActivity.this, MainActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                }
                                else
                                {
                                        Toast.makeText(AuthCodeActivity.this, "Invalid Registration Code", Toast.LENGTH_LONG).show();
                                        regcodeBox.setText("");
                                        validCodeButton.setText("Retry");
                                }
                        }
                        catch(Exception e)
                        {
                                e.printStackTrace();
                        }
                }
        }
        
        public void performCodeValidation(View v)
        {
                String regCode = regcodeBox.getText().toString();
                new GetAuthResult().execute(regCode);
        }
        
        public void skipCodeValidation(View v)
        {
                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }


}