package biker.life;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//20210118 S--------------------------------------------
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Address;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.ListView;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

//20210118 O--------------------------------------------

public class Biker_time_map extends AppCompatActivity
        implements OnMapReadyCallback, View.OnClickListener {
    //google map??????FragmentActivity???OnMapReadyCallback

    //plan------------------------------------------------------------------------
    private TextView time_map_check2, backbtn;
    private TextView distanceBtn;
    private AutoCompleteTextView location_start, location_goal;
    private ArrayAdapter<String> autoText;
    private TextView output;
    private Geocoder geocoder;
    private Button transfer;
    private TextView lat;
    private TextView lon;
    private TextView lat_goal;
    private TextView lon_goal;
    private double latitude, longitude, latitude_goal, longitude_goal;
    private Button map_btn;
    //private static final String DB_File = "bikermap.db", DB_TABLE = "E100";
    private static final String DB_FILE = "bikerlife.db", DB_TABLE = "E100";
    private SQLiteDatabase mBikermapDb;
    private TextView output_history;
    private ListView hList;
    private String[] hListArr;
    private ScrollView time_map_history_scroll;
    private Dialog mLoginDlg;

    private int DBversion;
    private FriendDbHelper22 dbHper;
    private ArrayList<String> recSet;

    //plan------------------------------------------------------------------------

    private static final int REQUEST_CODE = 1;
    private Button plan;
    private Intent intent = new Intent();
    private Intent intent01 = new Intent();
    private TextView watch, time_map_pathfinding;
    private Button selectBtn;
    private ImageView main_map;
    private ImageView main_map2;
    private EditText time_map_start;
    private EditText time_map_goal;
    private TextView img_friend1, img_friend2, img_friend3;
    private SeekBar mSeekBar;
    private GoogleMap mMap;
    private Menu menu;

    private static final String TAG = Biker_time_map.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;

    // ?????? Places API
    private PlacesClient placesClient;

    // ?????? Fused Location Provider
    private FusedLocationProviderClient fusedLocationProviderClient;

    //?????????????????????????????????????????????????????????(?????????)???zoom
    //private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);  //24.17113123820597, 120.61010273294859
    private final LatLng defaultLocation = new LatLng(24.17113123820597, 120.61010273294859);
    //-------------20210118 add
    private final LatLng userLocationA = new LatLng(24.17004914514876, 120.61018707616434);

    //-------------20210118 add
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // ????????????(??????)?????????????????????
    private Location lastKnownLocation;

    // ??????????????????????????????
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // ????????????????????????
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    private String[][] locations;
    private double dLat;
    private double dLon;
    private int resID;
    private int resID1;
    private BitmapDescriptor image_des;
    private int icosel;
    private LatLng VGPS;
    private String sqlctl;

    private String t_u_id;
    private double t_lat;
    private double t_lon;
    private GoogleSignInAccount account;
    private LocationManager manager;// ?????????????????????LocationManager??????
    private String addressName;
    private String addressName_goal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);

        //????????????????????????????????????????????????????????????
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        //?????????????????????????????????
        setContentView(R.layout.biker_time_map);
        u_checkgps();
        setupViewcomponent();
        DBversion=Integer.parseInt(getString(R.string.SQLite_version));
        //20210118 S--------------------------
        initDB();  //??????SQLite
        //20210118 O--------------------------

        //????????????PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        // ????????????FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public static void enableStrictMode(Context context) {//????????????mysql(or??????????????????)?????????????????????
        StrictMode.setThreadPolicy(//?????????????????????
//                -------------????????????????????????????????????------------------------------(????????????)
                new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()//??????????????????
                        .detectDiskWrites()//??????????????????
                        .detectNetwork()//??????UI???????????????????????????????????????
                        .penaltyLog() //????????????(e.g.)??????????????????????????????????????????               //?????? penaltyDeath(). //?????????????????????????????????Crash?????????????????????
                        .build());
        StrictMode.setVmPolicy(//??????????????????
                new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()//?????? SQLiteCursor ?????? ?????? SQLite???????????????????????????
                        .penaltyLog()//????????????(e.g.SQLite?????????????????????)??????????????????????????????????????????
                        .build());
    }


    private void setupViewcomponent() {
        plan = (Button) findViewById(R.id.time_map_plan);
//        watch = (TextView) findViewById(R.id.time_map_watch);
        time_map_pathfinding = (TextView) findViewById(R.id.time_map_pathfinding);
        main_map = (ImageView) findViewById(R.id.time_map_main_map);
        img_friend1 = (TextView) findViewById(R.id.map_img_fr01);
        img_friend2 = (TextView) findViewById(R.id.map_img_fr02);
        img_friend3 = (TextView) findViewById(R.id.map_img_fr03);
        mSeekBar = (SeekBar) findViewById(R.id.map_seekBar);

        //??????????????????
        plan.setOnClickListener(this);
//        watch.setOnClickListener(this);
        time_map_pathfinding.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarOn);

        selectBtn = (Button) findViewById(R.id.time_map_show);
        selectBtn.setOnClickListener(this);
        //??????
        //main_map.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_alpha_in));
        plan.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_trans_in));

        //20210118 S--------------------------
        time_map_check2 = (TextView) findViewById(R.id.time_map_check2);
        time_map_check2.setText("?????????");
        time_map_check2.setTextColor(getResources().getColor(R.color.white));
        backbtn = (TextView) findViewById(R.id.time_map_watch);
        distanceBtn = (TextView) findViewById(R.id.time_map_show);
