package com.jdemandre.instartist.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jdemandre.instartist.Adapters.PostAdapter;
import com.jdemandre.instartist.Controller.UserController;
import com.jdemandre.instartist.Model.Post;
import com.jdemandre.instartist.Model.Publication;
import com.jdemandre.instartist.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "INSTARTIST";
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private List<String> followingList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Publications")
                .orderBy("id", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Publication> options = new FirestoreRecyclerOptions.Builder<Publication>()
                .setQuery(query, Publication.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Publication, HomeFragment.PublicationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final HomeFragment.PublicationViewHolder publicationViewHolder, int i, @NonNull final Publication publication) {
                publicationViewHolder.setPublicationDescription(publication.getDescription());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                storageRef.child(publication.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        publicationViewHolder.setPublicationImage(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                UserController.getUser(publication.getAuthor()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                publicationViewHolder.setPublicationUsername(document.getString("userName"));
                                publicationViewHolder.setUserImage(document.getString("profilePic"));
                            } else {
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(getContext(), "Error happened... Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @NonNull
            @Override
            public HomeFragment.PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
                return new HomeFragment.PublicationViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private FirestoreRecyclerAdapter<Publication, PublicationViewHolder> adapter;

    private class PublicationViewHolder extends RecyclerView.ViewHolder {
        private View view;

        PublicationViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setPublicationDescription(String description) {
            TextView textView = view.findViewById(R.id.description);
            textView.setText(description);
        }

        void setUserImage(String url) {
            ImageView imageView = view.findViewById(R.id.image_profile);
            if (url != null) {
                Picasso.get().load(url).into(imageView);
            } else {
                Picasso.get().load("https://kooledge.com/assets/default_medium_avatar-57d58da4fc778fbd688dcbc4cbc47e14ac79839a9801187e42a796cbd6569847.png").into(imageView);
            }
        }

        void setPublicationImage(String url) {
            ImageView imageView = view.findViewById(R.id.post_image);
            if (url != null) {
                Picasso.get().load(url).into(imageView);
            } else {
                Picasso.get().load("https://kooledge.com/assets/default_medium_avatar-57d58da4fc778fbd688dcbc4cbc47e14ac79839a9801187e42a796cbd6569847.png").into(imageView);
            }
        }

        void setPublicationUsername(String username) {
            TextView textView = view.findViewById(R.id.username);
            textView.setText(username);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

}
