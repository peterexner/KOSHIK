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

package se.lth.cs.koshik.example;

import java.io.IOException;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.avro.AvroDocument;
import se.lth.cs.koshik.model.text.Sentence;
import se.lth.cs.koshik.model.text.Span;

public class AvroStats extends Configured implements Tool {
	private static final String OPTION_INPUTPATHS = "input";
	private static final String OPTION_OUTPUTPATH = "output";


	private static final Logger LOGGER = Logger.getLogger(AvroStats.class);

	private static class AvroStatsMapper extends Mapper<AvroKey<AvroDocument>, NullWritable, AvroKey<AvroDocument>, NullWritable> {
		@Override
		protected void map(AvroKey<AvroDocument> avroDocument, NullWritable nothing, Context context) throws IOException, InterruptedException {
			Document document = new Document(avroDocument.datum());
			context.getCounter("global", "article").increment(1);
			for(Sentence sentence:document.select(Sentence.class)) {
				context.getCounter("global", "sentence").increment(1);
				context.getCounter("global", "token").increment(sentence.getTokens().size());
			}
			context.getCounter("global", "spans").increment(document.select(Span.class).size());
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public int run(String[] args) throws Exception {
		Options options = new Options();
		options.addOption(OptionBuilder.withArgName("path,...").hasArg().withDescription("input path[s]").create(OPTION_INPUTPATHS));
		options.addOption(OptionBuilder.withArgName("path").hasArg().withDescription("output path").create(OPTION_OUTPUTPATH));


		CommandLine commandLine;
		CommandLineParser commandLineParser = new GnuParser();
		commandLine = commandLineParser.parse(options, args);

		if (!commandLine.hasOption(OPTION_INPUTPATHS) ||
				!commandLine.hasOption(OPTION_OUTPUTPATH)) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp(getClass().getName(), options);
			ToolRunner.printGenericCommandUsage(System.out);
			return -1;
		}


		String inputPaths = commandLine.getOptionValue(OPTION_INPUTPATHS);
		Path outputPath = new Path(commandLine.getOptionValue(OPTION_OUTPUTPATH));


		LOGGER.info("Utility name: " + this.getClass().getName());
		LOGGER.info(" - input path: " + inputPaths);
		LOGGER.info(" - output path: " + outputPath);

		Job job = new Job(getConf(), getClass().getName());
		job.setJarByClass(getClass());

		FileInputFormat.setInputPaths(job, inputPaths);
		FileOutputFormat.setOutputPath(job, outputPath);

		job.setInputFormatClass(AvroKeyInputFormat.class);
		job.setMapperClass(AvroStatsMapper.class);
		AvroJob.setInputKeySchema(job, AvroDocument.SCHEMA$);
		
		AvroJob.setMapOutputKeySchema(job, AvroDocument.SCHEMA$);
		job.setMapOutputValueClass(NullWritable.class);

		//job.setReducerClass(AvroStatsMapper.class);
		//AvroJob.setOutputKeySchema(job, Schema.create(Schema.Type.STRING));
		//AvroJob.setOutputValueSchema(job, AvroDocument.SCHEMA$);
		//job.setOutputFormatClass(AvroKeyValueOutputFormat.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new AvroStats(), args);
		System.exit(exitCode);
	}
}
