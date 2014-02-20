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

package se.lth.cs.koshik.input.conll;

import java.util.List;

import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;
import se.lth.cs.koshik.model.text.Token;



public class CoNLL2006Reader extends CoNLLReader {
	@Override
	protected void processSentenceLines(List<String> sentenceLines, StringBuilder content, Document document, Sentence sentence) {
		sentence.setBegin(content.length());
		buildTokens(sentenceLines, content, document, sentence);
		sentence.setEnd(content.length());
		
		buildDependencyEdges(sentenceLines, document, sentence);
	}
	
	private void buildTokens(List<String> sentenceLines, StringBuilder content, Document document, Sentence sentence) {
		String separator = "";
		
		for(String sentenceLine:sentenceLines) {
			String[] fields = sentenceLine.split("\t");
			
			if(fields.length == 8 || fields.length == 10) {
				try {
					Integer.parseInt(fields[0]);			
				} catch (NumberFormatException e) {
					System.err.println("Unknown format: " + sentenceLine);
				}
			} else {
				System.err.println("Unknown format: " + sentenceLine);
			}
			
			
			Token token = new Token(document);
			
			token.setFeature(CoNLLFeature.ID, fields[0]);
			token.setFeature(CoNLLFeature.FORM, fields[1]);
			token.setFeature(CoNLLFeature.LEMMA, fields[2]);
			token.setFeature(CoNLLFeature.CPOSTAG, fields[3]);
			token.setFeature(CoNLLFeature.POSTAG, fields[4]);
			token.setFeature(CoNLLFeature.FEATS, fields[5]);
			
			content.append(separator);
			
			token.setBegin(content.length());
			content.append(fields[1]);
			token.setEnd(content.length());
			
			separator = TOKEN_SEPARATOR;
		}
	}
	
	private void buildDependencyEdges(List<String> sentenceLines, Document document, Sentence sentence) {
		if(sentenceLines.size() != sentence.getTokens().size()) {
			System.err.println("Internal error: number of tokens not equal to number of conll tokens");
			return;
		}
		
		for(String sentenceLine:sentenceLines) {
			int index = sentenceLines.indexOf(sentenceLine) + 1;
			
			String[] fields = sentenceLine.split("\t");
			
			if(fields.length == 8 || fields.length == 10) {
				try {
					Integer.parseInt(fields[0]);			
				} catch (NumberFormatException e) {
					System.err.println("Unknown format: " + sentenceLine);
				}
			} else {
				System.err.println("Unknown format: " + sentenceLine);
			}
			
			if(!fields[6].equalsIgnoreCase("_") && !fields[7].equalsIgnoreCase("_")) {
				sentence.getToken(index).setFeature(CoNLLFeature.HEAD, fields[6]);
				sentence.getToken(index).setFeature(CoNLLFeature.DEPREL, fields[7]);				
			}
			
			if(fields.length == 10) {
				if(!fields[8].equalsIgnoreCase("_") && !fields[9].equalsIgnoreCase("_")) {
					sentence.getToken(index).setFeature(CoNLLFeature.PHEAD, fields[8]);
					sentence.getToken(index).setFeature(CoNLLFeature.PDEPREL, fields[9]);
				}
			}
		}
	}
}
