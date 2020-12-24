package com.ernazarsembayev.githubquery.model;
import com.google.gson.annotations.SerializedName;


public class Project {
    private final int id;
    @SerializedName("node_id")
    private final String nodeId;
    @SerializedName("name")
    private final String name;
    @SerializedName("full_name")
    private final String fullName;
    @SerializedName("description")
    private final String description;
    @SerializedName("created_at")
    private final String createdAt;


    public Project(int id, String nodeId,
                   String name, String fullName,
                   String description, String createdAt) {
        this.id = id;
        this.nodeId = nodeId;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Переопределение метода для класса Project
    @Override
    public String toString() {
        return String.format("\nName: %s\nDescription: %s\nFull name: %s\nID: %s\nNodeID: %s\nCreated at: %s\n\n",
                name, description, fullName, id, nodeId, createdAt);
    }
}