package com.example.recycli11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * @author Ronalyn Nanong
 * The barcode scanner activity to search for a product.
 */
public class ScannerActivity extends AppCompatActivity {

    String failPrompt = "Failed to find barcode";

    /*implements the ZXing library scanning features*/
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_recycle);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a Barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /*when a barcode matches the database the information will be displayed in the
    activity_recycle.xml
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView showScanResult = findViewById(R.id.item_barcode);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String  match = "5060339017419"; //barcodes that return this string will show the activity_recycle.xml

        if(scanResult != null) {
            if (scanResult.getContents().equals(match)){
                showScanResult.setText(scanResult.getContents());
            } else {
                Toast.makeText(ScannerActivity.this, failPrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(ScannerActivity.this, MainActivity.class));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
