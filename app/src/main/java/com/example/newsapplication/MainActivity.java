package com.example.newsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapplication.Model.Articles;
import com.example.newsapplication.Model.Headlines;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
//    EditText etQuery;
//    Button btnSearch,
    Button  btnAboutUs;
    Dialog dialog;
    final String API_KEY = "9d677849e6984a49b19a7e955bc0cdef";
//final String API_KEY = "Your API Key";
    Adapter adapter;
    List<Articles>  articles = new ArrayList<>();

    //For Navigation Bar
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;


    @SuppressLint({"RestrictedApi", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //New appl code
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);

//        etQuery = findViewById(R.id.etQuery);
//        btnSearch = findViewById(R.id.btnSearch);
        btnAboutUs = findViewById(R.id.aboutUs);
        dialog = new Dialog(MainActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String country = getCountry();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("",country,API_KEY);
            }
        });
        retrieveJson("",country,API_KEY);

        /*btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etQuery.getText().toString().equals("")){
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(etQuery.getText().toString(),country,API_KEY);
                        }
                    });
                    retrieveJson(etQuery.getText().toString(),country,API_KEY);
                }else{
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("",country,API_KEY);
                        }
                    });
                    retrieveJson("",country,API_KEY);
                }
            }
        });*/

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        //For Navigration Drawer
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);




    }

    public void retrieveJson(String query ,String country, String apiKey){


        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
//        if (!etQuery.getText().toString().equals("")){
//            call= ApiClient.getInstance().getApi().getSpecificData(query,apiKey);
//        }else{
            call= ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
//        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    public void showDialog(){
        Button btnClose;
        dialog.setContentView(R.layout.about_us_pop_up);
        dialog.show();
//        btnClose = dialog.findViewById(R.id.close);

//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact:
                Toast.makeText(MainActivity.this, "Contact us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(MainActivity.this, "About us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(MainActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }
}
