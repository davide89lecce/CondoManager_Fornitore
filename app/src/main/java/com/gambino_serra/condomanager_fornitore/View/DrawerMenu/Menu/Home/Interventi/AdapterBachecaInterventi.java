package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.Interventi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;

import java.util.ArrayList;

import static com.gambino_serra.condomanager_fornitore.tesi.R.id.imageView;


public class AdapterBachecaInterventi extends RecyclerView.Adapter<AdapterBachecaInterventi.MyViewHolder> {

    private ArrayList<TicketIntervento> dataset;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSegnalazione;
        ImageView imageViewIcon;
        TextView textViewIdSegnalazione;
        TextView textViewData;
        TextView textStato;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewSegnalazione = (TextView) itemView.findViewById(R.id.textViewSegnalazione);
            this.imageViewIcon = (ImageView) itemView.findViewById(imageView);
            this.textViewIdSegnalazione = (TextView) itemView.findViewById(R.id.textViewIdSegnalazione);
            this.textViewData = (TextView) itemView.findViewById(R.id.textViewData);
            this.textStato = (TextView) itemView.findViewById(R.id.textStato);

        }
    }

    public AdapterBachecaInterventi(ArrayList<TicketIntervento> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO: sistemare card_intervento_bacheca
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_intervento_bacheca, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaInterventi.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewSegnalazione = holder.textViewSegnalazione;
        ImageView imageView = holder.imageViewIcon;
        TextView textViewIdSegnalazione = holder.textViewIdSegnalazione;
        TextView textViewData = holder.textViewData;
        TextView textStato = holder.textStato;

        textViewSegnalazione.setText( dataset.get(listPosition).getOggetto());
        textViewIdSegnalazione.setText(dataset.get(listPosition).getIdTicketIntervento().toString());
        textViewData.setText(dataset.get(listPosition).getDataTicket());
        textStato.setText(dataset.get(listPosition).getStato());

        String stato = dataset.get(listPosition).getStato();

        switch(stato) {

            case "A":
                imageView.setImageResource(R.drawable.invia);
                break;

            case "B":
                imageView.setImageResource(R.drawable.wrench);
                break;

            case "C":
                imageView.setImageResource(R.drawable.wrench);
                break;

            case "D":
                imageView.setImageResource(R.drawable.wrench);
                break;

            case "E":
                imageView.setImageResource(R.drawable.checked);
                break;

            case "F":
                imageView.setImageResource(R.drawable.checked);
                break;

            case "G":
                imageView.setImageResource(R.drawable.error);
                break;
            default:
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
