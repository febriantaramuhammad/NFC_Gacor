package com.example.nfc_gacor.Activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfc_gacor.APIService.APIClient;
import com.example.nfc_gacor.APIService.APIInterfacesRest;
import com.example.nfc_gacor.APIService.AppUtil;
import com.example.nfc_gacor.R;
import com.example.nfc_gacor.model.audit.ModelPost;
import com.example.nfc_gacor.model.produk.Produk;
import com.example.nfc_gacor.model.topup.Topup;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TapProdukActivity extends AppCompatActivity {
    public static final String TAG = TapProdukActivity.class.getSimpleName();
    private TextView mTvMessageUser, mTvMessageSaldo;
    private NfcAdapter mNfcAdapter;
    private ProgressBar mProgress;
    Produk produk;
    Topup topup;
    Button btnprint;
    private Context context;
    private String nama = "";
    private String nominal = "";
    private int hargaproduk = 0;




    public static final String TAP_PRODUK = "tap_produk";
    public static final String TAP_TOPUP = "tap_topup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_produk);
        produk = getIntent().getParcelableExtra(TAP_PRODUK);


        btnprint = findViewById(R.id.btnpost2);
        btnprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAudit();
            }
        });

        initNFC();
        mTvMessageUser = findViewById(R.id.tv_message_user);
        mTvMessageSaldo = findViewById(R.id.tv_message_saldo);

        if (produk != null) {
            TextView textNama = findViewById(R.id.txtnama);
            textNama.setText(produk.getNamaProduk());

//            TextView textdeskripsi = findViewById(R.id.txtdeskripsi);
//            textdeskripsi.setText(produk.getDeskripsiProduk());

            TextView textharga = findViewById(R.id.txtharga);
            textharga.setText(produk.getHarga());


        }
    }


        APIInterfacesRest apiInterface;


        private void sendAudit() {
            //progressDialog.show();


            apiInterface = APIClient.getClient().create(APIInterfacesRest.class);

            Call<ModelPost> postAdd = apiInterface.sendAudit(

                    (AppUtil.replaceNull("No. User : "+nama)),
                    (AppUtil.replaceNull("Pembayaran " + produk.getNamaProduk())),
                    (AppUtil.replaceNull("Rp "+ String.valueOf(hargaproduk))),
                    (AppUtil.replaceNull("Credit"))



            );

            postAdd.enqueue(new Callback<ModelPost>() {
                @Override
                public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {
                    //progressDialog.dismiss();
                    ModelPost responServer = response.body();
                    if (responServer != null) {
                            Toast.makeText(TapProdukActivity.this,"Payment has been successful", Toast.LENGTH_LONG).show();
                        }
                        Intent a = new Intent(TapProdukActivity.this, MainActivity.class);
                        startActivity(a);
                    }

                @Override
                public void onFailure(Call<ModelPost> call, Throwable t) {
                    //progressDialog.show();
                    //MASUKIN KE DATABASE

                    //Player player = new Player();
                    //Random rand = new Random();
                    // player.setId(String.valueOf(rand.nextInt(1000)+1000));
                    // player.setName(txtnama.getText().toString());
                    // player.setEmail(txtemail.getText().toString());
                    //player.setHandphone(txthp.getText().toString());
                    // player.setPassword(tempFoto);
                    //player.setPassword(txtpassword.getText().toString());
                    // player.setRepassword(txtrepassword.getText().toString());

                    // player.setStatus("pending");
                    // player.save();

                    Toast.makeText(TapProdukActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
                    call.cancel();

                }
            });

        }

        //change string to requestbody
        public RequestBody toRequestBody(String value) {
            if (value == null) {
                value = "";
            }
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
            return body;
        }



    public void onNfcDetected (Ndef ndef, String messageToWrite){

        readFromNFC(ndef);
        mProgress.setVisibility(View.VISIBLE);

        //  writeToNfc(ndef,mTvMessageUser +"#"+messageToWrite );
    }




    public String [] readFromNFC (Ndef ndef){
        String[] lsdata = new String[2];
        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            lsdata= message.split("#");
            Log.d(TAG, "readFromNFC: " + message);
            if (lsdata!=null){
                if(lsdata.length>1){
                    nama = lsdata[0];
                    nominal = lsdata[1].trim();
                    mTvMessageUser.setText("ID User : " + lsdata[0]);
                    mTvMessageSaldo.setText("Sisa Saldo Anda : Rp " + lsdata[1]);

                }else {
                    Toast.makeText(TapProdukActivity.this,"NFC No Detected",Toast.LENGTH_SHORT).show();
                }
            }

            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
        return lsdata;
    }

    private void writeToNfc(Ndef ndef, String message){

        if (ndef != null) {

            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                //Write Successful
//                mTvMessageSaldo.setText("Success");

            } catch (IOException | FormatException e) {
                e.printStackTrace();
//                mTvMessageSaldo.setText("Fail");

            } finally {
//                mProgress.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        topup = getIntent().getParcelableExtra(TAP_TOPUP);
//        int harga = Integer.parseInt(topup.getNominal());
//        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if (tag != null) {
            Toast.makeText(this, "NFC Detected", Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            String[] nfcData = readFromNFC(ndef);



            try {
                hargaproduk = Integer.parseInt(produk.getHarga().substring(3, produk.getHarga().length() - 1).replace(".", "").replace(",", ""));

            } catch (Exception e) {
            }


            int debit = Integer.parseInt(nfcData[1].replace(".", "")) - hargaproduk;


            writeToNfc(ndef, nama + "#" + debit);

            readFromNFC(ndef);

        }
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

}