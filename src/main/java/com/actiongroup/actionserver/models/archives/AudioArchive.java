package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.archives.media.Audio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.util.List;

@Data
@Entity
public class AudioArchive extends Archive {
    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    private List<Audio> audios;
}
