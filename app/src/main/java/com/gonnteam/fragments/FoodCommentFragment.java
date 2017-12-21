package com.gonnteam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.activities.LoginActivity;
import com.gonnteam.adapters.CommentFirebaseAdapter;
import com.gonnteam.adapters.FoodFirebaseAdapter;
import com.gonnteam.models.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.facebook.FacebookSdk.getApplicationContext;


public class FoodCommentFragment extends Fragment{
    private Comment comment;
    private EditText txtComment;
    private ImageButton btnSend;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CollectionReference mDocRef;

    private RecyclerView revCommentDiscover;
    private CommentFirebaseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_comment, container, false);
        
        // add controls for post comment
        txtComment = rootView.findViewById(R.id.txtComment);
        btnSend = rootView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
        
        // add controls for show comment
        revCommentDiscover = rootView.findViewById(R.id.revComment);
        Query query = FirebaseFirestore.getInstance()
                .collection("comments")
                .orderBy("postedAt")
                .limit(50);
        adapter = new CommentFirebaseAdapter(query, getContext());
        revCommentDiscover.setAdapter(adapter.getAdapter());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        revCommentDiscover.setLayoutManager(layoutManager);
        return rootView;
    }

    private void postComment() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent login = new Intent(getActivity(),LoginActivity.class);
            startActivity(login);
        }else {
            String foodID = getActivity().getIntent().getStringExtra("foodID");
            String text = txtComment.getText().toString();
            if (text == ""){
                return;
            }

            mDocRef = FirebaseFirestore.getInstance().collection("comments");
            comment = new Comment(foodID, currentUser.getUid(), text);
            mDocRef.add(comment)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            txtComment.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"That bai " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    public FoodCommentFragment() {

    }



}
