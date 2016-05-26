package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.FicheRisque;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Radhan on 24/04/2016.
 */
public class FicheRisqueDAO {
    private static String TAG = FicheRisqueDAO.class.getSimpleName() ;
    private static DatabaseHelper db ;
    static String urlAllFicheRisques = "http://comment-telecharger.eu/ERDF/getAllFicheRisques.php" ;
    static String urlSetFicheRisque =  "http://comment-telecharger.eu/ERDF/setUneFiche.php" ;

    private FicheRisqueDAO() {
    }

    public static ArrayList<FicheRisque> getListeFicheRisque(Context unContext) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getAllFicheRisques() ;
    }

    public static ArrayList<FicheRisque> getUneFicheRisqueByFiche(Context unContext, String code) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getUneFicheRisqueByFiche(code) ;
    }

    public static ArrayList<FicheRisque> getUneFicheRisqueByRisque(Context unContext, String code) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getUneFicheRisqueByRisque(code) ;
    }

    public static void setUneFicheRisque(final Context pContext, final FicheRisque uneFicheRisque, boolean online) {

        if(online) {

        }
        else {
            // SQLite database handler
            db = new DatabaseHelper(pContext) ;

            db.setUneFicheRisque(uneFicheRisque) ;
        }
    }

    public static void syncGetListeFicheRisque(final Context pContext) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllFicheRisques, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    // On parcours les données reçues
                    for (int i = 1; i < response.length() + 1; i++) {
                        JSONObject oFicheRisque = response.getJSONObject(Integer.toString(i)) ;

                        Fiche uneFiche = FicheDAO.getUneFiche(pContext, oFicheRisque.getString("fri_fiche"), false) ;
                        Risque unRisque = RisqueDAO.getUnRisque(pContext, oFicheRisque.getString("fri_risque")) ;

                        //On déclare l'objet FicheRisque
                        boolean supprimerFicheRisque = oFicheRisque.getInt("fri_supprimer") > 0 ;
                        FicheRisque uneFicheRisque = new FicheRisque(uneFiche, unRisque, supprimerFicheRisque) ;

                        setUneFicheRisque(pContext, uneFicheRisque, false) ;
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
