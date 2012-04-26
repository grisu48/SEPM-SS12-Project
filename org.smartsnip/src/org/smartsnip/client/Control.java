package org.smartsnip.client;



import org.smartsnip.core.Session;
import org.smartsnip.shared.NoAccessException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The controller according to a MVC pattern.
 * 
 * The controller is implemented as Singleton. Each call must be in a static way
 * 
 * The controller is the main class in the client package.
 * As the name says it controls the GUI and on the other side it is responsible for the communication with the server
 * 
 * 
 */
public class Control implements EntryPoint {

	private static Control instance = null;
	private final static String COOKIE_SESSION = "smartsnip_SID";
	private final static ICommunicationAsync myComm = GWT.create(ICommunication.class);
	
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
	Control.testCommunication();
	
	}
	
	
	
	
	
	/**
	 * Gets the ID of the current session
	 * 
	 * @return the session ID of the current session
	 */
	static String getSessionID() throws NoAccessException {
		String sid = Cookies.getCookie(COOKIE_SESSION);
		if (sid == null) sid = createNewSession();
		return sid;
	}
	
	/**
	 * Creates a new session
	 * 
	 * @return the session id of the newly generated session
	 */
	private static String createNewSession()  {
		return "session.getCookie()";
	} 
	
	
	
	public static void testCommunication() {
		
		myComm.testTalk("test",
				new AsyncCallback<String>() {
			
					public void onFailure(Throwable caught) {
						myGUI.showTestPopup("fail");
						//System.out.print(caught.toString());
						//System.out.print("/////////////////////////");
					}
					public void onSuccess(String result) {
						myGUI.showTestPopup(result);
					}
				});
		
	}
	
	
}
