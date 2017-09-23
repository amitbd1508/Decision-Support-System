package com.xyz.ideasubmission.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xyz.ideasubmission.R;
import com.xyz.ideasubmission.admin.AdminActivity;
import com.xyz.ideasubmission.management.ManagmentActivity;
import com.xyz.ideasubmission.model.App;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

     EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _emailText= (EditText) findViewById(R.id.input_email);
        _passwordText= (EditText) findViewById(R.id.input_password);
        _loginButton= (Button) findViewById(R.id.btn_login);
        _signupLink= (TextView) findViewById(R.id.link_signup);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Validate All");
            return;
        }



        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        try{
            String url = "http://www.nabila.website/api/login.php?email="+email+"&password="+password;


            url = url.replaceAll(" ", "%20");
            Log.e(TAG, url);
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response",response);

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getString("error")=="true")
                                {
                                    onLoginFailed(jsonObject.getString("error_msg").toString());
                                }

                                else onLoginSuccess(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   // Log.e("GetData", error.getMessage());
                   onLoginFailed("Network Error ");
                }
            });

            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            rq.add(sr);
        }catch (Exception ex)
        {
            Snackbar.make(findViewById(android.R.id.content), "Network Error", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {


                Snackbar.make(findViewById(android.R.id.content),  "Please Login", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String response) {

        try{
            JSONObject jsonObject=new JSONObject(response);

            JSONObject userjson=new JSONObject(jsonObject.getString("user"));
            /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(App.ID,"Harneet");
            editor.putString(App.NAME,"Harneet");
            editor.putString(App.EMAIL,"Harneet");
            editor.apply();*/

            App.initUser();

            App.USER.id=userjson.getInt("id");
            App.USER.name=userjson.getString("name");
            App.USER.type=userjson.getString("type");
            App.USER.email=userjson.getString("email");
            Log.d("mess",App.USER.name+App.USER.id+App.USER.type);
            if(App.USER.type.equals((App.Admin)) || App.USER.type.contains("min"))
            {
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            }
            else if(App.USER.type.equals(App.Management)||App.USER.type.contains("anage")){
                startActivity(new Intent(getApplicationContext(),ManagmentActivity.class));
            }
            else if(App.USER.type.equals(App.Student)||App.USER.type.contains("udent")) {
                startActivity(new Intent(getApplicationContext(),ProblemListActivity.class));
            }


            _loginButton.setEnabled(true);

            finish();
        }catch (Exception ex){
            onLoginFailed("Error with parsing");
        }
    }

    public void onLoginFailed(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}

