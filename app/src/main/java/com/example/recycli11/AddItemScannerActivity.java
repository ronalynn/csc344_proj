package com.example.recycli11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * @author Ronalyn nanong
 * The barcode scanner activity to for the add an item activity.
 */
public class AddItemScannerActivity extends AppCompatActivity {

    String failPrompt = "Failed to find barcode";

    /*implements the ZXing scanner library*/
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_additem);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a Barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /*When a barcode is identified it will be displayed in the activity_additem.xml*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EditText showBarcode = findViewById(R.id.add_barcode);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(scanResult != null) {
            if (scanResult.getContents() != null){
                showBarcode.setText(scanResult.getContents());
            } else {
                Toast.makeText(AddItemScannerActivity.this, failPrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddItemScannerActivity.this, AddItemActivity.class));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
