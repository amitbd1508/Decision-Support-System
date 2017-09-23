package com.xyz.ideasubmission.student;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class ReplyActivity extends AppCompatActivity {

    TextView likes,title,replay;
    String stitle,sreplay;
    List<String> list;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        replay= (TextView) findViewById(R.id.postreply);
        title= (TextView) findViewById(R.id.posttitle);
        likes= (TextView) findViewById(R.id.likenumber);
        list=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        ListView listView= (ListView) findViewById(R.id.likelist);
        listView.setAdapter(adapter);


        sreplay="";
        stitle=App.replay_title;
        title.setText(stitle);

        fillupdata();
    }
    void fillupdata()
    {


        final ProgressDialog progressDialog = new ProgressDialog(ReplyActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        try{
            String url = "http://www.nabila.website/api/getreply.php?id="+App.replay_id;


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
                                JSONArray re=mainobj.getJSONArray("reply");

                                if(re.length()>0)
                                    replay.setText(re.getJSONObject(0).getString("reply"));
                                else
                                    replay.setText("No reply");
                                int x=0;
                                for(int i=0;i<like.length();i++)
                                {
                                    x++;
                                    adapter.add(like.getJSONObject(i).getString("user"));

                                    Log.d("++",like.getJSONObject(i).getString("user"));
                                }
                                likes.setText(x+" People Liked");
                                adapter.notifyDataSetChanged();





                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ReplyActivity.this, "Problem in Loading ", Toast.LENGTH_SHORT).show();

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
