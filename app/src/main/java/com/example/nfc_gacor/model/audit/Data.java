
package com.example.nfc_gacor.model.audit;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("audit")
    @Expose
    private List<Audit> audit = null;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = -470469786317310386L;

    protected Data(Parcel in) {
        in.readList(this.audit, (Audit.class.getClassLoader()));
    }

    public Data() {
    }

    public List<Audit> getAudit() {
        return audit;
    }

    public void setAudit(List<Audit> audit) {
        this.audit = audit;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(audit);
    }

    public int describeContents() {
        return  0;
    }

}
