package com.example.facedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {


    private Button newActivity;

    private Button faceDetectButton;
    private GraphicOverlay graphicOverlay;
    private CameraView cameraView;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        newActivity= findViewById(R.id.new_activity);

        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBarcode();
            }
        });


        faceDetectButton = findViewById(R.id.detect_face_btn);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        cameraView = findViewById(R.id.camera_view);
        cameraView.setFacing(CameraKit.Constants.FACING_FRONT);



        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait, Processing..")
                .setCancelable(false)
                .build();


        faceDetectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();

                cameraView.captureImage();
                graphicOverlay.clear();
                //change camera to be facing front

            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                alertDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();
                cameraView.setFacing(CameraKit.Constants.FACING_FRONT);

                processFacedetection(bitmap);

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

    }


    //gets the image from the console

    private void processFacedetection(Bitmap bitmap) {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions firebaseVisionFaceDetectorOptions = new FirebaseVisionFaceDetectorOptions.Builder().build();

        FirebaseVisionFaceDetector firebaseVisionFaceDetector = FirebaseVision.getInstance()
                .getVisionFaceDetector(firebaseVisionFaceDetectorOptions);

        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                        getFaceResults(firebaseVisionFaces);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getFaceResults(List<FirebaseVisionFace> firebaseVisionFaces) {
        int counter = 0;
        for(FirebaseVisionFace face : firebaseVisionFaces){
            Rect rect = face.getBoundingBox();
            RectOverlay rectOverlay = new RectOverlay(graphicOverlay, rect);

            graphicOverlay.add(rectOverlay);

            counter = 1 + counter;

        }
        alertDialog.dismiss();
    }


    public void openBarcode(){
        Intent intent = new Intent(this, barcode.class); //.barcode
        startActivity(intent);
    }



    @Override
    protected void onPause() {
        super.onPause();

        cameraView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraView.start();
    }

}