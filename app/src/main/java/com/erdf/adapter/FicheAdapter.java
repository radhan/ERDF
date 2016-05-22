package com.erdf.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.erdf.R;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radhan on 15/03/2016.
 */
public class FicheAdapter extends ArrayAdapter<Fiche> {

    public FicheAdapter(Activity context, ArrayList<Fiche> fiches) {
        super(context, 0, fiches) ;
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
            convertView.setTag(viewHolder) ;
        } else {
            viewHolder = (FicheViewHolder) convertView.getTag();
        }

        //getItem(position) va récupérer l'item [position] de la List actus
        Fiche uneFiche = getItem(position);
        Chantier unChantier = uneFiche.getUnChantier() ;

        //remplir la vue
        viewHolder.adresse.setText(unChantier.getNumRue() + " " + unChantier.getRue() + " " + unChantier.getCodePostal() + " " + unChantier.getVille());
        viewHolder.date.setText(uneFiche.getDate()) ;

        return convertView;
    }

    private class FicheViewHolder{
        public TextView id ;
        public TextView adresse ;
        public TextView date ;
    }

}

