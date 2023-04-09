package server.api.controllers;

import commons.models.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.api.services.ListService;
import server.api.services.TagService;
import server.api.services.TaskService;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.database.ListRepository;
import server.database.ListRepositoryTest;
import server.database.TagRepository;
import server.database.TagRepositoryTest;
import server.database.TaskRepository;
import server.database.TaskRepositoryTest;
import server.exceptions.CannotCreateBoard;
import server.exceptions.TagDoesNotExist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TagControllerTest {
    private TagService tagService;
    private TagRepository tagRepository;
    private BoardRepository boardRepository;
    private BoardService boardService;
    private ListService listService;
    private ListRepository listRepository;
    private TaskRepository taskRepository;
    private TaskService taskService;
    private TagController tagController;
    private BoardController boardController;
    @BeforeEach
    public void setup() throws CannotCreateBoard {
        boardController = new BoardController(boardService, tagService);
        taskRepository = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepository);
        tagRepository = new TagRepositoryTest();
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        tagService = new TagService(tagRepository, boardRepository);
        tagController = new TagController(tagService, boardService);
        taskService = new TaskService(taskRepository, listRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);

        boardService.create(new CreateBoardModel("1", "1"));
        tagService.createTag("1", 1L);
    }

    @Test
    void editTag() {
        assertEquals(1, tagRepository.count());
        assertEquals("1", tagRepository.findById(1L).get().getTitle());
        tagController.editTag("1", "2", "1");
        assertEquals("2", tagRepository.findById(1L).get().getTitle());
    }

    @Test
    void editTagEmpty(){
        assertEquals(1, tagRepository.count());
        assertEquals("1", tagRepository.findById(1L).get().getTitle());
        assertThrows(ResponseStatusException.class, () -> tagController.editTag("2", "", "1"));
    }

    @Test
    void getById() throws TagDoesNotExist {
        assertEquals("1", tagController.getById("1").getTitle());
        tagService.createTag("2", 2L);
        assertEquals("2", tagController.getById("2").getTitle());
        assertThrows(ResponseStatusException.class, () -> tagController.getById("3"));
    }

    @Test
    void getByTask() {
        //TODO
    }

    @Test
    void getByBoard() {
        assertEquals(0, tagController.getByBoard("1").getBody().size());
        boardService.createTag("1", "2");
        assertEquals(1, tagController.getByBoard("1").getBody().size());
        assertEquals("2", tagController.getByBoard("1").getBody().get(0).getTitle());
    }

    @Test
    void deleteById() throws TagDoesNotExist {
        assertEquals(1, tagRepository.count());
        tagController.deleteById("1", "1");
        assertEquals(0, tagRepository.count());
        assertThrows(ResponseStatusException.class, () -> tagController.deleteById("1", "1"));
    }
}