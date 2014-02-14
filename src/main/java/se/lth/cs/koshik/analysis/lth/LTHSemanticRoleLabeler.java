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

import se.lth.cs.srl.SemanticRoleLabeler;
import se.lth.cs.srl.corpus.Predicate;
import se.lth.cs.srl.corpus.Sentence;
import se.lth.cs.srl.corpus.Word;
import se.lth.cs.srl.languages.Language;
import se.lth.cs.srl.languages.Language.L;
import se.lth.cs.srl.pipeline.Pipeline;

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Token;

import is2.data.SentenceData09;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.zip.ZipFile;

public class LTHSemanticRoleLabeler implements ContentProcessor {
	private SemanticRoleLabeler semanticRoleLabeler;
	
	public LTHSemanticRoleLabeler(String modelFileName, L l) {
		try {
			Language.setLanguage(l);
			ZipFile zipFile=new ZipFile(new File("./model.zip/model/lth/" + modelFileName));
			this.semanticRoleLabeler = Pipeline.fromZipFile(zipFile);
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void process(Document document) throws Exception {
		for(se.lth.cs.koshik.model.text.Sentence sentence:document.select(se.lth.cs.koshik.model.text.Sentence.class)) {
			String[] tokens = new String[document.selectCoveredAnnotations(Token.class, sentence).size()];
			if(tokens.length > 150) {
				continue;
			}
			
			int i = 0;
			
			for(Token token:document.selectCoveredAnnotations(Token.class, sentence)) {
				tokens[i] = token.getFeature(CoNLLFeature.FORM);
				i++;
			}
			
			SentenceData09 sentenceData09 = new SentenceData09();
			sentenceData09.init(tokens);
			
			sentenceData09.pfeats=new String[sentenceData09.forms.length];
			sentenceData09.plemmas=new String[sentenceData09.forms.length];
			sentenceData09.ppos=new String[sentenceData09.forms.length];
			sentenceData09.pheads=new int[sentenceData09.forms.length];
			sentenceData09.plabels=new String[sentenceData09.forms.length];
			
			i = 0;
			for(Token token:document.selectCoveredAnnotations(Token.class, sentence)) {
				sentenceData09.pfeats[i] = token.getFeature(CoNLLFeature.PFEAT);
				sentenceData09.plemmas[i] = token.getFeature(CoNLLFeature.PLEMMA);
				sentenceData09.ppos[i] = token.getFeature(CoNLLFeature.PPOS);
				sentenceData09.pheads[i] = Integer.parseInt(token.getFeature(CoNLLFeature.PHEAD));
				sentenceData09.plabels[i] = token.getFeature(CoNLLFeature.PDEPREL);
				
				i++;
			}
			
			Sentence parsedLTHSentence = new Sentence(sentenceData09, false);
			this.semanticRoleLabeler.parseSentence(parsedLTHSentence);
			
			for(Predicate predicate:parsedLTHSentence.getPredicates()) {
				sentence.getToken(predicate.getIdx()).setFeature(CoNLLFeature.PRED, predicate.getSense());
				
				for(Entry<Word, String> entry:predicate.getArgMap().entrySet()) {
					sentence.getToken(entry.getKey().getIdx()).addSemanticRole(sentence.getToken(predicate.getIdx()), entry.getValue());
				}
			}
		}
	}

}
