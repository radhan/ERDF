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
import com.erdf.classe.metier.Fonction;
import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radhan on 19/04/2016.
 */
public class UtilisateurDAO {
    private static String TAG = UtilisateurDAO.class.getSimpleName()                                ;
    private static DatabaseHelper db                                                                ;
    static String urlAllUtilisateurs = "http://comment-telecharger.eu/ERDF/getAllUtilisateurs.php"  ;
    static String urlSetUtilisateur =  "http://comment-telecharger.eu/ERDF/setUnUtilisateur.php"    ;

    private UtilisateurDAO() {

    }

    public static ArrayList<Utilisateur> getListeUtilisateur(Context pContext) {
        db = new DatabaseHelper(pContext)   ;
        return db.getAllUtilisateurs()      ;
    }

    public static Utilisateur getUtilisateur(Context pContext, String pCode) {
        db = new DatabaseHelper(pContext)   ;
        return db.getUtilisateur(pCode)     ;
    }

    public static int getNbUtilisateur(Context pContext) {
        db = new DatabaseHelper(pContext)   ;
        return db.getNbUtilisateur()        ;
    }

    public static void addUtilisateur(final Context pContext, final Utilisateur pUtilisateur, boolean pOnline) {

        if(pOnline) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSetUtilisateur, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d(TAG, response) ;
                    try {

                        JSONObject oJson = new JSONObject(response) ;
                        if(oJson.getString("RESULTAT").equals("OK")) {
                            Toast.makeText(pContext, "Ajout d'un Utilisateur réussi", Toast.LENGTH_SHORT).show() ;
                            syncGetListeUtilisateur(pContext) ;
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
                    Toast.makeText(pContext, "Erreur lors de l'ajout d'un utilisateur.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<>()                         ;
                    params.put("nom", pUtilisateur.getNom())                            ;
                    params.put("prenom", pUtilisateur.getPrenom())                      ;
                    params.put("password", pUtilisateur.getUnCompte().getPassword())    ;
                    params.put("fonction", pUtilisateur.getUneFonction().getId())       ;
                    params.put("mail", pUtilisateur.getMail())                          ;

                    return params ;
                }
            };

            // On ajoute la requête à la file d'attente
            ConnexionControleur.getInstance().addToRequestQueue(stringRequest) ;
        }
        else {
            db = new DatabaseHelper(pContext)   ;
            db.addUtilisateur(pUtilisateur)     ;
        }
    }

    public static void updateUtilisateur(final Context pContext, final Utilisateur pUtilisateur, boolean pOnline) {
        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {

        } else {
            db = new DatabaseHelper(pContext)   ;
            db.updateUtilisateur(pUtilisateur)  ;
        }
    }

    public static void deleteUtilisateur(final Context pContext, final Utilisateur pUtilisateur, boolean pOnline) {
        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {

        } else {
            db = new DatabaseHelper(pContext)   ;
            db.deleteUtilisateur(pUtilisateur)  ;
        }
    }

    public static void syncGetListeUtilisateur(final Context pContext) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllUtilisateurs, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString()) ;

                try {

                    // On parcours les données reçues
                    for(int i = 1; i < response.length() + 1; i++) {
                        JSONObject oUtilisateur = response.getJSONObject(Integer.toString(i)) ;

                        //On déclare l'objet fonction
                        boolean supprimerFonction = oUtilisateur.getInt("fon_supprimer") > 0 ;
                        Fonction uneFonction = new Fonction(oUtilisateur.getString("fon_id"), oUtilisateur.getString("fon_libelle"), supprimerFonction);

                        boolean supprimerUti = oUtilisateur.getInt("use_supprimer") > 0 ;
                        Utilisateur unUtilisateur = new Utilisateur(oUtilisateur.getString("use_id"), oUtilisateur.getString("use_nom"), oUtilisateur.getString("use_prenom"), oUtilisateur.getString("use_mail"), uneFonction, supprimerUti) ;
                        addUtilisateur(pContext, unUtilisateur, false) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace() ;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError pError) {
                Toast.makeText(pContext, "Erreur de Synchronisation des utilisateurs.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
            }
        });

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(jsonObjReq) ;
    }

}
