package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM_NAME = "itemName";

    private EditText itemNameEditText;
    private FloatingActionButton saveItemActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);

        //Daten auslesen
        itemNameEditText = findViewById(R.id.itemName);
        saveItemActionButton = findViewById(R.id.fab);

        // Daten setzen
        String itemName = getIntent().getStringExtra(ARG_ITEM_NAME);
        if (itemName != null) {
            itemNameEditText.setText(itemName);
        }

        // Daten für Interaktion vorbereiten:
        // Alles dasselbe:
        // saveItemActionButton.setOnClickListener(this::onSaveItem);
        // saveItemActionButton.setOnClickListener(view -> this.onSaveItem());
        saveItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveItem(view);
            }
        });
    }

    private void onSaveItem(View view) {
        Intent returnData = new Intent();
        String itemNameFromInputText = itemNameEditText.getText().toString();
        returnData.putExtra(ARG_ITEM_NAME,itemNameFromInputText);

        this.setResult(Activity.RESULT_OK, returnData);
        finish();
    }

}
