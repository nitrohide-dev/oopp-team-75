package server.api.services;

import commons.Tag;
import commons.Task;
import org.springframework.stereotype.Service;
import server.database.TagRepository;
import server.exceptions.TagDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import javax.swing.text.html.HTML;
import java.util.List;
import java.util.Set;

public class TagService {
    private final TagRepository repo;

    public TagService(TagRepository repo) {
        this.repo = repo;
    }


    /**
     * Creates a tag from a title string and
     * @param id - the id for the tag that is to be created
     * @return The newly created tag
     */
    public Tag createTag(Long id, String title){
          Tag tag = new Tag(id,title);
        return repo.save(tag);
    }
    public Tag getById(long id) throws TagDoesNotExist {
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There exists no tag with the provided id.");
        return repo.findById(id).get();
    }
    public Set<Tag> getAllTagsByTask(long task_id){
       Set<Tag> allTags = (Set<Tag>) repo.getTagsByTask(task_id);
       return allTags;
    }
    public void deleteById(long id) throws TagDoesNotExist {
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There is no task with the provided id.");
        repo.deleteById(id);
    }
}
