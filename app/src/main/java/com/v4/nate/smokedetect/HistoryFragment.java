package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";
    HashMap<String, String> params = new HashMap<>();
    String url = "http://192.168.0.107/history.php";
    SendToDevicesActivity send = new SendToDevicesActivity();

    @BindView(R.id.history_button)
    Button _historyButton;
    @BindView(R.id.ll)
    LinearLayout _linearLayout;
    @BindView(R.id.history_list)
    ListView _historyList;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);


        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        _historyList.setAdapter(adapter);

        _historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger();
            }
        });


        return view;
    }

    public void trigger() {
        params.put("user", "1234");
        params.put("history", "1");


        send.queryServer(getActivity(), url, params, new SendToDevicesActivity.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    listItems.add("Detector alarm raised on:" + result.getString("date"));
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, result.toString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
