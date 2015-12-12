package net.iskandar.for_binadox.chat.client.model;

import java.util.Arrays;

import com.codeveo.gwt.stomp.client.Message;
import com.codeveo.gwt.stomp.client.MessageListener;
import com.codeveo.gwt.stomp.client.StompClient;
import com.google.gwt.core.client.GWT;

import net.iskandar.for_binadox.chat.client.Utils;
import net.iskandar.for_binadox.chat.client.log.Logger;
import net.iskandar.for_binadox.chat.client.mvp.ClientFactory;
import net.iskandar.for_binadox.chat.client.to.ChatMessageTo;

public class StompChatModel extends BaseChatModelImpl {

	private static final Logger log = new Logger(
			StompChatModel.class.getSimpleName());

	private StompClient stompClient;
	
	public StompChatModel(ClientFactory clientFactory) {
		super(clientFactory);
	}


	@Override
	protected void doBeforeInit() {
		log.log("initApp");
		String baseUrl = GWT.getHostPageBaseURL();
		log.log("Module base URL: " + baseUrl);

		if(stompClient == null){
			String stompUrl = "ws://localhost:8080/binadox-chat/app/stomp";
			stompClient = new StompClient(stompUrl, new StompClient.Callback() {

				@Override
				public void onError(String cause) {
					log.log("ERROR: cause=" + cause);
				}

				@Override
				public void onDisconnect() {
					// TODO Auto-generated method stub
				}

				@Override
				public void onConnect() {
					log.log("stompClient.onConnect");
					subscribe();
				}
			}, false, true);
			log.log("About to connect to stomp!");
			stompClient.connect();
		} else {
			stompClient.disconnect();
			stompClient.connect();
		}
	}
	
	private void subscribe(){
		stompClient.subscribe("/chats/" + getChatId(), new MessageListener() {
			@Override
			public void onMessage(Message message) {
//				log.log("stompClient.onMessage: json = " + message.getBody());
				ChatMessageTo chatMessage = Utils.parseChatMessageTo(message.getBody());
				log.log("stompClient.onMessage: " + chatMessage.toString());
				onNewMessages(Arrays.asList(chatMessage));
			}
		});
	}

	@Override
	protected void doAfterInit() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void doBeforePostMessage() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void doAfterPostMessage() {
		// TODO Auto-generated method stub
	}

}
