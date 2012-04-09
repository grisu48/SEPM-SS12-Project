package org.smartsnip.client;

import org.smartsnip.core.ISessionObserver;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Session;

public class Controller {
	/** Singleton instance of the controller */
	public static Controller instance;

	/**
	 * Observer of the session
	 * 
	 */
	private final ISessionObserver sessionObserver = new ISessionObserver() {

		@Override
		public void notification(Session source, Notification notification) {
			// TODO Auto-generated method stub

		}

		@Override
		public void logout(Session source) {
			// TODO Auto-generated method stub

		}

		@Override
		public void login(Session source, String username) {
			// TODO Auto-generated method stub

		}
	};

	/** Static constructor initialises the singleton instance */
	static {
		instance = new Controller();
	}

	/** Private constructor for Singleton pattern */
	private Controller() {
	}

	// zuständig für kommunikation mit dem server
	// verwaltet die GUI

}
