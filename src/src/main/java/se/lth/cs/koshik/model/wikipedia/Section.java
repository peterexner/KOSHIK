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

package se.lth.cs.koshik.model.wikipedia;

import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.avro.AvroAnnotation;
import se.lth.cs.koshik.model.text.Span;

public class Section extends Span {
	private static final String FEATURE_TITLE = "TITLE";
	private String title;
	
	public Section(Document document) {
		super(document);
	}

	public Section(Document document, AvroAnnotation avroAnnotation) {
		super(document, avroAnnotation);
	}

	public String getTitle() {
		if(title == null) {
			title = this.getFeature(Section.FEATURE_TITLE);
		}
		
		if(title == null) {
			return "";
		} else {
			return title;
		}
	}

	public void setTitle(String title) {
		this.setFeature(Section.FEATURE_TITLE, title);
		this.title = title;
	}
	
	
}
