package com.actiongroup.actionserver;

import com.actiongroup.actionserver.services.archives.media.MediaService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActionServerApplication implements CommandLineRunner {
@Resource
MediaService mediaService;
	public static void main(String[] args) {
		SpringApplication.run(ActionServerApplication.class, args);
	}
	@Override
	public void run(String... arg) throws Exception {
		mediaService.init();
	}
}
