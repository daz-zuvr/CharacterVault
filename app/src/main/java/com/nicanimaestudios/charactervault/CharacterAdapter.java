package com.nicanimaestudios.charactervault;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private List<CharacterItem> characterList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CharacterItem item);
        void onItemDelete(CharacterItem item);
    }

    public CharacterAdapter(List<CharacterItem> characterList, OnItemClickListener listener) {
        this.characterList = characterList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CharacterItem item = characterList.get(position);
        holder.tvNombre.setText(item.getName());

        if (item.getImageFile() != null && item.getImageFile().exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(item.getImageFile().getAbsolutePath());
            holder.ivPersonaje.setImageBitmap(bitmap);
        } else {
            holder.ivPersonaje.setImageDrawable(null);
        }

        // Clic para abrir el personaje
        holder.containerAbrir.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        // Clic para borrar el personaje
        holder.ivBorrar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return characterList != null ? characterList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPersonaje;
        TextView tvNombre;
        LinearLayout containerAbrir;
        ImageView ivBorrar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPersonaje = itemView.findViewById(R.id.iv_item_personaje);
            tvNombre = itemView.findViewById(R.id.tv_item_nombre);
            containerAbrir = itemView.findViewById(R.id.iv_abrir);
            ivBorrar = itemView.findViewById(R.id.iv_borrar);
        }
    }
}