package net.senmori.headselector.sqlite.pools;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import net.senmori.headselector.HeadSelector;

public abstract class Pool 
{
	protected String databaseName;
	protected final int batchSize = 250;
	
	protected Connection connection;
	protected PreparedStatement ps;
	
	
	public Pool(String databaseName)
	{
		this.databaseName = databaseName;
	}
	
	public abstract void insert(String... args);
	
	
	protected Connection getConnection()
	{
		File dataFolder = new File(HeadSelector.getInstance().getDataFolder(), databaseName+".db");
        // database doesn't exist
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
            	HeadSelector.getInstance().getLogger().log(Level.SEVERE, "File write error: "+ databaseName +".db");
            }
        }
        try {
            if( connection != null && !connection.isClosed() ){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
        	HeadSelector.getInstance().getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
        	HeadSelector.getInstance().getLogger().log(Level.SEVERE, "You need the SQLite JBDC library.");
        }
        return null;
	}

}
