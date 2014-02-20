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

import java.io.File;

import se.lth.cs.srl.preprocessor.tokenization.StanfordChineseSegmenterWrapper;
import se.lth.cs.srl.preprocessor.tokenization.Tokenizer;
import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;
import se.lth.cs.koshik.model.text.Token;

public class LTHStanfordChineseSegmenterWrapper implements ContentProcessor {
	private Tokenizer  tokenizer;
	
	public LTHStanfordChineseSegmenterWrapper() {
		tokenizer = new StanfordChineseSegmenterWrapper(new File("./model.zip/model/stanford/chineseseg"));

	}

	@Override
	public void process(Document document) throws Exception {
		for(Sentence sentence:document.select(Sentence.class)) {
			String tokens[] = tokenizer.tokenize(sentence.getContent());
			
			int offset = 0;
			int begin = 0;
			for(int i=1; i<tokens.length;i++) {
				if(tokens[i].equalsIgnoreCase("-LRB-") || tokens[i].equalsIgnoreCase("-RRB-")) {
					int end = offset + 2;
					if(end > sentence.getContent().length()) {
						end = sentence.getContent().length();
					}
					begin = sentence.getContent().indexOf(sentence.getContent().substring(offset, end).trim(), offset);
				} else {
					begin = sentence.getContent().indexOf(tokens[i], offset);
				}
				
				if(begin != -1) {
					Token koshikToken = new Token(document);
					koshikToken.setBegin(begin + sentence.getBegin());
					if(tokens[i].equalsIgnoreCase("-LRB-") || tokens[i].equalsIgnoreCase("-RRB-")) {
						offset = begin + 1;
					} else {
						offset = begin + tokens[i].length();
					}
					koshikToken.setEnd(offset + sentence.getBegin());
					koshikToken.setFeature(CoNLLFeature.ID, Integer.toString(i));
					koshikToken.setFeature(CoNLLFeature.FORM, tokens[i]);
				}
			}
		}
	}

}
