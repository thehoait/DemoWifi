package com.example.hoaht.demowifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ITEM_KEY = "key";

    private Button mBtnEnableWifi;
    private Button mBtnDisableWifi;
    private Button mBtnScanWifi;
    private WifiManager mWifiManager;
    private LinearLayout mLnContainer;
    private RecyclerView mRecyclerView;
    private WifiAdapter mAdapter;
    private List<ScanResult> mResults;
    private int mSize;

    private final List<HashMap<String, String>> mList = new ArrayList<>();
    private final BroadcastReceiver mWifiChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStateBtnWifi(mWifiManager.isWifiEnabled());
        }
    };
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBtnScanWifi.setEnabled(true);
            mResults = mWifiManager.getScanResults();
            mSize = mResults.size();
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(mWifiChangeReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mBtnEnableWifi = (Button) findViewById(R.id.btnEnableWifi);
        mBtnDisableWifi = (Button) findViewById(R.id.btnDisableWifi);
        mBtnScanWifi = (Button) findViewById(R.id.btnScanWifi);
        mLnContainer = (LinearLayout) findViewById(R.id.lnContainer);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new WifiAdapter(mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mBtnEnableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiManager.setWifiEnabled(true);
            }
        });
        mBtnDisableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiManager.setWifiEnabled(false);
            }
        });
        mBtnScanWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnScanWifi.setEnabled(false);
                mList.clear();
                mWifiManager.startScan();
                mSize = mSize - 1;
                while (mSize >= 0) {
                    ScanResult result = mResults.get(mSize);
                    if (!listContainsValue(result.SSID)) {
                        HashMap<String, String> item = new HashMap<>();
                        item.put(ITEM_KEY, result.SSID);
                        mList.add(item);
                    }
                    updateUi();
                    mAdapter.notifyDataSetChanged();
                    mSize--;
                }
            }
        });
    }

    private boolean listContainsValue(String value) {
        for (HashMap<String, String> hashMap : mList) {
            if (hashMap.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    private void updateUi() {
        boolean hasData = mList.size() > 0;
        mRecyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
        mLnContainer.setVisibility(hasData ? View.GONE : View.VISIBLE);
    }

    private void updateStateBtnWifi(boolean isWifiEnable) {
        mBtnEnableWifi.setEnabled(!isWifiEnable);
        mBtnDisableWifi.setEnabled(isWifiEnable);
        mBtnScanWifi.setEnabled(isWifiEnable);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mWifiChangeReceiver);
        super.onDestroy();
    }
}
