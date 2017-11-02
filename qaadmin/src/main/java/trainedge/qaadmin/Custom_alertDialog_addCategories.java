package trainedge.qaadmin;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class Custom_alertDialog_addCategories extends DialogFragment {


    public Custom_alertDialog_addCategories() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_custom_alert_dialog_add_categories, null);
        final EditText etCategory= (EditText) view.findViewById(R.id.et_category);
        builder.setView(view)
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCtegory = etCategory.getText().toString();
                        FirebaseDatabase.getInstance().getReference("categories").push().setValue(newCtegory);
                    }
                });

        return builder.create();
    }
}
