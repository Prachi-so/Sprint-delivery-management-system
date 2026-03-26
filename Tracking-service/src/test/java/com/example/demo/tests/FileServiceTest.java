package com.example.demo.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.service.FileService;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

	 @InjectMocks
	    private FileService fileService;

	    @Test
	    void testSaveFile() throws Exception {

	       
	        MockMultipartFile file = new MockMultipartFile(
	                "file",
	                "test.txt",
	                "text/plain",
	                "Hello World".getBytes()
	        );

	      
	        String result = fileService.saveFile(file);

	       
	        Assertions.assertNotNull(result);
	        Assertions.assertTrue(result.contains("uploads/"));
	        Assertions.assertTrue(result.contains("test.txt"));
	    }
}
