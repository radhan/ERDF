package com.erdf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.erdf.R;
import com.erdf.ViewRisque;

import java.util.List;

/**
 * Created by Radhan on 15/03/2016.
 */
public class RisqueAdapter  extends ArrayAdapter<ViewRisque> {

    public RisqueAdapter(Context context, List<ViewRisque> risques) {
        super(context, 0, risques);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_risque ,parent, false);
        }

        RisqueViewHolder viewHolder = (RisqueViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new RisqueViewHolder();
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.soustitre = (TextView) convertView.findViewById(R.id.soustitre);
            convertView.setTag(viewHolder);
        }
        //getItem(position) va récupérer l'item [position] de la List
        final ViewRisque risques = getItem(position);

        //remplir la vue
        viewHolder.titre.setText(risques.getTitre());
        viewHolder.soustitre.setText(risques.getSousTitre());

        return convertView;
    }

    private class RisqueViewHolder{
        public TextView id ;
        public CheckBox checkbox ;
        public TextView titre ;
        public TextView soustitre ;
    }

}

