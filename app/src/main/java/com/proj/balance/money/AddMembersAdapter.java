package com.proj.balance.money;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by anjali desai on 10-11-2017.
 */

public class AddMembersAdapter extends RecyclerView.Adapter<AddMembersAdapter.MyViewHolder> {

    private Context mContext;
    private List<AddMembersData> memberList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, contact;
        public ImageView personImage;

        public MyViewHolder(View view) {
            super(view);
            personName = (TextView)view.findViewById(R.id.person_name);
            contact = (TextView)view.findViewById(R.id.contact_num);
            personImage = (ImageView)view.findViewById(R.id.person_photo);
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
        AddMembersData membersData = memberList.get(position);
        holder.personName.setText(membersData.getPersonName());
        holder.contact.setText(membersData.getContactNum());


        // loading album cover using Glide library
        //Glide.with(mContext).load(Uri.parse(membersData.getImageUrl())).into(holder.personImage);

        /*holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    /*private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }*/

    /**
     * Click listener for popup menu items
     */
    /*class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }*/

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}

