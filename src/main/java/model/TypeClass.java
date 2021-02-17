package model;

public enum TypeClass {
    ECONOMY("ECONOMY"),
    BUSINESS("BUSINESS"),
    FIRST_CLASS("FIRST CLASS");

    private String classType;

    TypeClass(String s) {
        this.classType = s;
    }

    public String getClassType() {
        return classType;
    }
}
