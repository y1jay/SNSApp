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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yijun.myapplication.adapter.RecyclerViewAdapter;
import com.yijun.myapplication.api.NetworkClient;
import com.yijun.myapplication.api.PostsApi;
import com.yijun.myapplication.model.Post;
import com.yijun.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class WelcomActivity extends AppCompatActivity {

Button btn_logout;

RecyclerView recyclerView;
RecyclerViewAdapter adapter;
ArrayList<Post> postArrayList = new ArrayList<>();
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


        requestQueue = Volley.newRequestQueue(WelcomActivity.this);
       btn_logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sp=getSharedPreferences(
                       Utils.PREFERENCES_NAME,MODE_PRIVATE);
               final String token = sp.getString("token",null);

               JsonObjectRequest request = new JsonObjectRequest(
                       Request.Method.DELETE,
                       Utils.BASE_URL + "/api/v1/sns_users/logout",
                       null,
                       new Response.Listener<JSONObject>() {
                           @Override
                           public void onResponse(JSONObject response) {
                               try {
                                   boolean success = response.getBoolean("success");
                                   if(success == true){
                                       // 토큰을 지워줘야 한다.
                                       sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                                         SharedPreferences.Editor editor = sp.edit();
                                         editor.clear();
                                         editor.apply();

                                       Intent i = new Intent(WelcomActivity.this,Login.class);
                                       startActivity(i);
                                       finish();
                                   }else{
                                       Toast.makeText(WelcomActivity.this,"에러에러",Toast.LENGTH_SHORT).show();
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       },
                       new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                              Log.i("로그아웃",error.toString());
                               Toast.makeText(WelcomActivity.this,"실패실패",Toast.LENGTH_SHORT).show();
                           }
                       }
               ){
                   @Override
                   public Map<String, String> getHeaders() throws AuthFailureError {
                       Map<String, String> params  = new HashMap<>();
                       params.put ( "Authorization", "Bearer "+token);

                       return params;
                   }
               };

               requestQueue.add(request);
           }
       });

      SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
      token = sp.getString("token",null);
       getNetworkData();
    }

    private void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(WelcomActivity.this);

        PostsApi postsApi = retrofit.create(PostsApi.class);

        Call<ResponseBody> call = postsApi.getPosts(token, 0 , 25);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.i("AAA",response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              Log.i("AAA",t.toString());
            }
        });

    }
}
