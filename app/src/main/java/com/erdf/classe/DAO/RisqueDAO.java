package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    private static String TAG = RisqueDAO.class.getSimpleName()                             ;
    private static DatabaseHelper db                                                        ;
    static String urlAllRisques = "http://comment-telecharger.eu/ERDF/getAllRisques.php"    ;

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

    public static void addRisque(Context pContext, Risque pRisque) {
        db = new DatabaseHelper(pContext)   ;
        db.addRisque(pRisque)               ;
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
                        addRisque(pContext, unRisque) ;
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
