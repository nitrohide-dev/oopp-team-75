package commons;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


@Entity
public class Board {
    @Id
    private Long id;

    private List<Page> pages;

    public Board(List<Page> pages) {
        this.pages = pages;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void addPage(Page page) {
        this.pages.add(page);
    }

}
