package com.example.recycli11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * @author Ronalyn nanong
 * Displays the recycling information from the database
 */
public class RecycleActivity extends AppCompatActivity {

    TextView itemBarcodeTxt, itemNameTxt, itemBrandTxt, itemWeightTxt, itemLidTxt, itemContainerTxt, itemLabelTxt;
    DatabaseReference ref;
    ImageButton mapBtn, homeBtn;
    String mapPrompt = "Map selected";
    String homePrompt = "home";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        homeBtn = findViewById(R.id.home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RecycleActivity.this, homePrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(RecycleActivity.this, MainActivity.class));
            }
        });

        mapBtn = findViewById(R.id.map_btn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RecycleActivity.this, mapPrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(RecycleActivity.this, MapsActivity.class));
            }
        });

        itemBarcodeTxt = findViewById(R.id.item_barcode);
        itemNameTxt = findViewById(R.id.item_name);
        itemBrandTxt = findViewById(R.id.item_brand);
        itemWeightTxt = findViewById(R.id.item_weight);
        itemLidTxt = findViewById(R.id.item_lid_result);
        itemContainerTxt = findViewById(R.id.item_container_result);
        itemLabelTxt = findViewById(R.id.item_label_result);
        ref = FirebaseDatabase.getInstance().getReference().child("item");

        String ItemKey = getIntent().getStringExtra("ItemKey");

        /*when an item from the recycler view is selected in the home page, the data of the item in
        the database is shown in the activity_recycle.xml
         */
        ref.child(ItemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ItemModel item = dataSnapshot.getValue(ItemModel.class);

                itemBarcodeTxt.setText(item.barcode);
                itemNameTxt.setText(item.name);
                itemBrandTxt.setText(item.brand + ", ");
                itemWeightTxt.setText(item.weight);
                itemLidTxt.setText(item.lid);
                itemContainerTxt.setText(item.container);
                itemLabelTxt.setText(item.label);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
