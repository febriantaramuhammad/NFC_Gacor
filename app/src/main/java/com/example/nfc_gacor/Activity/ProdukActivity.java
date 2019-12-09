package com.example.nfc_gacor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.nfc_gacor.APIService.APIClient;
import com.example.nfc_gacor.APIService.APIInterfacesRest;
import com.example.nfc_gacor.R;
import com.example.nfc_gacor.adapter.ProdukAdapter;

import com.example.nfc_gacor.model.produk.ModelProduk;
import com.example.nfc_gacor.model.produk.Produk;
import com.raizlabs.android.dbflow.sql.language.SQLite;


import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukActivity extends AppCompatActivity {
RecyclerView rcv;
ProdukAdapter itemList2;
    ModelProduk modelproduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        rcv = findViewById(R.id.rcv);
        getProduk();
    }

    private void getProduk() {
        final APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        final Call<ModelProduk> produk = apiInterface.getProduk("5965E901D62F1F7913717CCF9347443B");

        produk.enqueue(new Callback<ModelProduk>() {
            @Override
            public void onResponse(Call <ModelProduk> call, Response<ModelProduk> response) {
               modelproduk =  response.body();

                if (response.body() != null) {
                    for(int i = 0; i<modelproduk.getData().getProduk().size(); i++) {
                        modelproduk.getData().getProduk().get(i).save();
                    }

                    List<Produk> model = SQLite.select()
                            .from(Produk.class)
                            .queryList();

                     itemList2 = new ProdukAdapter(model);
                    rcv.setLayoutManager(new LinearLayoutManager(ProdukActivity.this));
                    rcv.setItemAnimator(new DefaultItemAnimator());
                    rcv.setAdapter(itemList2);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(ProdukActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ProdukActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call <ModelProduk> call, Throwable t) {

                List<Produk> model = SQLite.select()
                        .from(Produk.class)
                        .queryList();

                itemList2 = new ProdukAdapter(model);
                rcv.setLayoutManager(new LinearLayoutManager(ProdukActivity.this));
                rcv.setAdapter(itemList2);

                Toast.makeText(ProdukActivity.this, "Terjadi gangguan koneksi", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
