package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proj.balance.money.R;

public class ViewBillsFragment extends Fragment {

    public ViewBillsFragment() {
        // Required empty public constructor
    }

    public static ViewBillsFragment newInstance() {
        ViewBillsFragment fragment = new ViewBillsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_bills, container, false);
    }
}
