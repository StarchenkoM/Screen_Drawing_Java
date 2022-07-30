package com.trd.screendrawing;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.trd.screendrawing.databinding.ActivityPaintBinding;
import com.trd.screendrawing.databinding.BottomSheetDialogBinding;

public class PaintActivity extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private final String SCREEN_VIEW_TAG = "SCREEN_VIEW_TAG";
    private PaintView paintView;
    private Paint mPaint;
    private Drawable drawableBackground;
    private BottomSheetDialog bottomSheetDialog;
    private ActivityPaintBinding binding;
    private BottomSheetDialogBinding bottomDialogBinding;
    private ColorItem selectedColor;
    private ColorHolder colorList;
    private Spinner colorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaintBinding.inflate(getLayoutInflater());
        initScreenElements();
        initListeners();
        setContentView(binding.getRoot());
    }

    private void initScreenElements() {
        initBottomSheetDialog();
        initPaintView();
        initPaint();
        initColorList();
        initSpinner();
        setBackground(drawableBackground);
    }

    private void initColorList() {
        colorList = new ColorHolder();
    }

    private void initSpinner() {
        selectedColor = colorList.getDefaultColor();
        colorSpinner = initColorSpinner(colorList);
    }

    private void initListeners() {
        setOpenBottomDialogListener();
        setOpenCameraListener();
        setOpenColorPickerListener();
        setCleanScreenListener();
    }

    private void setCleanScreenListener() {
        binding.clearScreenBtn.setOnClickListener(v -> {
            paintView.clearDrawing();
            clearScreenViews();
        });
    }

    private void clearScreenViews() {
        View screenView = binding.getRoot().findViewWithTag(SCREEN_VIEW_TAG);
        while (screenView != null) {
            ((ViewGroup) screenView.getParent()).removeView(screenView);
            screenView = binding.getRoot().findViewWithTag(SCREEN_VIEW_TAG);
        }
    }

    private void initBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        bottomDialogBinding = BottomSheetDialogBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bottomDialogBinding.getRoot());
    }

    private void setOpenBottomDialogListener() {
        binding.imageDialogBtn.setOnClickListener(view -> openBottomSheetDialog());
    }

    private void setOpenCameraListener() {
        binding.openCameraBtn.setOnClickListener(view -> handleCamera());
    }

    private void setOpenColorPickerListener() {
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                handleColorSelection(colorList, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void handleColorSelection(ColorHolder colorList, int position) {
        selectedColor = colorList.basicColors().get(position);
        int color = Color.parseColor(selectedColor.getHexHash());
        mPaint.setColor(color);
    }

    private Spinner initColorSpinner(ColorHolder colorList) {
        Spinner spinner = binding.colorSpinner;
        ColorSpinnerAdapter adapterA = new ColorSpinnerAdapter(getApplicationContext(), colorList.basicColors());
        spinner.setAdapter(adapterA);
        spinner.setSelection(colorList.colorPosition(selectedColor), false);
        return spinner;
    }

    private void initPaint() {
        mPaint = binding.paintView.getPaint();
    }

    private void initPaintView() {
        this.paintView = binding.paintView;
        paintView.setDrawingCacheEnabled(true);
    }

    private void setBackground(Drawable drawableBackground) {
        if (drawableBackground != null) {
            binding.paintView.setBackground(drawableBackground);
        } else {
            binding.paintView.setBackgroundResource(R.drawable.default_background);
        }
    }

    private void openBottomSheetDialog() {
        handleImageClick(bottomDialogBinding.positive); // need to refactor
        handleImageClick(bottomDialogBinding.cool);
        handleImageClick(bottomDialogBinding.sad);
        handleImageClick(bottomDialogBinding.happy);
        handleImageClick(bottomDialogBinding.star);
        handleImageClick(bottomDialogBinding.smile);
        bottomSheetDialog.show();
    }

    private void handleImageClick(ImageView image) {
        image.setOnClickListener(view -> {
            setViewMovingListener(image.getDrawable());
            bottomSheetDialog.dismiss();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViewMovingListener(Drawable image) {
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageDrawable(image);
        LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setTag(SCREEN_VIEW_TAG);
        binding.printLayout.addView(iv);
        moveView(iv);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void moveView(View image) {
        image.setOnTouchListener(new View.OnTouchListener() {
            float x, y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP: {
                        x = v.getX() - event.getRawX();
                        y = v.getY() - event.getRawY();
                        return true;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        image.animate()
                                .x(event.getRawX() + x)
                                .y(event.getRawY() + y)
                                .setDuration(0)
                                .start();
                        return true;
                    }
                    default:
                        return true;
                }
            }
        });
    }

    private void handleCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            handlePermissionResult(grantResults);
        }
    }

    private void handlePermissionResult(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.camera_permission_granted, Toast.LENGTH_LONG).show();
            launchCamera();
        } else {
            Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_LONG).show();
        }
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
            drawableBackground = new BitmapDrawable(getResources(), cameraImage);
            setBackground(drawableBackground);
        }
    }

}