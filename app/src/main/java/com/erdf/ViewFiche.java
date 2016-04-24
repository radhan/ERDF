package com.erdf;


/**
 * Created by Radhan on 15/03/2016.
 */
public class ViewFiche {
    public int id ;
    public String titre ;
    public String sousTitre ;

    public ViewFiche() {
    }

    public ViewFiche(String nrue, String rue, String codepo, String ville, String sousTitre) {

        this.titre = nrue+" "+rue+" "+codepo+" "+ville ;
        this.sousTitre = sousTitre;
    }

    public ViewFiche(int id, String titre, String sousTitre) {
        this.id = id;
        this.titre = titre;
        this.sousTitre = sousTitre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
