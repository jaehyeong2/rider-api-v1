package jjfactory.simpleapi.global.dto.res;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagingRes<T> {
    private int currentPage;
    private int totalPage;
    private long totalCount;
    private List<T> resultList;

    public PagingRes(Page<T> page) {
        this.currentPage = page.getPageable().getPageNumber()+1;
        this.totalPage = page.getTotalPages();
        this.totalCount = page.getTotalElements();
        this.resultList = page.getContent();
    }
}
