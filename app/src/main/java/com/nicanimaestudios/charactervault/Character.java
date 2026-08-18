package com.nicanimaestudios.charactervault;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Character extends LinearLayout {

    private ImageView ivPersonaje;
    private EditText etNombre, etEdad, etRaza, etSexo, etApodo, etPoderes, etPadre, etMadre, etHistoria;
    private TextView tvGuardar;

    private Uri imageUri = null;
    private OnImagePickRequestListener imagePickRequestListener;
    private String currentCharacterName = "";

    public interface OnImagePickRequestListener {
        void onImagePickRequested();
    }

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

        if (ivPersonaje != null) {
            ivPersonaje.setOnClickListener(v -> {
                if (imagePickRequestListener != null) {
                    imagePickRequestListener.onImagePickRequested();
                }
            });
        }

        if (tvGuardar != null) {
            tvGuardar.setOnClickListener(v -> saveData());
        }
    }

    public void setOnImagePickRequestListener(OnImagePickRequestListener listener) {
        this.imagePickRequestListener = listener;
    }

    public void setImageUri(Uri uri) {
        if (uri != null) {
            this.imageUri = uri;
            if (ivPersonaje != null) {
                ivPersonaje.setImageURI(uri);
            }
        }
    }

    /**
     * Establece y carga la información del personaje según su nombre.
     */
    public void setCharacterName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.currentCharacterName = name.trim();
            if (etNombre != null) {
                etNombre.setText(this.currentCharacterName);
            }
            loadData();
        }
    }

    private File getCharacterDirectory(String name) {
        String safeName = name.replaceAll("[^a-zA-Z0-9_-]", "_");
        File baseDir = new File(getContext().getExternalFilesDir(null), "CharacterVault" + File.separator + safeName);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir;
    }

    public void saveData() {
        String nameToSave = etNombre != null ? etNombre.getText().toString().trim() : "";

        if (nameToSave.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        this.currentCharacterName = nameToSave;
        File charDir = getCharacterDirectory(currentCharacterName);

        try {
            // Guardar datos en JSON
            JSONObject json = new JSONObject();
            json.put("nombre", currentCharacterName);
            json.put("edad", etEdad != null ? etEdad.getText().toString() : "");
            json.put("raza", etRaza != null ? etRaza.getText().toString() : "");
            json.put("sexo", etSexo != null ? etSexo.getText().toString() : "");
            json.put("apodo", etApodo != null ? etApodo.getText().toString() : "");
            json.put("poderes", etPoderes != null ? etPoderes.getText().toString() : "");
            json.put("padre", etPadre != null ? etPadre.getText().toString() : "");
            json.put("madre", etMadre != null ? etMadre.getText().toString() : "");
            json.put("historia", etHistoria != null ? etHistoria.getText().toString() : "");

            File jsonFile = new File(charDir, "data.json");
            FileOutputStream fos = new FileOutputStream(jsonFile);
            fos.write(json.toString().getBytes());
            fos.close();

            // Copiar imagen a la carpeta del personaje si fue seleccionada
            if (imageUri != null) {
                File destImage = new File(charDir, "profile.jpg");
                copyUriToFile(imageUri, destImage);
            }

            Toast.makeText(getContext(), "Guardado en CharacterVault/" + currentCharacterName, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadData() {
        if (currentCharacterName.isEmpty()) return;

        File charDir = getCharacterDirectory(currentCharacterName);
        File jsonFile = new File(charDir, "data.json");

        if (jsonFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(jsonFile);
                byte[] buffer = new byte[(int) jsonFile.length()];
                fis.read(buffer);
                fis.close();

                JSONObject json = new JSONObject(new String(buffer, "UTF-8"));

                if (etNombre != null) etNombre.setText(json.optString("nombre", ""));
                if (etEdad != null) etEdad.setText(json.optString("edad", ""));
                if (etRaza != null) etRaza.setText(json.optString("raza", ""));
                if (etSexo != null) etSexo.setText(json.optString("sexo", ""));
                if (etApodo != null) etApodo.setText(json.optString("apodo", ""));
                if (etPoderes != null) etPoderes.setText(json.optString("poderes", ""));
                if (etPadre != null) etPadre.setText(json.optString("padre", ""));
                if (etMadre != null) etMadre.setText(json.optString("madre", ""));
                if (etHistoria != null) etHistoria.setText(json.optString("historia", ""));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Cargar imagen guardada localmente
        File imageFile = new File(charDir, "profile.jpg");
        if (imageFile.exists() && ivPersonaje != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            ivPersonaje.setImageBitmap(bitmap);
        }
    }

    private void copyUriToFile(Uri srcUri, File destFile) {
        try (InputStream in = getContext().getContentResolver().openInputStream(srcUri);
             OutputStream out = new FileOutputStream(destFile)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}