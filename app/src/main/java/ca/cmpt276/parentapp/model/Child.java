package ca.cmpt276.parentapp.model;

import androidx.annotation.NonNull;

/**
 * Child class:
 *
 * Class representing a configured child
 * Stores the child's name
 *
 */
public class Child {
    private String name;
    private String photo;


    public Child (String name, String photo){
        this.name= name;
        this.photo = photo ;
    }

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto(){
        return photo;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

