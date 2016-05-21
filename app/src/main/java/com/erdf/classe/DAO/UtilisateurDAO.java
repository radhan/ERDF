package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.metier.Fonction;
import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Radhan on 19/04/2016.
 */
public class UtilisateurDAO {
    private static String TAG = UtilisateurDAO.class.getSimpleName() ;
    private static DatabaseHelper db ;
    static String urlAllUtilisateurs = "http://comment-telecharger.eu/ERDF/getAllUtilisateurs.php" ;

    private UtilisateurDAO() {

    }

    public static ArrayList<Utilisateur> getListeUtilisateur(Context unContext) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getAllUtilisateurs() ;
    }

    public static Utilisateur getUnUtilisateur(Context unContext, String code) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getUnUtilisateur(code) ;
    }

    public static void setUnUtilisateur(Context unContext, Utilisateur unUtilisateur) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        db.setUnUtilisateur(unUtilisateur) ;
    }

    public static void syncGetListeUtilisateur(final Context unContext) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllUtilisateurs, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    // On parcours les données reçues
                    for(int i = 1; i < response.length() ; i++) {
                        JSONObject oUtilisateur = response.getJSONObject(Integer.toString(i)) ;

                        //On déclare l'objet fonction
                        boolean supprimerFonction = oUtilisateur.getInt("fon_supprimer") > 0 ;
                        Fonction uneFonction = new Fonction(oUtilisateur.getString("fon_id"), oUtilisateur.getString("fon_libelle"), supprimerFonction);


                        boolean supprimerUti = oUtilisateur.getInt("use_supprimer") > 0 ;
                        Utilisateur unUtilisateur = new Utilisateur(oUtilisateur.getString("use_id"), oUtilisateur.getString("use_nom"), oUtilisateur.getString("use_prenom"), oUtilisateur.getString("use_mail"), uneFonction, supprimerUti) ;
                        setUnUtilisateur(unContext, unUtilisateur) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(jsonObjReq);
    }
}
