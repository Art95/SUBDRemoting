package table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Artem on 16.09.2016.
 */
public class Row implements Iterable<Object>, Serializable {
    private List<Object> values;
    private List<ColumnInfo> columnsInfo;

    public Row() {
        values = new ArrayList<>();
        columnsInfo = new ArrayList<>();
    }

    public Row(List<ColumnInfo> columnsInfo) {
        values = new ArrayList<>();
        this.columnsInfo = columnsInfo;

        for (ColumnInfo ci : columnsInfo) {
            addDefaultValue(ci.getColumnType());
        }
    }

    public Row(Object[] values, List<ColumnInfo> columnsInfo) {
        this.columnsInfo = columnsInfo;
        this.values = new ArrayList<>();
        int i = 0;

        for (Object value : values) {
            if (value.getClass().equals(String.class) && !columnsInfo.get(i).getColumnType().equals(String.class)) {
                value = stringToObject(value.toString(), columnsInfo.get(i).getColumnType());
            }

            this.values.add(value);
            ++i;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Row)) {
            return false;
        }

        Row objects = (Row) o;

        return values.equals(objects.values);

    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    public void setValue(int position, Object newValue) {
        if (position < 0 || position >= values.size()) {
            throw new IndexOutOfBoundsException();
        }

        Class oldValueClass = columnsInfo.get(position).getColumnType();
        Class newValueClass = newValue.getClass();


        if (!oldValueClass.equals(newValueClass)) {
            if (newValueClass.equals(String.class)) {
                String sNewValue = (String) newValue;

                try {
                    newValue = stringToObject(sNewValue, oldValueClass);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("newValue doesn't fit column's type");
                }
            }
        }

        values.set(position, newValue);
    }

    public Object getValue (int position) {
        if (position < 0 || position >= values.size()) {
            throw new IndexOutOfBoundsException();
        }

        return values.get(position);
    }

    public void addValue(Object value) {
        if (values.size() == columnsInfo.size()) {
            throw new IndexOutOfBoundsException("More values than columns");
        }

        Class valueClass = value.getClass();
        Class neededClass = columnsInfo.get(values.size()).getColumnType();

        if (!valueClass.equals(neededClass)) {
            if (valueClass.equals(String.class)) {
                String sValue = (String) value;

                try {
                    value = stringToObject(sValue, neededClass);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("newValue doesn't fit column's type");
                }
            }
        }

        values.add(value);
    }

    public void removeColumn(int index) {
        values.remove(index);
    }

    public void addDefaultValue(Class columnType) {
        if (columnType.equals(Integer.class)) {
            values.add(0);
        } else if (columnType.equals(Double.class)) {
            values.add(0.0);
        } else if (columnType.equals(Character.class)) {
            values.add('^');
        } else if (columnType.equals(String.class)) {
            values.add("nil");
        } else if (columnType.equals(StringInval.class)) {
            values.add("aaaa");
        } else {
            throw new IllegalArgumentException("Unknown type " + columnType.getName());
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return values.iterator();
    }

    public String toJson() {
        StringBuilder text = new StringBuilder();

        for (Object o : values) {
            text.append(o.toString() + " ");
        }

        return text.toString();
    }

    public static Row parseJson(String json, List<ColumnInfo> columnsInfo) {
        String[] sVals = json.split(" ");
        return new Row(sVals, columnsInfo);
    }

    private Object stringToObject(String string, Class type) throws IllegalArgumentException {
        Object newValue;

        if (type.equals(Integer.class)) {
            newValue = Integer.parseInt(string);
        } else if (type.equals(Character.class)) {
            if (string.length() > 1)
                throw new IllegalArgumentException();

            newValue = (Character) string.charAt(0);
        } else if (type.equals(Double.class)) {
            newValue = Double.parseDouble(string);
        } else if (type.equals(StringInval.class)) {
            if (!StringInval.isInInterval(string)) {
                throw new IllegalArgumentException();
            }

            newValue = string;
        } else {
            throw new IllegalArgumentException();
        }

        return newValue;
    }

    public int size() { return values.size(); }

}
