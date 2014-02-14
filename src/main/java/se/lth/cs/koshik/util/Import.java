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

package se.lth.cs.koshik.util;

import java.nio.charset.Charset;

import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.apache.mahout.text.wikipedia.XmlInputFormat;
import se.lth.cs.koshik.input.TextFileImportMapper;
import se.lth.cs.koshik.input.conll.CoNLL2006FileImportMapper;
import se.lth.cs.koshik.input.conll.CoNLL2009FileImportMapper;
import se.lth.cs.koshik.input.wikipedia.WikipediaImportMapper;
import se.lth.cs.koshik.io.hadoop.WholeTextFileInputFormat;
import se.lth.cs.koshik.model.avro.AvroDocument;

public class Import extends Configured implements Tool {
	public static final String OPTION_INPUTPATHS = "input";
	public static final String OPTION_INPUTFORMAT = "inputformat";
	public static final String OPTION_INPUTLANGUAGE = "language";
	public static final String OPTION_INPUTCHARSET = "charset";
	public static final String OPTION_OUTPUTPATH = "output";

	private static final Logger LOGGER = Logger.getLogger(Import.class);

	@SuppressWarnings("static-access")
	@Override
	public int run(String[] args) throws Exception {
		Options options = new Options();
		options.addOption(OptionBuilder.withArgName("path,...").hasArg().withDescription("input path[s]").create(OPTION_INPUTPATHS));
		options.addOption(OptionBuilder.withArgName("text|conll2006|conll2009|wikipedia").hasArg().withDescription("input format").create(OPTION_INPUTFORMAT));
		options.addOption(OptionBuilder.withArgName("ISO 639-2").hasArg().withDescription("input language").create(OPTION_INPUTLANGUAGE));
		options.addOption(OptionBuilder.withArgName("charset").hasArg().withDescription("input charset").create(OPTION_INPUTCHARSET));
		options.addOption(OptionBuilder.withArgName("path").hasArg().withDescription("output path").create(OPTION_OUTPUTPATH));
		
		
		CommandLine commandLine;
		CommandLineParser commandLineParser = new GnuParser();
		commandLine = commandLineParser.parse(options, args);

		if (!commandLine.hasOption(OPTION_INPUTPATHS) ||
				!commandLine.hasOption(OPTION_INPUTFORMAT) ||
				!commandLine.hasOption(OPTION_INPUTLANGUAGE) ||
				!commandLine.hasOption(OPTION_OUTPUTPATH)) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp(getClass().getName(), options);
			ToolRunner.printGenericCommandUsage(System.out);
			return -1;
		}
		
		
		String inputPaths = commandLine.getOptionValue(OPTION_INPUTPATHS);
		String inputFormat = commandLine.getOptionValue(OPTION_INPUTFORMAT).toLowerCase();
		String language = commandLine.getOptionValue(OPTION_INPUTLANGUAGE).toLowerCase();
		Charset charset = Charset.forName(commandLine.hasOption(OPTION_INPUTCHARSET) ? commandLine.getOptionValue(OPTION_INPUTCHARSET) : "UTF-8");
		Path outputPath = new Path(commandLine.getOptionValue(OPTION_OUTPUTPATH));
		
		if(!(inputFormat.equals("text") ||
				inputFormat.equals("conll2006") ||
				inputFormat.equals("conll2009") ||
				inputFormat.equals("wikipedia"))) {
			System.err.println("Error: " + OPTION_INPUTFORMAT + " must be one of: text, conll2006, conll2009, wikipedia");
			return -1;
		}

		LOGGER.info("Utility name: " + this.getClass().getName());
		LOGGER.info(" - input path: " + inputPaths);
		LOGGER.info(" - input format: " + inputFormat);
		LOGGER.info(" - input charset: " + charset.displayName());
		LOGGER.info(" - input language: " + language);
		LOGGER.info(" - output path: " + outputPath);

		Job job = new Job(getConf(), getClass().getName());
		job.setJarByClass(getClass());

		job.getConfiguration().set(OPTION_INPUTLANGUAGE, language);
		job.getConfiguration().set(OPTION_INPUTCHARSET, charset.toString());
		
		FileInputFormat.setInputPaths(job, inputPaths);
		FileOutputFormat.setOutputPath(job, outputPath);

		if(inputFormat.equals("text")) {
			job.setInputFormatClass(WholeTextFileInputFormat.class);
			WholeTextFileInputFormat.setCharset(charset);

			job.setMapperClass(TextFileImportMapper.class);
		} else if(inputFormat.equals("conll2006")) {
			job.setInputFormatClass(WholeTextFileInputFormat.class);
			WholeTextFileInputFormat.setCharset(charset);

			job.setMapperClass(CoNLL2006FileImportMapper.class);
		} else if(inputFormat.equals("conll2009")) {
			job.setInputFormatClass(WholeTextFileInputFormat.class);
			WholeTextFileInputFormat.setCharset(charset);

			job.setMapperClass(CoNLL2009FileImportMapper.class);
		} else if(inputFormat.equals("wikipedia")) {
			job.getConfiguration().set(XmlInputFormat.START_TAG_KEY, "<page>");
			job.getConfiguration().set(XmlInputFormat.END_TAG_KEY, "</page>");
			job.setInputFormatClass(XmlInputFormat.class);
			
			job.setMapperClass(WikipediaImportMapper.class);
		} else {
			System.err.println("Error: " + OPTION_INPUTFORMAT + ": " + inputFormat + " is not supported!");
			return -1;
		}
		
		AvroJob.setMapOutputKeySchema(job, AvroDocument.SCHEMA$);
		job.setMapOutputValueClass(NullWritable.class);
		
		job.setReducerClass(Reducer.class);
		AvroJob.setOutputKeySchema(job, AvroDocument.SCHEMA$);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputFormatClass(AvroKeyOutputFormat.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Import(), args);
		System.exit(exitCode);
	}
}
