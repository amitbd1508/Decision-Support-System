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
import android.widget.Toast;

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

public class ModifyUserActivity extends AppCompatActivity {

    public final String TAG="ModifyUserActivity";
    EditText _nameText;

    EditText _emailText;

    EditText _passwordText;
    EditText _reEnterPasswordText;

    Button _modifyButton;
    Button _delete;
    MaterialSpinner spinner;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        _modifyButton= (Button) findViewById(R.id.btn_modify);
        _delete= (Button) findViewById(R.id.btn_deleteuser);
        _reEnterPasswordText= (EditText) findViewById(R.id.input_reEnterPassword);
        _passwordText= (EditText) findViewById(R.id.input_password);
        _emailText= (EditText) findViewById(R.id.input_email);
        _nameText= (EditText) findViewById(R.id.input_name);


        _nameText.setText(App.modifiedUser.name);
        _emailText.setText(App.modifiedUser.email);

        list=new ArrayList<String>();
        list.add(App.Admin);
        list.add(App.Management);
        list.add(App.Student);
        spinner.setItems(list);
        int typeno=0;
        if(App.modifiedUser.type.equals(App.Admin))
        {
            typeno=0;
        }
        else if(App.modifiedUser.type.equals(App.Management))
        {
            typeno=1;
        }
        else if(App.modifiedUser.type.equals(App.Student))
        {
            typeno=2;
        }
        spinner.setSelectedIndex(typeno);

        _modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
        _delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(App.modifiedUser.id);
            }
        });
        
    }

    private void deleteUser(int id) {

        try{
            String url = "http://www.nabila.website/api/deleteuserbyid.php?id="+id;


            url = url.replaceAll(" ", "%20");
            Log.e("url", url);
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                            if(response.contains("1"))
                            {
                                Toast.makeText(ModifyUserActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // Log.e("GetData", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();

                }
            });

            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            rq.add(sr);
        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Problem in you network. Cannot update", Toast.LENGTH_SHORT).show();
        }



    }

    public void modify() {
        Log.d(TAG, "modify");

        if (!validate()) {
            onmodifyFailed("Please validate the field ");
            return;
        }

        _modifyButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ModifyUserActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Modifying Account...");
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
                String url = "http://www.nabila.website/api/modifyuser.php?id="+App.modifiedUser.id+" &name=" + name + "&email="+email+"&password="+password+"&type="+list.get(spinner.getSelectedIndex());
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
                                        onmodifyFailed(jsonObject.getString("error_msg").toString());
                                    } else onmodifySuccess();
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


    public void onmodifySuccess() {
        _modifyButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onmodifyFailed(String error) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();


        _modifyButton.setEnabled(true);
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
