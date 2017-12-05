package com.proj.balance.money;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anjali desai on 04-12-2017.
 */

public class PersonOwingsAdapter extends RecyclerView.Adapter<PersonOwingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<UserData> userDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, owing;

        public MyViewHolder(View view) {
            super(view);
            personName = (TextView)view.findViewById(R.id.person_name);
            owing = (TextView)view.findViewById(R.id.person_owing);
        }
    }

    public PersonOwingsAdapter(Context mContext, List<UserData> mUserList) {
        this.mContext = mContext;
        this.userDataList = mUserList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_single_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserData userData = userDataList.get(position);
        holder.personName.setText(userData.getUserGivenName());
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }
}
