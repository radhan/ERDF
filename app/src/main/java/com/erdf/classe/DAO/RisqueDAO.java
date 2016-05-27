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
import com.erdf.classe.metier.Risque;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radhan on 24/04/2016.
 */
public class RisqueDAO {
    private static String TAG = RisqueDAO.class.getSimpleName()                             ;
    private static DatabaseHelper db                                                        ;
    static String urlAllRisques = "http://comment-telecharger.eu/ERDF/getAllRisques.php"    ;
    static String urlSetRisque = "http://comment-telecharger.eu/ERDF/setUnRisque.php"    ;

    private RisqueDAO() {

    }

    public static ArrayList<Risque> getListeRisque(Context pContext, boolean pFiches) {
        db = new DatabaseHelper(pContext)   ;
        return db.getAllRisques(pFiches)    ;
    }

    public static Risque getRisque(Context pContext, String pCode, boolean pFiches) {
        db = new DatabaseHelper(pContext)   ;
        return db.getRisque(pCode, pFiches) ;
    }

    public static int getNbRisque(Context pContext) {
        db = new DatabaseHelper(pContext)   ;
        return db.getNbRisque()             ;
    }

    public static void addRisque(final Context pContext,final Risque pRisque, boolean pOnline) {
        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSetRisque, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d(TAG, response) ;
                    try {

                        JSONObject oJson = new JSONObject(response) ;
                        if(oJson.getString("RESULTAT").equals("OK")) {
                            Toast.makeText(pContext, "Ajout d'un Risque réussi", Toast.LENGTH_SHORT).show() ;
                            syncGetListeRisque(pContext) ;
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
                    Toast.makeText(pContext, "Erreur lors de l'ajout d'un risque.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<>()                         ;
                    params.put("titre", pRisque.getTitre())                            ;
                    params.put("sstitre", pRisque.getResume())                      ;

                    return params ;
                }
            };

            // On ajoute la requête à la file d'attente
            ConnexionControleur.getInstance().addToRequestQueue(stringRequest) ;

        }
        else {
            db = new DatabaseHelper(pContext);
            db.addRisque(pRisque);
        }
    }

    public static void updateRisque(final Context pContext, final Risque pRisque, boolean pOnline) {
        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {

        }
        else {
            db = new DatabaseHelper(pContext)   ;
            db.updateRisque(pRisque)            ;
        }
    }

    public static void deleteChantier(final Context pContext, final Risque pRisque, boolean pOnline) {
        //Si c'est en ligne alors on ajoute à MySql
        if(pOnline) {

        }
        else {
            db = new DatabaseHelper(pContext)   ;
            db.deleteRisque(pRisque)            ;
        }
    }

    public static void syncGetListeRisque(final Context pContext) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllRisques, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString()) ;

                try {

                    // On parcours les données reçues
                    for(int i = 1; i < response.length() + 1; i++) {
                        JSONObject oRisque = response.getJSONObject(Integer.toString(i)) ;
                        boolean supprimer = oRisque.getInt("ris_supprimer") > 0 ;
                        Risque unRisque = new Risque(oRisque.getString("ris_id"), oRisque.getString("ris_titre"), oRisque.getString("ris_resume"), supprimer);
                        addRisque(pContext, unRisque, false) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace() ;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError pError) {
                Toast.makeText(pContext, "Erreur de Synchronisation des risques.\n" + pError.toString(), Toast.LENGTH_SHORT).show() ;
            }
        });

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(jsonObjReq) ;
    }

}
