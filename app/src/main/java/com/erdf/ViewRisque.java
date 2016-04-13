package com.erdf;

import android.widget.CheckBox;

/**
 * Created by Radhan on 15/03/2016.
 */
public class ViewRisque {
    public int id ;
    public CheckBox checkbox ;
    public String titre ;
    public String sousTitre ;

    public ViewRisque() {
    }

    public ViewRisque(String titre, String sousTitre) {
        this.titre = titre;
        this.sousTitre = sousTitre;
    }

    public ViewRisque(int id, CheckBox checkbox, String titre, String sousTitre) {
        this.id = id;
        this.checkbox = checkbox;
        this.titre = titre;
        this.sousTitre = sousTitre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSousTitre() {
        return sousTitre;
    }

    public void setSousTitre(String sousTitre) {
        this.sousTitre = sousTitre;
    }
}
