package com.bainiaohe.dodo.main.fragments.messages.adapter;

/**
 * Created by Lewis on 2015/1/28.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import io.rong.imkit.Res;
import io.rong.imkit.adapter.BaseAdapter;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.model.UIGroup;
import io.rong.imkit.model.UIUserInfo;
import io.rong.imkit.utils.HighLightUtils;
import io.rong.imkit.utils.RCDateUtils;
import io.rong.imkit.utils.Util;
import io.rong.imkit.view.AsyncImageView;
import io.rong.imlib.RongIMClient.ConversationNotificationStatus;
import io.rong.imlib.RongIMClient.ConversationType;
import io.rong.imlib.RongIMClient.DiscussionNotificationMessage;
import io.rong.imlib.RongIMClient.SentStatus;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class ConversationListAdapter extends BaseAdapter<UIConversation> {
    private ConversationListAdapter.OnGetDataListener mOnGetDataListener;
    private boolean mIsGroup = false;

    public ConversationListAdapter(Context context, boolean isGroup) {
        super(context);
        this.mIsGroup = isGroup;
    }

    public boolean isIsGroup() {
        return this.mIsGroup;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ConversationListAdapter.ViewHolder holder = null;
        if (convertView != null && convertView.getTag() != null) {
            Log.d("FriendListAdapter", "this is not null");
            holder = (ConversationListAdapter.ViewHolder) convertView.getTag();
            holder.icon.clean();
            holder.time.setText("");
        } else {
            convertView = LayoutInflater.from(this.mContext).inflate(Res.getInstance(this.mContext).layout("rc_item_conversationlist"), (ViewGroup) null);
            holder = new ConversationListAdapter.ViewHolder();
            holder.layout = convertView.findViewById(android.R.id.widget_frame);
            holder.icon = (AsyncImageView) convertView.findViewById(android.R.id.icon);
            holder.message = (TextView) convertView.findViewById(Res.getInstance(this.mContext).id("rc_new_message"));
            holder.username = (TextView) convertView.findViewById(16908308);
            holder.time = (TextView) convertView.findViewById(16908309);
            holder.content = (TextView) convertView.findViewById(16908299);
            holder.icon.setDefaultDrawable(this.mContext.getResources().getDrawable(Res.getInstance(this.mContext).drawable("rc_default_portrait")));
            holder.imageView = (ImageView) convertView.findViewById(Res.getInstance(this.mContext).id("message_block"));
            convertView.setTag(holder);
        }
        Log.d("ConversationListAdapter", "getView");
        holder.username.setText("haha");
        holder.content.setText("123");
        holder.content.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
        UIConversation conversation = (UIConversation) this.dataSet.get(position);
        if (conversation != null) {
            int unReadCont = conversation.getUnreadMessageCount();
            String moreMsgFlag = null;
            if (unReadCont > 0) {
                if (unReadCont > 99) {
                    moreMsgFlag = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("new_message_more"));
                } else {
                    moreMsgFlag = String.valueOf(unReadCont);
                }

                holder.message.setVisibility(View.VISIBLE);
                holder.message.setText(moreMsgFlag);
            } else {
                holder.message.setVisibility(View.GONE);
            }

            if (conversation.isTop()) {
                holder.layout.setBackgroundColor(this.mContext.getResources().getColor(Res.getInstance(this.mContext).color("rc_list_item_istop_bg_color")));
            } else {
                holder.layout.setBackgroundColor(0);
            }

            if (conversation.getConversationType() != ConversationType.PRIVATE && conversation.getConversationType() != ConversationType.SYSTEM) {
                if (conversation.getConversationType() == ConversationType.DISCUSSION) {
                    holder.icon.setImageDrawable(this.mContext.getResources().getDrawable(Res.getInstance(this.mContext).drawable("rc_default_discussion_portrait")));
                    if (!TextUtils.isEmpty(conversation.getConversationTitle()) && conversation.getUiDiscussion() != null) {
                        if (conversation.getUiDiscussion() != null && !TextUtils.isEmpty(conversation.getUiDiscussion().getName())) {
                            conversation.setConversationTitle(conversation.getUiDiscussion().getName());
                        }
                    } else if (this.mOnGetDataListener != null) {
                        this.mOnGetDataListener.getDiscussionInfo(position, conversation.getTargetId());
                    }

                    if (!TextUtils.isEmpty(conversation.getConversationTitle())) {
                        holder.username.setText(conversation.getConversationTitle());
                    } else {
                        holder.username.setText(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("default_discussion_name")));
                    }

                    if (conversation.getLatestMessage() instanceof DiscussionNotificationMessage) {
                        DiscussionNotificationMessage time1 = (DiscussionNotificationMessage) conversation.getLatestMessage();
                        if (time1.getType() == 3) {
                            holder.username.setText(time1.getExtension());
                        }
                    }

                    if (conversation.getLatestMessage() == null && conversation.getSentTime() == 0L) {
                        conversation.setSentTime(System.currentTimeMillis());
                    }
                } else if (ConversationType.GROUP == conversation.getConversationType()) {
                    holder.icon.setImageDrawable(this.mContext.getResources().getDrawable(Res.getInstance(this.mContext).drawable("rc_group_default_portrait")));
                    if (this.mIsGroup) {
                        if (TextUtils.isEmpty(conversation.getConversationTitle())) {
                            if (this.mOnGetDataListener != null) {
                                this.mOnGetDataListener.getGroupInfo(conversation.getTargetId());
                            }
                        } else {
                            holder.username.setText(conversation.getConversationTitle());
                            UIGroup time2 = conversation.getUiGroup();
                            if (time2 != null) {
                                if (time2.getPortraitResource() != null) {
                                    Picasso.with(mContext).load(time2.getPortraitUri()).into(holder.icon);
                                    // holder.icon.setResource(time2.getPortraitResource());
                                } else {
                                    holder.icon.clean();
                                }
                            }
                        }
                    } else {
                        holder.username.setText(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("rc_group_conversation_list_name")));
                    }
                } else {
                    holder.username.setText(conversation.getTargetId());
                }
            } else {
                Log.d("ConversationListAdapter", "conversation.getConversationType():" + conversation.getConversationType());
                if (conversation.getUserInfo() == null) {
                    Log.d("ConversationListAdapter", "i am null");
                }
                if (conversation.getUserInfo() != null && !TextUtils.isEmpty(conversation.getUserInfo().getName()) && !TextUtils.isEmpty(conversation.getConversationTitle())) {

                    UIUserInfo time = conversation.getUserInfo();
                    if (time != null) {
                        holder.username.setText(time.getName());
                        Log.d("ConversationListAdapter", time.getName() + "1");
                        conversation.setConversationTitle(time.getName());
                        if (time.getPortraitResource() != null) {
                            Picasso.with(mContext).load(time.getPortraitUri()).into(holder.icon);
                            //  holder.icon.setResource(time.getPortraitResource());
                        } else {
                            holder.icon.clean();
                        }
                    }
                } else if (this.mOnGetDataListener != null) {
                    this.mOnGetDataListener.getUserInfo(position, conversation.getTargetId(), conversation.getTargetId(), conversation.getConversationType());
                }


            }

            if (conversation.getNotificationStatus() == ConversationNotificationStatus.DO_NOT_DISTURB) {
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }

            String time4;
            if (!TextUtils.isEmpty(conversation.getDraft())) {
                SpannableStringBuilder time3 = new SpannableStringBuilder(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_draft_content")));
                holder.content.append(time3.append(Util.highLightLink(HighLightUtils.loadHighLight(HighLightUtils.replaceEmoji(conversation.getDraft())))));
            } else if (conversation.getLatestMessage() != null) {
                if (TextUtils.isEmpty(conversation.getSenderUserName()) && (conversation.getConversationType() == ConversationType.GROUP || conversation.getConversationType() == ConversationType.DISCUSSION) && this.mOnGetDataListener != null) {
                    this.mOnGetDataListener.getUserInfo(position, conversation.getSenderUserId(), conversation.getTargetId(), conversation.getConversationType());
                }

                if (!this.mIsGroup && conversation.getConversationType() == ConversationType.GROUP && (conversation.getUiGroup() == null || TextUtils.isEmpty(conversation.getUiGroup().getName())) && this.mOnGetDataListener != null) {
                    this.mOnGetDataListener.getGroupInfo(conversation.getTargetId());
                }

                this.setContent(position, this.mIsGroup, holder, conversation);
                time4 = this.getCurrentUserInfo() != null ? this.getCurrentUserInfo().getUserId() : "";
                if (conversation.getSenderUserId() != null && conversation.getSenderUserId().equals(time4)) {
                    int width;
                    Drawable drawable;
                    if (conversation.getSentStatus() != null && conversation.getSentStatus() == SentStatus.FAILED) {
                        width = (int) this.mContext.getResources().getDimension(Res.getInstance(this.mContext).dimen("px_to_dip_26"));
                        drawable = this.mContext.getResources().getDrawable(Res.getInstance(this.mContext).drawable("rc_conversation_list_msg_send_failure"));
                        drawable.setBounds(0, 0, width, width);
                        holder.content.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
                    } else if (conversation.getSentStatus() == SentStatus.SENDING) {
                        width = (int) this.mContext.getResources().getDimension(Res.getInstance(this.mContext).dimen("px_to_dip_26"));
                        drawable = this.mContext.getResources().getDrawable(Res.getInstance(this.mContext).drawable("conversation_list_msg_sending"));
                        drawable.setBounds(0, 0, width, width);
                        holder.content.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
                    }
                }
            } else {
                holder.content.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
                holder.content.setText("");
            }

            if (conversation.getSentTime() > 0L) {
                time4 = RCDateUtils.getConvastionListFromatDate(new Date(conversation.getSentTime()));
                holder.time.setText(time4);
            } else {
                holder.time.setText("");
            }
        }

        return convertView;
    }

    private final void setContent(int position, boolean isGroup, ConversationListAdapter.ViewHolder hodler, UIConversation conversation) {
        if (isGroup) {
            if (conversation.getConversationType() == ConversationType.GROUP) {
                if (!TextUtils.isEmpty(conversation.getSenderUserName())) {
                    hodler.content.setText(this.getContent(position, conversation, false, isGroup));
                } else {
                    hodler.content.setText(this.getContent(position, conversation, true, isGroup));
                }
            } else {
                hodler.content.setText(this.getContent(position, conversation, true, isGroup));
            }
        } else if (conversation.getConversationType() == ConversationType.DISCUSSION) {
            if (!TextUtils.isEmpty(conversation.getSenderUserName())) {
                hodler.content.setText(this.getContent(position, conversation, false, isGroup));
            } else {
                hodler.content.setText(this.getContent(position, conversation, true, isGroup));
            }
        } else if (conversation.getConversationType() == ConversationType.GROUP) {
            if (conversation.getUiGroup() != null && !TextUtils.isEmpty(conversation.getUiGroup().getName())) {
                hodler.content.setText(this.getContent(position, conversation, false, isGroup));
            } else {
                hodler.content.setText(this.getContent(position, conversation, true, isGroup));
            }
        } else {
            hodler.content.setText(this.getContent(position, conversation, true, isGroup));
        }

    }

    private final SpannableStringBuilder getContent(int position, UIConversation conversation, boolean isDirect, boolean isGroup) {
        SpannableStringBuilder textContent = new SpannableStringBuilder();
        InformationNotificationMessage informationNotificationMessage;
        if (!isDirect) {
            if (!isGroup) {
                if (conversation.getConversationType() == ConversationType.GROUP) {
                    if (conversation.getLatestMessage() instanceof TextMessage) {
                        if (conversation.getUiGroup() != null && !TextUtils.isEmpty(conversation.getUiGroup().getName())) {
                            textContent.append(conversation.getUiGroup().getName()).append("：").append(conversation.getTextMessageContent());
                        } else {
                            textContent.append(conversation.getTextMessageContent());
                        }
                    } else if (conversation.getLatestMessage() instanceof ImageMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_content")));
                    } else if (conversation.getLatestMessage() instanceof VoiceMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_voice_content")));
                    } else if (conversation.getLatestMessage() instanceof RichContentMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_text_content")));
                    } else if (conversation.getLatestMessage() instanceof LocationMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_location_content")));
                    } else if (conversation.getLatestMessage() instanceof InformationNotificationMessage) {
                        informationNotificationMessage = (InformationNotificationMessage) conversation.getLatestMessage();
                        if (informationNotificationMessage != null && !TextUtils.isEmpty(informationNotificationMessage.getMessage())) {
                            textContent.append(Html.fromHtml(informationNotificationMessage.getMessage()).toString());
                        }
                    }
                } else if (conversation.getConversationType() == ConversationType.DISCUSSION) {
                    if (conversation.getLatestMessage() instanceof TextMessage) {
                        if (TextUtils.isEmpty(conversation.getSenderUserName())) {
                            textContent.append(conversation.getTextMessageContent());
                        } else {
                            textContent.append(conversation.getSenderUserName()).append("：").append(conversation.getTextMessageContent());
                        }
                    } else if (conversation.getLatestMessage() instanceof ImageMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_content")));
                    } else if (conversation.getLatestMessage() instanceof VoiceMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_voice_content")));
                    } else if (conversation.getLatestMessage() instanceof RichContentMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_text_content")));
                    } else if (conversation.getLatestMessage() instanceof LocationMessage) {
                        if (conversation.getSenderUserName() != null) {
                            textContent.append(conversation.getSenderUserName()).append("：");
                        }

                        textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_location_content")));
                    } else if (conversation.getLatestMessage() instanceof DiscussionNotificationMessage) {
                        DiscussionNotificationMessage informationNotificationMessage1 = (DiscussionNotificationMessage) conversation.getLatestMessage();
                        String selfUserId = this.getCurrentUserInfo() != null ? this.getCurrentUserInfo().getUserId() : "";
                        String[] userIds = null;
                        String operator = informationNotificationMessage1.getOperator();
                        if (!TextUtils.isEmpty(informationNotificationMessage1.getExtension())) {
                            if (informationNotificationMessage1.getExtension().indexOf(",") != -1) {
                                userIds = informationNotificationMessage1.getExtension().split(",");
                            } else {
                                userIds = new String[]{informationNotificationMessage1.getExtension()};
                            }
                        }

                        String formatString;
                        switch (informationNotificationMessage1.getType()) {
                            case 1:
                                if (operator.equals(selfUserId)) {
                                    if (userIds != null) {
                                        if (userIds.length > 1) {
                                            formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_add"));
                                            textContent.append(String.format(formatString, new Object[]{"你", Integer.valueOf(userIds.length)}));
                                        } else if (userIds.length == 1) {
                                            if (conversation.getOperatored() != null && !TextUtils.isEmpty(conversation.getOperatored().getName())) {
                                                if (conversation.getOperatored() != null && !TextUtils.isEmpty(conversation.getOperatored().getName())) {
                                                    formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_added"));
                                                    textContent.append(String.format(formatString, new Object[]{"你", conversation.getOperatored().getName()}));
                                                }
                                            } else if (this.mOnGetDataListener != null) {
                                                this.mOnGetDataListener.getUserInfo(position, userIds[0], conversation.getTargetId(), conversation.getConversationType());
                                            }
                                        }
                                    }
                                } else if (userIds != null) {
                                    if (!operator.equals(selfUserId) && (conversation.getOperator() == null || TextUtils.isEmpty(conversation.getOperator().getName())) && this.mOnGetDataListener != null) {
                                        this.mOnGetDataListener.getUserInfo(position, operator, conversation.getTargetId(), conversation.getConversationType());
                                    }

                                    if (userIds.length == 1) {
                                        if (!userIds[0].equals(selfUserId) && (conversation.getOperatored() == null || TextUtils.isEmpty(conversation.getOperatored().getName())) && this.mOnGetDataListener != null) {
                                            this.mOnGetDataListener.getUserInfo(position, userIds[0], conversation.getTargetId(), conversation.getConversationType());
                                        }

                                        if (conversation.getOperator() != null && !TextUtils.isEmpty(conversation.getOperator().getName())) {
                                            formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_added"));
                                            if (userIds[0].equals(selfUserId)) {
                                                textContent.append(String.format(formatString, new Object[]{conversation.getOperator().getName(), "你"}));
                                            } else if (conversation.getOperatored() != null && !TextUtils.isEmpty(conversation.getOperatored().getName())) {
                                                textContent.append(String.format(formatString, new Object[]{conversation.getOperator().getName(), conversation.getOperatored().getName()}));
                                            }
                                        }
                                    } else if (operator.equals(selfUserId)) {
                                        formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_add"));
                                        textContent.append(String.format(formatString, new Object[]{"你", Integer.valueOf(userIds.length)}));
                                    } else if (conversation.getOperator() != null && !TextUtils.isEmpty(conversation.getOperator().getName())) {
                                        formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_add"));
                                        textContent.append(String.format(formatString, new Object[]{conversation.getOperator().getName(), Integer.valueOf(userIds.length)}));
                                    }
                                }
                                break;
                            case 2:
                                if ((conversation.getOperator() == null || TextUtils.isEmpty(conversation.getOperator().getName())) && this.mOnGetDataListener != null) {
                                    this.mOnGetDataListener.getUserInfo(position, operator, conversation.getTargetId(), conversation.getConversationType());
                                }

                                if (userIds != null && conversation.getOperator() != null && !TextUtils.isEmpty(conversation.getOperator().getName())) {
                                    formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_exit"));
                                    textContent.append(String.format(formatString, new Object[]{conversation.getOperator().getName()}));
                                }
                                break;
                            case 3:
                                formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_rename"));
                                if (!TextUtils.isEmpty(informationNotificationMessage1.getExtension())) {
                                    if (operator.equals(selfUserId)) {
                                        textContent.append(String.format(formatString, new Object[]{"你", informationNotificationMessage1.getExtension()}));
                                    } else if (conversation.getOperator() != null && !TextUtils.isEmpty(conversation.getOperator().getName())) {
                                        if (conversation.getOperator() != null && !TextUtils.isEmpty(conversation.getOperator().getName())) {
                                            textContent.append(String.format(formatString, new Object[]{conversation.getOperator().getName(), informationNotificationMessage1.getExtension()}));
                                        }
                                    } else if (this.mOnGetDataListener != null) {
                                        this.mOnGetDataListener.getUserInfo(position, operator, conversation.getTargetId(), conversation.getConversationType());
                                    }

                                    conversation.setConversationTitle(informationNotificationMessage1.getExtension());
                                }
                                break;
                            case 4:
                                if (userIds != null) {
                                    formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_who_removed"));
                                    if (!operator.equals(selfUserId) && (conversation.getOperator() == null || TextUtils.isEmpty(conversation.getOperator().getName())) && this.mOnGetDataListener != null) {
                                        this.mOnGetDataListener.getUserInfo(position, operator, conversation.getTargetId(), conversation.getConversationType());
                                    }

                                    if (!userIds[0].equals(selfUserId) && (conversation.getOperatored() == null || TextUtils.isEmpty(conversation.getOperatored().getName())) && this.mOnGetDataListener != null) {
                                        this.mOnGetDataListener.getUserInfo(position, userIds[0], conversation.getTargetId(), conversation.getConversationType());
                                    }

                                    if (operator.equals(selfUserId)) {
                                        if (conversation.getOperatored() != null && !TextUtils.isEmpty(conversation.getOperatored().getName())) {
                                            textContent.append(String.format(formatString, new Object[]{conversation.getOperatored().getName(), "你"}));
                                        }
                                    } else if (conversation.getOperator() != null && !TextUtils.isEmpty(conversation.getOperator().getName())) {
                                        if (userIds[0].equals(selfUserId)) {
                                            textContent.append(String.format(formatString, new Object[]{"你", conversation.getOperator().getName()}));
                                        } else if (conversation.getOperatored() != null && !TextUtils.isEmpty(conversation.getOperatored().getName())) {
                                            textContent.append(String.format(formatString, new Object[]{conversation.getOperatored().getName(), conversation.getOperator().getName()}));
                                        }
                                    }
                                }
                                break;
                            case 5:
                                formatString = this.mContext.getResources().getString(Res.getInstance(this.mContext).string("notification_message_discussion_is_open_invite"));
                                if (operator.equals(selfUserId)) {
                                    if (!TextUtils.isEmpty(informationNotificationMessage1.getExtension())) {
                                        if (informationNotificationMessage1.getExtension().equals("1")) {
                                            textContent.append(String.format(formatString, new Object[]{"你", "关闭"}));
                                        } else if (informationNotificationMessage1.getExtension().equals("0")) {
                                            textContent.append(String.format(formatString, new Object[]{"你", "开放"}));
                                        }
                                    }
                                } else if (conversation.getOperator() != null && !TextUtils.isEmpty(conversation.getOperator().getName())) {
                                    if (!TextUtils.isEmpty(informationNotificationMessage1.getExtension())) {
                                        if (informationNotificationMessage1.getExtension().equals("1")) {
                                            textContent.append(String.format(formatString, new Object[]{conversation.getOperator().getName(), "关闭"}));
                                        } else if (informationNotificationMessage1.getExtension().equals("0")) {
                                            textContent.append(String.format(formatString, new Object[]{conversation.getOperator().getName(), "开放"}));
                                        }
                                    }
                                } else if (this.mOnGetDataListener != null) {
                                    this.mOnGetDataListener.getUserInfo(position, operator, conversation.getTargetId(), conversation.getConversationType());
                                }
                        }
                    } else if (conversation.getLatestMessage() instanceof InformationNotificationMessage) {
                        informationNotificationMessage = (InformationNotificationMessage) conversation.getLatestMessage();
                        if (informationNotificationMessage != null && !TextUtils.isEmpty(informationNotificationMessage.getMessage())) {
                            textContent.append(Html.fromHtml(informationNotificationMessage.getMessage()).toString());
                        }
                    }
                } else if (conversation.getLatestMessage() instanceof TextMessage) {
                    textContent.append(conversation.getTextMessageContent());
                } else if (conversation.getLatestMessage() instanceof ImageMessage) {
                    textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_content")));
                } else if (conversation.getLatestMessage() instanceof VoiceMessage) {
                    textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_voice_content")));
                } else if (conversation.getLatestMessage() instanceof RichContentMessage) {
                    textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_text_content")));
                } else if (conversation.getLatestMessage() instanceof LocationMessage) {
                    textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_location_content")));
                } else if (conversation.getLatestMessage() instanceof InformationNotificationMessage) {
                    informationNotificationMessage = (InformationNotificationMessage) conversation.getLatestMessage();
                    if (informationNotificationMessage != null && !TextUtils.isEmpty(informationNotificationMessage.getMessage())) {
                        textContent.append(Html.fromHtml(informationNotificationMessage.getMessage()).toString());
                    }
                }
            } else if (conversation.getLatestMessage() instanceof TextMessage) {
                if (TextUtils.isEmpty(conversation.getSenderUserName())) {
                    textContent.append(conversation.getTextMessageContent());
                } else {
                    textContent.append(conversation.getSenderUserName()).append("：").append(conversation.getTextMessageContent());
                }
            } else if (conversation.getLatestMessage() instanceof ImageMessage) {
                if (conversation.getSenderUserName() != null) {
                    textContent.append(conversation.getSenderUserName()).append("：");
                }

                textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_content")));
            } else if (conversation.getLatestMessage() instanceof VoiceMessage) {
                if (conversation.getSenderUserName() != null) {
                    textContent.append(conversation.getSenderUserName()).append("：");
                }

                textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_voice_content")));
            } else if (conversation.getLatestMessage() instanceof RichContentMessage) {
                if (conversation.getSenderUserName() != null) {
                    textContent.append(conversation.getSenderUserName()).append("：");
                }

                textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_text_content")));
            } else if (conversation.getLatestMessage() instanceof LocationMessage) {
                if (conversation.getSenderUserName() != null) {
                    textContent.append(conversation.getSenderUserName()).append("：");
                }

                textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_location_content")));
            } else if (conversation.getLatestMessage() instanceof InformationNotificationMessage) {
                informationNotificationMessage = (InformationNotificationMessage) conversation.getLatestMessage();
                if (informationNotificationMessage != null && !TextUtils.isEmpty(informationNotificationMessage.getMessage())) {
                    textContent.append(Html.fromHtml(informationNotificationMessage.getMessage()).toString());
                }
            }
        } else if (conversation.getLatestMessage() instanceof TextMessage) {
            textContent.append(conversation.getTextMessageContent());
        } else if (conversation.getLatestMessage() instanceof ImageMessage) {
            textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_content")));
        } else if (conversation.getLatestMessage() instanceof VoiceMessage) {
            textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_voice_content")));
        } else if (conversation.getLatestMessage() instanceof RichContentMessage) {
            textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_image_text_content")));
        } else if (conversation.getLatestMessage() instanceof LocationMessage) {
            textContent.append(this.mContext.getResources().getString(Res.getInstance(this.mContext).string("message_type_location_content")));
        } else if (conversation.getLatestMessage() instanceof InformationNotificationMessage) {
            informationNotificationMessage = (InformationNotificationMessage) conversation.getLatestMessage();
            if (informationNotificationMessage != null && !TextUtils.isEmpty(informationNotificationMessage.getMessage())) {
                textContent.append(Html.fromHtml(informationNotificationMessage.getMessage()).toString());
            }
        }

        return textContent;
    }

    public void setOnGetDataListener(ConversationListAdapter.OnGetDataListener mOnGetDataListener) {
        this.mOnGetDataListener = mOnGetDataListener;
    }

    public interface OnGetDataListener {
        void getDiscussionInfo(int var1, String var2);

        void getUserInfo(int var1, String var2, String var3, ConversationType var4);

        void getGroupInfo(String var1);
    }

    class ViewHolder {
        View layout;
        AsyncImageView icon;
        TextView message;
        TextView username;
        TextView time;
        TextView content;
        ImageView imageView;

        ViewHolder() {
        }
    }
}
