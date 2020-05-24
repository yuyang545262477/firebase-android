package com.android.davidyu.firebase_android;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.android.davidyu.firebase_android.dto.ShortCut;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MainBaseActivity {

    @BindView(R.id.editShortCutName)
    EditText editShortCutName;

    @BindView(R.id.actKeys)
    AutoCompleteTextView actKeys;

    @BindView(R.id.lblAllKeys)
    TextView lblAllKeys;

    ArrayList<String> allKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child("root").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ShortCut shortCut = child.getValue(ShortCut.class);
                    if (shortCut != null) {
                        Log.d("onDataChange", "onDataChange: " + shortCut.toString());
                        Toast.makeText(getApplicationContext(), "shortcut:" + shortCut.getName() + "Keys:" + shortCut.getKeys(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @OnClick(R.id.btnAddKey)
    public void onBtnAddKeyClicked() {
        String key = this.actKeys.getText().toString();
        allKeys.add(key);
        String currentKeys = lblAllKeys.getText().toString();
        lblAllKeys.setText(String.format("%s%s ", currentKeys, key));
        actKeys.setText("");
    }

    @OnClick(R.id.btnSave)
    public void saveShortCut() {
        ShortCut shortCut = new ShortCut();
        shortCut.setName(editShortCutName.getText().toString());
        shortCut.setKeys(allKeys);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child("root").push().setValue(shortCut);

        allKeys = new ArrayList<>();
        lblAllKeys.setText("");

    }

}
