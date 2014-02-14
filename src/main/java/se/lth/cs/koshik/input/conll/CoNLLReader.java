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

import java.util.ArrayList;
import java.util.List;

import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;


public abstract class CoNLLReader {
	protected static final String SENTENCE_SEPARATOR = " ";
	protected static final String TOKEN_SEPARATOR = " ";
	
	public void read(String text, Document document) {
		StringBuilder content = new StringBuilder();
		String[] textLines = text.split("\n");
				
		List<String> lines = new ArrayList<String>();
		Sentence sentence = new Sentence(document);
		String separator = "";
		
		for(String textLine:textLines) {
			textLine = textLine.trim();
			
			if (isFirstLine(textLine) && lines.size() > 0) {
				content.append(separator);
				separator = SENTENCE_SEPARATOR;
				this.processSentenceLines(lines, content, document, sentence);
				
				lines = new ArrayList<String>();
				
				sentence = new Sentence(document);
			} 
			
			if(textLine.length() > 0) {
				lines.add(textLine);
			}
		}

		if(lines.size() > 0) {
			content.append(separator);
			this.processSentenceLines(lines, content, document, sentence);
		}
		
		content.append("A");
		document.setContent(content.toString());
	}
	
	private static boolean isFirstLine(String line) {
		String[] columns = line.split("\\t");
		if(columns.length > 0) {
			if(columns[0].trim().equalsIgnoreCase("1")) {
				return true;
			}
		}

		return false;
	}

	protected abstract void processSentenceLines(List<String> sentenceLines, StringBuilder content, Document document, Sentence sentence);
}
