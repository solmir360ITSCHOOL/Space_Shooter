package com.example.space_shooter.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;

import com.example.space_shooter.Content;
import com.example.space_shooter.R;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {
    io.github.controlwear.virtual.joystick.android.JoystickView joystick;
    SeekBar shootMode;

    // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        joystick = findViewById(R.id.joystick);
        shootMode = findViewById(R.id.shootMode);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if (strength != 0 && angle != 0) {
                    Content.player.angle = angle;
                }
                float speed = Content.player.normalSpeed*(Float.valueOf(strength)/100);
                Content.player.vx = (float) (speed * Math.cos(Math.toRadians((angle - 90 + 360) % 360)));
                Content.player.vy = (float) (speed * Math.sin(Math.toRadians((angle - 90 + 360) % 360)));
            }
        });
        shootMode.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Content.player.shootMode = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}