package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.ISession;
import org.smartsnip.shared.ISessionAsync;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XSearch;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

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

	private String username = "Guest";
	private boolean loggedIn = false;

	private static Control instance = null;
	private final static String COOKIE_SESSION = ISession.cookie_Session_ID;
	private final static ISessionAsync session = ISession.Util.getInstance();

	private final static GUI myGUI = new GUI();

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
		session.getSessionCookie(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				myGUI.showTestPopup("Error getting session cookie: "
						+ caught.getMessage());
			}

			@Override
			public void onSuccess(String cookie) {
				if (cookie == null) {
					// Static Guest session if browser does not
					// supports cookies
					myGUI.showTestPopup("Null-Cookie returned");
					return;
				}
				Cookies.setCookie(COOKIE_SESSION, cookie);
			}
		});
		myGUI.getReady();

		// No contents yet added here
		// myGUI.showTestPopup("Currently no contents here ... ");
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
		case 'p':
			myGUI.showPersonalPage();
			break;
		case 'c':
			myGUI.showContactForm();
			break;
		case 'n':
			myGUI.showCreateSnippetForm();
			break;
		default:
		}
	}

	public void login(final String user, final String pw, final Login login) {

		try {
			session.login(user, pw, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof NoAccessException) {
						login.loginFailure("Wrong username/password");
						return;
					}

					handleException("Error doing the login", caught);

				}

				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						login.loginSuccess();
						refresh();
					} else {
						login.loginFailure("Access denial");
					}
				}
			});
		} catch (NoAccessException e) {
			login.loginFailure("Access denial");
		}
	}

	public void register(final String user, final String mail, final String pw,
			final Register register) {
		if (user.isEmpty() || mail.isEmpty() || pw.isEmpty())
			return;

		session.registerNewUser(user, pw, mail, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof NoAccessException) {
					register.registerFailure("Access denied");
				} else
					register.registerFailure("Unknown error: "
							+ caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				register.registerSuccess();
			}
		});
	}

	public void search(String searchString, List<String> tags,
			List<String> categories, XSearch.SearchSorting sorting, int start,
			int count, final SearchArea searchArea) {
		session.doSearch(searchString, tags, categories, sorting, start, count,
				new AsyncCallback<XSearch>() {

					@Override
					public void onFailure(Throwable caught) {
						String status = searchArea.searchFailed(caught);
						myGUI.updateSearchPage(null, status);
					}

					@Override
					public void onSuccess(XSearch result) {
						String status = searchArea.searchDone(result);
						myGUI.updateSearchPage(result, status);
					}
				});
	}

	public String getUsername() {
		// refresh();
		return username;
	}

	public boolean isLoggedIn() {
		// refresh();
		return loggedIn;
	}

	private void refresh() {
		session.getUsername(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				handleException("Error while refreshing the username", caught);
			}

			@Override
			public void onSuccess(String result) {
				if (result == null)
					result = "";
				if (result.isEmpty())
					result = "Guest";
				username = result;
				myGUI.metaRefresh();
			}
		});
		session.isLoggedIn(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				handleException("Error getting session login status", caught);
			}

			@Override
			public void onSuccess(Boolean result) {
				loggedIn = result;
			}
		});
	}

	private void handleException(String message, Throwable cause) {
		// TODO Write me!
	}

	public void logout() {
		session.logout(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				handleException("Logout went wrong", caught);
			}

			@Override
			public void onSuccess(Void result) {
				refresh();
			}
		});

	}

}
