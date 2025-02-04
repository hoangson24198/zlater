package com.krahs.adminzlater.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krahs.adminzlater.Model.Level;
import com.krahs.adminzlater.R;

import java.util.List;

/**
 * Created by Hoang Son on 18,October,2019
 **/
public class SpinnerLevelAdapter extends BaseAdapter {
    private List<Level> listLevel;
    private Context context;
    public SpinnerLevelAdapter(List<Level> listLevel,Context context){
        this.listLevel=listLevel;
        this.context=context;
    }

    @Override
    public int getCount() {
        return listLevel.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inf=((Activity)context).getLayoutInflater();
        view=inf.inflate(R.layout.one_item_spinner_level,null);
        TextView titleLevel=view.findViewById(R.id.titleLevel);
        /*ImageView imvLevel=view.findViewById(R.id.imvLevel);*/
        titleLevel.setText(listLevel.get(i).getTitle());
/*
        Picasso.get().load(listLevel.get(i).getImage()).into(imvLevel);
*/
        return view;
    }
}
