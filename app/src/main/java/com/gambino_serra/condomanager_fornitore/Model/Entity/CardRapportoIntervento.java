package com.gambino_serra.condomanager_fornitore.Model.Entity;


public class CardRapportoIntervento {

    private String idTicket;
    private String data;
    private String notaAmministratore;
    private String notaFornitore;

    public CardRapportoIntervento(String idTicket, String data, String notaAmministratore, String notaFornitore) {
        this.idTicket = idTicket;
        this.data = data;
        this.notaAmministratore = notaAmministratore;
        this.notaFornitore = notaFornitore;
    }


    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNotaAmministratore() {
        return notaAmministratore;
    }

    public void setNotaAmministratore(String notaAmministratore) {
        this.notaAmministratore = notaAmministratore;
    }

    public String getNotaFornitore() {
        return notaFornitore;
    }

    public void setNotaFornitore(String notaFornitore) {
        this.notaFornitore = notaFornitore;
    }
}
