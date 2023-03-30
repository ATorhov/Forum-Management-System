package com.example.forummanagementsystem.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="opinions")
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opinion_id")
    private Long id;


    @Column(name = "type")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opinion opinion = (Opinion) o;
        return Objects.equals(id, opinion.id) && Objects.equals(type, opinion.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

}
