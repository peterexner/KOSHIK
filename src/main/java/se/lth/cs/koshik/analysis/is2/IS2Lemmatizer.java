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

package se.lth.cs.koshik.analysis.is2;

import java.io.IOException;
import is2.lemmatizer.Lemmatizer;
import is2.data.SentenceData09;

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;
import se.lth.cs.koshik.model.text.Token;

public class IS2Lemmatizer implements ContentProcessor {
	private Lemmatizer lemmatizer;
	
	public IS2Lemmatizer(String modelFileName) {
		String[] args={"-model","./model.zip/model/is2/" + modelFileName};
		try {
			lemmatizer= new Lemmatizer(new is2.lemmatizer.Options(args));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void process(Document document) throws Exception {
		for(Sentence sentence:document.select(Sentence.class)) {
			String[] tokens = new String[document.selectCoveredAnnotations(Token.class, sentence).size()];
			int i = 0;
			
			for(Token token:document.selectCoveredAnnotations(Token.class, sentence)) {
				tokens[i] = token.getFeature(CoNLLFeature.FORM);
				i++;
			}
			
			String[] tokensWithRoot = new String[tokens.length+1];
			tokensWithRoot[0] = is2.io.CONLLReader09.ROOT;
			System.arraycopy(tokens, 0, tokensWithRoot, 1, tokens.length);
			
			SentenceData09 sentenceData09 = new SentenceData09();
			sentenceData09.init(tokensWithRoot);
			
			this.lemmatizer.apply(sentenceData09);
			
			i = 1;
			for(Token token:document.selectCoveredAnnotations(Token.class, sentence)) {
				token.setFeature(CoNLLFeature.PLEMMA, sentenceData09.plemmas[i]);
				token.setFeature(CoNLLFeature.PFEAT, "_");
				i++;
			}
		}
	}

}