//        map_btn = (Button) findViewById(R.id.time_map_back);
        ImageView main_map2 = (ImageView) findViewById(R.id.time_map_main_map2);
        transfer = (Button) findViewById(R.id.time_map_transfer);
        //???????????????????????????
        output = (TextView) findViewById(R.id.time_map_lblOutput);
        output_history = (TextView) findViewById(R.id.time_map_lblOutput2);
        lat = (TextView) findViewById(R.id.time_map_txtLat_start);
        lon = (TextView) findViewById(R.id.time_map_txtLong_start);
        lat_goal = (TextView) findViewById(R.id.time_map_txtLat_goal);
        lon_goal = (TextView) findViewById(R.id.time_map_txtLong_goal);
        // ??????????????????????????????
        location_start = (AutoCompleteTextView) findViewById(R.id.time_map_start);
        location_goal = (AutoCompleteTextView) findViewById(R.id.time_map_goal);
        // ??????Geocoder??????
        geocoder = new Geocoder(this, Locale.TAIWAN);
        //??????????????????
//        hList = (ListView) findViewById(R.id.history_list);
//        time_map_history_scroll = (ScrollView) findViewById(R.id.time_map_history_scroll);
//        hListArr = getResources().getStringArray(R.array.biker_map_arr);
//        ArrayAdapter hlist = new ArrayAdapter<String>(this, R.layout.find_list_item, hListArr);
//        hList.setAdapter(hlist);
//        time_map_history_scroll.setVisibility(View.INVISIBLE);
        //AutoCompleteTextView
        autoText = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        // autoText = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
//        autoText.add("????????????");
//        autoText.add("????????????");
//        autoText.add("????????????");
//        autoText.add("????????????");
//        autoText.add("????????????");
//        autoText.add("????????????");
//        autoText.add("????????????");
        location_start.setAdapter(autoText);
        location_goal.setAdapter(autoText);
        //??????
        transfer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_scale_rotate_in));
        //backbtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_scale_rotate_in));
        //distanceBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_scale_rotate_in));
        //??????????????????
        time_map_check2.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        distanceBtn.setOnClickListener(this);
        location_start.setOnClickListener(this);
        location_goal.setOnClickListener(this);
        transfer.setOnClickListener(this);
//        map_btn.setOnClickListener(this);
        //20210118 O--------------------------

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_map_watch: //?????????????????????
                intent.setClass(Biker_time_map.this, Biker_time_Stopwatch.class);
                setResult(REQUEST_CODE, intent); //REQUEST_CODE ??????AActivity.class?????????
                finish();
                startActivity(intent);
                break;
            //20210118 S--------------------------
            case R.id.time_map_check2: //?????????google map??????
                try {
                    if (output.getText().toString() != "?????????????????????\n???????????????????????????" && output.getText().toString() != "????????????\n???????????????????????????") {
                        double s_lat = Double.parseDouble(lat.getText().toString());
                        double s_lon = Double.parseDouble(lon.getText().toString());
                        double s_lat_goal = Double.parseDouble(lat_goal.getText().toString());
                        double s_lon_goal = Double.parseDouble(lon_goal.getText().toString());
                        String link_address = "http://maps.google.com/maps?f=d&saddr=" + s_lat + ", " + s_lon + "&daddr=" + s_lat_goal + ", " + s_lon_goal + "&hl=tw";
//                    Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=24.17387349666727, 120.60934934548084&daddr=24.17624579441691, 120.6408494529127&hl=tw");
                        Uri uri = Uri.parse(link_address);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    } else {
                        output.setText("????????????\n???????????????????????????");
                        Toast.makeText(getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_LONG).show(); //????????????
                    }
                } catch (Exception ex) {
                    output.setText("????????????\n???????????????????????????");
                    Toast.makeText(getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_LONG).show(); //????????????
                }
                break;
