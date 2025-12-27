package com.projectManagement.project.Page;

import java.io.Serializable;

    public class Page implements Serializable {
        private static final long serialVersionUID = -856460755313171820L;
        private int number;
        private int size;
        private int totalPages;
        private int contentSize;
        private long totalSize;

        public Page() {
        }

        public int getNumber() {
            return this.number;
        }

        public int getSize() {
            return this.size;
        }

        public int getTotalPages() {
            return this.totalPages;
        }

        public int getContentSize() {
            return this.contentSize;
        }

        public long getTotalSize() {
            return this.totalSize;
        }

        public void setNumber(final int number) {
            this.number = number;
        }

        public void setSize(final int size) {
            this.size = size;
        }

        public void setTotalPages(final int totalPages) {
            this.totalPages = totalPages;
        }

        public void setContentSize(final int contentSize) {
            this.contentSize = contentSize;
        }

        public void setTotalSize(final long totalSize) {
            this.totalSize = totalSize;
        }
    }
