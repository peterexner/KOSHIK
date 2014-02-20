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

package se.lth.cs.koshik.io.hadoop;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.*;


public class WholeTextFileInputFormat extends FileInputFormat<Text, Text> {
	private static Charset charset = Charset.forName("UTF-8");

	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		return false;
	}

	@Override
	public RecordReader<Text, Text> createRecordReader(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
		WholeTextFileRecordReader wholeTextFileRecordReader = new WholeTextFileRecordReader(charset);
		wholeTextFileRecordReader.initialize(genericSplit, context);
		return wholeTextFileRecordReader;
	}

	public static Charset getCharset() {
		return charset;
	}

	public static void setCharset(Charset charset) {
		WholeTextFileInputFormat.charset = charset;
	}
	
	public static class WholeTextFileRecordReader extends RecordReader<Text, Text> {
		private Charset charset;
		private FileSplit fileSplit;
		private Configuration conf;
		private Text key = new Text();
		private Text value = new Text();
		private boolean processed = false;

		public WholeTextFileRecordReader(Charset charset) {
			this.charset = charset;
		}

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
			this.fileSplit = (FileSplit) split;
			this.conf = context.getConfiguration();
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			if (!processed) {
				byte[] contents = new byte[(int) fileSplit.getLength()];
				Path file = fileSplit.getPath();
				key.set(file.getName());
				
				FileSystem fs = file.getFileSystem(conf);
				FSDataInputStream in = null;
				try {
					in = fs.open(file);
					IOUtils.readFully(in, contents, 0, contents.length);
					value.set(new String(contents, charset));
				} finally {
					IOUtils.closeStream(in);
				}
				
				processed = true;
				return true;
			}
			return false;
		}

		@Override
		public Text getCurrentKey() throws IOException, InterruptedException {
			return key;
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return value;
		}

		@Override
		public float getProgress() throws IOException {
			return processed ? 1.0f : 0.0f;
		}

		@Override
		public void close() throws IOException {
		}
	}
}
