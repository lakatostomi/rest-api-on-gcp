package org.example.demo.rest.webservice.pagination;

import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.rest.webservice.util.LinkUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@SuppressWarnings({"rawtypes"})
@Component
public class PaginationDiscoverabilityListener implements ApplicationListener<PaginationDiscoverabilityEvent> {


    @Override
    public void onApplicationEvent(PaginationDiscoverabilityEvent event) {
        addLinkHeader(event.getUriComponentsBuilder(), event.getServletResponse(),
                event.getPage(), event.getTotalPages());
    }

    private void addLinkHeader(UriComponentsBuilder uriComponentsBuilder, HttpServletResponse servletResponse,
                               int page, int totalPages) {
        uriComponentsBuilder.path("/api/rest/v1/countries");
        StringBuffer linkHeader = new StringBuffer();
        linkHeader.append("{");

        if (hasNextPage(page, totalPages)) {
            String nextPageUri = createNextPageUri(uriComponentsBuilder, page);
            linkHeader.append(LinkUtil.createLinkHeader(nextPageUri, LinkUtil.REL_NEXT));
            linkHeader.append(",");
        }

        if (hasPrevPage(page)) {
            String prevPageUri = createPrevPageUri(uriComponentsBuilder, page);
            linkHeader.append(LinkUtil.createLinkHeader(prevPageUri, LinkUtil.REL_PREV));
            linkHeader.append(",");
        }

        if (hasFirstPage(page)) {
            String firstPageUri = createFirstPageUri(uriComponentsBuilder);
            linkHeader.append(LinkUtil.createLinkHeader(firstPageUri, LinkUtil.REL_FIRST));
            linkHeader.append(",");
        }

        if (hasLastPage(page, totalPages)) {
            String lastPageUri = createLastPageUri(uriComponentsBuilder, totalPages);
            linkHeader.append(LinkUtil.createLinkHeader(lastPageUri, LinkUtil.REL_LAST));
        }

        if (linkHeader.length() > 0) {
            linkHeader.append("}");
            if (linkHeader.charAt(linkHeader.length() - 2) == ',') {
                linkHeader.deleteCharAt(linkHeader.length() - 2);
            }

            servletResponse.addHeader(HttpHeaders.LINK, linkHeader.toString());
        }
    }


    private String createLastPageUri(UriComponentsBuilder uriComponentsBuilder, int totalPages) {
        UriComponents uriComponents = uriComponentsBuilder.replaceQueryParam("page", totalPages - 1)
                .build()
                .encode();

        return uriComponents.getPath() + "?" + uriComponents.getQuery();
    }


    private String createFirstPageUri(UriComponentsBuilder uriComponentsBuilder) {
        UriComponents uriComponents = uriComponentsBuilder.replaceQueryParam("page", 0)
                .build()
                .encode();

        return uriComponents.getPath() + "?" + uriComponents.getQuery();
    }

    private String createPrevPageUri(UriComponentsBuilder uriComponentsBuilder, int page) {
        UriComponents uriComponents = uriComponentsBuilder.replaceQueryParam("page", page - 1)
                .build()
                .encode();
        return uriComponents.getPath() + "?" + uriComponents.getQuery();
    }

    private String createNextPageUri(UriComponentsBuilder uriComponentsBuilder, int page) {
        UriComponents uriComponents = uriComponentsBuilder.replaceQueryParam("page", page + 1)
                .build()
                .encode();
        return uriComponents.getPath() + "?" + uriComponents.getQuery();
    }

    private boolean hasNextPage(int page, int totalPages) {
        return totalPages - 1 > page;
    }

    private boolean hasPrevPage(int page) {
        return page > 0;
    }

    private boolean hasFirstPage(int page) {
        return hasPrevPage(page);
    }

    private boolean hasLastPage(int page, int totalPages) {
        return hasNextPage(page, totalPages);
    }
}
