package com.springboot.hospital.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:file_storage_config/file.directory.properties")
public class PropertiesConfig {

}
