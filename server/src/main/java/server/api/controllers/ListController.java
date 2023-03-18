package server.api.controllers;

import commons.TaskList;
import commons.models.CreateListModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.ListService;
import server.exceptions.CannotCreateList;
import server.exceptions.ListDoesNotExist;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping(path = { "", "/" })
    public List<TaskList> getAll() { return listService.getAllLists(); }

    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(@PathVariable("id") String id) {
        try {
            TaskList taskList = listService.getById(Long.parseLong(id));
            return ResponseEntity.ok(taskList);
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<TaskList> create(@RequestBody CreateListModel model) {
        try {
            TaskList taskList = listService.createList(model);
            return ResponseEntity.ok(taskList);
        } catch (CannotCreateList e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskList> deleteById(@PathVariable("id") String id) {
        try {
            listService.deleteListById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
