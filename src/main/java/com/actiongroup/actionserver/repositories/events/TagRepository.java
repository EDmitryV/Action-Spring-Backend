package com.actiongroup.actionserver.repositories.events;

import com.actiongroup.actionserver.models.events.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {

    public Tag findByName(String name);

    public List<Tag> findByParentTag(Tag tag);
}
