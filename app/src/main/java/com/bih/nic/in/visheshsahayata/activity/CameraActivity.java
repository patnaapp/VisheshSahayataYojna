package com.bih.nic.in.visheshsahayata.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bih.nic.in.visheshsahayata.Manifest;
import com.bih.nic.in.visheshsahayata.R;
import com.bih.nic.in.visheshsahayata.utility.CameraPreview;
import com.bih.nic.in.visheshsahayata.utility.GlobalVariables;
import com.bih.nic.in.visheshsahayata.utility.MarshmallowPermission;
import com.bih.nic.in.visheshsahayata.utility.Utiilties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CameraActivity extends Activity {

    MarshmallowPermission permission;
    Button btnCamType;
    Button takePhoto;
    ProgressBar progress_finding_location;
    boolean init;
    boolean backCam = true;
    int camType;
    FrameLayout preview;
    File file;
    String imageFilePath;
    String ServerDate="";
    //GoogleApiClient googleApiClient, mGoogleApiClint;
    // private GoogleMap mMap;
    static double latitude = 0.0, longitude = 0.0;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //private LocationRequest mLocationRequest;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 300; // 3 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        init = false;
        permission = new MarshmallowPermission(this, Manifest.permission.CAMERA);
        if (permission.result == -1 || permission.result == 0) {
            try {
                if (!init) initializeCamera(camType);
            } catch (Exception e) {
            }
        } else if (permission.result == 1) {
            if (!init) initializeCamera(camType);
        }

        permission = new MarshmallowPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission.result == -1 || permission.result == 0) {
            try {
                if (!init) initializeCamera(camType);
            } catch (Exception e) {
            }
        } else if (permission.result == 1) {
            if (!init) initializeCamera(camType);
        }

        super.onResume();

    }


    private Camera mCamera;
    private CameraPreview mPreview;

    static Location LastLocation = null;
    LocationManager mlocManager = null;

    AlertDialog.Builder alert;


    private final int UPDATE_ADDRESS = 1;
    private final int UPDATE_LATLNG = 2;
    private static final String TAG = "MyActivity";
    private static byte[] CompressedImageByteArray;
    private static Bitmap CompressedImage;
    private boolean isTimerStarted = false;
    Chronometer chronometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ServerDate = PreferenceManager.getDefaultSharedPreferences(CameraActivity.this).getString("ServerDate", "");

        takePhoto = (Button) findViewById(R.id.btnCapture);
        btnCamType = (Button) findViewById(R.id.btnCamType);
        progress_finding_location = (ProgressBar) findViewById(R.id.progress_finding_location);


        if (Utiilties.isfrontCameraAvalable() && getIntent().getStringExtra("KEY_PIC").equals("2")) {
            camType = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            camType = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        preview = (FrameLayout) findViewById(R.id.camera_preview);


        //takePhoto.setEnabled(false);


        btnCamType.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mCamera != null) {
                    mCamera.stopPreview();


                }


                if (camType == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camType = Camera.CameraInfo.CAMERA_FACING_FRONT;

                } else {
                    camType = Camera.CameraInfo.CAMERA_FACING_BACK;

                }

                preview.removeAllViews();

                initializeCamera(camType);


            }

        });

    }
    private Bitmap getDownsampledBitmap(Context ctx, Uri uri, int targetWidth, int targetHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options outDimens = getBitmapDimensions(uri);

            int sampleSize = calculateSampleSize(outDimens.outWidth, outDimens.outHeight, targetWidth, targetHeight);

            bitmap = downsampleBitmap(uri, sampleSize);

        } catch (Exception e) {

            e.printStackTrace();
            // Log.d("TAGEXCEPTION",e.printStackTrace());
            //handle the exception(s)
        }

        return bitmap;
    }

    private BitmapFactory.Options getBitmapDimensions(Uri uri) throws FileNotFoundException, IOException {
        BitmapFactory.Options outDimens = new BitmapFactory.Options();
        outDimens.inJustDecodeBounds = true; // the decoder will return null (no bitmap)

        InputStream is= getContentResolver().openInputStream(uri);
        // if Options requested only the size will be returned
        BitmapFactory.decodeStream(is, null, outDimens);
        is.close();

        return outDimens;
    }

    private int calculateSampleSize(int width, int height, int targetWidth, int targetHeight) {
        int inSampleSize = 1;

        if (height > targetHeight || width > targetWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) targetHeight);
            final int widthRatio = Math.round((float) width / (float) targetWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private Bitmap downsampleBitmap(Uri uri, int sampleSize) throws FileNotFoundException, IOException {
        Bitmap resizedBitmap;
        BitmapFactory.Options outBitmap = new BitmapFactory.Options();
        outBitmap.inJustDecodeBounds = false; // the decoder will return a bitmap
        outBitmap.inSampleSize = sampleSize;

        InputStream is = getContentResolver().openInputStream(uri);
        resizedBitmap = BitmapFactory.decodeStream(is, null, outBitmap);
        is.close();

        return resizedBitmap;
    }
    public void onCaptureClick(View view) {
        // System.gc();

        if (mCamera != null)
            mCamera.takePicture(shutterCallback, rawCallback, mPicture);

        Log.e("pic taken", "Yes");

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
        // mCamera.takePicture(null, null, mPicture);

    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {

                Log.e("pic callback", "Yes");
                Log.d(TAG, "Start");
                Bitmap bmp = BitmapFactory
                        .decodeByteArray(data, 0, data.length);

                Matrix mat = new Matrix();
                if (camType == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    mat.postRotate(-90);

                } else mat.postRotate(90);







                Bitmap bMapRotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                //changing
                Bitmap bmapBitmap2 = bMapRotate;
                Date d = new Date(GlobalVariables.glocation.getTime());
                String dat = d.toLocaleString();
                //String dat =Utiilties.getCurrentDate();
                //String dat =Utiilties.getCurrentDateWithTime();
                Bitmap bitmap2 = Utiilties.DrawText(CameraActivity.this, bmapBitmap2, "Lat:" + Double.toString(GlobalVariables.glocation.getLatitude()), "Long:" + Double.toString(GlobalVariables.glocation.getLongitude())
                        , "Accurecy:" + Float.toString(GlobalVariables.glocation.getAccuracy()), "GpsTime : " + ServerDate);
                latitude=GlobalVariables.glocation.getLatitude();
                longitude=GlobalVariables.glocation.getLongitude();
                SaveImage(bitmap2);

                imageFilePath = file.getAbsolutePath();
                // save(data);
               // Bitmap bitmapnew= getDownsampledBitmap(getApplicationContext(), Uri.fromFile(new File(imageFilePath)),500,500);
                Bitmap bitmapnew= getDownsampledBitmap(getApplicationContext(), Uri.fromFile(new File(imageFilePath)),380,380);
                Log.d("dgfhghghghghdsgh",imageFilePath+bitmapnew);

                setCameraImage(bitmapnew);

                // new CustomeDialogClass(CameraActivity.this,bmapBitmap2,Integer.parseInt(getIntent().getStringExtra("KEY_PIC"))).show();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    };
    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
          //  finalBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }
    private  void setCameraImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);


        byte[] byte_arr = stream.toByteArray();
        CompressedImageByteArray = byte_arr;

        bitmap.recycle();


        Intent returnIntent = new Intent();
        returnIntent.putExtra("CapturedImage", CompressedImageByteArray);
        returnIntent.putExtra("Lat", new DecimalFormat("#.0000000")
                .format(GlobalVariables.glocation.getLatitude()));
        returnIntent.putExtra("Lng", new DecimalFormat("#.0000000")
                .format(GlobalVariables.glocation.getLongitude()));

        returnIntent.putExtra("CapturedTime", Utiilties.getDateString());

        Date d = new Date(GlobalVariables.glocation.getTime());
        String dat = d.toLocaleString();
        returnIntent.putExtra("GPSTime", Utiilties.getDateString());
        returnIntent.putExtra("KEY_PIC",
                Integer.parseInt(getIntent().getStringExtra("KEY_PIC")));
        // returnIntent.putExtra("ss", 0);
        setResult(RESULT_OK, returnIntent);
        Log.e("Set camera image", "Yes");

        finish();

    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    private void initializeCamera(int camType) {

        init = true;
        chronometer = (Chronometer) findViewById(R.id.chronometer1);
        isTimerStarted = false;

        mCamera = getCameraInstance(camType);
        Camera.Parameters param;
        if (mCamera != null) {
            param = mCamera.getParameters();


            List<Camera.Size> sizes = param.getSupportedPictureSizes();
            int iTarget = 0;
            for (int i = 0; i < sizes.size(); i++) {
                Camera.Size size = sizes.get(i);
			/*if (size.width < 1000) {
				iTarget = i;
				break;
			}*/


                if (size.width >= 1024 && size.width <= 1280) {
                    iTarget = i;
                    break;
                } else {
                    if (size.width < 1024) {
                        iTarget = i;

                    }
                }

            }
            param.setJpegQuality(100);
            param.setPictureSize(sizes.get(iTarget).width,
                    sizes.get(iTarget).height);
            if (param.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(param);
            alert = new AlertDialog.Builder(this);
            Display getOrient = getWindowManager().getDefaultDisplay();

            int rotation = getOrient.getRotation();

            switch (rotation) {
                case Surface.ROTATION_0:
                    mCamera.setDisplayOrientation(90);
                    break;
                case Surface.ROTATION_90:

                    break;
                case Surface.ROTATION_180:
                    mCamera.setDisplayOrientation(0);

                    break;
                case Surface.ROTATION_270:
                    mCamera.setDisplayOrientation(90);
                    break;
                default:
                    break;
            }


            try {

                mPreview = new CameraPreview(this, mCamera);
                preview.addView(mPreview);


            } catch (Exception e) {
                finish();
            }
            locationManager();
        }

    }

    public static Camera getCameraInstance(int cameraType) {
        // Camera c = null;
        try {

            int numberOfCameras = Camera.getNumberOfCameras();
            int cameraId = 0;
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == cameraType) {
                    // Log.d(DEBUG_TAG, "Camera found");
                    cameraId = i;
                    break;

                }
            }

            return Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            return null;
        }
    }


    private void locationManager() {
        if(GlobalVariables.glocation==null){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            takePhoto.setEnabled(false);
            mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, (float) 0.01, mlistener);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, (float) 0.01, mlistener);
        }
        else {
            takePhoto.setEnabled(true);
            progress_finding_location.setVisibility(View.GONE);
            takePhoto.setText(" Take Photo ");
        }
    }

    private void updateUILocation(Location location) {

        Message.obtain(
                mHandler,
                UPDATE_LATLNG,
                new DecimalFormat("#.0000000").format(location.getLatitude())
                        + "-"
                        + new DecimalFormat("#.0000000").format(location
                        .getLongitude()) + "-" + location.getAccuracy() + "-" + location.getTime())
                .sendToTarget();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case UPDATE_ADDRESS:
                case UPDATE_LATLNG:
                    String[] LatLon = ((String) msg.obj).split("-");
                    TextView tv_Lat = (TextView) findViewById(R.id.tvLat);
                    TextView tv_Lon = (TextView) findViewById(R.id.tvLon);

                    tv_Lat.setText("" + latitude);
                    tv_Lon.setText("" + longitude);
                    Log.e("", "Lat-Long" + LatLon[0] + "   " + LatLon[1]);

                    if (!isTimerStarted) {
                        startTimer();
                    }

                    break;
            }
        }
    };
    public void startTimer() {

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        isTimerStarted = true;

    }


    private final LocationListener mlistener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            //A new location update is received. Do something useful with it.
            //Update the UI with
            //the location update.
            if (Utiilties.isGPSEnabled(CameraActivity.this)) {
                LastLocation = location;
                GlobalVariables.glocation = location;
                updateUILocation(GlobalVariables.glocation);
                //  if (getIntent().getStringExtra("KEY_PIC").equals("2")) {
                if (location.getLatitude() > 0.0) {
                    //long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                    if (location.getAccuracy() > 0 && location.getAccuracy() < 150) {

                        takePhoto.setText(" Take Photo ");
                        progress_finding_location.setVisibility(View.GONE);
                        takePhoto.setEnabled(true);
                    } else {

                        takePhoto.setText(" Wait for GPS to Stable ");
                        progress_finding_location.setVisibility(View.VISIBLE);
                        takePhoto.setEnabled(false);

                    }

                }

//                } else {
//                    GlobalVariables.glocation.setLatitude(0.0);
//                    GlobalVariables.glocation.setLongitude(0.0);
//                    GlobalVariables.glocation.setTime(0);
//                    updateUILocation(GlobalVariables.glocation);
//                    takePhoto.setText(" Take Photo ");
//                    progress_finding_location.setVisibility(View.GONE);
//                    takePhoto.setEnabled(true);
//                }
            } else {
                Message.obtain(
                        mHandler,
                        UPDATE_LATLNG,
                        new DecimalFormat("#.0000000").format(location.getLatitude())
                                + "-"
                                + new DecimalFormat("#.0000000").format(location
                                .getLongitude()) + "-" + location.getAccuracy() + "-" + location.getTime())
                        .sendToTarget();
                takePhoto.setText(" Take Photo ");
                progress_finding_location.setVisibility(View.GONE);
            }
            //Toast.makeText(getApplicationContext(), latitude + " WORKS offline " + longitude + "", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };
    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }


}
