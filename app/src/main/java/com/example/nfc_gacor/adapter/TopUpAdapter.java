package com.example.nfc_gacor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_gacor.R;
import com.example.nfc_gacor.Activity.TapTopUpActivity;
import com.example.nfc_gacor.model.topup.Topup;
import com.example.nfc_gacor.nfc.NFCWriteFragment;

import java.util.List;

import static com.example.nfc_gacor.R.layout.item_topup;

public class TopUpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private NFCWriteFragment mNfcWriteFragment;
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;


    private List<Topup> dataItemList;
    //utk membedakan xml

    public TopUpAdapter(List<Topup> dataItemList) {
        this.dataItemList = dataItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_topup, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung) holder).txtnominal.setText(dataItemList.get(position).getNominal());
        ((Penampung) holder).txtharga.setText("Harga   : " + dataItemList.get(position).getHarga());

    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }


    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtharga, txtnominal;
        public ImageView imgtopup;
        private Context context;
        Topup topup;

        @SuppressLint("ResourceType")
        public Penampung(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();

            txtharga = itemView.findViewById(R.id.txtharga);
            txtnominal = itemView.findViewById(R.id.txtnominal);
            imgtopup = itemView.findViewById(R.id.imageView3);
            imgtopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.imageView3) {
                        topup = createNewTopup();
                        Intent detailIntent = new Intent(context, TapTopUpActivity.class);
                        detailIntent.putExtra(TapTopUpActivity.TAP_TOPUP, (Parcelable) topup);
                        context.startActivity(detailIntent);
                    }
                }
            });

        }






        private Topup createNewTopup() {
            Topup topup = new Topup();
            topup.setNominal(txtnominal.getText().toString());

            return topup;
        }


        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " ");
        }
    }

}
