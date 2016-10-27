package database;

import table.Table;

import java.io.*;
import java.util.*;

/**
 * Created by Artem on 16.09.2016.
 */
public class DataBase implements Serializable {
    private String name;
    private Map<String, Table> tables;

    public DataBase(String name) {
        this.name = name;
        tables = new HashMap<>();
    }

    public void createTable(String tableName) {
        if (tables.containsKey(tableName)) {
            throw new IllegalArgumentException("Table with name '" + tableName + "' already exists");
        }

        tables.put(tableName, new Table(tableName));
    }

    public String getName() { return this.name; }

    public void dropTable(String tableName) {
        if (!tables.containsKey(tableName)) {
            throw new IllegalArgumentException("No table with name '" + tableName + "'");
        }

        tables.remove(tableName);
    }

    public void showTable (String tableName) {
        if (!tables.containsKey(tableName)) {
            throw new IllegalArgumentException("No table with name '" + tableName + "'");
        }

        System.out.println(tables.get(tableName));
    }

    public List<String> getTableNames() {
        List<String> names = new ArrayList<>();

        for (String name : tables.keySet()) {
            names.add(name);
        }

        return names;
    }

    public Table getTable(String tableName) {
        if (!tables.containsKey(tableName)) {
            throw new IllegalArgumentException("No table with name '" + tableName + "'");
        }

        return tables.get(tableName);
    }

    public void addTable(Table table) {
        String tName = table.getName();

        if (tables.containsKey(tName)) {
            throw new IllegalArgumentException("DataBase already contains table " + tName);
        }

        tables.put(tName, table);
    }

    public String toJson()
    {
        StringBuilder json = new StringBuilder();

        json.append(name + "\n");

        Integer tablesCount = tables.size();

        json.append(tablesCount.toString() + "\n");

        for (Table table : tables.values())
        {
            json.append(table.toJson() + "&\n");
        }

        return json.toString();
    }

    public static DataBase parseJson(String json)
    {
        String[] lines = json.split("\n");

        String nm = lines[0].trim();
        int tableCount = Integer.parseInt(lines[1].trim());

        String[] tablesJson = json.substring(lines[0].length() + lines[1].length() + 2).split("&");

        DataBase db = new DataBase(nm);

        for (int i = 0; i < tableCount; ++i)
        {
            db.addTable(Table.parseJson(tablesJson[i].trim()));
        }

        return db;
    }

    public void saveDataBase(String fileAddress) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(fileAddress);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
    }

    public static DataBase loadDataBase(String fileAddress) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(fileAddress);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        DataBase db = (DataBase) in.readObject();
        in.close();
        fileIn.close();

        return db;
    }
}
