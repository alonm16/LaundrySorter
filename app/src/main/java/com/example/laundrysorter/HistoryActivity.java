package com.example.laundrysorter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView mRecyclerView;
    public HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mRecyclerView = findViewById(R.id.history_recycler_view);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        FirebaseUser user = mAuth.getCurrentUser();
        CollectionReference handshakesReference = db.collection("users").document(user.getUid()).
                collection("history");
        Query query = handshakesReference.orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<History> options = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        adapter = new HistoryAdapter(options);

        mRecyclerView = findViewById(R.id.history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
