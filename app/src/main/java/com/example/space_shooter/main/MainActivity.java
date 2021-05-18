package com.example.space_shooter.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.space_shooter.Content;
import com.example.space_shooter.Game.GameActivity;
import com.example.space_shooter.R;
import com.example.space_shooter.Shop.ShopActivity;

public class MainActivity extends AppCompatActivity {
    Button enter, test;
    EditText playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enter = findViewById(R.id.enter);
        test = findViewById(R.id.shop);
        playerName = findViewById(R.id.playerName);

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
                finish();
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(i);
                finish();
            }
        });
        playerName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Content.player.name = playerName.getText().toString();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Вы уверены что хотите выйти?");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        quitDialog.show();
    }
}