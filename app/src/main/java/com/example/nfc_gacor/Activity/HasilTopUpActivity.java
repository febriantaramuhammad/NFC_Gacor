package com.example.nfc_gacor.Activity;

import androidx.annotation.Nullable;

import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nfc_gacor.Interface.Listener;
import com.example.nfc_gacor.R;

import java.io.IOException;
import java.nio.charset.Charset;

public class HasilTopUpActivity extends android.app.DialogFragment {

    public static final String TAP_TOPUP = "tap_topup";

    public static final String TAG = HasilTopUpActivity.class.getSimpleName();

    public static HasilTopUpActivity newInstance() {

        return new HasilTopUpActivity();
    }


        private TextView mTvMessage;
        private Listener mListener;
    private ProgressBar mProgress;

        @Nullable
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState){

            View view = inflater.inflate(R.layout.activity_hasil_top_up, container, false);
            initViews(view);
            return view;
        }

        private void initViews (View view){

            mTvMessage = view.findViewById(R.id.tv_message);
            mProgress = (ProgressBar) view.findViewById(R.id.progress);
        }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            mListener = (TopUpActivity) context;
            mListener.onDialogDisplayed();
        }

        @Override
        public void onDetach () {
            super.onDetach();
            mListener.onDialogDismissed();
        }

    public void onNfcDetected(Ndef ndef){

        mProgress.setVisibility(View.VISIBLE);
            readFromNFC(ndef);

        }

        private void readFromNFC (Ndef ndef){

            try {
                ndef.connect();
                NdefMessage ndefMessage = ndef.getNdefMessage();
                String message = new String(ndefMessage.getRecords()[0].getPayload());
                Log.d(TAG, "readFromNFC: " + message);
                mTvMessage.setText(message);
                ndef.close();

            } catch (IOException | FormatException e) {
                e.printStackTrace();

            }
        }

    private void writeToNfc(Ndef ndef, String message){


        mTvMessage.setText("");
        if (ndef != null) {

            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                //Write Successful
                mTvMessage.setText("Success");


            } catch (IOException | FormatException e) {
                e.printStackTrace();
                mTvMessage.setText("Fail");


            } finally {
                mProgress.setVisibility(View.GONE);
            }

        }
    }
}





