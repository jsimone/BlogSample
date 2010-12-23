package com.vmforce.samples;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

public class PostToCommentFix {
	
	public static void popluateComments(EntityManager em, Post post) {
		List<Comment> comments = 
			em.createQuery("SELECT c FROM Comment c").getResultList();
		
		List<Comment> postComments = new ArrayList<Comment>();
		Post currPost = null;
		
		for (Comment comment : comments) {
			currPost = comment.getPost();
			//TODO: currPost is null. That's why it's not working.
			if(currPost != null && post.getId().equals(currPost.getId())) {
				postComments.add(comment);
			}
		}
		
		post.setComments(postComments);
	}
	
}
