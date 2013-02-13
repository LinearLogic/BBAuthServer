package com.veltro.blazingbarrels.authserver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains a {@link #authorizedAccounts list of authorized accounts} and provides methods for operating on the list
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public class AccountManager {

	/**
	 * A list of the names of currently authorized account names. All accounts in this list will be able to connect to
	 * BlazingBarrels servers. Account names are converted to lower case before being added to this list.<p>
	 * As this list is accessed by both the main thread and the {@link CommThread}, methods that edit the list are
	 * synchronized to prevent concurrency issues.
	 */
	private static ArrayList<String> authorizedAccounts = new ArrayList<String>();

	/**
	 * Authorizes the provided username by adding it to {@link #authorizedAccounts} list. This method is synchronized
	 * to prevent threads from ending up with two different versions of this list after performing a write operation.
	 * 
	 * @param accountName The name of the account to authorize, if not already authorized
	 */
	public static synchronized void authorize(String accountName) {
		if (!authorizedAccounts.contains(accountName.toLowerCase()))
			authorizedAccounts.add(accountName.toLowerCase());
	}

	/**
	 * Deauthorizes the provided username by removing it from {@link #authorizedAccounts} list. This method is
	 * synchronized to prevent threads from ending up with two different versions of this list after performing a
	 * write operation.
	 * 
	 * @param accountName The name of the account to deauthorize, if currently authorized
	 * @return 'false' if the account was not authorized to begin with, else 'true'
	 */
	public static synchronized boolean deauthorize(String accountName) {
		return authorizedAccounts.remove(accountName.toLowerCase());
	}

	/**
	 * Empties the {@link #authorizedAccounts list of authorized accounts}. This method is synchronized to prevent
	 * threads from ending up with two different versions of this list after performing a write operation.
	 */
	public static synchronized void deauthorizeAll() {
		authorizedAccounts.clear();
	}

	/**
	 * @return The contents of the {@link #authorizedAccounts} list, cast to a String array and sorted alphabetically
	 */
	public static String[] getAuthorizedAccounts() {
		String[] output = new String[authorizedAccounts.size()];
		Arrays.sort(authorizedAccounts.toArray(output));
		return output;
	}

	/**
	 * Checks whether the provided account name is currently authorized
	 * 
	 * @param accountName The name of the account to check
	 * @return 'true' iff the account is authorized (i.e. if the {@link #authorizedAccounts} list contains the account)
	 */
	public static boolean isAuthorized(String accountName) {
		return authorizedAccounts.contains(accountName.toLowerCase());
	}

	/**
	 * @return The number of currently authorized accounts
	 */
	public static int countAuthorizedAccounts() {
		return authorizedAccounts.size();
	}
}
