package com.actiongroup.actionserver.models.users;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class UserRelation {

    public enum RelationTypes{
        Blocked,
        Subscription,
    }


    @Enumerated(EnumType.ORDINAL)
    private RelationTypes relationType;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private User sourceUser;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private User targetUser;

}
