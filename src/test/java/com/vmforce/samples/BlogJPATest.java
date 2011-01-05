package com.vmforce.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.*;

import org.junit.*;

public class BlogJPATest {

	private EntityManager em;
	
	private String postTitle = "JPA unit Test Post Title";
	private String postBody300Long = "This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post This is a long post ";
	private String postId;
	private String commentAuthor = "Author";
	private String commentBody = "This is my comment";
	private String commentId;
	
	@Before
	public void setUp() throws Exception {
		EntityManagerFactory fac = Persistence.createEntityManagerFactory("forceDatabase");
		em = fac.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Post post = new Post();
			post.setTitle(postTitle);
			
			em.persist(post);
	
			em.flush();
			
			Comment comment = new Comment();
			
			comment.setAuthor(commentAuthor);
			comment.setBody(commentBody);
			comment.setPost(post);
			
			em.persist(comment);
			em.flush();
			tx.commit();
			
			postId = post.getId();
			commentId = comment.getId();
			
			
		}
		finally {
			if(tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Test
	public void testRetrievePost() throws Exception {
		
		Post post = em.find(Post.class, postId);
		
		assertEquals(postId, post.getId());
		assertEquals(postTitle, post.getTitle());
	}
	
	@Test
	public void testRetrieveComment() throws Exception {
		
		Comment comment = em.find(Comment.class, commentId);
		
		assertEquals(commentId, comment.getId());
		assertEquals(commentAuthor, comment.getAuthor());
		assertEquals(commentBody, comment.getBody());
	}
	
	@Test
	public void testSaveLongPostBody() throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Post post = new Post();
            post.setTitle(postTitle);
            post.setBody(postBody300Long);
            
            em.persist(post);
            em.flush();
            tx.commit();
            assertTrue("Should not have reached this statement because an exception should be thrown", false);
        } catch(PersistenceException e) {
            assertEquals("body__c: data value too large:", e.getMessage().substring(0, 30));
        } finally {
            if(tx.isActive()) {
                tx.rollback();
            }
        }
	}

	@Test
	public void testRetrievePostByTitle() throws Exception {
		
		List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE title = '" + postTitle + "'").getResultList();		

		assertTrue(posts.size() > 0);
		
		Post post = posts.get(0);
		
		assertEquals(postTitle, post.getTitle());
	}
	
	@Test
	public void testRetrieveCommentByBody() throws Exception {
		
		List<Comment> comments = em.createQuery("SELECT c FROM Comment c WHERE body = '" + commentBody + "'").getResultList();		
		
		assertTrue(comments.size() > 0);
		
		Comment comment = comments.get(0);
		
		assertEquals(commentAuthor, comment.getAuthor());
		assertEquals(commentBody, comment.getBody());
	}
	
	@Test
	public void testRetrievePostFromComment() throws Exception {
		
		Comment comment = em.find(Comment.class, commentId);
		Post post = comment.getPost();
		
		assertEquals(postId, post.getId());
		assertEquals(postTitle, post.getTitle());
	}
	
	@Test
	public void testRetrieveCommentFromPost() throws Exception {
	
		Post post = em.find(Post.class, postId);
		
		List<Comment> comments = post.getComments();
		
		assertEquals(1, comments.size());
		Comment comment = comments.get(0);
		
		assertEquals(commentId, comment.getId());
		assertEquals(commentAuthor, comment.getAuthor());
		assertEquals(commentBody, comment.getBody());
	}
	
	@After
	public void tearDown() throws Exception {
		
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			if(commentId != null) {
				Comment comment = em.find(Comment.class, commentId);
				em.remove(comment);
			}
		
			if(postId != null) {
				Post post = em.find(Post.class, postId);
				em.remove(post);
			}
			
			em.flush();
			
			tx.commit();
		}
		finally {
			if(tx.isActive()) {
				tx.rollback();
			}
		}		
	}
}
