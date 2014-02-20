/**
 * KOSHIK is an NLP framework for large scale processing using Hadoop. 
 * Copyright © 2014 Peter Exner
 * 
 * This file is part of KOSHIK.
 *
 * KOSHIK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KOSHIK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KOSHIK.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.lth.cs.koshik.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.TreeSet;

import se.lth.cs.koshik.model.avro.AvroAnnotation;
import se.lth.cs.koshik.model.avro.AvroDocument;


public class Document {
	private final AvroDocument avroDocument;
	private final IdentityHashMap<AvroAnnotation, Annotation> annotations;

	public Document() {
		avroDocument = AvroDocument.newBuilder().setAttributes(new HashMap<CharSequence, CharSequence>()).setAnnotations(new ArrayList<AvroAnnotation>()).build();

		annotations = new IdentityHashMap<AvroAnnotation, Annotation>();
	}

	public Document(AvroDocument avroDocument) {
		this.avroDocument = avroDocument;

		annotations = new IdentityHashMap<AvroAnnotation, Annotation>();
	}


	public <T extends Annotation> TreeSet<T> select(final Class<T> type) {
		String layer = type.getCanonicalName();
		TreeSet<T> annotationSet = new TreeSet<T>();

		for(AvroAnnotation avroAnnotation:avroDocument.getAnnotations()) {
			if(avroAnnotation.getLayer().equals(layer)) {
				T annotation = getOrCreateAnnotation(type, avroAnnotation);
				annotationSet.add(annotation);
			}
		}

		return annotationSet;
	}

	public <T extends Annotation> TreeSet<T> selectCoveredAnnotations(final Class<T> type, Annotation coveringAnnotation) {
		String layer = type.getCanonicalName();
		TreeSet<T> annotationSet = new TreeSet<T>();

		for(AvroAnnotation avroAnnotation:avroDocument.getAnnotations()) {
			if(avroAnnotation.getLayer().equals(layer)) {
				T annotation = getOrCreateAnnotation(type, avroAnnotation);
				if(coveringAnnotation.covers(annotation)) {
					annotationSet.add(annotation);
				}
			}
		}

		return annotationSet;
	}

	public <T extends Annotation> T selectCoveringAnnotation(final Class<T> type, Annotation coveredAnnotation) {
		String layer = type.getCanonicalName();
		T annotation = null;

		for(AvroAnnotation avroAnnotation:avroDocument.getAnnotations()) {
			if(avroAnnotation.getLayer().equals(layer)) {
				annotation = getOrCreateAnnotation(type, avroAnnotation);
				if(annotation.covers(coveredAnnotation)) {
					return annotation;
				}
			}
		}

		return null;
	}

	public String getIdentifier() {
		return avroDocument.getIdentifier().toString();
	}

	public void setIdentifier(String identifier) {
		avroDocument.setIdentifier(identifier);
	}

	public String getTitle() {
		return avroDocument.getTitle().toString();
	}

	public void setTitle(String title) {
		avroDocument.setTitle(title);
	}

	public String getRevision() {
		return avroDocument.getRevision().toString();
	}

	public void setRevision(String revision) {
		avroDocument.setRevision(revision);
	}

	public String getLanguage() {
		return avroDocument.getLanguage().toString();
	}

	public void setLanguage(String language) {
		avroDocument.setLanguage(language);
	}
	
	public String getEncoding() {
		return avroDocument.getEncoding().toString();
	}

	public void setEncoding(String encoding) {
		avroDocument.setEncoding(encoding);
	}

	public String getSource() {
		return avroDocument.getSource().toString();
	}

	public void setSource(String source) {
		avroDocument.setSource(source);
	}

	public String getContent() {
		return avroDocument.getContent().toString();
	}

	public void setContent(String content) {
		avroDocument.setContent(content);
	}

	public String getAttribute(String attribute) {
		return avroDocument.getAttributes().get(attribute).toString();
	}

	public void setAttribute(String attribute, String value) {
		avroDocument.getAttributes().put(attribute, value);
	}

	public <T extends Annotation> T getOrCreateAnnotation(final Class<T> type, AvroAnnotation avroAnnotation) {
		@SuppressWarnings("unchecked")
		T annotation = (T) annotations.get(avroAnnotation);
		
		if(annotation == null) {
			try {
				annotation = type.getDeclaredConstructor(Document.class, AvroAnnotation.class).newInstance(this, avroAnnotation);
				annotations.put(avroAnnotation, annotation);
			} catch (InvocationTargetException e1) {
				e1.printStackTrace(System.err);
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		return annotation;
	}

	public void registerAnnotation(Annotation annotation) {
		if(!avroDocument.getAnnotations().contains(annotation.getAvroAnnotation())) {
			avroDocument.getAnnotations().add(annotation.getAvroAnnotation());
		}

		if(!annotations.containsKey(annotation.getAvroAnnotation())) {
			annotations.put(annotation.getAvroAnnotation(), annotation);
		}
	}

	public AvroDocument getAvroDocument() {
		return avroDocument;
	}
}
