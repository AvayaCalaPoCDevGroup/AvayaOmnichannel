package com.example.avayaivr.UI.Fragments;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.avayaivr.UI.Activities.MainActivity;
import com.example.avayaivr.R;
import com.example.avayaivr.UI.Clases.Constants;
import com.example.avayaivr.UI.Clases.Models.Preset;
import com.example.avayaivr.UI.Clases.WebMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment {

    public static final int PICK_IMAGE_REQUEST_HEAD = 45330;
    public static final int PICK_IMAGE_REQUEST_BODY = 45331;

    private ArrayList<Preset> presetsList = new ArrayList<>();
    private ArrayList<String> presetsStringList = new ArrayList<>();
    private ArrayAdapter adapterPresets;

    private Spinner spnr_presets;
    private TextView et_settings_contact;
    private TextView et_settings_whatsapp;
    private TextView et_settings_messenger;
    private TextView et_settings_facebook;
    private TextView et_settings_chat;
    private TextView et_settings_instagram;
    private TextView et_settings_twitter;
    private TextView et_settings_google;
    private TextView et_settings_spaces;
    private TextView et_settings_emailto;
    private TextView et_settings_emailsubject;
    private TextView et_settings_emailbody;
    private TextView et_settings_head;
    private TextView et_settings_body;
    private Button btn_settings_head;
    private Button btn_settings_body;
    private Button btn_save;

    private SharedPreferences mSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        mSharedPreferences = getContext().getSharedPreferences(Constants.AVAYAIVR_PREFERENCES,0);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spnr_presets = view.findViewById(R.id.spnr_presets);
        et_settings_contact = view.findViewById(R.id.et_settings_contact);
        et_settings_whatsapp = view.findViewById(R.id.et_settings_whatsapp);
        et_settings_facebook = view.findViewById(R.id.et_settings_facebook);
        et_settings_messenger = view.findViewById(R.id.et_settings_messenger);
        et_settings_chat = view.findViewById(R.id.et_settings_chat);
        et_settings_instagram = view.findViewById(R.id.et_settings_instagram);
        et_settings_twitter = view.findViewById(R.id.et_settings_twitter);
        et_settings_google = view.findViewById(R.id.et_settings_google);
        et_settings_spaces = view.findViewById(R.id.et_settings_spaces);
        et_settings_emailto = view.findViewById(R.id.et_settings_emailto);
        et_settings_emailsubject = view.findViewById(R.id.et_settings_emailsubject);
        et_settings_emailbody = view.findViewById(R.id.et_settings_emailbody);
        et_settings_head = view.findViewById(R.id.et_settings_head);
        et_settings_body = view.findViewById(R.id.et_settings_body);
        et_settings_body.setVisibility(View.GONE); //Esto lo puse por que el usuario solo quiere una imagen
        btn_settings_head = view.findViewById(R.id.btn_settings_head);
        btn_settings_body = view.findViewById(R.id.btn_settings_body);
        btn_settings_body.setVisibility(View.GONE); //Esto lo puse por que el usuario solo quiere una imagen
        btn_save = view.findViewById(R.id.btn_save);


        adapterPresets = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, presetsStringList);
        spnr_presets.setAdapter(adapterPresets);

        et_settings_contact.setText(mSharedPreferences.getString(Constants.PREF_CONTACT, ""));
        et_settings_whatsapp.setText(mSharedPreferences.getString(Constants.PREF_WHATSAPP, ""));
        et_settings_messenger.setText(mSharedPreferences.getString(Constants.PREF_MESSENGERID, ""));
        et_settings_facebook.setText(mSharedPreferences.getString(Constants.PREF_FACEBOOK, ""));
        et_settings_chat.setText(mSharedPreferences.getString(Constants.PREF_CHAT, Constants.CHAT_DEFAULT));
        et_settings_instagram.setText(mSharedPreferences.getString(Constants.PREF_INSTAGRAM, ""));
        et_settings_twitter.setText(mSharedPreferences.getString(Constants.PREF_TWITTER, ""));
        et_settings_google.setText(mSharedPreferences.getString(Constants.PREF_GOOGLE, ""));
        et_settings_spaces.setText(mSharedPreferences.getString(Constants.PREF_SPACES, ""));
        et_settings_emailto.setText(mSharedPreferences.getString(Constants.PREF_EMAIL_TO, ""));
        et_settings_emailsubject.setText(mSharedPreferences.getString(Constants.PREF_EMAIL_SUBJECT, ""));
        et_settings_emailbody.setText(mSharedPreferences.getString(Constants.PREF_EMAIL_BODY, ""));

        et_settings_head.setText(mSharedPreferences.getString(Constants.PREF_HEAD, ""));
        et_settings_body.setText(mSharedPreferences.getString(Constants.PREF_BODY, ""));

        Button btn_test = view.findViewById(R.id.btn_test);

        spnr_presets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("SettingsFrament", "item pressed = " + presetsList.get(position).name);
                if(position > 0)
                    renderValues(presetsList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        btn_save.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(Constants.PREF_CONTACT,et_settings_contact.getText().toString());
            editor.putString(Constants.PREF_WHATSAPP,et_settings_whatsapp.getText().toString());
            editor.putString(Constants.PREF_FACEBOOK,et_settings_facebook.getText().toString());
            editor.putString(Constants.PREF_MESSENGERID,et_settings_messenger.getText().toString());
            editor.putString(Constants.PREF_CHAT,et_settings_chat.getText().toString());
            editor.putString(Constants.PREF_INSTAGRAM,et_settings_instagram.getText().toString());
            editor.putString(Constants.PREF_TWITTER,et_settings_twitter.getText().toString());
            editor.putString(Constants.PREF_GOOGLE,et_settings_google.getText().toString());
            editor.putString(Constants.PREF_SPACES,et_settings_spaces.getText().toString());
            editor.putString(Constants.PREF_EMAIL_TO,et_settings_emailto.getText().toString());
            editor.putString(Constants.PREF_EMAIL_SUBJECT,et_settings_emailsubject.getText().toString());
            editor.putString(Constants.PREF_EMAIL_BODY,et_settings_emailbody.getText().toString());

            editor.putString(Constants.PREF_HEAD,et_settings_head.getText().toString());
            editor.putString(Constants.PREF_BODY,et_settings_body.getText().toString());

            editor.commit();

            Toast.makeText(getContext(), "Guardado", Toast.LENGTH_SHORT).show();
        });

        new getPresetsAsync().execute();

    }

    private void renderValues(Preset preset) {
        et_settings_contact.setText(preset.Phone);
        et_settings_whatsapp.setText(preset.WhatsApp);
        et_settings_messenger.setText(preset.Messenger);
        et_settings_facebook.setText(preset.Facebook);
        et_settings_chat.setText(preset.WebChat);
        et_settings_instagram.setText(preset.Instagram);
        et_settings_twitter.setText(preset.Twitter);
        et_settings_google.setText(preset.Google);
        et_settings_spaces.setText(preset.Spaces);
        et_settings_emailto.setText(preset.Email);
        //et_settings_emailsubject.setText(mSharedPreferences.getString(Constants.PREF_EMAIL_SUBJECT, ""));
        //et_settings_emailbody.setText(mSharedPreferences.getString(Constants.PREF_EMAIL_BODY, ""));
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
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(Constants.PREF_COLOR,color);
            editor.commit();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                        et_settings_body.setText(getPath(getContext(),uri));
                        break;
                    case PICK_IMAGE_REQUEST_HEAD:
                        et_settings_head.setText(getPath(getContext(),uri));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Uri filePathUri = null;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri)
    {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        filePathUri = uri;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }

            //DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id.replace("msf:", "")));

                //return getDataColumn(context, uri, null, null);
                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        FileInputStream input = null;
        FileOutputStream output = null;

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (IllegalArgumentException e){
            e.printStackTrace();

            File file = new File(context.getCacheDir(), "tmp");
            String filePath = file.getAbsolutePath();

            try {
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(filePathUri, "r");
                if (pfd == null)
                    return null;

                FileDescriptor fd = pfd.getFileDescriptor();
                input = new FileInputStream(fd);
                output = new FileOutputStream(filePath);
                int read;
                byte[] bytes = new byte[4096];
                while ((read = input.read(bytes)) != -1) {
                    output.write(bytes, 0, read);
                }

                input.close();
                output.close();
                return new File(filePath).getAbsolutePath();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        } finally{
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public class getPresetsAsync extends AsyncTask<Void, Void, String>{

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.dialog_wait_title), getResources().getString(R.string.dialog_wait_msg), true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String resp = WebMethods.getProfiles();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.e("SettingsFrament", "GetPresets On POst Execute s = " + s);
            ArrayList<Preset> newpresetsList = null;
            try{
                newpresetsList = new Gson().fromJson(s, new TypeToken<List<Preset>>(){}.getType());
            } catch (Exception ex) {
                Log.e("SettingsFrament", "GetPresets On POst Execute error = " + ex.getMessage());
                Toast.makeText(getContext(), "Error al obtener los presets", Toast.LENGTH_LONG).show();
            }
            ActualizaPresets(newpresetsList);
        }

        private void ActualizaPresets(ArrayList<Preset> newpresetsList) {
            presetsList.clear();
            presetsStringList.clear();
            presetsList.add(new Preset());
            if(newpresetsList != null)
                presetsList.addAll(newpresetsList);
            for (Preset preset : presetsList) {
                presetsStringList.add(preset.name);
            }
            adapterPresets.notifyDataSetChanged();

        }
    }
}