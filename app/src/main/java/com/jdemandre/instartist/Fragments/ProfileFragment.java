package com.jdemandre.instartist.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jdemandre.instartist.EditActivity;
import com.jdemandre.instartist.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private RewardedAd rewardedAd;

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Uri photoUrl = currentUser.getPhotoUrl();
        ImageView imageView = getView().findViewById(R.id.image_profile);
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(imageView);
        } else {
            Picasso.get().load("https://kooledge.com/assets/default_medium_avatar-57d58da4fc778fbd688dcbc4cbc47e14ac79839a9801187e42a796cbd6569847.png").into(imageView);
        }


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
                            Toast.makeText(ProfileFragment.this.getContext(), "You win !!!", Toast.LENGTH_SHORT).show();
                            Log.d("jo", String.valueOf(reward.getAmount()));
                            // TODO: handle give reward to the user we want
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

    }



}
