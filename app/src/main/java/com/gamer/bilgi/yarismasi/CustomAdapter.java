package com.gamer.bilgi.yarismasi;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.gamer.bilgi.yarismasi.MainActivity.userModel;

public class CustomAdapter extends ArrayAdapter<CustomListviewModel> implements View.OnClickListener{

    private ArrayList<CustomListviewModel> dataSet;
    Context mContext;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView textPuan;
        TextView txtSorusayi;
        ImageView photo;
        TextView oynama_Sayi;
    }

    public CustomAdapter(ArrayList<CustomListviewModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        CustomListviewModel dataModel=(CustomListviewModel)object;


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomListviewModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.isim);
            viewHolder.textPuan = (TextView) convertView.findViewById(R.id.puan);
            viewHolder.txtSorusayi = (TextView) convertView.findViewById(R.id.soru_sayisi);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.oynama_Sayi = (TextView) convertView.findViewById(R.id.gnd_soru);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.oynama_Sayi.setText(dataModel.getOy_sayi());
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.textPuan.setText(dataModel.getPuan());
        viewHolder.txtSorusayi.setText(dataModel.getSoru_sayi());

        Picasso.get().load(dataModel.getPhoto()).into(  viewHolder.photo );





       // viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
