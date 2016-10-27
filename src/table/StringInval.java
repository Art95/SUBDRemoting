package table;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Artem on 16.09.2016.
 */
public class StringInval extends Object {
    private String string;
    private static String first = "aaaa";
    private static String last = "zzzz";
    private static String[] values = {  "aaaa", "bbbb", "cccc", "dddd", "eeee", "ffff", "gggg", "hhhh", "iiii", "jjjj",
                                        "kkkk", "llll", "mmmm", "nnnn", "oooo", "pppp", "qqqq", "rrrr", "ssss", "tttt",
                                        "uuuu", "vvvv", "wwww", "xxxx", "yyyy", "zzzz"};

    private static Set<String> interval = new HashSet<>(Arrays.asList(values));

    public StringInval() {
        string = "";
        interval = new HashSet<>(Arrays.asList(values));
    }

    public StringInval(String first, String last, String[] values) {
        this.first = first;
        this.last = last;

        this.values = values;
        interval = new HashSet<>(Arrays.asList(values));
    }

    @Override
    public String toString() {
        return string;
    }

    public static boolean isInInterval(String value) {
        return value.compareTo(first) >= 0 && value.compareTo(last) <= 0 && interval.contains(value);
    }
}
