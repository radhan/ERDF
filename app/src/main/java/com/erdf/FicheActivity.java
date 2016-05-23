package com.erdf;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.erdf.classe.DAO.ChantierDAO;
import com.erdf.classe.adapter.RisqueAdapter;
import com.erdf.classe.DAO.FicheDAO;
import com.erdf.classe.DAO.RisqueDAO;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.InternetDetection;
import com.erdf.classe.technique.SessionManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FicheActivity extends BaseActivity {

    ArrayList<Risque> listeRisques  = new ArrayList<>() ;

    @InjectView(R.id.sDate)             TextView dateText       ;
    @InjectView(R.id.sAdresse)          TextView adresseText    ;
    @InjectView(R.id.bAdresse)          Button btnAdresse       ;
    @InjectView(R.id.button)            Button btnEnvoyer       ;
    @InjectView(R.id.listView)          ListView listviewRisque ;

    Fiche uneFiche ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On déclare l'objet fiche
        uneFiche = new Fiche() ;

        //Configuration button d'edition
        btnAdresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adresseText.setEnabled(!adresseText.isEnabled());
            }
        });

        //Configuration button d'envoie
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFiche();
            }
        });

        //On récupère la liste des risques et des chantiers
        getRisques() ;

        //On récupère la date et l'heure
        getDate() ;

        //On regarde si on dispose d'une connexion internet
        InternetDetection inter = new InternetDetection(getApplicationContext());
        Boolean isInternetPresent = inter.isConnectingToInternet(); // true or false

        //S'il dispose d'une connexion internet
        if(isInternetPresent) {
            //Si le GPS est activé, alors on va chercher la localisation
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
                getLocalisation() ;
                adresseText.setEnabled(false);
            }
            else {
                adresseText.setEnabled(true);
            }
        }
        else {
            adresseText.setEnabled(true);
        }

    }

    //Méthode qui récupère la date
    private void getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        dateText.setText(dateFormat.format(date));
    }

    //Méthode qui récupère la localisation
    private void getLocalisation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null) {
            getAdresse(location);
        }

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                getAdresse(location);
            }

            public void onProviderDisabled(String arg0) {
            }

            public void onProviderEnabled(String arg0) {
            }

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
    }

    //Méthode permettant d'avoir l'adresse
    private void getAdresse(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder maLocalisation = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> localisation ;
        try {
            localisation = maLocalisation.getFromLocation(latitude, longitude, 1);
            if(localisation.size() == 1) {
                adresseText.setText(localisation.get(0).getAddressLine(0) + ", " + localisation.get(0).getLocality() + ", " + localisation.get(0).getPostalCode()) ;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //On récupère la liste des risques
    public void getRisques() {

        final ArrayList<Risque> listeRisque = RisqueDAO.getListeRisque(getApplicationContext()) ;

        if (!listeRisque.isEmpty()) {
            listviewRisque = (ListView) findViewById(R.id.listView);
            RisqueAdapter adapter = new RisqueAdapter(FicheActivity.this, listeRisque);
            listviewRisque.setAdapter(adapter);

            listviewRisque.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    if (view != null) {
                        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                        checkBox.setChecked(!checkBox.isChecked());

                        Risque unRisque = new Risque(listeRisque.get(position).getId(), listeRisque.get(position).getTitre(), listeRisque.get(position).getResume(), listeRisque.get(position).isSupprimer()) ;

                        if(checkBox.isChecked()) {
                            listeRisques.add(unRisque) ;
                        } else {
                            listeRisques.remove(unRisque) ;
                        }
                    }
                }
            });
        }
    }

    public void setFiche() {

        SessionManager session = new SessionManager(getApplicationContext()) ;
        String idUtilisateur = session.getIdUtilisateur() ;

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date inputDate = null;
        try {
            inputDate = fmt.parse(dateText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create the MySQL datetime string
        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = fmt.format(inputDate);

        //On récupère la liste des chantiers
        ArrayList<Chantier> listeChantier = ChantierDAO.getListeChantier(getApplicationContext()) ;

        //On récupère l'adresse de la géolocalisation dans un tableau
        String adresse[] = adresseText.getText().toString().split(",") ;

        //Si l'adresse n'est pas correcte on affiche un message d'erreur, 3 pour numRue/Rue - Ville - CodePostal
        if(adresse.length < 3) {
            Toast.makeText(getApplicationContext(), "Le format de l'adresse est incorrect.\nVeuillez respecter le format suivant :\n(Numero Rue) Rue, Ville, CodePostal", Toast.LENGTH_SHORT).show();
            return ;
        }

        //On créer un objet Chantier
        Chantier leChantier = new Chantier() ;

        for(Chantier unChantier : listeChantier) {
            //Si le chantier existe déjà
            if((unChantier.getNumRue() + " " + unChantier.getRue()).equals(adresse[0]) && unChantier.getVille().equals(adresse[1]) && unChantier.getCodePostal().equals(adresse[2])) {
                leChantier = unChantier ;
                break ;
            }
        }

        //Si le chantier n'existe pas
        if(leChantier.getCode() == null) {
            //Récupérer le dernier id
            String dernierId = ChantierDAO.getDernierIdChantier(getApplicationContext()) ;
            if(dernierId != null && !dernierId.isEmpty()) {
                dernierId = String.valueOf(Integer.parseInt(dernierId) + 1);
            }

            //On divise le numéro de rue et la rue
            String rue[] = adresse[0].split(" ", 2) ;

            leChantier.setCode(dernierId) ;

            //Si le numéro de rue est renseigné
            if(rue[0].matches(".*\\d.*")){
                leChantier.setNumRue(rue[0]) ;
                leChantier.setRue(rue[1]) ;
            }
            else {
                leChantier.setNumRue("0") ;
                leChantier.setRue(adresse[0]) ;
            }

            leChantier.setLibelle("Bla bla") ;
            leChantier.setVille(adresse[1]) ;
            leChantier.setCodePostal(adresse[2]) ;
            leChantier.setSupprimer(false) ;

            ChantierDAO.setUnChantier(getApplicationContext(), leChantier, true) ;
        }

        Utilisateur unUtilisateur = new Utilisateur() ;
        unUtilisateur.setId(idUtilisateur) ;

        uneFiche.setUnChantier(leChantier) ;
        uneFiche.setUnUtilisateur(unUtilisateur) ;
        uneFiche.setDate(dateString) ;
        uneFiche.setListeRisque(listeRisques);

        FicheDAO.setUneFiche(getApplicationContext(), uneFiche, true);
    }

}
