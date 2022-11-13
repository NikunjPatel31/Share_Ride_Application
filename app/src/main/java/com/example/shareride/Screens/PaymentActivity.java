package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shareride.R;

public class PaymentActivity extends AppCompatActivity {


    EditText etName, etUpi, etNode, etAmount;

    final int UPI_PAYMENT = 0;

    public void pay(View view) {

        String name = etName.getText().toString();
        String upiId = etUpi.getText().toString();
        String note = etNode.getText().toString();
        String amount = etAmount.getText().toString();

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaymentActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initialize();
    }

    private void  initialize() {
        etName = findViewById(R.id.name_edit_text);
        etUpi = findViewById(R.id.upi_edit_text);
        etNode = findViewById(R.id.note_edit_text);
        etAmount = findViewById(R.id.amount_edit_text);
    }
}