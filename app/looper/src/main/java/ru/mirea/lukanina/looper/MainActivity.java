package ru.mirea.lukanina.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText ageEditText;
    private EditText occupationEditText;
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ageEditText = findViewById(R.id.editTextAge);
        occupationEditText = findViewById(R.id.editTextOccupation);
        calculateButton = findViewById(R.id.buttonCalculate);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age = Integer.parseInt(ageEditText.getText().toString());
                String occupation = occupationEditText.getText().toString();

                // Создаем новый поток и передаем ему возраст и профессию
                MyLooper myLooper = new MyLooper(age, occupation);
                myLooper.start();
            }
        });
    }

    public class MyLooper extends Thread {

        private int age;
        private String occupation;

        public MyLooper(int age, String occupation) {
            this.age = age;
            this.occupation = occupation;
        }

        @Override
        public void run() {
            Log.d("MyLooper", "run");

            // Подсчет времени задержки (в миллисекундах) в зависимости от возраста
            long delayTime = age * 1000;

            // Создаем новый Handler для основного потока
            Handler mainHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Bundle bundle = msg.getData();
                    String result = bundle.getString("result");
                    Log.d("MyLooper", result);
                }
            };

            // Отправляем сообщение с результатом в основной поток через Handler
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("result", "Your age: " + age + ", Your occupation: " + occupation);
            message.setData(bundle);
            mainHandler.sendMessage(message);

            // Задержка выполнения потока
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Отправляем сообщение с результатом в основной поток через Handler после задержки
            message = new Message();
            bundle = new Bundle();
            bundle.putString("result", "Time delay complete. Your age: " + age + ", Your occupation: " + occupation);
            message.setData(bundle);
            mainHandler.sendMessage(message);
        }
    }
}
