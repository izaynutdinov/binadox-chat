package net.iskandar.for_binadox.chat.client.model;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import net.iskandar.for_binadox.chat.client.ChatFacadeAsync;
import net.iskandar.for_binadox.chat.client.log.Logger;
import net.iskandar.for_binadox.chat.client.to.ChatMessageTo;
import net.iskandar.for_binadox.chat.client.to.ChatMessagesTo;

public class PollingChatModelImpl extends BaseChatModelImpl {

	private ChatFacadeAsync chatFacade;
	private Timer updateMessagesTimer;
	private static final Logger log = new Logger("PollingChatModelImpl");

	public PollingChatModelImpl(ChatFacadeAsync chatFacade) {
		super();
		this.chatFacade = chatFacade;
	}

	@Override
	protected void doInit() {
		if(updateMessagesTimer != null){
			if(updateMessagesTimer.isRunning())
				updateMessagesTimer.cancel();
			updateMessagesTimer = null;
		}
		log.log("About to call getChatMessages chatId = " + getChatId() + ", daysMessages = " + getDaysMessages());
		chatFacade.getChatMessages(getChatId(), getDaysMessages(), new AsyncCallback<ChatMessagesTo>() {
			@Override
			public void onSuccess(ChatMessagesTo result) {
				onNewMessages(result.getMessages());
				if(result.getMessages().isEmpty())
					setLastMessageId(result.getLastMessageId());
				updateMessagesTimer = new Timer(){
					@Override
					public void run() {
						updateMessages();
					}
				};
				//updateMessagesTimer.schedule(1000);
			}

			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

	@Override
	public void postMessage(String text) {
		if(updateMessagesTimer != null && updateMessagesTimer.isRunning())
			updateMessagesTimer.cancel();
		chatFacade.postMessage(getChatId(), text, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				log.log("postMessage.onFailure");
				
			}

			@Override
			public void onSuccess(Void result) {
				log.log("postMessage.onSuccess");				
				updateMessages();
			}
		});
	}
	
	protected void updateMessages(){
		chatFacade.updateChatMessages(new Integer[]{ getChatId() }, getLastMessageId(), new AsyncCallback<List<ChatMessageTo>>() {

			@Override
			public void onSuccess(List<ChatMessageTo> result) {
				onNewMessages(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

		});
		updateMessagesTimer.schedule(1000);
	}

	private native int getTimeZoneOffset() /*-{
    	return new Date().getTimezoneOffset();
	}-*/;

}
