package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Risque {
    private String id ;
    private String titre ;
    private String resume ;
    private boolean supprimer ;

    public Risque() {
    }

    public Risque(String id, String titre, String resume, boolean supprimer) {
        this.id = id;
        this.titre = titre;
        this.resume = resume;
        this.supprimer = supprimer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }
}
