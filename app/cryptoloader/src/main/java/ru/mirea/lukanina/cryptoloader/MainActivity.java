package ru.mirea.lukanina.cryptoloader;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 123;
    private EditText editTextPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhrase = findViewById(R.id.editText_phrase);
    }

    public void onClickEncrypt(View view) {
        String phrase = editTextPhrase.getText().toString().trim();
        if (!phrase.isEmpty()) {
            try {
                SecretKey key = generateKey();
                Bundle bundle = new Bundle();
                bundle.putString(MyLoader.ARG_WORD, phrase);
                bundle.putSerializable(MyLoader.ARG_KEY, key);
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, bundle, this);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error generating key", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MyLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data != null) {
            byte[] encryptedBytes = Base64.decode(data, Base64.DEFAULT);
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, ((MyLoader) loader).key);
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                String decryptedPhrase = new String(decryptedBytes);
                Toast.makeText(this, "Decrypted message: " + decryptedPhrase, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error decrypting message", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // No action needed
    }

    private SecretKey generateKey() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed("any data used as random seed".getBytes());
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, secureRandom);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}