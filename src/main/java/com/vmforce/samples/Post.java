package com.vmforce.samples;

import java.util.List;

import javax.persistence.*;

import com.force.sdk.jpa.annotation.CustomObject;

/**
 * Entity object representing a blog post
 * 
 * @author jsimone
 *
 */
@Entity
@CustomObject(enableFeeds=true)
public class Post {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
    
    private String title;
    
    private String body;
    
    @OneToMany(mappedBy="post", fetch=FetchType.EAGER)
    private List<Comment> comments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
}
