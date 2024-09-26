package fr.forestba.baseexterne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText etDebut;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        etDebut = findViewById(R.id.etDebut);
    }

    public void rechercher(View v) {
        String ville = etDebut.getText().toString();

        //appeler le service web pour rechercher dans la base
        String urlServiceWeb= "http://172.16.47.13/serveur-web/index.php?nom="+ville;

        //afficher
        tvResult.setText("Communes débute par \"" + ville +  "\" : \n");
        tvResult.append(getServerDataTexteBrut(urlServiceWeb));


    }

    public String getServerDataTexteBrut(String urlAJoindre){
        String res ="";
        String ligne;
        URL url = null;
        //autoriser les opération réseaux sur le thread principal
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            url =new URL(urlAJoindre);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            while ((ligne = bufferedReader.readLine()) != null) {
                res += " - " + ligne + "\n";
            }
        } catch (Exception e) {
            Log.d("Myapp", "Erreur échange avec serveur : "+ e.toString());
            return "";
        }
        return res;
    }
}