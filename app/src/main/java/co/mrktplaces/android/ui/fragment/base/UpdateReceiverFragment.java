package co.mrktplaces.android.ui.fragment.base;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import co.mrktplaces.android.core.receiver.UpdateReceiver;

/**
 * Created by ugar on 02.03.16.
 */
public abstract class UpdateReceiverFragment extends BaseFragment implements UpdateReceiver.UpdateBroadcastListener, SwipeRefreshLayout.OnRefreshListener {

    private UpdateReceiver mUpdateReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpdateReceiver = new UpdateReceiver();
        mUpdateReceiver.setUpdaterBroadcastListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mUpdateReceiver, new IntentFilter(UpdateReceiver.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mUpdateReceiver);
        super.onPause();
    }
}
