package net.iskandar.for_binadox.chat.client;

import java.util.List;

import net.iskandar.for_binadox.chat.client.log.Logger;
import net.iskandar.for_binadox.chat.client.mvp.ClientFactory;
import net.iskandar.for_binadox.chat.client.mvp.ui.ChatPanel;
import net.iskandar.for_binadox.chat.client.to.ChatTo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Initializer3Impl implements Initializer {

	private static final Logger log = new Logger(
			Initializer3Impl.class.getSimpleName());

	private static final ClientFactory clientFactory = GWT
			.create(ClientFactory.class);

	@Override
	public void initApp() {
		Window.enableScrolling(false);
		clientFactory.chatFacade().getChats(new AsyncCallback<List<ChatTo>>() {

			@Override
			public void onFailure(Throwable caught) {
				log.log("getChats FAILED!");
			}

			@Override
			public void onSuccess(List<ChatTo> result) {

				final HLayout mainLayout = new HLayout();
				mainLayout.setWidth100();
				mainLayout.setHeight100();
				Window.addResizeHandler(
						new ResizeHandler() {

					@Override
					public void onResize(ResizeEvent event) {
						log.log("onWindowResizeHandler");
						int newWidth = Window.getClientWidth() - 15;
						int newHeight = Window.getClientHeight() - 30;
						log.log("onResize newWidth=" + newWidth
								+ ", newHeight=" + newHeight);
						mainLayout.setWidth(newWidth);
						mainLayout.setHeight(newHeight);
					}

				});
				VLayout menu = new VLayout();
				menu.setWidth("150px");
				menu.setShowResizeBar(true);
				for (ChatTo chat : result) {
					HTMLFlow item = new HTMLFlow();
					item.setContents("<div><a href=\"#chat:" + chat.getId()
							+ "\">" + chat.getTitle() + "</a></div>");
					menu.addMember(item);
				}
				final VLayout centerPanel = new VLayout();
				centerPanel.setWidth("*");
				final ChatPanel chatPanel = clientFactory.chatPanel();
				chatPanel.getElement().addClassName("chat-panel");
				chatPanel.setChatId(1);
				chatPanel.setWidth(centerPanel.getWidth());
				chatPanel.setHeight(centerPanel.getHeight());
				centerPanel.addResizedHandler(new ResizedHandler() {

					@Override
					public void onResized(ResizedEvent event) {
						log.log("centerPanel.onResized");
						// chatPanel.minimizeTextArea();
						log.log("centerPanel.getWidth() = "
								+ centerPanel.getWidth());
						log.log("centerPanel.getHeight() = "
								+ centerPanel.getHeight());
						// chatPanel.setWidth(centerPanel.getWidth());
						// chatPanel.setHeight(centerPanel.getHeight());
						chatPanel.resize(centerPanel.getWidth(),
								centerPanel.getHeight());
					}

				});
				centerPanel.setMembers(chatPanel);
				mainLayout.setMembers(menu, centerPanel);
				RootPanel.get().add(mainLayout);
			}

		});

	}

}
