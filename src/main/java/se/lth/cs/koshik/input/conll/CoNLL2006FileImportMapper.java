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

package se.lth.cs.koshik.input.conll;

import java.io.IOException;

import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.avro.AvroDocument;

public class CoNLL2006FileImportMapper extends Mapper<Text, Text, AvroKey<AvroDocument>, NullWritable> {
	private AvroKey<AvroDocument> avroKey;
	private CoNLL2006Reader coNLL2006Reader;
	
	@Override
	protected void setup(Context context) {
		avroKey = new AvroKey<AvroDocument>(null);
		coNLL2006Reader = new CoNLL2006Reader();
	}
	
	@Override
	protected void map(Text fileName, Text content, Context context)
			throws IOException, InterruptedException {
		Document document = new Document();
		document.setIdentifier(fileName.toString());
		document.setTitle(fileName.toString());
		document.setSource(fileName.toString());
		
		coNLL2006Reader.read(content.toString(), document);
		
		avroKey.datum(document.getAvroDocument());
		context.write(avroKey, NullWritable.get());
	}
}