//            case R.id.time_map_watch2: //?????????????????????
//                intent.setClass(Biker_time_map.this, Biker_time_Stopwatch.class);
//                startActivity(intent);
//                break;
//            case R.id.time_map_show: //????????????/????????????
//                if (distanceBtn.isSelected()) {
//                    distanceBtn.setSelected(false);
//                    distanceBtn.setText("?????????");
//                } else {
//                    distanceBtn.setSelected(true);
//                    distanceBtn.setText("????????????");
//                }
//                break;
            case R.id.time_map_start: //?????????
                String s = location_start.getText().toString();
                autoText.add(s);
                break;
            case R.id.time_map_goal: //?????????
                String s2 = location_goal.getText().toString();
                autoText.add(s2);
                break;
            case R.id.time_map_transfer: // ?????????????????????????????????
                addrToLatLon();   // ?????????????????????????????????
                break;
//            case R.id.time_map_back: //??????????????????
//                intent.setClass(Biker_time_map_plan.this, Biker_time_map.class);
//                startActivity(intent);
//                break;

            //20210118 O--------------------------

        }

        //???????????????????????????????????????
        if (v == selectBtn) {
            if (selectBtn.isSelected()) {
                selectBtn.setSelected(false);
                selectBtn.setText(getString(R.string.biker_map_show));
//                img_friend1.setAlpha((float) 0.0);
//                img_friend2.setAlpha((float) 0.0);
//                img_friend3.setAlpha((float) 0.0);
                img_friend1.setVisibility(View.INVISIBLE);
                img_friend2.setVisibility(View.INVISIBLE);
                img_friend3.setVisibility(View.INVISIBLE);
                mSeekBar.setVisibility(View.INVISIBLE);
            } else {
                selectBtn.setSelected(true);
                selectBtn.setText(getString(R.string.biker_map_hide));
//                img_friend1.setAlpha((float) 1.0);
//                img_friend2.setAlpha((float) 1.0);
//                img_friend3.setAlpha((float) 1.0);
                img_friend1.setVisibility(View.VISIBLE);
                img_friend2.setVisibility(View.VISIBLE);
                img_friend3.setVisibility(View.VISIBLE);
                mSeekBar.setVisibility(View.VISIBLE);

            }
        }

    }

    //20210130??????s
    //???????????????MySQL
    private void mysql_insert() {//??????
        //?????????????????????ID
        SharedPreferences u_id = getSharedPreferences("USER_ID", 0);
        t_u_id = u_id.getString("USER_ID", "");

        //???????????????????????????????????????
        t_lat = lastKnownLocation.getLatitude();
        t_lon = lastKnownLocation.getLongitude();

        ArrayList<String> E200_ValuePairs = new ArrayList<>();//?????????????????????????????????
        E200_ValuePairs.add(t_u_id);
        E200_ValuePairs.add(String.valueOf(t_lat)); //double???string???????????????
        E200_ValuePairs.add(String.valueOf(t_lon));
        try {//??????Thread ??????0.5???
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------//???????????????????????????mysql
        String result = DBConnector22.executeInsert(E200_ValuePairs);
//-----------------------------------------------
    }

    //???????????????MySQL(??????????????????)
    private void mysql_insert_history() {//??????
//        dbHper.insertRec(addressName, addressName_goal, longitude, latitude, longitude_goal, latitude_goal);   //????????????????????????20210115
        //?????????????????????ID
        SharedPreferences u_id = getSharedPreferences("USER_ID", 0);
        t_u_id = u_id.getString("USER_ID", "");

        ArrayList<String> E100_ValuePairs = new ArrayList<>();
        E100_ValuePairs.add(t_u_id);
        E100_ValuePairs.add(addressName);
        E100_ValuePairs.add(addressName_goal);
        E100_ValuePairs.add(String.valueOf(longitude));
        E100_ValuePairs.add(String.valueOf(latitude));
        E100_ValuePairs.add(String.valueOf(longitude_goal));
        E100_ValuePairs.add(String.valueOf(latitude_goal));


        try {//??????Thread ??????0.5???
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------//???????????????????????????mysql
        String result_history = DBConnector22.executeInsert_history(E100_ValuePairs);
//-----------------------------------------------
    }

    //??????????????????GPS
    private void u_checkgps() {
        // ?????????????????????LocationManager??????
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // ?????????????????????GPS
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // ????????????????????????GPS
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("????????????")
                    .setCancelable(false)
                    .setIcon(R.drawable.time_stopwatch_gpsicon)
                    .setMessage("GPS???????????????????????????.\n"
                            + "????????????????????????????????????GPS?")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //??????????????????
                            // ??????Intent?????????????????????????????????GPS??????
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "GPS?????????", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }
    //20210130??????o

    //20210118 S--------------------------
// ?????????????????????????????????
    public void addrToLatLon() {
        output_history.setText("");
        addressName = location_start.getText().toString();
        addressName_goal = location_goal.getText().toString();
        try {
            // ??????????????????????????????List??????
            List<Address> listGPSAddress = geocoder.getFromLocationName(addressName, 1);
            List<Address> listGPSAddress_goal = geocoder.getFromLocationName(addressName_goal, 1);
            // ????????????????????????
            if (listGPSAddress != null) {
                latitude = listGPSAddress.get(0).getLatitude();
                longitude = listGPSAddress.get(0).getLongitude();
                latitude_goal = listGPSAddress_goal.get(0).getLatitude();
                longitude_goal = listGPSAddress_goal.get(0).getLongitude();
                output.setText("?????????\n??????: " + latitude +
                        "\n??????: " + longitude + "\n?????????\n??????: " + latitude_goal +
                        "\n??????: " + longitude_goal);
                lat.setText(String.valueOf(latitude)); // ?????????
                lon.setText(String.valueOf(longitude));
                lat_goal.setText(String.valueOf(latitude_goal)); // ?????????
                lon_goal.setText(String.valueOf(longitude_goal));
                //u_dbadd();   //???????????????????????? 20210115
                dbHper.insertRec(addressName, addressName_goal, longitude, latitude, longitude_goal, latitude_goal);   //????????????????????????20210115
                mysql_insert_history(); //??????????????????????????????MySQL
            }
        } catch (Exception ex) {
            output.setText("?????????????????????\n???????????????????????????");
            Toast.makeText(getApplicationContext(), "?????????????????????\n???????????????????????????", Toast.LENGTH_LONG).show(); //????????????
        }
    }

    // ??????Google??????
    public void startGoogleMap() {
        // ?????????????????????
        float latitude = Float.parseFloat(lat.getText().toString());
        float longitude = Float.parseFloat(lon.getText().toString());
        // ??????URI??????
        String uri = String.format("geo:%f,%f?z=18", latitude, longitude);
        // ??????Intent??????
        Intent geoMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(geoMap);  // ????????????
    }

    //---------------------------------
    //??????????????? 20210115???
//    private void u_dbadd() {
//
//        ContentValues newRow = new ContentValues(); //SQLite ????????????
//        //newRow.put("E101", userID);  //UserID
//        //????????????????????? ???key ???value
//        newRow.put("E102", location_start.getText().toString());  //?????????
//        newRow.put("E103", location_goal.getText().toString());  //?????????
//        newRow.put("E104", longitude);  //???????????????
//        newRow.put("E105", latitude);  //???????????????
//        newRow.put("E106", longitude_goal);  //????????????
//        newRow.put("E107", latitude_goal);  //????????????
//
//        mBikermapDb.insert(DB_TABLE, null, newRow);  //??????SQLite
//    }
    //?????????????????????
    private void u_dblist() {
        output_history.setText("");
        //Cursor cur_list = mBikermapDb.query(
        Cursor cur_list = dbHper.getDatabase(getApplicationContext()).query(
                true,
                DB_TABLE,
                new String[]{"id", "E101", "E102", "E103", "E104", "E105", "E106", "E107"},
                null,
                null,
                null,
                null,
                null,
                null);
        // ?????? SQLite ????????? (select) ??????
// ??? SQLiteDatabase ???????????????????????????????????????????????????????????????
// ?????????????????????????????? query ????????? rawQuery ???????????????
// query ???????????????????????????????????????
// ?????????????????????????????? SQLite select ?????????????????????
// ????????????????????????????????????????????????????????????(distinct) true false
// ???????????? (table)
// ???????????? (columns)
// ???????????? (selection)
// ?????????????????? (selectionArgs)
// ???????????? (groupBy)
// ???????????? (orderBy)
// ??????????????????????????? (limit) ??????
// (cancellationSignal)
        if (cur_list == null) return;
        ;
        if (cur_list.getCount() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.biker_map_nodata), Toast.LENGTH_LONG).show();
        } else {
            cur_list.moveToFirst(); //????????????????????????
            output_history.setText(cur_list.getString(0) + " ,"
                    + cur_list.getString(2) + " ,"
                    + cur_list.getString(3));
            while (cur_list.moveToNext()) {
                output_history.append("\n" + cur_list.getString(0) + " ,"
                        + cur_list.getString(2) + " ,"
                        + cur_list.getString(3));
            }
            cur_list.close();
        }
    }
    //---------------------------------
    //20210118 O--------------------------

    public void onValueAnimatorTest(final View view) {
        // ??????????????????
        final int maxWidth = getWindowManager().getDefaultDisplay().getWidth();
        ValueAnimator valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(this, R.animator.value_animator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                // ?????????????????????????????????????????????
                int currentValue = (Integer) animator.getAnimatedValue();
                // ????????????????????????view?????????
                view.getLayoutParams().width = maxWidth * currentValue / 200;
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }

    //??????????????????????????????
    private SeekBar.OnSeekBarChangeListener mSeekBarOn = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            img_friend1.getBackground().setAlpha(progress * 255 / 100);
            img_friend1.setAlpha((float) (progress / 100.0));
            img_friend2.setAlpha((float) (progress / 100.0));
            img_friend3.setAlpha((float) (progress / 100.0));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };


    //    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(24.172127, 120.610313);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));     // ??????????????? 16 ??????
