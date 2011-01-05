package com.vmforce.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import javax.persistence.*;

import org.junit.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ModelAndView;

public class BlogControllerTest {
	
	private BlogController blogController;
		
	private String postTitle = "Controller unit Test Post Title - ";
    private String postBody300Long = "This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post ";
	private String commentAuthor = "Author";
	private String commentBody = "This is my comment";
	
	@Before
	public void setUp() throws Exception {
		
		Resource resource = new FileSystemResource("src/main/webapp/WEB-INF/spring/root-context.xml");
		XmlBeanFactory factory = new XmlBeanFactory(resource);
		
		blogController = factory.getBean(BlogController.class);
	    
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("forceDatabase");
        EntityManager em = fac.createEntityManager();
        
        Random randomGen = new Random();
        postTitle = postTitle + randomGen.nextInt();
        
        blogController.setEm(em);
        
        blogController.newPost(postTitle, "");
	}
	
	@Test
	public void testPostList() throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
		ModelAndView retModel = blogController.home(modelAndView);
		
		assertEquals("blog", retModel.getViewName());
		
		List<Post> posts = (List<Post>) retModel.getModel().get("posts");
		assertTrue(posts.size() > 0);
		
		Post post = posts.get(0);
		assertEquals(postTitle, post.getTitle());		
	}
	
	@Test
	public void testDeletePost() throws Exception {
	    
        ModelAndView modelAndView = new ModelAndView();
        ModelAndView retModel = blogController.home(modelAndView);
        
        assertEquals("blog", retModel.getViewName());
        
        List<Post> posts = (List<Post>) retModel.getModel().get("posts");
        int postsSize = posts.size();
        
        Post post = posts.get(0);

        if(postTitle.equals(post.getTitle())) {
            blogController.deletePost(post.getId());            
        }    
 
        retModel = blogController.home(modelAndView);
                
        posts = (List<Post>) retModel.getModel().get("posts");
        assertEquals(postsSize - 1, posts.size());
	}
	
	@Test
	public void testComments() throws Exception {
	    
	    //test saving of a comment
        ModelAndView modelAndView = new ModelAndView();
        ModelAndView retModel = blogController.home(modelAndView);
                
        List<Post> posts = (List<Post>) retModel.getModel().get("posts");	
        Post post = posts.get(0);
        assertEquals(postTitle, post.getTitle());
        
        blogController.newComment(post.getId(), commentAuthor, commentBody);
        
        //test retrieval of a comment
        retModel = blogController.home(modelAndView);
      
        posts = (List<Post>) retModel.getModel().get("posts");   
        post = posts.get(0);
        assertEquals(postTitle, post.getTitle());
        List<Comment> comments = post.getComments();
        int commentCount = comments.size();
        assertTrue(commentCount > 0);
        Comment comment = comments.get(0);
        assertEquals(commentAuthor, comment.getAuthor());
        assertEquals(commentBody, comment.getBody());
        
        //test deletion of a comment
        blogController.deleteComment(comment.getId());
        
        retModel = blogController.home(modelAndView);
        
        posts = (List<Post>) retModel.getModel().get("posts");   
        post = posts.get(0);       
        comments = post.getComments();

        assertEquals(commentCount - 1, comments.size());
	}
	
	@Test
	public void testPostLengthOverflow() throws Exception {
	    try {
	        blogController.newPost(postTitle, postBody300Long);	        
        } catch(PersistenceException e) {
            assertEquals("body__c: data value too large:", e.getMessage().substring(0, 30));
        }
	}
	
	@After
	public void tearDown() throws Exception {
		
        ModelAndView modelAndView = new ModelAndView();
        ModelAndView retModel = blogController.home(modelAndView);
                
        List<Post> posts = (List<Post>) retModel.getModel().get("posts");
        
        if(posts.size() > 0) {
            Post post = posts.get(0);
            if(postTitle.equals(post.getTitle())) {
                blogController.deletePost(post.getId());            
            }   
        }
	}
	
}
