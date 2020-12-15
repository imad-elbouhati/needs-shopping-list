package com.example.needs2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.needs2.Adapter.RecyclerViewAdapter;
import com.example.needs2.data.DataBaseHandler;
import com.example.needs2.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DataBaseHandler dataBaseHandler;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
  //  private DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        recyclerView = findViewById(R.id.recyclerView);

        dataBaseHandler = new DataBaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        itemList = dataBaseHandler.getAllItems();

        for(Item item : itemList){
            Log.d("TAG", "onCreate: "+item.getItemName());
        }

        recyclerViewAdapter =  new RecyclerViewAdapter(this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();

            }
        });
    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        saveButton = view.findViewById(R.id.saveBotton);
        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColor = view.findViewById(R.id.colorItem);
        itemSize = view.findViewById(R.id.sizeItem);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("save", "onClick: ");
                if(!babyItem.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()){

                    saveItem(v);
                }else{
                    Snackbar.make(v,"Empty Fields not Allowed",Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
    private void saveItem(View view) {
        Item item = new Item();
        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int newSize = Integer.parseInt(itemSize.getText().toString().trim());
        int newQuantity = Integer.parseInt(itemSize.getText().toString().trim());


        item.setItemName(newItem);
        item.setItemSize(newSize);
        item.setItemQuantity(newQuantity);
        item.setItemColor(newColor);


        dataBaseHandler.AddItem(item);
        Snackbar.make(view,"Item Saved",Snackbar.LENGTH_SHORT)
                .show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(ItemListActivity.this,ItemListActivity.class));
                finish();
            }
        },1200);

    }
}