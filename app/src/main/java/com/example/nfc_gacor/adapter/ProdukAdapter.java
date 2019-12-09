package com.example.nfc_gacor.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.nfc_gacor.Activity.TapProdukActivity;
import com.example.nfc_gacor.model.produk.Produk;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Produk> dataItemList;
    //utk membedakan xml

    public ProdukAdapter(List<Produk> dataItemList) {
        this.dataItemList = dataItemList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung)holder).txtnama.setText(dataItemList.get(position).getNamaProduk());
        ((Penampung)holder).txtdeskripsi.setText(dataItemList.get(position).getDeskripsiProduk());
        ((Penampung)holder).txtharga.setText((String.valueOf(dataItemList.get(position).getHarga())));
        ImageView imagerole = ((Penampung) holder).imgproduk;
        Picasso.get().load(dataItemList.get(position).getFoto()).into(imagerole);
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtnama, txtdeskripsi, txtharga;
        public ImageView imgproduk;
        private Context context;
        Produk produk;


        public Penampung(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
            txtnama =  itemView.findViewById(R.id.txtnama);
            txtdeskripsi =  itemView.findViewById(R.id.txtdeskripsi);
            txtharga = itemView.findViewById(R.id.txtharga);
            imgproduk = itemView.findViewById(R.id.imageproduk);



            imgproduk.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v) {
                    if (v.getId() == R.id.imageproduk) {
                        produk = createNewProduk();
                        Intent detailIntent = new Intent(context, TapProdukActivity.class);
                        detailIntent.putExtra(TapProdukActivity.TAP_PRODUK, (Parcelable) produk);
                        context.startActivity(detailIntent);
                    }
                }


            });


            }

        private Produk createNewProduk() {
            Produk produk = new Produk();

            produk.setNamaProduk(txtnama.getText().toString());
            produk.setDeskripsiProduk(txtdeskripsi.getText().toString());
            produk.setHarga(txtharga.getText().toString());


            return produk;
        }




        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " ");
        }
        }




    }


