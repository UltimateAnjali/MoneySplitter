package com.proj.balance.money.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proj.balance.money.DataModels.UserMembersData;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;
import com.proj.balance.money.DataModels.UserData;

import java.util.List;

/**
 * Created by anjali desai on 04-12-2017.
 */

public class PersonOwingsAdapter extends RecyclerView.Adapter<PersonOwingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<UserMembersData> userDataList;
    MyFonts fontFace;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, owing, owesTextDisplay;

        public MyViewHolder(View view) {
            super(view);
            personName = (TextView)view.findViewById(R.id.person_name);
            owing = (TextView)view.findViewById(R.id.person_owing);
            owesTextDisplay = (TextView)view.findViewById(R.id.owesText);
        }
    }

    public PersonOwingsAdapter(Context mContext, List<UserMembersData> mUserList) {
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

        //Binding font family for the person name
        fontFace = new MyFonts(mContext);
        holder.personName.setTypeface(fontFace.getMerri());
        holder.owesTextDisplay.setTypeface(fontFace.getMerri());

        UserMembersData userData = userDataList.get(position);
        holder.personName.setText(userData.getUserName());

        String userAmount = userData.getUserAmount();
        if(userAmount.equals("0.00")){
            holder.owesTextDisplay.setText("All set");
            holder.owing.setText("$ "+userData.getUserAmount());
        }
        else{
            String tempSign = userAmount.substring(0,1);
            String owingAmount = userAmount.substring(1);

            if(tempSign.equals("+")){
                holder.owesTextDisplay.setText("You owe");
                holder.owing.setText("$ "+owingAmount);
                holder.owesTextDisplay.setTextColor(mContext.getResources().getColor(R.color.redOwing));
                holder.owing.setTextColor(mContext.getResources().getColor(R.color.redOwing));
            }
            else if(tempSign.equals("-")){
                holder.owesTextDisplay.setText("Owes you");
                holder.owing.setText("$ "+owingAmount);
                holder.owesTextDisplay.setTextColor(mContext.getResources().getColor(R.color.greenOwing));
                holder.owing.setTextColor(mContext.getResources().getColor(R.color.greenOwing));
            }
        }
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }
}
