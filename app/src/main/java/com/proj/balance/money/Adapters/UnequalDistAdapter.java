package com.proj.balance.money.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;

/**
 * Created by anjali desai on 09-01-2018.
 */

public class UnequalDistAdapter extends RecyclerView.Adapter<UnequalDistAdapter.MyViewHolder> {

    private Context mContext;
    MyFonts fontFace;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView dollarCurrency;
        public EditText amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.user_name_text);
            dollarCurrency = (ImageView) itemView.findViewById(R.id.currencyImage);
            amount = (EditText)itemView.findViewById(R.id.amount_for_single_user);
        }
    }

    public  UnequalDistAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unequal_single_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        fontFace = new MyFonts(mContext);
        holder.name.setTypeface(fontFace.getMerri());
        holder.amount.setTypeface(fontFace.getMont());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
