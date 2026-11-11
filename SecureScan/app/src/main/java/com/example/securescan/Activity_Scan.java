package com.example.securescan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Activity_Scan extends AppCompatActivity {
    private static final String TAG = "Activity_Scan";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    private final AtomicBoolean isProcessingFrame = new AtomicBoolean();
    private final AtomicBoolean hasReportedScan = new AtomicBoolean();
    private PreviewView cameraPreview;
    private ExecutorService cameraExecutor;
    private BarcodeScanner barcodeScanner;
    private ActivityResultLauncher<String> galleryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryPicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(), this::scanGalleryImage);
        setContentView(R.layout.activity_scan);

        cameraPreview = findViewById(R.id.cameraPreview);
        Button buttonImportGallery = findViewById(R.id.buttonImportGallery);
        ImageButton buttonScanQR = findViewById(R.id.buttonScanQR);
        cameraExecutor = Executors.newSingleThreadExecutor();
        barcodeScanner = BarcodeScanning.getClient();

        buttonImportGallery.setOnClickListener(v -> galleryPicker.launch("image/*"));
        buttonScanQR.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            }
        });
    }

    private void startCamera() {
        hasReportedScan.set(false);
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeImage);

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis);
            } catch (Exception e) {
                Log.e(TAG, "Failed to start camera", e);
                Toast.makeText(this, "Unable to start the camera", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void analyzeImage(ImageProxy imageProxy) {
        if (hasReportedScan.get() || !isProcessingFrame.compareAndSet(false, true)) {
            imageProxy.close();
            return;
        }

        try {
            android.media.Image mediaImage = imageProxy.getImage();
            if (mediaImage == null) {
                isProcessingFrame.set(false);
                imageProxy.close();
                return;
            }
            InputImage image = InputImage.fromMediaImage(
                    mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            barcodeScanner.process(image)
                    .addOnSuccessListener(this::handleCameraResults)
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to scan camera frame", e))
                    .addOnCompleteListener(task -> {
                        isProcessingFrame.set(false);
                        imageProxy.close();
                    });
        } catch (RuntimeException e) {
            isProcessingFrame.set(false);
            imageProxy.close();
            Log.e(TAG, "Failed to process camera frame", e);
        }
    }

    private void handleCameraResults(List<Barcode> barcodes) {
        for (Barcode barcode : barcodes) {
            String value = barcode.getRawValue();
            if (value != null && !value.trim().isEmpty()
                    && hasReportedScan.compareAndSet(false, true)) {
                showScanResult(value);
                return;
            }
        }
    }

    private void scanGalleryImage(Uri imageUri) {
        if (imageUri == null) {
            return;
        }

        try {
            InputImage image = InputImage.fromFilePath(this, imageUri);
            barcodeScanner.process(image)
                    .addOnSuccessListener(this::handleGalleryResults)
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to scan gallery image", e);
                        Toast.makeText(this, "Failed to scan image", Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            Log.e(TAG, "Unable to open gallery image", e);
            Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleGalleryResults(List<Barcode> barcodes) {
        for (Barcode barcode : barcodes) {
            String value = barcode.getRawValue();
            if (value != null && !value.trim().isEmpty()) {
                showScanResult(value);
                return;
            }
        }
        Toast.makeText(this, "No QR code found in the selected image", Toast.LENGTH_SHORT).show();
    }

    private void showScanResult(String value) {
        ScanResultClassifier.Result result = ScanResultClassifier.classify(value);
        String message;
        switch (result) {
            case SECURE:
                message = "Secure HTTPS link detected";
                break;
            case INSECURE:
                message = "Warning: HTTP link is not secure";
                break;
            default:
                message = "Scanned code is not a valid web link";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        if (barcodeScanner != null) {
            barcodeScanner.close();
        }
    }
}
