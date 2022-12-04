package com.example.shareride.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareride.R;

public class RideCompleteFragment extends DialogFragment {

    private static final String TAG = "RideCompleteFragment";
    private AppCompatButton doneBtn;

    public RideCompleteFragment() {
        // Required empty public constructor
    }

    public static RideCompleteFragment display(FragmentManager fragmentManager) {
        RideCompleteFragment rideCompleteFragment = new RideCompleteFragment();
        rideCompleteFragment.show(fragmentManager,TAG);
        return rideCompleteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_complete, container, false);
        initializeComponents(view);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    private void initializeComponents(View view) {
        doneBtn = view.findViewById(R.id.done_button);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        }
    }
}