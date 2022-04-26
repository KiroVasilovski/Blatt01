package de.dis.data.store;

import de.dis.data.DbColumn;
import de.dis.data.DbConnectionManager;
import de.dis.data.DbStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * A DbRowFactory allows for the creation of individual DbRow objects. A DbRowFactory
 * is bound to a single table in the database.
 *
 * @param <T>
 */
public class DbRowFactory<T extends Enum<T> & DbColumn> {
    private static Connection getConnection() {
        return DbConnectionManager.getInstance().getConnection();
    }

    private final String table;
    private final T idColumn;
    private final T[] columns;

    private class Row implements DbRow<T> {
        private final Object id;
        private final Map<T, Object> values;

        private Row(Object id, Map<T, Object> initialValues) {
            Objects.requireNonNull(id);
            if (initialValues.size() != columns.length)
                throw new IllegalStateException(
                        String.format("%d columns expected, but found %d",
                                columns.length, initialValues.size()));

            values = new HashMap<>(initialValues);
            values.put(idColumn, id);
            this.id = id;

            cache.put(id, this);
        }

        @Override
        public Object getId() {
            return id;
        }

        @Override
        public Object get(T column) {
            return values.get(column);
        }

        @Override
        public void set(T column, Object value) {
            String query = DbStatement.update(table, new DbColumn[]{column}, idColumn);

            Connection con = getConnection();

            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setObject(1, value, column.type());
                statement.setObject(2, id, idColumn.type());
                statement.executeUpdate();

                values.put(column, value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object[] getAll() {
            return Arrays
                    .stream(columns)
                    .map(values::get)
                    .toArray();
        }

        @Override
        public void setAll(Object... newValues) {
            if (columns.length != newValues.length)
                throw new IllegalArgumentException(
                        String.format("%d columns required, but %d values provided",
                                columns.length, newValues.length));

            String query = DbStatement.update(table, columns, idColumn);

            Connection con = getConnection();

            try (PreparedStatement statement = con.prepareStatement(query)) {
                for (int i = 0; i < columns.length; i++) {
                    T column = columns[i];
                    statement.setObject(i + 1, newValues[i], column.type());
                }
                statement.setObject(columns.length + 1, id, idColumn.type());
                statement.executeUpdate();

                for (int i = 0; i < columns.length; i++) {
                    values.put(columns[i], newValues[i]);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < columns.length; i++) {
                values.put(columns[i], newValues[i]);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DbRow other) {
                return this.id.equals(other.getId());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    private final Map<Object, Row> cache = new HashMap<>();

    /**
     * Create a new DbRowFactory. The first of the provided columns is
     * inferred to be the identifying column (usually also the primary key).
     *
     * @param table   the name of the table to create the DbRowFactory for
     * @param columns the columns, starting with the identifying column, that the table holds
     */
    public DbRowFactory(String table, T[] columns) {
        this(table, columns[0], Arrays.copyOfRange(columns, 1, columns.length));
    }

    /**
     * Create a new DbRowFactory.
     *
     * @param table    the name of the table to create the DbRowFactory for
     * @param idColumn the identifying column (usually also the primary key)
     * @param columns  the remaining columns that the table holds
     */
    public DbRowFactory(String table, T idColumn, T... columns) {
        this.table = table;
        this.idColumn = idColumn;
        this.columns = columns;
    }

    /**
     * Load the row with the specified identifier into a DbRow object.
     * Objects are cached where possible.
     *
     * @param id the identifier of the row to look up
     * @return a DbRow for the corresponding row, or null if none were found
     */
    public DbRow<T> load(Object id) {
        Objects.requireNonNull(id);

        if (cache.containsKey(id)) return cache.get(id);

        return loadWhere(idColumn, id);
    }

    /**
     * Load the first row where the specified column matches the specified value
     * and return it as a DbRow object.
     *
     * @param column the column to filter by
     * @param value  the value to filter for
     * @return a DbRow corresponding to the **first** row matching the value, or null if none were found
     */
    public DbRow<T> loadWhere(T column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);

        Connection con = getConnection();
        String query = DbStatement.select(table, column);

        try {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setObject(1, value, column.type());
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) return null;
                    Map<T, Object> initialValues = new HashMap<>();
                    for (T _column : columns) {
                        initialValues.put(_column, rs.getObject(_column.title()));
                    }
                    return new Row(rs.getObject(idColumn.title()), initialValues);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a new table row and corresponding DbRow object
     * with an explicit identifier column (as opposed to auto-generated identifier keys).
     * This is useful in vertical partitioning to reuse primary keys across subtype tables.
     *
     * @param id     the value of the identifier column
     * @param values the remaining row values, in the order that the columns were supplied to this factory
     * @return the DbRow object corresponding to the newly created row, or null if an error occurred
     */
    public DbRow<T> createWithId(Object id, Object... values) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(values);
        if (values.length != columns.length)
            throw new IllegalArgumentException(
                    String.format("%d columns required, but %d values provided", columns.length, values.length)
            );

        Connection con = getConnection();
        DbColumn[] withId = Arrays.copyOf(columns, columns.length + 1);
        withId[columns.length] = idColumn;
        String query = DbStatement.insert(table, withId);

        try {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                for (int i = 0; i < columns.length; i++) {
                    statement.setObject(i + 1, values[i], columns[i].type());
                }
                statement.setObject(columns.length + 1, id, idColumn.type());

                statement.executeUpdate();

                return load(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a new table row and corresponding DbRow object without specifying an identifier value.
     * This is useful for tables with an auto-incrementing key.
     *
     * @param values the row values, in the order that the columns were provided to this factory
     * @return the DbRow object corresponding to the newly created row, or null if an error occurred
     */
    public DbRow<T> create(Object... values) {
        Objects.requireNonNull(values);
        if (values.length != columns.length)
            throw new IllegalArgumentException(
                    String.format("%d columns required, but %d values provided", columns.length, values.length)
            );

        Connection con = getConnection();
        String query = DbStatement.insert(table, columns);

        try {
            try (PreparedStatement statement = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < columns.length; i++) {
                    statement.setObject(i + 1, values[i], columns[i].type());
                }

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (!rs.next()) return null;
                    Object id = rs.getObject(idColumn.title());

                    return load(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete a table row and remove corresponding DbRow objects from cache.
     *
     * @param id the identifier value of the row to delete
     */
    public void delete(Object id) {
        Objects.requireNonNull(id);

        Connection con = getConnection();
        String query = DbStatement.delete(table, idColumn);

        try {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setObject(1, id, idColumn.type());
                statement.executeUpdate();

                cache.remove(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a set of all (!) existing rows of the table.
     *
     * @return a Set containing corresponding DbRow objects for all rows of the table, or null if an error occurred
     */
    public Set<DbRow<T>> loadAll() {
        return _loadAllWhere(null, null);
    }

    /**
     * Get a set of all rows of the table where the specified column matches the specified value.
     *
     * @param column the column to filter by
     * @param value  the value to filter for
     * @return a Set containing corresponding DbRow objects for all matching rows of the table, or null if an error occurred
     */
    public Set<DbRow<T>> loadAllWhere(T column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);

        return _loadAllWhere(column, value);
    }

    private Set<DbRow<T>> _loadAllWhere(T _column, Object _value) {
        Connection con = getConnection();
        String query = _column == null ? DbStatement.select(table) : DbStatement.select(table, _column);

        Set<DbRow<T>> result = new HashSet<>();

        try {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                if (_column != null) statement.setObject(1, _value, _column.type());
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        Object id = rs.getObject(idColumn.title());
                        if (cache.containsKey(id)) {
                            result.add(cache.get(id));
                        } else {
                            Map<T, Object> initialValues = new HashMap<>();
                            for (T column : columns) {
                                initialValues.put(column, rs.getObject(column.title()));
                            }
                            result.add(new Row(id, initialValues));
                        }
                    }

                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}