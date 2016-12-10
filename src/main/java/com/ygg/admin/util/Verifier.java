package com.ygg.admin.util;

/**
 * @author lorabit
 * @since 16-4-27
 */

import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The Class Verifier.
 *
 * @author jack.zhang
 */
public class Verifier {
    private final List<String> messages = new ArrayList<String>();

    /**
     * Verifies that the specified {@link String String} argument has non-blank text. If the string is
     * null or does not have any non-blank text the message is added to the list of verification
     * failure messages. <p/> <p>The validation error message is formatted using {@link
     * String#format(String, Object...) String.format(String, Object...)}****</p>
     *
     * @param string the string to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier hasText(String string, String format, Object... args) {
        boolean condition = (string != null) && (string.trim().length() > 0);
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Map Map} argument is empty. A map is empty if it is null or
     * if it has no entries. If the argument is not empty the message is added to the list of
     * verification failure messages. <p/> <p>The validation error message is formatted using {@link
     * String#format(String, Object...) String.format(String, Object...)}****</p>
     *
     * @param map    the map to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isEmpty(Map<?, ?> map, String format, Object... args) {
        boolean condition = (map == null) || (map.isEmpty());
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Collection Collection} argument is empty. A collection is
     * empty if it is not null or if it has no entries. If the argument is not empty the message is
     * added to the list of verification failure messages. <p/> <p>The validation error message is
     * formatted using {@link String#format(String, Object...) String.format(String,
     * Object...)}****</p>
     *
     * @param collection the collection to be validated
     * @param format     verification failure message format
     * @param args       verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isEmpty(Collection<?> collection, String format, Object... args) {
        boolean condition = (collection == null) || collection.isEmpty();
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link String String} argument is empty. A string is empty if it is
     * null or if it has a length of zero. If the argument is not empty the message is added to the
     * list of verification failure messages. <p/> <p>The validation error message is formatted using
     * {@link String#format(String, Object...) String.format(String, Object...)}****</p>
     *
     * @param string the string to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isEmpty(String string, String format, Object... args) {
        boolean condition = (string != null) && (string.length() > 0);
        isFalse(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified condition is false. If the condition is not false the message is
     * added to the list of verification failure messages. <p/> <p>The validation error message is
     * formatted using {@link String#format(String, Object...) String.format(String,
     * Object...)}****</p>
     *
     * @param condition the condition to be validated
     * @param format    verification failure message format
     * @param args      verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isFalse(boolean condition, String format, Object... args) {
        if (condition) {
            messages.add(MessageFormatter.arrayFormat(format, args).getMessage());
        }

        return this;
    }

    /**
     * Verifies that the specified {@link String String} argument is not empty. A string is not empty
     * if it is not null and it has a length greater than zero. If the argument is empty the message
     * is added to the list of verification failure messages. <p/> <p>The validation error message is
     * formatted using {@link String#format(String, Object...) String.format(String,
     * Object...)}****</p>
     *
     * @param string the string to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isNotEmpty(String string, String format, Object... args) {
        boolean condition = (string != null) && (string.length() > 0);
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Collection Collection} argument is not empty. A collection
     * is not empty if it is not null and has one or more entries. If the argument is empty the
     * message is added to the list of verification failure messages. <p/> <p>The validation error
     * message is formatted using {@link String#format(String, Object...) String.format(String,
     * Object...)}****</p>
     *
     * @param collection the collection to be validated
     * @param format     verification failure message format
     * @param args       verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isNotEmpty(Collection<?> collection, String format, Object... args) {
        boolean condition = (collection == null) || collection.isEmpty();
        isFalse(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Map Map} argument is not empty. A map is not empty if it is
     * not null and has one or more entries. If the argument is empty the message is added to the list
     * of verification failure messages. <p/> <p>The validation error message is formatted using
     * {@link String#format(String, Object...) String.format(String, Object...)}****</p>
     *
     * @param map    the map to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isNotEmpty(Map<?, ?> map, String format, Object... args) {
        boolean condition = (map == null) || (map.isEmpty());
        isFalse(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified object is not null. If the object is null the message is added to
     * the list of verification failure messages. <p/> <p>The validation error message is formatted
     * using {@link String#format(String, Object...) String.format(String, Object...)}****</p>
     *
     * @param object the object to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isNotNull(Object object, String format, Object... args) {
        isFalse((object == null), format, args);

        return this;
    }

    /**
     * Verifies that the specified object is null. If the object is not null the message is added to
     * the list of verification failure messages. <p/> <p>The validation error message is formatted
     * using {@link String#format(String, Object...) String.format(String, Object...)}****</p>
     *
     * @param object the object to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isNull(Object object, String format, Object... args) {
        isTrue((object == null), format, args);

        return this;
    }

    /**
     * Verifies that the specified condition is true. If the condition is not true the message is
     * added to the list of verification failure messages. <p/> <p>The validation error message is
     * formatted using {@link String#format(String, Object...) String.format(String,
     * Object...)}****</p>
     *
     * @param condition the condition to be validated
     * @param format    verification failure message format
     * @param args      verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public Verifier isTrue(boolean condition, String format, Object... args) {
        if (!condition) {
            messages.add(MessageFormatter.arrayFormat(format, args).getMessage());
        }

        return this;
    }

    /**
     * Throw a verification exception when one or more verification tests had verification failures.
     * The exception message is the concatenated list of all of the verification failure messages.
     * <p/> <p>Invokes {@link #throwIfError(String)} with an empty prefix message.</p>
     *
     * @return the verifier
     */
    public Verifier throwIfError() {
        return throwIfError("");
    }

