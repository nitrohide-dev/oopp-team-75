package server.api.controllers;

import commons.Tag;
import commons.Task;
import commons.TaskList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.TagService;
import server.exceptions.TagDoesNotExist;
import java.util.Set;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * creates a tag in the database with a given title
     * @param title the tag title
     * @param id the tag key
    * @return the stored tag
    */
    @PostMapping("/create/{id}")
   public ResponseEntity<Tag> createTag(@PathVariable("id") String id,String title) {

           Tag tag = tagService.createTag(Long.parseLong(id),title);
            return ResponseEntity.ok(tag);

   }
    /**
     * Gets a tag from the database by id. If the id does not exist in the
     * database, the method will respond with a bad request.
     * @param id the tag key
     * @return the stored tag
     */
    @PostMapping("/get/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") String id) throws TagDoesNotExist{
        try {
            Tag tag = tagService.getById(Long.parseLong(id));
            return ResponseEntity.ok(tag);

        } catch (NumberFormatException | TagDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    /**
     * Gets a set of tags appended to a given task, using its id
     * @param id the task id
     * @return the stored tag list
     */
    @GetMapping("/getbytask/{id}")
    public ResponseEntity<Set<Tag>> getByTask(@PathVariable("id") String id) {
            Set<Tag> tags = tagService.getAllTagsByTask(Long.parseLong(id));
            return ResponseEntity.ok(tags);
    }
    /**
     * Deletes a tag from the database by its id. If
     * the id does not exist in the database or has a wrong format, the method will respond with a
     * bad request.
     * @param id the tag id
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskList> deleteById(@PathVariable("id") String id) throws TagDoesNotExist {
        try {
            tagService.deleteById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException | TagDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }


}
