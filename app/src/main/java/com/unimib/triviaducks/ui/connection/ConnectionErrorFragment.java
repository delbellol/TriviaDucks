package com.unimib.triviaducks.ui.connection;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.HomeActivity;
import com.unimib.triviaducks.util.NetworkUtil;

public class ConnectionErrorFragment extends Fragment {

    private static final String TAG = com.unimib.triviaducks.ui.connection.ConnectionErrorFragment.class.getSimpleName();

    public ConnectionErrorFragment() {
        // Required empty public constructor
    }

    public static ConnectionErrorFragment newInstance() {
        return new ConnectionErrorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"paperino 1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"paperino2");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG,"paperino3");
        Button tryAgain = view.findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(v -> {
            if (getContext() != null) {
                if (NetworkUtil.isInternetAvailable(getContext())) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    Snackbar.make(view, "Internet not available. Check connetion and then try again", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        Button closeApp = view.findViewById(R.id.closeApp);
        closeApp.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().moveTaskToBack(true);
            }
        });
    }
}