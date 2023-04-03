package server.database;

import commons.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BoardRepositoryTest implements BoardRepository {

    private final List<Board> boards;
    public BoardRepositoryTest() {
        boards = new ArrayList<>();
    }
    public BoardRepositoryTest(List<Board> boards) {
        this.boards = boards;
    }


    @Override
    public List<Board> findAll() {
        return boards;
    }

    @Override
    public List<Board> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Board> findAllById(Iterable<String> strings) {
        List<Board> queryBoards = new ArrayList<>();
        for(String  i : (Iterable<String>) strings) {
            for(Board board : boards) {
                if(board.getKey().equals(i)) {
                    queryBoards.add(board);
                }
            }
        }
        return queryBoards;

    }

    @Override
    public long count() {
        return boards.size();
    }

    @Override
    public void deleteById(String s) {
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getKey().equals(s)) {
                boards.remove(i);
                break;
            }
        }

    }

    @Override
    public void delete(Board entity) {
        boards.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        for (String s : strings) {
            for (int i = 0; i < boards.size(); i++) {
                if (boards.get(i).getKey().equals(s)) {
                    boards.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Board> entities) {
        for(Board board : entities) {
            boards.remove(board);
        }
    }

    @Override
    public void deleteAll() {
        boards.clear();

    }

    @Override
    public <S extends Board> S save(S entity) {
        boards.add(entity);
        return entity;
    }

    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) {
        for(S entity : entities) {
            boards.add(entity);
        }
        return (List<S>) boards;
    }

    @Override
    public Optional<Board> findById(String s) {
        for(Board board : boards) {
            if(board.getKey().equals(s)) {
                return Optional.of(board);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        // checks if an entity with the given id exists
        for(Board board : boards) {
            if(board.getKey().equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Board> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Board> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Board getOne(String s) {
        return null;
    }

    @Override
    public Board getById(String s) {
        for(Board board : boards) {
            if(board.getKey().equals(s)) {
                return board;
            }
        }
        return null;
    }

    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Board> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Board> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Board> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Board, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

}