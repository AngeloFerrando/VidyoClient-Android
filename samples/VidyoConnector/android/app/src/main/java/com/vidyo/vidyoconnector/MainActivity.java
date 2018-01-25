package com.vidyo.vidyoconnector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.vidyo.VidyoClient.Connector.ConnectorPkg;
import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Device.Device;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Endpoint.LogRecord;
import com.vidyo.VidyoClient.NetworkInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.unige.dibris.utils.CameraActivity;
import it.unige.dibris.utils.Custom_CameraActivity;

public class MainActivity extends Activity implements
        Connector.IRegisterLocalCameraEventListener,
        Connector.IConnect,
        Connector.IRegisterLogEventListener,
        Connector.IRegisterNetworkInterfaceEventListener,
        IVideoFrameListener {

    private static final int PICTURE_TAKEN = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private TakePhotoUtil photoUtil = null;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private TextView mLocationTextView = null;
    private AudioRecorder audioRecorder = null;

    enum VidyoConnectorState {
        VidyoConnectorStateConnected,
        VidyoConnectorStateDisconnected,
        VidyoConnectorStateDisconnectedUnexpected,
        VidyoConnectorStateFailure
    }

    private VidyoConnectorState mVidyoConnectorState = VidyoConnectorState.VidyoConnectorStateDisconnected;
    private boolean mVidyoClientInitialized = false;
    private Logger mLogger = Logger.getInstance();
    private Connector mVidyoConnector = null;
    private ToggleButton mToggleConnectButton;
    private ProgressBar mConnectionSpinner;
    private LinearLayout mControlsLayout;
    private LinearLayout mToolbarLayout;
    private EditText mHost;
    private EditText mDisplayName;
    private EditText mToken;
    private EditText mResourceId;
    private TextView mToolbarStatus;
    private TextView mClientVersion;
    private VideoFrameLayout mVideoFrame;
    private boolean mHideConfig = false;
    private boolean mAutoJoin = false;
    private boolean mAllowReconnect = true;
    private boolean mCameraPrivacy = false;
    private boolean mMicrophonePrivacy = false;
    private boolean mEnableDebug = false;
    private String mReturnURL = null;
    private String mExperimentalOptions = null;
    private MainActivity mSelf;
    private boolean mRefreshSettings = true;
    private boolean front = true;

    /*
     *  Operating System Events
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLogger.Log("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize the member variables
        mToggleConnectButton = (ToggleButton) findViewById(R.id.toggleConnectButton);
        mControlsLayout = (LinearLayout) findViewById(R.id.controlsLayout);
        mToolbarLayout = (LinearLayout) findViewById(R.id.toolbarLayout);
        mVideoFrame = (VideoFrameLayout) findViewById(R.id.videoFrame);
        mVideoFrame.Register(this);
        mHost = (EditText) findViewById(R.id.hostTextBox);
        mDisplayName = (EditText) findViewById(R.id.displayNameTextBox);
        mToken = (EditText) findViewById(R.id.tokenTextBox);
        mResourceId = (EditText) findViewById(R.id.resourceIdTextBox);
        mToolbarStatus = (TextView) findViewById(R.id.toolbarStatusText);
        mClientVersion = (TextView) findViewById(R.id.clientVersion);
        mConnectionSpinner = (ProgressBar) findViewById(R.id.connectionSpinner);
        mSelf = this;
        mLocationTextView = (TextView) findViewById(R.id.location_text_view);

        // Suppress keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initialize the VidyoClient
        ConnectorPkg.setApplicationUIContext(this);
        mVidyoClientInitialized = ConnectorPkg.initialize();

        photoUtil = new TakePhotoUtil(this, Camera.CameraInfo.CAMERA_FACING_BACK);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    /*Toast.makeText(MainActivity.this,
                            "[knownName] " + knownName + ", " +
                                    "[postalCode]" + postalCode + ", " +
                                    "[country]" + country + ", " +
                                    "[state]" + state + ", " +
                                    "[city]" + city + ", " +
                                    "[address]" + address
                            , Toast.LENGTH_LONG).show();*/
                    mLocationTextView.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        } else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mLogger.Log("onNewIntent");
        super.onNewIntent(intent);

        // Set the refreshSettings flag so the app settings are refreshed in onStart
        mRefreshSettings = true;

        // New intent was received so set it to use in onStart
        setIntent(intent);
    }

    @Override
    public void onLocalCameraAdded(LocalCamera localCamera) {
        //localCamera.setMaxConstraint(1024, 720, 1000);

    }

    @Override
    public void onLocalCameraRemoved(LocalCamera localCamera) {
        int pippo = 0;
    }

    @Override
    public void onLocalCameraSelected(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraStateUpdated(LocalCamera localCamera, Device.DeviceState deviceState) {

    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/camtest/" + now + ".jpg";

            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            View v1 = findViewById(R.id.rel_layout);
            v1.setDrawingCacheEnabled(true);

            Bitmap bitmap = Bitmap.createBitmap(v1.getWidth(),
                    v1.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v1.draw(canvas);


           // Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        mLogger.Log("onStart");
        super.onStart();

        // Initialize or refresh the app settings.
        // When app is first launched, mRefreshSettings will always be true.
        // Each successive time that onStart is called, app is coming back to foreground so check if the
        // settings need to be refreshed again, as app may have been launched via URI.
        if (mRefreshSettings && (mVidyoConnectorState != VidyoConnectorState.VidyoConnectorStateConnected)) {

            Intent intent = getIntent();
            Uri uri = intent.getData();

            // Check if app was launched via URI
            if (uri != null) {
                String param = uri.getQueryParameter("host");
                mHost.setText( param != null ? param : "prod.vidyo.io");

                param = uri.getQueryParameter("token");
                mToken.setText(param != null ? param : "");

                param = uri.getQueryParameter("displayName");
                mDisplayName.setText(param != null ? param : "");

                param = uri.getQueryParameter("resourceId");
                mResourceId.setText(param != null ? param : "");

                mReturnURL = uri.getQueryParameter("returnURL");
                mHideConfig = uri.getBooleanQueryParameter("hideConfig", false);
                mAutoJoin = uri.getBooleanQueryParameter("autoJoin", false);
                mAllowReconnect = uri.getBooleanQueryParameter("allowReconnect", true);
                mCameraPrivacy = uri.getBooleanQueryParameter("cameraPrivacy", false);
                mMicrophonePrivacy = uri.getBooleanQueryParameter("microphonePrivacy", false);
                mEnableDebug = uri.getBooleanQueryParameter("enableDebug", false);
                mExperimentalOptions = uri.getQueryParameter("experimentalOptions");
            } else {
                // If the app was launched by a different app, then get any parameters; otherwise use default settings
                mHost.setText(intent.hasExtra("host") ? intent.getStringExtra("host") : "prod.vidyo.io");
                mToken.setText(intent.hasExtra("token") ? intent.getStringExtra("token") : "");
                mDisplayName.setText(intent.hasExtra("displayName") ? intent.getStringExtra("displayName") : "");
                mResourceId.setText(intent.hasExtra("resourceId") ? intent.getStringExtra("resourceId") : "");
                mReturnURL = intent.hasExtra("returnURL") ? intent.getStringExtra("returnURL") : null;
                mHideConfig = intent.getBooleanExtra("hideConfig", false);
                mAutoJoin = intent.getBooleanExtra("autoJoin", false);
                mAllowReconnect = intent.getBooleanExtra("allowReconnect", true);
                mCameraPrivacy = intent.getBooleanExtra("cameraPrivacy", false);
                mMicrophonePrivacy = intent.getBooleanExtra("microphonePrivacy", false);
                mEnableDebug = intent.getBooleanExtra("enableDebug", false);
                mExperimentalOptions = intent.hasExtra("experimentalOptions") ? intent.getStringExtra("experimentalOptions") : null;
            }

            mLogger.Log("onStart: hideConfig = " + mHideConfig + ", autoJoin = " + mAutoJoin + ", allowReconnect = " + mAllowReconnect + ", enableDebug = " + mEnableDebug);

            // Hide the controls if hideConfig enabled
            if (mHideConfig) {
                mControlsLayout.setVisibility(View.GONE);
            }

            // Apply the app settings if the Connector object has been created
            if (mVidyoConnector != null) {
                mSelf.applySettings();
            }
        }

        mRefreshSettings = false;

        // Enable toggle connect button
        mToggleConnectButton.setEnabled(true);
    }

    @Override
    protected void onResume() {
        mLogger.Log("onResume");
        super.onResume();

        ViewTreeObserver viewTreeObserver = mVideoFrame.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mVideoFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // If the vidyo connector was not previously successfully constructed then construct it

                    if (mVidyoConnector == null) {

                        if (mVidyoClientInitialized) {

                            try {
                                mVidyoConnector = new Connector(mVideoFrame,
                                        Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default,
                                        15,
                                        "info@VidyoClient info@VidyoConnector warning",
                                        "",
                                        0);

                                // Set the client version in the toolbar
                                mClientVersion.setText("VidyoClient-AndroidSDK " + mVidyoConnector.getVersion());

                                // Set initial position
                                refreshUI();

                                // Register for network interface callbacks
                                if (!mVidyoConnector.registerNetworkInterfaceEventListener(mSelf)) {
                                    mLogger.Log("VidyoConnector RegisterNetworkInterfaceEventListener failed");
                                }

                                // Register for log callbacks
                                if (!mVidyoConnector.registerLogEventListener(mSelf, "info@VidyoClient info@VidyoConnector warning")) {
                                    mLogger.Log("VidyoConnector RegisterLogEventListener failed");
                                }

                                if(!mVidyoConnector.registerLocalCameraEventListener(mSelf)){
                                    mLogger.Log("VidyoConnector RegisterLocalCameraEventListener failed");
                                }

                                // Apply the app settings
                                mSelf.applySettings();
                            }
                            catch (Exception e) {
                                mLogger.Log("VidyoConnector Construction failed");
                                mLogger.Log(e.getMessage());
                            }
                        } else {
                            mLogger.Log("ERROR: VidyoClientInitialize failed - not constructing VidyoConnector ...");
                        }

                        Logger.getInstance().Log("onResume: mVidyoConnectorConstructed => " + (mVidyoConnector != null ? "success" : "failed"));
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        mLogger.Log("onPause");
        /*if (mVidyoConnector != null) {
            mVidyoConnector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Background);
        }*/
        super.onPause();
    }

    @Override
    protected void onRestart() {
        mLogger.Log("onRestart");
        super.onRestart();
        if (mVidyoConnector != null) {
            mVidyoConnector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Foreground);
        }
    }

    @Override
    protected void onStop() {
        mLogger.Log("onStop");
        if (mVidyoConnector != null) {
            mVidyoConnector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Background);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLogger.Log("onDestroy");

        // Release device resources
        mVidyoConnector.disable();
        mVidyoConnector = null;

        // Uninitialize the VidyoClient library
        ConnectorPkg.uninitialize();

        super.onDestroy();
    }

    // The device interface orientation has changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mLogger.Log("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);

        // Refresh the video size after it is painted
        ViewTreeObserver viewTreeObserver = mVideoFrame.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mVideoFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Width/height values of views not updated at this point so need to wait
                    // before refreshing UI

                    refreshUI();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Private Utility Functions
     */

    // Apply some of the app settings
    private void
    applySettings() {
        // If enableDebug is configured then enable debugging
        if (mEnableDebug) {
            mVidyoConnector.enableDebug(7776, "warning info@VidyoClient info@VidyoConnector");
            mClientVersion.setVisibility(View.VISIBLE);
        } else {
            mVidyoConnector.disableDebug();
        }

        // If cameraPrivacy is configured then mute the camera
        ToggleButton cp = (ToggleButton)findViewById(R.id.cameraPrivacyButton);
        cp.setChecked(false); // reset state
        if (mCameraPrivacy) {
            cp.performClick();
        }

        // If microphonePrivacy is configured then mute the microphone
        ToggleButton mp = (ToggleButton)findViewById(R.id.microphonePrivacyButton);
        mp.setChecked(false); // reset state
        if (mMicrophonePrivacy) {
            mp.performClick();
        }

        // Set experimental options if any exist
        if (mExperimentalOptions != null) {
            ConnectorPkg.setExperimentalOptions(mExperimentalOptions);
        }

        // If configured to auto-join, then simulate a click of the toggle connect button
        if (mAutoJoin) {
            mToggleConnectButton.performClick();
        }
    }

    // Refresh the UI
    private void refreshUI() {
        // Refresh the rendering of the video
        mVidyoConnector.showViewAt(mVideoFrame, 0, 0, mVideoFrame.getWidth(), mVideoFrame.getHeight());
        mLogger.Log("VidyoConnectorShowViewAt: x = 0, y = 0, w = " + mVideoFrame.getWidth() + ", h = " + mVideoFrame.getHeight());
    }

    // The state of the VidyoConnector connection changed, reconfigure the UI.
    // If connected, dismiss the controls layout
    private void connectorStateUpdated(VidyoConnectorState state, final String statusText) {
        mLogger.Log("connectorStateUpdated, state = " + state.toString());

        mVidyoConnectorState = state;

        // Execute this code on the main thread since it is updating the UI layout

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Update the toggle connect button to either start call or end call image
                mToggleConnectButton.setChecked(mVidyoConnectorState == VidyoConnectorState.VidyoConnectorStateConnected);

                // Set the status text in the toolbar
                mToolbarStatus.setText(statusText);

                if (mVidyoConnectorState == VidyoConnectorState.VidyoConnectorStateConnected) {
                    if (!mHideConfig) {
                        // Update the view to hide the controls
                        mControlsLayout.setVisibility(View.GONE);
                    }
                } else {
                    // VidyoConnector is disconnected

                    // Display toolbar in case it is hidden
                    mToolbarLayout.setVisibility(View.VISIBLE);

                    // If a return URL was provided as an input parameter, then return to that application
                    if (mReturnURL != null) {
                        // Provide a callstate of either 0 or 1, depending on whether the call was successful
                        Intent returnApp = getPackageManager().getLaunchIntentForPackage(mReturnURL);
                        returnApp.putExtra("callstate", (mVidyoConnectorState == VidyoConnectorState.VidyoConnectorStateDisconnected) ? 1 : 0);
                        startActivity(returnApp);
                    }

                    // If the allow-reconnect flag is set to false and a normal (non-failure) disconnect occurred,
                    // then disable the toggle connect button, in order to prevent reconnection.
                    if (!mAllowReconnect && (mVidyoConnectorState == VidyoConnectorState.VidyoConnectorStateDisconnected)) {
                        mToggleConnectButton.setEnabled(false);
                        mToolbarStatus.setText("Call ended");
                    }

                    if (!mHideConfig ) {
                        // Update the view to display the controls
                        mControlsLayout.setVisibility(View.VISIBLE);
                    }
                }

                // Hide the spinner animation
                mConnectionSpinner.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*
     * Button Event Callbacks
     */

    // The Connect button was pressed.
    // If not in a call, attempt to connect to the backend service.
    // If in a call, disconnect.
    public void toggleConnectButtonPressed(View v) {
        if (mToggleConnectButton.isChecked()) {
            // Abort the Connect call if resourceId is invalid. It cannot contain empty spaces or "@".
            if (mResourceId.getText().toString().contains(" ") || mResourceId.getText().toString().contains("@")) {
                mToolbarStatus.setText("Invalid Resource ID");
                mToggleConnectButton.setChecked(false);
            } else {
                mToolbarStatus.setText("Connecting...");

                // Display the spinner animation
                mConnectionSpinner.setVisibility(View.VISIBLE);

                mToken.setText("cHJvdmlzaW9uAGNpY2NpbzFANzkyNTNkLnZpZHlvLmlvADYzNjg0OTE5ODE1AABiYjk3NDVjZGFjYWFiNTY1NjA4MGYzOWIyZjdmNDQ2YmNiNmFjNWI2YzcwNDhlYWFlMDJjNWI4YjhjOWJkZDMxZDg1N2I3MDY3ZDExZDUyMDExODAxNWYyODAwMzEyZGE=");

                final boolean status = mVidyoConnector.connect(
                        mHost.getText().toString(),
                        mToken.getText().toString(),
                        mDisplayName.getText().toString(),
                        mResourceId.getText().toString(),
                        this);
                if (!status) {
                    // Hide the spinner animation
                    mConnectionSpinner.setVisibility(View.INVISIBLE);

                    connectorStateUpdated(VidyoConnectorState.VidyoConnectorStateFailure, "Connection failed");
                }
                mLogger.Log("VidyoConnectorConnect status = " + status);
            }
        } else {
            // The button just switched to the callStart image: The user is either connected to a resource
            // or is in the process of connecting to a resource; call VidyoConnectorDisconnect to either disconnect
            // or abort the connection attempt.
            // Change the button back to the callEnd image because do not want to assume that the Disconnect
            // call will actually end the call. Need to wait for the callback to be received
            // before swapping to the callStart image.
            mToggleConnectButton.setChecked(true);

            mToolbarStatus.setText("Disconnecting...");

            mVidyoConnector.disconnect();
        }
    }

    // Toggle the microphone privacy
    public void microphonePrivacyButtonPressed(View v) {
        mVidyoConnector.setMicrophonePrivacy(((ToggleButton) v).isChecked());

        /*mVidyoConnector.selectLocalCamera(null);


        Intent intent = new Intent(this, MakePhotoActivity.class);
        startActivityForResult(intent, PICTURE_TAKEN);*/


        //mVidyoConnector.setCameraPrivacy(true);
        //cameraPrivacyButtonPressed(v);
//        photoUtil.takePicture();

       // mVidyoConnector.setCameraPrivacy(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICTURE_TAKEN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mVidyoConnector.selectDefaultCamera();
            }
        }
    }

    // Toggle the camera privacy
    public void cameraPrivacyButtonPressed(View v) {
        mVidyoConnector.setCameraPrivacy(((ToggleButton) v).isChecked());

        mVidyoConnector.selectLocalMicrophone(null);
        mVidyoConnector.selectLocalCamera(null);
        startActivity(new Intent(this, CameraActivity.class));
        //startActivity(new Intent(this, Custom_CameraActivity.class));

        //to register audio
        /*mVidyoConnector.selectLocalMicrophone(null);
        if(audioRecorder == null){
            audioRecorder = new AudioRecorder();
            try {
                audioRecorder.startRecording("test.wav");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            try {
                audioRecorder.stop();
                audioRecorder = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    // Handle the camera swap button being pressed. Cycle the camera.
    public void cameraSwapButtonPressed(View v) {
        this.front = !this.front;
        mVidyoConnector.cycleCamera();
    }

    // Toggle debugging
    public void toggleDebugButtonPressed(View v) {
        mEnableDebug = !mEnableDebug;
        if (mEnableDebug) {
            mVidyoConnector.enableDebug(7776, "warning info@VidyoClient info@VidyoConnector");
            mClientVersion.setVisibility(View.VISIBLE);
        } else {
            mVidyoConnector.disableDebug();
            mClientVersion.setVisibility(View.INVISIBLE);
        }
    }

    // Toggle visibility of the toolbar
    @Override
    public void onVideoFrameClicked() {
        if (mVidyoConnectorState == VidyoConnectorState.VidyoConnectorStateConnected) {
            if (mToolbarLayout.getVisibility() == View.VISIBLE) {
                mToolbarLayout.setVisibility(View.INVISIBLE);
            } else {
                mToolbarLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /*
     *  Connector Events
     */

    // Handle successful connection.
    @Override
    public void onSuccess() {
        mLogger.Log("onSuccess: successfully connected.");
        connectorStateUpdated(VidyoConnectorState.VidyoConnectorStateConnected, "Connected");
    }

    // Handle attempted connection failure.
    @Override
    public void onFailure(Connector.ConnectorFailReason reason) {
        mLogger.Log("onFailure: connection attempt failed, reason = " + reason.toString());

        // Update UI to reflect connection failed
        connectorStateUpdated(VidyoConnectorState.VidyoConnectorStateFailure, "Connection failed");
    }

    // Handle an existing session being disconnected.
    @Override
    public void onDisconnected(Connector.ConnectorDisconnectReason reason) {
        if (reason == Connector.ConnectorDisconnectReason.VIDYO_CONNECTORDISCONNECTREASON_Disconnected) {
            mLogger.Log("onDisconnected: successfully disconnected, reason = " + reason.toString());
            connectorStateUpdated(VidyoConnectorState.VidyoConnectorStateDisconnected, "Disconnected");
        } else {
            mLogger.Log("onDisconnected: unexpected disconnection, reason = " + reason.toString());
            connectorStateUpdated(VidyoConnectorState.VidyoConnectorStateDisconnectedUnexpected, "Unexpected disconnection");
        }
    }

    // Handle a message being logged.
    @Override
    public void onLog(LogRecord logRecord) {
        mLogger.LogClientLib(logRecord.name + " : " + logRecord.level + " : " + logRecord.functionName + " : " + logRecord.message);
    }

    @Override
    public void onNetworkInterfaceAdded(NetworkInterface vidyoNetworkInterface) {
        mLogger.Log("onNetworkInterfaceAdded: name=" + vidyoNetworkInterface.getName() + " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() + " family=" + vidyoNetworkInterface.getFamily());
    }

    @Override
    public void onNetworkInterfaceRemoved(NetworkInterface vidyoNetworkInterface) {
        mLogger.Log("onNetworkInterfaceRemoved: name=" + vidyoNetworkInterface.getName() + " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() + " family=" + vidyoNetworkInterface.getFamily());
    }

    @Override
    public void onNetworkInterfaceSelected(NetworkInterface vidyoNetworkInterface, NetworkInterface.NetworkInterfaceTransportType vidyoNetworkInterfaceTransportType) {
        mLogger.Log("onNetworkInterfaceSelected: name=" + vidyoNetworkInterface.getName() + " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() + " family=" + vidyoNetworkInterface.getFamily());
    }

    @Override
    public void onNetworkInterfaceStateUpdated(NetworkInterface vidyoNetworkInterface, NetworkInterface.NetworkInterfaceState vidyoNetworkInterfaceState) {
        mLogger.Log("onNetworkInterfaceStateUpdated: name=" + vidyoNetworkInterface.getName() + " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() + " family=" + vidyoNetworkInterface.getFamily() + " state=" + vidyoNetworkInterfaceState);
    }
}
