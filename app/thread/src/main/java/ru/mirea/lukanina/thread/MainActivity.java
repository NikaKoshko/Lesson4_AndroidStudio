package ru.mirea.lukanina.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import ru.mirea.lukanina.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // Объявление переменной для привязки макета
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получение информации о главном потоке и изменение его имени
        Thread mainThread = Thread.currentThread();
        binding.resultTextView.setText("Имя текущего потока: " + mainThread.getName());

        // Меняем имя и выводим в текстовом поле
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-09-21, НОМЕР ПО СПИСКУ: 14, МОЙ ЛЮБИМЫЙ ФИЛЬМ: ...");
        binding.resultTextView.append("\n Новое имя потока: " + mainThread.getName());

        // Вывод стека вызовов в Log
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        // Вывод информации о группе потока в Log
        Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на кнопку "Calculate"
                calculateAveragePairs();
            }
        });

        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток № %d студентом группы № %s номер по списку № %d ",
                                numberThread, "БСБО-09-21", 14));
                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d("ThreadProject", "Выполнен поток № " + numberThread);
                        }
                    }
                }).start();
            }
        });
    }

    private void calculateAveragePairs() {
        // Ваша логика подсчета среднего количества пар в месяц
        // Можете запустить фоновый поток здесь и обновить TextView с результатом
        // Например:
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Выполните вычисления здесь
                final double averagePairs = calculateAveragePairsInBackground();

                // Обновите UI с результатом в главном потоке
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Обновление TextView с результатом
                        binding.resultTextView.setText("Average pairs per day: " + averagePairs);
                    }
                });
            }
        }).start();
    }

    private double calculateAveragePairsInBackground() {
        // Здесь вычисляется среднее количество пар в месяц в фоновом потоке
        // Верните здесь реальное среднее количество пар
        return 4.5; // Пример
    }
}
