package commons.models;

import lombok.Getter;
import lombok.Setter;


/**
 * Class to use for moving tasks
 */
public class TaskMoveModel {

    @Getter
    @Setter
    private Long task_id;
    @Getter
    @Setter
    private Long tasklist_id;
    @Getter
    @Setter
    private int new_task_order;
    public TaskMoveModel(){} // for object mappers, please don't use.

    public TaskMoveModel(Long task_id,Long tasklist_id, int new_task_order) {

        this.task_id = task_id;
        this.tasklist_id = tasklist_id;
        this.new_task_order = new_task_order;
    }

}