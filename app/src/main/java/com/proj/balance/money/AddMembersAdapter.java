package com.proj.balance.money;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anjali desai on 10-11-2017.
 */

public class AddMembersAdapter extends RecyclerView.Adapter<AddMembersAdapter.MyViewHolder> {

    private Context mContext;
    private List<AddMembersData> memberList;
    public List<String> selectedMembers = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, contact, addedText;
        public ImageView personImage;
        public Button addBtn;

        public MyViewHolder(View view) {
            super(view);
            personName = (TextView)view.findViewById(R.id.person_name);
            contact = (TextView)view.findViewById(R.id.contact_num);
            personImage = (ImageView)view.findViewById(R.id.person_photo);
            addBtn = (Button)view.findViewById(R.id.add_btn);
            addedText = (TextView)view.findViewById(R.id.addedTv);
        }
    }

    public AddMembersAdapter(Context mContext, List<AddMembersData> members) {
        this.mContext = mContext;
        this.memberList = members;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_members_single_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AddMembersData membersData = memberList.get(position);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(membersData.getPersonName().charAt(0)), R.color.loginBtnRipple);
        //ImageView image = (ImageView) findViewById(R.id.image_view);
        holder.personImage.setImageDrawable(drawable);

        holder.personName.setText(membersData.getPersonName());
        holder.contact.setText(membersData.getContactNum());

        if(membersData.getAdded()){
            holder.addBtn.setVisibility(View.GONE);
            holder.addedText.setVisibility(View.VISIBLE);
        }
        else {
            holder.addBtn.setVisibility(View.VISIBLE);
            holder.addedText.setVisibility(View.GONE);

            holder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedMembers.add(membersData.getKey());
                    //Toast.makeText(mContext,"Key: "+membersData.getKey(),Toast.LENGTH_LONG).show();
                    membersData.setAdded(true);
                    holder.addBtn.setVisibility(View.GONE);
                    holder.addedText.setVisibility(View.VISIBLE);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}