    /**
     * Throw a verification exception when one or more verification tests had verification failures.
     * The specified message is prefixed to the messages associated with each verification failure
     * message. The exception message is the concatenated list of all of the verification failure
     * messages.
     *
     * @param prefixMessage message is prefixed to the verification failure messages
     * @return the verifier
     * @throws IllegalArgumentException thrown if any verification tests have failed
     */
    public Verifier throwIfError(String prefixMessage) {
        if (messages.size() > 0) {
            StringBuilder exceptionMessage = new StringBuilder((prefixMessage == null)
                    ? "" : prefixMessage);
            String sp = (prefixMessage == null) ? "" : " ";

            for (String msg : messages) {
                exceptionMessage.append(sp)
                        .append(msg);
                sp = " ";
            }

            throw new IllegalArgumentException(exceptionMessage.toString());
        }

        return this;
    }

    /**
     * Is positive number.
     *
     * @param number the number
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public Verifier isPositiveNumber(Number number, String format, Object... args) {
        boolean condition = (number != null) && (number.intValue() > 0);
        isTrue(condition, format, args);
        return this;
    }

    /**
     * Is positive number.
     *
     * @param str    the str
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public Verifier isNumberic(String str, String format, Object... args) {
        boolean condition = str != null && str.length() > 0;
        isTrue(condition, format, args);
        if (!condition) {
            return this;
        }

        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                isTrue(condition, format, args);
            }
        }
        return this;
    }

    /**
     * Is less than.
     *
     * @param number   the number
     * @param maxValue the max value
     * @param format   the format
     * @param args     the args
     * @return the verifier
     */
    public Verifier isLessThan(Number number, int maxValue, String format, Object... args) {
        boolean condition = (number != null) && (number.intValue() > maxValue);
        isFalse(condition, format, args);
        return this;
    }

    /**
     * Is not nagative.
     *
     * @param number the number
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public Verifier isNotNagative(Number number, String format, Object... args) {
        boolean condition = (number != null) && (number.intValue() >= 0);
        isTrue(condition, format, args);
        return this;
    }

    /**
     * Is Null Or Positive number.
     *
     * @param number the number
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public Verifier isNullOrPositiveNumber(Number number, String format, Object... args) {
        boolean condition = (number == null) || (number.intValue() > 0);
        isTrue(condition, format, args);
        return this;
    }

}