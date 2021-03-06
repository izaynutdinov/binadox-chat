package net.iskandar.for_binadox.chat.server;

import java.util.ArrayList;

import static java.lang.System.out;

import java.util.List;

import javax.servlet.http.HttpServletRequest;







import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import net.iskandar.for_binadox.chat.client.ChatFacade;
import net.iskandar.for_binadox.chat.client.ChatFacadeException;
import net.iskandar.for_binadox.chat.client.ChatFacadeException.ErrorCode;
import net.iskandar.for_binadox.chat.client.to.ChatMessageTo;
import net.iskandar.for_binadox.chat.client.to.ChatMessagesTo;
import net.iskandar.for_binadox.chat.client.to.ChatTo;
import net.iskandar.for_binadox.chat.client.to.ChatUserTo;
import net.iskandar.for_binadox.chat.server.domain.Chat;
import net.iskandar.for_binadox.chat.server.domain.ChatMessage;
import net.iskandar.for_binadox.chat.server.domain.ChatMessages;
import net.iskandar.for_binadox.chat.server.domain.ChatUser;
import net.iskandar.for_binadox.chat.server.domain.User;
import net.iskandar.for_binadox.chat.server.service.ChatService;
import net.iskandar.for_binadox.chat.server.service.ChatServiceException;

public class ChatFacadeImpl extends RemoteServiceServlet implements ChatFacade {

	private static final String CURRENT_USER_ATTR_NAME = "net.iskandar.for_binadox.chat.server.ChatFacadeImpl.CURRENT_USER";
	
	private static final Logger log = LogManager.getLogger(ChatFacadeImpl.class);
	
	private ChatService chatService;
	
	public ChatService getChatService() {
		return chatService;
	}

	public void setChatService(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@Override
	public String processCall(String payload) throws SerializationException {
		// TODO Auto-generated method stub
		out.println("Processing call payload=[" + payload + "]");
		return super.processCall(payload);
	}

	private User currentUser(){
		HttpServletRequest req = getThreadLocalRequest();
		User user = (User) req.getAttribute(CURRENT_USER_ATTR_NAME);
		if(user != null)
			return user;
		Integer userId = Utils.getUserId(req);
		if(userId != null){
			user = chatService.getUser(userId);
			req.setAttribute(CURRENT_USER_ATTR_NAME, userId);
			return user;
		}
		return null;
	}

	@Override
	public List<ChatTo> getChats() throws ChatFacadeException {
		User currentUser = currentUser();
		if(currentUser == null)
			throw createNotLoggedIn("You are not logged in!");
		List<ChatTo> result = new ArrayList<ChatTo>();
		for(Chat chat : chatService.getChats(currentUser)){
			result.add(Utils.createChatTo(chat));
		}
		return result;
	}

	@Override
	public List<ChatUserTo> getChatUsers(Integer chatId)
			throws ChatFacadeException {
		User currentUser = currentUser();
		if(currentUser == null)
			throw createNotLoggedIn("You are not logged in!");
		List<ChatUserTo> results = new ArrayList<ChatUserTo>();
		try {
			for(ChatUser chatUser : chatService.getChatUsers(currentUser, chatId)){
				results.add(Utils.createChatUserTo(chatUser));
			}
		} catch(ChatServiceException ex){
			throw createServiceError(ex);
		}
		return results;
	}

	@Override
	public ChatMessagesTo getChatMessages(Integer chatId, int days)
			throws ChatFacadeException {
		log.debug("getChatMessages chatId=" + chatId + ", days=" + days);
		
		User currentUser = currentUser();
		if(currentUser == null)
			throw createNotLoggedIn("You are not logged in!");
		try {
			ChatMessagesTo chatMessagesTo = new ChatMessagesTo();
			List<ChatMessageTo> messages = new ArrayList<ChatMessageTo>();
			ChatMessages chatMessages = chatService.getChatMessages(currentUser, chatId, days);
			chatMessagesTo.setChatId(chatMessages.getChatId());
			chatMessagesTo.setLastMessageId(chatMessages.getLastMessageId());
			for(ChatMessage chatMessage : chatMessages.getChatMessages()){
				messages.add(Utils.createChatMessageTo(chatMessage));
			}
			chatMessagesTo.setMessages(messages);
			return chatMessagesTo;
		} catch (ChatServiceException e) {
			throw createServiceError(e);
		}
	}

	@Override
	public List<ChatMessageTo> updateChatMessages(Integer[] chats,
			Integer lastMessageId) throws ChatFacadeException {
		User currentUser = currentUser();
		if(currentUser == null)
			throw createNotLoggedIn("You are not logged in!");
		try {
			List<ChatMessageTo> result = new ArrayList<ChatMessageTo>();
			for(ChatMessage chatMessage : chatService.updateChatMessages(currentUser, chats, lastMessageId)){
				result.add(Utils.createChatMessageTo(chatMessage));
			}
			return result;
		} catch (ChatServiceException e) {
			throw createServiceError(e);
		}
	}

	@Override
	public void postMessage(Integer chatId, String text)
			throws ChatFacadeException {
		User user = currentUser();
		if(user == null)
			throw createNotLoggedIn("You are not logged in!");		
		try {
			chatService.postMessage(user, chatId, text);
		} catch (ChatServiceException e) {
			throw createServiceError(e);
		}
	}
	
	private static ChatFacadeException createServiceError(ChatServiceException ex) throws ChatFacadeException {
		return new ChatFacadeException(ErrorCode.SERVICE_ERROR, ex.getMessage());
	}	
	
	private static ChatFacadeException createNotLoggedIn(String message) throws ChatFacadeException {
		return new ChatFacadeException(ErrorCode.NOT_LOGGED_IN, message);
	}	

}
