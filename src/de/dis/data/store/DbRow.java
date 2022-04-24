package de.dis.data.store;

import de.dis.data.DbColumn;

/**
 * A DbRow handles updating the database values when a model object is updated.
 * Each DbRow is bound to one row in a table.
 *
 * @param <T> the enum of all columns in the table
 */
public interface DbRow<T extends Enum<T> & DbColumn> {
    /**
     * Get the value of the identifier column (usually primary key) of the row.
     * @return the identifier value for the row.
     */
    public Object getId();

    public Object get(T column);
    public void set(T column, Object value);

    /**
     * Get all values in the row.
     * @return all values in the row, in the order that the columns were provided to the factory.
     */
    public Object[] getAll();

    /**
     * Set all values in the row.
     * @param values all new values in the row, in the order that the columns were provided to the factory.
     */
    public void setAll(Object... values);
}
