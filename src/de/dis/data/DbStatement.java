package de.dis.data;

import java.util.Arrays;

/**
 * Helper class to construct SQL prepared statements.
 */
public class DbStatement {
    private static String asAssignment(DbColumn column) {
        return String.format("%s = ?", column.title());
    }

    /**
     * Create a new INSERT INTO statement.
     * @param table the table to insert into
     * @param columns the columns for which values will be provided
     * @return `INSERT INTO table (columns[0], ..., columns[i]) VALUES (?, ..., ?)`
     */
    public static String insert(String table, DbColumn[] columns) {
        String[] qMarks = new String[columns.length];
        Arrays.fill(qMarks, "?");
        String[] nColumns = Arrays
                .stream(columns)
                .map(DbColumn::title)
                .toArray(String[]::new);
        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                table,
                String.join(", ", nColumns),
                String.join(", ", qMarks));
    }

    /**
     * Create a new UPDATE statement.
     * @param table the table to update
     * @param setColumns the columns to update in the SET clause
     * @param whereColumn the identifying column to use in the WHERE clause
     * @return `UPDATE table SET setColumns[0] = ?, ..., setColumns[i] = ? WHERE whereColumn = ?`
     */
    public static String update(String table, DbColumn[] setColumns, DbColumn whereColumn) {
        String[] qNonIdColumns = Arrays
                .stream(setColumns)
                .map(DbStatement::asAssignment)
                .toArray(String[]::new);
        String qIdColumn = asAssignment(whereColumn);
        return String.format("UPDATE %s SET %s WHERE %s",
                table,
                String.join(", ", qNonIdColumns),
                qIdColumn);
    }

    /**
     * Create a new DELETE statement.
     * @param table the table to delete a row from
     * @param whereColumn the column to use in the WHERE clause
     * @return `DELETE * FROM table WHERE whereColumn = ?`
     */
    public static String delete(String table, DbColumn whereColumn) {
        return String.format("DELETE FROM %s WHERE %s", table, asAssignment(whereColumn));
    }

    /**
     * Create a new bare SELECT statement.
     * @param table the table to select all from
     * @return `SELECT * FROM table`
     */
    public static String select(String table) {
        return String.format("SELECT * FROM %s", table);
    }

    /**
     * Create a new SELECT statement with WHERE.
     * @param table the table to select a row from
     * @param whereColumn the column to use in the WHERE clause
     * @return `SELECT * FROM table WHERE whereColumn = ?`
     */
    public static String select(String table, DbColumn whereColumn) {
        return String.format("SELECT * FROM %s WHERE %s", table, asAssignment(whereColumn));
    }
}
