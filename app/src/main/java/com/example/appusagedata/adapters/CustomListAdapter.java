package com.example.appusagedata.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.appusagedata.R;
import com.example.appusagedata.models.AppStatData;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {
    public CustomListAdapter(Context context, ArrayList<AppStatData> appStatDataList) {
        super(context, 0, appStatDataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppStatData appStat = (AppStatData) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.app_name);
        TextView tvHome = convertView.findViewById(R.id.time_spend);

        tvName.setText(appStat.getAppName());
        tvHome.setText(appStat.getTimeUsed() + " minute(s) spent");

        return convertView;
    }

}
