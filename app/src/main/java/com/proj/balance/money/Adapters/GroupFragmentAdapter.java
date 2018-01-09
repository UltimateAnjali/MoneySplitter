package com.proj.balance.money.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.proj.balance.money.DataModels.GroupOwingsData;
import com.proj.balance.money.Fragments.GroupDetails;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;

import java.util.List;

/**
 * Created by anjali desai on 27-11-2017.
 */

public class GroupFragmentAdapter extends RecyclerView.Adapter<GroupFragmentAdapter.MyViewHolder> {

    private Context mContext;
    private List<GroupOwingsData> groupOwingsDataList;
    MyFonts fontFace;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView grpName, grpOwing, grpType, grpOwingTextDisplay;

        public MyViewHolder(View view) {
            super(view);
            grpName = (TextView)view.findViewById(R.id.grp_name);
            grpOwing = (TextView)view.findViewById(R.id.grp_owing);
            grpType = (TextView)view.findViewById(R.id.grp_type_text);
            grpOwingTextDisplay = (TextView)view.findViewById(R.id.grpOwingText);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        GroupOwingsData posGrpOwingsData = groupOwingsDataList.get(pos);

                        Bundle bundle = new Bundle();
                        bundle.putString("grpKey",posGrpOwingsData.getGrpKey());
                        Fragment mFrag = GroupDetails.newInstance();
                        FragmentTransaction transaction = ((MainActivity)mContext).getSupportFragmentManager().beginTransaction();
                        mFrag.setArguments(bundle);
                        transaction.replace(R.id.frame_layout, mFrag);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        //Toast.makeText(mContext,"hg->"+posGrpOwingsData.getGrpName(),Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public GroupFragmentAdapter(Context mContext, List<GroupOwingsData> groups) {
        this.mContext = mContext;
        this.groupOwingsDataList = groups;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_single_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Binding font family for group name
        fontFace = new MyFonts(mContext);
        holder.grpName.setTypeface(fontFace.getMerri());
        holder.grpType.setTypeface(fontFace.getVolk());
        holder.grpOwingTextDisplay.setTypeface(fontFace.getMerri());

        GroupOwingsData grpData = groupOwingsDataList.get(position);
        holder.grpName.setText(grpData.getGrpName());
        holder.grpType.setText(grpData.getGrpType());
        String grpOws = grpData.getGrpOwing();
        if(grpOws.equals("0.00")){
            holder.grpOwingTextDisplay.setText("All set");
            holder.grpOwing.setText("$ "+grpData.getGrpOwing());
        }
        else{
            String temp = grpOws.substring(0,1);
            String value = grpOws.substring(1);
            if(temp.equals("+")){
                holder.grpOwingTextDisplay.setText("You are owed");
                holder.grpOwing.setText("$ "+value);
                holder.grpOwingTextDisplay.setTextColor(mContext.getResources().getColor(R.color.greenOwing));
                holder.grpOwing.setTextColor(mContext.getResources().getColor(R.color.greenOwing));
            }
            else if(temp.equals("-")){
                holder.grpOwingTextDisplay.setText("You owe");
                holder.grpOwing.setText("$ "+value);
                holder.grpOwingTextDisplay.setTextColor(mContext.getResources().getColor(R.color.redOwing));
                holder.grpOwing.setTextColor(mContext.getResources().getColor(R.color.redOwing));
            }
        }

    }

    @Override
    public int getItemCount() {
        return groupOwingsDataList.size();
    }
}
