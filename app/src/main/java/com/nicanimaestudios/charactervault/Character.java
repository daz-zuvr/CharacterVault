package com.nicanimaestudios.charactervault;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class Character extends LinearLayout {

    private ImageView ivPersonaje;
    private EditText etNombre, etEdad, etRaza, etSexo, etApodo, etPoderes, etPadre, etMadre, etHistoria;
    private TextView tvGuardar;

    private Uri imageUri = null;
    private ActivityResultLauncher<String> galleryLauncher;
    private static final String PREFS_NAME = "FichaPersonajePrefs";

    public Character(Context context) {
        super(context);
        init(context);
    }

    public Character(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Character(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.activity_character, this, true);

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

        setupGalleryLauncher(context);

        ivPersonaje.setOnClickListener(v -> {
            if (galleryLauncher != null) {
                galleryLauncher.launch("image/*");
            }
        });

        tvGuardar.setOnClickListener(v -> saveData());

        loadData();
    }

    private void setupGalleryLauncher(Context context) {
        if (context instanceof ComponentActivity) {
            ComponentActivity activity = (ComponentActivity) context;
            galleryLauncher = activity.registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            imageUri = uri;
                            ivPersonaje.setImageURI(uri);
                            try {
                                context.getContentResolver().takePersistableUriPermission(
                                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    public void saveData() {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
        Toast.makeText(getContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

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