package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import model.DataItem;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";

    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;
    private FloatingActionButton saveItemActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);

        //Daten auslesen
        itemNameEditText = findViewById(R.id.itemName);
        itemDescriptionEditText = findViewById(R.id.itemDescription);
        saveItemActionButton = findViewById(R.id.fab);

        // Daten setzen
        DataItem item = (DataItem)getIntent().getSerializableExtra(ARG_ITEM);
        if (item != null) {
            itemNameEditText.setText(item.getName());
            itemDescriptionEditText.setText(item.getDescription());
        }

        // Daten für Interaktion vorbereiten:
//        saveItemActionButton.setOnClickListener(this::onSaveItem);
        saveItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onSaveItem(view); }
        });
    }


    private void onSaveItem(View view) {
        Intent returnData = new Intent();
        String itemNameFromInputText = itemNameEditText.getText().toString();
        String itemDescriptionFromInputText = itemDescriptionEditText.getText().toString();

        DataItem item = new DataItem();
        item.setName(itemNameFromInputText);
        item.setDescription(itemDescriptionFromInputText);

        returnData.putExtra(ARG_ITEM,item);

        this.setResult(Activity.RESULT_OK, returnData);
        finish();
    }

}
