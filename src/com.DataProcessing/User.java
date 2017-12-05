package com.DataProcessing;

abstract public class User
{
	protected String name;
	protected String password;
	protected int role;
	protected boolean loggedIn;

	public String getPassword()
	{   return password;}
	public String getName()
	{   return name;}
	public int getRole()
	{   return role;}
	public boolean getLoggedIn()
	{   return loggedIn;}
	public void setPassword(String p)
	{     password = p;}
	public void setName(String n)
	{     name = n;}
	public void setRole(int r)
	{     role = r;}
	public void setLoggedIn(boolean l)
	{     loggedIn = l;}
	public abstract void showMenu();

	static public void exitSystem()
	{
		System.out.println("exiting...");
		DataProcessing.saveData();
		System.exit(0);
	}
}
