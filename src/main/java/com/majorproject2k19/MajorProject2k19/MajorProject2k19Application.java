package com.majorproject2k19.MajorProject2k19;

import com.majorproject2k19.MajorProject2k19.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableScheduling
@EnableConfigurationProperties(FileStorageProperties.class)
public class MajorProject2k19Application {
	public static void main(String[] args) {
		SpringApplication.run(MajorProject2k19Application.class, args);
	}
}
