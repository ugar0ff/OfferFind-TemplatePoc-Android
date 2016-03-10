package com.dddev.market.place.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Account;
import com.dddev.market.place.core.api.strongloop.AccountPutRepository;
import com.dddev.market.place.ui.activity.CropActivity;
import com.dddev.market.place.ui.activity.MainActivity;
import com.dddev.market.place.ui.adapter.GeoAutoCompleteAdapter;
import com.dddev.market.place.ui.fragment.base.BaseLocationFragment;
import com.dddev.market.place.ui.model.GeoSearchResult;
import com.dddev.market.place.ui.views.DelayAutoCompleteTextView;
import com.dddev.market.place.utils.PreferencesUtils;
import com.dddev.market.place.utils.StaticKeys;
import com.dddev.market.place.utils.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;

import timber.log.Timber;

/**
 * Created by ugar on 17.02.16.
 */
public class AccountEditFragment extends BaseLocationFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Bitmap bitmap;
    private String imageInfo;
    private Uri uri;
    private DelayAutoCompleteTextView inputAddress;
    private EditText inputName, inputEmail, inputBankInfo;
    private GeoAutoCompleteAdapter geoAutoCompleteAdapter;

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
        inputName = (EditText) view.findViewById(R.id.name);
        inputEmail = (EditText) view.findViewById(R.id.email);
        inputBankInfo = (EditText) view.findViewById(R.id.banking_info);
        inputAddress = (DelayAutoCompleteTextView) view.findViewById(R.id.location);
//        inputAddress.setThreshold(2);
        geoAutoCompleteAdapter = new GeoAutoCompleteAdapter(getActivity(), mLastLocation);
        inputAddress.setAdapter(geoAutoCompleteAdapter);
        inputAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
                inputAddress.setText(result.getAddress());
            }
        });
        inputAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        CheckBox checkBoxLocale = (CheckBox) view.findViewById(R.id.checkbox_locale);
        checkBoxLocale.setOnCheckedChangeListener(this);
//        if (PreferencesUtils.isLocaleCheckBoxEnable(getActivity())) {
            checkBoxLocale.setChecked(PreferencesUtils.isLocaleCheckBoxEnable(getActivity()));
//            onCheckedChanged(checkBoxLocale, PreferencesUtils.isLocaleCheckBoxEnable(getActivity()));
//        }
        ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
        avatarView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("uri", uri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        inputName.setText(PreferencesUtils.getUserName(getActivity()));
        inputAddress.setText(PreferencesUtils.getUserAddress(getActivity()));
        inputEmail.setText(PreferencesUtils.getUserEmail(getActivity()));
        inputBankInfo.setText(PreferencesUtils.getUserBankInfo(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                saveUserData();
                break;
            case R.id.cancel:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.avatar:
                showAttachDialog();
                break;
        }
    }

    private void saveUserData() {
        final AccountPutRepository accountPutRepository = AppOfferFind.getRestAdapter(getActivity()).createRepository(AccountPutRepository.class);
        accountPutRepository.createContract();
        if (inputName.getText().toString().length() == 0) {
            inputName.setError(getString(R.string.invalid_name));
            return;
        }
        if (!Utilities.isValidEmail(inputEmail.getText().toString())) {
            inputEmail.setError(getString(R.string.invalid_email));
            return;
        }
        if (PreferencesUtils.isLocaleCheckBoxEnable(getActivity())) {
            accountPutRepository.accounts(inputName.getText().toString(), inputBankInfo.getText().toString(),
                    inputEmail.getText().toString(), new AccountPutRepository.UserCallback() {
                @Override
                public void onSuccess(Account account) {
                    Timber.i("onSuccess response=%s", account.toString());
                    if (getActivity() != null) {
                        getParentFragment().getChildFragmentManager().popBackStack();
                        PreferencesUtils.setUserName(getActivity(), account.getName());
                        PreferencesUtils.setUserBankInfo(getActivity(), account.getBankInfo());
                        PreferencesUtils.setUserEmail(getActivity(), account.getEmail());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Timber.e("onError Throwable: %s", t.toString());
                    showDialog(t.toString());
                }
            });
        } else {
            accountPutRepository.accounts(inputName.getText().toString(), inputBankInfo.getText().toString(),
                    inputEmail.getText().toString(), inputAddress.getText().toString(), new AccountPutRepository.UserCallback() {
                @Override
                public void onSuccess(Account account) {
                    Timber.i("onSuccess response=%s", account.toString());
                    getParentFragment().getChildFragmentManager().popBackStack();
                    if (getActivity() != null) {
                        PreferencesUtils.setUserName(getActivity(), account.getName());
                        PreferencesUtils.setUserBankInfo(getActivity(), account.getBankInfo());
                        PreferencesUtils.setUserEmail(getActivity(), account.getEmail());
                        PreferencesUtils.setUserAddress(getActivity(), account.getAddress());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Timber.e("onError Throwable: %s", t.toString());
                    showDialog(t.toString());
                }
            });
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
                Timber.i("uri = %s", uri);
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
        Timber.i("bitmapToBase64 byteArray.length = %s", +byteArray.length);
        imageInfo = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        Timber.i("bitmapToBase64 imageInfo = %s", imageInfo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case StaticKeys.CROUP_REQUEST_CODE:
                if (resultCode == MainActivity.RESULT_OK) {
                    String imagePatch = intent.getStringExtra(StaticKeys.CROP_IMAGE_URI);
                    Timber.i("imagePatch = %s", imagePatch);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PreferencesUtils.setLocaleCheckBoxState(getActivity(), isChecked);
        inputAddress.setEnabled(true);
        if (isChecked) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                inputAddress.setText(getString(R.string.device_location));
                inputAddress.setEnabled(false);
            } else {
                inputAddress.setText("");
                loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
            }
        } else {
            inputAddress.setText("");
        }
        getAddress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.i("onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    inputAddress.setText(getString(R.string.device_location));
                    inputAddress.setEnabled(false);
                } else {
                    inputAddress.setEnabled(true);
                    inputAddress.setText("");
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void addressReceiveResult(String result) {
        Timber.i("addressReceiveResult = %s", result);
    }

    @Override
    public void locationReceiveResult(Location location) {
        Timber.i("locationReceiveResult = %s", location);
        if (getActivity() != null && PreferencesUtils.isLocaleCheckBoxEnable(getActivity())) {
            geoAutoCompleteAdapter.setLocation(location);
        }
    }
}
