package trainedge.qaapp.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import trainedge.qaapp.R;


public class PracticeFragment extends Fragment implements ValueEventListener {
    private ArrayList<String> subject;
    private ProgressDialog dialog;
    private PracticeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        //show progress
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();
        // firebase
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("categories");
        dbRef.orderByValue().addValueEventListener(this);
        subject=new ArrayList<>();

        RecyclerView rv_practice = (RecyclerView) view.findViewById(R.id.rv_practice);
        rv_practice.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PracticeAdapter();
        rv_practice.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //hide progress
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (dataSnapshot.hasChildren()) {
            subject.clear();


            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                // step 17
                subject.add(snapshot.getValue(String.class));
                // update adapter
            }
            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getContext(), "no data found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        if (databaseError != null) {
            Toast.makeText(getContext(), "could not get data " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class PracticeHolder extends RecyclerView.ViewHolder {
        Button btn_start;
        TextView tv_subject, tv_marks, marks, tv_duration, duration;

        public PracticeHolder(View itemView) {
            super(itemView);
            //step6.1
            btn_start = (Button) itemView.findViewById(R.id.btn_start);
            tv_subject = (TextView) itemView.findViewById(R.id.tv_subject);
            tv_marks = (TextView) itemView.findViewById(R.id.tv_qno);
            marks = (TextView) itemView.findViewById(R.id.marks);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            duration = (TextView) itemView.findViewById(R.id.duration);

        }
    }

    private class PracticeAdapter extends RecyclerView.Adapter<PracticeHolder> {

        @Override
        public PracticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //step7.1
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_test, parent, false);
            return new PracticeHolder(v);
        }

        @Override
        public void onBindViewHolder(PracticeHolder holder, int position) {
            String item = subject.get(position);
            holder.tv_subject.setText(item);
            holder.marks.setText("10 Marks");
            holder.duration.setText("10 Min");

        }



        @Override
        public int getItemCount() {
            //step7.3
            return subject.size();
        }
    }
}
