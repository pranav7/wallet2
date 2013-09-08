package com.example.wallet2;


import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {


        EditText mobilenumBox;
        Button loginButton;
        Button skipLoginButton;
        
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);
                
                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
            boolean authenticated = settings.getBoolean("authenticated", false);
            
            if(authenticated)
            {
                    Intent intent = new Intent(this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
            }
                
                mobilenumBox = (EditText)findViewById(R.id.mobilenum_box);
                loginButton = (Button)findViewById(R.id.login_button);
                skipLoginButton = (Button)findViewById(R.id.skiplogin_button);
        }
        
        private class GetMessage extends AsyncTask<String, Void, Void>
        {


                @Override
                protected Void doInBackground(String... params) 
                {
                        // TODO Auto-generated method stub
                        
                        for (String mobileNumURL : params)
                        {
                                HttpClient messageClient = new DefaultHttpClient();
                                try 
                                {
                                    //try to fetch the data
                                        HttpPost httppost = new HttpPost("http://vast-woodland-7582.herokuapp.com/creditAPI/register/");
                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                nameValuePairs.add(new BasicNameValuePair("phn", mobileNumURL));
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                        HttpResponse messageResponse = messageClient.execute(httppost);
                                        StatusLine messageStatus = messageResponse.getStatusLine();
                                        if (messageStatus.getStatusCode() == 200) 
                                        {
                                                Log.d("Wallet 2","Mobile Number sent successfully");
                                        }
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                        }
                        return null;
                }
                
                protected void onPostExecute()
                {
                        loginButton.setEnabled(true);
                        skipLoginButton.setEnabled(true);
                }
        }
        
        public void performLogin(View v)
        {
                String mobNumber = mobilenumBox.getText().toString();
                loginButton.setEnabled(false);
                skipLoginButton.setEnabled(false);
                new GetMessage().execute(mobNumber);
                Intent intent = new Intent(this, AuthCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra(Constants.mobNum_REG, mobNumber);
                startActivity(intent);
        }
        
        public void skipLogin(View v)
        {
                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }
}