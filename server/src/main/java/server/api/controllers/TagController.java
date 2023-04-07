package server.api.controllers;

import commons.Board;
import commons.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.TagService;
import server.exceptions.TagDoesNotExist;

import java.util.Set;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;
    private final BoardController boardService;

    public TagController(TagService tagService, BoardController boardService) {
        this.tagService = tagService;
        this.boardService = boardService;
    }

    /**
     * changes the tag's title to title
     * @param id the id of the tag that is having its title changed
     * @param title the tag title to change the tag to
     */
    @MessageMapping("/tag/edit/{boardKey}/{id}")
    @SendTo("/topic/boards")
    public Board editTag(@DestinationVariable("id") String id, String title,
                         @DestinationVariable("boardKey") String boardKey) {
        try {
            tagService.editTag(Long.parseLong(id),title);
            return boardService.findByKey(boardKey).getBody();
        } catch (NumberFormatException | TagDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Gets a tag from the database by id. If the id does not exist in the
     * database, the method will respond with a bad request.
     * @param id the tag key
     * @return the stored tag
     */
    @GetMapping("/getById/{id}")
    public Tag getById(@PathVariable("id") String id) throws TagDoesNotExist{
        try {
            return tagService.getById(Long.parseLong(id));
        } catch (NumberFormatException | TagDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Gets a set of tags appended to a given task, using its id
     * @param id the task id
     * @return the stored tag list
     */
    @GetMapping("/getByTask/{id}")
    public ResponseEntity<Set<Tag>> getByTask(@PathVariable("id") String id) {
        Set<Tag> tags = tagService.getAllTagsByTask(Long.parseLong(id));
        return ResponseEntity.ok(tags);
    }

    /**
     * Gets a set of tags appended to a given board, using its id
     * @param id the board id
     * @return the stored tag list
     */
    @GetMapping("/getByBoard/{id}")
    public ResponseEntity<Set<Tag>> getByBoard(@PathVariable("id") String id) {
        Set<Tag> tags = tagService.getAllTagsByBoard(Long.parseLong(id));
        return ResponseEntity.ok(tags);
    }

    /**
     * Deletes a tag from the database by its id. If
     * the id does not exist in the database or has a wrong format, the method will respond with a
     * bad request.
     * @param id the tag id
     */
    @MessageMapping("/tag/delete/{id}")
    @SendTo("/topic/boards")
    public Board deleteById(@DestinationVariable("id") String id, String boardKey) throws TagDoesNotExist {
        try {
            tagService.deleteById(Long.parseLong(id));
            return boardService.findByKey(boardKey).getBody();
        } catch (NumberFormatException | TagDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
