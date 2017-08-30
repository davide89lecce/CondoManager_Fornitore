package com.gambino_serra.condomanager_fornitore.Model.Entity;


public class MessaggioCondomino {

    private String id;
    private String data;
    private String tipologia;
    private String messaggio;
    private String uidCondomino;
    private String uidAmministratore;
    private String stabile;
    private String foto;

    public MessaggioCondomino(String id, String data, String tipologia, String messaggio, String uidCondomino, String uidAmministratore, String stabile, String foto) {
        this.id = id;
        this.data = data;
        this.tipologia = tipologia;
        this.messaggio = messaggio;
        this.uidCondomino = uidCondomino;
        this.uidAmministratore = uidAmministratore;
        this.stabile = stabile;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getUidCondomino() {
        return uidCondomino;
    }

    public void setUidCondomino(String uidCondomino) {
        this.uidCondomino = uidCondomino;
    }

    public String getUidAmministratore() {
        return uidAmministratore;
    }

    public void setUidAmministratore(String uidAmministratore) {
        this.uidAmministratore = uidAmministratore;
    }

    public String getStabile() {
        return stabile;
    }

    public void setStabile(String stabile) {
        this.stabile = stabile;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }


}
