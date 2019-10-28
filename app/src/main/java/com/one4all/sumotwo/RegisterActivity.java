package com.one4all.sumotwo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";


    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private CircleImageView circleImageView;
    ProgressDialog progressDialog;

    // Firebase instance variables
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
//    FirebaseStorage firebaseStorage;
//    Firestore firestore;
    Button choosePhoto;
    Uri uri;
    String imageLink;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);






        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null){
            Log.d("return","photoclicked");



                try{

             uri = data.getData();
            Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            BitmapDrawable bitmapDrawable1 = new BitmapDrawable(bit);

//            choosePhoto.setBackgroundDrawable(bitmapDrawable1);
            circleImageView.setImageBitmap(bit);
            choosePhoto.setAlpha(0f);


        } catch (IOException e) {
            e.printStackTrace();
        }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        FirebaseApp.initializeApp(getApplicationContext());

        mEmailView =  findViewById(R.id.register_email);
        mPasswordView =  findViewById(R.id.register_password);
        mConfirmPasswordView =  findViewById(R.id.register_confirm_password);
        mUsernameView =  findViewById(R.id.register_username);
        choosePhoto = findViewById(R.id.choose_photo);
        circleImageView = findViewById(R.id.profile_image);
        getSupportActionBar().setTitle("Enter your details");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//unless doesn't work
                intent.setType("image/*");
                startActivityForResult(intent,0);
//                startActivity(intent);
            }
        });

//         firebaseStorage.getReference().child("userList").putBytes(mEmailView.getText().toString().getBytes());







//

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });


        //  instance of FirebaseAuth

        firebaseAuth  = FirebaseAuth.getInstance();



    }

        // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        progressDialog.setTitle("Please wait!!");
        progressDialog.setMessage("Account is creating");
        progressDialog.show();

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the userListFragment entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFireBaseUser();
            String name = mUsernameView.getText().toString();
            String userEmail = mEmailView.getText().toString();

//
// ...
// future.get() blocks on response

//



        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //Check  confirm password
        String confiremPassword = mConfirmPasswordView.getText().toString();

        return password.equals(confiremPassword) && password.length() > 5;
    }

    // Create a Firebase userListFragment
    public void createFireBaseUser(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("sumo","sumo created");

                if (!task.isSuccessful()){
//                    showErrorDialog("Email already exists");
                }else {
                    uploadImageToFirebaseStorage();
                    saveName();
                    progressDialog.dismiss();
                    Intent intent = new Intent(RegisterActivity.this,LatestMessageActivity.class);

//                    finish();
                    startActivity(intent);
                }
            }
        });


    }


    // Save the display name to Shared Preferences
    private  void saveName(){
        String name = mUsernameView.getText().toString();
        SharedPreferences sharedPreferences =getSharedPreferences(CHAT_PREFS,0);
        sharedPreferences.edit().putString(DISPLAY_NAME_KEY,name).apply();
    }



    public void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", null).show();
    }
    public void goBack(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void choosePhoto(View view){
        Log.d("choosePhoto","choosePhoto clicked");
    }
    public void uploadImageToFirebaseStorage(){
        if (uri == null){


                    uri = Uri.parse("android.resource://com.one4all.sumotwo/drawable/sumo1.jpg");



        }

        String fileName = UUID.randomUUID().toString();
        final StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference("/images/"+fileName);


        firebaseStorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageLink = uri.toString();

                        Log.d("name", uri.toString());
                        Users fireBaseUserList = new Users(firebaseAuth.getUid(),mUsernameView.getText().toString(),mEmailView.getText().toString(),imageLink);
                         String  uid = FirebaseAuth.getInstance().getUid();
                        databaseReference.child("userList/"+uid).push().setValue(fireBaseUserList).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                    Log.d("userListFragment list created","userlistCreated");
                            }
                        });

                        Log.d("uid",uid);
                    }
                });
            }
        });
    }
}
















