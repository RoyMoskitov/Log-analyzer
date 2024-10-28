package backend.academy.LogMapping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilterFieldsTest {

    @Test
    void TestisValidMethod() {
        assertTrue(FilterFields.isValid("REMOTE_ADDRESS"));
        assertFalse(FilterFields.isValid("REMOTE_ADD"));
    }
}
