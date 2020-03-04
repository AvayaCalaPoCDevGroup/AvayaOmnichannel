package com.example.avayaivr.UI.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.avayaivr.R;
import com.example.avayaivr.UI.Clases.Constants;

import java.io.File;
import java.io.IOException;

public class HomeFragment extends Fragment {

    ImageView iv_home_head;
    ImageView iv_home_body;

    private SharedPreferences mSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mSharedPreferences = getContext().getSharedPreferences(Constants.AVAYAIVR_PREFERENCES,0);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_home_head = view.findViewById(R.id.iv_home_head);
        iv_home_body = view.findViewById(R.id.iv_home_body);
        iv_home_body.setVisibility(View.GONE); //Esto lo puse por que el usuario solo quiere una imagen.
        String headPath = mSharedPreferences.getString(Constants.PREF_HEAD, "");
        String bodyPath = mSharedPreferences.getString(Constants.PREF_BODY, "");

        if(!headPath.equals("")){
            Uri uriHead = Uri.fromFile(new File(headPath));
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriHead);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            iv_home_head.setImageDrawable(null);
            iv_home_head.setBackground(drawable);
        }
        if(!bodyPath.equals("")){
            Uri uribody = Uri.fromFile(new File(bodyPath));
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uribody);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);

            iv_home_body.setImageDrawable(null);
            iv_home_body.setBackground(drawable);
        }
    }
}