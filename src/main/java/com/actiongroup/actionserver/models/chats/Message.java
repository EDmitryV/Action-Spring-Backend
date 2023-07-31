package com.actiongroup.actionserver.models.chats;

import java.time.LocalDateTime;

import com.actiongroup.actionserver.models.EntityWithStatus;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.users.User;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Message extends EntityWithStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    private AudioArchive audioArchive;
    
    private LocalDateTime at;
    

    boolean isPinned;

    @Enumerated(EnumType.STRING)
    private MsgStatus msgStatus;

    public enum MsgStatus {
        RECEIVED, DELIVERED
    }


}
