package com.example.map_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    public static Admin admin;
    public static Credentials credentials;
    private GoogleMap mMap;
    private LocationManager locMgr;
    private String txt = "";
    private AppBarConfiguration mAppBarConfiguration;
    //緯度
    Double mylatitude = 0.0;
    //經度
    Double mylongitude = 0.0;

    public KingOfCountry kingOfCountry;
    private String pid1 = "3727b45af619a3ae5f061257f465d848cbf5eb1bb536dd7b302dfc9e52e82864";
    private String pid2 = "3727b45af619a3ae5f061257f465d848cbf5eb1bb536dd7b302dfc9e52e82864";
    private String contract_address = "0x8ad7fc6a3c560e3ae34086878548efa2b2498fff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //詢問位置存取權限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            admin = Admin.build(new HttpService("http://140.120.55.6:8545"));//連接到server
            credentials = Credentials.create(pid1);
            setContract();
            //修改nav_header_profile
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            TextView textView = navigationView.getHeaderView(0).findViewById(R.id.owner);
            textView.setText(credentials.getAddress());
            TextView alliance = navigationView.getHeaderView(0).findViewById(R.id.alliance);
            alliance.setText("陣營：綠色");
            leftSlider();

            try {
                String tmp = kingOfCountry.life().sendAsync().get().toString();
                Log.e("test", "connect to blockchain");
                Toast.makeText(getApplicationContext(), "connection succeeded", Toast.LENGTH_SHORT).show();
            } catch (ExecutionException e) {
                Log.e("test", "wrong");
                Toast.makeText(getApplicationContext(), "connection failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.e("test", "wrong");
                Toast.makeText(getApplicationContext(), "connection failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            //確認是否支援定位
            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //詢問定位是否開啟
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                try {
                    Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//網路定位
                    if (location == null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//手機gps定位
                    }
                    //緯度
                    mylatitude = location.getLatitude();
                    //經度
                    mylongitude = location.getLongitude();
                    txt = "Latitude:" + mylatitude + "    Longitude:" + mylongitude;
                    //Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
                    Log.e("test", txt);
                } catch (SecurityException e) {
                    Log.e("test", e.toString());
                    e.printStackTrace();
                }
            } else {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));//開啟定位
            }
        }

    }

    //連線合約設定
    public void setContract() {
        BigInteger GAS_PRICE = BigInteger.valueOf(15_000_000_000L);
        BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000L);
        String contractAddress = contract_address;
        kingOfCountry = KingOfCountry.load(
                contractAddress,
                admin,
                credentials,
                GAS_PRICE,
                GAS_LIMIT
        );
    }

    //slider method
    public void leftSlider() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //帳戶餘額
        try {
            BigInteger balance = admin.ethGetBalance(credentials.getAddress(), DefaultBlockParameter.valueOf("latest")).sendAsync().get().getBalance();
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.nav_token)
                        Toast.makeText(getApplicationContext(), Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString(), Toast.LENGTH_SHORT).show();
                    if (item.getItemId() == R.id.nav_home) {
                        //查詢擁有地標
                        ArrayList<String> list = new ArrayList<String>();
                        try {
                            contract_address = "0x8ad7fc6a3c560e3ae34086878548efa2b2498fff";
                            setContract();
                            if (kingOfCountry.owner().sendAsync().get().equals(credentials.getAddress()))
                                list.add(new String("0x8ad7fc6a3c560e3ae34086878548efa2b2498fff"));
                            contract_address = "0x8eae2b13eb1057a8496c2459c44dcd95c81676be";
                            setContract();
                            if (kingOfCountry.owner().sendAsync().get().equals(credentials.getAddress()))
                                list.add(new String("0x8eae2b13eb1057a8496c2459c44dcd95c81676be"));
                            contract_address = "0x5a13a835ca20b145b26e2aff599a9ea32d6638f4";
                            setContract();
                            if (kingOfCountry.owner().sendAsync().get().equals(credentials.getAddress()))
                                list.add(new String("0x5a13a835ca20b145b26e2aff599a9ea32d6638f4"));
                            contract_address = "0xa997437da66e1a5271720af2a358844538547b4d";
                            setContract();
                            if (kingOfCountry.owner().sendAsync().get().equals(credentials.getAddress()))
                                list.add(new String("0xa997437da66e1a5271720af2a358844538547b4d"));
                            contract_address = "0x14fbb2e21f6879e1e8528442c6b8e505146cd46b";
                            setContract();
                            if (kingOfCountry.owner().sendAsync().get().equals(credentials.getAddress()))
                                list.add(new String("0x14fbb2e21f6879e1e8528442c6b8e505146cd46b"));
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        AlertDialog.Builder dialog_list = new AlertDialog.Builder(MapsActivity.this);
                        dialog_list.setTitle("佔領地標");
                        dialog_list.setItems(list.toArray(new String[list.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog_list.show();
                    }
                    if (item.getItemId() == R.id.nav_logout) {//登入其他帳號
                        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MapsActivity.this);
                        View login = getLayoutInflater().inflate(R.layout.login, null);
                        Button btn_login = (Button) login.findViewById(R.id.btn_login);
                        EditText account = login.findViewById(R.id.account);
                        EditText passwd = login.findViewById(R.id.passwd);
                        mbuilder.setView(login);
                        mbuilder.setCancelable(false);
                        Dialog dialog = mbuilder.create();
                        btn_login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //測試帳號
                                if (account.getText().toString().equals("123") && passwd.getText().toString().equals("123")) {
                                    pid1 = "3727b45af619a3ae5f061257f465d848cbf5eb1bb536dd7b302dfc9e52e82864";
                                    dialog.dismiss();
                                } else if (account.getText().toString().equals("456") && passwd.getText().toString().equals("456")) {
                                    pid1 = "a1101f8a102c066ccb7cf818195fc695e3e99b12690595393f1145383b234f49";
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "登入失敗~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                credentials = Credentials.create(pid1);
                                setContract();
                                //修改nav_header_profile
                                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                                TextView textView = navigationView.getHeaderView(0).findViewById(R.id.owner);
                                textView.setText(credentials.getAddress());
                                TextView alliance = navigationView.getHeaderView(0).findViewById(R.id.alliance);
                                TextView name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
                                if (pid1.equals("a1101f8a102c066ccb7cf818195fc695e3e99b12690595393f1145383b234f49")) {
                                    alliance.setText("陣營：藍色");
                                    name.setText("大帥哥");
                                } else {
                                    alliance.setText("陣營：綠色");
                                    name.setText("大香蕉");
                                }
                                leftSlider();
                            }
                        });
                        dialog.show();
                    }
                    return false;
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add  marker
        mMap.addMarker(new MarkerOptions().position(new LatLng(24.120890, 120.674291)).title("鴨子湖"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(24.119811, 120.674265)).title("黃金屋"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(24.121133, 120.673294)).title("集中營"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(24.123522, 120.672753)).title("文青樓"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(24.122368, 120.681188)).title("金牛角"));

        /*MarkerOptions markerOpt = new MarkerOptions();//標記
        markerOpt.position(new LatLng(24.119910, 120.674282));
        markerOpt.title("台北101");
        markerOpt.snippet("好啊好啊~");
        markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(markerOpt);*/

        mMap.setMyLocationEnabled(true);//允許定位
        mMap.getUiSettings().setZoomControlsEnabled(true);// 右下角的放大縮小功能
        mMap.getUiSettings().setCompassEnabled(true);// 左上角的指南針，要兩指旋轉才會出現
        mMap.getUiSettings().setMapToolbarEnabled(true);// 右下角的導覽及開啟 Google Map功能


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mylatitude, mylongitude), 16));// 放大地圖到 16 倍大
        //點擊地標
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                double distance = 0.0;
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    try {
                        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location == null) {
                            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                        //算距離
                        Location tmp = new Location("place");
                        tmp.setLatitude(marker.getPosition().latitude);
                        tmp.setLongitude(marker.getPosition().longitude);
                        distance = location.distanceTo(tmp);
                        Toast.makeText(getApplicationContext(), String.valueOf(distance), Toast.LENGTH_SHORT).show();
                    } catch (SecurityException e) {
                        Log.e("test", e.toString());
                        e.printStackTrace();
                    }
                } else {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                contract_address = marker.getTitle().equals("鴨子湖") ? "0x8ad7fc6a3c560e3ae34086878548efa2b2498fff" :
                        marker.getTitle().equals("黃金屋") ? "0x8eae2b13eb1057a8496c2459c44dcd95c81676be" :
                                marker.getTitle().equals("集中營") ? "0x5a13a835ca20b145b26e2aff599a9ea32d6638f4" :
                                        marker.getTitle().equals("文青樓") ? "0xa997437da66e1a5271720af2a358844538547b4d" :
                                                "0x14fbb2e21f6879e1e8528442c6b8e505146cd46b";

                setContract();
                /************顯示bottom sheet************/
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MapsActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_sheet,
                                null
                        );
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                TextView txt = bottomSheetDialog.findViewById(R.id.textView2);
                txt.setText(marker.getTitle());//修改dialog標題
                String distanceDialog = "";
                distanceDialog = distance <= 200 ? "距離地標為：" + String.valueOf(distance) : "超過可攻擊範圍";
                TextView dis = bottomSheetDialog.findViewById(R.id.distance);
                dis.setText(distanceDialog);//與地標距離
                try {
                    //設定地標擁有者
                    TextView owner = bottomSheetDialog.findViewById(R.id.owner);
                    String user = kingOfCountry.owner().sendAsync().get();
                    if (!user.equals("0x0000000000000000000000000000000000000000"))
                        owner.setText(kingOfCountry.owner().sendAsync().get());
                    //存取地標紀錄
                    TextView record = bottomSheetDialog.findViewById(R.id.record);
                    Tuple3<String, BigInteger, BigInteger> record_contract = kingOfCountry.attackhistory(contract_address).sendAsync().get();
                    if (!user.equals("0x0000000000000000000000000000000000000000")) {
                        record.setText(record_contract.getValue1() + "\nPrice：" + record_contract.getValue2().toString() + "\nTime：" + record_contract.getValue3().toString());
                    }
                    //設定地標合約地址
                    TextView marker_adr = bottomSheetDialog.findViewById(R.id.marker_address);
                    marker_adr.setText(kingOfCountry.getContractAddress());
                    //設定生命、價值
                    TextView life = bottomSheetDialog.findViewById(R.id.life);
                    life.setText("生命：" + kingOfCountry.life().sendAsync().get().toString());
                    TextView price = bottomSheetDialog.findViewById(R.id.price);
                    BigDecimal tmp = Convert.fromWei(String.valueOf(kingOfCountry.price().sendAsync().get()), Convert.Unit.ETHER);
                    price.setText("地標價值：" + tmp);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ImageView img = bottomSheetDialog.findViewById(R.id.imageView);
                int rs = marker.getTitle().equals("鴨子湖") ? R.drawable.duck : marker.getTitle().equals("黃金屋") ? R.drawable.gold : marker.getTitle().equals("集中營") ? R.drawable.camp : marker.getTitle().equals("文青樓") ? R.drawable.book : R.drawable.ox;
                img.setImageResource(rs);//設圖片

                NestedScrollView nest = bottomSheetDialog.findViewById(R.id.bottomSheet);
                //nest.setBackgroundColor(Color.parseColor("#123456"));

                if (distance <= 200) {//電子圍籬
                    bottomSheetDialog.findViewById(R.id.occupy).setOnClickListener(new View.OnClickListener() {//佔領按鈕事件
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.findViewById(R.id.attack).setVisibility(View.VISIBLE);
                            bottomSheetDialog.findViewById(R.id.protect).setVisibility(View.GONE);
                            //Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.findViewById(R.id.attack1).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //change price
                                        int contact_life = kingOfCountry.life().sendAsync().get().intValue();
                                        if ((contact_life - 5) <= 100 && (contact_life - 5) >= 0) {
                                            BigInteger dec_num = Convert.toWei(new BigDecimal("5"), Convert.Unit.ETHER).toBigInteger();
                                            kingOfCountry.setPrice(dec_num).sendAsync().get();
                                        }

                                        //攻擊
                                        kingOfCountry.becomeking(new BigInteger("5")).sendAsync().get();
                                        leftSlider();

                                        contract_content(bottomSheetDialog);//更改bottom_sheet資料

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            bottomSheetDialog.findViewById(R.id.attack2).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //change price
                                        int contact_life = kingOfCountry.life().sendAsync().get().intValue();
                                        if ((contact_life - 10) <= 100 && (contact_life - 10) >= 0) {
                                            BigInteger dec_num = Convert.toWei(new BigDecimal("10"), Convert.Unit.ETHER).toBigInteger();
                                            kingOfCountry.setPrice(dec_num).sendAsync().get();
                                        }

                                        //攻擊
                                        kingOfCountry.becomeking(new BigInteger("10")).sendAsync().get();
                                        leftSlider();

                                        contract_content(bottomSheetDialog);//更改bottom_sheet資料

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            bottomSheetDialog.findViewById(R.id.attack3).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //change price
                                        int contact_life = kingOfCountry.life().sendAsync().get().intValue();
                                        if ((contact_life - 15) <= 100 && (contact_life - 15) >= 0) {
                                            BigInteger dec_num = Convert.toWei(new BigDecimal("15"), Convert.Unit.ETHER).toBigInteger();
                                            kingOfCountry.setPrice(dec_num).sendAsync().get();
                                        }

                                        //攻擊
                                        kingOfCountry.becomeking(new BigInteger("15")).sendAsync().get();
                                        leftSlider();

                                        contract_content(bottomSheetDialog);//更改bottom_sheet資料

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    bottomSheetDialog.findViewById(R.id.protect_Action).setOnClickListener(new View.OnClickListener() {//防禦按鈕事件
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.findViewById(R.id.attack).setVisibility(View.GONE);
                            bottomSheetDialog.findViewById(R.id.protect).setVisibility(View.VISIBLE);
                            //Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.findViewById(R.id.protected1).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //change price
                                        int contact_life = kingOfCountry.life().sendAsync().get().intValue();
                                        if ((contact_life + 5) <= 100 && (contact_life + 5) >= 0) {
                                            BigInteger dec_num = Convert.toWei(new BigDecimal("5"), Convert.Unit.ETHER).toBigInteger();
                                            kingOfCountry.setPrice(dec_num).sendAsync().get();
                                        }

                                        //防禦
                                        kingOfCountry.protect(new BigInteger("5")).sendAsync().get();
                                        leftSlider();

                                        contract_content(bottomSheetDialog);//更改bottom_sheet資料

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            bottomSheetDialog.findViewById(R.id.protected2).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //change price
                                        int contact_life = kingOfCountry.life().sendAsync().get().intValue();
                                        if ((contact_life + 10) <= 100 && (contact_life + 10) >= 0) {
                                            BigInteger dec_num = Convert.toWei(new BigDecimal("10"), Convert.Unit.ETHER).toBigInteger();
                                            kingOfCountry.setPrice(dec_num).sendAsync().get();
                                        }

                                        //防禦
                                        kingOfCountry.protect(new BigInteger("10")).sendAsync().get();
                                        leftSlider();

                                        contract_content(bottomSheetDialog);//更改bottom_sheet資料

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            bottomSheetDialog.findViewById(R.id.protected3).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //change price
                                        int contact_life = kingOfCountry.life().sendAsync().get().intValue();
                                        if ((contact_life + 15) <= 100 && (contact_life + 15) >= 0) {
                                            BigInteger dec_num = Convert.toWei(new BigDecimal("15"), Convert.Unit.ETHER).toBigInteger();
                                            kingOfCountry.setPrice(dec_num).sendAsync().get();
                                        }

                                        //防禦
                                        kingOfCountry.protect(new BigInteger("15")).sendAsync().get();
                                        leftSlider();

                                        contract_content(bottomSheetDialog);//更改bottom_sheet資料

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }
                /*************************************/
                marker.showInfoWindow();
                //Toast.makeText(getApplicationContext(), marker.getPosition().toString(), Toast.LENGTH_SHORT).show();//地標位置
                return false;
            }
        });
    }

    //更改bottom_sheet資料
    public void contract_content(BottomSheetDialog bottomSheetDialog) throws ExecutionException, InterruptedException {
        //設定生命、價值
        TextView life = bottomSheetDialog.findViewById(R.id.life);
        life.setText("生命：" + kingOfCountry.life().sendAsync().get().toString());
        TextView price = bottomSheetDialog.findViewById(R.id.price);
        BigDecimal tmp = Convert.fromWei(String.valueOf(kingOfCountry.price().sendAsync().get()), Convert.Unit.ETHER);
        price.setText("地標價值：" + tmp);
        //設定地標擁有者
        TextView owner = bottomSheetDialog.findViewById(R.id.owner);
        String user = kingOfCountry.owner().sendAsync().get();
        if (!user.equals("0x0000000000000000000000000000000000000000"))
            owner.setText(kingOfCountry.owner().sendAsync().get());
        //存取地標紀錄
        TextView record = bottomSheetDialog.findViewById(R.id.record);
        Tuple3<String, BigInteger, BigInteger> record_contract = kingOfCountry.attackhistory(contract_address).sendAsync().get();
        if (!user.equals("0x0000000000000000000000000000000000000000")) {
            record.setText(record_contract.getValue1() + "\nPrice：" + record_contract.getValue2().toString() + "\nTime：" + record_contract.getValue3().toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(), "位置換了~", Toast.LENGTH_SHORT);
        if (location != null) {
            String msg = String.format("%f , %f ", location.getLatitude(), location.getLatitude());
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            //String x = Double.toString(location.getLatitude());
            //String y = Double.toString(location.getLongitude());
            //LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));// 放大地圖到 16 倍大
            LatLng sydney = new LatLng(location.getLatitude(), location.getLatitude());
            Log.e("test", "位置換了~");
            mMap.addMarker(new MarkerOptions().position(sydney).title("自己~"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            //mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);// 右下角的放大縮小功能
            mMap.getUiSettings().setCompassEnabled(true);// 左上角的指南針，要兩指旋轉才會出現
            mMap.getUiSettings().setMapToolbarEnabled(true);// 右下角的導覽及開啟 Google Map功能

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));// 放大地圖到 16 倍大

        } else {
            Toast.makeText(getApplicationContext(), "Location is null", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
