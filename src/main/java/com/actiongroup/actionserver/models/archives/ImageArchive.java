package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class ImageArchive extends Archive{
    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    private List<Image> images;
}
