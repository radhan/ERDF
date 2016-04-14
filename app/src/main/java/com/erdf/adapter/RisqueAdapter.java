package com.erdf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.erdf.R;
import com.erdf.ViewRisque;

import java.util.List;

/**
 * Created by Radhan on 15/03/2016.
 */
public class RisqueAdapter extends ArrayAdapter<ViewRisque> {

    private final List<ViewRisque> risques ;

    public RisqueAdapter(Activity context, List<ViewRisque> risques) {
        super(context, 0, risques) ;
        this.risques = risques ;
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

            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    risques.get(getPosition).setSelected(buttonView.isChecked());

                    View row = (View) buttonView.getParent();
                    if (isChecked) {
                        row.setBackgroundResource(android.R.color.holo_red_light);
                    } else {
                        row.setBackgroundResource(android.R.color.transparent);
                    }
                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.titre, viewHolder.titre);
            convertView.setTag(R.id.soustitre, viewHolder.soustitre);
            convertView.setTag(R.id.checkbox, viewHolder.checkbox);
        } else {
            viewHolder = (RisqueViewHolder) convertView.getTag();
        }
        viewHolder.checkbox.setTag(position);

        //remplir la vue
        viewHolder.titre.setText(getItem(position).getTitre());
        viewHolder.soustitre.setText(getItem(position).getSousTitre());
        viewHolder.checkbox.setChecked(getItem(position).isSelected());

        return convertView;
    }

    private class RisqueViewHolder{
        public TextView id ;
        public CheckBox checkbox ;
        public TextView titre ;
        public TextView soustitre ;
    }

}

