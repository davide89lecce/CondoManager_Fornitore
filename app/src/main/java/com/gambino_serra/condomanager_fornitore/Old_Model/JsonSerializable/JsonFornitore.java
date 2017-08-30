package com.gambino_serra.condomanager_fornitore.Old_Model.JsonSerializable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by condomanager_fornitore on 13/02/17.
 */

public class JsonFornitore {

    @SerializedName("usernameF")
    public String username;
    @SerializedName("azienda")
    public String azienda;
    @SerializedName("indirizzo")
    public String indirizzo;
    @SerializedName("telefono")
    public String telefono;
    @SerializedName("email")
    public String email;
    @SerializedName("partitaIva")
    public String partitaIva;
    @SerializedName("responsabile")
    public String responsabile;
}
