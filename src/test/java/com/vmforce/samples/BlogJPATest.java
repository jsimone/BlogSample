package com.vmforce.samples;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlogJPATest {

	private EntityManager em;
	
	private String postTitle = "JPA unit Test Post Title";
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
		
		//remove this when @OneToMany works
		PostToCommentFix.popluateComments(em, post);
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
