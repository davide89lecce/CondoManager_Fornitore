package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;

import java.util.ArrayList;

import static com.gambino_serra.condomanager_fornitore.tesi.R.id.D_Priorità;
import static com.gambino_serra.condomanager_fornitore.tesi.R.id.Logo_Interv;
import static com.gambino_serra.condomanager_fornitore.tesi.R.id.pin;


public class AdapterBachecaInterventi extends RecyclerView.Adapter<AdapterBachecaInterventi.MyViewHolder> {

    private ArrayList<TicketIntervento> dataset;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mStabile;
        TextView mIndirizzo;
        TextView mOggetto;

        ImageView mLogoPriorità;
        TextView IdTicket;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.mStabile = (TextView) itemView.findViewById(R.id.D_Condominio);
            this.mIndirizzo = (TextView) itemView.findViewById(R.id.D_Indirizzo);
            this.mOggetto = (TextView) itemView.findViewById(R.id.D_OggettoInterv);

            this.mLogoPriorità = (ImageView) itemView.findViewById(D_Priorità);
            //Campo nascosto per recuperare il riferimento
            this.IdTicket = (TextView) itemView.findViewById(R.id.IDTicket);
        }
    }

    public AdapterBachecaInterventi(ArrayList<TicketIntervento> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_intervento_bacheca, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaInterventiInCorso.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView mStabile = holder.mStabile;
        TextView mIndirizzo = holder.mIndirizzo;
        TextView mOggetto = holder.mOggetto;

        ImageView mLogoPriorità = holder.mLogoPriorità;
        TextView IdTicket = holder.IdTicket;


        mStabile.setText(dataset.get(listPosition).getStabile());
        mIndirizzo.setText("PER ORA NON C'E'");
        mOggetto.setText(dataset.get(listPosition).getOggetto());

        IdTicket.setText(dataset.get(listPosition).getIdTicketIntervento());


        // Stringa usata per tenere traccia della priorità e lavorare sullimmagine rappresentata
        String priorità = dataset.get(listPosition).getPriorità();

//TODO : cerca di colorare sia il padding della card che l'immagine sulla parte sinistra
        switch(priorità) {
            // intervento richiesto o rifiutato (al condomino interressa solo che sia stato processato
            // dall'amministratore, se un fornitore lo rifiuterà, lui lo vedrà ancora in attesa
            // di essere preso in carico
            case "1" :
            {
                mLogoPriorità.setBackgroundColor(0xFF00FF00);
                break;
            }

            case "2": // intervento in corso
            {
                mLogoPriorità.setBackgroundColor(0xF220F550);
                break;
            }
            case "3":   // intervento concluso
            {
                mLogoPriorità.setBackgroundColor(0xFF00FF00);
                break;
            }

            default:
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
