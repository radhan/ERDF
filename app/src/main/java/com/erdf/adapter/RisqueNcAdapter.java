package com.erdf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.erdf.R;
import com.erdf.classe.metier.Risque;

import java.util.ArrayList;

/**
 * Created by Radhan on 15/03/2016.
 */
public class RisqueNcAdapter extends ArrayAdapter<Risque> {

    public RisqueNcAdapter(Activity context, ArrayList<Risque> risques) {
        super(context, 0, risques) ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_risquenc ,parent, false);
        }

        RisqueNcViewHolder viewHolder = (RisqueNcViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new RisqueNcViewHolder();
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.soustitre = (TextView) convertView.findViewById(R.id.soustitre);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.titre, viewHolder.titre);
            convertView.setTag(R.id.soustitre, viewHolder.soustitre);
        } else {
            viewHolder = (RisqueNcViewHolder) convertView.getTag();
        }

        //remplir la vue
        viewHolder.titre.setText(getItem(position).getTitre());
        viewHolder.soustitre.setText(getItem(position).getResume());

        return convertView;
    }

    private class RisqueNcViewHolder{
        public TextView titre ;
        public TextView soustitre ;
    }

}

