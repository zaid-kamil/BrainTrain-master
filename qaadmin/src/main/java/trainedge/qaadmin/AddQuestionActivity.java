package trainedge.qaadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import trainedge.qaadmin.Models.QAModel;

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ValueEventListener {

    public static final String QUESTIONS = "Questions";
    private Spinner spinner_category, spinner_answer;
    private EditText et_question, et_a, et_b, et_c, et_d;
    private Button btn_submit;
    private FirebaseDatabase db;
    private ArrayList<String> category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_answer = (Spinner) findViewById(R.id.spinner_answer);
        et_question = (EditText) findViewById(R.id.et_question);
        et_a = (EditText) findViewById(R.id.et_a);
        et_b = (EditText) findViewById(R.id.et_b);
        et_c = (EditText) findViewById(R.id.et_c);
        et_d = (EditText) findViewById(R.id.et_d);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        //spinner array
        category = new ArrayList();


        // step 5
        //setup database
        db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("categories");
        dbRef.addValueEventListener(this);



        //spinner array
        ArrayList<String> answer = new ArrayList();
        answer.add("A");
        answer.add("B");
        answer.add("C");
        answer.add("D");
        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, answer);
        adapter1.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner_answer.setAdapter(adapter1);
        spinner_answer.setOnItemSelectedListener(this);

        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        //show progress
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading..");
        dialog.setCancelable(false);
        dialog.show();

        String var = et_question.getText().toString().trim();
        if (var.isEmpty()) {
            //Toast.makeText(this, "Enter Question!", Toast.LENGTH_SHORT).show();
            et_question.setError("Enter Question!");
            return;
        }
        String var1 = et_a.getText().toString().trim();
        if (var1.isEmpty()) {
            et_a.setError("Enter Option 'a'!");
            return;
        }
        String var2 = et_b.getText().toString().trim();
        if (var2.isEmpty()) {
            et_b.setError("Enter Option 'b'!");
            return;
        }
        String var3 = et_c.getText().toString().trim();
        if (var3.isEmpty()) {
            et_c.setError("Enter Option 'c'!");
            return;
        }
        String var4 = et_d.getText().toString().trim();
        if (var4.isEmpty()) {
            et_d.setError("Enter Option 'd'!");
            return;
        } else {

            String category = spinner_category.getSelectedItem().toString();
            String answer = spinner_answer.getSelectedItem().toString();

            FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
            DatabaseReference dbref = fbdb.getReference(QUESTIONS);
            dbref.push().setValue(new QAModel(category, var, var1, var2, var3, var4, answer),
                    new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //hide progress
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (databaseError == null) {
                                Toast.makeText(AddQuestionActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddQuestionActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        Intent intent=new Intent(AddQuestionActivity.this,QuestionActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
        category.clear();
        // step 8
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            category.add(snapshot.getValue(String.class));
            // step 17
            // update a
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, category);
            adapter.setDropDownViewResource(R.layout.simple_spinner_item);
            spinner_category.setAdapter(adapter);
            spinner_category.setOnItemSelectedListener(this);

        }
    } else {
        // step 9
        Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
    }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
