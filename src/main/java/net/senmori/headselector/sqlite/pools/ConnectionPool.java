package net.senmori.headselector.sqlite.pools;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import net.senmori.headselector.HeadSelector;
import net.senmori.headselector.util.Reference;
import net.senmori.headselector.util.Reference.SQLError;

import org.apache.commons.lang3.StringUtils;

public class ConnectionPool extends Pool{
	
	
	private HashMap<String,String> entries;
	private String sql = "REPLACE INTO " + Reference.SQLITE_TABLE_NAME + " (name,value,category) VALUES(?,?,?)";
	
	//private Connection conn = null;
	//private PreparedStatement ps = null;
	
	public ConnectionPool(String databaseName) 
	{
		super(databaseName);
		entries = new HashMap<String,String>();
	}
	
	@Override
	public void insert(String... args)
	{
		entries.put(args[0], args[1] + "|" + args[2]);

		if(entries.size() % batchSize == 0)
		{
			connection = getConnection();
			try
			{
				int count = 0;
				ps = connection.prepareStatement(sql);
				for(String entry : entries.keySet())
				{
					String[] values = StringUtils.split(entries.get(entry), "|");
					ps.setString(1, entry);
					ps.setString(2, values[0]);
					ps.setString(3, values[1]);
					ps.addBatch();
					if(++count % batchSize == 0)
					{
						ps.executeBatch();
					}
				}
			}
			catch(SQLException e)
			{
				HeadSelector.log.log(Level.SEVERE, SQLError.SQLITE_CONNECTION_EXECUTE, e);
			}
			finally
			{
				try
				{
					if(ps != null || !ps.isClosed())
						ps.close();
					if(connection != null || !connection.isClosed())
						connection.close();
				}
				catch(SQLException ex)
				{
					HeadSelector.log.log(Level.SEVERE, SQLError.SQLITE_CONNECTION_CLOSE, ex);
				}
			}
		}
	}
	
	public void insertRemainingEntries()
	{
		connection = getConnection();
		try
		{
			ps = connection.prepareStatement(sql);
			for(String entry : entries.keySet())
			{
				String[] values = StringUtils.split(entries.get(entry), "|");
				ps.setString(1, entry);
				ps.setString(2, values[0]);
				ps.setString(3, values[1]);
				ps.addBatch();
			}
		}
		catch(SQLException e)
		{
			HeadSelector.log.log(Level.SEVERE, SQLError.SQLITE_CONNECTION_EXECUTE, e);
		}
		finally
		{
			try
			{
				ps.executeBatch();
				if(ps != null || !ps.isClosed())
					ps.close();
				if(connection != null | !connection.isClosed())
					connection.close();
			}
			catch(SQLException ex)
			{
				HeadSelector.log.log(Level.SEVERE, SQLError.SQLITE_CONNECTION_CLOSE, ex);
			}
		}
	}
}
