package net.iskandar.for_binadox.chat.client.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.iskandar.for_binadox.chat.client.to.ChatMessageTo;

public abstract class BaseChatModelImpl implements ChatModel {

	private Integer chatId;
	private Integer lastMessageId = -1;
	private int daysMessages;
	private List<Listener> listeners = new ArrayList<Listener>();
	
	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
		setLastMessageId(-1);
	}

	public Integer getLastMessageId() {
		return lastMessageId;
	}

	public void setLastMessageId(Integer lastMessageId) {
		this.lastMessageId = lastMessageId;
	}

	public int getDaysMessages() {
		return daysMessages;
	}

	public void setDaysMessages(int daysMessages) {
		this.daysMessages = daysMessages;
	}

	@Override
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	protected void onNewMessages(List<ChatMessageTo> messages){
		List<ChatMessageTo> messagesToEmit = new ArrayList<ChatMessageTo>();
		for(ChatMessageTo message : messages){
			if(lastMessageId < message.getId()){
				lastMessageId = message.getId();
				messagesToEmit.add(message);
			}
		}
		for(Listener l : listeners)
			l.newMessages(this, messagesToEmit);			
	}

	@Override
	public void init(Integer chatId, int daysMessages) {
		setChatId(chatId);
		setDaysMessages(daysMessages);
		doInit();
	}

	protected abstract void doInit();

}
