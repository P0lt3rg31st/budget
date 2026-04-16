package ru.utmn.budget;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@EqualsAndHashCode
@ToString
public class OffsetPageRequest implements Pageable {

    private final long offset;
    private final int pageSize;
    private final Sort sort;

    private OffsetPageRequest(long offset, int pageSize, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be >= 0");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be >= 1");
        }

        this.offset = offset;
        this.pageSize = pageSize;
        this.sort = sort == null ? Sort.unsorted() : sort;
    }

    public static OffsetPageRequest of(long offset, int pageSize, Sort sort) {
        return new OffsetPageRequest(offset, pageSize, sort);
    }

    public static OffsetPageRequest of(long offset, int pageSize) {
        return new OffsetPageRequest(offset, pageSize, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / pageSize);
    }

    @Override
    public Pageable next() {
        return new OffsetPageRequest(offset + pageSize, pageSize, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious()
                ? new OffsetPageRequest(offset - pageSize, pageSize, sort)
                : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPageRequest(0, pageSize, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        }
        return new OffsetPageRequest((long) pageNumber * pageSize, pageSize, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > 0;
    }
}