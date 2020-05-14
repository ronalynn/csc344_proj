package com.example.recycli11;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * @author Ronalyn Nanong
 * @version 1.0
 * The MainActivity class launches the home page of the application shown in activity_main.xml
 * Navigation to the map button, barcode scanner, search bar, and adding a product to the database
 * is created here.
 */

public class MainActivity extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView resultView;
    FirebaseRecyclerOptions options;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference dbRef;
    ImageButton cameraBtn, mapBtn;
    Button addDataBtn;
    String scannerPrompt = "Scanner Selected";
    String addDataPrompt = "Add Item";
    String mapPrompt = "Map Selected";

    /*sets the home page using activity_main.xml*/
    /*connects the java files to the xml files*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbRef = FirebaseDatabase.getInstance().getReference().child("item");

        inputSearch = findViewById(R.id.home_search_bar);
        resultView = findViewById(R.id.rview_results);
        resultView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        resultView.setHasFixedSize(true);

        cameraBtn = findViewById(R.id.home_camera_btn);
        cameraBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), scannerPrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, ScannerActivity.class));
            }
        });

        addDataBtn = findViewById(R.id.home_add_btn);
        addDataBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, addDataPrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, AddItemActivity.class));
            }
        });

        mapBtn = findViewById(R.id.map_btn);
        mapBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, mapPrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        loadData(" ");
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString() != null){
                    loadData(s.toString());
                } else {
                    loadData("");
                }

            }
        });
    }

    /*uses any input in the search bar to search the database to display in the recycler view*/
    private void loadData(String data) {
        Query qry = dbRef.orderByChild("name").startAt(data).endAt(data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<ItemModel>().setQuery(qry, ItemModel.class).build();
        adapter = new FirebaseRecyclerAdapter<ItemModel, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull ItemModel model) {
                holder.itemName.setText(model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1));
                holder.itemBrandWeight.setText(model.getBrand().substring(0, 1).toUpperCase() + model.getBrand().substring(1).trim()
                    + ", " + model.getWeight());
                /*when an item in the recycler view is selected, the information from the database
                is sent to the RecycleActivity
                 */
                holder.v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, RecycleActivity.class);
                        intent.putExtra("ItemKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            /*
            Creates items in the recycler view
             */
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
                return new ItemViewHolder(v);
            }
        };
        adapter.startListening();
        resultView.setAdapter(adapter);
    }
}
