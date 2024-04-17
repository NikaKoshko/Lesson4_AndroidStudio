package ru.mirea.lukanina.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_WORD = "word";
    public static final String ARG_KEY = "key";

    private String phrase;
    public SecretKey key;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            phrase = args.getString(ARG_WORD);
            key = (SecretKey) args.getSerializable(ARG_KEY);
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(phrase.getBytes());
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
