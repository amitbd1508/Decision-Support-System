package com.xyz.ideasubmission.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xyz.ideasubmission.R;
import com.xyz.ideasubmission.management.ResolveProblemActivity;
import com.xyz.ideasubmission.model.App;
import com.xyz.ideasubmission.model.Problem;

import java.util.List;


public class ProblemAdminAdapter extends RecyclerView.Adapter<ProblemAdminAdapter.ViewHolder>{


    List<Problem> problems;
    Context contex;

    public ProblemAdminAdapter(List<Problem> problems, Context contex) {
        this.problems = problems;
        this.contex = contex;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.problem_list_item_admin, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.title.setText(problems.get(position).getTitle());
        holder.description.setText(problems.get(position).getBody());
        holder.like.setText(problems.get(position).getLikes()+"");
        holder.submittedby.setText("Submitted By :"+problems.get(position).getSubmittedby()+"");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String url = "http://www.nabila.website/api/deletepost.php?post_id="+problems.get(position).getId();


                    url = url.replaceAll(" ", "%20");
                    Log.e("url", url);
                    StringRequest sr = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response", response);
                                    if(response.contains("1"))
                                    {
                                        problems.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            // Log.e("GetData", error.getMessage());
                            Toast.makeText(contex, "Failed to delete", Toast.LENGTH_SHORT).show();


                        }
                    });

                    RequestQueue rq = Volley.newRequestQueue(contex);
                    rq.add(sr);
                }catch (Exception ex)
                {
                    Toast.makeText(contex, "Problem in you network. Cannot update", Toast.LENGTH_SHORT).show();
                }


            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(contex,ResolveProblemActivity.class);
                App.id=problems.get(position).getId()+"";
                App.body=(problems.get(position).getBody());
                App.title=problems.get(position).getTitle();
                App.likes=problems.get(position).getLikes()+"";

                contex.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return problems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, like,submittedby;
        View card;
        Button delete;


        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            like = (TextView) view.findViewById(R.id.likes);
            submittedby = (TextView) view.findViewById(R.id.submitedby);
            delete = (Button) view.findViewById(R.id.remove);

            card =  view.findViewById(R.id.card);

        }
    }
}
