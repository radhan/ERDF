package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Risque {
    private String titre ;
    private String resume ;

    public Risque() {
    }

    public Risque(String titre, String resume) {
        this.titre = titre;
        this.resume = resume;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
