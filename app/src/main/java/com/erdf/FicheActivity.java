package com.erdf;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.erdf.adapter.RisqueAdapter;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FicheActivity extends AppCompatActivity implements GetResponse {

    List<ViewRisque> lesRisques = new ArrayList<>() ;
    ListView listviewRisque ;

    ConnexionBDD oConnexion ;

    @InjectView(R.id.tDate) TextView dateText ;
    @InjectView(R.id.tNumero) TextView numeroText ;
    @InjectView(R.id.tAdresse) TextView adresseText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //Configuration button de renvoie
        Button btnEnvoyer = (Button) findViewById(R.id.button) ;
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //On récupère la liste des risques
        oConnexion = new ConnexionBDD("Risque", FicheActivity.this);
        oConnexion.getResponse = FicheActivity.this;
        oConnexion.execute();

        //On récupère la date et l'heure
        getDate() ;

        //On va chercher la localisation
        getLocalisation() ;
    }

    private void getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        dateText.setText("Date : " + dateFormat.format(date));
    }

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
                // TODO Auto-generated method stub
            }

            public void onProviderEnabled(String arg0) {
                // TODO Auto-generated method stub
            }

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                // TODO Auto-generated method stub
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
    }

    private void getAdresse(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder maLocalisation = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> localisation ;
        try {
            localisation = maLocalisation.getFromLocation(latitude, longitude, 1);
            if(localisation.size() == 1) {
                adresseText.setText("Adresse : " + localisation.get(0).getAddressLine(0) + ", " + localisation.get(0).getLocality() + ", " + localisation.get(0).getPostalCode()) ;
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    public Void getData(String resultatJson) {
        if(oConnexion.isJSON()) {
            ParserJSON oParser = new ParserJSON(resultatJson) ;
            for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                ParserJSON oFiche = new ParserJSON(oParser.getOJSON(), Integer.toString(i)) ;

                ViewRisque vRisque = new ViewRisque(oFiche.getString("ris_titre"), oFiche.getString("ris_resume"));
                lesRisques.add(vRisque);
            }
        }

        if(!lesRisques.isEmpty()) {
            listviewRisque = (ListView) findViewById(R.id.listView);
            RisqueAdapter adapter = new RisqueAdapter(FicheActivity.this, lesRisques);
            listviewRisque.setAdapter(adapter);

            listviewRisque.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    if (view != null) {
                        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox);
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                }
            });
        }

        return null ;
    }
}
