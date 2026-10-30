package se.yrgo.libraryapp.entities;

public class DdsClassification {
    private int code;
    private String classification;

    public DdsClassification(int code, String classification) {
        this.code = code;
        this.classification = classification;
    }

    public int getCode() {
        return code;
    }

    public String getClassification() {
        return classification;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classification == null) ? 0 : classification.hashCode());
        result = prime * result + code;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DdsClassification other = (DdsClassification) obj;
        if (classification == null) {
            if (other.classification != null)
                return false;
        }
        else if (!classification.equals(other.classification))
            return false;
        return (code == other.code);
    }

    @Override
    public String toString() {
        return "DdsClassification [classification=" + classification + ", code=" + code + "]";
    }   
}
