package net.senmori.headselector.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import net.senmori.headselector.sqlite.Database; // import the database class.
import net.senmori.headselector.util.Reference;
import net.senmori.headselector.HeadSelector; // import your main class


public class SQLite extends Database
{
    String dbname;
    public SQLite(HeadSelector instance)
    {
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", Reference.SQLITE_TABLE_NAME); 
    }

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS "+ Reference.SQLITE_TABLE_NAME +" (" + 
            "id INTEGER PRIMARY KEY NOT NULL," + 
    		"name TEXT NOT NULL," + 
            "value  TEXT NOT NULL" +
    		"category TEXT NOT NULL" + 
            ");";

 
    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        // database doesn't exist
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+ dbname +".db");
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
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }
 
    public void load() {
        connection = getSQLConnection();     
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}
