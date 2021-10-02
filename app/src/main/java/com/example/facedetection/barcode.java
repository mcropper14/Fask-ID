package com.example.facedetection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


//the app requires a setup that has the user enter their name and scan mask qr code
//that name is then stored in the app as a String
//the mask has a qr code that stores the name of the user's mask as a String
//scanCode scans the mask qr code
//if the String stored in the qr code of the mask matches the string stored in the app then the phone unlocks

//create a GUI page to setup
//textbox to enter name
//picture to store qr code, etc....


public class barcode extends AppCompatActivity implements View.OnClickListener{


    Button scanBtn;

    String maskName = "Myra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        scanBtn = (Button)findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        scanCode();
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Mask Code");

        //used 0 to test qr code on computer
        integrator.setCameraId(1); //1 is front facing, 0 is rear facing
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result  = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                if(result.getContents().equals(maskName)){
                    Toast.makeText(this, "Unlocked Phone", Toast.LENGTH_LONG).show();
                    //add code for unlocking phone
                    finishAffinity();
                }else{
                    Toast.makeText(this, "Incorrect Mask Code!!", Toast.LENGTH_LONG).show();
                    builder.setPositiveButton("Scan Mask Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            scanCode();
                        }
                    }).setNegativeButton("finished", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    });
                }
                builder.setTitle("Scanning Mask Code..");

                AlertDialog dialogue = builder.create();
                dialogue.show();

            }
            else{
                Toast.makeText(this, "Not a valid mask code", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }



    }


}