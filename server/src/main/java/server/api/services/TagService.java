package server.api.services;
import commons.Tag;
import server.database.TagRepository;
import server.exceptions.TagDoesNotExist;
import java.util.Set;

public class TagService {
    private final TagRepository repo;

    public TagService(TagRepository repo) {
        this.repo = repo;
    }


    /**
     * Creates a tag in the database from a title string
     * @param title - the name of the tag to be created
     * @return The newly created tag
     */
    public Tag createTag(String title){
          Tag tag = new Tag(title);
        return repo.save(tag);
    }
    /**
     * changes a task's title in the database to the string "title"
     * @param title - the new tag title
     * @throws TagDoesNotExist when the there is no tag for the given tag id in the database
     */
    public void editTag(long id,String title)throws TagDoesNotExist{
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There exists no tag with the provided id.");
         Tag tag = repo.findById(id).get();
        tag.setTitle(title);
        repo.save(tag);
    }
    /**
     * gets a tag from the database by its id
     * @param id - the id of the tag
     * @return The Tag object corresponding to it
     * @throws TagDoesNotExist when there is no tag for the given tag id
     */
    public Tag getById(long id) throws TagDoesNotExist {
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There exists no tag with the provided id.");
        return repo.findById(id).get();
    }
    /**
     * gets all tags of a task from the database
     * @param task_id - the id of the task to look up tags for
     * @return a set of tags of the task
     */
    public Set<Tag> getAllTagsByTask(long task_id){
       Set<Tag> allTags = (Set<Tag>) repo.getTagsByTask(task_id);
       return allTags;
    }
    /**
     * TO DO
     * gets all tags of a task from the database
     * @param task_id - the id of the task to look up tags for
     * @return a set of tags of the task
     */
    public Set<Tag> getAllTagsByBoard(long task_id){
        Set<Tag> allTags = (Set<Tag>) repo.getTagsByTask(task_id);
        return allTags;
    }
    /**
     * delete a tag from the database by its id
     * @param id - the id of the tag
     * @throws TagDoesNotExist when there is no tag for the given tag id already
     */
    public void deleteById(long id) throws TagDoesNotExist {
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There is no tag with the provided id.");
        repo.deleteById(id);
    }
}
