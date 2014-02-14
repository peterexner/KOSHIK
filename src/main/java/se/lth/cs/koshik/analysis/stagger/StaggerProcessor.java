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

package se.lth.cs.koshik.analysis.stagger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;
import se.su.ling.stagger.TaggedToken;
import se.su.ling.stagger.Tagger;
import se.su.ling.stagger.Token;
import se.su.ling.stagger.Tokenizer;
import se.su.ling.stagger.SwedishTokenizer;



public class StaggerProcessor implements ContentProcessor {
	Tagger tagger;
	
	public StaggerProcessor() {
		try {
			ObjectInputStream modelReader = new ObjectInputStream(new FileInputStream("./model.zip/model/sv/swedish.bin"));
			tagger = (Tagger) modelReader.readObject();
			modelReader.close();
			tagger.setExtendLexicon(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void process(Document document) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(document.getContent().getBytes("UTF-8"))));
		
		Tokenizer tokenizer = new SwedishTokenizer(reader);
		
		ArrayList<Token> sentence;
		int sentIdx = 0;
		
		while((sentence=tokenizer.readSentence())!=null) {
			TaggedToken[] sent = new TaggedToken[sentence.size()];
			
			for(int j=0; j<sentence.size(); j++) {
				Token tok = sentence.get(j);
				String id;
				id = sentIdx + ":" + tok.offset;
				sent[j] = new TaggedToken(tok, id);
			}
			TaggedToken[] taggedSent = tagger.tagSentence(sent, true, false);
			
			if(taggedSent.length > 0) {
				int lastIndex = taggedSent.length - 1;
				int begin = taggedSent[0].token.offset;
				int end = taggedSent[lastIndex].token.offset + taggedSent[lastIndex].token.value.length();
				
				Sentence koshikSentence = new Sentence(document);
				koshikSentence.setBegin(begin);
				koshikSentence.setEnd(end);
				
			}
			
			int tokenIdx = 1;
			for(TaggedToken token:taggedSent) {
				String lemma = token.lf;
				String form = token.token.value;
				String pos = tagger.getTaggedData().getPosTagSet().getTagName(token.posTag);
				int begin = token.token.offset;
				int end = token.token.offset + token.token.value.length();
				
				se.lth.cs.koshik.model.text.Token koshikToken = new se.lth.cs.koshik.model.text.Token(document);
				koshikToken.setFeature(CoNLLFeature.ID, String.valueOf(tokenIdx));
				koshikToken.setFeature(CoNLLFeature.FORM, token.token.value);
				koshikToken.setFeature(CoNLLFeature.LEMMA, token.lf);
				
				String posTag = tagger.getTaggedData().getPosTagSet().getTagName(token.posTag);
				String[] posTagParts = posTag.split("\\|");
				String predictedPosTag;
				String predictedFeats;
				
				if(posTagParts.length > 1) {
					predictedPosTag = posTagParts[0];
					predictedFeats = posTag.substring(predictedPosTag.length() + 1) ;
				} else {
					predictedPosTag = posTagParts[0];
					predictedFeats = posTagParts[0];
				}
				
				koshikToken.setFeature(CoNLLFeature.PPOS, predictedPosTag);
				koshikToken.setFeature(CoNLLFeature.PFEAT, predictedFeats);
				
				koshikToken.setBegin(begin);
				koshikToken.setEnd(end);
				
				tokenIdx++;
			}
			
			sentIdx++;
		}
		tokenizer.yyclose();
		
		
	}
}
