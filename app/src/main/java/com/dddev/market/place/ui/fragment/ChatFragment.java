package com.dddev.market.place.ui.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.StreamModel;
import com.dddev.market.place.core.api.strongloop.MessagesGetRepository;
import com.dddev.market.place.core.api.strongloop.MessagesPostRepository;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.ui.adapter.ChatAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.utils.StaticKeys;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import timber.log.Timber;

/**
 * Created by ugar on 29.02.16.
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ChatAdapter adapter;
    private int id;
    private final static String BIDS_ID = "bids_id";
    private final static String BIDS_ACCESS = "bids_access";
    private EditText messageEdit;
    private boolean accessToWriteMessage;
    private LinearLayout messagesLayout;
    private ListView listView;
    SwingBottomInAnimationAdapter swingBottomInAnimationAdapter;

    public static ChatFragment newInstance(int id, boolean accessToWriteMessage) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BIDS_ID, id);
        bundle.putBoolean(BIDS_ACCESS, accessToWriteMessage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(BIDS_ID);
            accessToWriteMessage = getArguments().getBoolean(BIDS_ACCESS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        view.findViewById(R.id.chat_send_button).setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new ChatAdapter(getActivity());
        swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
        swingBottomInAnimationAdapter.setAbsListView(listView);
        listView.setAdapter(swingBottomInAnimationAdapter);
        messageEdit = (EditText) view.findViewById(R.id.message_edit);
        messagesLayout = (LinearLayout) view.findViewById(R.id.message_layout);
        setAccessToWriteMessage(accessToWriteMessage);
        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.MESSAGE_LOADER, null, this);
        return view;
    }

    public void setAccessToWriteMessage(boolean accessToWriteMessage) {
        if (messagesLayout != null) {
            messagesLayout.setVisibility(accessToWriteMessage ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_button:
                String message = messageEdit.getText().toString();
                if (message.length() > 0) {
                    sendMessage(message);
                }
                break;
        }
    }

    private void getMessages() {
        final MessagesGetRepository messagesGetRepository = AppOfferFind.getRestAdapter(getActivity()).createRepository(MessagesGetRepository.class);
        messagesGetRepository.createContract();
        messagesGetRepository.messages(id, new MessagesGetRepository.MessagesCallback() {
            @Override
            public void onSuccess(StreamModel messages) {
                adapter.clear();
                addAdapterHeaderView();
                if (messages != null && messages.getList() != null) {
                    adapter.addAll(messages.getList());
                }
                if (adapter.getCount() > 0) {
                    if (swingBottomInAnimationAdapter.getViewAnimator() != null) {
                        swingBottomInAnimationAdapter.getViewAnimator().setShouldAnimateFromPosition(adapter.getCount() - 1);
                    }
                    listView.smoothScrollToPosition(adapter.getCount() - 1);
                }
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
            }
        });
    }

    private void sendMessage(String message) {
        if (message.replaceAll(" ", "").length() == 0) {
            return;
        }
        final MessagesPostRepository messagesPostRepository = AppOfferFind.getRestAdapter(getActivity()).createRepository(MessagesPostRepository.class);
        messagesPostRepository.createContract();
        messagesPostRepository.messages(message, id, new MessagesPostRepository.MessagesCallback() {
            @Override
            public void onSuccess(StreamModel.ModelMessages messages) {
                Timber.i("onSuccess response=%s", messages.toString());
                messageEdit.setText("");
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
            }
        });
    }

    public void addAdapterHeaderView() {
        adapter.add(0, new StreamModel.ModelMessages());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.MESSAGE_LOADER:
                String[] projection = new String[]{CacheHelper.MESSAGE_ID + " as _id ",
                        CacheHelper.MESSAGE_TEXT,
                        CacheHelper.MESSAGE_SENDER_ID,
                        CacheHelper.MESSAGE_OWNER_ID};
                String selection = CacheHelper.MESSAGE_BID_ID + " = ?";
                String[] selectionArg = new String[]{String.valueOf(id)};
                return new CursorLoader(getActivity(), CacheContentProvider.MESSAGE_URI, projection, selection, selectionArg, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.MESSAGE_LOADER:
                adapter.clear();
                addAdapterHeaderView();
                if (cursor.moveToFirst()) {
                    do {
                        StreamModel.ModelMessages modelMessages = new StreamModel.ModelMessages();
                        modelMessages.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                        modelMessages.setText(cursor.getString(cursor.getColumnIndex(CacheHelper.MESSAGE_TEXT)));
                        modelMessages.setOwnerId(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_OWNER_ID)));
                        modelMessages.setSenderId(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_SENDER_ID)));
                        adapter.add(modelMessages);
                    } while (cursor.moveToNext());
                }
                if (adapter.getCount() > 0) {
                    if (swingBottomInAnimationAdapter.getViewAnimator() != null) {
                        swingBottomInAnimationAdapter.getViewAnimator().setShouldAnimateFromPosition(adapter.getCount() - 1);
                    }
                    listView.smoothScrollToPosition(adapter.getCount() - 1);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }
}
