package com.jdemandre.instartist.Fragments;


import android.content.Intent;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jdemandre.instartist.Controller.UserController;
import com.jdemandre.instartist.EditActivity;
import com.jdemandre.instartist.Model.Publication;
import com.jdemandre.instartist.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "INSTARTIST";
    private RewardedAd rewardedAd;
    private FirebaseAuth mAuth;


    private RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this.getContext(),
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Toast.makeText(getContext(), "New ad loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                Toast.makeText(getContext(), "New ad failed loaded + errcode " + errorCode, Toast.LENGTH_SHORT).show();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        Uri photoUrl = currentUser.getPhotoUrl();
        final ImageView imageView = getView().findViewById(R.id.image_profile);
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(imageView);
        } else {
            Picasso.get().load("https://kooledge.com/assets/default_medium_avatar-57d58da4fc778fbd688dcbc4cbc47e14ac79839a9801187e42a796cbd6569847.png").into(imageView);
        }

        UserController.getUser(currentUser.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getString("profilePic") != null) {
                            Picasso.get().load(document.getString("profilePic")).into(imageView);
                        } else {
                            Picasso.get().load("https://kooledge.com/assets/default_medium_avatar-57d58da4fc778fbd688dcbc4cbc47e14ac79839a9801187e42a796cbd6569847.png").into(imageView);
                        }

                        TextView username = getView().findViewById(R.id.username);
                        username.setText(document.getString("userName"));

                        TextView posts = getView().findViewById(R.id.posts);
                        posts.setText("0");

                        TextView earnings = getView().findViewById(R.id.earnings);
                        double earningsData = (double) document.get("earnings");
                        earnings.setText(String.valueOf(earningsData));
                    } else {
                        Toast.makeText(getContext(), "Error happened... Please try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(getContext(), "Error happened... Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        rewardedAd = createAndLoadRewardedAd();
        getView().findViewById(R.id.adButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd.isLoaded()) {
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            Toast.makeText(getContext(), "New ad opened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            rewardedAd = createAndLoadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            final double rewardAmount = reward.getAmount();
                            UserController.getUser(currentUser.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Double earnings = document.getDouble("earnings");
                                            earnings += rewardAmount;
                                            UserController.updateEarnings(currentUser.getUid(), earnings);
                                            Toast.makeText(ProfileFragment.this.getContext(), "Reward given", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Error happened... Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                        Toast.makeText(getContext(), "Error happened... Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            Toast.makeText(getContext(), "New ad failed to display: errcode: " + errorCode, Toast.LENGTH_SHORT).show();
                        }
                    };
                    rewardedAd.show(getActivity(), adCallback);
                } else {
                    Toast.makeText(getContext(), "Please wait, ad is loading...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getView().findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditActivity.class));
            }
        });

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_user_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Publications")
                .orderBy("id", Query.Direction.DESCENDING)
                .whereEqualTo("author",currentUser.getUid());

        FirestoreRecyclerOptions<Publication> options = new FirestoreRecyclerOptions.Builder<Publication>()
                .setQuery(query, Publication.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Publication, PublicationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PublicationViewHolder publicationViewHolder, int i, @NonNull final Publication publication) {
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
            public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
                return new PublicationViewHolder(view);
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
