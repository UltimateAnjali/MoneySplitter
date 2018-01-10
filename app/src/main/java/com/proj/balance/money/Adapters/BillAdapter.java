package com.proj.balance.money.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proj.balance.money.DataModels.BillsData;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;

import java.util.List;

/**
 * Created by anjali desai on 09-01-2018.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    Context mContext;
    List<BillsData> myBillsData;
    MyFonts fontFace;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView billname, members, dollarView, amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            billname = (TextView) itemView.findViewById(R.id.bill_name);
            members = (TextView)itemView.findViewById(R.id.noOfMembers);
            dollarView = (TextView)itemView.findViewById(R.id.dollarText);
            amount = (TextView)itemView.findViewById(R.id.totalAmount);
        }
    }

    public BillAdapter(Context context, List<BillsData> billsDataList){
        mContext = context;
        myBillsData = billsDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_bill_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BillsData singleBill = myBillsData.get(position);

        fontFace = new MyFonts(mContext);
        holder.billname.setTypeface(fontFace.getMerri());
        holder.members.setTypeface(fontFace.getMerri());
        holder.amount.setTypeface(fontFace.getMont());
        holder.dollarView.setTypeface(fontFace.getMont());

        holder.billname.setText(singleBill.getDescription());
        holder.members.setText("Members: "+singleBill.getMembers().size());
        holder.amount.setText(singleBill.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return myBillsData.size();
    }
}
