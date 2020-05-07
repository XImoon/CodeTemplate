package cn.ximoon.plugin;

import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DBUtil {

    private static DBUtil INSTANCE;
    private Connection mConnection;
    private static final int VERSION = 1;

    public static void main(String[] args) {
        DBUtil.getInstance().connect();
        DBUtil.getInstance().queryKey("123 456");
//        DBUtil.getInstance().queryAll();
        DBUtil.getInstance().close();
    }

    public static DBUtil getInstance(){
        if (null == INSTANCE){
            synchronized (DBUtil.class){
                INSTANCE = new DBUtil();
                INSTANCE.connect();
            }
        }
        return INSTANCE;
    }

    private void connect() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(getDBURL());
            mConnection = ds.getConnection();
            int curVersion = getVersion();
            if (curVersion < VERSION){
                updateVersion(curVersion);

            }
        }
        catch( Exception e ) {
            e.printStackTrace ( );
        }
    }

    private String getDBURL() {
        Properties properties = System.getProperties();
        String url;
        url = properties.getProperty("idea.plugins.path");
        if (null == url || 0 == url.replace(" ", "").length()){
            url = properties.getProperty("idea.config.path");
            if (null == url || 0 == url.replace(" ", "").length()){
                url = properties.getProperty("user.home");
                url += "\\.";
                url +=  properties.getProperty("idea.paths.selector");
                url +=  "\\config\\plugins";
            } else {
                url += "\\plugins";
            }
        }
        url = "jdbc:sqlite:" + url + "\\CodeTemplatePlugin\\templete.db";
        return url;
    }
    private int getVersion(){
        int version = 0;
        try {
            PreparedStatement preparedStatement = mConnection.prepareStatement("PRAGMA USER_VERSION");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                version = resultSet.getInt("USER_VERSION");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return version;
    }

    private void updateVersion(int oldVersion){
        switch (oldVersion){
            case 0:
                createTable();
                break;
        }
        try {
            PreparedStatement preparedStatement = mConnection.prepareStatement("PRAGMA USER_VERSION = " + VERSION);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(){
        try {
            DatabaseMetaData metaData = mConnection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, "templete.db", "tb_command", new String[]{"TABLE"});
            if (!resultSet.next()){
                PreparedStatement preparedStatement = mConnection.prepareStatement("CREATE TABLE tb_command(_id INTEGER PRIMARY KEY AUTOINCREMENT, fkey_words TEXT, fdescription TEXT, ftag TEXT, fpattern INTEGER, fparam TEXT, fdetail TEXT, faccount TEXT, fdate TEXT)");
                preparedStatement.execute();
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DefaultListModel<Map<String, String>> queryAll(){
        DefaultListModel<Map<String, String>> data = new DefaultListModel();
        Map<String, String> resMap;
        try {
            PreparedStatement preparedStatement = mConnection.prepareStatement("SELECT * FROM tb_command");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()){
                resMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String key = metaData.getColumnName(i);
                    resMap.put(key, String.valueOf(resultSet.getObject(key)));
                }
//                getData(resMap, resultSet);
                data.addElement(resMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public DefaultListModel<Map<String, String>> queryKey(String key){
        String[] words = key.split(" ");
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_command WHERE");
        StringBuilder orderby = new StringBuilder("ORDER BY (CASE WHEN fkey_words = '");
        orderby.append(key).append("' THEN 1 WHEN fkey_words LIKE '%").append(key).append("%' THEN 2 WHEN");
        int length = words.length;
        for (int i = 0; i < length; i++) {
            sql.append(" fkey_words LIKE ? OR");
            if (0 == i){
                orderby.append(" fkey_words LIKE ").append("'%" + words[i] + "%'");
            } else {
                orderby.append(" OR fkey_words LIKE ").append("'%" + words[i] + "%'");
            }
        }
        orderby.append(" THEN 3 ELSE 4 END)");
        sql.delete(sql.length() - 2, sql.length());
        sql.append(orderby.toString());
        DefaultListModel<Map<String, String>> data = new DefaultListModel();
        Map<String, String> resMap;
        try {
            PreparedStatement preparedStatement = mConnection.prepareStatement(sql.toString());
            for (int i = 1; i <= length; i++) {
                preparedStatement.setString(i, "%" + words[i-1] + "%");
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                resMap = new HashMap<>();
                getData(resMap, resultSet);
                data.addElement(resMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void getData(Map<String, String> resMap, ResultSet resultSet) throws SQLException {
        resMap.put("_id", String.valueOf(resultSet.getLong("_id")));
        resMap.put("fkey_words",resultSet.getString("fkey_words"));
        resMap.put("fdescription",resultSet.getString("fdescription"));
        resMap.put("ftag",resultSet.getString("ftag"));
        resMap.put("fpattern", String.valueOf(resultSet.getLong("fpattern")));
        resMap.put("fparam",resultSet.getString("fparam"));
        resMap.put("fdetail",resultSet.getString("fdetail"));
        resMap.put("faccount",resultSet.getString("faccount"));
        resMap.put("fdate",resultSet.getString("fdate"));
    }

    private void close(){
        try {
            mConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Map<String, String> mData) {
        String id = mData.get("_id");
        if (null == id || 0 == id.length()){
            try {
                PreparedStatement preparedStatement = mConnection.prepareStatement("INSERT INTO tb_command (fkey_words, fdescription, ftag, fpattern, fparam, fdetail, faccount, fdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, mData.get("fkey_words"));
                preparedStatement.setString(2, mData.get("fdescription"));
                preparedStatement.setString(3, mData.get("ftag"));
                preparedStatement.setInt(4, 1);
                preparedStatement.setString(5, mData.get("fparam"));
                preparedStatement.setString(6, mData.get("fdetail"));
//                preparedStatement.setString(7, System.getProperty("user.name"));
                preparedStatement.setString(7, "XImoon");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                preparedStatement.setString(8, dateFormat.format(new Date()));
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement preparedStatement = mConnection.prepareStatement("UPDATE tb_command SET fkey_words = ?, fdescription = ?, ftag = ?, fparam = ?, fdetail = ? WHERE _id = ?");
                preparedStatement.setString(1, mData.get("fkey_words"));
                preparedStatement.setString(2, mData.get("fdescription"));
                preparedStatement.setString(3, mData.get("ftag"));
                preparedStatement.setString(4, mData.get("fparam"));
                preparedStatement.setString(5, mData.get("fdetail"));
                preparedStatement.setLong(6, Long.parseLong(mData.get("_id")));
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
