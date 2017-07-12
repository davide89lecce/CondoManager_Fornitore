package com.gambino_serra.condomanager_fornitore.Model.Entity;

/**
 * Created by condomanager_fornitore on 13/02/17.
 */

public class Condominio {

    private String idCondominio;
    private String condominio;
    private String indirizzo;
    private String amministratore;

    public Condominio(String idCondominio, String condominio, String indirizzo, String amministratore) {
        super();
        this.idCondominio = idCondominio;
        this.condominio = condominio;
        this.indirizzo = indirizzo;
        this.amministratore = amministratore;
    }

    public String getIdCondominio() {
        return idCondominio;
    }

    public String getCondominio() {
        return condominio;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getAmministratore() {
        return amministratore;
    }

    public void setIdCondominio(String idCondominio){
        this.idCondominio = idCondominio;
    }

    public void setCondominio(String condominio){
        this.condominio = condominio;
    }

    public void setIndirizzo(String indirizzo){
        this.indirizzo = indirizzo;
    }

    public void setAmministratore(String amministratore){
        this.amministratore = amministratore;
    }

}
