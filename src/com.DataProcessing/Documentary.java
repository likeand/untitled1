package com.DataProcessing;


import java.sql.Timestamp;

public class Documentary
{
	String ID;
	String creator;
	String description;
	String fileName;
	Timestamp timestamp;

	Documentary(String ID, String creator, Timestamp timestamp, String description, String fileName)
	{
		this.ID = ID;
		this.creator = creator;
		this.timestamp = timestamp;
		this.description = description;
		this.fileName = fileName;
	}

	public String getID()
	{
		return ID;
	}

	public void setID(String ID)
	{
		this.ID = ID;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public Timestamp getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp)
	{
		this.timestamp = timestamp;
	}
}
