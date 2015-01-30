package com.bainiaohe.dodo.main.fragments.messages;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bainiaohe.dodo.main.fragments.messages.adapter.ConversationListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.RCloudContext;
import io.rong.imkit.RCloudContext.ConnectionStatusListener;
import io.rong.imkit.Res;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GetGroupInfoProvider;
import io.rong.imkit.common.RongConst.BROADCAST;
import io.rong.imkit.data.DBHelper;
import io.rong.imkit.fragment.ActionBaseFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.model.UIDiscussion;
import io.rong.imkit.model.UIGroup;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.model.UIUserInfo;
import io.rong.imkit.utils.RongToast;
import io.rong.imkit.view.ActionBar;
import io.rong.imkit.view.LoadingDialog;
import io.rong.imkit.view.SelectDialog;
import io.rong.imkit.view.SelectDialog.OnDialogItemViewListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConversationType;
import io.rong.imlib.RongIMClient.DiscussionNotificationMessage;
import io.rong.imlib.RongIMClient.Group;
import io.rong.imlib.RongIMClient.MessageContent;
import io.rong.imlib.RongIMClient.UserInfo;

public class MessageFragment extends ActionBaseFragment implements OnItemClickListener, OnItemLongClickListener, OnScrollListener, ConversationListAdapter.OnGetDataListener {
    public static final String TAG = "MessageFragment";
    public static final String INTENT_PRIVATE_SELECT_PEOPLE = "intent_private_select_people";
    protected static final int HANDLE_NOTIFY_ADAPTER = 1100;
    protected static final int HANDLE_NOTIFY_LOAD_DATA = 1101;
    protected static final int START_CONVERSATION = 1103;
    protected static final int HANDLE_ONCLICK_ITEM = 1104;
    protected static final int START_CONVERSATION_CREATE_SUCCESS = 1105;
    protected static final int HANDLE_SETTING_FUNCTION_SEND_BROADCAST = 1106;
    protected static final int HANDLE_HAS_MESSAGE = 1107;
    protected static final int HANDLE_RE_LOAD_DATA = 1108;
    private static final int GET_DISCUSSION_INFO = 1102;
    protected ConversationListAdapter mConversationListAdapter;
    protected ConversationType mConversationType;
    protected UIConversation mGroupUIConversation;
    private ListView listView;
    private TextView mEmptyView;
    private LoadingDialog mDialog;
    private TextView mConnectStateTextView;
    private int mGroupUnreadMessageCount = 0;

    public MessageFragment() {
    }

    public void onResume() {
        super.onResume();
        if (RCloudContext.getInstance() != null) {
            RCloudContext.getInstance().getRongIMClient();
            RongIMClient.clearNotifications();
            RCloudContext.getInstance().setNotificationNewMessageCount(0);
            RCloudContext.getInstance().clearNotificationUserIdList();
            RCloudContext.getInstance().setConnectionStatusListener(new ConnectionStatusListener() {
                public void onChanged(int code) {
                    MessageFragment.this.setNetStatus(code);
                }
            });
            this.setGroupUnReadMessageCount();
        }

        this.setCurrentConversationTargetId((String) null);
    }

