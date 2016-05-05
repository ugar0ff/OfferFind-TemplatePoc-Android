package co.mrktplaces.android.ui.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import co.mrktplaces.android.R;
import co.mrktplaces.android.core.api.strongloop.Bids;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.ui.fragment.base.BaseFragment;
import co.mrktplaces.android.utils.StaticKeys;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by ugar on 11.02.16.
 */
public class ProposalFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String PROPOSAL_MODEL = "proposal_model";
    private final static String OPPORTUNITIES_STATUS = "opportunities_status";
    private final static String OPPORTUNITIES_NAME = "opportunities_name";
    private Bids.ModelBids itemModel;
    private String statusOpportunities;
    private String opportunitiesName;
    private ChatFragment chatFragment;
    private TextView price;
    private FrameLayout providerLayout;
    private FrameLayout chatLayout;
    private int chatLayoutHeight;
    private Toolbar toolbar;

    public static ProposalFragment newInstance(Bids.ModelBids itemModel, String statusOpportunities, String opportunitiesName) {
        ProposalFragment fragment = new ProposalFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROPOSAL_MODEL, itemModel);
        bundle.putString(OPPORTUNITIES_STATUS, statusOpportunities);
        bundle.putString(OPPORTUNITIES_NAME, opportunitiesName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemModel = getArguments().getParcelable(PROPOSAL_MODEL);
            statusOpportunities = getArguments().getString(OPPORTUNITIES_STATUS);
            opportunitiesName = getArguments().getString(OPPORTUNITIES_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        if (itemModel.getOwner().getName() != null) {
            title.setText(itemModel.getOwner().getName());
        }
        price = (TextView) view.findViewById(R.id.price);
        price.setText(String.format("$ %s", itemModel.getPrice()));

        setAcceptButtonState();

        ImageView picture = (ImageView) view.findViewById(R.id.picture);
        if (itemModel.getOwner() != null && itemModel.getOwner().getAvatar() != null && !itemModel.getOwner().getAvatar().isEmpty()) {
            Picasso.with(getActivity()).load(itemModel.getOwner().getAvatar()).into(picture);
        } else {
            Picasso.with(getActivity()).load(R.drawable.placeholder_proposal_item).into(picture);
        }

        setFade((ImageView) view.findViewById(R.id.blur));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            title.setTransitionName(String.format("title%s", itemModel.getId()));
            price.setTransitionName(String.format("price%s", itemModel.getId()));
            picture.setTransitionName(String.format("picture%s", itemModel.getId()));
        }

        providerLayout = (FrameLayout) view.findViewById(R.id.provider_layout);
        chatLayout = (FrameLayout) view.findViewById(R.id.container);

        if (toolbar == null) {
            toolbar = toolbarController.getToolbar();
        }

        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.ACCEPT_STATE_LOADER, null, this);
        return view;
    }

    private void setFade(final ImageView blur) {
        blur.post(new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(1000);

                AnimationSet animation = new AnimationSet(false);
                animation.addAnimation(fadeIn);
                blur.setAnimation(animation);
            }
        });
    }

    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (chatLayoutHeight == 0) {
                chatLayoutHeight = chatLayout.getHeight();
            }
            if (chatLayoutHeight <= chatLayout.getHeight()) {
                providerLayout.setVisibility(View.VISIBLE);
                providerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            } else {
                providerLayout.animate().translationY(-providerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                providerLayout.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (opportunitiesName != null) {
            toolbarController.setToolbarTitle(opportunitiesName);
        }
        if (chatLayout != null) {
            chatLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
        }
        toolbar.setY(0);
    }

    @Override
    public void onPause() {
        if (chatLayout != null) {
            chatLayout.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardLayoutListener);
        }
        super.onPause();
    }

    private void setChatFragment(int id) {
        try {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction tr = fm.beginTransaction();
            try {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean accessToWriteMessage = !statusOpportunities.equals(StaticKeys.State.CLOSED);
            if (accessToWriteMessage && statusOpportunities.equals(StaticKeys.State.ACCEPTED)) {
                accessToWriteMessage = itemModel.getState().equals(statusOpportunities);
            }
            chatFragment = ChatFragment.newInstance(id, accessToWriteMessage);
            tr.replace(R.id.container, chatFragment);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAcceptButtonState() {
        boolean access;
        if (itemModel.getState().equals(StaticKeys.State.CLOSED)) {
            access = false;
        } else if (itemModel.getState().equals(StaticKeys.State.ACCEPTED)) {
            access = true;
        } else {
            access = statusOpportunities.equals(itemModel.getState());
        }
        if (chatFragment == null) {
            setChatFragment(itemModel.getId());
        } else {
            chatFragment.setAccessToWriteMessage(access);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.ACCEPT_STATE_LOADER:
                String[] projection = new String[]{CacheHelper.BIDS_ID + " as _id ", CacheHelper.BIDS_STATUS};
                String selection = CacheHelper.BIDS_ID + " = ?";
                String[] selectionArg = new String[]{String.valueOf(itemModel.getId())};
                return new CursorLoader(getActivity(), CacheContentProvider.BIDS_URI, projection, selection, selectionArg, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.ACCEPT_STATE_LOADER:
                if (cursor.moveToFirst()) {
                    itemModel.setState(cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_STATUS)));
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }

}
