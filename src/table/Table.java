package table;

import javax.swing.table.AbstractTableModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 16.09.2016.
 */
public class Table extends AbstractTableModel implements Serializable {
    private String name;
    private List<Row> rows;
    private List<ColumnInfo> columnsInfo;

    public Table(String name) {
        this.name = name;
        this.rows = new ArrayList<>();
        this.columnsInfo = new ArrayList<>();
    }

    public Table(String name, List<ColumnInfo> columnsInfo) {
        this.name = name;
        this.rows = new ArrayList<>();
        this.columnsInfo = columnsInfo;
    }

    public String getName() { return this.name; }

    public void addRow() {
        Row newRow = new Row(columnsInfo);
        rows.add(newRow);
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public void deleteRow(int index) {
        if (index < 0 || index >= rows.size()) {
            throw new IndexOutOfBoundsException();
        }

        rows.remove(index);
    }

    public Row getRow(int index) {
        if (index < 0 || index >= rows.size()) {
            throw new IndexOutOfBoundsException();
        }

        return rows.get(index);
    }

    public Table searchInTable(String pattern) {
        Table table = new Table("SearchResults", this.columnsInfo);

        for (Row row : rows) {
            for (Object val : row) {
                String sVal = val.toString();

                if (sVal.contains(pattern)) {
                    table.addRow(row);
                }
            }
        }

        return table;
    }

    public void addColumn (String columnName, Class columnType) {
        columnsInfo.add(new ColumnInfo(columnName, columnType));

        for (Row row : rows) {
            row.addDefaultValue(columnType);
        }
    }

    public void addColumn (ColumnInfo columnInfo) {
        columnsInfo.add(columnInfo);

        for (Row row : rows) {
            row.addDefaultValue(columnInfo.getColumnType());
        }
    }


    public void deleteColumn(String columnName) {
        for (int i = 0; i < columnsInfo.size(); ++i) {
            if (columnsInfo.get(i).getColumnName().equals(columnName)) {
                columnsInfo.remove(i);

                for (Row row : rows) {
                    row.removeColumn(i);
                }
            }
        }
    }

    public void changeValueInRow(int rowIndex, int position, Object value) {
        if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException();
        }

        rows.get(rowIndex).setValue(position, value);
    }

    public Object[][] getTableData() {
        Object[][] data = new Object[rows.size()][columnsInfo.size()];

        int i = -1, j;

        for (Row row : rows) {
            ++i;
            j = 0;

            for (Object value : row) {
                data[i][j] = value;
                ++j;
            }
        }

        return data;
    }

    public Object[] getColumnsNames() {
        Object[] names = new Object[columnsInfo.size()];
        int i = 0;

        for (ColumnInfo ci : columnsInfo) {
            names[i++] = ci.getColumnName();
        }

        return names;
    }

    public String toJson()
    {
        StringBuilder json = new StringBuilder();

        json.append(name + '\n');

        Integer columnsCount = columnsInfo.size();
        Integer rowsCount = rows.size();

        json.append(columnsCount.toString() + '\n');
        json.append(rowsCount.toString() + '\n');

        for (ColumnInfo ci : columnsInfo)
        {
            json.append(ci.toJson() + '\n');
        }

        for (Row row : rows)
        {
            json.append(row.toJson() + '\n');
        }

        return json.toString();
    }

    public List<ColumnInfo> getColumns()
    {
        return columnsInfo;
    }

    public static Table parseJson(String json)
    {
        String[] lines = json.split("\n");

        String nm = lines[0].trim();

        Table table = new Table(nm);

        int columnsCount = Integer.parseInt(lines[1].trim());
        int rowsCount = Integer.parseInt(lines[2].trim());

        for (int i = 3; i < columnsCount + 3; ++i)
        {
            table.addColumn(ColumnInfo.parseJson(lines[i].trim()));
        }

        for (int i = columnsCount + 3; i < columnsCount + 3 + rowsCount; ++i)
        {
            table.addRow(Row.parseJson(lines[i].trim(), table.getColumns()));
        }

        return table;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columnsInfo.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnsInfo.get(columnIndex).getColumnName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnsInfo.get(columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getValue(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException();
        }

        try {
            rows.get(rowIndex).setValue(columnIndex, aValue);
        } catch (Exception ex) {
            return;
        }
    }


    private static Class stringToClass(String string) {
        switch (string) {
            case "Integer":
                return Integer.class;
            case "Double":
                return Double.class;
            case "Character":
                return Character.class;
            case "String":
                return String.class;
            case "StringInval":
                return StringInval.class;
            default:
                return null;
        }
    }


    private String classToString(Class _class) {
        if (_class.equals(Integer.class)) {
            return "Integer";
        } else if (_class.equals(Double.class)) {
            return "Double";
        } else if (_class.equals(Character.class)) {
            return "Character";
        } else if (_class.equals(String.class)) {
            return "String";
        } else if (_class.equals(StringInval.class)) {
            return "StringInval";
        } else {
            return null;
        }
    }
}