//    }


    // ????????????????????????????????????
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


//    /**
//     * Sets up the options menu.
//     * @param menu The options menu.
//     * @return Boolean.
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.current_place_menu, menu);
//        return true;
//    }
//
//    /**
//     * Handles a click on the menu option to get a place.
//     * @param item The menu item to handle.
//     * @return Boolean.
//     */
//    // [START maps_current_place_on_options_item_selected]
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.option_get_place) {
//            showCurrentPlace();
//        }
//        return true;
//    }
    // [END maps_current_place_on_options_item_selected]

    //?????????????????????????????? ???????????????????????????????????????
    //onMapReady ?????????????????? maps_current_place_on_map_ready
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // [START_EXCLUDE]
        // [START map_current_place_set_info_window_adapter]
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet2);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission();
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }
    // [END maps_current_place_on_map_ready]

    //?????????????????????????????????????????????????????????
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                mysql_insert(); //20210130?????? ???????????????????????????????????????MySQL
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });

            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    // [END maps_current_place_get_device_location]

    //??????????????????
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    //????????????????????????????????? maps_current_place_on_request_permissions_result]
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    //??????????????????????????????-?????????????????????????????????  [START maps_current_place_show_current_place]
    private void showCurrentPlace() {
        if (map == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        Biker_time_map.this.openPlacesDialog();
                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });

            //-----------------------------------------------20210118 add
            map.addMarker(new MarkerOptions()
                    .title("User?????????")
                    .position(userLocationA)
                    .snippet("User?????????"));
            //-----------------------------------------------20210118 add


        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }


    //[START maps_current_place_open_places_dialog]
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }
    // [END maps_current_place_open_places_dialog]

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    // [END maps_current_place_update_location_ui]

    //20210125 S--------------------------
    private void showloc() {
        // ???????????????????????????
        for (int i = 0; i < locations.length; i++) {
            String[] sLocation = locations[i][1].split(",");
            dLat = Double.parseDouble(sLocation[0]); // ?????????
            dLon = Double.parseDouble(sLocation[1]); // ?????????
            String vtitle = locations[i][0];
            resID = 0;
            resID1 = 0;
            // --- ????????????????????????????????? ---
            // raw ????????? ?????? q01.png ~ q06.png  t01.png ~t07.png ???????????? ??? t99.png & q99.png
            if (i >= 0 && i < 8) {
                String idName = "t" + String.format("%02d", i);
                String imgName = "q" + String.format("%02d", i);
                resID = getResources().getIdentifier(idName, "drawable", getPackageName());
                resID1 = getResources().getIdentifier(imgName, "drawable", getPackageName());
                image_des = BitmapDescriptorFactory.fromResource(resID);// ????????????
            } else {
                resID = getResources().getIdentifier("t99", "drawable", getPackageName());
                resID1 = getResources().getIdentifier("q99", "drawable", getPackageName());
            }

            // --- ????????????????????????????????? ---//
            switch (icosel) {
                case 0:
                    image_des = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN); // ??????????????????
                    break;
                case 1:
                    // ????????????
                    image_des = BitmapDescriptorFactory.fromResource(resID);// ????????????
                    break;
            }
            //-----------------------------------------
            vtitle = vtitle + "#" + resID1; //??????????????????
            //-----------------------------------------
            VGPS = new LatLng(dLat, dLon);// ?????????????????????????????????
            // --- ????????????????????????????????????/????????????????????? ---//

            map.addMarker(new MarkerOptions()
                            .position(VGPS)
                            .alpha(0.9f)
                            .title(i + "." + vtitle)
                            .snippet("??????:" + String.valueOf(dLat) + "\n??????:" + String.valueOf(dLon))
                            .infoWindowAnchor(0.5f, 0.95f)
                            .icon(image_des)
                    // .draggable(true) //??????maker ?????????
            );
            //--------------------?????????????????????-------------------------------------------------------
            map.setInfoWindowAdapter(new CustomInfoWindowAdapter());//????????????
        }
    }

    //20210125 O--------------------------

    //20210118 S--------------------------
    private void initDB() {

        if (dbHper == null) {
            dbHper = new FriendDbHelper22(this, DB_FILE, null, DBversion);
        }
        //-----------210128??????
        dbmysql();
        //-----------
        recSet = dbHper.getRecSet(); //all data ??????????????????
    }

    //0129??????S------------
    private void dbmysql() {
        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = DBConnector22.executeQuery(nameValuePairs);
            /**************************************************************************
             * SQL ??????????????????????????????JSONArray
             * ?????????????????????????????????JSONObject?????? JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL ?????????????????????
//--------------------------------------------------------
                int rowsAffected = dbHper.clearRec();                 // ?????????,????????????SQLite??????
//--------------------------------------------------------
                // ??????JASON ????????????????????????
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) ?????????????????? --?????? jsonObject ????????????("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // ??????????????????
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // ???????????????????????????
                        // -------------------------------------------------------------------
                    }
                    // ---(2) ????????????????????????---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------??????SQLite---------------------------------------
                    long rowID = dbHper.insertRec_m(newRow);

                }
                //  Toast.makeText(getApplicationContext(), "????????? " + Integer.toString(jsonArray.length()) + " ?????????", Toast.LENGTH_SHORT).show();
                // ---------------------------
            } else {
                //   Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet();  //????????????SQLite
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }


//        sqlctl = "SELECT * FROM member ORDER BY id ASC";//sql
//        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
//        try {
//            String result = DBConnector.executeQuery(nameValuePairs);
//
//            /**************************************************************************
//             * SQL ??????????????????????????????JSONArray
//             * ?????????????????????????????????JSONObject?????? JSONObject
//             * jsonData = new JSONObject(result);
//             **************************************************************************/
//            JSONArray jsonArray = new JSONArray(result);//?????????????????????
//            // -------------------------------------------------------
//            if (jsonArray.length() > 0) { // MySQL ?????????????????????
//
//                int rowsAffected = dbHper.clearRec();                 // ?????????,????????????SQLite??????
//
//                // ??????JASON ????????????????????????
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonData = jsonArray.getJSONObject(i);
//                    ContentValues newRow = new ContentValues();
//                    // --(1) ?????????????????? --?????? jsonObject ????????????("key","value")-----------------------
//                    Iterator itt = jsonData.keys();
//                    while (itt.hasNext()) {
//                        String key = itt.next().toString();
//                        String value = jsonData.getString(key); // ??????????????????
//                        if (value == null) {
//                            continue;
//                        } else if ("".equals(value.trim())) {
//                            continue;
//                        } else {
//                            jsonData.put(key, value.trim());
//                        }
//                        // ------------------------------------------------------------------
//                        newRow.put(key, value.toString()); // ???????????????????????????
//                        // -------------------------------------------------------------------
//                    }
//                    // ---(2) ????????????????????????---------------------------
//                    // newRow.put("id", jsonData.getString("id").toString());
//                    // newRow.put("name",
//                    // jsonData.getString("name").toString());
//                    // newRow.put("grp", jsonData.getString("grp").toString());
//                    // newRow.put("address", jsonData.getString("address")
//                    // -------------------??????SQLite---------------------------------------
//                    long rowID = dbHper.insertRec_m(newRow);
//                    //??????????????????????????????????????????
//                    //Toast.makeText(getApplicationContext(), "????????? " + Integer.toString(jsonArray.length() ) + " ?????????", Toast.LENGTH_SHORT).show();
//                }
//                //???????????????????????????????????????(??????????????????????????????)
//                Toast.makeText(getApplicationContext(), "????????? " + dbHper.RecCount()  + " ?????????", Toast.LENGTH_SHORT).show();
//
//                // ---------------------------
//            } else {
//                Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
//            }
//            recSet = dbHper.getRecSet();  //????????????SQLite
//            //????????????(???????????????)
//            showRec(0);
//            //
//            u_setspinner(); //??????
//            // --------------------------------------------------------
//        } catch (Exception e) {
//            Log.d(TAG, e.toString());
//        }
    }

    //0129??????O------------
    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHper == null) {
            dbHper = new FriendDbHelper22(this, DB_FILE, null, DBversion);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }


    //20210118 O--------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    ////----------------------------------?????????menu????????????----------------
    private void Signin_menu() {//??????????????????menu
        menu.setGroupVisible(R.id.g01, false);
        menu.setGroupVisible(R.id.g02, true);
    }

    private void Signout_menu() {//??????????????????menu
        menu.setGroupVisible(R.id.g01, true);
        menu.setGroupVisible(R.id.g02, false);
    }

    @Override
    public void onBackPressed() {
//super.onBackPressed();
    }


    @Override//?????????menu???item?????????icon
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.biker_menu, menu);
        this.menu = menu;
        menu.setGroupVisible(R.id.g01, false);
        menu.setGroupVisible(R.id.g02, false);
        //menu.add(Menu.NONE, MyActivity.MENU_ID, 0, "Your Title").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        menu.findItem(R.id.Item07).setTitle("??????");

        //20210118 S--------------------------
