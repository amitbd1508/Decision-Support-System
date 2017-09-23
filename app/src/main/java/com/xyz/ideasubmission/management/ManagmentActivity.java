package com.xyz.ideasubmission.management;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nipunbirla.boxloader.BoxLoaderView;
import com.xyz.ideasubmission.R;
import com.xyz.ideasubmission.model.Problem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManagmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProblemListAdapterManagement problemListAdapter;
    List<Problem> problemlist;
    MaterialSpinner spinner;
    BoxLoaderView boxLoader;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boxLoader = (BoxLoaderView) findViewById(R.id.progress);

        toolbar.setTitle("Computer Problem");
        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        final List<String> list=new ArrayList<String>();
        list.add("Computer Problem");
        list.add("Immigration Problem");
        list.add("Lab Problem");
        list.add("Hostel Problem");
        list.add("Food Problem");

        spinner.setItems(list);


        problemlist=new ArrayList<Problem>();



        boxLoader.setVisibility(View.GONE);

        recyclerView= (RecyclerView) findViewById(R.id.recycleview);
        problemListAdapter=new ProblemListAdapterManagement(problemlist,this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(problemListAdapter);
        fillupdatabycategory("Computer Problem");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {


                fillupdatabycategory(item);
                toolbar.setTitle(item);

                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();

            }
        });
    }


    void fillupdatabycategory(String category)
    {
        recyclerView.setVisibility(View.GONE);
        boxLoader.setVisibility(View.VISIBLE);
        problemlist.clear();
        problemListAdapter.notifyDataSetChanged();
        try{
            String url = "http://www.nabila.website/api/getpostbycategory.php?category="+category;


            url = url.replaceAll(" ", "%20");
            Log.e("url", url);
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            Log.d("Response",response);

                            try {
                                recyclerView.setVisibility(View.VISIBLE);
                                boxLoader.setVisibility(View.GONE);
                                JSONArray jsonArray=new JSONArray(response);
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=new JSONObject();
                                    jsonObject=jsonArray.getJSONObject(i);

                                    Problem problem=new Problem();
                                    problem.setBody(jsonObject.getString("body"));
                                    problem.setCategory(jsonObject.getString("category"));
                                    problem.setSubmittedby(jsonObject.getString("username"));
                                    problem.setDate(jsonObject.getString("date"));
                                    problem.setTitle(jsonObject.getString("title"));
                                    problem.setId(jsonObject.getInt("id"));
                                    problem.setLikes(jsonObject.getInt("likes"));


                                    problemlist.add(problem);
                                    problemListAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // onLoginFailed();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // Log.e("GetData", error.getMessage());

                    Snackbar.make(findViewById(android.R.id.content), "Error in getting data ", Snackbar.LENGTH_LONG)
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create:

                return true;
            case R.id.help:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
