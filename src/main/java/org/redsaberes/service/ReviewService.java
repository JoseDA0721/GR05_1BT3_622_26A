package org.redsaberes.service;

public interface ReviewService {
	void validateCommentLength(String comment);

	boolean containsOffensiveContent(String comment);
}
