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
    private Long task_order;

}