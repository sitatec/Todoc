package com.cleanup.todoc.model;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * <p>Models for project in which tasks are included.</p>
 *
 * @author Gaëtan HERFRAY
 */
@Entity
public class Project {
    /**
     * The unique identifier of the project
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * The name of the project
     */
    @NonNull
    private final String name;

    /**
     * The hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    private final int color;

    /**
     * Instantiates a new Project.
     *
     * @param name  the name of the project to set
     * @param color the hex (ARGB) code of the color associated to the project to set
     */
    public Project(@NonNull String name, @ColorInt int color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Used by Room (ID is auto-generated)
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier of the project.
     *
     * @return the unique identifier of the project
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the name of the project.
     *
     * @return the name of the project
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns the hex (ARGB) code of the color associated to the project.
     *
     * @return the hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    public int getColor() {
        return color;
    }

    @Override
    @NonNull
    public String toString() {
        return getName();
    }
}
