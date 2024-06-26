package com.actiongroup.actionserver.models.archives;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Video;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class VideoArchive extends Archive{
    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    private List<Video> videos;
    
}