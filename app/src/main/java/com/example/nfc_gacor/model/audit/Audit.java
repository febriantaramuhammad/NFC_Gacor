
package com.example.nfc_gacor.model.audit;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.example.nfc_gacor.AppController;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppController.class)
public class Audit extends BaseModel implements Serializable, Parcelable
{
@PrimaryKey
@Column
    @SerializedName("id")
    @Expose
    private String id;
    @Column
    @SerializedName("nama_user")
    @Expose
    private String namaUser;
    @Column
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @Column
    @SerializedName("nominal")
    @Expose
    private String nominal;
    @Column
    @SerializedName("debet_credit")
    @Expose
    private String debetCredit;
    public final static Creator<Audit> CREATOR = new Creator<Audit>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Audit createFromParcel(Parcel in) {
            return new Audit(in);
        }

        public Audit[] newArray(int size) {
            return (new Audit[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5608291178699866246L;

    protected Audit(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.namaUser = ((String) in.readValue((String.class.getClassLoader())));
        this.keterangan = ((String) in.readValue((String.class.getClassLoader())));
        this.nominal = ((String) in.readValue((String.class.getClassLoader())));
        this.debetCredit = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Audit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getDebetCredit() {
        return debetCredit;
    }

    public void setDebetCredit(String debetCredit) {
        this.debetCredit = debetCredit;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(namaUser);
        dest.writeValue(keterangan);
        dest.writeValue(nominal);
        dest.writeValue(debetCredit);
    }

    public int describeContents() {
        return  0;
    }

}
