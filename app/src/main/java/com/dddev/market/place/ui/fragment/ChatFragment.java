package com.dddev.market.place.ui.fragment;

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
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.api.strongloop.MessagesGetRepository;
import com.dddev.market.place.core.api.strongloop.MessagesPostRepository;
import com.dddev.market.place.ui.adapter.ChatAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.views.eventsource_android.EventSource;
import com.dddev.market.place.ui.views.eventsource_android.EventSourceHandler;
import com.dddev.market.place.ui.views.eventsource_android.MessageEvent;
import com.dddev.market.place.utils.PreferencesUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 29.02.16.
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener {

    private List<Messages.ModelMessages> adapterList;
    private ChatAdapter adapter;
    private int id;
    private final static String BIDS_ID = "bids_id";
    private final static String BIDS_ACCESS = "bids_access";
    private EditText messageEdit;
    private EventSource eventSource;
    private ListView listView;
    private boolean accessToWriteMessage;
    private LinearLayout messagesLayout;

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
        adapterList = new ArrayList<>();
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
        adapter = new ChatAdapter(getActivity(), adapterList);
        listView.setAdapter(adapter);
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
                Timber.i("onSuccess response=%s", messages.toString());
                adapterList.clear();
                if (messages != null && messages.getList() != null) {
                    for (Messages.ModelMessages modelMessages : messages.getList()) {
                        adapterList.add(modelMessages);
                    }
                }
                adapter.notifyDataSetChanged();
                if (adapterList.size() > 0) {
                    listView.setSelection(adapterList.size() - 1);
                }
                streamMessages();
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

    private void streamMessages() {
        Thread eventThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    eventSource = new EventSource(URI.create(AppOfferFind.API + "Messages/streamUpdates?_format=event-stream&access_token=" + PreferencesUtils.getUserToken(getActivity())), new SSEHandler(), null, true);
                    eventSource.connect();
                }
            }
        });
        eventThread.start();
    }

    private class SSEHandler implements EventSourceHandler {

        public SSEHandler() {
        }

        @Override
        public void onConnect() {
            Timber.v("SSE Connected");
        }

        @Override
        public void onMessage(String event, MessageEvent message) {
            Timber.v("SSE Message %s", event);
            Timber.v("SSE Message: %s", message.lastEventId);
            Timber.v("SSE Message: %s", message.data);
            if (message.getMessageData() == null) {
                return;
            }
            if (message.getMessageData().getData() == null) {
                return;
            }
            if (message.getMessageData().getData().getBidId() != id) {
                return;
            }
            adapterList.add(message.getMessageData().getData());
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onComment(String comment) {
            //comments only received if exposeComments turned on
            Timber.v("SSE Comment %s", comment);
        }

        @Override
        public void onError(Throwable t) {
            Timber.v("SSE Error");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            Timber.v("SSE Stacktrace %s", sw.toString());

        }

        @Override
        public void onClosed(boolean willReconnect) {
            Timber.v("SSE Closed reconnect? %s", willReconnect);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMessages();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (eventSource != null && eventSource.isConnected()) {
            eventSource.close();
        }
    }
}
