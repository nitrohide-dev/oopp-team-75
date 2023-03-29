package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateBoardModelTest {

    private CreateBoardModel model;

    @BeforeEach
    void setup() {
        model = new CreateBoardModel("a", "a", 0);
    }

    @Test
    void constructor1() {
        CreateBoardModel model = new CreateBoardModel();
        assertNull(model.getKey());
        assertNull(model.getTitle());
        assertEquals(model.getPassword(),0);
    }

    @Test
    void constructor2() {
        CreateBoardModel model = new CreateBoardModel("a", "a", 0);
        assertEquals("a", model.getKey());
        assertEquals("a", model.getTitle());
        assertEquals(0, model.getPassword());
    }

    @Test
    void getKey() {
        assertEquals("a", model.getKey());
        assertNotEquals("b", model.getKey());
    }

    @Test
    void setKey() {
        model.setKey("b");
        assertNotEquals("a", model.getKey());
        assertEquals("b", model.getKey());
    }

    @Test
    void getTitle() {
        assertEquals("a", model.getTitle());
        assertNotEquals("b", model.getTitle());
    }

    @Test
    void setTitle() {
        model.setTitle("b");
        assertNotEquals("a", model.getTitle());
        assertEquals("b", model.getTitle());
    }

    @Test
    void getPassword() {
        assertEquals(0, model.getPassword());
        assertNotEquals("b".hashCode(), model.getPassword());
    }

    @Test
    void setPassword() {
        model.setPassword(1);
        assertNotEquals(0, model.getPassword());
        assertEquals(1, model.getPassword());
    }

    @Test
    void isValid() {
        CreateBoardModel model1 = new CreateBoardModel("a", "a", 0);
        CreateBoardModel model2 = new CreateBoardModel(null, "a", 0);
        CreateBoardModel model3 = new CreateBoardModel("a", null, 0);
        CreateBoardModel model4 = new CreateBoardModel("a", "a", 0);
        assertTrue(model1.isValid());
        assertFalse(model2.isValid());
        assertFalse(model3.isValid());
        assertTrue(model4.isValid());
    }
}