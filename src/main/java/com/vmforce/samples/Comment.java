package com.vmforce.samples;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.salesforce.persistence.datanucleus.annotation.CustomObject;

/**
 * Entity object representing a comment
 * 
 * @author jsimone
 *
 */
@Entity
@CustomObject(enableFeeds=true)
public class Comment {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
    
    private String author;
    
    private String body;
    
	@ManyToOne
    private Post post;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}
