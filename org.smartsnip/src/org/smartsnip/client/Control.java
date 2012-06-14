package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.ISession;
import org.smartsnip.shared.ISessionAsync;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.ISnippetAsync;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSnippet;
import org.smartsnip.shared.XUser;

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

	/** User currently logged in */
	private final XUser user = new XUser("Guest", "noreal", "nomail");
	/** Indicating if logged in */
	private boolean loggedIn = false;

	/** Singleton instance */
	private static Control instance = null;
	/** Session cookie */
	private final static String COOKIE_SESSION = ISession.cookie_Session_ID;

	/** Search entity */
	public static final Search search = new Search();

	/** Session proxy object, used for RPC in GWT */
	public final static ISessionAsync proxySession = ISession.Util.getInstance();
	/** Snippet proxy object, used for RPC in GWT */
	public final static ISnippetAsync proxySnippet = ISnippet.Util.getInstance();
	/** Main GUI distributor */
	public final static GUI myGUI = new GUI();

	private Control() {
	}

	public static Control getInstance() {
		if (instance == null) {
			instance = new Control();
		}
		return instance;
	}

	/**
	 * This <em>JSNI</em> method includes the {@code prettyPrint()} function of
	 * the <em>google-code-prettify</em> package. It is made up of a bundle of
	 * <em>JavaScript</em> files in the folder <em>googleCodePrettify</em> and
	 * the {@code prettify.css} file.
	 */
	public static native void prettyPrint() /*-{
		$wnd.prettyPrint();
	}-*/;

	/**
	 * The main method, which is loaded on start or refresh
	 * 
	 * 
	 */
	@Override
	public void onModuleLoad() {
		proxySession.getSessionCookie(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				myGUI.showTestPopup("Error getting session cookie: " + caught.getMessage());
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
		// XXX: Paul - hab' das ich gemacht?!! Ergibt keinen Sinn auser ich
		// dachte
		// ich will das noch machen ...
		return "session.getCookie()";
	}

	/**
	 * Changes the Smartsnip-Site
	 * 
	 * @param char A char which indicates which change should be done
	 * 
	 */
	public void changeSite(char c) {
		switch (c) {
		case 'i':
			myGUI.showImpressum();
			break;
		case 'u':
			// TODO Remove this
			System.out.println("THIS SOLD BE REMOVED");
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
		case 'm':
			myGUI.showModPage();
			break;
		default:
		}
	}

	/**
	 * Change to show one snippet
	 * 
	 * @param XSnippet
	 *            The Snippet which should be shown
	 * 
	 */
	public void changeToSnipPage(XSnippet snip) {
		myGUI.showSnipPage(snip);
	}

	/**
	 * Calls a login on the server
	 * 
	 * @param String
	 *            the username
	 * @param String
	 *            the password
	 * 
	 */
	public void login(final String user, final String pw, final Login login) {

		try {
			proxySession.login(user, pw, new AsyncCallback<Boolean>() {

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

	/**
	 * Calls a registration on the server
	 * 
	 * @param String
	 *            the username
	 * @param String
	 *            the mailadress
	 * @param String
	 *            the password
	 * 
	 */
	public void register(final String user, final String mail, final String pw, final Register register) {
		if (user.isEmpty() || mail.isEmpty() || pw.isEmpty()) return;

		proxySession.registerNewUser(user, pw, mail, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof NoAccessException) {
					register.registerFailure("Access denied");
				} else
					register.registerFailure("Unknown error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				register.registerSuccess();
			}
		});
	}

	/**
	 * Gets the local username
	 * 
	 * @return String the username
	 */
	public String getUsername() {
		return user.username;
	}

	/**
	 * Gets the local usermail
	 * 
	 * @return String email adress
	 */
	public String getUserMail() {
		return user.email;
	}

	/**
	 * Gets the login status
	 * 
	 * @return boolean login status
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Updates the local variables with the information on the server
	 * 
	 */
	public void refresh() {

		proxySession.isLoggedIn(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				handleException("Error getting session login status", caught);
			}

			@Override
			public void onSuccess(Boolean result) {
				loggedIn = result;
				myGUI.myMeta.update();
				myGUI.mySearchArea.update();
			}
		});

		proxySession.getUser(user.username, new AsyncCallback<XUser>() {

			@Override
			public void onFailure(Throwable caught) {
				handleException("Error while refreshing the user", caught);

			}

			@Override
			public void onSuccess(XUser result) {
				if (result == null || result.username.isEmpty()) user.username = "Guest";
				else {
					user.username = result.username;
					user.email = result.email;
					user.realname = result.realname;
				}
				myGUI.myMeta.update();
				myGUI.mySearchArea.update();

			}
		});

	}

	private void handleException(String message, Throwable cause) {
		// TODO Write me!
	}

	/**
	 * Send a logout request to the server
	 * 
	 */
	public void logout() {
		proxySession.logout(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				handleException("Logout went wrong", caught);
			}

			@Override
			public void onSuccess(Void result) {
				refresh();
				myGUI.myMeta.update();
				myGUI.showSearchPage();
			}
		});

	}

	/**
	 * writes a comment to the server
	 * 
	 * @param String
	 *            the comment
	 * @param long the hash of the current snippet
	 */
	public void writeComment(String comment, long hash) {
		if (comment == null || comment.isEmpty()) return;

		ISnippetAsync snippetProxy = ISnippet.Util.getInstance();

		snippetProxy.addComment(hash, comment, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof NoAccessException) {
					if (!isLoggedIn()) myGUI.showErrorPopup("You must login first");
					else
						myGUI.showErrorPopup("Access denied.");

				} else
					myGUI.showErrorPopup("Creation of new comment failed", caught);
			}

			@Override
			public void onSuccess(Void result) {
				myGUI.myCommentArea.update();
			}
		});
	}

	/**
	 * adds a snippet to the favorite list of a user
	 * 
	 * @param long the hash of the current snippet
	 */
	public void toFav(long hash) {
		ISnippetAsync snippetProxy = ISnippet.Util.getInstance();

		snippetProxy.addToFavorites(hash, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof NoAccessException) {
					myGUI.showErrorPopup("Access denial", caught);
				} else if (caught instanceof NotFoundException) {
					myGUI.showErrorPopup("Snippet cannot be found by server", caught);
				} else {
					myGUI.showErrorPopup("Adding snippet to favorites failed", caught);
				}
			}

			@Override
			public void onSuccess(Void result) {

			}
		});
	}

	/**
	 * changes a snippet (with server call)
	 * 
	 * @param XSnippet
	 *            a snippet
	 */
	public void changeSnippet(XSnippet snip) {

		ISnippet.Util.getInstance().edit(snip, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Control.myGUI.mySnipArea.update();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * writes a new password on the server
	 * 
	 * @param String
	 *            new Password
	 * @param String
	 *            new Password
	 */
	public void setPassword(String pw1, String pw2) {
		if (pw1.equals(pw2)) {

			IUser.Util.getInstance().setPassword(pw1, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					myGUI.myPersonalArea.update(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					myGUI.myPersonalArea.update(false);
				}
			});

		} else {
			myGUI.myPersonalArea.update(false);
		}

	}

	/**
	 * shows the snippet of the day
	 * 
	 */
	public void showSnippetOfDay() {
		ISnippet.Util.getInstance().getSnippetOfDay(new AsyncCallback<XSnippet>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(XSnippet result) {
				changeToSnipPage(result);
			}

		});

	}
}
