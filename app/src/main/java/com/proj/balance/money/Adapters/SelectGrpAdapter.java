package com.proj.balance.money.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.balance.money.Activities.MainActivity;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.Fragments.AddModifications;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;

import java.util.List;

/**
 * Created by anjali desai on 13-12-2017.
 */

public class SelectGrpAdapter extends RecyclerView.Adapter<SelectGrpAdapter.MyViewHolder> {

    private Context mContext;
    private List<GroupData> groupDataList;
    public GroupData selectedGrp;
    MyFonts fontFace;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView grpName;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            grpName = (TextView)view.findViewById(R.id.selectGrpTv);
            image = (ImageView)view.findViewById(R.id.justForFun);
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
        fontFace = new MyFonts(mContext);
        holder.grpName.setTypeface(fontFace.getVolk());
        final GroupData grpData = groupDataList.get(position);
        holder.grpName.setText(grpData.getGrpName());

        int posMod = position %10;
        int imageResource;
        switch (posMod){
            case 0:
            case 5:
                imageResource = R.drawable.smile_emoji;
                break;
            case 1:
            case 6:
                imageResource = R.drawable.dude_emoji;
                break;
            case 2:
            case 7:
                imageResource = R.drawable.happy_emoji;
                break;
            case 3:
            case 8:
                imageResource = R.drawable.rich_emoji;
                break;
            case 4:
            case 9:
                imageResource = R.drawable.winking_emoji;
                break;

            default:
                imageResource = R.drawable.happy_emoji;
                break;
        }
        holder.image.setImageResource(imageResource);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedGrp = grpData;
               // Toast.makeText(mContext,selectedGrp.grpName,Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedGrp",selectedGrp);
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
