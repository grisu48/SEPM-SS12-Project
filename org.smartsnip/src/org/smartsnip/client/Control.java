package org.smartsnip.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
	
	
	public static void testCommunication() {
		
		final ICommunicationAsync firstTest = GWT.create(ICommunication.class);
		
		firstTest.testTalk("test",
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
