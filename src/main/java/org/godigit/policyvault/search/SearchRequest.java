package org.godigit.policyvault.search;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SearchRequest {

    @Size(max = 100, message = "Department must be at most 100 characters")
    private String department;

    @Size(max = 200, message = "Keyword must be at most 200 characters")
    private String keyword;

    private LocalDate fromDate;
    private LocalDate toDate;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be >= 1")
    @Max(value = 200, message = "Size must be <= 200")
    private Integer size = 20;

    @Pattern(regexp = "^[A-Za-z0-9_.]+$", message = "Invalid sortBy field")
    private String sortBy = "updatedAt";

    @Pattern(regexp = "(?i)ASC|DESC", message = "sortDir must be ASC or DESC")
    private String sortDir = "DESC";

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = emptyToNull(department);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = emptyToNull(keyword);
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        normalizeDates();
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
        normalizeDates();
    }

    public Integer getPage() {
        return page != null ? page : 0;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size != null ? size : 20;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy != null ? sortBy : "updatedAt";
    }

    public void setSortBy(String sortBy) {
        this.sortBy = emptyToNull(sortBy);
    }

    public String getSortDir() {
        return sortDir != null ? sortDir : "DESC";
    }

    public void setSortDir(String sortDir) {
        this.sortDir = emptyToNull(sortDir);
    }

    public Pageable toPageable() {
        Sort sort = "ASC".equalsIgnoreCase(getSortDir())
                ? Sort.by(getSortBy()).ascending()
                : Sort.by(getSortBy()).descending();
        return PageRequest.of(getPage(), getSize(), sort);
    }

    private void normalizeDates() {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            LocalDate tmp = fromDate;
            fromDate = toDate;
            toDate = tmp;
        }
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}