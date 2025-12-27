package com.projectManagement.project.Page;


import com.projectManagement.project.Page.Page;

import java.io.Serializable;
import java.util.List;


    public class PageResponse<T> implements Serializable {
        Page page;
        List<T> content;

        public PageResponse() {
        }

        public Page getPage() {
            return this.page;
        }

        public List<T> getContent() {
            return this.content;
        }

        public void setPage(final Page page) {
            this.page = page;
        }

        public void setContent(final List<T> content) {
            this.content = content;
        }
    }
