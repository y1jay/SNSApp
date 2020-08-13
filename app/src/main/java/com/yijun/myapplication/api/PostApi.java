package com.yijun.myapplication.api;

import com.yijun.myapplication.model.Post;
import com.yijun.myapplication.model.PostRes;
import com.yijun.myapplication.model.UserRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostApi {
    @GET("/api/v1/sns_friends/friends")
    Call<PostRes> getPosts(@Header ("Authorization") String token,
                           @Query("offset") int offset,
                           @Query("limit")int limit);
    // 파일 전송을 가능하게 해준다.@Multipart
    @Multipart
    @POST("/api/v1/sns_users/posting")
    Call<UserRes> createPost(@Header("Authorization") String token,
                         @Part MultipartBody.Part file,
                         @Part ("posting")RequestBody requestBody);

}
