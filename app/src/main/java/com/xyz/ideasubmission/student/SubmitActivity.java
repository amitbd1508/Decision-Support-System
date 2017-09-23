package com.xyz.ideasubmission.student;

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

public class SubmitActivity extends AppCompatActivity {

    EditText _title,_body,_category;
    String title,body,category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        final MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);

        _title= (EditText) findViewById(R.id.problem_title);
        _body= (EditText) findViewById(R.id.problem_body);


        final List<String> list=new ArrayList<String>();
        list.add("Computer Problem");
        list.add("Immigration Problem");
        list.add("Lab Problem");
        list.add("Hostel Problem");
        list.add("Food Problem");

        spinner.setItems(list);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    title=_title.getText().toString();
                    body=_body.getText().toString();
                    category=list.get(spinner.getSelectedIndex());

                    if(title==null&&title.isEmpty()) return;
                    if(body==null&&body.isEmpty()) return;


                    String url = "http://www.nabila.website/api/insert_post.php?title="+title+"&body="+body+"&submittedby="+ App.USER.id+"&category="+category;


                    url = url.replaceAll(" ", "%20");
                    Log.e("url", url);
                    StringRequest sr = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("response",response);
                                    if(response.contains("1"))
                                    {
                                        Toast.makeText(getApplicationContext(), "post submitted", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                    else {
                                        Snackbar.make(findViewById(android.R.id.content), "cannot save at this time", Snackbar.LENGTH_LONG)
                                                .setActionTextColor(Color.RED)
                                                .show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), "Login Faild ", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                    rq.add(sr);
                }catch (Exception ex)
                {
                    Snackbar.make(findViewById(android.R.id.content), "Sorry for this time", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                    ex.printStackTrace();
                }
            }
        });
    }
}
