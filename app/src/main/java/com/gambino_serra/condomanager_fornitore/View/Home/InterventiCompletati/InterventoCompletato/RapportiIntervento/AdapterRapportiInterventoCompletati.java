package com.gambino_serra.condomanager_fornitore.View.Home.InterventiCompletati.InterventoCompletato.RapportiIntervento;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.gambino_serra.condomanager_fornitore.Model.Entity.CardRapportoIntervento;
import com.gambino_serra.condomanager_fornitore.View.Dialog.DialogVisualizzaFotoRapporto;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import java.util.ArrayList;


public class AdapterRapportiInterventoCompletati extends RecyclerView.Adapter<AdapterRapportiInterventoCompletati.MyViewHolder> {

    private ArrayList<CardRapportoIntervento> dataset;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TidRapportoIntervento;
        TextView Tdata;
        TextView TnotaAmministratore;
        TextView TnotaFornitore;
        TextView TurlFoto;
        static ImageButton TfotoRapporto;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.Tdata = (TextView) itemView.findViewById(R.id.D_Data);
            this.TnotaAmministratore = (TextView) itemView.findViewById(R.id.D_NotaAmministratore);
            this.TnotaFornitore = (TextView) itemView.findViewById(R.id.D_NotaPersonale);
            this.TfotoRapporto = (ImageButton)  itemView.findViewById(R.id.btnVisualizzaFotoRapporto);
            //Campo nascosto per recuperare il riferimento
            this.TurlFoto = (TextView) itemView.findViewById(R.id.D_IDIntervento);
        }

    }

    public AdapterRapportiInterventoCompletati(ArrayList<CardRapportoIntervento> dataset, Activity activity) {
        this.dataset = dataset;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_rapporto_intervento, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView Tdata = holder.Tdata;
        TextView TnotaAmministratore = holder.TnotaAmministratore;
        TextView TnotaFornitore = holder.TnotaFornitore;
        TextView TidRapportoIntervento = holder.TidRapportoIntervento;
        final TextView TurlFoto = holder.TurlFoto;
        final ImageButton TfotoRapporto = holder.TfotoRapporto;

        MyViewHolder.TfotoRapporto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("foto", dataset.get(listPosition).getFoto());

                DialogVisualizzaFotoRapporto newFragment = new DialogVisualizzaFotoRapporto();
                newFragment.setArguments(bundle);
                newFragment.show(activity.getFragmentManager(), "DialogChiama");
                }
            });

        try
            {
            Tdata.setText(dataset.get(listPosition).getData());
            TnotaAmministratore.setText(dataset.get(listPosition).getNotaAmministratore());
            TnotaFornitore.setText(dataset.get(listPosition).getNotaFornitore());
            TidRapportoIntervento.setText(dataset.get(listPosition).getIdRapportoIntervento());
            TurlFoto.setText(dataset.get(listPosition).getFoto());
            }
        catch (NullPointerException e){ }

        // Nasconte il pulsante se non è presente alcuna foto
        String foto= dataset.get(listPosition).getFoto().toString();

        if (foto.equals("-") ){
            TfotoRapporto.setVisibility(View.INVISIBLE);
            }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
        }

}