package server.database;

import commons.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class TaskRepositoryTest implements TaskRepository{
    private List<Task> tasks;

    private SubTaskRepository repo;

    public TaskRepositoryTest(SubTaskRepository repo) {
        tasks = new ArrayList<>();
        this.repo = repo;
    }

    public TaskRepositoryTest() {
        tasks = new ArrayList<>();
        this.repo = new SubTaskRepositoryTest();
    }

    public TaskRepositoryTest(List<Task> tasks) {
        this.tasks = tasks;
        this.repo = new SubTaskRepositoryTest();
    }

    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public List<Task> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Task> findAllById(Iterable<Long> longs) {
        List<Task> queryTasks = new ArrayList<>();
        for(Long i : (Iterable<Long>) longs) {
            for(Task task : tasks) {
                if(task.getId() == i) {
                    queryTasks.add(task);
                }
            }
        }
        return queryTasks;
    }

    @Override
    public long count() {
        return tasks.size();
    }

    @Override
    public void deleteById(Long aLong) {
        for(Task task : tasks) {
            if(task.getId() == aLong) {
                tasks.remove(task);
                break;
            }
        }
    }

    @Override
    public void delete(Task entity) {
        for(Task task : tasks) {
            if(task.equals(entity)) {
                tasks.remove(task);
                break;
            }
        }

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        for(Long i : (Iterable<Long>) longs) {
            for(Task task : tasks) {
                if(task.getId() == i) {
                    tasks.remove(task);
                    break;
                }
            }
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Task> entities) {
        if (entities == null) {
            return;
        }

        for(Task task : (Iterable<Task>) entities) {
            for(Task task2 : tasks) {
                if(task.getId() == task2.getId()) {
                    tasks.remove(task2);
                    break;
                }
            }
        }
    }

    @Override
    public void deleteAll() {
        tasks.clear();
    }

    @Override
    public <S extends Task> S save(S entity) {
        for(Task task : tasks) {
            if(task.getId() == entity.getId()) {
                tasks.remove(task);
                tasks.add(entity);
                repo.saveAll(repo.getSubTasksOfTask(entity.getId()));
                return entity;
            }
        }
        tasks.add(entity);
        repo.saveAll(repo.getSubTasksOfTask(entity.getId()));
        return entity;
    }

    @Override
    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
        if (entities == null) {
            return null;
        }
        List<S> savedTasks = new ArrayList<>();
        for(Task task : (Iterable<Task>) entities) {
            for(Task task2 : tasks) {
                if(task.getId() == task2.getId()) {
                    tasks.remove(task2);
                    save(task);

                    savedTasks.add((S) task);
                    break;
                }
            }
            save(task);
            savedTasks.add((S) task);
        }
        return savedTasks;
    }

    @Override
    public Optional<Task> findById(Long aLong) {
        for(Task task : tasks) {
            if(task.getId() == aLong) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for(Task task : tasks) {
            if(task.getId() == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Task> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Task> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Task getOne(Long aLong) {
        return null;
    }

    @Override
    public Task getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Task> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Task> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Task> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Task, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
    @Override
    public int getOrderById(long id){
        return 0;
    }
    @Override
    public void updateInitialListOrder(int order,long id){

    }
    @Override
    public void updateTargetListOrder(int order,long id){

    }
    @Override
    public void moveTask(long id1,long id2,int order){

    }
}