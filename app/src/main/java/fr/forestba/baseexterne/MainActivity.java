package fr.forestba.baseexterne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

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
        String urlServiceWeb= "http://172.16.47.13//serveur-web/index.php?debut="+ville;

        //afficher
        tvResult.setText("Communes débute par \"" + ville +  "\" : \n");
        tvResult.append(getServerDataJSON(urlServiceWeb));
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

        public String getServerDataJSON(String urlAJoindre) {
        //autoriser les opérations réseau sur le thread principal
        StringBuilder res = new StringBuilder();
        String ligne;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url;
        try {
            url = new URL(urlAJoindre);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            connexion.connect();
            InputStream inputStream = connexion.getInputStream();

            //Récuperer le flux JSON
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder ch = new StringBuilder();
            while ((ligne = bufferedReader.readLine()) != null){
                ch.append(ligne);
            }

            JSONArray jsonArray = new JSONArray(ch.toString());

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                res.append(" - ").append(jsonObject.getString("nom")).append(" - ").append(jsonObject.getString("cp")).append(" - lat : ").append(String.format("%.3f",jsonObject.getDouble("lat"))).append(" - long : ").append(String.format("%.3f",jsonObject.getDouble("lon"))).append("\n");
            }
        } catch (Exception e) {
            Log.d ("MyApp", "Erreur échange avec serveur : "+ e);
        }

        return res.toString();
    }
}