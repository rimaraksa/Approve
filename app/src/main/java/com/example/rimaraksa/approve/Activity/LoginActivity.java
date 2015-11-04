package com.example.rimaraksa.approve.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rimaraksa.approve.ServerConnection.Login;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.R;


public class LoginActivity extends ActionBarActivity {

//    DatabaseHelper helper = new DatabaseHelper(this);


    private Activity activity;
    private String username, password;


    //    Components on layout
    private EditText usernameField, passwordField;
    private Button bSignup, bLogin, bCancel;
    private TextView tvSignupLink;
    EditText tfUsername, tfPassword;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
//        Setting own font for logo
        String fontPath = "fonts/Coquette Bold.ttf";
        TextView tvLogo = (TextView) findViewById(R.id.TVLogo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        tvLogo.setTypeface(typeface);

//        usernameField = (EditText)findViewById(R.id.TFUsername);
//        passwordField = (EditText)findViewById(R.id.TFPassword);
//        tvSignupLink = (TextView) findViewById(R.id.TVSignupLink);
        bSignup = (Button) findViewById(R.id.BSignup);
        bLogin = (Button) findViewById(R.id.BLogin);

//        Login the account
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                username = usernameField.getText().toString();
//                password = passwordField.getText().toString();
//
//                new Login(v.getContext()).execute(username, password);

                popLoginDialog();

            }
        });

        //        Login the account
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);

            }
        });

////        Listening to signup link
//        tvSignupLink.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
////                Switching to Signup screen
//                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
//                startActivity(i);
//            }
//        });


    }

    private void popLoginDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_login, null);
        alertDialog.setView(view);

        bCancel = (Button) view.findViewById(R.id.BCancel);
        bLogin = (Button) view.findViewById(R.id.BLogin);
        tfUsername = (EditText) view.findViewById(R.id.TFUsername);
        tfPassword = (EditText) view.findViewById(R.id.TFPassword);

        // Showing Alert Message
        final AlertDialog ad = alertDialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ad.dismiss();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String username, password;

                username = tfUsername.getText().toString();
                password = tfPassword.getText().toString();

                if(username.equals("") || password.equals("")){
                    Util.loginError(activity, 0);
                }
                else{

                    new Login(v.getContext(), activity).execute(username, password);
                }



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

    @Override
    public void onBackPressed() {
        Intent backtoHome = new Intent(Intent.ACTION_MAIN);
        backtoHome.addCategory(Intent.CATEGORY_HOME);
        backtoHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backtoHome);
    }

}
