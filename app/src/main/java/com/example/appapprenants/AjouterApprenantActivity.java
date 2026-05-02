package com.example.appapprenants;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AjouterApprenantActivity extends AppCompatActivity {

    private EditText nom, prenom;
    private Spinner ville;
    private RadioButton m, f;
    private Button add;
    private RequestQueue requestQueue;

    private static final String URL = "http://10.0.2.2/projet/ws/ajouterApprenant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_apprenant);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);
        add = findViewById(R.id.add);

        requestQueue = Volley.newRequestQueue(this);

        add.setOnClickListener(v -> envoyer());
    }

    private void envoyer() {
        String nomValue = nom.getText().toString().trim();
        String prenomValue = prenom.getText().toString().trim();
        String villeValue = ville.getSelectedItem().toString();
        String sexeValue = m.isChecked() ? "homme" : "femme";

        if (nomValue.isEmpty()) {
            nom.setError("Veuillez entrer le nom");
            nom.requestFocus();
            return;
        }

        if (prenomValue.isEmpty()) {
            prenom.setError("Veuillez entrer le prénom");
            prenom.requestFocus();
            return;
        }

        if (villeValue.equals("Choisir une ville")) {
            Toast.makeText(this, "Veuillez choisir une ville", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!m.isChecked() && !f.isChecked()) {
            Toast.makeText(this, "Veuillez sélectionner le sexe", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                response -> {
                    Toast.makeText(this, "Ajout réussi", Toast.LENGTH_SHORT).show();
                    viderChamps();
                },
                error -> Toast.makeText(
                        this,
                        "Erreur de connexion : " + error.toString(),
                        Toast.LENGTH_LONG
                ).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nom", nomValue);
                params.put("prenom", prenomValue);
                params.put("ville", villeValue);
                params.put("sexe", sexeValue);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void viderChamps() {
        nom.setText("");
        prenom.setText("");
        ville.setSelection(0);
        m.setChecked(false);
        f.setChecked(false);
    }
}