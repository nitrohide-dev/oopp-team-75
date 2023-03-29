package server.database;

import commons.TaskList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ListRepositoryTest implements ListRepository{
    private List<TaskList> lists;

    public ListRepositoryTest() {
        lists = new ArrayList<>();
    }
     public ListRepositoryTest(List<TaskList> lists) {
        this.lists = lists;
    }


    @Override
    public List<TaskList> findAll() {
        return lists;
    }

    @Override
    public List<TaskList> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<TaskList> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<TaskList> findAllById(Iterable<Long> longs) {
        List<TaskList> queryLists = new ArrayList<>();
        for(Long i : (Iterable<Long>) longs) {
            for(TaskList list : lists) {
                if(list.getId() == i) {
                    queryLists.add(list);
                }
            }
        }
        return queryLists;
    }

    @Override
    public long count() {
        return lists.size();
    }

    @Override
    public void deleteById(Long aLong) {
        for(TaskList list : lists) {
            if(list.getId() == aLong) {
                lists.remove(list);
            }
        }
    }

    @Override
    public void delete(TaskList entity) {
        lists.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        for(Long i : (Iterable<Long>) longs) {
            for(TaskList list : lists) {
                if(list.getId() == i) {
                    lists.remove(list);
                }
            }
        }
    }

    @Override
    public void deleteAll(Iterable<? extends TaskList> entities) {
        for(TaskList list : (Iterable<TaskList>) entities) {
            lists.remove(list);
        }
    }

    @Override
    public void deleteAll() {
        lists.clear();
    }

    @Override
    public <S extends TaskList> S save(S entity) {
        lists.add(entity);
        return entity;
    }

    @Override
    public <S extends TaskList> List<S> saveAll(Iterable<S> entities) {
        for(TaskList list : (Iterable<TaskList>) entities) {
            lists.add(list);
        }
        return (List<S>) entities;
    }

    @Override
    public Optional<TaskList> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for(TaskList list : lists) {
            if(list.getId() == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends TaskList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends TaskList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<TaskList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public TaskList getOne(Long aLong) {
        return null;
    }

    @Override
    public TaskList getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends TaskList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends TaskList> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TaskList> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends TaskList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TaskList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TaskList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends TaskList, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}