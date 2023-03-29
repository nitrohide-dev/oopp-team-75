package server.api.services;

import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.TagRepository;
import server.exceptions.TagDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.List;
import java.util.Set;

public class TagService {
    private final TagRepository repo;

    public TagService(TagRepository repo) {
        this.repo = repo;
    }
    public Tag getById(long id) throws TagDoesNotExist {
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There exists no tag with the provided id.");
        return repo.findById(id).get();
    }
    public Set<Tag> getAllTags(long task_id){
       return repo.findById(id).get();
    }
    public void deleteById(long id) throws TagDoesNotExist {
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There is no task with the provided id.");
        repo.deleteById(id);
    }
}
