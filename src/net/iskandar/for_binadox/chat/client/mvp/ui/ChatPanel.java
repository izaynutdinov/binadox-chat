package net.iskandar.for_binadox.chat.client.mvp.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.iskandar.for_binadox.chat.client.ChatFacadeException;
import net.iskandar.for_binadox.chat.client.log.Logger;
import net.iskandar.for_binadox.chat.client.model.ChatModel;
import net.iskandar.for_binadox.chat.client.to.ChatMessageTo;
import net.iskandar.for_binadox.chat.client.to.UserTo;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ChatPanel extends VLayout implements ChatModel.Listener {

	private static final Logger log = new Logger("ChatPanel");

	private TextArea textArea;
	private VLayout chatLog;
	private HTMLFlow latestChatItem;
	private DockPanel chatControl;
	private ChatModel chatModel;
	private Button button;
	
	private HandlerRegistration resizeHandler;
	
	private Timer scrollToBottom = new Timer(){

		@Override
		public void run() {
			chatLog.scrollToBottom();
		}
		
	};
	
	public ChatPanel(ChatModel chatModel) {
		super();
		
		this.chatModel = chatModel;
		chatModel.addListener(this);
		
		//setShowEdges(true);

		chatLog = new VLayout();
		chatLog.setWidth("100%");
		chatLog.setHeight("*");
		chatLog.setShowEdges(true);
		chatLog.setOverflow(Overflow.AUTO);

		chatControl = new DockPanel();
		chatControl.setStyleName("cw-DockPanel");
		chatControl.setWidth("100%");
		chatControl.setHeight("105px");		

		textArea = new TextArea();
		textArea.setWidth("400px");
		textArea.setHeight("87px");
		textArea.addStyleName("chat-textarea");

		button = new Button("Send");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ChatPanel.this.chatModel.postMessage(textArea.getText());
				textArea.setText("");
			}

		});
		button.setWidth("95px");
		button.setHeight("95px");
		chatControl.add(textArea, DockPanel.CENTER);
		chatControl.add(button, DockPanel.EAST);
		
//		chatControl.setMembers(textArea, button);
//		chatControl.setMembers(new BlueBox("*", null, "TEXTAREA"), new BlueBox("95px", null, "TEXTAREA"));
		
		addMember(chatLog);
		addMember(chatControl);

	}

	public void setChatId(Integer chatId){
		chatLog.clear();
		chatModel.init(chatId, 0);
	}

	@Override
	protected void onBind() {
		super.onBind();
		log.log("onBind");
	}
	
	public void resize(int width, int height){
		setWidth(width);
		setHeight(height);
		int newWidth = (getWidth() - 130);
		ChatPanel.log.log("onResized newWidth=" + newWidth);
		textArea.setWidth(newWidth + "px");
		textArea.setHeight("87px");
	}

/*	@Override
	protected void onDraw() {
		super.onDraw();
		log.log("onDraw");
		if(resizeHandler == null){
			textArea.setWidth((this.getWidth() - 140) + "px");
			button.setSize("95px", "95px");
			resizeHandler = addResizedHandler(new ResizedHandler() {
				@Override
				public void onResized(ResizedEvent event) {
					int newWidth = (ChatPanel.this.getWidth() - 140);
					ChatPanel.log.log("onResized newWidth=" + newWidth);
					textArea.setWidth(newWidth + "px");
					textArea.setHeight("87px");
				}
			});
		}
	}
	
*/	

	@Override
	protected void onAttach() {
		super.onAttach();
		log.log("onAttach");
		textArea.setWidth((this.getWidth() - 120) + "px");
		button.setSize("95px", "95px");
		addResizedHandler(new ResizedHandler() {
			@Override
			public void onResized(ResizedEvent event) {
				int newWidth = (ChatPanel.this.getWidth() - 120);
				ChatPanel.log.log("onResized newWidth=" + newWidth);
				textArea.setWidth(newWidth + "px");
				textArea.setHeight("87px");
			}
		});
	}
	
	public void minimizeTextArea(){ // HACK CAN'T AVOID
		textArea.setSize("10px", "10px");
		//button.setSize("10px", "10px");
	}

	@Override
	public void newMessages(ChatModel sender, List<ChatMessageTo> messages) {
		for(ChatMessageTo message : messages){
			log.log("newMessage: from:" + message.getChatUser().getUser().getLogin() + ", text:" + message.getText());
			HTMLFlow flow = null;
			if(latestChatItem != null){
				flow = latestChatItem;
			} else {
				flow = new HTMLFlow();
				chatLog.addMember(flow);					
			}
			StringBuffer buf = new StringBuffer();
			String[] lines = message.getText().split("\n");
			for(int i = 0; i < lines.length; i++){
				if(i < (lines.length - 1))
					buf.append(lines[i] + "<br/>");
				else
					buf.append(lines[i]);
			}
			UserTo user = message.getChatUser().getUser();
			flow.setContents("<div class=\"chat-message\"><b>" + message.getTime().toString() + " " + user.getFirstName() + " " + user.getLastName() + " wrote:  </b>" + buf.toString() + "</div>");
	
			latestChatItem = new HTMLFlow();
			latestChatItem.setContents("<div style=\"width: 100%;height: 100px;\"></div>");
			chatLog.addMember(latestChatItem);
			//
			//messagesAdded.add(flow);
		}
		if(!messages.isEmpty()){
//			chatLog.scrollToBottom();
	 		if(scrollToBottom.isRunning())
				scrollToBottom.cancel();
			scrollToBottom.schedule(1);
		}
	}

	@Override
	public void chatError(ChatFacadeException error) {
		// TODO Auto-generated method stub
		
	}
	
	
    class BlueBox extends Label {  
    	  
        public BlueBox(String contents) {  
            setAlign(Alignment.CENTER);  
            setBorder("1px solid #808080");  
            setBackgroundColor("#C3D9FF");  
            setContents(contents);  
        }  
  
        public BlueBox(Integer width, Integer height, String contents) {  
            this(contents);  
            if (width != null) setWidth(width);  
            if (height != null) setHeight(height);  
        }  
  
        public BlueBox(Integer width, String height, String contents) {  
            this(contents);  
            if (width != null) setWidth(width);  
            if (height != null) setHeight(height);  
        }  
  
        public BlueBox(String width, String height, String contents) {  
            this(contents);  
            if (width != null) setWidth(width);  
            if (height != null) setHeight(height);  
        }  
    }  	

}
