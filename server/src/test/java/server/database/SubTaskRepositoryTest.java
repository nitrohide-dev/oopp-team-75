package server.database;

import commons.SubTask;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SubTaskRepositoryTest implements SubTaskRepository{

    private List<SubTask> subTasks;

    public SubTaskRepositoryTest(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public SubTaskRepositoryTest() {
        subTasks = new ArrayList<>();
    }


    @Override
    public List<SubTask> findAll() {
        return subTasks;
    }

    @Override
    public List<SubTask> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<SubTask> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<SubTask> findAllById(Iterable<Long> longs) {
        List<SubTask> querySubTasks = new ArrayList<>();
        for(Long i : (Iterable<Long>) longs) {
            for(SubTask subTask : subTasks) {
                if(subTask.getId() == i) {
                    querySubTasks.add(subTask);
                }
            }
        }
        return querySubTasks;
    }

    @Override
    public long count() {
        return subTasks.size();
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

        for(SubTask subTask : subTasks) {
            if(subTask.getId() == aLong) {
                subTasks.remove(subTask);
                break;
            }
        }
    }

    @Override
    public void delete(SubTask entity) {
        subTasks.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        for (Long aLong : longs) {
            deleteById(aLong);
        }

    }

    @Override
    public void deleteAll(Iterable<? extends SubTask> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends SubTask> S save(S entity) {
        subTasks.add(entity);
        return entity;
    }

    @Override
    public <S extends SubTask> List<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            subTasks.add(entity);
        }
        return (List<S>) subTasks;
    }

    @Override
    public Optional<SubTask> findById(Long aLong) {
        for(SubTask subTask : subTasks) {
            if(subTask.getId() == aLong) {
                return Optional.of(subTask);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for(SubTask subTask : subTasks) {
            if(subTask.getId() == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends SubTask> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends SubTask> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<SubTask> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public SubTask getOne(Long aLong) {
        for(SubTask subTask : subTasks) {
            if(subTask.getId() == aLong) {
                return subTask;
            }
        }
        return null;
    }

    @Override
    public SubTask getById(Long aLong) {
        for(SubTask subTask : subTasks) {
            if(subTask.getId() == aLong) {
                return subTask;
            }
        }
        return null;
    }

    @Override
    public <S extends SubTask> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends SubTask> List<S> findAll(Example<S> example) {
        // find all subtasks that match the example
        List<S> querySubTasks = new ArrayList<>();
        for(SubTask subTask : subTasks) {
            if(subTask.equals(example)) {
                querySubTasks.add((S) subTask);
            }
        }
        return querySubTasks;
    }

    @Override
    public <S extends SubTask> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends SubTask> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends SubTask> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends SubTask> boolean exists(Example<S> example) {
        return subTasks.contains(example);
    }

    @Override
    public <S extends SubTask, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<SubTask> getSubTasksOfTask(long task_id) {
        List<SubTask> querySubTasks = new ArrayList<>();
        for(SubTask subTask : subTasks) {
            if(subTask.getTask().getId() == task_id) {
                querySubTasks.add(subTask);
            }
        }
        return querySubTasks;
    }
}
