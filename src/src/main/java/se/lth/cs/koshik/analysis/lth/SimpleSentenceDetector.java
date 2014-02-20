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

package se.lth.cs.koshik.analysis.lth;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;



public class SimpleSentenceDetector implements ContentProcessor {
	private final Pattern sentenceSeparatorPattern;

	public SimpleSentenceDetector(String sentenceSeparatorRegex) throws IOException {
		this.sentenceSeparatorPattern = Pattern.compile(sentenceSeparatorRegex, Pattern.CASE_INSENSITIVE);
	}

	@Override
	public void process(Document document) throws Exception {
		if(document.getContent() != null) { 
			Matcher matcher = sentenceSeparatorPattern.matcher(document.getContent());
			boolean match = matcher.find();
			int start = 0;
			
			while(match) {
				Sentence koshikSentence = new Sentence(document);
				koshikSentence.setBegin(start);
				koshikSentence.setEnd(matcher.end());
				
				start = matcher.end();
				match = matcher.find();
			}
			
			if(start < document.getContent().length()) {
				Sentence koshikSentence = new Sentence(document);
				koshikSentence.setBegin(start);
				koshikSentence.setEnd(document.getContent().length());
			}
		}
	}
}
