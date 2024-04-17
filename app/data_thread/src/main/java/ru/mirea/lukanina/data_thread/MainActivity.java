package ru.mirea.lukanina.data_thread;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.TimeUnit;

import ru.mirea.lukanina.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Изначальное описание в TextView
        binding.textViewResult.setText("Запуск трех задач с различными задержками: runn1 (2 сек), runn2 (3 сек), runn3 (5 сек).");

        final Handler handler = new Handler();

        final Runnable runn1 = new Runnable() {
            @Override
            public void run() {
                binding.textViewResult.setText("runn1 запущен: первая задача с задержкой в 2 секунды.");
            }
        };

        final Runnable runn2 = new Runnable() {
            @Override
            public void run() {
                binding.textViewResult.setText("runn2 запущен: вторая задача с последующей задержкой в 1 секунду после runn1.");
            }
        };

        final Runnable runn3 = new Runnable() {
            @Override
            public void run() {
                binding.textViewResult.setText("runn3 запущен: третья задача с задержкой в 2 секунды после runn2.");
            }
        };

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2); // Задержка на 2 секунды для runn1
                    handler.post(runn1);
                    TimeUnit.SECONDS.sleep(1); // Задержка на 1 секунду для runn2
                    handler.post(runn2);
                    TimeUnit.SECONDS.sleep(2); // Задержка на 2 секунды для runn3
                    handler.post(runn3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
