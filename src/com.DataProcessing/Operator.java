package com.DataProcessing;

import java.io.*;
import java.sql.Timestamp;
import java.util.Scanner;

public class Operator extends User
{
	public void showMenu()
	{
		gui.showOperatorMenu(this);
	}
	public Operator(String nm, String pswd)
	{
		name = nm;
		password = pswd;
		role = 2;
		loggedIn = false;
		//DataProcessing.insert(name, password, role);
	}
}
