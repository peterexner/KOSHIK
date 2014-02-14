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

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;
import se.lth.cs.koshik.model.text.Token;

import is2.data.SentenceData09;
import is2.tag.Tagger;

public class IS2POSTagger implements ContentProcessor {
	private Tagger tagger;
	
	public IS2POSTagger(String modelFileName) {
		String[] argsT = {"-model","./model.zip/model/is2/" + modelFileName};
		tagger = new is2.tag.Tagger(new is2.tag.Options(argsT));
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
			
			sentenceData09.pfeats=new String[sentenceData09.forms.length];
			sentenceData09.plemmas=new String[sentenceData09.forms.length];
			
			sentenceData09.pfeats[0] = "_";
			sentenceData09.plemmas[0] = is2.io.CONLLReader09.ROOT;
			
			i = 1;
			for(Token token:document.selectCoveredAnnotations(Token.class, sentence)) {
				sentenceData09.pfeats[i] = token.getFeature(CoNLLFeature.PFEAT);
				sentenceData09.plemmas[i] = token.getFeature(CoNLLFeature.PLEMMA);
				i++;
			}
			
			tagger.apply(sentenceData09);
			
			i = 1;
			for(Token token:document.selectCoveredAnnotations(Token.class, sentence)) {
				token.setFeature(CoNLLFeature.PPOS, sentenceData09.ppos[i]);
				i++;
			}
		}
	}

}
