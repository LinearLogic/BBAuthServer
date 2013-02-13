package com.veltro.blazingbarrels.authserver;

import java.util.Scanner;

/**
 * Program entry point and command handler
 * 
 * @author LinearLogic
 * @version 0.0.4
 */
public class BBAuthServer {

	public static void main(String[] args) {
		CommThread communicator = new CommThread(7777);
		communicator.start();
		Scanner sc = new Scanner(System.in);
		while (true) {
			String[] cmd = sc.nextLine().trim().toLowerCase().split("\\s+");
			String name = cmd[0];
			if (name.equals("/authorize")) {
				if (cmd.length != 2) {
					System.out.println("Usage: /authorize <username>");
					continue;
				}
				if (AccountManager.authorize(cmd[1]))
					System.out.println("Successfully authorized user " + cmd[1]);
				else
					System.out.println("User " + cmd[1] + " is already authorized");
				continue;
			}
			if (name.equals("/deauthorize")) {
				if (cmd.length != 2) {
					System.out.println("Usage: /deauthorize <username/all>");
					continue;
				}
				if (cmd[1].equals("all")) {
					AccountManager.deauthorizeAll();
					System.out.println("Successfully deauthorized all users");
				} else {
					if (AccountManager.deauthorize(cmd[1]))
						System.out.println("Successfully deauthorized user " + cmd[1]);
					else
						System.out.println("User " + cmd[1] + " has not been authorized");
				}
				continue;
			}
			if (name.equals("/help") || name.equalsIgnoreCase("/?")) {
				System.out.println("Commands:\n/authorize <username> - adds the specified user to the list of " +
						"authenticated accounts\n/deauthorize <username/all> - removes the specified user from the " +
						"list of authenticated accounts\n/listusers - lists authenticated accounts in alphabetical " +
						"order\n/stats - displays info about authenticated users\n/stop - halts the authentication " +
						"server, clearing the list of authenticated users");
				continue;
			}
			if (name.equals("/listusers")) {
				if (AccountManager.countAuthorizedAccounts() == 0)
					System.out.println("There are no currently authorized users");
				else {
					System.out.println("Currently authorized users:");
					for (String username : AccountManager.getAuthorizedAccounts())
						System.out.println(username);
				}
				continue;
			}
			if (name.equals("/stats")) {
				System.out.println("<=====[Stats]=====>\nNumber of currently authorized accounts: " +
						AccountManager.countAuthorizedAccounts());
				continue;
			}
			if (name.equals("/stop")) {
				System.out.println("Halting the account authorization server...");
				break;
			}
			System.out.println("Input not recognized. Type /help for a list of available commands.");
		}

		AccountManager.deauthorizeAll();
		sc.close();
		System.out.println("Server successfully termina- ZALGO!!!");
		System.exit(0);
	}
}
