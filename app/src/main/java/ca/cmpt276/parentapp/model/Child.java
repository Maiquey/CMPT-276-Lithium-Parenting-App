package ca.cmpt276.parentapp.model;

/**
 * Child class:
 *
 * Class representing a configured child
 * Stores the child's name
 *
 */
public class Child {
    private String name;

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

