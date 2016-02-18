package com.offerfind.template.poc.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.core.gestures.MoveGestureDetector;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;
import com.offerfind.template.poc.utils.StaticKeys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

/**
 * Created by ugar on 17.02.16.
 */
public class FragmentCrop extends BaseFragment implements View.OnClickListener, View.OnTouchListener {

    private String filePatch;
    private ImageView image, imageTemplate;
    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;
    private Matrix mMatrix = new Matrix();
    private float mScaleFactor = 1f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private float prevPointX = 0, prevPointY = 0;
    private float scaledImagePointX = 0, scaledImagePointY = 0;
    private Bitmap photoImg;
    private View viewTop;

    public static FragmentCrop newInstance() {
        return new FragmentCrop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crop, container, false);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.save).setOnClickListener(this);
        image = (ImageView) view.findViewById(R.id.cp_img);
        image.setOnTouchListener(this);
        imageTemplate = (ImageView) view.findViewById(R.id.cp_face_template);
        viewTop = view.findViewById(R.id.view_padding_top);
        filePatch = getActivity().getIntent().getStringExtra(StaticKeys.CROP_IMAGE_URI);
        Timber.i("filePatch = " + filePatch);
        try {
            getCorrectlyOrientedImage(filePatch);
        } catch (IOException e) {
            Timber.i("catch onCreate");
            e.printStackTrace();
        }

        image.setImageBitmap(photoImg);
        image.post(new Runnable() {
            @Override
            public void run() {
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                mScaleFactor = 1;
                mMatrix.postTranslate(mFocusX, mFocusY);
                image.setImageMatrix(mMatrix);
            }
        });

        mScaleDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());
        mMoveDetector = new MoveGestureDetector(getActivity(), new MoveListener());
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            photoImg.recycle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCorrectlyOrientedImage(String photoUri) throws IOException {
        InputStream is = getActivity().getContentResolver().openInputStream(Uri.parse("file://" + photoUri));
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        is = getActivity().getContentResolver().openInputStream(Uri.parse("file://" + photoUri));
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int max = width > height ? width : height;
        if (dbo.outWidth > max || dbo.outHeight > max) {
            float widthRatio = ((float) dbo.outWidth) / ((float) max);
            float heightRatio = ((float) dbo.outHeight) / ((float) max);
            float maxRatio = Math.max(widthRatio, heightRatio);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            photoImg = BitmapFactory.decodeStream(is, null, options);
        } else {
            photoImg = BitmapFactory.decodeStream(is);
        }
        is.close();

        ExifInterface exif = new ExifInterface(photoUri);
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Timber.i("rotation = " + rotation + " String.valueOf(photoUri) = " + String.valueOf(photoUri));
        int rotationInDegrees = exifToDegrees(rotation);
        Matrix matrix = new Matrix();
        if (rotation != 0) {
            matrix.preRotate(rotationInDegrees);
        }
        if (photoImg != null) {
            photoImg = Bitmap.createBitmap(photoImg, 0, 0, photoImg.getWidth(), photoImg.getHeight(), matrix, true);
        }

    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName());
                if (!path.mkdirs()) {
                    path.mkdirs();
                }
                File newFile;
                if (filePatch.contains(getActivity().getPackageName())) {
                    newFile = new File(filePatch);
                } else {
                    String photoName = String.valueOf(System.currentTimeMillis());
                    newFile = new File(path.getPath() + File.separator + photoName + ".jpg");
                }

                try {
                    FileOutputStream out = new FileOutputStream(newFile);
                    image.buildDrawingCache(true);
                    image.setDrawingCacheEnabled(true);
                    imageTemplate.buildDrawingCache(true);
                    imageTemplate.setDrawingCacheEnabled(true);
                    cropImage(image.getDrawingCache(true), imageTemplate.getDrawingCache(true)).compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    photoImg.recycle();
                } catch (Exception e) {
                    Timber.i("catch onClick");
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra(StaticKeys.CROP_IMAGE_URI, newFile.getAbsolutePath());
                getActivity().setResult(getActivity().RESULT_OK, intent);
                getActivity().finish();
                break;
            case R.id.cancel:
                photoImg.recycle();
                getActivity().finish();
                break;
        }
    }

    public Bitmap cropImage(Bitmap img, Bitmap templateImage) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap finalBm = Bitmap.createBitmap(img, (img.getWidth() - templateImage.getWidth()) / 2,
                viewTop.getHeight() + imageTemplate.getTop(), templateImage.getWidth(), templateImage.getHeight(), matrix, true);
        img.recycle();
        templateImage.recycle();
        return finalBm;
    }

    public boolean onTouch(View v, MotionEvent event) {
        float firstPointX = 0, firstPointY = 0, secondPointX = 0, secondPointY = 0;

        mScaleDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    firstPointX = event.getX(0);
                    firstPointY = event.getY(0);
                    secondPointX = event.getX(1);
                    secondPointY = event.getY(1);
                    float dx = firstPointX - secondPointX;
                    float dy = firstPointY - secondPointY;
                    scaledImagePointX = (firstPointX - dx / 2);
                    scaledImagePointY = (firstPointY - dy / 2);
                }
                mMatrix.postTranslate((scaledImagePointX - scaledImagePointX * mScaleFactor) / mScaleFactor + prevPointX,
                        (scaledImagePointY - scaledImagePointY * mScaleFactor) / mScaleFactor + prevPointY);
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                mScaleFactor = 1;
                mMatrix.postTranslate(mFocusX, mFocusY);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) {
                    prevPointX = (scaledImagePointX - scaledImagePointX * mScaleFactor) / mScaleFactor;
                    prevPointY = (scaledImagePointY - scaledImagePointY * mScaleFactor) / mScaleFactor;
                }
                break;
        }

        ImageView view = (ImageView) v;
        view.setImageMatrix(mMatrix);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mFocusX = d.x;
            mFocusY = d.y;
            return true;
        }
    }
}
