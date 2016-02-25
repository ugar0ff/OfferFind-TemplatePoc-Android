package com.dddev.market.place.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dddev.market.place.core.service.UpdateService;
import com.dddev.market.place.utils.StaticKeys;

/**
 * Created by ugar on 24.02.16.
 */
public class UpdateReceiver extends BroadcastReceiver {
    public static final String BROADCAST_ACTION = "com.dddev.market.place.core.receiver.update";

    private UpdateBroadcastListener serverBroadcastListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            UpdateService.RequestStatus request = (UpdateService.RequestStatus) intent.getSerializableExtra(StaticKeys.KEY_REQUEST);
            if (request != null) {
                if (request == UpdateService.RequestStatus.TASK_OK) {
                    serverBroadcastListener.onHandleServerRequest();
                } else {
                    serverBroadcastListener.onHandleServerRequestError();
                }
            } else {
                serverBroadcastListener.onHandleServerRequestError();
            }
        }
    }

    public void setUpdaterBroadcastListener(UpdateBroadcastListener serverBroadcastListener) {
        this.serverBroadcastListener = serverBroadcastListener;
    }

    public interface UpdateBroadcastListener {
        void onHandleServerRequest();
        void onHandleServerRequestError();
    }
}
