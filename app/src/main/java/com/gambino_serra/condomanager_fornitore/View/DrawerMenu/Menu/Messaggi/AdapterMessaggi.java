package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Messaggi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gambino_serra.condomanager_fornitore.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import java.util.ArrayList;
import static com.gambino_serra.condomanager_fornitore.tesi.R.id.imageView;


public class AdapterMessaggi extends RecyclerView.Adapter<AdapterMessaggi.MyViewHolder> {

    private ArrayList<MessaggioCondomino> dataset;

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

    public AdapterMessaggi(ArrayList<MessaggioCondomino> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO: sistemare card_messaggio_bacheca
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_messaggio_bacheca, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaMessaggi.myOnClickListener);

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

        textViewSegnalazione.setText( dataset.get(listPosition).getData());
        textViewIdSegnalazione.setText(dataset.get(listPosition).getMessaggio().toString());
        textViewData.setText(dataset.get(listPosition).getStabile());
        textStato.setText(dataset.get(listPosition).getTipologia());

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
