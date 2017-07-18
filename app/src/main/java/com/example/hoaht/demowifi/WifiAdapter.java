package com.example.hoaht.demowifi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * WifiAdapter.
 *
 * @author HoaHT
 */

class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiViewHolder> {
    private final List<HashMap<String, String>> mList;

    WifiAdapter(List<HashMap<String, String>> list) {
        this.mList = list;
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_wifi, parent, false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {
        holder.mTvTitle.setText(mList.get(position).get(MainActivity.ITEM_KEY));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class WifiViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvTitle;

        WifiViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
