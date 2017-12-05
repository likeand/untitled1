package com.DataProcessing;

import java.util.Scanner;

public class Browser extends User
{
	public void showMenu()
	{
		gui.showBrowserMenu(this);
	}

	public Browser(String nm, String pswd)
	{
		name = nm;
		password = pswd;
		role = 3;
		loggedIn = false;
		//DataProcessing.insert(name, password, role);
	}
}