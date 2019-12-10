package mirrormirror.swen302.mirrormirrorandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mirrormirror.swen302.mirrormirrorandroid.R;
import mirrormirror.swen302.mirrormirrorandroid.adapters.HorizontalAdapter;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ImageStorageManager;
import mirrormirror.swen302.mirrormirrorandroid.utilities.InputPeakFlowDialog;
import mirrormirror.swen302.mirrormirrorandroid.utilities.InputWeightDialog;
import mirrormirror.swen302.mirrormirrorandroid.utilities.WeightPopupDialog;
import mirrormirror.swen302.mirrormirrorandroid.utilities.PermissionRequester;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ServerController;

/**
 * Created by bondkyal on 10/08/17.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;
    private WeightPopupDialog popup;

    public static final int CAMERA_ACTIVITY_REQUEST_CODE = 10;

    RecyclerView recyclerView;
    HorizontalAdapter horizontalAdapter;

    private List<String> filePaths;
    boolean isLoadingImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        ActionBar titleBar = getSupportActionBar();
        titleBar.setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        filePaths = new ArrayList<>();
        isLoadingImages = false;
        this.popup = null;
        setRecyclerView();
        setSocketListeners();

        initialLoadImages();

    }

    public List<String> getFilePaths(){
        return filePaths;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void setRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.horizontal_recycler_view);
        horizontalAdapter = new HorizontalAdapter(filePaths,this);
        final LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(horizontalAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = horizontalLayoutManager.getChildCount();
                int totalItemCount = horizontalLayoutManager.getItemCount();
                int pastVisible = horizontalLayoutManager.findFirstVisibleItemPosition();
                if(pastVisible + visibleItemCount >= totalItemCount && !isLoadingImages){
                    isLoadingImages = true;
                    ServerController.sendImageAdditionsRequest(HomeActivity.this, 5, totalItemCount);
                }
            }
        });
    }

    private void setSocketListeners(){
        ServerController.setSocketListeners(this);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.take_image){
            //if has permission already will run here, else will be activated in onpermission result
            if(PermissionRequester.requestCameraPermission(this)){
                Intent intent = new Intent(this, CameraPreviewActivity.class);
                startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
            }
        } else if(item.getItemId() == R.id.input_peak_flow){
            InputPeakFlowDialog ipfd = new InputPeakFlowDialog(this);
            ipfd.show();
        } else if(item.getItemId() == R.id.input_weight){
            InputWeightDialog iwd = new InputWeightDialog(this);
            iwd.show();
        } else if(item.getItemId() == R.id.logout) {
            logout();
        } else if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getString(R.string.login_details), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("uid");
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadImages();
    }

    private void initialLoadImages(){
        File[] files = getFilesDir().listFiles();

        for(int i = 0; i < files.length; i ++){
            filePaths.add(files[i].getName());
        }
        orderFilePaths();

        ServerController.sendImagesRequest(this, 5);
    }

    private void loadImages(){
        File[] files = getFilesDir().listFiles();
        for(int i = filePaths.size(); i < files.length; i ++){
            filePaths.add(files[i].getName());
        }
        orderFilePaths();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().notifyDataSetChanged();

            }
        });


    }

    public void loadInitialImagesFromServer(JSONArray images){
        for(int i = 0; i < images.length(); i++){
            try {
                JSONObject object = images.getJSONObject(i);
                String bytesString = object.getString("imageString");
                String time = object.getString("time");
                ImageStorageManager.storeImage(time, Base64.decode(bytesString, Base64.DEFAULT), this);
                loadImages();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        //loadImages();
        if(filePaths.size() < 10){
            ServerController.sendImageAdditionsRequest(this, 5, filePaths.size());
        }


    }

    public void loadMoreImages(JSONArray images){
        for(int i = 0; i < images.length(); i++){
            try {
                JSONObject object = images.getJSONObject(i);
                String bytesString = object.getString("imageString");
                String time = object.getString("time");
                ImageStorageManager.storeImage(time, Base64.decode(bytesString, Base64.DEFAULT), this);
                loadImages();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        isLoadingImages = false;
    }

    public void loadWeightPopup(JSONObject object){
        try {
            final double weight = object.getDouble("weight");

            final double bmi = object.getDouble("bmi");

            System.out.println(weight);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(HomeActivity.this.popup == null || !HomeActivity.this.popup.isShowing()) {
                        HomeActivity.this.popup = new WeightPopupDialog(HomeActivity.this, weight, bmi);
                        popup.show();
                    }else{
                        popup.updateValues(weight, bmi);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void orderFilePaths(){
        Collections.sort(filePaths, new strComparator());

    }

    //React to items selected within the sidebar.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int numberOfDays = 7;
        Intent weightIntent = new Intent(HomeActivity.this, WeightGraphActivity.class);
        if(id == R.id.weight7days){
            weightIntent.putExtra("numDays", 7);
            weightIntent.putExtra("title", "Your weight in past 7 days");
        }else if(id == R.id.weight30days){
            System.out.println("30");
            weightIntent.putExtra("numDays", 30);
            weightIntent.putExtra("title", "Your weight in past 30 days");
            numberOfDays = 30;
        }else if(id == R.id.weight180days){
            System.out.println("180");
            weightIntent.putExtra("numDays", 180);
            weightIntent.putExtra("title", "Your weight in past 180 days");

            numberOfDays = 180;
        }else if(id == R.id.weight365days){
            System.out.println("365");
            weightIntent.putExtra("numDays", 365);
            weightIntent.putExtra("title", "Your weight in past 365 days");

            numberOfDays = 365;
        }

        HomeActivity.this.startActivity(weightIntent);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PermissionRequester.CAMERA_PERMISSION_CONSTANT){
            Intent intent = new Intent(this, CameraPreviewActivity.class);
            startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
        }
        else if(requestCode == PermissionRequester.INTERNET_PERMISSION_CONSTANT){

        }
    }

    public void onConnection(){
        ServerController.sendAndroidIdEvent(this);
    }

    class strComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2){
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");
                Date d1 = fmt.parse(s1);
                Date d2 = fmt.parse(s2);

                if(d1.before(d2))return 1;
                else{
                    return -1;
                }
            }
            catch(Exception e){
                return -1;
            }
        }
    }

}
