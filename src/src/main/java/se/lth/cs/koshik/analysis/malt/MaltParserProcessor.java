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

package se.lth.cs.koshik.analysis.malt;

import java.util.TreeSet;

import org.maltparser.MaltParserService;

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;
import se.lth.cs.koshik.model.text.Token;



public class MaltParserProcessor implements ContentProcessor {
	MaltParserService service;

	public MaltParserProcessor() {
		try {
			service =  new MaltParserService();
			service.initializeParserModel("-c " + "swemalt-1.7.2.mco" + " -m parse -w " + "./model.zip/model/sv/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(Document document) throws Exception {
		for(Sentence sentence:document.select(Sentence.class)) {
			TreeSet<Token> tokens = sentence.getTokens(); 
			String[] maltTokens = new String[tokens.size()];
			int i = 0;

			for(Token token:tokens) {
				maltTokens[i] = token.getFeature(CoNLLFeature.ID) + "\t" + token.getFeature(CoNLLFeature.FORM) + "\t" + token.getFeature(CoNLLFeature.LEMMA) + "\t" + token.getFeature(CoNLLFeature.PPOS) + "\t" + token.getFeature(CoNLLFeature.PPOS) + "\t" + token.getFeature(CoNLLFeature.PFEAT);
				i++;
			}

			String[] structure = service.parseTokens(maltTokens);
			for(String row:structure) {
				int index = 0;
				index++;

				String[] rowParts = row.split("\t");

				if(rowParts.length == 8) {
					if(!rowParts[6].equalsIgnoreCase("_") && !rowParts[7].equalsIgnoreCase("_")) {
						sentence.getToken(index).setFeature(CoNLLFeature.PHEAD, rowParts[6]);
						sentence.getToken(index).setFeature(CoNLLFeature.PDEPREL, rowParts[7]);
					}
				}
			}
		}


	}
}
