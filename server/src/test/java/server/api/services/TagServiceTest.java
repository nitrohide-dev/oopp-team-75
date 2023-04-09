package server.api.services;

import commons.models.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class TagServiceTest {
    private TagService tagService;
    private TagRepository tagRepository;
    private BoardRepository boardRepository;
    private BoardService boardService;
    private ListService listService;
    private ListRepository listRepository;
    private TaskRepository taskRepository;
    private TaskService taskService;


    @BeforeEach
    public void setup() throws CannotCreateBoard {
        taskRepository = new TaskRepositoryTest();
        tagRepository = new TagRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepository);
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        tagService = new TagService(tagRepository, boardRepository);
        taskService = new TaskService(taskRepository, listRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);

        boardService.create(new CreateBoardModel("1", "1"));
        tagService.createTag("1", 1L);
    }

    @Test
    void createTagEmpty() {
        tagRepository.deleteAll();
        assertEquals(0, tagRepository.count());
    }

    @Test
    void createTag() {
        assertEquals(1, tagRepository.count());
    }
    @Test
    void createTag2() {
        tagService.createTag("3");
        assertEquals(2, tagRepository.count());
    }
    @Test
    void editTagException() {
        assertThrows(TagDoesNotExist.class, () -> tagService.editTag(2, "2"));
        assertThrows(TagDoesNotExist.class, () -> tagService.editTag(3000, "2"));
    }

    @Test
    void editTag() throws TagDoesNotExist {
        tagService.createTag("2", 2L);
        tagService.editTag(1L, "2");
        assertEquals("2", tagService.getById(1L).getTitle());
    }

    @Test
    void getById() throws TagDoesNotExist {
        tagService.createTag("2", 2L);
        assertEquals("1", tagService.getById(1L).getTitle());
        assertEquals("2", tagService.getById(2L).getTitle());
    }

    @Test
    void getByIdException() {
        assertThrows(TagDoesNotExist.class, () -> tagService.getById(2));
        assertThrows(TagDoesNotExist.class, () -> tagService.getById(3000));
    }

//    @Test
//    void getAllTagsByTask() throws CannotCreateBoard {
//        tagService.createTag("2", 2L);
//        tagService.createTag("3", 3L);
//        boardService.create(new CreateBoardModel("3", "1")); //new board
//        boardService.createList(boardRepository.getById("1")); //new list
//        listService.createTask(boardRepository.getById("1").getTaskLists().get(0), "1"); //new task
//        taskService.addTag(listService.getAll().get(0).getTasks().get(0).getId(), tagRepository.getById(1L)); //add tag
//        assertEquals(1, tagService.getAllTagsByTask(1L).size());
//
//
//        taskService.addTag(1L, tagRepository.getById(2L));
//        assertEquals(2, tagService.getAllTagsByTask(1L).size());
//        taskService.addTag(1L, tagRepository.getById(3L));
//        assertEquals(3, tagService.getAllTagsByTask(1L).size());
//        listService.createTask(listRepository.getById(1L), "2");
//        taskService.addTag(2L, tagRepository.getById(1L));
//        assertEquals(1, tagService.getAllTagsByTask(2L).size());
//        assertEquals(3, tagService.getAllTagsByTask(1L).size());
//    }

    @Test
    void getAllTagsByBoard() throws CannotCreateBoard {
        assertEquals(0, tagService.getAllTagsByBoard("1").size());
        boardService.createTag("1", "1");
        assertEquals(1, tagService.getAllTagsByBoard("1").size());
        boardService.createTag("1", "1");
        assertEquals(1, tagService.getAllTagsByBoard("1").size());
        boardService.create(new CreateBoardModel("2", "1"));
        assertEquals(0, tagService.getAllTagsByBoard("2").size());
        boardService.createTag("2", "2");
        assertEquals(1, tagService.getAllTagsByBoard("2").size());
        boardService.createTag("2", "2");
        assertEquals(1, tagService.getAllTagsByBoard("2").size());
        assertEquals(1, tagService.getAllTagsByBoard("1").size());
    }

    @Test
    void deleteById() throws TagDoesNotExist {
        assertEquals(1, tagRepository.count());
        tagService.deleteById(1L);
        assertEquals(0, tagRepository.count());
        assertThrows(TagDoesNotExist.class, () -> tagService.deleteById(1L));
    }
}