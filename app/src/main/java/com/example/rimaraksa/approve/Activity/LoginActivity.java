package com.example.rimaraksa.approve.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.DatabaseConnection.Login;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class LoginActivity extends ActionBarActivity {

//    DatabaseHelper helper = new DatabaseHelper(this);


    private String username, password;


    //    Components on layout
    private EditText usernameField, passwordField;
    private Button loginButton;
    private TextView tvSignupLink;


    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText)findViewById(R.id.TFUsername);
        passwordField = (EditText)findViewById(R.id.TFPassword);
        tvSignupLink = (TextView) findViewById(R.id.TVSignupLink);
        loginButton = (Button) findViewById(R.id.BLogin);

//        Login the account
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                new Login(v.getContext()).execute(username, password);

            }
        });

//        Listening to signup link
        tvSignupLink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Switching to Signup screen
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
