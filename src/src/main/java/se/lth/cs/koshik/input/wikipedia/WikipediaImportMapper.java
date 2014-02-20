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

package se.lth.cs.koshik.input.wikipedia;

import java.io.IOException;

import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import se.lth.cs.koshik.input.wikipedia.language.WikipediaPage;
import se.lth.cs.koshik.input.wikipedia.language.WikipediaPageFactory;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.avro.AvroDocument;
import se.lth.cs.koshik.util.Import;

public class WikipediaImportMapper extends Mapper<LongWritable, Text, AvroKey<AvroDocument>, NullWritable> {
	private AvroKey<AvroDocument> avroKey;
	private WikipediaPage wikipediaPage;
	
	@Override
	protected void setup(Context context) {
		avroKey = new AvroKey<AvroDocument>(null);
		wikipediaPage = WikipediaPageFactory.createWikipediaPage(context.getConfiguration().get(Import.OPTION_INPUTLANGUAGE));
	}

	@Override
	protected void map(LongWritable pos, Text content, Context context)
			throws IOException, InterruptedException {
		wikipediaPage.readFromXMLPage(content.toString(), context.getConfiguration().get(Import.OPTION_INPUTCHARSET));

		if(wikipediaPage.isArticle()) {
			context.getCounter("global", "articles").increment(1);
		}

		if(wikipediaPage.isDisambiguationPage()) {
			context.getCounter("global", "disambiguationPages").increment(1);
		}

		if(wikipediaPage.isRedirectionPage()) {
			context.getCounter("global", "redirectionPages").increment(1);
		}

		if(wikipediaPage.isStubPage()) {
			context.getCounter("global", "stubPages").increment(1);
		}
		
		if(wikipediaPage.isArticle() || wikipediaPage.isStubPage()) {
			Document document = wikipediaPage.getDocument();
			
			if(document != null) {
				String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
				document.setSource(fileName);
				document.setLanguage(context.getConfiguration().get(Import.OPTION_INPUTLANGUAGE));
				document.setEncoding(context.getConfiguration().get(Import.OPTION_INPUTCHARSET));
				
				avroKey.datum(document.getAvroDocument());
				context.write(avroKey, NullWritable.get());
				
				if(wikipediaPage.isArticle()) {
					context.getCounter("global", "articlesProcessed").increment(1);
				}
				
				if(wikipediaPage.isStubPage()) {
					context.getCounter("global", "stubPagesProcessed").increment(1);
				}
			}
		}
	}
}
