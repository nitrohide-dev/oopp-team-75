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
        model = new CreateBoardModel("a", "a");
    }

    @Test
    void constructor1() {
        CreateBoardModel model = new CreateBoardModel();
        assertNull(model.getKey());
        assertNull(model.getTitle());
    }

    @Test
    void constructor2() {
        CreateBoardModel model = new CreateBoardModel("a", "a");
        assertEquals("a", model.getKey());
        assertEquals("a", model.getTitle());
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
    void isValid() {
        CreateBoardModel model1 = new CreateBoardModel("a", "a");
        CreateBoardModel model2 = new CreateBoardModel(null, "a");
        CreateBoardModel model3 = new CreateBoardModel("a", null);
        CreateBoardModel model4 = new CreateBoardModel("a", "a");
        assertTrue(model1.isValid());
        assertFalse(model2.isValid());
        assertFalse(model3.isValid());
        assertTrue(model4.isValid());
    }
}