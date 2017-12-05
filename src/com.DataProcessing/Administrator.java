package com.DataProcessing;


public class Administrator extends User
{
	public void showMenu()
	{
		gui.showAdministratorMenu(this);
	}
	public Administrator(String nm, String pswd)
	{
		name = nm;
		password = pswd;
		role = 1;
		loggedIn = false;
		//DataProcessing.insert(name, password, role);
	}
}
