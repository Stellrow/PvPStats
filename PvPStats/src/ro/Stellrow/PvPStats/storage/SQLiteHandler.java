package ro.Stellrow.PvPStats.storage;


import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class SQLiteHandler {
    private File pluginDataFolder;
    private String fileName;
    private String sqlDefaultStatement;
    private String table;

    private Connection connection;

    //We ask for the plugins folder,how the file should be named,how the table should be named(for later uses),and a default statement allowing to specify columns
    public SQLiteHandler(File pluginDataFolder,String fileName,String table,String sqlDefaultStatement){
        this.pluginDataFolder=pluginDataFolder;
        this.fileName=fileName;
        this.sqlDefaultStatement = sqlDefaultStatement;
        this.table=table;
    }


    /*
        An example on how to use the constructor

        private SQLiteHandler sqLiteHandler = new SQLiteHandler(this.getDataFolder(),"chunkHoppers","chunkHoppers",
            "CREATE TABLE IF NOT EXISTS chunkHoppers (" +
                    "`uuid` INTEGER PRIMARY KEY," +
                    "`world` varchar(50) NOT NULL," +
                    "`x` INT NOT NULL," +
                    "`y` INT NOT NULL," +
                    "`z` INT NOT NULL" +
                    ");"
            );


    */


    public Connection getSQLConnection() {
        if(!pluginDataFolder.exists()){
            pluginDataFolder.mkdirs();
        }
        File dataFolder = new File(pluginDataFolder, fileName+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    ///////
//Call this onEnable() "load()"
/////////
    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(sqlDefaultStatement);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void close(PreparedStatement ps,Connection conn){
        try {
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Add method
    public void addPlayer(UUID toAdd){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + table + " (uuid,kills,deaths,topkillstreak,level,experience,activekillstreak) VALUES(?,?,?,?,?,?,?)");
            ps.setString(1,toAdd.toString());
            ps.setInt(2,0);
            ps.setInt(3,0);
            ps.setInt(4,0);
            ps.setInt(5,1);
            ps.setInt(6,0);
            ps.setInt(7,0);
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps,conn);
        }
    }
    public boolean playerExists(UUID toCheck){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM "+table +" WHERE uuid=?");
            ps.setString(1,toCheck.toString());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
        }finally {
            close(ps,conn);
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public DataSet getPlayerData(UUID toGet){
        DataSet ds = new DataSet();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT uuid,kills,deaths,topkillstreak,level,experience,activekillstreak FROM "+table +" WHERE uuid=?");
            ps.setString(1,toGet.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                Bukkit.getConsoleSender().sendMessage("Found entry");
                ds.kills = rs.getInt("kills");
                ds.deaths = rs.getInt("deaths");
                ds.topkillstreak = rs.getInt("topkillstreak");
                ds.level = rs.getInt("level");
                ds.experience = rs.getInt("experience");
                ds.activeKillStreak = rs.getInt("activekillstreak");
            }
            return ds;

        } catch (SQLException e) {
        }finally {
            close(ps,conn);
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ds;
    }
    public void updatePlayerData(UUID toUpdate,DataSet dataset){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO "+table +" (kills,deaths,topkillstreak,level,experience,activekillstreak) VALUES" +
                    "(?,?,?,?,?,?) WHERE uuid=?");
            ps.setInt(1,dataset.kills);
            ps.setInt(2,dataset.deaths);
            ps.setInt(3,dataset.topkillstreak);
            ps.setInt(4,dataset.level);
            ps.setInt(5,dataset.experience);
            ps.setInt(6,dataset.activeKillStreak);
            ps.setString(7,toUpdate.toString());
            ps.executeUpdate();
            return;

        } catch (SQLException e) {
        }finally {
            close(ps,conn);
        }
    }
}