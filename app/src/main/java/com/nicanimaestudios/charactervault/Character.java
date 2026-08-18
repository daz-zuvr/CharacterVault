package com.nicanimaestudios.charactervault;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Character extends LinearLayout {

    private ImageView ivPersonaje;
    private EditText etNombre, etEdad, etRaza, etSexo, etApodo, etPoderes, etPadre, etMadre, etHistoria;
    private TextView tvGuardar;

    private Uri imageUri = null;
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

        if (tvGuardar != null) {
            tvGuardar.setOnClickListener(v -> saveData());
        }

        loadData();
    }

    public void setImageUri(Uri uri) {
        if (uri != null) {
            this.imageUri = uri;
            if (ivPersonaje != null) {
                ivPersonaje.setImageURI(uri);
            }
        }
    }

    public void setOnImageClickListener(OnClickListener listener) {
        if (ivPersonaje != null) {
            ivPersonaje.setOnClickListener(listener);
        }
    }

    public void saveData() {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (etNombre != null) editor.putString("nombre", etNombre.getText().toString());
        if (etEdad != null) editor.putString("edad", etEdad.getText().toString());
        if (etRaza != null) editor.putString("raza", etRaza.getText().toString());
        if (etSexo != null) editor.putString("sexo", etSexo.getText().toString());
        if (etApodo != null) editor.putString("apodo", etApodo.getText().toString());
        if (etPoderes != null) editor.putString("poderes", etPoderes.getText().toString());
        if (etPadre != null) editor.putString("padre", etPadre.getText().toString());
        if (etMadre != null) editor.putString("madre", etMadre.getText().toString());
        if (etHistoria != null) editor.putString("historia", etHistoria.getText().toString());

        if (imageUri != null) {
            editor.putString("imagen_uri", imageUri.toString());
        }

        editor.apply();
        Toast.makeText(getContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (etNombre != null) etNombre.setText(prefs.getString("nombre", ""));
        if (etEdad != null) etEdad.setText(prefs.getString("edad", ""));
        if (etRaza != null) etRaza.setText(prefs.getString("raza", ""));
        if (etSexo != null) etSexo.setText(prefs.getString("sexo", ""));
        if (etApodo != null) etApodo.setText(prefs.getString("apodo", ""));
        if (etPoderes != null) etPoderes.setText(prefs.getString("poderes", ""));
        if (etPadre != null) etPadre.setText(prefs.getString("padre", ""));
        if (etMadre != null) etMadre.setText(prefs.getString("madre", ""));
        if (etHistoria != null) etHistoria.setText(prefs.getString("historia", ""));

        String savedUriString = prefs.getString("imagen_uri", null);
        if (savedUriString != null && ivPersonaje != null) {
            imageUri = Uri.parse(savedUriString);
            ivPersonaje.setImageURI(imageUri);
        }
    }
}