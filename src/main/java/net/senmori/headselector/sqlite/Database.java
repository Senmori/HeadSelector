package net.senmori.headselector.sqlite;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import net.senmori.headselector.HeadSelector;
import net.senmori.headselector.sqlite.pools.ConnectionPool;
import net.senmori.headselector.sqlite.pools.Pool;
import net.senmori.headselector.util.Reference;
import net.senmori.headselector.util.Reference.SQLError;


public abstract class Database {
    protected HeadSelector plugin;
    protected Connection connection;
    protected Pool connectionPool;
    
    // The name of the table we created back in SQLite class.
    public String table = Reference.SQLITE_TABLE_NAME;

    public Database(HeadSelector instance)
    {
        plugin = instance;
        connectionPool = new ConnectionPool(Reference.SQLITE_TABLE_NAME);
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
        	// check for table existence
            DatabaseMetaData dbData = connection.getMetaData();
            ResultSet rs = dbData.getTables(null, null, "%", null);
            while(rs.next())
            {
            	if(rs.getString(3).equalsIgnoreCase(Reference.SQLITE_TABLE_NAME))
            	{
            		return;
            	}
            }
         
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }
 
    // Search database for any names that have `query` in them and return hashmap of those names and values
	public HashMap<String,String> searchValuesByName(String query) 
    {
		HashMap<String,String> values = new HashMap<String,String>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try 
        {
            conn = getSQLConnection();
            // select all rows that have `query` in their name
            // e.g `sheep` would return on `red sheep`, `Sheep`, `Awesome sheep`, etc.
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE name LIKE '%" + query + "%';");
            rs = ps.executeQuery();
            while(rs.next())
            {
            	// concat the strings for easy storage
            	// to retrieve the category for this item just split at first index of '|'
               values.put(rs.getString("category") + "|" + rs.getString("name"), rs.getString("value"));
            }
        } 
        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, SQLError.SQLITE_CONNECTION_EXECUTE, ex);
        } 
        finally 
        {
            try 
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) 
            {
                plugin.getLogger().log(Level.SEVERE, SQLError.SQLITE_CONNECTION_CLOSE, ex);
            }
        }
        return null;     
    }
    
	// If you want to get all values from the database
	// This should only be called when the plugin is intializing in an async thread to not decrease performance
    public HashMap<String,String> getAllValues()
    {
    	HashMap<String,String> heads = new HashMap<String,String>();
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try
    	{
    		conn = getSQLConnection();
    		ps = conn.prepareStatement("SELECT * FROM " + table + ";");
    		rs = ps.executeQuery();
    		
    		while(rs.next())
    		{
    			heads.put(rs.getString("name"), rs.getString("value"));
    		}
    	} catch(SQLException e)
    	{
    		plugin.getLogger().log(Level.SEVERE, SQLError.SQLITE_CONNECTION_EXECUTE, e);
    	}

    	return null;
    }
    
    // handle inserting records with ConnectionPool
    public void insert(String... args)
    {
    	connectionPool.insert(args);
    }
    
    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't close the PreparedStatement or ResultSet: ", ex);
        }
    }
}