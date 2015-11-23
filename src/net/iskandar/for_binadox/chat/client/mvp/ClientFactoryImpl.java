package net.iskandar.for_binadox.chat.client.mvp;

import com.google.gwt.core.client.GWT;

import net.iskandar.for_binadox.chat.client.ChatFacade;
import net.iskandar.for_binadox.chat.client.ChatFacadeAsync;
import net.iskandar.for_binadox.chat.client.model.PollingChatModelImpl;
import net.iskandar.for_binadox.chat.client.mvp.ui.ChatPanel;

public class ClientFactoryImpl implements ClientFactory {

	private static ChatFacadeAsync chatFacade = GWT.create(ChatFacade.class);
	private static ChatPanel chatPanel;
	
	static {
		PollingChatModelImpl chatModel = new PollingChatModelImpl(chatFacade); 
		chatPanel = new ChatPanel(chatModel);		
	}
	
	@Override
	public ChatPanel chatPanel() {
		return chatPanel;
	}

	@Override
	public ChatFacadeAsync chatFacade() {
		return chatFacade;
	}

}
