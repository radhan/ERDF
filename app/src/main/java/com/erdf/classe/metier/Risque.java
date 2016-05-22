package com.erdf.classe.metier;

import java.util.ArrayList;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Risque {
    private String id ;
    private String titre ;
    private String resume ;
    private boolean supprimer ;
    private String createdAt ;
    private ArrayList<Fiche> listeFiche = new ArrayList<>() ;
    public boolean selected ;

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

    public ArrayList<Fiche> getListeFiche() {
        return listeFiche;
    }

    public void setListeFiche(ArrayList<Fiche> listeFiche) {
        this.listeFiche = listeFiche;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
