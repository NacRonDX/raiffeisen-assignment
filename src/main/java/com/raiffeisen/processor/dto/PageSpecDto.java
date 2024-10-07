package com.raiffeisen.processor.dto;

import com.raiffeisen.processor.constants.SortField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Getter
public class PageSpecDto {
    private int page;
    private int sizePerPage;
    private SortField sortField;
    private Sort.Direction sortDirection;
}