//        menu.add(0, 0, 0, "??????").setIcon(android.R.drawable.ic_media_play);
        menu.add(0, 1, 0, "??????").setIcon(android.R.drawable.ic_media_play);
        menu.add(0, 2, 0, "????????????").setIcon(android.R.drawable.ic_media_play);
        menu.add(0, 3, 0, "????????????").setIcon(android.R.drawable.ic_media_play);
        menu.add(0, 4, 0, "???????????????").setIcon(android.R.drawable.ic_media_play);
        menu.add(0, 5, 0, "??????????????????").setIcon(android.R.drawable.ic_media_play);
        menu.add(0, 6, 0, "??????????????????").setIcon(android.R.drawable.ic_media_play);
        //20210118 O--------------------------
        //20210118 ???
//        menu.add(0, 1, 0, "??????").setIcon(android.R.drawable.ic_media_play);
//        menu.add(0, 2, 0, "????????????").setIcon(android.R.drawable.ic_media_play);
        menu.add(0, 7, 0, "??????????????????").setIcon(android.R.drawable.ic_media_play);


        return true;  //???????????????true???,????????????????????????????????????
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //20210118 ???
//            case 1: //?????????????????????
//                watch.callOnClick();
//                break;
//            case 2: //???????????????????????????
//                time_map_pathfinding.callOnClick();
//                break;
//            case 3: //???????????????????????????
//                selectBtn.callOnClick();
//                break;
            //20210118 S--------------------------
            case 1: //?????????????????????
                backbtn.callOnClick();
                break;
            case 2: //?????????google map
                time_map_check2.callOnClick();
                break;
            case 3: //????????????
                u_dblist();
                break;
            case 4: //???????????????
                transfer.callOnClick();
                break;
            case 5: //??????????????????
