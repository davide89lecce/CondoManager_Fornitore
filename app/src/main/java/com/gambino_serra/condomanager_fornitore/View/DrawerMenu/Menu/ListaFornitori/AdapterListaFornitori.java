package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambino_serra.condomanager_fornitore.Model.Entity.Fornitore;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import java.util.ArrayList;


public class AdapterListaFornitori extends RecyclerView.Adapter<AdapterListaFornitori.MyViewHolder> {

    private ArrayList<Fornitore> dataset;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUidFornitore;
        TextView textViewDettaglioFornNome;
        TextView DettaglioFornNome;
        TextView textViewDettaglioFornCategoria;
        TextView DettaglioFornCategoria;
        ImageView imageViewFornitore;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewUidFornitore = (TextView) itemView.findViewById(R.id.textViewUidFornitore);
            this.textViewDettaglioFornNome = (TextView) itemView.findViewById(R.id.textViewDettaglioFornNome);
            this.DettaglioFornNome = (TextView) itemView.findViewById(R.id.DettaglioFornNome);
            this.textViewDettaglioFornCategoria = (TextView) itemView.findViewById(R.id.textViewDettaglioFornCategoria);
            this.DettaglioFornCategoria = (TextView) itemView.findViewById(R.id.DettaglioFornCategoria);
            this.imageViewFornitore = (ImageView) itemView.findViewById(R.id.imageViewFornitore);
        }
    }

    public AdapterListaFornitori(ArrayList<Fornitore> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fornitore_bacheca, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe listafornitori
        view.setOnClickListener(ListaFornitori.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        //TextView textViewDettaglioFornNome = holder.textViewDettaglioFornNome;
        TextView DettaglioFornNome = holder.DettaglioFornNome;
        //TextView textViewDettaglioFornCategoria = holder.textViewDettaglioFornCategoria;
        TextView DettaglioFornCategoria = holder.DettaglioFornCategoria;
        //ImageView imageViewFornitore = holder.imageViewFornitore;
        TextView textViewUidFornitore = holder.textViewUidFornitore;

        textViewUidFornitore.setText(dataset.get(listPosition).getUid().toString());
        //textViewDettaglioFornNome.setText( dataset.get(listPosition).getData());
        DettaglioFornNome.setText(dataset.get(listPosition).getNome_azienda().toString());
        //textViewDettaglioFornCategoria.setText(dataset.get(listPosition).getStabile());
        DettaglioFornCategoria.setText(dataset.get(listPosition).getCategoria());

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}