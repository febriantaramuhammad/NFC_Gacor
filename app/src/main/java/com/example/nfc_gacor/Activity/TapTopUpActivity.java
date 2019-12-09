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
import com.example.nfc_gacor.model.topup.Topup;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TapTopUpActivity extends AppCompatActivity {

    public static final String TAG = TapTopUpActivity.class.getSimpleName();
    private TextView mTvMessageUser, mTvMessageSaldo;
    private NfcAdapter mNfcAdapter;
    private ProgressBar mProgress;
    Topup topup;
    private String nama = "";
    private String nominal = "";
    private Context context;
    private int saldo_topup = 0;

Button btnprint;
    public static final String TAP_TOPUP = "tap_topup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_top_up);
        btnprint= findViewById(R.id.btnpost);
        btnprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAudit();
            }
        });
         topup = getIntent().getParcelableExtra(TAP_TOPUP);

        initNFC();
        mTvMessageUser = findViewById(R.id.tv_message_user);
        mTvMessageSaldo = findViewById(R.id.tv_message_saldo);

        if(topup!=null){
            TextView txtnominal = findViewById(R.id.txtnominal);
            txtnominal.setText("Top Up : "+ topup.getNominal());


        }
    }

    public void onNfcDetected (Ndef ndef,  String messageToWrite){

        readFromNFC(ndef);
        mProgress.setVisibility(View.VISIBLE);

      //  writeToNfc(ndef,mTvMessageUser +"#"+messageToWrite );
    }

    APIInterfacesRest apiInterface;


    private void sendAudit() {
        //progressDialog.show();


        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);

        Call<ModelPost> postAdd = apiInterface.sendAudit(

                (AppUtil.replaceNull("No. User : "+nama)),
                (AppUtil.replaceNull("Pembayaran Top Up " + topup.getNominal())),
                (AppUtil.replaceNull("Rp "+ String.valueOf(saldo_topup))),
                (AppUtil.replaceNull("Debet"))




        );

        postAdd.enqueue(new Callback<ModelPost>() {
            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {
                //progressDialog.dismiss();
                ModelPost responServer = response.body();
                if (responServer != null) {
                    Toast.makeText(TapTopUpActivity.this,"Payment has been successful", Toast.LENGTH_LONG).show();
                }
                Intent a = new Intent(TapTopUpActivity.this, MainActivity.class);
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

                Toast.makeText(TapTopUpActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
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


    public void readFromNFC (Ndef ndef){

        try {
            //connect ndef
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            //mendapatkan  message records array ke 0
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            //pemisah menggunakan split
            String[] lsdata = message.split("#");
            Log.d(TAG, "readFromNFC: " + message);
            //jika datanya tidak null
            if (lsdata!=null){
                //jika array datanya lebih dari satu
                if(lsdata.length>1){
                    nama = lsdata[0];
                    nominal = lsdata[1].trim();
                    mTvMessageUser.setText("ID User : " + lsdata[0]);
                    mTvMessageSaldo.setText("Saldo Anda : Rp " + lsdata[1]);

                }else {
                    Toast.makeText(TapTopUpActivity.this,"NFC No Detected",Toast.LENGTH_SHORT).show();
                }
            }

            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }


    private void writeToNfc(Ndef ndef, String message){

        mTvMessageSaldo.setText(topup.getNominal());
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
//        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if (tag != null) {
            Toast.makeText(this, "NFC Detected", Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            readFromNFC(ndef);

            int saldo = 0;
            try {
                saldo = Integer.parseInt(nominal.replace(".", ""));
            } catch (Exception e) {
            }


            try {
                saldo_topup = Integer.parseInt(topup.getNominal().replace(".", ""));
            } catch (Exception e) {
            }

            int saldo_akhir = 0;
            try {
                saldo_akhir = saldo_topup + saldo;
            } catch (Exception e) {
            }

            writeToNfc(ndef, nama + "#" + (0 + saldo_akhir));
            readFromNFC(ndef);

        }
    }



        private void initNFC(){

            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }

    }
