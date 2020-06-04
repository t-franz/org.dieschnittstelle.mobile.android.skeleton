package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;

import model.DataItem;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";

    private DataItem item;
    private ActivityDetailviewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);

        this.item = (DataItem)getIntent().getSerializableExtra(ARG_ITEM);
        if (this.item == null) {
            this.item = new DataItem();
        }

        binding.setController(this);

    }


    public void onSaveItem(View view) {
        Intent returnData = new Intent();

        returnData.putExtra(ARG_ITEM,this.item);

        this.setResult(Activity.RESULT_OK, returnData);
        finish();
    }

    public DataItem getItem() {
        return item;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addContact:selectAndAddContactToItem();return true;
            case R.id.doSomethingElse:
                Toast.makeText(this,
                        "Something else",
                        Toast.LENGTH_SHORT).show();return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectAndAddContactToItem() {
        Toast.makeText(this,
                "Will select contact ...",
                Toast.LENGTH_SHORT).show();
    }
}
