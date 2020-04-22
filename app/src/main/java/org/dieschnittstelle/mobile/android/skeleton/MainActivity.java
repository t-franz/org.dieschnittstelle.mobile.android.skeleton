package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dieschnittstelle.mobile.android.skeleton.R;

public class MainActivity extends AppCompatActivity {

    private ViewGroup listView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = this.findViewById(R.id.listView);
        this.fab = this.findViewById(R.id.fab);

        for (int i=0; i<this.listView.getChildCount();i++) {
            TextView currentChild = (TextView)this.listView.getChildAt(i);
            currentChild.setOnClickListener((view) -> {
                //Toast.makeText(this, "Click on: " + currentChild.getText() +"!", Toast.LENGTH_SHORT).show();
                this.showFeedbackMessage("Click on: " + currentChild.getText() +"!");
            });
        }

        this.fab.setOnClickListener((view) -> {
            this.showFeedbackMessage("Add new Element!");
        });

/*        TextView welcomeMessage = this.findViewById(R.id.welcomeMessage);

        welcomeMessage.setOnClickListener(view -> {
            Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
        });*/
    }

    private void showFeedbackMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }
}
