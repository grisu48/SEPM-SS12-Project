package org.smartsnip.client;

import org.smartsnip.shared.ISession;
import org.smartsnip.shared.ISessionAsync;
import org.smartsnip.shared.NoAccessException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The controller according to a MVC pattern.
 * 
 * The controller is implemented as Singleton. Each call must be in a static way
 * 
 * The controller is the main class in the client package. As the name says it
 * controls the GUI and on the other side it is responsible for the
 * communication with the server
 * 
 * 
 */
public class Control implements EntryPoint {

	private static Control instance = null;
	private final static String COOKIE_SESSION = ISession.cookie_Session_ID;
	private final static ISessionAsync session = ISession.Util.getInstance();

	private static GUI myGUI = new GUI();

	private Control() {
	}

	public static Control getInstance() {
		if (instance == null) {
			instance = new Control();
		}
		return instance;
	}

	@Override
	public void onModuleLoad() {
		myGUI.getReady();

		// No contents yet added here
		//myGUI.showTestPopup("Currently no contents here ... ");
	}

	/**
	 * Gets the ID of the current session
	 * 
	 * @return the session ID of the current session
	 */
	static String getSessionID() throws NoAccessException {
		String sid = Cookies.getCookie(COOKIE_SESSION);
		if (sid == null) {
			sid = createNewSession();
		}
		return sid;
	}

	/**
	 * Creates a new session
	 * 
	 * @return the session id of the newly generated session
	 */
	private static String createNewSession() {
		return "session.getCookie()";
	}

	
	
	
	public void changeSite(char c) {
		switch (c) {
		case 'i':
			myGUI.showImpressum();
			break;
		case 'l':
			myGUI.showLoginPopup();
			break;
		case 'r':
			myGUI.showRegisterPopup();
			break;	
		default:	
		}
	}

	public void login(String user, String pw){
	
			try {
				session.login(user, pw, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							myGUI.showTestPopup("No AccessFailure");
						}

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								myGUI.showTestPopup("Login yes");
							} else
							{
								myGUI.showTestPopup("No AccessBooleanNO");
							}
						}
					});
			} catch (NoAccessException e) {
				myGUI.showTestPopup("No AccessException");
				e.printStackTrace();
			}
	}
	
	public void register(String user, String mail, String pw){
		
		//Todo....
}
	
	
	
	
	

}
