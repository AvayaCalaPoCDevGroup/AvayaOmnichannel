package com.example.avayaivr.UI.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.avayaivr.UI.Activities.MainActivity;
import com.example.avayaivr.R;

import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment {

    public static final int PICK_IMAGE_REQUEST_HEAD = 45330;
    public static final int PICK_IMAGE_REQUEST_BODY = 45331;

    private TextView et_settings_contact;
    private TextView et_settings_whatsapp;
    private TextView et_settings_messenger;
    private TextView et_settings_head;
    private TextView et_settings_body;
    private Button btn_settings_head;
    private Button btn_settings_body;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_settings_contact = view.findViewById(R.id.et_settings_contact);
        et_settings_whatsapp = view.findViewById(R.id.et_settings_whatsapp);
        et_settings_messenger = view.findViewById(R.id.et_settings_messenger);
        et_settings_head = view.findViewById(R.id.et_settings_head);
        et_settings_body = view.findViewById(R.id.et_settings_body);
        btn_settings_head = view.findViewById(R.id.btn_settings_head);
        btn_settings_body = view.findViewById(R.id.btn_settings_body);

        Button btn_test = view.findViewById(R.id.btn_test);

        btn_test.setOnClickListener(v -> {
            AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getContext(), 0, colorListener);
            ambilWarnaDialog.show();
        });
        btn_settings_head.setOnClickListener(v -> {
            ChooseImage(PICK_IMAGE_REQUEST_HEAD);
        });
        btn_settings_body.setOnClickListener(v -> {
            ChooseImage(PICK_IMAGE_REQUEST_BODY);
        });

    }

    private void ChooseImage(int requestCode) {
        if(((MainActivity)getActivity()).ChekFilePermission()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
        } else {
            Toast.makeText(getContext(), "File permission needed", Toast.LENGTH_SHORT).show();
        }
    }

    AmbilWarnaDialog.OnAmbilWarnaListener colorListener = new AmbilWarnaDialog.OnAmbilWarnaListener() {
        @Override
        public void onCancel(AmbilWarnaDialog dialog) {

        }

        @Override
        public void onOk(AmbilWarnaDialog dialog, int color) {
            ((MainActivity)getActivity()).setAppColor(color);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_IMAGE_REQUEST_BODY || requestCode == PICK_IMAGE_REQUEST_HEAD) && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //Si el intent recivido es de la seleccion de imagen, se procede a generar el recurso que sera el logo de la aplicacion
            Uri uri = data.getData();
            //Uri.fromFile(new File("/sdcard/sample.jpg"))

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                     /*Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                     getSupportActionBar().setLogo(drawable);
                     getSupportActionBar().setDisplayUseLogoEnabled(true);*/
                switch (requestCode){
                    case PICK_IMAGE_REQUEST_BODY:
                        et_settings_body.setText(uri.getPath());
                        break;
                    case PICK_IMAGE_REQUEST_HEAD:
                        et_settings_head.setText(uri.getPath());
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}