package trainedge.qaadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.Comparator;


import trainedge.qaadmin.Models.Category;

public class CategoriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener {

    private RecyclerView rv;
    private ArrayList<Category> mylist;
    private MyAdapter adapter;
    private ProgressDialog dialog;
    private FirebaseDatabase db;
    public static final String EXTRA_CATEGORY = "trainedge.qaadmin.EXTRA_CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Custom_alertDialog_addCategories frag = new Custom_alertDialog_addCategories();
                frag.show(getSupportFragmentManager(), "error");

            }
        });

        //show progress
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //step 1
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));


        // step 4
        mylist = new ArrayList<>();

        // step 5
        //setup database
        db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("categories");

        // step 6
        //fetch from internet
        dbRef.orderByValue().addValueEventListener(this);
        // step 15
        //create adapter
        adapter = new MyAdapter();
        // step 16
        // set adapter to RecyclerView
        rv.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                mylist.add(new Category(snapshot.getValue(String.class)));
                // step 17
                // update adapter
                adapter.notifyDataSetChanged();
            }
        } else {
            // step 9
            Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        }


    }

    @Override

    public void onCancelled(DatabaseError databaseError) {
        // step 6.5
        if (databaseError != null) {
            Toast.makeText(this, "could not get data " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //step 10

    //holder will be a subclass of RecylerView.ViewHolder

    class Holder extends RecyclerView.ViewHolder {
        TextView tv_category;
        ImageView btn_view, btn_delete;

        public Holder(View itemView) {
            super(itemView);
            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
            btn_view = (ImageView) itemView.findViewById(R.id.btn_view);
            btn_delete = (ImageView) itemView.findViewById(R.id.btn_delete);

        }
    }
    //step 11
    // adapter will be subclass of RecyclerView.Adapter<HolderClass>

    class MyAdapter extends RecyclerView.Adapter<Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //step 13
            View v = LayoutInflater.from(CategoriesActivity.this).inflate(R.layout.simple_card_item, parent, false);
            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            //step 14
            final Category category = mylist.get(position);
            holder.tv_category.setText(category.getName());
            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNextActivity(category);
                }
            });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCategory(category);
                }
            });

        }

        private void deleteCategory(final Category category) {
            // firebase coode
            DatabaseReference reference = db.getReference("categories");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getValue(String.class).equals(category.getName())) {
                            Toast.makeText(CategoriesActivity.this, "deleting...", Toast.LENGTH_SHORT).show();
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

        private void loadNextActivity(Category category) {
            //intent
            Intent intent = new Intent(CategoriesActivity.this, QuestionActivity.class);
            intent.putExtra(EXTRA_CATEGORY, category.getName());

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

