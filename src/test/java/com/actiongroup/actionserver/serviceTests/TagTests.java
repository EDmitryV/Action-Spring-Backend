package com.actiongroup.actionserver.serviceTests;

import com.actiongroup.actionserver.TestDataLoader;
import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.events.EventService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class TagTests {

    @Autowired
    private EventService eventService;

    private List<Tag> childrenTags;

    private void recursiveTagSave(Tag tag){
        if(tag.getParentTag() != null) {
            recursiveTagSave(tag.getParentTag());
        }
        eventService.saveTag(tag);
    }
    private Tag getTagTreeParent(){
        Tag tag = childrenTags.get(0);
        while (tag.getParentTag()!=null){
            tag = tag.getParentTag();
        }
        return eventService.findTagByName(tag.getName());
    }

    private void saveTags(){
        for(Tag tag: childrenTags){
            recursiveTagSave(tag);
        }
        for(int i = 0; i<childrenTags.size(); i++){
            childrenTags.set(i, eventService.findTagByName(childrenTags.get(i).getName()));
        }
    }
    @BeforeAll
    public void setUp() {
        childrenTags = TestDataLoader.createTagTree();
        System.out.println("СОХРАНЕНИ ТЕГОВ");
        saveTags();
        System.out.println("ТЕГИ СОХРАНЕНЫ");
    }

    @Test
    @Order(1)
    public void parentTagSettedCorrect(){
        Tag parent = getTagTreeParent();
        Assertions.assertEquals("спорт", parent.getName());
    }

    @Test
    @Order(2)
    public void getChildrenWorksCorrect(){
        Tag parent = getTagTreeParent();
        List<Tag> children = eventService.getTagChildren(parent);
        Assertions.assertEquals(3, children.size());
    }

    @Test
    @Order(3)
    public void removingParentTagRemovesAllChildren(){
        Tag parent = getTagTreeParent();
        eventService.deleteTag(parent);
        for(Tag tag: childrenTags){
            Assertions.assertNull(eventService.findTagByName(tag.getName()));
        }
        System.out.println("УДАЛЕНИЕ ВСЕГО");

    }



}
