package commons;

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
    private TaskList tasklist;
    @Getter
    @Setter
    private int new_task_order;
    @Getter
    @Setter
    private int old_task_order;

}