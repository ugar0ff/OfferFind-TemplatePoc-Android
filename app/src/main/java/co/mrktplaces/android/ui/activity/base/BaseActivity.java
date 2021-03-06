package co.mrktplaces.android.ui.activity.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import co.mrktplaces.android.R;
import co.mrktplaces.android.core.AppOfferFind;
import co.mrktplaces.android.ui.controller.SwitchFragmentListener;
import co.mrktplaces.android.utils.PreferencesUtils;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class BaseActivity extends AppCompatActivity implements SwitchFragmentListener, FragmentManager.OnBackStackChangedListener{

    private boolean isBind;

    @Override
    public void switchFragment(Fragment fragment, boolean add, String tag) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            fm.addOnBackStackChangedListener(this);
            FragmentTransaction tr = fm.beginTransaction();
            if (add) {
                tr.addToBackStack(tag);
            } else {
                try {
                    fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tr.replace(R.id.container, fragment);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showDialog(String message) {
        if (!isFinishing()) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        boolean upShow = getSupportFragmentManager().getBackStackEntryCount() > 0;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(upShow);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("activity create");
        if (PreferencesUtils.getUserToken(this) != null) {
            isBind = bindService(((AppOfferFind) getApplicationContext()).getStreamServiceIntent(), streamServiceConnect, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        if (streamServiceConnect != null && isBind) {
            unbindService(streamServiceConnect);
        }
        Timber.i("activity destroy");
        super.onDestroy();
    }

    public ServiceConnection streamServiceConnect = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Timber.d("ServiceConnection connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Timber.d("ServiceConnection disconnected");
        }
    };
}
