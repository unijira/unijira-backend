package it.unical.unijira.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter @Setter @ToString
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Basic(optional = false)
    private String name;

    @Column
    @Basic(optional = false)
    private String key;

    @Column
    private URL icon;

    @ManyToOne
    @JoinColumn
    private User owner;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Member> members = new ArrayList<>();


}