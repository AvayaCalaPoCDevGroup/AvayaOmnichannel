package com.example.avayaivr.UI.Fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.avayaivr.R;
import com.example.avayaivr.UI.Clases.Constants;

import java.io.File;
import java.io.IOException;

public class ChatFragment extends Fragment {

    private ImageView iv_chat_head;
    private WebView wv_chat_chat;
    private SharedPreferences mSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        mSharedPreferences = getContext().getSharedPreferences(Constants.AVAYAIVR_PREFERENCES,0);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_chat_head = view.findViewById(R.id.iv_chat_head);
        wv_chat_chat = view.findViewById(R.id.wv_chat_chat);

        String headPath = mSharedPreferences.getString(Constants.PREF_HEAD, "");

        if(!headPath.equals("")){
            Uri uriHead = Uri.fromFile(new File(headPath));
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriHead);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            iv_chat_head.setBackground(drawable);
        }

        WebSettings Ws = wv_chat_chat.getSettings();
        Ws.setJavaScriptEnabled(true);
        //Ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_chat_chat.setWebViewClient(new WebViewClient());
        wv_chat_chat.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        wv_chat_chat.loadUrl("https://urldefense.proofpoint.com/v2/url?u=https-3A__s02.ysocial.net-3A9095_02d0d50314eea0c3f41e7faee62ac82a9351f846_29663c7a-2Db7f9-2D4288-2Db322-2D932ada1c1a8b_chat&d=DwMFaQ&c=BFpWQw8bsuKpl1SgiZH64Q&r=aa_d_CV7CZiPr48wXAkUvirApAlAbCas7mQZKpwzV8E&m=BkUHE0gOxqsGwICTgWofSKcNzzlxkxos5dBn1-iC_U0&s=A6GRgfRXbaqTWvMgVFPUOHQ-8bEg8QCU6HJkVOlkpIo&e=");
    }
}
