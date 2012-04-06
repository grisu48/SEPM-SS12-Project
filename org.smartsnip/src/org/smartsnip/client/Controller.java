package org.smartsnip.client;

public class Controller {
	/** Singleton instance of the controller */
	public static Controller instance;

	/** Static constructor initialises the singleton instance */
	static {
		instance = new Controller();
	}

	/** Private constructor for Singleton pattern */
	private Controller() {
	}

	// observer pattern??
	// zuständig für kommunikation mit dem server
	// verwaltet die GUI
}
