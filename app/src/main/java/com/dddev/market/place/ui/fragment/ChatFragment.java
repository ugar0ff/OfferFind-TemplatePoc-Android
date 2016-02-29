package com.dddev.market.place.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.api.strongloop.MessagesGetRepository;
import com.dddev.market.place.core.api.strongloop.MessagesPostRepository;
import com.dddev.market.place.ui.adapter.ChatAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 29.02.16.
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener{

    private List<Messages.ModelMessages> adapterList;
    private ChatAdapter adapter;
    private int id;
    private final static String BIDS_ID = "bids_id";
    private EditText messageEdit;

    public static ChatFragment newInstance(int id) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BIDS_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
        if (getArguments() != null) {
            id = getArguments().getInt(BIDS_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        view.findViewById(R.id.chat_send_button).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new ChatAdapter(getActivity(), adapterList);
        listView.setAdapter(adapter);
        messageEdit = (EditText) view.findViewById(R.id.message_edit);
        view.findViewById(R.id.chat_send_button).setOnClickListener(this);
        getMessages();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_button:
                String message = messageEdit.getText().toString();
                if (message.length() > 0) {
                    setMessages(message);
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
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
            }
        });
    }

    private void setMessages(String message) {
        final MessagesPostRepository messagesPostRepository = AppOfferFind.getRestAdapter(getActivity()).createRepository(MessagesPostRepository.class);
        messagesPostRepository.createContract();
        messagesPostRepository.messages(message, id, new MessagesPostRepository.MessagesCallback() {
            @Override
            public void onSuccess(Messages.ModelMessages messages) {
                Timber.i("onSuccess response=%s", messages.toString());
                adapterList.add(messages);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
            }
        });
    }
}
