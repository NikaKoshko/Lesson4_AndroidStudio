package ru.mirea.lukanina.serviceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Проверяем разрешение и запрашиваем его при необходимости
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Не делаем ничего здесь
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_CODE);
        }

        // Настройка обработчиков нажатия на кнопки
        findViewById(R.id.buttonPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMusicService();
            }
        });

        findViewById(R.id.buttonStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusicService();
            }
        });
    }

    private void startMusicService() {
        Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
        // Добавляем название трека в Intent
        serviceIntent.putExtra("trackName", "Borislav Slavov - Down by the River");
        startForegroundService(serviceIntent);
    }

    private void stopMusicService() {
        stopService(new Intent(MainActivity.this, PlayerService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, ничего не делаем здесь
            } else {
                Log.d(MainActivity.class.getSimpleName(), "Пользователь отказал в разрешениях");
            }
        }
    }
}
