package com.proj.balance.money.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.proj.balance.money.CircleTransform;
import com.proj.balance.money.DataModels.SingleGroupMembersData;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by anjali desai on 08-01-2018.
 */

public class MemberDisplayAdapter extends RecyclerView.Adapter<MemberDisplayAdapter.MyViewHolder> {

    private Context mContext;
    private List<SingleGroupMembersData> membersDataList;
    MyFonts fontFace;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, memberType, amount;
        public ImageView personImage2;

        public MyViewHolder(View itemView) {
            super(itemView);
            personImage2 = (ImageView) itemView.findViewById(R.id.person_photo2);
            personName = (TextView)itemView.findViewById(R.id.person_name2);
            memberType = (TextView)itemView.findViewById(R.id.grpMemberType);
            amount = (TextView)itemView.findViewById(R.id.dispAmount);
        }
    }

    public MemberDisplayAdapter(Context context, List<SingleGroupMembersData> data){
        this.mContext = context;
        this.membersDataList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_member_single_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        SingleGroupMembersData singleMemberData = membersDataList.get(position);

        //Setting image
        Picasso.with(mContext)
                .load(Uri.parse(singleMemberData.getUserPhoto()+"?sz=500"))
                .noFade()
                .transform(new CircleTransform())
                .into(holder.personImage2);

        //Setting font typeface
        fontFace = new MyFonts(mContext);
        holder.personName.setTypeface(fontFace.getVolk());
        holder.memberType.setTypeface(fontFace.getMont());

        //Setting all the values
        holder.personName.setText(singleMemberData.getUsername());
        holder.memberType.setText("~"+singleMemberData.getUserType());
        String userAmount = singleMemberData.getUserAmount();
        if(userAmount.equals("0.00")){
            holder.amount.setText("All set");
        }
        else{
            String sign = userAmount.substring(0,1);
            String tempValue = userAmount.substring(1);
            if(sign.equals("+")){
                holder.amount.setTextColor(mContext.getResources().getColor(R.color.greenOwing));
            }
            else if(sign.equals("-")){
                holder.amount.setTextColor(mContext.getResources().getColor(R.color.redOwing));
            }
            holder.amount.setText("$ "+tempValue);
        }
    }

    @Override
    public int getItemCount() {
        return membersDataList.size();
    }
}
