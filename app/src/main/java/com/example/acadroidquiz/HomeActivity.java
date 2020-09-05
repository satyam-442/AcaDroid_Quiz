package com.example.acadroidquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.acadroidquiz.Fragment.LoginFragment;
import com.example.acadroidquiz.Fragment.RegisterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ChipNavigationBar bottomNavigationView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        if (savedInstanceState == null) {
            bottomNavigationView.setItemSelected(R.id.login, true);
            fragmentManager = getSupportFragmentManager();
            LoginFragment loginFragment = new LoginFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, loginFragment).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.login:
                        fragment = new LoginFragment();
                        break;
                    case R.id.register:
                        fragment = new RegisterFragment();
                        break;
                }
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();
                } else {
                    Toast.makeText(HomeActivity.this, "ERROR OCCURRED...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected(this)) {
            showCustomDialog();
        }
    }


    private boolean isConnected(HomeActivity homeActivity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) homeActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiCon != null && wifiCon.isConnected()) || (mobileCon != null && mobileCon.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Please get connect to internet to proceed further!")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.login:
                selectedFragment = new LoginFragment();
                break;

            case R.id.register:
                selectedFragment = new RegisterFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    }

    /*private void draw() {
        bottomNavigationView.mFirstCurveStartPoint.set((bottomNavigationView.mNavigationBarWidth * 10/12)
                - (bottomNavigationView.CURVE_CIRCLE_RADIUS*2)
                - (bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        bottomNavigationView.mFirstCurveEndPoint.set(bottomNavigationView.mNavigationBarWidth * 10/12,bottomNavigationView.CURVE_CIRCLE_RADIUS
                +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.mSecondCurveStartPoint = bottomNavigationView.mFirstCurveEndPoint;
        bottomNavigationView.mSecondCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth * 10/12)
                +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2)+(bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        bottomNavigationView.mFirstCurveControlPoint1.set(bottomNavigationView.mFirstCurveStartPoint.x
                        +bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                bottomNavigationView.mFirstCurveStartPoint.y);

        bottomNavigationView.mFirstCurveControlPoint2.set(bottomNavigationView.mFirstCurveEndPoint.x -
                        (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mFirstCurveEndPoint.y);

        //FOR SECOND
        bottomNavigationView.mSecondCurveControlPoint1.set(bottomNavigationView.mSecondCurveStartPoint.x
                        + (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mSecondCurveStartPoint.y);
        bottomNavigationView.mSecondCurveControlPoint2.set(bottomNavigationView.mSecondCurveEndPoint.x -
                (bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),bottomNavigationView.mSecondCurveEndPoint.y);
    }

    private void drawAnimation(final VectorMasterView fab) {
        outline = fab.getPathModelByName("outline");
        outline.setStrokeColor(Color.WHITE);
        outline.setTrimPathEnd(0.0f);

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                outline.setTrimPathEnd((Float)valueAnimator.getAnimatedValue());
                fab.update();
            }
        });
        valueAnimator.start();
    }

    private void draw(int i) {
        bottomNavigationView.mFirstCurveStartPoint.set((bottomNavigationView.mNavigationBarWidth/i)
        -(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - (bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        bottomNavigationView.mFirstCurveEndPoint.set(bottomNavigationView.mNavigationBarWidth/i,bottomNavigationView.CURVE_CIRCLE_RADIUS
        +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.mSecondCurveStartPoint = bottomNavigationView.mFirstCurveEndPoint;
        bottomNavigationView.mSecondCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth/i)
        +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2)+(bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        bottomNavigationView.mFirstCurveControlPoint1.set(bottomNavigationView.mFirstCurveStartPoint.x
        +bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                bottomNavigationView.mFirstCurveStartPoint.y);

        bottomNavigationView.mFirstCurveControlPoint2.set(bottomNavigationView.mFirstCurveEndPoint.x -
                (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mFirstCurveEndPoint.y);

        //FOR SECOND
        bottomNavigationView.mSecondCurveControlPoint1.set(bottomNavigationView.mSecondCurveStartPoint.x
                        + (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mSecondCurveStartPoint.y);
        bottomNavigationView.mSecondCurveControlPoint2.set(bottomNavigationView.mSecondCurveEndPoint.x -
                (bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),bottomNavigationView.mSecondCurveEndPoint.y);
    }*/
}