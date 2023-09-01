package org.example.demo.rest.webservice.pagination;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;

@Getter
public class PaginationDiscoverabilityEvent<T extends Serializable> extends ApplicationEvent {

    private final UriComponentsBuilder uriComponentsBuilder;
    private final HttpServletResponse servletResponse;
    private final int page;
    private final int totalPages;

    public PaginationDiscoverabilityEvent(Class<T> tClass, UriComponentsBuilder uriComponentsBuilder, HttpServletResponse servletResponse, int page, int totalPages) {
        super(tClass);
        this.uriComponentsBuilder = uriComponentsBuilder;
        this.servletResponse = servletResponse;
        this.page = page;
        this.totalPages = totalPages;
    }
}
