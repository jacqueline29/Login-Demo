package com.frendz.registerdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.ConstantStore;
import utils.NetworkConnection;
import utils.ServiceHandler;
import utils.ValidateUser;

public class MainActivity extends Activity {

    EditText edtUsername, edtPassword;
    Button btnSignin;
    String strUsername, strPassword;
    boolean netConnection = false;
    NetworkConnection nw;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername = (EditText) findViewById(R.id.activity_login_edt_Username);
        edtPassword = (EditText) findViewById(R.id.activity_login_edt_Password);
        btnSignin = (Button) findViewById(R.id.activity_login_btn_login);


        nw = new NetworkConnection(getApplicationContext());
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLogin();
            }
        });

    }

    private void OpenLogin() {
        strUsername = edtUsername.getText().toString();
        strPassword = edtPassword.getText().toString();

        if (nw.isConnectingToInternet() == true) {
            if (ValidateUser.isNotNull(strUsername) && ValidateUser.isNotNull(strPassword)) {
                if (ValidateUser.isNotNull(strUsername) && ValidateUser.isNotNull(strPassword)) {
                    new signinOperation().execute();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Please fill the entire form.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Internet is not available. Please try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private class signinOperation extends AsyncTask<String, Void, Void> {

        String status, message;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.setMessage("Please wait...");
            prgDialog.show();
        }
        @Override
        protected Void doInBackground(String... params)
        {

            if (nw.isConnectingToInternet() == true) {
                try {
                    List<NameValuePair> nameValepairs = new ArrayList<NameValuePair>();

                    nameValepairs.add(new BasicNameValuePair("method", "SignIn"));
                    nameValepairs.add(new BasicNameValuePair("email", strUsername));
                    nameValepairs.add(new BasicNameValuePair("password", strPassword));

                    ServiceHandler sh = new ServiceHandler();
                    String response = sh.makeServiceCall(ConstantStore.API_URL, ServiceHandler.GET, nameValepairs);

                    Log.e("check ", "check response" + response);

                    JSONObject js = new JSONObject(response);
                    status = js.getString("status");
                    Log.e("status", status);

                    if (status.contains("Fail")) {
                        message = js.getString("message");
                    } else {
                        JSONObject jslogin = js.getJSONObject("user_info");
                        String str_user_id = jslogin.getString("user_id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                netConnection = true;
            }
            else
            {
                netConnection = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            prgDialog.dismiss();
            if (netConnection == false) {
                Toast toast = Toast.makeText(getApplicationContext(), "Internet is not available. please turn on and try again.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else
            {
                //move to activity
            }
            super.onPostExecute(aVoid);
        }
    }
}
