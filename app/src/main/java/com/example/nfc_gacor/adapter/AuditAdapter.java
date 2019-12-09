package com.example.nfc_gacor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_gacor.R;
import com.example.nfc_gacor.model.audit.Audit;
import com.example.nfc_gacor.nfc.NFCWriteFragment;


import java.util.List;

import static com.example.nfc_gacor.R.layout.item_audit;


public class AuditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private NFCWriteFragment mNfcWriteFragment;
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;


    private List<Audit> dataItemList;
    //utk membedakan xml

    public AuditAdapter(List<Audit> dataItemList) {
        this.dataItemList = dataItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_audit, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung) holder).txtnama.setText(dataItemList.get(position).getNamaUser());
        ((Penampung) holder).txtket.setText(dataItemList.get(position).getKeterangan());
        ((Penampung) holder).txtnominal.setText(dataItemList.get(position).getNominal());
        ((Penampung) holder).txtdc.setText(dataItemList.get(position).getDebetCredit());

    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }


    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtnama, txtket, txtnominal, txtdc;
       // public ImageView imgtopup;
        private Context context;


        @SuppressLint("ResourceType")
        public Penampung(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();

            txtnama = itemView.findViewById(R.id.txtnama);
            txtnominal = itemView.findViewById(R.id.txtnominal);
            txtket = itemView.findViewById(R.id.txtket);
            txtdc = itemView.findViewById(R.id.txtdc);
            createNewAudit();
        }
        private Audit createNewAudit() {
            Audit audit = new Audit();
            audit.setNamaUser(txtnama.getText().toString());
            audit.setKeterangan(txtket.getText().toString());
            audit.setNominal(txtnominal.getText().toString());
            audit.setDebetCredit(txtdc.getText().toString());

            return audit;
        }


        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " ");
        }
    }

}


