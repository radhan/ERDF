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
import com.erdf.classe.metier.FicheRisque;
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

    /*public static void setUneFiche(final Context pContext, final Fiche uneFiche) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSetFiche, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response) ;
                try {

                    JSONObject oJson = new JSONObject(response) ;
                    if(oJson.getString("RESULTAT").equals("OK")) {
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
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(pContext, error.toString(), Toast.LENGTH_SHORT).show() ;
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //On crééer une chaine de caractère avec les risques
                String risques = "" ;
                for(int i = 0; i < uneFiche.getListeRisque().size() ; i++) {
                    if(!risques.isEmpty()) {
                        risques = risques + "|" ;
                    }
                    risques = risques + uneFiche.getListeRisque().get(i).getId() ;
                }

                Map<String,String> params = new HashMap<>() ;
                params.put("chantier", uneFiche.getUnChantier().getCode()) ;
                params.put("utilisateur", uneFiche.getUnUtilisateur().getId()) ;
                params.put("date", uneFiche.getDate()) ;
                params.put("risque", risques) ;

                return params;
            }
        };

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(stringRequest);
    }*/

    public static void syncGetListeFicheRisque(final Context pContext) {
        // SQLite database handler
        db = new DatabaseHelper(pContext) ;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllFicheRisques, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    // On parcours les données reçues
                    for (int i = 1; i < response.length() + 1; i++) {
                        JSONObject oFicheRisque = response.getJSONObject(Integer.toString(i)) ;

                        Fiche uneFiche = FicheDAO.getUneFiche(pContext, oFicheRisque.getString("fri_fiche")) ;
                        Risque unRisque = RisqueDAO.getUnRisque(pContext, oFicheRisque.getString("fri_risque")) ;

                        //On déclare l'objet FicheRisque
                        boolean supprimerFicheRisque = oFicheRisque.getInt("fri_supprimer") > 0 ;
                        FicheRisque uneFicheRisque = new FicheRisque(uneFiche, unRisque, supprimerFicheRisque) ;

                        db.setUneFicheRisque(uneFicheRisque) ;
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
