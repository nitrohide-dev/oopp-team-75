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
        model = new CreateBoardModel("a", "a", "a");
    }

    @Test
    void constructor1() {
        CreateBoardModel model = new CreateBoardModel();
        assertNull(model.getKey());
        assertNull(model.getTitle());
        assertNull(model.getPassword());
    }

    @Test
    void constructor2() {
        CreateBoardModel model = new CreateBoardModel("a", "a", "a");
        assertEquals("a", model.getKey());
        assertEquals("a", model.getTitle());
        assertEquals("a", model.getPassword());
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
        assertEquals("a", model.getPassword());
        assertNotEquals("b", model.getPassword());
    }

    @Test
    void setPassword() {
        model.setPassword("b");
        assertNotEquals("a", model.getPassword());
        assertEquals("b", model.getPassword());
    }

    @Test
    void isValid() {
        CreateBoardModel model1 = new CreateBoardModel("a", "a", "a");
        CreateBoardModel model2 = new CreateBoardModel(null, "a", "a");
        CreateBoardModel model3 = new CreateBoardModel("a", null, "a");
        CreateBoardModel model4 = new CreateBoardModel("a", "a", null);
        assertTrue(model1.isValid());
        assertFalse(model2.isValid());
        assertFalse(model3.isValid());
        assertFalse(model4.isValid());
    }
}