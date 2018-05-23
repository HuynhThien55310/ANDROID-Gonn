package com.gonnteam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gonnteam.R;
import com.gonnteam.activities.LoginActivity;
import com.gonnteam.adapters.CommentAdapter;
import com.gonnteam.models.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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
    private CommentAdapter adapter;
    private String foodID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_comment, container, false);

        foodID = getActivity().getIntent().getStringExtra("foodID");

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
                .whereEqualTo("foodID", foodID)
                .limit(50);
        adapter = new CommentAdapter(query, getContext());
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

            String text = txtComment.getText().toString();
            if (text == ""){
                return;
            }

            mDocRef = FirebaseFirestore.getInstance().collection("comments");
            comment = new Comment(foodID, currentUser.getUid(), text);
            txtComment.setText("");
            mDocRef.add(comment);
        }
    }


    public FoodCommentFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.getAdapter().startListening();
    }



    @Override
    public void onStop() {
        super.onStop();
        adapter.getAdapter().stopListening();
    }

}
