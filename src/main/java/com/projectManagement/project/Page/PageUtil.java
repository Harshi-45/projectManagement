package com.projectManagement.project.Page;


import com.projectManagement.project.Page.Page;

public class PageUtil {
        public PageUtil() {
        }

        public static <T> Page fromDomainPage(org.springframework.data.domain.Page<T> domainPage) {
            Page page = new Page();
            page.setNumber(domainPage.getTotalElements() == 0L ? domainPage.getNumber() : domainPage.getNumber() + 1);
            page.setSize(domainPage.getSize());
            page.setTotalPages(domainPage.getTotalPages());
            page.setContentSize(domainPage.getNumberOfElements());
            page.setTotalSize(domainPage.getTotalElements());
            return page;
        }
    }

