package com.dddev.market.place.ui.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.api.strongloop.MessagesGetRepository;
import com.dddev.market.place.core.api.strongloop.MessagesPostRepository;
import com.dddev.market.place.core.receiver.MessageReceiver;
import com.dddev.market.place.ui.adapter.ChatAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.utils.StaticKeys;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import timber.log.Timber;

/**
 * Created by ugar on 29.02.16.
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener, MessageReceiver.MessageBroadcastListener {

    private ChatAdapter adapter;
    private int id;
    private final static String BIDS_ID = "bids_id";
    private final static String BIDS_ACCESS = "bids_access";
    private EditText messageEdit;
    private boolean accessToWriteMessage;
    private LinearLayout messagesLayout;
    private MessageReceiver messageReceiver;
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
        messageReceiver = new MessageReceiver();
        messageReceiver.setMessageBroadcastListener(this);
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
            public void onSuccess(Messages messages) {
                adapter.clear();
                if (messages != null && messages.getList() != null) {
                    adapter.addAll(messages.getList());
                }
                //                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            listView.smoothScrollToPosition(adapter.getCount() - 1);
//                        }
//                    }, 1000);
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
        final MessagesPostRepository messagesPostRepository = AppOfferFind.getRestAdapter(getActivity()).createRepository(MessagesPostRepository.class);
        messagesPostRepository.createContract();
        messagesPostRepository.messages(message, id, new MessagesPostRepository.MessagesCallback() {
            @Override
            public void onSuccess(Messages.ModelMessages messages) {
                Timber.i("onSuccess response=%s", messages.toString());
                messageEdit.setText("");
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getMessages();
        getActivity().registerReceiver(messageReceiver, new IntentFilter(MessageReceiver.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(messageReceiver);
        super.onPause();
    }

    @Override
    public void onStreamMessage(Messages.ModelMessages message) {
        if (message == null) {
            return;
        }
        if (message.getBidId() != id) {
            return;
        }
        adapter.add(message);
    }

    @Override
    public void onHandleServerRequest(Messages.ModelMessages message) {
        Timber.i("onHandleServerRequest message");
        onStreamMessage(message);
    }

    @Override
    public void onHandleServerRequestError() {
        Timber.e("onHandleServerRequestError message");
    }
}
