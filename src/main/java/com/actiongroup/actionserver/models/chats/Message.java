package com.actiongroup.actionserver.models.chats;

import java.time.LocalDateTime;

import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;

import jakarta.persistence.*;



@Entity
public class Message {
    

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne()
    @JoinColumn(name="chat_id", referencedColumnName = "id")
    private Chat chat;


    @ManyToOne()
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;


    @ManyToOne()
    @JoinColumn(name="message_id", referencedColumnName = "id")
    private Message replyedTo;

    private String content;

    @OneToOne()
    @JoinColumn(name="image_archive_id", referencedColumnName = "id")
    private ImageArchive imageArchive;

    @OneToOne()
    @JoinColumn(name="video_archive_id", referencedColumnName = "id")
    private VideoArchive videoArchive;

    @OneToOne()
    @JoinColumn(name="music_archive_id", referencedColumnName = "id")
    private MusicArchive musicArchive;
    
    private LocalDateTime at;
    

    boolean isPinned;
}
