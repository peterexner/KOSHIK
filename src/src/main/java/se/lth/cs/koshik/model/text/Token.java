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

package se.lth.cs.koshik.model.text;

import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Annotation;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.avro.AvroAnnotation;

public class Token extends Annotation {
	private Sentence sentence;
	
	public Token(Document document) {
		super(document);
	}

	public Token(Document document, AvroAnnotation avroAnnotation) {
		super(document, avroAnnotation);
	}

	public Token(Document document, AvroAnnotation avroAnnotation, Sentence sentence) {
		super(document, avroAnnotation);

		this.sentence = sentence;
	}
	
	public void addSemanticRole(Token predicateToken, String semanticRole) {
		if(this.hasFeature(CoNLLFeature.APRED)) {
			this.setFeature(CoNLLFeature.APRED, this.getFeature(CoNLLFeature.APRED) + ";" + predicateToken.getFeature(CoNLLFeature.ID) + ":" + semanticRole);
		} else {
			this.setFeature(CoNLLFeature.APRED, predicateToken.getFeature(CoNLLFeature.ID) + ":" + semanticRole);
		}
	}

	private Sentence getSentence() {
		if(sentence == null) {
			sentence = (Sentence) document.selectCoveringAnnotation(Sentence.class, this);
		}

		return sentence;
	}
}
