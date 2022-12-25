package com.example.qrcodescanner_21c2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.telecom.Call;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.AndroidException;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.invoke.ConstantCallSite;
import java.util.Locale;


//implementasi dari onclicklistener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private TextView textViewNama, textViewKelas, textViewNim;
    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewNama = (TextView) findViewById(R.id.textViewNama);
        textViewKelas = (TextView) findViewById(R.id.textViewKelas);
        textViewNim = (TextView) findViewById(R.id.textViewNIM);

//intialisasi scan object
        qrScan = new IntentIntegrator(this);

//implementasi onclick listener
        buttonScan.setOnClickListener(this);
    }

//untuk mendapatkan hasil scanning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                resultCode, data);
        if (result != null) {

//jika qrcode tidak ada sama sekali
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil SCAN tidak ada", Toast.LENGTH_LONG).show();

//jika qr ada/ditemukan data nya

            //1.scanweb
            } if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent OpenBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(OpenBrowser);
            }

            //2.Call number
            String number;
            number = new String(result.getContents());

            if (number.matches("^[0-9]*$") && number.length() > 11) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + number));
                startActivity(call);
            } else {
                try {
            //3.konversi datanya ke jsom
                    JSONObject obj = new JSONObject(result.getContents());
                //di set nilai datanya ke textviews
                    textViewNama.setText(obj.getString("nama"));
                    textViewKelas.setText(obj.getString("kelas"));
                    textViewNim.setText(obj.getString("nim"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick (View view){
        qrScan.initiateScan();
    }
}
