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

import java.util.HashMap;
import java.util.TreeSet;

import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Annotation;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.avro.AvroAnnotation;

public class Sentence extends Annotation {
	private final Token rootToken;
	private TreeSet<Token> tokens;
	private final AvroAnnotation rootTokenAvroAnnotation;
	
	
	public Sentence(Document document) {
		super(document);
		
		rootTokenAvroAnnotation = AvroAnnotation.newBuilder().setFeatures(new HashMap<CharSequence, CharSequence>()).build();
		this.rootToken = new RootToken(document, rootTokenAvroAnnotation, this);
		this.rootToken.setFeature(CoNLLFeature.ID, "0");
		this.rootToken.setFeature(CoNLLFeature.FORM, "ROOT");
		this.tokens = new TreeSet<Token>();
	}
	
	public Sentence(Document document, AvroAnnotation avroAnnotation) {
		super(document, avroAnnotation);
		
		rootTokenAvroAnnotation = AvroAnnotation.newBuilder().setFeatures(new HashMap<CharSequence, CharSequence>()).build();
		this.rootToken = new RootToken(document, rootTokenAvroAnnotation, this);
		this.rootToken.setFeature(CoNLLFeature.ID, "0");
		this.rootToken.setFeature(CoNLLFeature.FORM, "ROOT");
		this.tokens = new TreeSet<Token>();
	}
	
	public Token getToken(int index) {
		Token returnedToken = null;
		int i = 1;
		
		for(Token token:this.getTokens()) {
			if(i == index) {
				returnedToken = token;
				break;
			}
			i++;
		}
		
		return returnedToken;
	}
	
	public TreeSet<Token> getTokens() {
		if(this.tokens.size() == 0) {
			this.tokens = document.selectCoveredAnnotations(Token.class, this);
		}
		return this.tokens;
	}
	
	public Token getRootToken() {
		return rootToken;
	}

	public TreeSet<Token> getPredicateTokens() {
		TreeSet<Token> predicateTokens = new TreeSet<Token>();
		
		for(Token token:getTokens()) {
			if(token.hasFeature(CoNLLFeature.PRED)) {
				predicateTokens.add(token);
			}
		}
		
		return predicateTokens;
	}
}
