package com.vmforce.samples;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.salesforce.persistence.datanucleus.annotation.CustomObject;

@Entity
@CustomObject(enableFeeds=true)
public class Post {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
    
    private String title;
    
    private String body;
    
    @Transient
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
