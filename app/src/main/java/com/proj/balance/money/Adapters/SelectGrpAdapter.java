package com.proj.balance.money.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.balance.money.Activities.MainActivity;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.Fragments.AddModifications;
import com.proj.balance.money.R;

import java.util.List;

/**
 * Created by anjali desai on 13-12-2017.
 */

public class SelectGrpAdapter extends RecyclerView.Adapter<SelectGrpAdapter.MyViewHolder> {

    private Context mContext;
    private List<GroupData> groupDataList;
    public static GroupData selectedGrp;

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
        final GroupData grpData = groupDataList.get(position);
        holder.grpName.setText(grpData.getGrpName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedGrp = grpData;
                Toast.makeText(mContext,selectedGrp.grpName,Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putParcelable("lol",selectedGrp);
                Fragment fragment = AddModifications.newInstance();
                FragmentTransaction transaction = ((MainActivity)mContext).getSupportFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupDataList.size();
    }
}
