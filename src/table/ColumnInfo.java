package table;

import java.io.Serializable;

/**
 * Created by Artem on 17.09.2016.
 */
public class ColumnInfo implements Serializable {
    private String columnName;
    private Class columnType;

    public ColumnInfo(String columnName, Class columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public Class getColumnType() {
        return columnType;
    }

    public String toJson()
    {
        return columnName + "#" + typeToString(columnType.toString());
    }

    public static ColumnInfo parseJson(String json)
    {
        String[] vals = json.split("#");
        return new ColumnInfo(vals[0].trim(), stringToClass(vals[1].trim()));
    }

    private static Class stringToClass(String st)
    {
        if (st.equals(Integer.class.toString()) || st.equals("Integer"))
        {
            return Integer.class;
        }
        else if (st.equals(Double.class.toString()) || st.equals("Double"))
        {
            return Double.class;
        }
        else if (st.equals(Character.class.toString()) || st.equals("Character"))
        {
            return Character.class;
        }
        else if (st.equals(String.class.toString()) || st.equals("String"))
        {
            return String.class;
        }
        else
        {
            return String.class;
        }
    }

    private String typeToString(String st)
    {
        if (st.equals(Integer.class.toString()))
        {
            return "Integer";
        }
        else if (st.equals(Double.class.toString()))
        {
            return "Double";
        }
        else if (st.equals(Character.class.toString()))
        {
            return "Character";
        }
        else if (st.equals(String.class.toString()))
        {
            return "String";
        }
        else
        {
            return "String";
        }
    }
}
