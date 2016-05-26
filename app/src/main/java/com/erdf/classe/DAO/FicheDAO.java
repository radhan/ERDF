package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Fonction;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radhan on 24/04/2016.
 */
public class FicheDAO {
    private static String TAG = FicheDAO.class.getSimpleName()                          ;
    private static DatabaseHelper db                                                    ;
    static String urlAllFiches = "http://comment-telecharger.eu/ERDF/getAllFiches.php"  ;
    static String urlSetFiche =  "http://comment-telecharger.eu/ERDF/setUneFiche.php"   ;

    private FicheDAO() {
    }

    public static ArrayList<Fiche> getListeFiche(Context pContext, boolean pRisques) {
        db = new DatabaseHelper(pContext)   ;
        return db.getAllFiches(pRisques)    ;
    }

    public static Fiche getFiche(Context pContext, String pCode, boolean pRisques) {
        db = new DatabaseHelper(pContext)   ;
        return db.getFiche(pCode, pRisques) ;
    }

    public static void addFiche(final Context pContext, final Fiche pFiche, boolean pOnline) {

        if(pOnline) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSetFiche, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d(TAG, response) ;

                    try {

                        JSONObject oJson = new JSONObject(response) ;

                        if (oJson.getString("RESULTAT").equals("OK")) {
                            Toast.makeText(pContext, "Ajout d'une fiche réussi", Toast.LENGTH_SHORT).show() ;
                            syncGetListeFiche(pContext) ;
                        }
                        else {
                            Toast.makeText(pContext, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show() ;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace() ;
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError pError) {
                    Toast.makeText(pContext, "Erreur lors de l'ajout d'une fiche.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    //On crééer une chaine de caractère avec les risques
                    String risques = "" ;
                    for (int i = 0; i < pFiche.getListeRisque().size(); i++) {
                        if (!risques.isEmpty()) {
                            risques = risques + "|" ;
                        }
                        risques = risques + pFiche.getListeRisque().get(i).getId() ;
                    }

                    Map<String, String> params = new HashMap<>()                ;
                    params.put("chantier", pFiche.getUnChantier().getCode())    ;
                    params.put("utilisateur", pFiche.getUnUtilisateur().getId());
                    params.put("date", pFiche.getDate())                        ;
                    params.put("risque", risques)                               ;

                    return params ;
                }
            };

            // On ajoute la requête à la file d'attente
            ConnexionControleur.getInstance().addToRequestQueue(stringRequest) ;
        }
        else {
            db = new DatabaseHelper(pContext)   ;
            db.addFiche(pFiche)                 ;
        }
    }

    public static void syncGetListeFiche(final Context pContext) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllFiches, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString()) ;

                try {

                    ArrayList<Risque> listeRisque = new ArrayList<>() ;

                    // On parcours les données reçues
                    for (int i = 1; i < response.length() + 1; i++) {
                        JSONObject oFiche = response.getJSONObject(Integer.toString(i)) ;
                        listeRisque.clear() ;

                        //On déclare l'objet chantier
                        boolean supprimerChantier = oFiche.getInt("cha_supprimer") > 0 ;
                        Chantier unChantier = new Chantier(oFiche.getString("cha_code"), oFiche.getString("cha_libelle"), oFiche.getString("cha_nrue"), oFiche.getString("cha_rue"), oFiche.getString("cha_ville"), oFiche.getString("cha_codepo"), supprimerChantier);

                        //On déclare l'objet fonction
                        boolean supprimerFonction = oFiche.getInt("fon_supprimer") > 0 ;
                        Fonction uneFonction = new Fonction(oFiche.getString("fon_id"), oFiche.getString("fon_libelle"), supprimerFonction);

                        //On déclare l'objet utilisateur
                        boolean supprimerUtilisateur = oFiche.getInt("use_supprimer") > 0 ;
                        Utilisateur unUtilisateur = new Utilisateur(oFiche.getString("use_id"), oFiche.getString("use_nom"), oFiche.getString("use_prenom"), oFiche.getString("use_mail"), uneFonction, supprimerUtilisateur);

                        //Si le nombre de risques dans le fiche est supérieur à 0
                        if(oFiche.getInt("nbRisque") > 0) {
                            for (int j = 0; j < oFiche.getInt("nbRisque"); j++) {
                                JSONObject oRisque = oFiche.getJSONObject(Integer.toString(j)) ;
                                //On déclare l'objet risque
                                boolean supprimerRisque = oRisque.getInt("ris_supprimer") > 0 ;
                                Risque unRisque = new Risque(oRisque.getString("ris_id"), oRisque.getString("ris_titre"), oRisque.getString("ris_resume"), supprimerRisque);
                                listeRisque.add(unRisque) ;
                            }
                        }

                        //On déclare l'objet Fiche
                        boolean supprimerFiche = oFiche.getInt("fic_supprimer") > 0 ;
                        Fiche uneFiche = new Fiche(oFiche.getString("fic_id"), unChantier, unUtilisateur, oFiche.getString("fic_date"), supprimerFiche) ;
                        uneFiche.setListeRisque(listeRisque) ;

                        addFiche(pContext, uneFiche, false) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace() ;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError pError) {
                Toast.makeText(pContext, "Erreur de Synchronisation des fiche.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
            }
        });

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(jsonObjReq) ;
    }
}