    protected void setGroupUnReadMessageCount() {
        if (RCloudContext.getInstance() != null && RCloudContext.getInstance().getRongIMClient() != null) {
            this.getHandler().post(new Runnable() {
                public void run() {
                    ConversationType[] conversationTypes = new ConversationType[]{ConversationType.GROUP};
                    MessageFragment.this.mGroupUnreadMessageCount = RCloudContext.getInstance().getRongIMClient().getUnreadCount(conversationTypes);
                    if (MessageFragment.this.mConversationListAdapter != null) {
                        int count = MessageFragment.this.mConversationListAdapter.getCount();

                        for (int i = 0; i < count; ++i) {
                            UIConversation uiConversation = (UIConversation) MessageFragment.this.mConversationListAdapter.getItem(i);
                            if (ConversationType.GROUP == uiConversation.getConversationType()) {
                                uiConversation.setUnreadMessageCount(MessageFragment.this.mGroupUnreadMessageCount);
                                MessageFragment.this.mConversationListAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }

                }
            });
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(Res.getInstance(this.getActivity()).layout("fragment_message"), (ViewGroup) null);
        this.listView = (ListView) this.getViewById(view, android.R.id.list);
        this.initAdapter();


        this.listView.setAdapter(this.mConversationListAdapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        this.mEmptyView = (TextView) this.getViewById(view, android.R.id.empty);
        this.listView.setEmptyView(this.mEmptyView);
        this.mConnectStateTextView = (TextView) this.getViewById(view, android.R.id.text1);
        this.mConversationListAdapter.setOnGetDataListener(this);
        if (this.getActionBar() != null) {
            this.setActionBar(this.getActionBar());
        }

        return view;
    }

    protected void initAdapter() {
        this.mConversationListAdapter = new ConversationListAdapter(this.getActivity(), false);
    }

    protected void setActionBar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.getTitleTextView().setText(Res.getInstance(this.getActivity()).string("conversation_list_action_bar_title"));
            View view = LayoutInflater.from(this.getActivity()).inflate(Res.getInstance(this.getActivity()).layout("rc_action_bar_conversation_list_select"), this.getActionBar(), false);
            ImageView peopleView = (ImageView) this.getViewById(view, "rc_conversation_list_select_image");
            peopleView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    RongIM.getInstance().startFriendSelect(MessageFragment.this.getActivity());
                }
            });
            actionBar.addView(peopleView);
            actionBar.setOnBackClick(new OnClickListener() {
                public void onClick(View v) {
                    MessageFragment.this.getActivity().finish();
                }
            });
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.listView.setOnScrollListener(this);
        this.mConversationListAdapter.setListView(this.listView);
        this.getHandler().post(new Runnable() {
            public void run() {
                MessageFragment.this.resetData();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation conversation = this.mConversationListAdapter.getItem(position);
        this.mConversationType = conversation.getConversationType();
        Uri uri;
        if (this.mConversationType == ConversationType.GROUP) {
            this.setCurrentConversationTargetId((String) null);
            uri = Uri.parse("rong://" + this.getActivity().getApplicationInfo().packageName).buildUpon().appendPath("conversationgrouplist").build();
        } else {
            this.setCurrentConversationTargetId(conversation.getTargetId());
            String title = "";
            if (!TextUtils.isEmpty(conversation.getConversationTitle())) {
                title = conversation.getConversationTitle();
            } else if (conversation.getConversationType() == ConversationType.DISCUSSION) {
                if (conversation.getUiDiscussion() != null) {
                    title = conversation.getUiDiscussion().getName();
                }
            } else if (conversation.getConversationType() == ConversationType.PRIVATE && conversation.getUserInfo() != null) {
                title = conversation.getUserInfo().getName();
            }

            uri = Uri.parse("rong://" + this.getActivity().getApplicationInfo().packageName).buildUpon().appendPath("conversation").appendPath(conversation.getConversationType().getName().toLowerCase()).appendQueryParameter("targetId", conversation.getTargetId()).appendQueryParameter("title", title).build();
        }

        this.getActivity().startActivity(new Intent("android.intent.action.VIEW", uri));
    }

    private final void removeConversation(String conversationId) {
        int count = this.mConversationListAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            UIConversation conversation = (UIConversation) this.mConversationListAdapter.getItem(i);
            if (conversation != null && conversation.getTargetId().equals(conversationId)) {
                this.mConversationListAdapter.remove(i);
                this.mConversationListAdapter.notifyDataSetChanged();
                break;
            }
        }

    }

    protected void resetData() {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                ArrayList list = DBHelper.getInstance().getConversationList();
                if (list != null && list.size() != 0) {
                    boolean isHaveGroup = false;
                    final ArrayList conversations = new ArrayList();
                    if (MessageFragment.this.mGroupUIConversation != null) {
                        MessageFragment.this.mGroupUIConversation.setUnreadMessageCount(0);
                    }

                    Iterator var4 = list.iterator();

                    while (var4.hasNext()) {
                        UIConversation uiConversation = (UIConversation) var4.next();

                        if (ConversationType.GROUP == uiConversation.getConversationType()) {
                            if (isHaveGroup) {
                                MessageFragment.this.mGroupUIConversation.setUnreadMessageCount(MessageFragment.this.mGroupUIConversation.getUnreadMessageCount() + uiConversation.getUnreadMessageCount());
                            } else {
                                MessageFragment.this.mGroupUIConversation = uiConversation;
                                conversations.add(uiConversation);
                                isHaveGroup = true;
                            }
                        } else {
                            conversations.add(uiConversation);
                        }
                    }

                    MessageFragment.this.getHandler().post(new Runnable() {
                        public void run() {
                            MessageFragment.this.mConversationListAdapter.removeAll();
                            MessageFragment.this.clearListCache();
                            MessageFragment.this.mConversationListAdapter.addData(conversations);
                            MessageFragment.this.mConversationListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void receiveData(Intent intent) {
        String action = intent.getAction();
        int leftMessageCount = intent.getIntExtra("left_message_count", 0);
        int messageCount = intent.getIntExtra("message_count", 0);
        if (messageCount > 20 && leftMessageCount == 0) {
            this.getHandler().obtainMessage(1108).sendToTarget();
        } else {
            if (BROADCAST.ACTION_P2P_MESSAGE_RECEIVE.equals(action) || BROADCAST.ACTION_DISCUSSION_MESSAGE_RECEIVE.equals(action) || BROADCAST.ACTION_GROUP_MESSAGE_RECEIVE.equals(action) || BROADCAST.ACTION_CHATROOM_MESSAGE_RECEIVE.equals(action) || BROADCAST.ACTION_SYSTEM_MESSAGE_RECEIVE.equals(action)) {
                UIMessage message = (UIMessage) intent.getParcelableExtra("message_obj");
                String targetId = message.getTargetId();
                if (RCloudContext.getInstance() != null && RCloudContext.getInstance().getCurrentTargetId() != null && RCloudContext.getInstance().getCurrentTargetId().equals(targetId)) {
                    return;
                }

                this.getHandler().obtainMessage(1107, message).sendToTarget();
            }

        }
    }

    protected void setNetStatus(final int status) {
        if (this.getActivity() != null) {
            Runnable runnable = new Runnable() {
                public void run() {
                    switch (status) {
                        case -9:
                            MessageFragment.this.mConnectStateTextView.setText(Res.getInstance(MessageFragment.this.getActivity()).string("conntect_state_prompt_disconnect"));
                            MessageFragment.this.mConnectStateTextView.setVisibility(View.VISIBLE);
                            break;
                        case -1:
                            MessageFragment.this.mConnectStateTextView.setText(Res.getInstance(MessageFragment.this.getActivity()).string("conntect_state_prompt_unknow_error"));
                            MessageFragment.this.mConnectStateTextView.setVisibility(View.VISIBLE);
                            break;
                        case 0:
                            MessageFragment.this.mConnectStateTextView.setVisibility(View.GONE);
                            break;
                        case 1:
                            MessageFragment.this.mConnectStateTextView.setText(Res.getInstance(MessageFragment.this.getActivity()).string("conntect_state_prompt_network_unavailable"));
                            MessageFragment.this.mConnectStateTextView.setVisibility(View.VISIBLE);
                            break;
                        case 6:
                            MessageFragment.this.mConnectStateTextView.setText(Res.getInstance(MessageFragment.this.getActivity()).string("conntect_state_prompt_other_device_login"));
                            MessageFragment.this.mConnectStateTextView.setVisibility(View.VISIBLE);
                    }

                }
            };
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run();
            } else {
                this.getHandler().post(runnable);
            }

        }
    }

    public void receivePageIntent(Intent intent) {
        String action = intent.getAction();
        if (!BROADCAST.ACTION_REMOVE_CONVERSATION_FOR_DELETE_FRIEND.equals(action) && !BROADCAST.ACTION_RESET_DATA_FOR_CONVERSION_LIST.equals(action)) {
            if (BROADCAST.ACTION_BUNDLE_IO_RONG_IMKIT_CONVERSATION.equals(action)) {
                this.resetData();
                this.mConversationType = null;
                this.setCurrentConversationTargetId((String) null);
            } else if (BROADCAST.ACTION_BUNDLE_IO_RONG_IMKIT_CONVERSATION_SETTING.equals(action)) {
                this.getHandler().obtainMessage(1106, intent).sendToTarget();
            }

        } else {
            this.resetData();
        }
    }

    public void registerActions(List<String> actions) {
        actions.add(BROADCAST.ACTION_P2P_MESSAGE_RECEIVE);
        actions.add(BROADCAST.ACTION_GROUP_MESSAGE_RECEIVE);
        actions.add(BROADCAST.ACTION_DISCUSSION_MESSAGE_RECEIVE);
        actions.add(BROADCAST.ACTION_SYSTEM_MESSAGE_RECEIVE);
        super.registerActions(actions);
    }

    public void registerBundleActions(List<String> actions) {
        actions.add(BROADCAST.ACTION_BUNDLE_IO_RONG_IMKIT_CONVERSATION);
        actions.add(BROADCAST.ACTION_BUNDLE_IO_RONG_IMKIT_CONVERSATION_SETTING);
        actions.add(BROADCAST.ACTION_REMOVE_CONVERSATION_FOR_DELETE_FRIEND);
        actions.add(BROADCAST.ACTION_RESET_DATA_FOR_CONVERSION_LIST);
        super.registerBundleActions(actions);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, final int positionArg, long id) {
        final SelectDialog mSelectDialog = new SelectDialog(this.getActivity());
        final UIConversation conversation = (UIConversation) this.mConversationListAdapter.getItem(positionArg);
        if (conversation != null) {
            mSelectDialog.setTitle(conversation.getConversationTitle());
        }

        if (!conversation.isTop()) {
            mSelectDialog.setFristLineContent("dialog_converastion_istop");
        } else {
            mSelectDialog.setFristLineContent("dialog_converastion_istop_cancel");
        }

        mSelectDialog.setSecondLineContent("dialog_converastion_remove");
        mSelectDialog.setOnDialogItemViewListener(new OnDialogItemViewListener() {
            public void OnDialogItemViewClick(View view, int position) {
                if (position == 0) {
                    DBHelper.getInstance().setTop(conversation.getConversationType(), conversation.getTargetId(), !conversation.isTop());
                    if (conversation.isTop()) {
                        Toast.makeText(MessageFragment.this.getActivity(), MessageFragment.this.getResources().getString(Res.getInstance(MessageFragment.this.getActivity()).string("conversation_list_set_top_cancel")), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageFragment.this.getActivity(), MessageFragment.this.getResources().getString(Res.getInstance(MessageFragment.this.getActivity()).string("conversation_list_set_top")), Toast.LENGTH_SHORT).show();
                    }

                    MessageFragment.this.resetData();
                } else if (position == 1) {
                    String targetId = null;
                    if (conversation != null) {
                        targetId = conversation.getTargetId();
                        if (!TextUtils.isEmpty(targetId)) {
                            DBHelper.getInstance().removeConversation(conversation.getConversationType(), targetId);
                            MessageFragment.this.mConversationListAdapter.remove(positionArg);
                            MessageFragment.this.mConversationListAdapter.notifyDataSetChanged();
                        }
                    }
                }

                mSelectDialog.dismiss();
            }
        });
        mSelectDialog.show();
        return true;
    }

    protected void rongHandleMessage(Message msg) {
        if (msg.what == 1100) {
            this.mConversationListAdapter.notifyDataSetChanged();
        } else if (msg.what == 1101) {
            this.resetData();
            if (this.mDialog != null) {
                this.mDialog.dismiss();
            }
        } else {
            final UIConversation conversation;
            if (msg.what == 1102) {
                UIDiscussion message = (UIDiscussion) msg.obj;
                conversation = this.getUIConversation(message.getId());
                conversation.setUiDiscussion(message);
                conversation.setConversationTitle(message.getName());
                this.mConversationListAdapter.notifyDataSetChanged();
            } else {
                Intent message1;
                if (msg.what == 1103) {
                    message1 = (Intent) msg.obj;
                    ArrayList conversation2 = message1.getParcelableArrayListExtra("extra_users");
                    int isQuitDiscussion = message1.getIntExtra("intent_private_select_people", 0);
                    ConversationType isSetTopConversation = null;
                    if (isQuitDiscussion > 0) {
                        isSetTopConversation = ConversationType.setValue(isQuitDiscussion);
                    }

                    if (conversation2.size() <= 1 && (isSetTopConversation == null || isSetTopConversation != ConversationType.DISCUSSION)) {
                        if (conversation2.size() == 1) {
                            UIConversation discussionName1 = new UIConversation();
                            discussionName1.setConversationTitle(((UserInfo) conversation2.get(0)).getName());
                            discussionName1.setTargetId(((UserInfo) conversation2.get(0)).getUserId());
                            discussionName1.setConversationType(ConversationType.PRIVATE);
                            this.startConversation(discussionName1);
                            this.setCurrentConversationTargetId(((UserInfo) conversation2.get(0)).getUserId());
                        }
                    } else {
                        ArrayList discussionName = new ArrayList(conversation2.size());
                        ArrayList createDiscussion = new ArrayList(10);
                        Iterator isClearMessages = conversation2.iterator();

                        while (isClearMessages.hasNext()) {
                            UserInfo conversation1 = (UserInfo) isClearMessages.next();
                            discussionName.add(conversation1.getUserId());
                            if (createDiscussion.size() < 10) {
                                createDiscussion.add(conversation1.getName());
                            }
                        }

                        UserInfo isClearMessages1 = this.getCurrentUserInfo();
                        if (isClearMessages1 != null && !TextUtils.isEmpty(isClearMessages1.getName())) {
                            createDiscussion.add(isClearMessages1.getName());
                        }

                        Intent conversation4 = new Intent(BROADCAST.ACTION_DISCUSSION_CREATE);
                        conversation4.putStringArrayListExtra("multi_talk_id_array", discussionName);
                        conversation4.putExtra("multi_talk_name", TextUtils.join(",", createDiscussion));
                        this.mDialog = new LoadingDialog(this.getActivity());
                        this.mDialog.setText(Res.getInstance(this.getActivity()).string("discussion_create_loading_title"));
                        this.mDialog.show();
                        this.sendAction(conversation4, new ActionCallback() {
                            public void callback(Intent intentArg) {
                                MessageFragment.this.mDialog.dismiss();
                                boolean isSuccess = intentArg.getBooleanExtra("intent_api_operation_status", false);
                                if (isSuccess) {
                                    String targetId = intentArg.getStringExtra("multi_talk_id");
                                    String discussionName = intentArg.getStringExtra("multi_talk_name");
                                    MessageFragment.this.setCurrentConversationTargetId(targetId);
                                    UIConversation conversation = new UIConversation();
                                    conversation.setTargetId(targetId);
                                    conversation.setConversationType(ConversationType.DISCUSSION);
                                    conversation.setConversationTitle(discussionName);
                                    MessageFragment.this.newMessageSetTop(conversation);
                                    MessageFragment.this.getHandler().obtainMessage(1105, conversation).sendToTarget();
                                } else {
                                    if (MessageFragment.this.mDialog != null && MessageFragment.this.mDialog.isShowing()) {
                                        MessageFragment.this.mDialog.dismiss();
                                    }

                                    RongToast.toast(MessageFragment.this.getActivity(), Res.getInstance(MessageFragment.this.getActivity()).string("discussion_create_failure"));
                                }

                            }
                        });
                    }
                } else if (msg.what == 1104) {
                    message1 = (Intent) msg.obj;
                    conversation = (UIConversation) message1.getParcelableExtra("extra_conversation");
                    this.mWorkHandler.post(new Runnable() {
                        public void run() {
                            UIConversation conversationTemp = DBHelper.getInstance().getConversation(conversation.getConversationType(), conversation.getTargetId());
                            if (conversationTemp != null && !TextUtils.isEmpty(conversationTemp.getTargetId())) {
                                final UIMessage uiMessage = new UIMessage();
                                uiMessage.setContent(conversationTemp.getLatestMessage());
                                uiMessage.setTargetId(conversationTemp.getTargetId());
                                uiMessage.setSentTime(conversationTemp.getSentTime());
                                uiMessage.setConversationType(conversationTemp.getConversationType());
                                uiMessage.setDraft(conversationTemp.getDraft());
                                uiMessage.setSenderUserId(conversationTemp.getSenderUserId());
                                uiMessage.setSentStatus(conversationTemp.getSentStatus());
                                MessageFragment.this.getHandler().post(new Runnable() {
                                    public void run() {
                                        MessageFragment.this.hasNewMessage(uiMessage, false, false);
                                    }
                                });
                            }
                        }
                    });
                } else if (msg.what == 1105) {
                    UIConversation message2 = (UIConversation) msg.obj;
                    this.startConversation(message2);
                } else if (msg.what == 1106) {
                    message1 = (Intent) msg.obj;
                    String conversation3 = message1.getStringExtra("target_id");
                    boolean isQuitDiscussion1 = message1.getBooleanExtra("intent_quit_discussion_close_page", false);
                    int isSetTopConversation1 = message1.getIntExtra("intent_set_top_conversation_success", -1);
                    String discussionName2 = message1.getStringExtra("intent_update_name_discussion");
                    String createDiscussion1 = message1.getStringExtra("intent_create_discussion_success");
                    boolean isClearMessages2 = message1.getBooleanExtra("intent_clear_message_success", false);
                    UIConversation conversation5 = this.getUIConversation(conversation3);
                    if (conversation5 != null) {
                        if (!TextUtils.isEmpty(createDiscussion1)) {
                            this.newMessageSetTop(conversation5);
                            return;
                        }

                        if (isQuitDiscussion1) {
                            this.mConversationListAdapter.remove(conversation5);
                            this.mConversationListAdapter.notifyDataSetChanged();
                            return;
                        }

                        UIConversation conversationTemp = DBHelper.getInstance().getConversation(conversation5.getConversationType(), conversation5.getTargetId());
                        if (conversationTemp != null) {
                            conversation5.setLatestMessage(conversationTemp.getLatestMessage());
                            conversation5.setSentTime(conversationTemp.getSentTime());
                        }

                        if (!TextUtils.isEmpty(discussionName2)) {
                            if (conversation5 != null && conversation5.getUiDiscussion() != null) {
                                UIDiscussion uiConversation = conversation5.getUiDiscussion();
                                uiConversation.setName(discussionName2);
                            }

                            conversation5.setConversationTitle(discussionName2);
                            this.mConversationListAdapter.notifyDataSetChanged();
                        }

                        UIConversation uiConversation1;
                        if (isClearMessages2) {
                            uiConversation1 = this.getUIConversation(conversation3);
                            if (uiConversation1 != null) {
                                uiConversation1.setLatestMessage((MessageContent) null);
                                this.mConversationListAdapter.notifyDataSetChanged();
                            }
                        }

                        if (isSetTopConversation1 != -1) {
                            if (isSetTopConversation1 == 1) {
                                uiConversation1 = this.getUIConversation(conversation5.getTargetId());
                                uiConversation1.setTop(true);
                                int count = this.mConversationListAdapter.getCount();
                                if (count > 1) {
                                    this.removeConversation(conversation5.getTargetId());
                                    this.mConversationListAdapter.addItem(0, uiConversation1);
                                }

                                this.mConversationListAdapter.notifyDataSetChanged();
                            } else {
                                this.resetData();
                            }
                        }
                    }
                } else if (msg.what == 990001) {
                    this.mConversationListAdapter.notifyDataSetChanged();
                } else if (msg.what == 990003) {
                    if (msg.obj instanceof Group) {
                        Group message3 = (Group) msg.obj;
                        conversation = this.getUIConversation(message3.getId());
                        if (conversation != null) {
                            conversation.setUiGroup(new UIGroup(message3));
                            conversation.setConversationTitle(message3.getName());
                            this.mConversationListAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (msg.what == 1107) {
                    if (msg.obj instanceof UIMessage) {
                        UIMessage message4 = (UIMessage) msg.obj;
                        this.hasNewMessage(message4, true, false);
                    }
                } else if (msg.what == 1108) {
                    this.resetData();
                }
            }
        }

    }

    private void startConversation(UIConversation conversation) {
        String title = "";
        if (!TextUtils.isEmpty(conversation.getConversationTitle())) {
            title = conversation.getConversationTitle();
        }

        Uri uri = Uri.parse("rong://" + this.getActivity().getApplicationInfo().packageName).buildUpon().appendPath("conversation").appendPath(conversation.getConversationType().getName().toLowerCase()).appendQueryParameter("targetId", conversation.getTargetId()).appendQueryParameter("title", title).build();
        this.getActivity().startActivity(new Intent("android.intent.action.VIEW", uri));
    }

    protected boolean wrapGroupCoversation(UIMessage message) {
        if (ConversationType.GROUP == message.getConversationType()) {
            if (this.mGroupUIConversation != null) {
                if (this.mGroupUIConversation.getUiGroup() != null && !this.mGroupUIConversation.getUiGroup().getId().equals(message.getTargetId())) {
                    this.mGroupUIConversation.setUiGroup((UIGroup) null);
                }

                this.mGroupUIConversation.setSenderUserName((String) null);
                this.mGroupUIConversation.setUserInfo(message.getUserInfo());
                this.mGroupUIConversation.setTextMessageContent((SpannableStringBuilder) null);
                this.mGroupUIConversation.setTargetId(message.getTargetId());
                this.mGroupUIConversation.setLatestMessage(message.getContent());
                this.mGroupUIConversation.setSentTime(message.getSentTime());
                this.mGroupUIConversation.setConversationType(ConversationType.GROUP);
                this.mGroupUIConversation.setUnreadMessageCount(this.mGroupUIConversation.getUnreadMessageCount() + 1);
                this.mGroupUIConversation.setSenderUserId(message.getSenderUserId());
                this.mConversationListAdapter.notifyDataSetChanged();
            } else {
                this.mGroupUIConversation = new UIConversation();
                this.mGroupUIConversation.setLatestMessage(message.getContent());
                this.mGroupUIConversation.setConversationTitle(this.getString(Res.getInstance(this.getActivity()).string("rc_group_conversation_list_name")));
                this.mGroupUIConversation.setSentTime(message.getSentTime());
                this.mGroupUIConversation.setUnreadMessageCount(1);
                this.mGroupUIConversation.setTargetId(message.getTargetId());
                this.mGroupUIConversation.setConversationType(ConversationType.GROUP);
                this.mConversationListAdapter.addItem(0, this.mGroupUIConversation);
                this.mConversationListAdapter.notifyDataSetChanged();
            }

            return true;
        } else {
            return false;
        }
    }

    protected void hasNewMessage(UIMessage message, boolean isNewMessage, boolean isGroup) {
        if (!isGroup) {
            boolean targetId = this.wrapGroupCoversation(message);
            if (targetId) {
                return;
            }
        } else if (message != null && message.getConversationType() != ConversationType.GROUP) {
            return;
        }

        String var11 = message.getTargetId();
        int count = this.mConversationListAdapter.getCount();
        boolean isExit = false;
        int isNotExitTopPosition = 0;

        while (true) {
            UIConversation conversation;
            if (isNotExitTopPosition < count) {
                conversation = (UIConversation) this.mConversationListAdapter.getItem(isNotExitTopPosition);
                if (!conversation.getTargetId().equals(var11)) {
                    ++isNotExitTopPosition;
                    continue;
                }

                conversation.setDraft(message.getDraft());
                conversation.setSentTime(message.getSentTime());
                conversation.setLatestMessage(message.getContent());
                conversation.setTextMessageContent((SpannableStringBuilder) null);
                conversation.setLatestMessage(message.getContent());
                conversation.setSentStatus(message.getSentStatus());
                conversation.setConversationType(message.getConversationType());
                conversation.setSenderUserName((String) null);
                conversation.setUserInfo((UIUserInfo) null);
                conversation.setSenderUserId(message.getSenderUserId());
                conversation.setOperator((UIUserInfo) null);
                conversation.setOperatored((UIUserInfo) null);
                if (conversation.getReceivedTime() > message.getReceivedTime() || conversation.getSentTime() > message.getSentTime()) {
                    this.resetData();
                    return;
                }

                if ((conversation.getReceivedTime() == message.getReceivedTime() || conversation.getSentTime() == message.getSentTime()) && !isNewMessage) {
                    conversation.setUnreadMessageCount(0);
                    this.mConversationListAdapter.notifyDataSetChanged();
                    return;
                }

                if (isNewMessage) {
                    if (!(conversation.getLatestMessage() instanceof DiscussionNotificationMessage)) {
                        conversation.setUnreadMessageCount(conversation.getUnreadMessageCount() + 1);
                    }
                } else {
                    conversation.setUnreadMessageCount(0);
                }

                if (conversation.isTop()) {
                    if (isNotExitTopPosition != 0) {
                        this.mConversationListAdapter.remove(isNotExitTopPosition);
                        this.mConversationListAdapter.addItem(0, conversation);
                    }
                } else {
                    for (int uiConversation = 0; uiConversation < count; ++uiConversation) {
                        UIConversation uiConversation1 = (UIConversation) this.mConversationListAdapter.getItem(uiConversation);
                        if (!uiConversation1.isTop()) {
                            this.mConversationListAdapter.remove(isNotExitTopPosition);
                            this.mConversationListAdapter.addItem(uiConversation, conversation);
                            break;
                        }
                    }
                }

                this.getHandler().obtainMessage(1100).sendToTarget();
                isExit = true;
            }

            if (!isExit) {
                isNotExitTopPosition = -1;

                for (int var12 = 0; var12 < count; ++var12) {
                    UIConversation var13 = (UIConversation) this.mConversationListAdapter.getItem(var12);
                    if (!var13.isTop()) {
                        isNotExitTopPosition = var12;
                        break;
                    }
                }

                conversation = message.toConversation();
                if (!isNewMessage) {
                    conversation.setSentStatus(message.getSentStatus());
                    conversation.setUnreadMessageCount(0);
                } else if (!(conversation.getLatestMessage() instanceof DiscussionNotificationMessage)) {
                    conversation.setUnreadMessageCount(1);
                }

                if (isNotExitTopPosition >= 0) {
                    this.mConversationListAdapter.addItem(isNotExitTopPosition, conversation);
                } else {
                    this.mConversationListAdapter.addItem(count, conversation);
                }

                this.mConversationListAdapter.notifyDataSetChanged();
            }

            return;
        }
    }

    private final void newMessageSetTop(UIConversation conversation) {
        if (this.mConversationListAdapter != null && conversation != null) {
            int count = this.mConversationListAdapter.getCount();
            if (count <= 0) {
                this.mConversationListAdapter.addData(conversation);
            } else {
                for (int i = 0; i < count; ++i) {
                    UIConversation uiConversation = (UIConversation) this.mConversationListAdapter.getItem(i);
                    if (!uiConversation.isTop()) {
                        this.mConversationListAdapter.addItem(i, conversation);
                        break;
                    }
                }
            }

            this.mConversationListAdapter.notifyDataSetChanged();
        }

    }

    public boolean onBackPressed() {
        return false;
    }

    public void getDiscussionInfo(int position, String discusstionId) {
        this.getDiscussionInfo(discusstionId, position);
    }

    private void getDiscussionInfo(final String discusstionId, final int position) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    Log.d("MessageFragment", "*************Looper.myLooper() ==Looper.getMainLooper()*******getDiscussionInfo***");
                }

                MessageFragment.this.getDiscussionInfo(discusstionId, new GetDiscussionInfoCallback() {
                    public void onSuccess(UIDiscussion discussion) {
                        if (discussion != null && !TextUtils.isEmpty(discussion.getName())) {
                            MessageFragment.this.getHandler().obtainMessage(1102, position, 0, discussion).sendToTarget();
                        }

                    }

                    public void onError() {
                        UIDiscussion discussion = new UIDiscussion();
                        discussion.setId(discusstionId);
                        discussion.setName(MessageFragment.this.getString(Res.getInstance(MessageFragment.this.getActivity()).string("default_discussion_name")));
                        MessageFragment.this.getHandler().obtainMessage(1102, position, 0, discussion).sendToTarget();
                    }
                });
            }
        });
    }

    public void getUserInfo(int position, final String userId, final String targetId, final ConversationType conversationType) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    Log.d("MessageFragment", "*************Looper.myLooper() ==Looper.getMainLooper()*******getGroupInfo***");
                }

                String tempUserId = userId;
                if (conversationType != null && conversationType == ConversationType.PRIVATE) {
                    tempUserId = targetId;
                }
                Log.d("ConversationListAdapter", "startgetUserInfo");
                MessageFragment.this.getUserInfo(tempUserId, new GetUserInfoCallback() {
                    public void onSuccess(UIUserInfo user) {
                        if (user != null) {
                            UIConversation uiConversation = new UIConversation();
                            uiConversation.setTargetId(targetId);
                            uiConversation.setUserInfo(user);
                            uiConversation.setConversationType(conversationType);
                            MessageFragment.this.setUserInfoForConversation(uiConversation);
                            MessageFragment.this.getHandler().obtainMessage(990001, uiConversation).sendToTarget();
                            Log.d("ConversationListAdapter", "success");
                        }

                    }

                    public void onExist(Object object) {
                        if (object != null) {
                            UIConversation uiConversation = new UIConversation();
                            uiConversation.setTargetId(targetId);
                            uiConversation.setConversationType(conversationType);
                            if (object instanceof UIUserInfo) {
                                uiConversation.setUserInfo((UIUserInfo) object);
                            }

                            MessageFragment.this.setUserInfoForConversation(uiConversation);
                            MessageFragment.this.getHandler().obtainMessage(990001, uiConversation).sendToTarget();
                            Log.d("ConversationListAdapter", "onExist");
                        }

                    }

                    public void onError() {
                        UIUserInfo user = new UIUserInfo(userId, userId, "");
                        UIConversation uiConversation = new UIConversation();
                        uiConversation.setTargetId(targetId);
                        uiConversation.setUserInfo(user);
                        uiConversation.setConversationType(conversationType);
                        MessageFragment.this.setUserInfoForConversation(uiConversation);
                        MessageFragment.this.getHandler().obtainMessage(990001, uiConversation).sendToTarget();
                        Log.d("ConversationListAdapter", "onError");
                    }
                });
                Log.d("ConversationListAdapter", "endGetUserInfo");
            }
        });
    }

    public void getGroupInfo(final String groupId) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    Log.d("MessageFragment", "*************Looper.myLooper() ==Looper.getMainLooper()*******getGroupInfo***");
                }

                GetGroupInfoProvider provider = null;
                if (RCloudContext.getInstance() != null) {
                    provider = RCloudContext.getInstance().getGetGroupInfoProvider();
                }

                if (provider != null) {
                    Group group = provider.getGroupInfo(groupId);
                    if (group == null || TextUtils.isEmpty(group.getName())) {
                        group = new Group(groupId, groupId, "groupId");
                    }

                    MessageFragment.this.getHandler().obtainMessage(990003, group).sendToTarget();
                }

            }
        });
    }

    protected UIConversation getUIConversation(String targetId) {
        int count = this.mConversationListAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            UIConversation conversation = (UIConversation) this.mConversationListAdapter.getItem(i);
            if (conversation != null && conversation.getTargetId().equals(targetId)) {
                return conversation;
            }
        }

        return null;
    }

    private final void setUserInfoForConversation(UIConversation uiConversation) {
        int count = this.mConversationListAdapter.getCount();
        UIUserInfo uiUserInfo = uiConversation.getUserInfo();
        Log.d("ConversationListAdapter", "start!" + count);
        for (int i = 0; i < count; ++i) {


            UIConversation conversationTemp = (UIConversation) this.mConversationListAdapter.getItem(i);
            if (uiUserInfo != null && conversationTemp != null) {
                if (conversationTemp.getConversationType() != ConversationType.PRIVATE && conversationTemp.getConversationType() != ConversationType.SYSTEM) {
                    if (uiUserInfo.getUserId().equals(conversationTemp.getSenderUserId())) {
                        conversationTemp.setSenderUserName(uiUserInfo.getName());
                    }
                } else {
                    Log.d("ConversationListAdapter", "setUserInfo-------conversation.getConversationType():" + conversationTemp.getConversationType());
                    if (uiUserInfo.getUserId().equals(conversationTemp.getTargetId())) {
                        conversationTemp.setUserInfo(uiUserInfo);
                        conversationTemp.setConversationTitle(uiUserInfo.getName());
                    }
                }

                if (conversationTemp.getLatestMessage() instanceof DiscussionNotificationMessage) {
                    DiscussionNotificationMessage discussionNotificationMessage = (DiscussionNotificationMessage) conversationTemp.getLatestMessage();
                    if (discussionNotificationMessage.getOperator().equals(uiUserInfo.getUserId())) {
                        conversationTemp.setOperator(uiUserInfo);
                    } else if (!TextUtils.isEmpty(discussionNotificationMessage.getExtension()) && discussionNotificationMessage.getExtension().indexOf(",") == -1 && discussionNotificationMessage.getExtension().equals(uiUserInfo.getUserId())) {
                        conversationTemp.setOperatored(uiUserInfo);
                    }
                }
            }
        }

    }
}
