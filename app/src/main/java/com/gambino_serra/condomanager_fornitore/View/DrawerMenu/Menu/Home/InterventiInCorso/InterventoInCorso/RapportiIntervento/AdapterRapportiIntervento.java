package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso.InterventoInCorso.RapportiIntervento;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gambino_serra.condomanager_fornitore.Model.Entity.CardTicketIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;

import java.util.ArrayList;


public class AdapterRapportiIntervento extends RecyclerView.Adapter<AdapterRapportiIntervento.MyViewHolder> {

    private ArrayList<CardTicketIntervento> dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TidRapportoIntervento;
        TextView Tdata;
        TextView TnotaAmministratore;
        TextView TnotaFornitore;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.Tdata = (TextView) itemView.findViewById(R.id.D_Indirizzo);
            this.TnotaAmministratore = (TextView) itemView.findViewById(R.id.D_Oggetto);
            this.TnotaFornitore = (TextView) itemView.findViewById(R.id.D_Data);
            //Campo nascosto per recuperare il riferimento
            this.TidRapportoIntervento = (TextView) itemView.findViewById(R.id.D_Condominio);
        }
    }

    public AdapterRapportiIntervento(ArrayList<CardTicketIntervento> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_rapporto_intervento, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe RapportiIntervento
        //view.setOnClickListener(BachecaInterventiInCorso.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView Tdata = holder.Tdata;
        TextView TnotaAmministratore = holder.TnotaAmministratore;
        TextView TnotaFornitore = holder.TnotaFornitore;
        TextView TidRapportoIntervento = holder.TidRapportoIntervento;

        try {
            Tdata.setText(dataset.get(listPosition).getNomeStabile());
            TnotaAmministratore.setText(dataset.get(listPosition).getIndirizzoStabile());
            TnotaFornitore.setText(dataset.get(listPosition).getOggetto());
            TidRapportoIntervento.setText(dataset.get(listPosition).getIdTicketIntervento());
        }catch (NullPointerException e){

        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}