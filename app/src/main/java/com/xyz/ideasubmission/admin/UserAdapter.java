package com.xyz.ideasubmission.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xyz.ideasubmission.R;
import com.xyz.ideasubmission.model.App;
import com.xyz.ideasubmission.model.User;

import java.util.List;

/**
 * Created by Amit on 8/7/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<User> list;
    Context context;

    public UserAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);

        return new UserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                App.modifiedUser.id=list.get(position).id;
                App.modifiedUser.name=list.get(position).name;
                App.modifiedUser.email=list.get(position).email;
                App.modifiedUser.type=list.get(position).type;
                context.startActivity(new Intent(context,ModifyUserActivity.class));

            }
        });
        holder.type.setText(list.get(position).getType());
        holder.name.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, type;
        Button change;
        View card;

        public ViewHolder(View view) {
            super(view);
            name= (TextView) view.findViewById(R.id.name);
            type= (TextView) view.findViewById(R.id.type);

            change= (Button) view.findViewById(R.id.change);




        }
    }
}
