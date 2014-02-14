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

import java.util.HashMap;

import se.lth.cs.koshik.model.avro.AvroAnnotation;

public abstract class Annotation implements Comparable<Annotation> {
	protected final Document document;
	
	private final AvroAnnotation avroAnnotation; 
	
	public Annotation(Document document) {
		this.document = document;
		this.avroAnnotation = AvroAnnotation.newBuilder().setLayer(this.getClass().getCanonicalName()).setFeatures(new HashMap<CharSequence, CharSequence>()).build();
		
		document.registerAnnotation(this);
	}
	
	public Annotation(Document document, AvroAnnotation avroAnnotation) {
		this.document = document;
		this.avroAnnotation = avroAnnotation;
		
		document.registerAnnotation(this);
	}
	
	@Override
	public int compareTo(Annotation other) {
		return this.getBegin().compareTo(other.getBegin());
	}
	
	public boolean covers(Annotation other) {
		return this.getBegin() <= other.getBegin() && this.getEnd() >= other.getEnd();
	}
	
	public Integer getBegin() {
		return avroAnnotation.getBegin();
	}
	
	public void setBegin(Integer begin) {
		avroAnnotation.setBegin(begin);
	}
	
	public Integer getEnd() {
		return avroAnnotation.getEnd();
	}
	
	public void setEnd(Integer end) {
		avroAnnotation.setEnd(end);
	}
	
	public String getContent() {
		if(avroAnnotation.getContent() == null || avroAnnotation.getContent().toString().equalsIgnoreCase("")) {
			if(this.getBegin() != -1 && this.getEnd() != -1) {
				return document.getContent().substring(this.getBegin(), this.getEnd());
			}
		}
		
		return avroAnnotation.getContent().toString();
	}

	public void setContent(String content) {
		avroAnnotation.setContent(content);
	}
	
	public boolean hasFeature(String feature) {
		return avroAnnotation.getFeatures().containsKey(feature);
	}
	
	public String getFeature(String feature) {
		return avroAnnotation.getFeatures().get(feature).toString();
	}
	
	public void setFeature(String feature, String value) {
		avroAnnotation.getFeatures().put(feature, value);
	}
	
	public Document getDocument() {
		return document;
	}
	
	protected AvroAnnotation getAvroAnnotation() {
		return avroAnnotation;
	}
}
