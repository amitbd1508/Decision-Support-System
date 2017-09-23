package com.xyz.ideasubmission.student;

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
import com.xyz.ideasubmission.model.App;
import com.xyz.ideasubmission.model.Problem;

import java.util.List;



public class ProblemListAdapter extends RecyclerView.Adapter<ProblemListAdapter.ViewHolder>{


    List<Problem> problems;
    Context contex;

    public ProblemListAdapter(List<Problem> problems, Context contex) {
        this.problems = problems;
        this.contex = contex;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.problem_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.title.setText(problems.get(position).getTitle());
        holder.description.setText(problems.get(position).getBody());
        holder.submitedby.setText("Submitted By: "+problems.get(position).getSubmittedby());
        holder.like.setText(problems.get(position).getLikes()+"");
        holder.category.setText("Category : "+problems.get(position).getCategory()+"");

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.replay_id=problems.get(position).getId();
                App.replay_title=problems.get(position).getTitle();
                Log.d("++",App.replay_id+"");
                contex.startActivity(new Intent(contex,ReplyActivity.class));
            }
        });
        holder.btnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setText("+ 1 ");
                holder.btnlike.setVisibility(View.GONE);
                //holder.like.setVisibility(View.GONE);
                try{
                    String url = "http://www.nabila.website/api/inclikes.php?id="+problems.get(position).getId()+"&user_id="+App.USER.id;


                    url = url.replaceAll(" ", "%20");
                    Log.e("url", url);
                    StringRequest sr = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response", response);
                                    if(response=="1")
                                    {
                                        holder.like.setText("+1 "+(Integer.parseInt(holder.like.getText().toString()))+"");
                                        holder.btnlike.setVisibility(View.GONE);
                                    }
                                        //Toast.makeText(contex, "You Liked the post", Toast.LENGTH_SHORT).show();

                                    else
                                        Toast.makeText(contex, "You cannot give like", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            // Log.e("GetData", error.getMessage());


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


        holder.btndislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.like.setText("- 1");
                holder.btndislike.setVisibility(View.GONE);
                //holder.like.setVisibility(View.GONE);
                Toast.makeText(contex, "You disliked the post", Toast.LENGTH_SHORT).show();
                try{
                    String url = "http://www.nabila.website/api/declikes.php?id="+problems.get(position).getId()+"&user_id="+App.USER.id;


                    url = url.replaceAll(" ", "%20");
                    Log.e("url", url);
                    StringRequest sr = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response", response);
                                    if(response=="1")
                                    {
                                        holder.like.setText("- 1"+(Integer.parseInt(holder.like.getText().toString()))+"");
                                        holder.btnlike.setVisibility(View.GONE);
                                        Toast.makeText(contex, "You disliked the post", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(contex, "Problem in you network. Cannot update", Toast.LENGTH_SHORT).show();


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            // Log.e("GetData", error.getMessage());


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

    }

    @Override
    public int getItemCount() {
        return problems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, like,category,submitedby;
        Button btnlike,btndislike;
        View card;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            like = (TextView) view.findViewById(R.id.likes);
            submitedby = (TextView) view.findViewById(R.id.submitedby);
            category = (TextView) view.findViewById(R.id.category);

            btndislike= (Button) view.findViewById(R.id.btndislike);
            btnlike= (Button) view.findViewById(R.id.btnlike);
            card=  view.findViewById(R.id.card);




        }
    }
}
