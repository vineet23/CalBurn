package com.paper.squeeze.calburn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paper.squeeze.calburn.CustomClass.User;

import java.util.HashMap;
import java.util.Map;

public class HeightActivity extends AppCompatActivity {

    ImageView back;
    TextInputEditText height;
    FloatingActionButton fab;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        height = findViewById(R.id.height_edit);
        fab = findViewById(R.id.fab);

        final SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sharedPreferences.getInt("height", 0) != Integer.parseInt(height.getText().toString())
                    && Integer.parseInt(height.getText().toString())>5) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HeightActivity.this);
                        builder.setTitle("Wait");
                        builder.setMessage("Updating ...");
                        builder.setCancelable(false);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //to update at firebase and save at preference
                        db.collection("users")
                                .whereEqualTo("id",mUser.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            if (!task.getResult().isEmpty()){
                                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                Map<String, Object> userp = new HashMap<>();
                                                userp.put("height", Integer.parseInt(height.getText().toString()));
                                                db.collection("users")
                                                        .document(documentSnapshot.getId())
                                                        .update(userp)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putInt("height",Integer.parseInt(height.getText().toString()));
                                                                    editor.commit();
                                                                    alertDialog.dismiss();
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                                                    alertDialog.dismiss();
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                                        alertDialog.dismiss();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    } else {
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Please fill the height properly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        height.setText(sharedPreferences.getInt("height",0)+"");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
