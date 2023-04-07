package commons;

import commons.models.TaskMoveModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class   TaskMoveModelTest {

    private TaskMoveModel model;

    @BeforeEach
    void setup() {

        model = new TaskMoveModel(Integer.toUnsignedLong(3), Integer.toUnsignedLong(13), 21);
    }


    @Test
    void Constructor(){
        assertEquals(3,model.getTask_id());
        assertEquals(13,model.getTasklist_id());
        assertEquals(21,model.getNew_task_order());
    }
    @Test
    void getTask_id(){
        assertEquals(3,model.getTask_id());
        assertNotEquals(10,model.getTask_id());

    }
    @Test
    void setTask_id(){
        model.setTask_id(Integer.toUnsignedLong(5));
        assertEquals(5,model.getTask_id());
        model.setTask_id(Integer.toUnsignedLong(11));
        assertNotEquals(5,model.getTask_id());
    }
    @Test
    void getTasklist_id(){
        assertEquals(13,model.getTasklist_id());
        assertNotEquals(10,model.getTask_id());

    }
    @Test
    void setTasklist_id(){
        model.setTasklist_id(Integer.toUnsignedLong(12));
        assertEquals(12,model.getTasklist_id());
        model.setTasklist_id(Integer.toUnsignedLong(11));
        assertNotEquals(40,model.getTask_id());
    }
    @Test
    void getNew_task_order(){
        assertEquals(21,model.getNew_task_order());
        assertNotEquals(10,model.getNew_task_order());
    }
    void setNew_task_order(){
        model.setNew_task_order(33);
        assertEquals(33,model.getNew_task_order());
        model.setNew_task_order(71);
        assertNotEquals(33,model.getNew_task_order());
    }

}
