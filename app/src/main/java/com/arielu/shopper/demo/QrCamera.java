package com.arielu.shopper.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.util.List;

public class QrCamera extends AppCompatActivity {

CameraView camera_view ;
boolean isDetected = false ;
Button btn_start_again ;

FirebaseVisionBarcodeDetectorOptions options ;
FirebaseVisionBarcodeDetector detector ;


    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_qr_camera);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA )
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupCamera() ;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QrCamera.this , "You must accept permission!" , Toast.LENGTH_LONG) ;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.RECORD_AUDIO )
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupCamera() ;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QrCamera.this , "You must accept permission!" , Toast.LENGTH_LONG) ;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void setupCamera() {
        btn_start_again = (Button) findViewById(R.id.btn_again);
        btn_start_again.setEnabled(isDetected);
        btn_start_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDetected = !isDetected ;
            }
        });

        camera_view = (CameraView) findViewById(R.id.cameraView) ;
        camera_view.setLifecycleOwner(this);
        camera_view.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                processImage(getVisionImageFromFrame(frame)) ;
            }
        });

        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build() ;
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options) ;

    }


    private void processImage(FirebaseVisionImage image) {
        if(!isDetected)
        {
            detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                            processResult(firebaseVisionBarcodes);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QrCamera.this , ""+e.getMessage() , Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {

        if(firebaseVisionBarcodes.size() > 0)
        {
            isDetected = true ;
            btn_start_again.setEnabled(isDetected);
            for(FirebaseVisionBarcode item : firebaseVisionBarcodes)
            {
                int value_type = item.getValueType() ;
                switch (value_type)
                {
                    case FirebaseVisionBarcode.TYPE_TEXT :
                    {
                        createDialog(item.getRawValue());
                    }
                    break;
                    case FirebaseVisionBarcode.TYPE_URL :
                    {
                        //Start Browser intent
                        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(item.getRawValue()));
                    }
                    break ;
                    case FirebaseVisionBarcode.TYPE_CONTACT_INFO :
                    {
                        String info = new StringBuilder("Name: ")
                                .append(item.getContactInfo().getName().getFormattedName())
                                .append("\n")
                                .append("Address: ")
                                .append(item.getContactInfo().getAddresses().get(0).getAddressLines()[0])
                                .append("\n")
                                .append("Email: ")
                                .append(item.getContactInfo().getEmails().get(0).getAddress())
                                .toString();
                        createDialog(info);
                    }
                    break;
                }
            }
        }
    }

    private void createDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create() ;
        dialog.show();

    }

    private FirebaseVisionImage getVisionImageFromFrame(Frame frame) {
        byte[] data = frame.getData() ;
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
               // .setRotation(frame.getRotation()) //only use it if you want to work on Land scape mode , - for portrail , dont use it
                .build() ;

        return FirebaseVisionImage.fromByteArray(data , metadata) ;

    }

}
