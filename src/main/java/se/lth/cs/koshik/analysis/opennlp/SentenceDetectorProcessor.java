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

package se.lth.cs.koshik.analysis.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.text.Sentence;



public class SentenceDetectorProcessor implements ContentProcessor {
	private SentenceDetectorME sentenceDetector;

	public SentenceDetectorProcessor(String modelFileName) throws IOException {
		InputStream modelIn = null;

		modelIn = new FileInputStream("./model.zip/model/opennlp/" + modelFileName);

		SentenceModel model = new SentenceModel(modelIn);
		sentenceDetector = new SentenceDetectorME(model);
		
		modelIn.close();
	}

	@Override
	public void process(Document document) throws Exception {
		String[] sentences;

		if(document.getContent() != null) {
			sentences = sentenceDetector.sentDetect(document.getContent());

			int offset = 0;

			for(String sentence:sentences) {
				int begin = document.getContent().indexOf(sentence, offset);
				if(begin != -1) {
					Sentence koshikSentence = new Sentence(document);
					koshikSentence.setBegin(begin);
					offset = begin + sentence.length();
					koshikSentence.setEnd(offset);
				}
			}
		}
	}
}
