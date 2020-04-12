package com.paper.squeeze.calburn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paper.squeeze.calburn.CustomClass.User;

public class LoginActivity extends AppCompatActivity {

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.clientID))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == 100) {
            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this,getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Wait");
        builder.setMessage("Logging in ...");
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //to load the data if user has data else go to the collection activity
                            db.collection("users").
                                    whereEqualTo("id",user.getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                if (task.getResult().isEmpty()){
                                                    alertDialog.dismiss();
                                                    startActivity(new Intent(LoginActivity.this,CollectionActivity.class));
                                                    finish();
                                                }else{
                                                    try {
                                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                                        User user1 = documentSnapshot.toObject(User.class);
                                                        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putInt("weight", user1.getWeight());
                                                        editor.putInt("height", user1.getHeight());
                                                        editor.putInt("calories", user1.getCalories());
                                                        editor.putInt("age", user1.getAge());
                                                        editor.putString("gender", user1.getGender());
                                                        editor.commit();
                                                        alertDialog.dismiss();
                                                        Toast.makeText(LoginActivity.this, " Sign In Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                        finish();
                                                    }catch (Exception e){
                                                        Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                                        mAuth.signOut();
                                                        alertDialog.dismiss();
                                                    }
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                                alertDialog.dismiss();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    alertDialog.dismiss();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Try again",
                                    Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });
    }
}
