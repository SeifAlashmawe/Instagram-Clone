/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package hope.afterlifeprojects.seif.french.insgramclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener, View.OnKeyListener {

    Boolean signUpModeActive = true ;
    TextView ChangeSingUpMode ;
    EditText passwordEditText ;

    public void showUserList ()
    {
        Intent intent = new Intent(getApplicationContext() , UserListActivity.class);
        startActivity(intent);

    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if ( i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
        {
            signUp(view);
        }

        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.changeSignUpModeTextView){

            Button sigupButton = (Button)findViewById(R.id.sigupButton);

           if (signUpModeActive){

               signUpModeActive = false ;
               sigupButton.setText("Login");
               ChangeSingUpMode.setText("Or , Signup");

           }else {

               signUpModeActive = true ;
               sigupButton.setText("Signup");
               ChangeSingUpMode.setText("Or , Login");
           }


        } else if (view.getId() == R.id.backgroungRealtiveLayout || view.getId() == R.id.logoImageView){

            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }

    }

    public void signUp(View v)
    {

        EditText usernameEditText = (EditText)findViewById(R.id.userNameEditText);
        EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches(""))
        {
            Toast.makeText(this, "a username and password are required.", Toast.LENGTH_SHORT).show();
        }else {

            if (signUpModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Log.i("Signup", "Successful");
                            showUserList();
                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }else {

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null)
                        {
                            Log.i("Signup", "Successful");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.backgroungRealtiveLayout);
        ImageView imageView = (ImageView)findViewById(R.id.logoImageView);
        relativeLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);


        ChangeSingUpMode = (TextView)findViewById(R.id.changeSignUpModeTextView);
        ChangeSingUpMode.setOnClickListener(this);
        passwordEditText.setOnKeyListener(this);

        if (ParseUser.getCurrentUser() != null)
        {
            showUserList();
        }




        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}
