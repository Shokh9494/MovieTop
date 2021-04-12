package com.example.movietop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietop.R;
import com.example.movietop.data.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private ArrayList<Trailer> trailers;
    private OntrailerClickListner ontrailerClickListner;

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
Trailer trailer=trailers.get(position);
holder.textViewNameOfMovies.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public interface OntrailerClickListner {
        void onTrailerClick(String url);
    }


    class TrailerViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameOfMovies;


        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameOfMovies = itemView.findViewById(R.id.textNameOfMovies);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ontrailerClickListner != null) {
                        ontrailerClickListner.onTrailerClick(trailers.get(getAdapterPosition()).getKey());
                    }
                }
            });

        }
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public void setOntrailerClickListner(OntrailerClickListner ontrailerClickListner) {
        this.ontrailerClickListner = ontrailerClickListner;
    }
}
