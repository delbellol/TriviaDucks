package com.unimib.triviaducks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimib.triviaducks.R;

import java.util.List;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {
    // Lista di risorse immagini per le categorie che verranno mostrate nella RecyclerView
    private List<Integer> images;

    // La classe ViewHolder contiene la vista per ogni elemento in RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // ImageView per mostrare l'immagine della categoria
        private final ImageView imageView;

        // Costruttore del ViewHolder che inizializza l'imageView
        public ViewHolder(@NonNull View view) {
            super(view);
            // Assegna l'ImageView dall'elemento di layout "image_item.xml"
            this.imageView = view.findViewById(R.id.imageView);
        }

        // Metodo getter per ottenere l'ImageView
        public ImageView getImageView() {
            return imageView;
        }
    }

    // Costruttore dell'adapter: riceve una lista di immagini come input
    public CategoriesRecyclerAdapter(List<Integer> images) {
        this.images = images; // Assegna la lista di immagini alla variabile membro
    }

    // Metodo per creare nuovi ViewHolder quando necessario
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla (carica) il layout "image_item.xml" per ogni elemento della RecyclerView
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_item, viewGroup, false);

        // Restituisce una nuova istanza di ViewHolder con la vista appena creata
        return new ViewHolder(view);
    }

    // Metodo per collegare i dati alla vista del ViewHolder in una specifica posizione
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Imposta l'immagine nella ImageView utilizzando la risorsa dalla lista
        viewHolder.getImageView().setImageResource(images.get(position));
    }

    // Metodo che restituisce il numero di elementi nella lista delle immagini
    @Override
    public int getItemCount() {
        return images.size(); // Ritorna la dimensione della lista
    }
}
