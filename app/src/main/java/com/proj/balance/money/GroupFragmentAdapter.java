package com.proj.balance.money;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by anjali desai on 27-11-2017.
 */

public class GroupFragmentAdapter extends RecyclerView.Adapter<GroupFragmentAdapter.MyViewHolder> {

    private Context mContext;
    private List<GroupData> groupDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView grpName, grpOwing, grpType;

        public MyViewHolder(View view) {
            super(view);
            grpName = (TextView)view.findViewById(R.id.grp_name);
            grpOwing = (TextView)view.findViewById(R.id.grp_owing);
            grpType = (TextView)view.findViewById(R.id.grp_type_text);
        }
    }

    public GroupFragmentAdapter(Context mContext, List<GroupData> groups) {
        this.mContext = mContext;
        this.groupDataList = groups;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_single_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupData grpData = groupDataList.get(position);
        holder.grpName.setText(grpData.getGrpName());
        holder.grpType.setText(grpData.getGrpType());
    }

    @Override
    public int getItemCount() {
        return groupDataList.size();
    }
}
