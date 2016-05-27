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
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radhan on 19/04/2016.
 */
public class ChantierDAO {
    private static String TAG = ChantierDAO.class.getSimpleName()                               ;
    private static DatabaseHelper db                                                            ;
    static String urlAllChantiers = "http://comment-telecharger.eu/ERDF/getAllChantiers.php"    ;
    static String urlSetChantier = "http://comment-telecharger.eu/ERDF/setUnChantier.php"       ;

    private ChantierDAO() {

    }

    public static ArrayList<Chantier> getListeChantier(Context pContext) {
        db = new DatabaseHelper(pContext)   ;
        return db.getAllChantiers()         ;
    }

    public static Chantier getChantier(Context pContext, String pCode) {
        db = new DatabaseHelper(pContext)   ;
        return db.getChantier(pCode)        ;
    }

    public static String getDernierIdChantier(Context pContext) {
        db = new DatabaseHelper(pContext)   ;
        return db.getDernierIdChantier()    ;
    }

    public static int getNbChantier(Context pContext) {
        db = new DatabaseHelper(pContext)   ;
        return db.getNbChantier()    ;
    }

    public static void addChantier(final Context pContext, final Chantier pChantier, boolean pOnline) {

        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSetChantier, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d(TAG, response) ;

                    try {

                        JSONObject oJson = new JSONObject(response) ;

                        if (oJson.getString("RESULTAT").equals("OK")) {
                            Toast.makeText(pContext, "Ajout d'un chantier réussi", Toast.LENGTH_SHORT).show()   ;
                            syncGetListeChantier(pContext)                                                      ;
                        }
                        else {
                            Toast.makeText(pContext, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show()       ;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace() ;
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError pError) {
                    Toast.makeText(pContext, "Erreur lors de l'ajout d'un chantier.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>()        ;
                    params.put("code", pChantier.getCode())             ;
                    params.put("numRue", pChantier.getNumRue())         ;
                    params.put("rue", pChantier.getRue())               ;
                    params.put("ville", pChantier.getVille())           ;
                    params.put("codePostal", pChantier.getCodePostal()) ;

                    return params ;
                }
            };

            // On ajoute la requête à la file d'attente
            ConnexionControleur.getInstance().addToRequestQueue(stringRequest) ;
        }
        else {
            db = new DatabaseHelper(pContext)   ;
            db.addChantier(pChantier)           ;
        }
    }

    public static void updateChantier(final Context pContext, final Chantier pChantier, boolean pOnline) {

        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {

        }
        else {
            db = new DatabaseHelper(pContext)   ;
            db.updateChantier(pChantier)        ;
        }
    }

    public static void deleteChantier(final Context pContext, final Chantier pChantier, boolean pOnline) {

        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {

        }
        else {
            db = new DatabaseHelper(pContext)   ;
            db.deleteChantier(pChantier)        ;
        }
    }

    public static void syncGetListeChantier(final Context pContext) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllChantiers, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString()) ;

                try {

                    // On parcours les données reçues
                    for(int i = 1; i < response.length() + 1; i++) {
                        JSONObject oChantier = response.getJSONObject(Integer.toString(i)) ;
                        boolean supprimer = oChantier.getInt("cha_supprimer") > 0 ;
                        Chantier unChantier = new Chantier(oChantier.getString("cha_code"), oChantier.getString("cha_nrue"), oChantier.getString("cha_rue"), oChantier.getString("cha_ville"), oChantier.getString("cha_codepo"), supprimer) ;
                        addChantier(pContext, unChantier, false) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace() ;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError pError) {
                Toast.makeText(pContext, "Erreur de Synchronisation des chantiers.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
            }
        });

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(jsonObjReq) ;
    }
}
