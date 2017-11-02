package trainedge.qaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import trainedge.qaapp.Fragment.QuesFragment;
import trainedge.qaapp.Model.QuesModel;


public class TestQuesActivity extends AppCompatActivity implements ValueEventListener {


    private TextView tv_subjectname;
    private TextClock textClock;
    private Button btn_previous, btn_next;

    private ViewPager mViewPager;

    private ArrayList<QuesModel> question;
    public static final String SUBJECT_NAME = "trainedge.qaapp.SUBJECT_NAME";
    private String category;
    private QAdapter adapter;
    private boolean isLoaded = false;
    private ProgressDialog dialog;
    private Button btnFin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ques);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_subjectname = (TextView) findViewById(R.id.tv_subjectname);
        textClock = (TextClock) findViewById(R.id.textClock);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        btn_next = (Button) findViewById(R.id.btn_next);
        btnFin = (Button) findViewById(R.id.btnFin);


        Intent intent = getIntent();
        category = intent.getStringExtra(SUBJECT_NAME);

        mViewPager = (ViewPager) findViewById(R.id.container);


        // firebase
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Questions");
        dbRef.orderByValue().addValueEventListener(this);
        question = new ArrayList<>();
        showDialog();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int next = 0;
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem < question.size()) {
                    next = currentItem + 1;
                } else {
                    next = 0;
                }
                mViewPager.setCurrentItem(next);
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prev = 0;
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem > question.size()) {
                    prev = currentItem - 1;
                } else {
                    prev = 0;
                }
                mViewPager.setCurrentItem(prev);
            }
        });
        btnFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestQuesActivity.this, "Your test have been taken,results will be sent next week", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading papers");
        dialog.show();
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (dataSnapshot.hasChildren()) {
            question.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                // step 17
                if (snapshot.child("category").getValue(String.class).equals(category)) {
                    continue;
                }
                question.add(snapshot.getValue(QuesModel.class));
                /*adapter.notifyDataSetChanged();*/

                // update adapter
            }
            isLoaded = true;
            adapter = new QAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(adapter);
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        if (databaseError != null) {
            Toast.makeText(TestQuesActivity.this, "could not get data " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public class QAdapter extends FragmentStatePagerAdapter {

        public QAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            QuesModel model = question.get(position);
            String op1 = model.getoption1();
            String op2 = model.getoption2();
            String op3 = model.getoption3();
            String op4 = model.getoption4();
            String ans = model.getAnswer();
            String que = model.getQuestion();
            return QuesFragment.getInstance(que, op1, op2, op3, op4, ans);
        }

        @Override
        public int getCount() {
            return question.size();
        }
    }


}