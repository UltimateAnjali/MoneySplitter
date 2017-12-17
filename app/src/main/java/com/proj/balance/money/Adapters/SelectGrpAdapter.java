package com.proj.balance.money.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.R;

import java.util.List;

/**
 * Created by anjali desai on 13-12-2017.
 */

public class SelectGrpAdapter extends RecyclerView.Adapter<SelectGrpAdapter.MyViewHolder> {

    private Context mContext;
    private List<GroupData> groupDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView grpName;

        public MyViewHolder(View view) {
            super(view);
            grpName = (TextView)view.findViewById(R.id.selectGrpTv);
        }
    }

    public SelectGrpAdapter(Context mContext, List<GroupData> groups) {
        this.mContext = mContext;
        this.groupDataList = groups;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_grp_single_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupData grpData = groupDataList.get(position);
        holder.grpName.setText(grpData.getGrpName());
    }

    @Override
    public int getItemCount() {
        return groupDataList.size();
    }
}
