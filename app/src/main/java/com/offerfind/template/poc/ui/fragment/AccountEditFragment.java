package com.offerfind.template.poc.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.CropActivity;
import com.offerfind.template.poc.ui.activity.MainActivity;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;
import com.offerfind.template.poc.utils.StaticKeys;

import java.io.ByteArrayOutputStream;
import java.io.File;

import timber.log.Timber;

/**
 * Created by ugar on 17.02.16.
 */
public class AccountEditFragment extends BaseFragment implements View.OnClickListener {

    private ImageView avatarView;
    private Bitmap bitmap;
    private String imageInfo;
    private Uri uri;

    public static AccountEditFragment newInstance() {
        return new AccountEditFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            uri = savedInstanceState.getParcelable("uri");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_edit, container, false);
        view.findViewById(R.id.save).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        avatarView = (ImageView) view.findViewById(R.id.avatar);
        avatarView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("uri", uri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                //TODO: save changes
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.cancel:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.avatar:
                showAttachDialog();
                break;
        }
    }

    private void showAttachDialog() {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_attach_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView).create();
        final AlertDialog dialog = builder.show();
        dialogView.findViewById(R.id.attach_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startCroup(StaticKeys.ATTACH_IMAGE_REQUEST_CODE);
            }
        });
        dialogView.findViewById(R.id.make_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startCroup(StaticKeys.MAKE_PHOTO_REQUEST_CODE);
            }
        });
    }

    private void startCroup(int attach) {
        Intent intent = null;
        switch (attach) {
            case StaticKeys.ATTACH_IMAGE_REQUEST_CODE:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                break;
            case StaticKeys.MAKE_PHOTO_REQUEST_CODE:
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(getActivity(), "SD card not available", Toast.LENGTH_LONG).show();
                    return;
                }
                File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName());
                if (!path.mkdirs()) {
                    path.mkdirs();
                }
                if (!path.exists()) {
                    Toast.makeText(getActivity(), "Can not create folder.", Toast.LENGTH_LONG).show();
                    return;
                }
                String photoName = String.valueOf(System.currentTimeMillis());
                File newFile = new File(path.getPath() + File.separator + photoName + ".jpg");
                uri = Uri.fromFile(newFile);
                Timber.i("uri = " + uri);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                break;
        }

        if (intent == null)
            return;

        try {
            getParentFragment().startActivityForResult(intent, attach);
//            cropStart = true;
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "No handler App for this type of file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void bitmapToBase64() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Timber.i("bitmapToBase64 byteArray.length = " + byteArray.length);
        imageInfo = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        Timber.i("bitmapToBase64 imageInfo = " + imageInfo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case StaticKeys.CROUP_REQUEST_CODE:
                if (resultCode == MainActivity.RESULT_OK) {
                    String imagePatch = intent.getStringExtra(StaticKeys.CROP_IMAGE_URI);
                    Timber.i("imagePatch = " + imagePatch);
//                    imageInfo = imagePatch.substring(imagePatch.lastIndexOf(".") + 1).toLowerCase();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = BitmapFactory.decodeFile(imagePatch, options);
                    bitmapToBase64();
                    //TODO: add imageInfo to server
                } else if (resultCode == MainActivity.RESULT_CANCELED) {
                    Timber.i("Funded sale canceled");
                }
                break;
            case StaticKeys.MAKE_PHOTO_REQUEST_CODE: {
                if (resultCode == MainActivity.RESULT_OK) {
                    String imagePatch = String.valueOf(uri).replace("file://", "");
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = BitmapFactory.decodeFile(imagePatch, options);
                    bitmapToBase64();
                    //TODO: add imageInfo to server
                } else if (resultCode == MainActivity.RESULT_CANCELED) {
                    Timber.i("Funded sale canceled");
                }
                break;
            }
            case StaticKeys.ATTACH_IMAGE_REQUEST_CODE:
                if (resultCode == MainActivity.RESULT_OK) {
                    String filePath = null;
                    if ("content".equalsIgnoreCase(intent.getData().getScheme())) {
                        final String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(intent.getData(), projection, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            cursor.close();
                        } else {
                            Timber.e("Error cursor onActivityResult");
                            return;
                        }
                    }
                    if (filePath == null) {
                        showDialog(getResources().getString(R.string.bad_file_format));
                        return;
                    }
                    String imageInfo = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
                    if (!(imageInfo.equals("jpeg") || imageInfo.equals("jpg") || imageInfo.equals("png"))) {
                        showDialog(getResources().getString(R.string.bad_file_format));
                        return;
                    }
                    intent = new Intent(getActivity(), CropActivity.class);
                    intent.putExtra(StaticKeys.CROP_IMAGE_URI, filePath);
                    getParentFragment().startActivityForResult(intent, StaticKeys.CROUP_REQUEST_CODE);
                } else if (resultCode == MainActivity.RESULT_CANCELED) {
                    Timber.i("Funded sale canceled");
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, intent);
        }
    }

}
