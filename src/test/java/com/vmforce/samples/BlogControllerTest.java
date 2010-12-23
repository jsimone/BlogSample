package com.vmforce.samples;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ModelAndView;

public class BlogControllerTest {
	
	@javax.annotation.Resource
	private BlogController blogController;
	
	private String postTitle = "Controller unit Test Post Title";
	private String postId;
	private String commentAuthor = "Author";
	private String commentBody = "This is my comment";
	private String commentId;
	
	@Before
	public void setUp() throws Exception {
		
//		Resource resource = new FileSystemResource("src/main/webapp/WEB-INF/spring/root-context.xml");
//		XmlBeanFactory factory = new XmlBeanFactory(resource);
		
//		blogController = factory.getBean(BlogController.class);
	}
	
	@Test
	public void testPostList() throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
		ModelAndView retModel = blogController.home(modelAndView);
		
		System.out.println("retmodel: " + retModel.toString());
		assertEquals("blog", retModel.getViewName());
		
	}
	
	@After
	public void tearDown() throws Exception {
		
		
	}
	
}
