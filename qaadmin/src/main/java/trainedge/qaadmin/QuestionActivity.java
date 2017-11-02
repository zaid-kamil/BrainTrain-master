package trainedge.qaadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import trainedge.qaadmin.Models.QAModel;

public class QuestionActivity extends AppCompatActivity implements ValueEventListener {
    public static final String EXTRA_CATEGORY = "trainedge.qaadmin.EXTRA_CATEGORY";
    public static final String EXTRA_QUESTION = "trainedge.qaadmin.EXTRA_QUESTION";
    private RecyclerView rv;
    private ArrayList<QAModel> mylist;
    private MyAdapter adapter;

    private ProgressDialog dialog;
    private FirebaseDatabase db;
    private String category;
    private TextView tv_cat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionActivity.this, AddQuestionActivity.class);
                startActivity(intent);


            }
        });

        tv_cat = (TextView) findViewById(R.id.tv_cat);


        if (getIntent() != null) {
            category = getIntent().getStringExtra(EXTRA_CATEGORY);
            tv_cat.setText(category + ": Questions");

        }

        //show progress
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();


        //step 1
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // step 4
        mylist = new ArrayList<>();

        // step 5
        //setup database
        db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Questions");

        // step 6
        //fetch from internet
        dbRef.addValueEventListener(this);

        // step 15
        //create adapter
        adapter = new MyAdapter();
        // step 16
        // set adapter to RecyclerView
        rv.setAdapter(adapter);


    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //hide progress
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        //step 7
        if (dataSnapshot.hasChildren()) {
            mylist.clear();
            // step 8
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String dbCategory = snapshot.child("category").getValue(String.class);
                if (this.category.equalsIgnoreCase(dbCategory)) {
                    mylist.add(snapshot.getValue(QAModel.class));
                }

                // step 17
                // update adapter}
                adapter.notifyDataSetChanged();
            }
            if (mylist.size() == 0) {

                Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();

            }
        } else {
            // step 9
            Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    //step 10

    //holder will be a subclass of RecylerView.ViewHolder

    class Holder extends RecyclerView.ViewHolder {
        TextView tv_question;
        ImageView btn_edit, btn_delete;

        public Holder(View itemView) {
            super(itemView);
            tv_question = (TextView) itemView.findViewById(R.id.tv_question);
            btn_edit = (ImageView) itemView.findViewById(R.id.btn_edit);
            btn_delete = (ImageView) itemView.findViewById(R.id.btn_delete);

        }
    }
    //step 11
    // adapter will be subclass of RecyclerView.Adapter<HolderClass>

    class MyAdapter extends RecyclerView.Adapter<QuestionActivity.Holder> {

        @Override
        public QuestionActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //step 13
            View v = LayoutInflater.from(QuestionActivity.this).inflate(R.layout.simple_card_item2, parent, false);
            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final QAModel quest = mylist.get(position);
            holder.tv_question.setText(quest.getQuestion());
            holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNextActivity(quest);
                }

            });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteQuestion(quest);
                }
            });
        }

        private void deleteQuestion(final QAModel question) {
            // firebase coode
            DatabaseReference reference = db.getReference("Questions");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("question").getValue(String.class).equals(question.getQuestion())) {
                            Toast.makeText(QuestionActivity.this, "deleting...", Toast.LENGTH_SHORT).show();
                            snapshot.getRef().removeValue();
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void loadNextActivity(QAModel question) {
            //intent
            Intent intent = new Intent(QuestionActivity.this, EditQuestionActivity.class);
            intent.putExtra(EXTRA_QUESTION, question.getQuestion());
            intent.putExtra(EXTRA_CATEGORY, question.getCategory());
            startActivity(intent);

            //putExtra
        }


        @Override
        public int getItemCount() {
            //step 12
            return mylist.size();//no of items
        }


    }


}
