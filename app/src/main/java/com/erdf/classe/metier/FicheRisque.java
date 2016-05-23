package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class FicheRisque {
    private Fiche uneFiche ;
    private Risque unRisque ;
    private boolean supprimer ;
    private String createdAt ;

    public FicheRisque() {
    }

    public FicheRisque(Fiche uneFiche, Risque unRisque, boolean supprimer) {
        this.uneFiche = uneFiche;
        this.unRisque = unRisque;
        this.supprimer = supprimer;
    }

    public Fiche getUneFiche() {
        return uneFiche;
    }

    public void setUneFiche(Fiche uneFiche) {
        this.uneFiche = uneFiche;
    }

    public Risque getUnRisque() {
        return unRisque;
    }

    public void setUnRisque(Risque unRisque) {
        this.unRisque = unRisque;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
