package com.example.bosswebtech.secrye.Profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bosswebtech.secrye.GeoLocation.GeocodingLocation;
import com.example.bosswebtech.secrye.MainActivity;
import com.example.bosswebtech.secrye.Model.EditProfile.Data;
import com.example.bosswebtech.secrye.Model.EditProfile.ResponseEditProfile;
import com.example.bosswebtech.secrye.Model.ResponseChangePassword;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.R;
import com.example.bosswebtech.secrye.Utils.UrlConfig;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.MediaRecorder.VideoSource.CAMERA;

class PerUtility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}

public class EditProfile extends AppCompatActivity {
    EditText name, email, phoneNumber, location;
    Button logout, changePassword, submit;
    FloatingActionButton edit_image;
    CircleImageView profileImage;
    String clicked = "";
    private String selectedImagePath1="";
    MultipartBody.Part body;
    private int GALLERY = 1, CAMERA = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int RequestPermissionCode = 1;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    String path,accessToken,message,status,groupId;
    Integer userId;
    File finalfile;
    RequestBody reqFile;
    String getName,getEmail,getMobno,getLocation,getPhoneNumber,userImage,memberType,memberTypeId,latitude,longitude,androidDeviceId,getMemberType,getphoneNumber;
    Call<ResponseEditProfile> call;
    RadioButton radioButtonMemberType,radioArmed,radioUnarmed;
    RadioGroup radioGroup;
    TextView txt_memberType;
    SharedPreferences sharedPreferences;
    private Data editedUserList;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA);
            } else {
                Toast.makeText(getApplicationContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Edit Profile");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sharedPreferences = getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("User_id",0);
        groupId = sharedPreferences.getString("Group_id","");
        userImage = sharedPreferences.getString("UserImage","");
        getPhoneNumber = sharedPreferences.getString("Phone","");
        getMemberType = sharedPreferences.getString("MemberTypeId","");

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");

        name = (EditText) findViewById(R.id.edt_name);
        email = (EditText) findViewById(R.id.edt_email);
        phoneNumber = (EditText) findViewById(R.id.edt_phonenumber);
        location = (EditText) findViewById(R.id.edt_location);
        logout = (Button) findViewById(R.id.btn_logout);
        changePassword = (Button) findViewById(R.id.btn_changePassword);
        submit = (Button) findViewById(R.id.btn_editprofile);
        edit_image = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        profileImage = (CircleImageView)findViewById(R.id.img_profileImage);
        radioGroup = (RadioGroup)findViewById(R.id.radioMemberType);
        radioArmed = (RadioButton)findViewById(R.id.radioarm);
        radioUnarmed = (RadioButton)findViewById(R.id.radiowithoutarm);
        txt_memberType = (TextView)findViewById(R.id.txt_memberType);

        name.setText(sharedPreferences.getString("Name", ""));
        email.setText(sharedPreferences.getString("Email", ""));
        phoneNumber.setText(sharedPreferences.getString("Phone", ""));
        location.setText(sharedPreferences.getString("Address", ""));
        if(!userImage.equals("")) {
            Picasso.with(getApplicationContext()).load(userImage).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(profileImage);
        }

        if(groupId.equals("4")){
            txt_memberType.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radioButtonMemberType = (RadioButton)radioGroup.findViewById(checkedId);
                if (null != radioButtonMemberType && checkedId > -1) {
                    //Toast.makeText(SignupActivity.this, radioButtonCategory.getText(), Toast.LENGTH_SHORT).show();
                    memberType = radioButtonMemberType.getText().toString();

                }
                }
        });

        if(getMemberType !=null && getMemberType.equals("1")){
            radioUnarmed.setChecked(true);
        }else if(getMemberType !=null && getMemberType.equals("2")){
            radioArmed.setChecked(true);
        }

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePasswordIntent = new Intent(EditProfile.this, ChangePassword.class);
                changePasswordIntent.putExtra("accessToken",accessToken);
                startActivity(changePasswordIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentLogout = new Intent(EditProfile.this, LoginActivity.class);

                SharedPreferences preferences =getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
//            try {
//                FirebaseInstanceId.getInstance().deleteInstanceId();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
                Toast.makeText(EditProfile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(intentLogout);
                logout();
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getName = name.getText().toString();
                getEmail = email.getText().toString();
                getMobno = phoneNumber.getText().toString();
                getLocation = location.getText().toString();
                getPhoneNumber = phoneNumber.getText().toString();


                if(groupId.equals("4") && memberType.equals("Armed")){
                    memberTypeId="2";
                }else{
                    memberTypeId="1";
                }
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(getLocation,getApplicationContext(),new GeocoderHandler());

            }
        });

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
           // _addressText.setText(locationAddress);
            String[] addString = locationAddress.split("\n");
            latitude = addString[0];
            longitude = addString[1];

            editProfile();


        }
    }

    private void editProfile() {

       // accessToken = "Bearer"+" "+accessToken;
        Map<String, String> map = new HashMap<>();
        map.put("Authorization",accessToken);

        if(finalfile != null) {
            reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), finalfile);
            body = MultipartBody.Part.createFormData("image", finalfile.getName(), reqFile);
        }else {
            body =null;
        }

        RFInterface api = RFClient.getApiService();
        if(body == null) {
            call = api.edit_profile(map, userId + "", getName, getEmail,getMobno, getLocation, latitude, longitude, groupId, memberTypeId);
        }else {
            call = api.edit_profileImage(map, userId + "", getName, getEmail,getMobno, getLocation, latitude, longitude, groupId, memberTypeId,body);
        }
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(EditProfile.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseEditProfile>() {
            @Override
            public void onResponse(Call<ResponseEditProfile> call, Response<ResponseEditProfile> response) {
                if (response.isSuccessful()) {
                    message = response.body().getMsg();
                    status = response.body().getStatus();
                    editedUserList = response.body().getData();

                    if(status.equals("success")) {
                        Toast.makeText(EditProfile.this, "success"+message, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("Name");
                        editor.remove("Phone");
                        editor.remove("Address");
                        editor.remove("MemberTypeId");
                        editor.remove("UserImage");
                        editor.commit();
                        //sharedPreferences = getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Name",editedUserList.getName());

                        editor.putString("Phone",editedUserList.getMobile().toString());
                        editor.putString("Address",editedUserList.getAddress());
                        editor.putString("MemberTypeId",editedUserList.getMemberTypeId());
                        editor.putString("UserImage",editedUserList.getUserimage());

                        editor.commit();
                        progressDoalog.dismiss();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "fail"+message, Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error"+message, Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseEditProfile> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in ProfileUpdation", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }

    private void logout() {
        //cmplt_accessToken = "Bearer"+" "+accessToken;
        Map<String,String>map = new HashMap<>();
        map.put("Authorization",accessToken);

        RFInterface api = RFClient.getApiService();
        Call<ResponseBody> call = api.logOut(map,userId, androidDeviceId);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(EditProfile.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(EditProfile.this, "success logout", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "error"+message, Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in PasswordChange", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }

    private void showPictureDialog(){
        PerUtility.checkPermission(this);
        verifyStoragePermissions(this);
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    path = saveImage(bitmap);
                    Toast.makeText(EditProfile.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    profileImage.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(bitmap);
            String path = saveImage(bitmap);

            Toast.makeText(EditProfile.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
        Uri tempUri = getImageUri(getApplicationContext(),bitmap);
        finalfile = new File(getRealPathFromURI(tempUri));
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());


            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
