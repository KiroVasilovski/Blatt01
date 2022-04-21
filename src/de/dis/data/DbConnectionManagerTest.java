package de.dis.data;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class DbConnectionManagerTest {
    @Test
    public void testConnectionEstablished() {
        DbConnectionManager instance = DbConnectionManager.getInstance();
        assertNotNull(instance);

        Connection connection = instance.getConnection();
        assertNotNull(connection);
    }
}
