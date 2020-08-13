package com.yijun.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yijun.myapplication.adapter.RecyclerViewAdapter;
import com.yijun.myapplication.api.NetworkClient;
import com.yijun.myapplication.api.PostApi;
import com.yijun.myapplication.api.UserApi;
import com.yijun.myapplication.model.Post;
import com.yijun.myapplication.model.PostRes;
import com.yijun.myapplication.model.Row;
import com.yijun.myapplication.model.UserRes;
import com.yijun.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WelcomActivity extends AppCompatActivity {

Button btn_logout;
Button btnPosting;
RecyclerView recyclerView;
RecyclerViewAdapter adapter;
List<Row> postArrayList = new ArrayList<>();
String token;
    SharedPreferences sp;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        btn_logout = findViewById(R.id.btnlog_out);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(WelcomActivity.this));
        btnPosting = findViewById(R.id.btnPosting);


       btn_logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sp=getSharedPreferences(
                       Utils.PREFERENCES_NAME,MODE_PRIVATE);
               final String token = sp.getString("token",null);

              Retrofit retrofit = NetworkClient.getRetrofitClient(WelcomActivity.this);
               UserApi userApi = retrofit.create(UserApi.class);
               Call<UserRes> call = userApi.logoutUser("Bearer "+token);
               call.enqueue(new Callback<UserRes>() {
                   @Override
                   public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                       if (response.isSuccessful()){
                           if (response.body().isSuccess()){
                               sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                               SharedPreferences.Editor editor = sp.edit();
                               editor.putString("token",null);
                               editor.apply();

                               Intent i = new Intent(WelcomActivity.this, Login.class);
                               startActivity(i);
                               finish();
                           }
                       }
                   }

                   @Override
                   public void onFailure(Call<UserRes> call, Throwable t) {
                     Log.i("AAAA","? ",t);
                   }
               });
           }
       });

      SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
      token = sp.getString("token",null);
       getNetworkData();
    }

    private void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(WelcomActivity.this);

        PostApi postsApi = retrofit.create(PostApi.class);

       Call<PostRes> call = postsApi.getPosts("Bearer "+token, 0,25);
       call.enqueue(new Callback<PostRes>() {
           @Override
           public void onResponse(Call<PostRes> call, Response<PostRes> response) {
               // response.body() ==> PostRes 클래스
               Log.i("AAAA",response.body().getSuccess().toString());
               //PostRes.get(0) => List<row>의 첫번째 Item 객체.
               // PostRes.get(0).getPosting()=> 위의 Row 객체에 저장된 Posting 값
               Log.i("AAAA",response.body().getRows().get(0).getPosting());
               Log.i("AAAA",response.body().getCnt().toString());

               postArrayList = response.body().getRows();

               adapter = new RecyclerViewAdapter(WelcomActivity.this, postArrayList);
               recyclerView.setAdapter(adapter);
           }

           @Override
           public void onFailure(Call<PostRes> call, Throwable t) {

           }
       });
   btnPosting.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(WelcomActivity.this,Posting.class);
        startActivity(i);
    }
});
    }
}
