package com.DataProcessing;

import javax.print.Doc;
import java.io.*;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class DataProcessing
{
	static Hashtable users = new java.util.Hashtable<String, User>();
	static Hashtable docs = new java.util.Hashtable<String, Documentary>();
	static boolean connectedToDB;
	static String DB_pathname = "D:\\untitled1\\DATAbase";
	static String User_r_pathname = "users.txt";
	static String File_r_pathname = "files";
	static String Doc_r_pathname = "docs.txt";
	static public User search(String name)
	{
		if(users.containsKey(name))
			return (User) users.get(name);
		else
			return null;
	}
	static public boolean searchUser(String name)
	{
		return users.containsKey(name);
	}
	static public Enumeration<User> getAllUser()
	{
		Enumeration<User> e = users.elements();
		return e;
	}
	static public boolean update(String name, String password, int role)
	{
		if(users.containsKey(name))
		{
			users.remove(name);
			User new_user;
			switch (role)
			{
				case 1: new_user = new Administrator(name, password); break;
				case 2: new_user = new Operator(name, password); break;
				case 3: new_user = new Browser(name, password); break;
				default: System.out.println("wrong input"); return false;
			}
			users.put(name, new_user);
			return true;
		}
		else
			return false;
	}
	static public boolean insert(String name, String password, int role)
	{
		if(users.containsKey(name))
			return false;
		else
		{
			User new_user;
			switch (role)
			{
				case 1: new_user = new Administrator(name, password); break;
				case 2: new_user = new Operator(name, password); break;
				case 3: new_user = new Browser(name, password); break;
				default: System.out.println("wrong input"); return false;
			}
			users.put(name, new_user);
			return true;
		}
	}
	static public boolean delete(String name)
	{
		if(users.containsKey(name))
		{
			users.remove(name);
			return true;
		}
		else
			return false;
	}
	static void Initialize() throws FileNotFoundException
	{
		//import data from database;
		File DB = new File(DB_pathname);
		File USERS = new File(DB, User_r_pathname);
		File DOCS = new File(DB, Doc_r_pathname);
		FileInputStream Input_USERS = new FileInputStream(USERS);
		FileInputStream Input_DOCS = new FileInputStream(DOCS);
		if (USERS.exists())
		{
			Scanner s = new Scanner(Input_USERS).useDelimiter("\r");
			while(true)
			{
				String name;
				String password;
				int role;
				{

					if(s.hasNext())
					{
						name = s.next();
						System.out.println("the read name is "+name);
					}
					else
						break;
					if(s.hasNext())
					{
						password = s.next();System.out.println("the read password is "+password);
					}
					else
						break;
					if(s.hasNext())
					{
						String a = s.next();
						System.out.println("the read role is "+a);role = Integer.parseInt(a);
					}
					else break;
					DataProcessing.insert(name, password, role);
				}
			}

		}
		else
			throw new FileNotFoundException(USERS.getAbsolutePath());
		if (DOCS.exists())
		{
			String ID, creator, description, fileName;
			Timestamp timestamp;
			Scanner s = new Scanner(Input_DOCS).useDelimiter("\r");
			while(true)
			{
			
				if(s.hasNext())
					ID = s.next();
				else
					break;
				if(s.hasNext())
					creator = s.next();
				else
					break;
				if(s.hasNext())
					timestamp = Timestamp.valueOf(s.next());
				else break;
				if(s.hasNext())
					description = s.next();
				else break;
				if(s.hasNext())
					fileName = s.next();
				else break;
				DataProcessing.insertDocument(ID, creator, timestamp, description, fileName);
				System.out.println("the read filename in init is: "+ID);
			}

		}
		else
			throw new FileNotFoundException(USERS.getAbsolutePath());
	try
	{
		Input_DOCS.close();
		Input_USERS.close();
	}catch(IOException ex)
	{
		System.out.println(ex.toString());
	}
	}
	static Documentary searchDocument(String ID)
	{
		return (Documentary) docs.get(ID);
	}
	static Enumeration<Documentary> getAllDocuments()
	{
		System.out.println(docs.size());
		if(0 == docs.size())
			return null;
		Enumeration<Documentary> e = docs.elements();
		return e;
	}
	static boolean insertDocument(String ID, String creator, Timestamp t, String d, String f)
	{
		if(docs.containsKey(ID))
			return false;
		else
		{
			Documentary new_doc = new Documentary(ID, creator, t, d, f);
			docs.put(ID, new_doc);
			return true;
		}
	}
	static void disconnetFromDB()
	{
		connectedToDB = false;
	}
	static void saveData()
	{
		String str;
		User u;
		Documentary doc;
		byte bytes[];
		File DB = new File(DB_pathname);
		File USERS = new File(DB, User_r_pathname);
		File DOCS = new File(DB, Doc_r_pathname);

		FileOutputStream Output_USERS = null;
		try
		{
			Output_USERS = new FileOutputStream(USERS);
		}catch(FileNotFoundException e)
		{
			System.out.println("User list not found.");
		}
		FileOutputStream Output_DOCS = null;
		try
		{
			Output_DOCS = new FileOutputStream(DOCS);
		}catch(FileNotFoundException e)
		{
			System.out.println("No file directory.");
		}
		Enumeration<User> e = getAllUser();
		while(e.hasMoreElements())
		{
			try
			{
				u = e.nextElement();
				str = u.getName() + "\r";
				bytes = str.getBytes();
				Output_USERS.write(bytes);
				str = u.password + "\r";
				bytes = str.getBytes();
				Output_USERS.write(bytes);
				str = u.getRole() + "\r";
				bytes = str.getBytes();
				Output_USERS.write(bytes);
			}catch(IOException ex)
			{
				System.out.println("initialization shut down due to reading error.");
			}
		}

		Enumeration<Documentary> d = getAllDocuments();
		if(d != null)
			while(d.hasMoreElements())
			{
				try
				{
					doc = d.nextElement();
					str = doc.getID() + "\r";
					bytes = str.getBytes();
					Output_DOCS.write(bytes);

					str = doc.getCreator() + "\r";
					bytes = str.getBytes();
					Output_DOCS.write(bytes);

					str = doc.getTimestamp().toString() + "\r";
					bytes = str.getBytes();
					Output_DOCS.write(bytes);

					str = doc.getDescription() + "\r";
					bytes = str.getBytes();
					Output_DOCS.write(bytes);

					str = doc.getFileName() + "\r";
					bytes = str.getBytes();
					Output_DOCS.write(bytes);
				}catch(IOException ex)
				{
					System.out.println("initialization shut down due to reading error.");
				}

			}

	}





}
