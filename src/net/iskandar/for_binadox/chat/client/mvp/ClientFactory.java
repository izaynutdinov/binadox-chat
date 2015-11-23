package net.iskandar.for_binadox.chat.client.mvp;

import net.iskandar.for_binadox.chat.client.ChatFacadeAsync;
import net.iskandar.for_binadox.chat.client.mvp.ui.ChatPanel;

public interface ClientFactory {

	ChatPanel chatPanel();
	ChatFacadeAsync chatFacade();
	
}
