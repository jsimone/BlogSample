package com.vmforce.samples;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BlogController {

	private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

	@PersistenceContext
	EntityManager em;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView home(ModelAndView mv) {		
		List<Post> posts = em.createQuery("SELECT p FROM Post p order by createddate desc").getResultList();		
		for (Post post : posts) {
			PostToCommentFix.popluateComments(em, post);
		}
		
		mv.addObject("posts", posts);
		mv.setViewName("blog");
		return mv;
	}

	@RequestMapping(value="/admin/createPost", method=RequestMethod.POST)
	public String newPost(@RequestParam("title") String title,
						@RequestParam("body") String body) {
		Post post = createPost(title, body);
		logger.info("Created post: "+post.getTitle()+" (with id "+post.getId()+")");
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/createComment", method=RequestMethod.POST)
	public String newComment(@RequestParam("postId") String postId,
							@RequestParam("name") String name,
							@RequestParam("comment") String commentText) {
		Post post = em.find(Post.class, postId);
		Comment comment = createComment(name, commentText, post);
		logger.info("Created comment by: "+comment.getAuthor()+" (with id "+comment.getId()+")");
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/admin/deleteComment", method=RequestMethod.POST)
	@Transactional
	public String deleteComment(@RequestParam("commentId") String commentId) {
		Comment comment = em.find(Comment.class, commentId);
		em.remove(comment);
		logger.info("Comment deleted with id "+comment.getId()+")");
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/admin/deletePost", method=RequestMethod.POST)
	@Transactional
	public String deletePost(@RequestParam("postId") String postId) {
		Post post = em.find(Post.class, postId);
		em.remove(post);
		logger.info("Post deleted with id "+post.getId()+")");
		
		return "redirect:/";
	}
	
	
	@Transactional
	private Post createPost(String title, String body) {
		Post post = new Post();
		post.setTitle(title);
		post.setBody(body);
		em.persist(post);
		return post;
	}
	
	@Transactional
	private Comment createComment(String author, String body, Post post) {
		Comment comment = new Comment();
		comment.setAuthor(author);
		comment.setBody(body);
		comment.setPost(post);
		em.persist(comment);
		return comment;
	}
}

