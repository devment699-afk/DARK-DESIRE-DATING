package com.dark.desire.dating.ui.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.ChatEventListener;
import com.dark.desire.dating.network.WebSocketManager;
import com.dark.desire.dating.network.models.ChatMessage;
import com.dark.desire.dating.network.models.UserProfile;
import com.dark.desire.dating.ui.ChatActivity;
import com.dark.desire.dating.utils.PrefsManager;

public class MatchFragment extends Fragment implements ChatEventListener {

    private View radarRingOuter, radarRingMid, radarRingInner;
    private TextView tvMatchStatus, tvPreferenceFilter;
    private ProgressBar matchProgress;
    private AppCompatButton btnStartMatching;
    private boolean isSearching = false;

    private ObjectAnimator outerAnim, midAnim, innerAnim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        radarRingOuter = view.findViewById(R.id.radarRingOuter);
        radarRingMid = view.findViewById(R.id.radarRingMid);
        radarRingInner = view.findViewById(R.id.radarRingInner);
        tvMatchStatus = view.findViewById(R.id.tvMatchStatus);
        tvPreferenceFilter = view.findViewById(R.id.tvPreferenceFilter);
        matchProgress = view.findViewById(R.id.matchProgress);
        btnStartMatching = view.findViewById(R.id.btnStartMatching);

        String lookingFor = PrefsManager.getInstance().getLookingFor();
        tvPreferenceFilter.setText("Target: " + lookingFor);

        btnStartMatching.setOnClickListener(v -> {
            if (!isSearching) {
                startMatchingAnimation();
                WebSocketManager.getInstance().startSearching();
            } else {
                stopMatchingAnimation();
                WebSocketManager.getInstance().stopSearching();
            }
        });

        WebSocketManager.getInstance().addListener(this);
        return view;
    }

    private void startMatchingAnimation() {
        isSearching = true;
        btnStartMatching.setText(R.string.stop_matching);
        tvMatchStatus.setText(R.string.searching_status);
        matchProgress.setVisibility(View.VISIBLE);

        outerAnim = createPulseAnim(radarRingOuter, 1500, 0);
        midAnim = createPulseAnim(radarRingMid, 1500, 300);
        innerAnim = createPulseAnim(radarRingInner, 1500, 600);

        outerAnim.start();
        midAnim.start();
        innerAnim.start();
    }

    private void stopMatchingAnimation() {
        isSearching = false;
        btnStartMatching.setText(R.string.start_matching);
        tvMatchStatus.setText("Tap Start to connect with a secret stranger");
        matchProgress.setVisibility(View.INVISIBLE);

        if (outerAnim != null) outerAnim.cancel();
        if (midAnim != null) midAnim.cancel();
        if (innerAnim != null) innerAnim.cancel();

        radarRingOuter.setScaleX(1f); radarRingOuter.setScaleY(1f); radarRingOuter.setAlpha(1f);
        radarRingMid.setScaleX(1f); radarRingMid.setScaleY(1f); radarRingMid.setAlpha(1f);
        radarRingInner.setScaleX(1f); radarRingInner.setScaleY(1f); radarRingInner.setAlpha(1f);
    }

    private ObjectAnimator createPulseAnim(View target, long duration, long startDelay) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.35f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.35f, 1f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.4f, 1f, 0.4f);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(target, scaleX, scaleY, alpha);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        return anim;
    }

    @Override
    public void onConnected() {}

    @Override
    public void onSearching() {}

    @Override
    public void onMatched(UserProfile partner) {
        if (!isAdded()) return;
        stopMatchingAnimation();
        startActivity(new Intent(requireContext(), ChatActivity.class));
    }

    @Override
    public void onMessageReceived(ChatMessage message) {}

    @Override
    public void onPartnerTyping(boolean isTyping) {}

    @Override
    public void onPartnerDisconnected() {}

    @Override
    public void onError(String error) {
        if (!isAdded()) return;
        stopMatchingAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WebSocketManager.getInstance().removeListener(this);
        stopMatchingAnimation();
    }
}
