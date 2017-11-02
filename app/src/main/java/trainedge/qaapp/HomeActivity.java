package trainedge.qaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private ImageView iv_profile, iv_dashboard, iv_reports, iv_help, iv_test, iv_settings;
    private TextView tv_profile, tv_dashboard, tv_reports, tv_help, tv_test, tv_settings;
    private CardView cv_profile, cv_dashboard, cv_reports, cv_help, cv_test, cv_settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, SignupActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        cv_profile = (CardView) findViewById(R.id.cv_profile);
     //   cv_dashboard = (CardView) findViewById(R.id.cv_dashboard);
        cv_reports = (CardView) findViewById(R.id.cv_reports);
        cv_help = (CardView) findViewById(R.id.cv_help);
        cv_test = (CardView) findViewById(R.id.cv_test);
        cv_settings = (CardView) findViewById(R.id.cv_settings);

        iv_profile = (ImageView) findViewById(R.id.iv_profile);
      //  iv_dashboard = (ImageView) findViewById(R.id.iv_dashboard);
        iv_reports = (ImageView) findViewById(R.id.iv_reports);
        iv_help = (ImageView) findViewById(R.id.iv_help);
        iv_test = (ImageView) findViewById(R.id.iv_test);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);

        tv_profile = (TextView) findViewById(R.id.tv_profile);
       // tv_dashboard = (TextView) findViewById(R.id.tv_dashboard);
        tv_reports = (TextView) findViewById(R.id.tv_reports);
        tv_test = (TextView) findViewById(R.id.tv_test);
        tv_help = (TextView) findViewById(R.id.tv_help);
        tv_settings = (TextView) findViewById(R.id.tv_settings);

        cv_profile.setOnClickListener(this);
        cv_test.setOnClickListener(this);
        cv_help.setOnClickListener(this);
        cv_settings.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.setting:
                return true;
            case R.id.logout:
                Logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Logout() {
        //logging out the user
        firebaseAuth.signOut();
        //closing activity
        finish();
        //starting login activity
        startActivity(new Intent(this, SignupActivity.class));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_profile:
                startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                break;
           // case R.id.cv_dashboard:
             //   break;
            case R.id.cv_reports:
                break;
            case R.id.cv_test:
                startActivity(new Intent(HomeActivity.this, TestActivity.class));
                break;
            case R.id.cv_help:
                startActivity(new Intent(HomeActivity.this, HelpActivity.class));
                break;
            case R.id.cv_settings:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
        }
    }
}
