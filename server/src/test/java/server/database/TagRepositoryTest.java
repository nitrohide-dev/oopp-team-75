package server.database;

import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TagRepositoryTest implements TagRepository{
    private List<Tag> tags;
    //TODO implement
    public TagRepositoryTest() {
        tags = new ArrayList<>();
    }

    public TagRepositoryTest(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public List<Tag> findAll() {
        return tags;
    }

    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
        List<Tag> queryTags = new ArrayList<>();
        for(Long i : (Iterable<Long>) longs) {
            for(Tag tag : tags) {
                if(tag.getId() == i) {
                    queryTags.add(tag);
                }
            }
        }
        return queryTags;
    }

    @Override
    public long count() {
        return tags.size();
    }

    @Override
    public void deleteById(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (aLong < 0) {
            throw new IllegalArgumentException("id must not be negative");
        }
        if (findById(aLong).isEmpty()) {
            throw new IllegalArgumentException("id must exist");
        }
        for(Tag tag : tags) {
            if(tag.getId() == aLong) {
                tags.remove(tag);
                break;
            }
        }
    }

    @Override
    public void delete(Tag entity) {
        tags.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        for(Long i : longs) {
            for(Tag tag : tags) {
                if(tag.getId() == i) {
                    tags.remove(tag);
                    break;
                }
            }
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {
        for(Tag tag : entities) {
            tags.remove(tag);
        }
    }


    @Override
    public void deleteAll() {
        tags.clear();
    }

    @Override
    public <S extends Tag> S save(S entity) {
        if (entity == null) {
            return null;
        }
        for (Tag tag : tags) {
            if (tag.getId() == entity.getId()) {
                tags.remove(tag);
                break;
            }
        }
        tags.add(entity);
        return entity;
    }

    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        if (entities == null) {
            return null;
        }
        for (Tag tag : entities) {
            save(tag);
        }
        return (List<S>) entities;
    }

    @Override
    public Optional<Tag> findById(Long aLong) {
        for(Tag tag : tags) {
            if(tag.getId() == aLong) {
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for (Tag tag : tags) {
            if (tag.getId() == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {
    }


    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Tag getOne(Long aLong) {
        return null;
    }

    @Override
    public Tag getById(Long aLong) {
        for (Tag tag : tags) {
            if (tag.getId() == aLong) {
                return tag;
            }
        }
        return null;
    }

    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        for (Tag tag : tags) {
            if (tag.equals(example.getProbe())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <S extends Tag, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Collection<Tag> getTagsByTask(long task_id) {
        Collection<Tag> taskTags = new ArrayList<>();
        for(Tag tag : tags) {
            if(tag.isChildOfTask(task_id)) {
                taskTags.add(tag);
            }
        }
        return taskTags;
    }

    /**
     * @param board_key the id of the board to look up tags of
     * @return
     */
    @Override
    public Collection<Tag> getTagsByBoard(long board_key) {
        return null;
    }

    @Override
    public Collection<Tag> getTagsByBoard(String board_key) {
        Collection<Tag> boardTags = new ArrayList<>();
        for(Tag tag : tags) {
            if(tag.isChildOfBoard(board_key)) {
                boardTags.add(tag);
            }
        }
        return boardTags;
    }
}
