package com.nicanimaestudios.charactervault;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CharacterList extends LinearLayout {

    private RecyclerView rvPersonajes;
    private TextView tvEmpty;
    private Button btnCrear;
    private CharacterAdapter adapter;
    private List<CharacterItem> characterList = new ArrayList<>();
    private OnCharacterClickListener listener;

    public CharacterList(Context context) {
        super(context);
        init(context);
    }

    public CharacterList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CharacterList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.activity_character_list, this, true);

        rvPersonajes = findViewById(R.id.rv_personajes);
        tvEmpty = findViewById(R.id.tv_empty);
        btnCrear = findViewById(R.id.btn_crear_personaje);

        if (rvPersonajes != null) {
            rvPersonajes.setLayoutManager(new LinearLayoutManager(context));
        }

        if (btnCrear != null) {
            btnCrear.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCreateNewCharacterClick();
                }
            });
        }

        loadCharacters();
    }

    public void setOnCharacterClickListener(OnCharacterClickListener listener) {
        this.listener = listener;
    }

    public void loadCharacters() {
        characterList.clear();
        File vaultDir = new File(getContext().getExternalFilesDir(null), "CharacterVault");

        if (vaultDir.exists() && vaultDir.isDirectory()) {
            File[] files = vaultDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && !file.getName().equalsIgnoreCase("Nuevo")) {
                        File imageFile = new File(file, "profile.jpg");
                        characterList.add(new CharacterItem(file.getName(), imageFile));
                    }
                }
            }
        }

        if (characterList.isEmpty()) {
            if (tvEmpty != null) tvEmpty.setVisibility(View.VISIBLE);
            if (rvPersonajes != null) rvPersonajes.setVisibility(View.GONE);
        } else {
            if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
            if (rvPersonajes != null) {
                rvPersonajes.setVisibility(View.VISIBLE);
                adapter = new CharacterAdapter(characterList, item -> {
                    if (listener != null) {
                        listener.onCharacterClick(item.getName());
                    }
                });
                rvPersonajes.setAdapter(adapter);
            }
        }
    }
}