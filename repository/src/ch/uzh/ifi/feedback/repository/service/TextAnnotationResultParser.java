package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.TextAnnotation;

@Singleton
public class TextAnnotationResultParser extends DbResultParser<TextAnnotation> {

	public TextAnnotationResultParser() {
		super(TextAnnotation.class);
	}

}
