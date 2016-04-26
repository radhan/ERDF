package com.erdf;

/**
 * Created by Alexandre on 26/04/2016.
 */
public class ViewRisqueNc {

    public String titre ;
    public String sousTitre ;

    public ViewRisqueNc() {
    }

    public ViewRisqueNc(String titre, String sousTitre) {
        this.titre = titre;
        this.sousTitre = sousTitre;
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
