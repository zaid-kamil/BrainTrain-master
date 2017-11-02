package trainedge.qaapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import trainedge.qaapp.Model.QuesModel;
import trainedge.qaapp.R;


public class QuesFragment extends Fragment {

    public static final String QUESTION = "question";
    public static final String OP_1 = "op1";
    public static final String OP_2 = "op2";
    public static final String OP_3 = "op3";
    public static final String OP_4 = "op4";
    public static final String ANS = "ans";
    public TextView tv_question;
    RadioButton rb_option1, rb_option2, rb_option3, rb_option4;
    private String que;
    private String ans;
    private String op1;
    private String op2;
    private String op3;
    private String op4;


    public static Fragment getInstance(String que, String op1, String op2, String op3, String op4, String ans){
        QuesFragment quesFragment = new QuesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION,que);
        bundle.putString(OP_1,op1);
        bundle.putString(OP_2,op2);
        bundle.putString(OP_3,op3);
        bundle.putString(OP_4,op4);
        bundle.putString(ANS,ans);
        quesFragment.setArguments(bundle);
        return quesFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            Bundle args = getArguments();
            que = args.getString(QUESTION, "");
            ans = args.getString(ANS, "");
            op1 = args.getString(OP_1, "");
            op2 = args.getString(OP_2, "");
            op3 = args.getString(OP_3, "");
            op4 = args.getString(OP_4, "");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ques, container, false);

        tv_question = (TextView) view.findViewById(R.id.tv_question);
        rb_option1 = (RadioButton) view.findViewById(R.id.rb_option1);
        rb_option2 = (RadioButton) view.findViewById(R.id.rb_option2);
        rb_option3 = (RadioButton) view.findViewById(R.id.rb_option3);
        rb_option4 = (RadioButton) view.findViewById(R.id.rb_option4);
        tv_question.setText(que);
        rb_option1.setText(op1);
        rb_option2.setText(op2);
        rb_option3.setText(op3);
        rb_option4.setText(op4);
        return view;
    }


}



/*    public class QuesHolder extends RecyclerView.ViewHolder {
        public TextView tv_question;
        RadioButton rb_option1, rb_option2, rb_option3, rb_option4;

        public QuesHolder(View itemView) {
            super(itemView);

            tv_question = (TextView) itemView.findViewById(R.id.tv_question);
            rb_option1 = (RadioButton) itemView.findViewById(R.id.rb_option1);
            rb_option2 = (RadioButton) itemView.findViewById(R.id.rb_option2);
            rb_option3 = (RadioButton) itemView.findViewById(R.id.rb_option3);
            rb_option4 = (RadioButton) itemView.findViewById(R.id.rb_option4);
        }
    }

    private class QuesAdapter extends RecyclerView.Adapter<QuesHolder> {

        public QuesAdapter(QuesFragment quesFragment, ArrayList<QuesModel> question) {

        }

        @Override
        public QuesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //step7.1
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_ques, parent, false);
            return new QuesHolder(v);
        }

        @Override
        public void onBindViewHolder(QuesHolder holder, int position) {
            //step 7.2
            holder.tv_question.setText(item);
            holder.rb_option1.setText(item.get(position));
            holder.rb_option2.setText(item.get(position));
            holder.rb_option3.setText(item.get(position));
            holder.rb_option4.setText(item.get(position));


        }

        @Override
        public int getItemCount() {
            return question.size();
        }
    }*/


