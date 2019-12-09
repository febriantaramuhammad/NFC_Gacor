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
import com.example.nfc_gacor.adapter.AuditAdapter;
import com.example.nfc_gacor.model.audit.Audit;
import com.example.nfc_gacor.model.audit.ModelAudit;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuditActivity extends AppCompatActivity {
    AuditAdapter itemList2;
    ModelAudit modelaudit;
  RecyclerView rcv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        rcv= findViewById(R.id.rcv);
        getAudit();
    }

    private void getAudit() {
        final APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        final Call<ModelAudit> audit = apiInterface.getAudit("5965E901D62F1F7913717CCF9347443B");

        audit.enqueue(new Callback<ModelAudit>() {
            @Override
            public void onResponse(Call <ModelAudit> call, Response<ModelAudit> response) {
                modelaudit =  response.body();

                if (response.body() != null) {
                  /*  for(int i = 0; i<modelaudit.getData().getAudit().size(); i++) {
                        modelaudit.getData().getAudit().get(i).save();
                    }

                    List<Audit> model = SQLite.select()
                            .from(Audit.class)
                            .queryList();
*/
                    itemList2 = new AuditAdapter(modelaudit.getData().getAudit());
                    rcv.setLayoutManager(new LinearLayoutManager(AuditActivity.this));
                    rcv.setItemAnimator(new DefaultItemAnimator());
                    rcv.setAdapter(itemList2);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(AuditActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call <ModelAudit> call, Throwable t) {

              /*  List<Audit> model = SQLite.select()
                        .from(Audit.class)
                        .queryList();

                itemList2 = new AuditAdapter(model);
                rcv.setLayoutManager(new LinearLayoutManager(AuditActivity.this));
                rcv.setAdapter(itemList2); */

                Toast.makeText(AuditActivity.this, "Terjadi gangguan koneksi", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