//                output_history.setText("");
//                mBikermapDb.delete(DB_TABLE, null, null);
//                Toast.makeText(getApplicationContext(), getString(R.string.biker_map_dataClean), Toast.LENGTH_LONG).show();
                // ??????
                //??????dialog-----------------------------
//                MyAlertDialog aldDial = new MyAlertDialog(Biker_time_map_plan.this);
//                aldDial.setTitle(getString(R.string.m_altitle_map));
//                aldDial.setMessage(getString(R.string.m_message_map));
//                aldDial.setIcon(android.R.drawable.ic_dialog_info);
//                aldDial.setCancelable(false); //???????????????
//                aldDial.setButton(BUTTON_POSITIVE, "????????????", aldBtListener);
//                aldDial.setButton(BUTTON_NEGATIVE, "????????????", aldBtListener);
//                aldDial.show();
                //??????dialog-----------------------------
                //??????dialog-----------------------------
                mLoginDlg = new Dialog(Biker_time_map.this);
                //mLoginDlg.setTitle(getString(R.string.biker_map_dialog_title));
                mLoginDlg.setCancelable(false);//?????????????????????true???????????????????????????
//R.layout.profile_dialog??????Dialog?????????
                mLoginDlg.setContentView(R.layout.alert_dialog);
                Button mLoginDlg_btnOK = (Button) mLoginDlg.findViewById(R.id.train_btnOK);
                Button mLoginDlg_btnCancel = (Button) mLoginDlg.findViewById(R.id.train_btnCancel);
                TextView mLoginDlg_warning = (TextView) mLoginDlg.findViewById(R.id.tarin_waring);
                mLoginDlg_warning.setText(getString(R.string.m_message_map));//??????dialog??????
                mLoginDlg_btnCancel.setOnClickListener(mDlgListener);
                mLoginDlg_btnOK.setOnClickListener(mDlgListener);
                mLoginDlg.show();
                //??????dialog-----------------------------
                break;
            case 6: //??????????????????
                output_history.setText("");
                break;

            case 7: //???????????????????????????
                selectBtn.callOnClick();
                break;

