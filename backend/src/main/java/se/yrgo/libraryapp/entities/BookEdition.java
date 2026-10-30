package se.yrgo.libraryapp.entities;

public class BookEdition {
    private int count;
    private String title;
    private String author;
    private String isbn;
    private DdsClassification dds;

    public BookEdition(int count, String title, String author, String isbn, DdsClassification dds) {
        this.count = count;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.dds = dds;
    }   

    public BookEdition(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public int getCount() {
        return count;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public DdsClassification getDdsClassification() {
        return dds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + count;
        result = prime * result + ((dds == null) ? 0 : dds.hashCode());
        result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("java:S3776") // cognitive complexity, is a generated function
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookEdition other = (BookEdition) obj;
        if (author == null) {
            if (other.author != null)
                return false;
        }
        else if (!author.equals(other.author))
            return false;
        if (count != other.count)
            return false;
        if (dds == null) {
            if (other.dds != null)
                return false;
        }
        else if (!dds.equals(other.dds))
            return false;
        if (isbn == null) {
            if (other.isbn != null)
                return false;
        }
        else if (!isbn.equals(other.isbn))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        }
        else if (!title.equals(other.title))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BookEdition [author=" + author + ", count=" + count + ", dds=" + dds
                + ", isbn=" + isbn + ", title=" + title + "]";
    }
}
