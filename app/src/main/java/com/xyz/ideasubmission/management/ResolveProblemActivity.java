package com.xyz.ideasubmission.management;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xyz.ideasubmission.R;
import com.xyz.ideasubmission.model.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResolveProblemActivity extends AppCompatActivity {

    TextView tvbody,tvlike,tvtitle,likes;
    EditText reply;
    String re="";
    List<String> list;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolve_problem);

        tvbody= (TextView) findViewById(R.id.description);
        tvlike= (TextView) findViewById(R.id.likes);
        tvtitle= (TextView) findViewById(R.id.title);
        reply= (EditText) findViewById(R.id.replay);
        likes= (TextView) findViewById(R.id.likenumber);
        tvbody.setText(App.body+"");
        tvlike.setText(App.likes+"");
        tvtitle.setText(App.title+"");
        list=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        ListView listView= (ListView) findViewById(R.id.likelist);
        listView.setAdapter(adapter);

        fillupdata();


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re=reply.getText().toString();

                try{
                    String url = "http://www.nabila.website/api/insert_reply.php?reply="+re+"&post_id="+App.id;
                    url = url.replaceAll(" ", "%20");

                    StringRequest sr = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("response",response);
                                    if(response.contains("1"))
                                    {
                                        Toast.makeText(ResolveProblemActivity.this, "Submitted Sucessfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(ResolveProblemActivity.this, "Submitted Unsucessfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Snackbar.make(findViewById(android.R.id.content), "Network Error", Snackbar.LENGTH_LONG)
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
        });



    }

    void fillupdata()
    {



        final ProgressDialog progressDialog = new ProgressDialog(ResolveProblemActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        try{
            String url = "http://www.nabila.website/api/getreply.php?id="+App.id;


            url = url.replaceAll(" ", "%20");
            Log.e("url", url);
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            Log.d("Response",response);

                            try {

                                JSONObject mainobj=new JSONObject(response);
                                JSONArray like=mainobj.getJSONArray("likes");



                                int x=0;
                                for(int i=0;i<like.length();i++)
                                {
                                    x++;
                                    adapter.add(like.getJSONObject(i).getString("user"));
                                    adapter.notifyDataSetChanged();

                                    Log.d("++",like.getJSONObject(i).getString("user"));
                                }
                                likes.setText(x+" People Liked");






                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Problem in Loading ", Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                                finish();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(findViewById(android.R.id.content), "Error in getting data ", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();

                    progressDialog.dismiss();
                    finish();

                }
            });

            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            rq.add(sr);
        }catch (Exception ex)
        {
            Snackbar.make(findViewById(android.R.id.content), "Network Error", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
            progressDialog.dismiss();
        }
    }
}
