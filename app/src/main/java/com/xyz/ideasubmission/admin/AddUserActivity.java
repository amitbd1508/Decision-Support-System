package com.xyz.ideasubmission.admin;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.xyz.ideasubmission.R;
import com.xyz.ideasubmission.model.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity {

    public final String TAG="AddUserActivity";
    EditText _nameText;

    EditText _emailText;

    EditText _passwordText;
    EditText _reEnterPasswordText;

    Button _signupButton;
    MaterialSpinner spinner;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        _signupButton= (Button) findViewById(R.id.btn_signup);
        _reEnterPasswordText= (EditText) findViewById(R.id.input_reEnterPassword);
        _passwordText= (EditText) findViewById(R.id.input_password);
        _emailText= (EditText) findViewById(R.id.input_email);
        _nameText= (EditText) findViewById(R.id.input_name);
        list=new ArrayList<String>();
        list.add(App.Admin);
        list.add(App.Management);
        list.add(App.Student);
        spinner.setItems(list);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });





    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Please validate the field ");
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AddUserActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();

        String email = _emailText.getText().toString();

        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if(password==reEnterPassword)
        {
            progressDialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content), "Password are not same. ", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
            return;

        }
        else {
            try{
                String url = "http://www.nabila.website/api/register.php?name=" + name + "&email="+email+"&password="+password+"&type="+list.get(spinner.getSelectedIndex());
                url = url.replaceAll(" ", "%20");
                Log.e(TAG, url);
                StringRequest sr = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("error") == "true") {
                                        onSignupFailed(jsonObject.getString("error_msg").toString());
                                    } else onSignupSuccess();
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
                        Snackbar.make(findViewById(android.R.id.content), "Request failed ", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show();
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
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(String error) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();


        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
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
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

}
