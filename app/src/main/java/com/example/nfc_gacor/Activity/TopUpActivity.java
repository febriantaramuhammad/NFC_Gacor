package com.example.nfc_gacor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nfc_gacor.APIService.APIClient;
import com.example.nfc_gacor.APIService.APIInterfacesRest;
import com.example.nfc_gacor.Interface.Listener;
import com.example.nfc_gacor.R;
import com.example.nfc_gacor.adapter.TopUpAdapter;
import com.example.nfc_gacor.model.topup.ModelTopup;
import com.example.nfc_gacor.model.topup.Topup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUpActivity extends AppCompatActivity implements Listener {
    RecyclerView rcv;
    TopUpAdapter itemList2;
    ModelTopup modeltopup;
    private Button btRead;

    private HasilTopUpActivity mNfcReadFragment = (HasilTopUpActivity) getFragmentManager().findFragmentByTag(HasilTopUpActivity.TAG);//(HasilTopUpActivity) getFragmentManager().findFragmentByTag(HasilTopUpActivity.TAG);

    private boolean isDialogDisplayed = false;
    private boolean isRead = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;
    private HasilTopUpActivity mNfcWriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        initViews();
        initNFC();


        getTopup();
    }
//ngeread fragment
    private void initViews() {

        rcv = findViewById(R.id.rcv);
        btRead = findViewById(R.id.btn_read);

        btRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReadFragment();
            }
        });
    }
//method nfc adapter
    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }
//method untuk nge show fragment
    private void showReadFragment() {

       mNfcReadFragment = (HasilTopUpActivity) getFragmentManager().findFragmentByTag(HasilTopUpActivity.TAG);
//jika read fragment null
        if (mNfcReadFragment == null) {
//new instance dari hasil top up activity
           mNfcReadFragment = HasilTopUpActivity.newInstance();
        }
        //show get fragment dari hasil top up activity
        mNfcReadFragment.show(getFragmentManager(),HasilTopUpActivity.TAG);

    }
//method untuk write nfc
    private void showWriteFragment() {

        isWrite = true;

        mNfcWriteFragment = (HasilTopUpActivity) getFragmentManager().findFragmentByTag(HasilTopUpActivity.TAG);

        if (mNfcWriteFragment == null) {

            mNfcWriteFragment = HasilTopUpActivity.newInstance();
        }
        mNfcWriteFragment.show(getFragmentManager(),HasilTopUpActivity.TAG);

    }

    @Override
    public void onDialogDisplayed() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Intent
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

//        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if (tag != null) {
            Toast.makeText(this, "NFC Tag Detected", Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                if (isRead) {

                    mNfcReadFragment = (HasilTopUpActivity) getFragmentManager().findFragmentByTag(HasilTopUpActivity.TAG);//mNfcReadFragment = (HasilTopUpActivity) getFragmentManager().findFragmentByTag(HasilTopUpActivity.TAG);
                    mNfcReadFragment.onNfcDetected(ndef);

                } else {

                }
            }
        }
    }




    private void getTopup() {
        final APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        final Call<ModelTopup> topup = apiInterface.getTopup("5965E901D62F1F7913717CCF9347443B");

        topup.enqueue(new Callback<ModelTopup>() {
            @Override
            public void onResponse(Call <ModelTopup> call, Response<ModelTopup> response) {
                modeltopup =  response.body();

                if (response.body() != null) {

                        for(int i = 0; i<modeltopup.getData().getTopup().size(); i++) {
                            modeltopup.getData().getTopup().get(i).save();
                        }


                   List<Topup> model = SQLite.select()
                            .from(Topup.class)
                            .queryList();

                    itemList2 = new TopUpAdapter(model);
                    rcv.setLayoutManager(new LinearLayoutManager(TopUpActivity.this));
                    rcv.setItemAnimator(new DefaultItemAnimator());
                    rcv.setAdapter(itemList2);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TopUpActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TopUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call <ModelTopup> call, Throwable t) {

               List<Topup> model = SQLite.select()
                        .from(Topup.class)
                        .queryList();

                itemList2 = new TopUpAdapter(model);
                rcv.setLayoutManager(new LinearLayoutManager(TopUpActivity.this));
                rcv.setAdapter(itemList2);

                Toast.makeText(TopUpActivity.this, "Terjadi gangguan koneksi", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }


}
