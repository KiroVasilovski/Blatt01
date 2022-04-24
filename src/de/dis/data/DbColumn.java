package de.dis.data;

/**
 * A Column with a type (the data type; see {@link java.sql.Types})
 * and a title (the name of the column).
 * Suitable to be implemented by enums.
 */
public interface DbColumn {
    public int type();
    public String title();
}
