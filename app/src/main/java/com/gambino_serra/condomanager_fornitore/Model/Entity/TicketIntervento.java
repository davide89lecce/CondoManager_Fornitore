package com.gambino_serra.condomanager_fornitore.Model.Entity;


public class TicketIntervento {

    private String idTicketIntervento;
    private String uidAmministratore;
    private String dataTicket;
    private String dataUltimoAggiornamento;
    private String fornitore;
    private String aggiornamentoCondomini;
    private String descrizioneCondomini;
    private String oggetto;
    private String richiesta;
    private String stabile;
    private String stato;
    private String priorità;
    private String foto;
    private String nomeAmministratore;
    private String nomeStabile;
    private String indirizzoStabile;
    private String latitudine;
    private String longitudine;

    public TicketIntervento(String idTicketIntervento,
                            String uidAmministratore,
                            String dataTicket,
                            String dataUltimoAggiornamento,
                            String fornitore,
                            String aggiornamentoCondomini,
                            String descrizioneCondomini,
                            String oggetto,
                            String richiesta,
                            String stabile,
                            String stato,
                            String priorità,
                            String foto,
                            String nomeAmministratore,
                            String nomeStabile,
                            String indirizzoStabile,
                            String latitudine,
                            String longitudine
    ) {

        this.idTicketIntervento = idTicketIntervento;
        this.uidAmministratore = uidAmministratore;
        this.dataTicket = dataTicket;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
        this.fornitore = fornitore;
        this.aggiornamentoCondomini = aggiornamentoCondomini;
        this.descrizioneCondomini = descrizioneCondomini;
        this.oggetto = oggetto;
        this.richiesta = richiesta;
        this.stabile = stabile;
        this.stato = stato;
        this.priorità = priorità;
        this.foto = foto;
        this.nomeAmministratore = nomeAmministratore;
        this.nomeStabile = nomeStabile;
        this.indirizzoStabile = indirizzoStabile;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }



    public String getIdTicketIntervento() {
        return idTicketIntervento;
    }

    public void setIdTicketIntervento(String idTicketIntervento) {
        this.idTicketIntervento = idTicketIntervento;
    }

    public String getUidAmministratore() {
        return uidAmministratore;
    }

    public void setUidAmministratore(String uidAmministratore) {
        this.uidAmministratore = uidAmministratore;
    }

    public String getDataTicket() {
        return dataTicket;
    }

    public void setDataTicket(String dataTicket) {
        this.dataTicket = dataTicket;
    }

    public String getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setDataUltimoAggiornamento(String dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getAggiornamentoCondomini() {
        return aggiornamentoCondomini;
    }

    public void setAggiornamentoCondomini(String aggiornamentoCondomini) {
        this.aggiornamentoCondomini = aggiornamentoCondomini;
    }

    public String getDescrizioneCondomini() {
        return descrizioneCondomini;
    }

    public void setDescrizioneCondomini(String descrizioneCondomini) {
        this.descrizioneCondomini = descrizioneCondomini;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(String richiesta) {
        this.richiesta = richiesta;
    }

    public String getStabile() {
        return stabile;
    }

    public void setStabile(String stabile) {
        this.stabile = stabile;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getPriorità() {
        return priorità;
    }

    public void setPriorità(String priorità) {
        this.priorità = priorità;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNomeAmministratore() {
        return nomeAmministratore;
    }

    public void setNomeAmministratore(String nomeAmministratore) {
        this.nomeAmministratore = nomeAmministratore;
    }

    public String getNomeStabile() {
        return nomeStabile;
    }

    public void setNomeStabile(String nomeStabile) {
        this.nomeStabile = nomeStabile;
    }

    public String getIndirizzoStabile() {
        return indirizzoStabile;
    }

    public void setIndirizzoStabile(String indirizzoStabile) {
        this.indirizzoStabile = indirizzoStabile;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }


}
