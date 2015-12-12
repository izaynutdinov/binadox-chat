package net.iskandar.for_binadox.chat.server;

import java.util.ArrayList;
import java.util.List;

import net.iskandar.for_binadox.chat.client.to.*;

public class ChatModel {

	private class Chat {

		private Integer id;
		private List<Listener> listeners = new ArrayList<Listener>();

		public Chat(Integer id) {
			super();
			this.id = id;
		}

		public void addListener(Listener l, ChatUserTo chatUser){
			listeners.add(l);
			for(Listener ls : listeners)
				ls.onOnline(ChatModel.this, chatUser);
		}

		public void removeListener(Listener l, ChatUserTo chatUser){
			
		}

	}

	public interface Listener {
		void onOnline(ChatModel model, ChatUserTo chatUser);
		void onOffline(ChatModel model, ChatUserTo chatUser);
		void onNewMessage(ChatModel chatModel, ChatMessageTo chatMessage);
	}

}
