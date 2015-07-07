package net.senmori.headselector.util;

public class Reference {

	public static final String SQLITE_TABLE_NAME = "heads";

	public static class Permissions
	{
		
	}
	
	public static class SQLError
	{
		public static final String SQLITE_CONNECTION_EXECUTE = "Couldn't execute MySQL statement: ";
		public static final String SQLITE_CONNECTION_CLOSE = "Failed to close MySQL connection: ";
		public static final String SQLITE_NO_CONNECTION = "Unable to retreive MYSQL connection: ";
		public static final String SQLITE_NO_TABLE_FOUND = "Database Error: No Table Found";
	}
}
