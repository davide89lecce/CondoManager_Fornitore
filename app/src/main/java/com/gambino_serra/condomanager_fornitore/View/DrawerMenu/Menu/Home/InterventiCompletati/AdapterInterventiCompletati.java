package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiCompletati;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import java.util.ArrayList;

public class AdapterInterventiCompletati extends RecyclerView.Adapter<AdapterInterventiCompletati.MyViewHolder> {

    private ArrayList<TicketIntervento> dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TidTicketIntervento;
        TextView TuidAmministratore;
        TextView TdataTicket;
        TextView TdataUltimoAggiornamento;
        TextView Tfornitore;
        TextView TmessaggioCondomino;
        TextView TaggiornamentoCondomini;
        TextView TdescrizioneCondomini;
        TextView Toggetto;
        TextView TrapportiIntervento;
        TextView Trichiesta;
        TextView Tstabile;
        TextView Tstato;
        TextView Tpriorità;
        TextView Tindirizzo;
        TextView IdTicket;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.TdataTicket = (TextView) itemView.findViewById(R.id.DataAgg_Interv);
            this.TdataUltimoAggiornamento = (TextView) itemView.findViewById(R.id.D_UltimoRapporto);
            this.Tstabile = (TextView) itemView.findViewById(R.id.D_Condominio);
            this.Toggetto = (TextView) itemView.findViewById(R.id.D_OggettoInterv);
            //Campo nascosto per recuperare il riferimento
            this.IdTicket = (TextView) itemView.findViewById(R.id.IDTicket);
        }
    }

    public AdapterInterventiCompletati(ArrayList<TicketIntervento> dataset) {
        this.dataset = dataset;
    }

    @Override
    public AdapterInterventiCompletati.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_intervento_completato, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaInterventiCompletati.myOnClickListener);

        AdapterInterventiCompletati.MyViewHolder myViewHolder = new AdapterInterventiCompletati.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterInterventiCompletati.MyViewHolder holder, final int listPosition) {

        TextView TdataTicket = holder.TdataTicket;
        TextView TdataUltimoAggiornamento = holder.TdataUltimoAggiornamento;
        TextView Tstabile = holder.Tstabile;
        TextView Toggetto = holder.Toggetto;
        TextView IdTicket = holder.IdTicket;

        TdataTicket.setText(dataset.get(listPosition).getDataTicket());
        TdataUltimoAggiornamento.setText(dataset.get(listPosition).getDataUltimoAggiornamento());
        Tstabile.setText(dataset.get(listPosition).getStabile());
        Toggetto.setText(dataset.get(listPosition).getOggetto());
        IdTicket.setText(dataset.get(listPosition).getIdTicketIntervento());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
