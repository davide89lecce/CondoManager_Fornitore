package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.StoricoInterventi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;

import java.util.ArrayList;

public class AdapterInterventiArchiviati extends RecyclerView.Adapter<AdapterInterventiArchiviati.MyViewHolder> {

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

            this.Tstabile = (TextView) itemView.findViewById(R.id.D_Condominio);
            this.Tindirizzo = (TextView) itemView.findViewById(R.id.D_Indirizzo); //TODO: Aggiungere indirizzo
            this.Toggetto = (TextView) itemView.findViewById(R.id.D_Oggetto);
            //Campo nascosto per recuperare il riferimento
            this.IdTicket = (TextView) itemView.findViewById(R.id.D_IDIntervento);
        }
    }

    public AdapterInterventiArchiviati(ArrayList<TicketIntervento> dataset) {
        this.dataset = dataset;
    }

    @Override
    public AdapterInterventiArchiviati.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_intervento_archiviato, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaInterventiArchiviati.myOnClickListener);

        AdapterInterventiArchiviati.MyViewHolder myViewHolder = new AdapterInterventiArchiviati.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterInterventiArchiviati.MyViewHolder holder, final int listPosition) {

        TextView Tstabile = holder.Tstabile;
        //TextView Tindirizzo = holder.Tindirizzo;
        TextView Toggetto = holder.Toggetto;
        TextView IdTicket = holder.IdTicket;

        Tstabile.setText(dataset.get(listPosition).getStabile());
        //Tindirizzo.setText("INDIRIZZO CHE PER ORA NON C'E'");
        Toggetto.setText(dataset.get(listPosition).getOggetto());
        IdTicket.setText(dataset.get(listPosition).getIdTicketIntervento());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
