package com.ernazarsembayev.githubquery.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectData {
    @SerializedName("items")
    private final List<Project> projects;

    @SerializedName("total_count")
    private final int totalCount;

    public ProjectData(List<Project> projects, int totalCount) {
        this.projects = projects;
        this.totalCount = totalCount;
    }


    @Override
    public String toString() {
        StringBuilder itemsText = new StringBuilder();
        boolean first = true;
        if (totalCount != 0) {
            for (Project item : projects) {
                if (first) {
                    first = false;
                }
                itemsText.append(item);
            }
            return "Total count: " + totalCount + "\n\n"
                    + "Projects: " + itemsText.toString();
        } else return "Total count: " + totalCount + "\n\nNothing to show, try again.";
    }

}