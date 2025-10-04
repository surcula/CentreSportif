package dto;

import java.util.List;

public class Page <T>{
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    private Page(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = Math.max(1, page);
        this.size = Math.max(1, size);
        this.totalElements = Math.max(0, totalElements);
        this.totalPages = (int) Math.max(1, Math.ceil((double) this.totalElements / this.size));
    }

    public static <T> Page<T> of(List<T> content, int page, int size, long totalElements) {
        return new Page<>(content, page, size, totalElements);
    }

    public List<T> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }

    // (optionnel mais pratique pour debug/logs)
    @Override
    public String toString() {
        return String.format("Page %d/%d (size=%d, totalElements=%d)", page, totalPages, size, totalElements);
    }


}