//            case 0: //?????????????????????
//                intent.setClass(Biker_time_map.this, Biker_time_map.class);
//                startActivity(intent);
//                break;
            //20210118 O--------------------------
            case R.id.action_settings:  //??????
                Toast.makeText(getApplicationContext(), getString(R.string.action_settings), Toast.LENGTH_LONG).show();
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //20210118 S--------------------------
//??????dialog-----------------------------
    private View.OnClickListener mDlgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.train_btnOK:
                    output_history.setText("");
                    //dbHper.deleteRec(DB_TABLE, null, null);
                    dbHper.deleteRec();
                    Toast.makeText(getApplicationContext(), getString(R.string.biker_map_dataClean), Toast.LENGTH_LONG).show();
                    mLoginDlg.cancel();
                    break;
                case R.id.train_btnCancel:
                    Toast.makeText(getApplicationContext(), getString(R.string.biker_map_dataClean_cancel), Toast.LENGTH_LONG).show();
                    mLoginDlg.cancel();
                    break;
            }
        }
    };
    //??????dialog-----------------------------
//    //??????dialog-----------------------------
//    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {
//
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            switch (which) {
//                case BUTTON_POSITIVE:
//                    output_history.setText("");
//                    mBikermapDb.delete(DB_TABLE, null, null);
//                    Toast.makeText(getApplicationContext(), getString(R.string.biker_map_dataClean), Toast.LENGTH_LONG).show();
//                    break;
//                case BUTTON_NEGATIVE:
//                    Toast.makeText(getApplicationContext(), getString(R.string.biker_map_dataClean_cancel), Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    };
//    //??????dialog-----------------------------

    //20210118 O--------------------------

    //20210125S---------------------------
    //========?????????infowindow class============//
    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            // ?????????layout??????????????????????????????View??????
            // --------------------------------------------------------------------------------------
            // ?????????
            // View infoWindow=
            // getLayoutInflater().inflate(R.layout.custom_info_window,
            // null);
            // ??????????????????
            View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_content, null);
            infoWindow.setAlpha(0.5f);
            // ----------------------------------------------
            // ????????????title
            TextView title = ((TextView) infoWindow.findViewById(R.id.title));
            String[] ss = marker.getTitle().split("#");
            title.setText(ss[0]);
            // ????????????snippet
            TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
            snippet.setText(marker.getSnippet());
            // ????????????
            ImageView imageview = ((ImageView) infoWindow.findViewById(R.id.content_ico));
            imageview.setImageResource(Integer.parseInt(ss[1]));

            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            Toast.makeText(getApplicationContext(), "getInfoContents", Toast.LENGTH_LONG).show();
            return null;
        }
    }

//// -------------subclass end------------------------------
// ===================main class end ===========================

    //20210125O---------------------------

}
