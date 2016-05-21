package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Radhan on 24/04/2016.
 */
public class RisqueDAO {
    private static String TAG = RisqueDAO.class.getSimpleName() ;
    private static DatabaseHelper db ;
    static String urlAllRisques = "http://comment-telecharger.eu/ERDF/getAllRisques.php" ;

    private RisqueDAO() {

    }

    public static ArrayList<Risque> getListeRisque(Context unContext) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getAllRisques() ;
    }

    public static Risque getUnRisque(Context unContext, String code) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getUnRisque(code) ;
    }

    public static void setUnRisque(Context unContext, Risque unRisque) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        db.setUnRisque(unRisque) ;
    }

    public static void syncGetListeRisque(final Context unContext) {


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllRisques, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    // On parcours les données reçues
                    for(int i = 1; i < response.length() + 1; i++) {
                        JSONObject oRisque = response.getJSONObject(Integer.toString(i)) ;
                        boolean supprimer = oRisque.getInt("ris_supprimer") > 0 ;
                        Risque unRisque = new Risque(oRisque.getString("ris_id"), oRisque.getString("ris_titre"), oRisque.getString("ris_resume"), supprimer);
                        setUnRisque(unContext, unRisque) ;
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
