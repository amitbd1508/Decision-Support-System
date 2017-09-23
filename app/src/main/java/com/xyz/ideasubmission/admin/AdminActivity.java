package com.xyz.ideasubmission.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nipunbirla.boxloader.BoxLoaderView;
import com.xyz.ideasubmission.R;
import com.xyz.ideasubmission.model.Problem;
import com.xyz.ideasubmission.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<User> list;
    UserAdapter userAdapter;
    BoxLoaderView boxLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView= (RecyclerView) findViewById(R.id.userlist);
        list=new ArrayList<User>();
        userAdapter=new UserAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);
        //list.add(new User(1,"fsdfd","fsdf"));
        userAdapter.notifyDataSetChanged();
        boxLoader = (BoxLoaderView) findViewById(R.id.progress);
        findViewById(R.id.newUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddUserActivity.class));
            }
        });

        findViewById(R.id.managepost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PostViewActivity.class));
            }
        });
        fillupdata();
    }

    int x=0;
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Data Refreshing", Toast.LENGTH_SHORT).show();
        fillupdata();
    }

    void fillupdata()
    {

        list.clear();
        recyclerView.setVisibility(View.GONE);
        boxLoader.setVisibility(View.VISIBLE);
        try{
            String url = "http://www.nabila.website/api/getalluser.php";


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

                                    User user=new User();
                                    user.id=Integer.parseInt(jsonObject.getString("id"));
                                    user.name=(jsonObject.getString("name"));
                                    user.email=(jsonObject.getString("email"));
                                    user.type=(jsonObject.getString("type"));
                                    list.add(user);
                                    userAdapter.notifyDataSetChanged();
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
}
