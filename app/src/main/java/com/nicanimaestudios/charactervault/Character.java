package com.nicanimaestudios.charactervault;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class Character extends AppCompatActivity {

    private ImageView ivPersonaje;
    private EditText etNombre, etEdad, etRaza, etSexo, etApodo, etPoderes, etPadre, etMadre, etHistoria;
    private TextView tvGuardar;

    private Uri imageUri = null;
    private ActivityResultLauncher<String> galleryLauncher;

    private static final String PREFS_NAME = "FichaPersonajePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        initViews();
        setupGalleryLauncher();
        loadData();

        ivPersonaje.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        tvGuardar.setOnClickListener(v -> saveData());
    }

    private void initViews() {
        ivPersonaje = findViewById(R.id.iv_personaje);
        etNombre = findViewById(R.id.et_nombre_personaje);
        etEdad = findViewById(R.id.at_1);
        etRaza = findViewById(R.id.at_2);
        etSexo = findViewById(R.id.at_3);
        etApodo = findViewById(R.id.at_4);
        etPoderes = findViewById(R.id.at_5);
        etPadre = findViewById(R.id.at_6);
        etMadre = findViewById(R.id.at_7);
        etHistoria = findViewById(R.id.et_historia);
        tvGuardar = findViewById(R.id.tv_guardar);
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        ivPersonaje.setImageURI(uri);
                        // Persistir permiso de lectura de la imagen para cargas futuras
                        try {
                            getContentResolver().takePersistableUriPermission(
                                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("nombre", etNombre.getText().toString());
        editor.putString("edad", etEdad.getText().toString());
        editor.putString("raza", etRaza.getText().toString());
        editor.putString("sexo", etSexo.getText().toString());
        editor.putString("apodo", etApodo.getText().toString());
        editor.putString("poderes", etPoderes.getText().toString());
        editor.putString("padre", etPadre.getText().toString());
        editor.putString("madre", etMadre.getText().toString());
        editor.putString("historia", etHistoria.getText().toString());

        if (imageUri != null) {
            editor.putString("imagen_uri", imageUri.toString());
        }

        editor.apply();
        Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        etNombre.setText(prefs.getString("nombre", ""));
        etEdad.setText(prefs.getString("edad", ""));
        etRaza.setText(prefs.getString("raza", ""));
        etSexo.setText(prefs.getString("sexo", ""));
        etApodo.setText(prefs.getString("apodo", ""));
        etPoderes.setText(prefs.getString("poderes", ""));
        etPadre.setText(prefs.getString("padre", ""));
        etMadre.setText(prefs.getString("madre", ""));
        etHistoria.setText(prefs.getString("historia", ""));

        String savedUriString = prefs.getString("imagen_uri", null);
        if (savedUriString != null) {
            imageUri = Uri.parse(savedUriString);
            ivPersonaje.setImageURI(imageUri);
        }
    }
}