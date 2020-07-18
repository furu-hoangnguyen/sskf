package sskf.util;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil extends StringUtils {

    /**
     * isEmpty.
     *
     * @param s
     *            a {@link String} object.
     * @return a boolean.
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String getDateFromDateTime(Date dateTime, String formatDate) {
        if (dateTime == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(formatDate);
        return df.format(dateTime);
    }

    public static String getTimeFromDateTime(Date dateTime) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(dateTime);
    }


    /**
     * Checks if the field isn't null and length of the field is greater than
     * zero not including whitespace.
     *
     * @param str
     *            String
     * @return result of checking
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * check string isNumber
     *
     * @param str
     *            is the input for check
     * @return a boolean.
     */
    public static boolean isNumeric(String str) {
        return !isEmpty(str) && str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * check string isNotNumber
     *
     * @param str
     *            is the input for check
     * @return a boolean.
     */
    public static boolean isNotNumeric(String str) {
        return !isNumeric(str); // match a number with optional '-' and decimal.
    }

    /**
     * Check the string is a boolean.
     *
     * @param s
     *            is checker
     * @return the result of parseBoolean
     * @since 1.5
     */
    public static boolean isBoolean(String s) {
        return ((s != null) && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")));
    }

    /**
     * Check the string is not a boolean.
     *
     * @param s is checker
     * @return the result of parseBoolean
     * @since 1.5
     */
    public static boolean isNotBoolean(String s) {
        return !((s != null) && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")));
    }

    /**
     * check equal two string
     *
     * @param text1 the text1 compare
     * @param text2 the text2 compare
     * @return the result of compare
     */
    public static Boolean equal(String text1, String text2) {
        return (text1 == null && text2 == null) || (text1 != null && text1.equals(text2));
    }

    /**
     * check not equal two string
     *
     * @param text1 the text1 compare
     * @param text2 the text2 compare
     * @return the result of compare
     */
    public static Boolean notEqual(String text1, String text2) {
        return !equal(text1, text2);
    }

    /**
     * Escape sql character string.
     *
     * @param source the source
     * @return the string
     */
    public static String escapeSQLCharacter(String source) {
        if (source == null || source.length() == 0) {
            return source;
        }
        return source.replaceAll("([%_\\\\])", "\\\\$1");
    }

    /**
     * Escape rsql character string.
     *
     * @param source the source
     * @return the string
     */
    public static String escapeRSQLCharacter(String source) {
        if (source == null || source.length() == 0) {
            return source;
        }
        return source.replaceAll("(\\\\)", "\\\\$1")
                .replaceAll("(\\[%_])", "\\\\$1")
                .replaceAll("(\\\\\")", "\\\\$1");
    }

    /**
     * Returns the string representation of the {@code Object} argument.
     *
     * @param   obj   an {@code Object}.
     * @return  if the argument is {@code null}, then a string equal to
     *          {@code ""}; otherwise, the value of
     *          {@code obj.toString()} is returned.
     * @see     java.lang.Object#toString()
     */
    public static String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    /**
     * Returns the string representation of the {@code Object} argument.
     *
     * @param   obj   an {@code Object}.
     * @return  if the argument is {@code null}, then a string equal to
     *          defaultValue; otherwise, the value of
     *          {@code obj.toString()} is returned.
     * @see     java.lang.Object#toString()
     */
    public static String valueOf(Object obj, String defaultValue) {
        return (obj == null) ? defaultValue : obj.toString();
    }

}
