package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    SendToDevicesActivity send = new SendToDevicesActivity();
    @BindView(R.id.history_button)
    Button _historyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        _historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blocked();
            }
        });

        return view;
    }

    public void blocked() {
        send.sendHistory(getActivity(), new SendToDevicesActivity.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    Log.d("Response:%n %s", result.toString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
