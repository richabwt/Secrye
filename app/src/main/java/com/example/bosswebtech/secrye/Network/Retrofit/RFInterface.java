package com.example.bosswebtech.secrye.Network.Retrofit;

import com.example.bosswebtech.secrye.Model.EditProfile.ResponseEditProfile;
import com.example.bosswebtech.secrye.Model.Incident.ResponseIncident;
import com.example.bosswebtech.secrye.Model.Login.ResponseLogin;
import com.example.bosswebtech.secrye.Model.ResponseAcceptIncident;
import com.example.bosswebtech.secrye.Model.ResponseChangePassword;
import com.example.bosswebtech.secrye.Model.ResponseChoosePlan;
import com.example.bosswebtech.secrye.Model.ResponseForgotPassword;
import com.example.bosswebtech.secrye.Model.ResponseSignUp;
import com.example.bosswebtech.secrye.Model.ResponseToken;
import com.example.bosswebtech.secrye.Model.SelectPlans.ResponseSelectPlans;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Brajendr on 11/24/2016.
 */

public interface RFInterface {

    @POST("login")
    Call<ResponseLogin> getUser(
            @Query("email") String email,
            @Query("password") String password,
            @Query("device_id") String device_id,
            @Query("reg_token") String reg_token,
            @Query("device_type") String device_type
    );

    @POST("forgot_password")
    Call<ResponseForgotPassword> forgotPassword(
            @Query("email") String email

    );

    @Headers({

            "Content-Type: application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST("http://secyre.com/beta/oauth/token")
    Call<ResponseToken> generateToken(
          @Field("grant_type") String grant_type,
          @Field("client_id") String client_id,
          @Field("client_secret") String client_secret,
          @Field("username") String username,
          @Field("password") String password
         );


    @POST("change_password")
    Call<ResponseChangePassword> changePassword(
            @HeaderMap Map<String, String> headers,
            @Query("email") String email,
            @Query("old") String old,
            @Query("password") String password,
            @Query("password_confirmation") String password_confirmation
    );

    @POST("http://secyre.com/beta/api/webservice/logout")
    Call<ResponseBody> logOut(
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Integer user_id,
            @Query("device_id") String device_id

    );

    @POST("register")
    Call<ResponseSignUp> signUp(
            @Query("name") String name,
            @Query("email") String email,
            @Query("mobile") String mobile,
            @Query("password") String password,
            @Query("address") String address,
            @Query("group_id") String group_id,
//            @Query("device_id") String device_id,
//            @Query("reg_token") String reg_token,
//            @Query("device_type") String device_type,
            @Query("longitude") String longitude,
            @Query("latitude") String latitude
    );

    @POST("incidents")
    Call<ResponseIncident> incidentDetails(
            @HeaderMap Map<String, String> headers,
            @Query("user_id") String user_id,
            @Query("group_id") String group_id

    );

    @POST("accept_incident")
    Call<ResponseAcceptIncident> accept_incident(
            @HeaderMap Map<String, String> headers,
            @Query("incident_id") String incident_id,
            @Query("member_id") String member_id

    );
    @Multipart
    @POST("edit_profile")
    Call<ResponseEditProfile> edit_profileImage(
            @HeaderMap Map<String, String> headers,
            @Query("user_id") String user_id,
            @Query("name") String name,
            @Query("email") String email,
            @Query("mobile") String mobile,
            @Query("address") String address,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("group_id") String group_id,
            @Query ("member_type_id") String member_type_id,
            @Part MultipartBody.Part image
    );


    @POST("edit_profile")
    Call<ResponseEditProfile> edit_profile(
            @HeaderMap Map<String, String> headers,
            @Query("user_id") String user_id,
            @Query("name") String name,
            @Query("email") String email,
            @Query("mobile") String mobile,
            @Query("address") String address,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("group_id") String group_id,
            @Query ("member_type_id") String member_type_id

    );

    @POST("client_plan")
    Call<ResponseSelectPlans> client_plan(
            @HeaderMap Map<String, String> headers,
            @Query("client_id") String client_id


    );

    @POST("select_plan")
    Call<ResponseChoosePlan> select_plan(
            @HeaderMap Map<String, String> headers,
            @Query("client_id") String client_id,
             @Query("plan_id") String plan_id


    );
}
