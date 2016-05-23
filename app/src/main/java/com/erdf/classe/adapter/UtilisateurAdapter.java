package com.erdf.classe.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erdf.R;
import com.erdf.classe.metier.Utilisateur;

import java.util.ArrayList;

/**
 * Created by Radhan on 15/03/2016.
 */
public class UtilisateurAdapter extends ArrayAdapter<Utilisateur> {

    public UtilisateurAdapter(Activity context, ArrayList<Utilisateur> utilisateurs) {
        super(context, 0, utilisateurs) ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_utilisateur ,parent, false);
        }

        UtilisateurViewHolder viewHolder = (UtilisateurViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new UtilisateurViewHolder();
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            viewHolder.nom = (TextView) convertView.findViewById(R.id.nom);
            convertView.setTag(viewHolder) ;
        } else {
            viewHolder = (UtilisateurViewHolder) convertView.getTag();
        }

        //getItem(position) va récupérer l'item [position] de la List actus
        Utilisateur unUtilisateur = getItem(position);

        //remplir la vue
        viewHolder.avatar.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.personne));
        viewHolder.nom.setText(unUtilisateur.getNom() + " " + unUtilisateur.getPrenom());

        return convertView;
    }

    private class UtilisateurViewHolder{
        public ImageView avatar;
        public TextView nom;
    }

}

