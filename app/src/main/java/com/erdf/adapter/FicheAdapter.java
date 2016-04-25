package com.erdf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.erdf.R;
import com.erdf.ViewFiche;

import java.util.List;

/**
 * Created by Radhan on 15/03/2016.
 */
public class FicheAdapter extends ArrayAdapter<ViewFiche> {

    private final List<ViewFiche> fiches ;

    public FicheAdapter(Activity context, List<ViewFiche> fiches) {
        super(context, 0, fiches) ;
        this.fiches = fiches ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_fiche ,parent, false);
        }

        FicheViewHolder viewHolder = (FicheViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new FicheViewHolder();
            viewHolder.adresse = (TextView) convertView.findViewById(R.id.adresse);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.adresse, viewHolder.adresse);
            convertView.setTag(R.id.date, viewHolder.date);
        } else {
            viewHolder = (FicheViewHolder) convertView.getTag();
        }

        //remplir la vue
        viewHolder.adresse.setText(getItem(position).getTitre());
        viewHolder.date.setText(getItem(position).getSousTitre());

        return convertView;
    }

    private class FicheViewHolder{
        public TextView id ;
        public TextView adresse ;
        public TextView date ;
    }

}

