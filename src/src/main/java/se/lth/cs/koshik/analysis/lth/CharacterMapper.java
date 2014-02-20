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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

import se.lth.cs.koshik.analysis.ContentProcessor;
import se.lth.cs.koshik.model.Document;



public class CharacterMapper implements ContentProcessor {
	private final String[] searchList;
	private final String[] replacementList;

	public CharacterMapper(String dictionaryFileName) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("./model.zip/model/lth/" + dictionaryFileName), Charset.forName("UTF-8")));
		String line;
		ArrayList<String> from = new ArrayList<String>();
		ArrayList<String> to = new ArrayList<String>();

		while ((line = in.readLine()) != null) {
			String[] lineParts = line.split("\\t");
			if(lineParts.length == 2) {
				from.add(lineParts[0]);
				to.add(lineParts[1]);
			}
		}
		
		in.close();
		
		this.searchList = from.toArray(new String[0]);
		this.replacementList = to.toArray(new String[0]);
	}

	@Override
	public void process(Document document) throws Exception {
		if(document.getContent() != null) { 
			document.setContent(StringUtils.replaceEach(document.getContent(), searchList, replacementList));
		}
	}
}
