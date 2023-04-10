package server.api.services;

import commons.Board;
import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.TagRepository;
import server.exceptions.TagDoesNotExist;

import java.util.Set;
@Service
public class TagService {
    private final TagRepository repo;
    private final BoardRepository boardRepo;

    public TagService(TagRepository repo, BoardRepository boardRepo) {
        this.repo = repo;
        this.boardRepo = boardRepo;
    }




    /**
     * ONLY FOR TESTING
     */
    public Tag createTag(String title, Long Id){
        var tag = Tag.createTag(title, Id);
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
     * gets all tags of a task from the database
     * @param boardKey - the id of the task to look up tags for
     * @return a set of tags of the task
     */
    public Set<Tag> getAllTagsByBoard(String boardKey){
        var board = boardRepo.getById(boardKey);
        var tags = board.getTags();
        return tags;
    }
    /**
     * delete a tag from the database by its id
     * @param id - the id of the tag
     * @throws TagDoesNotExist when there is no tag for the given tag id already
     */
    public void deleteById(long id) throws TagDoesNotExist {
        if (!repo.existsById(id))
            throw new TagDoesNotExist("There is no tag with the provided id.");
        Tag tag = getById(id);
        Board board = tag.getBoard();
        board.getTags().remove(tag);
        boardRepo.save(board);
    }
}
