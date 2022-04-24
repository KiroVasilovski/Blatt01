package de.dis.data;

import de.dis.data.model.estate.Apartment;
import org.junit.Test;

import java.sql.Connection;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DbConnectionManagerTest {
    @Test
    public void testConnectionEstablished() {
        DbConnectionManager instance = DbConnectionManager.getInstance();
        assertNotNull(instance);

        Connection connection = instance.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testValueFetch() {
        Apartment a = Apartment.get(3);
        assertEquals(a.getRent(), 450);
        assertEquals(a.getStreetNumber(), "177");
    }
}
