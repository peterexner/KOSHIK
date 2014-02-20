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
import java.util.ArrayList;

import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import se.lth.cs.koshik.input.conll.CoNLLFeature;
import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.avro.AvroDocument;
import se.lth.cs.koshik.model.text.Sentence;
import se.lth.cs.koshik.model.text.Token;

public class DocumentToCoNLL2009 extends Configured implements Tool {
	private static final String OPTION_INPUTPATHS = "input";
	private static final String OPTION_OUTPUTPATH = "output";


	private static final Logger LOGGER = Logger.getLogger(DocumentToCoNLL2009.class);

	private static class DocumentReducer extends Reducer<AvroKey<AvroDocument>, NullWritable, Text, NullWritable> {
		static final Text out = new Text();
		static final StringBuilder sb = new StringBuilder();
		@Override
		protected void reduce(AvroKey<AvroDocument> avroDocument, Iterable<NullWritable> nothing, Context context) throws IOException, InterruptedException {
			try {
				Document document = new Document(avroDocument.datum());

				sb.setLength(0);
				sb.append(document.getIdentifier());
				sb.append('\n');


				for(Sentence sentence:document.select(Sentence.class)) {
					ArrayList<String> predicateList = new ArrayList<String>();
					for(Token token:sentence.getTokens()) {
						if(token.hasFeature(CoNLLFeature.PRED) && !token.getFeature(CoNLLFeature.PRED).equalsIgnoreCase("") && token.hasFeature(CoNLLFeature.ID)) {
							predicateList.add(token.getFeature(CoNLLFeature.ID));
						}
					}

					for(Token token:sentence.getTokens()) {
						if(token.hasFeature(CoNLLFeature.ID)) {
							sb.append(token.getFeature(CoNLLFeature.ID));
						} else {
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.FORM)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.FORM));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.LEMMA)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.LEMMA));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.PLEMMA)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.PLEMMA));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.POS)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.POS));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.PPOS)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.PPOS));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.FEAT)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.FEAT));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.PFEAT)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.PFEAT));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.HEAD)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.HEAD));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.PHEAD)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.PHEAD));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.DEPREL)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.DEPREL));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.PDEPREL)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.PDEPREL));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.FILLPRED)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.FILLPRED));
						} else if(token.hasFeature(CoNLLFeature.PRED) && !token.getFeature(CoNLLFeature.PRED).equalsIgnoreCase("")) {
							sb.append('\t');
							sb.append('Y');
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.PRED)) {
							sb.append('\t');
							sb.append(token.getFeature(CoNLLFeature.PRED));
						} else {
							sb.append('\t');
							sb.append('_');
						}

						if(token.hasFeature(CoNLLFeature.APRED)) {
							String[] semanticRoles = token.getFeature(CoNLLFeature.APRED).split("[;]");

							for(String predicate:predicateList) {
								for(String semanticRole:semanticRoles) {
									String[] semanticRoleParts = semanticRole.split("[:]");
									if(semanticRoleParts.length == 2) {
										if(semanticRoleParts[0].trim().equalsIgnoreCase(predicate.trim())) {
											sb.append('\t');
											sb.append(semanticRoleParts[1]);	
										} else {
											sb.append('\t');
											sb.append('_');
										}
									} else {
										sb.append('\t');
										sb.append('_');
									}
								}
							}
						} else {
							sb.append(StringUtils.repeat("\t_", predicateList.size()));
						}

						sb.append('\n');
					}

					sb.append('\n');
				}

				if(sb.length() > 0) {
					out.set(sb.toString());
					context.write(out, NullWritable.get());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		AvroJob.setInputKeySchema(job, AvroDocument.SCHEMA$);
		AvroJob.setMapOutputKeySchema(job, AvroDocument.SCHEMA$);
		job.setMapOutputValueClass(NullWritable.class);

		job.setReducerClass(DocumentReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DocumentToCoNLL2009(), args);
		System.exit(exitCode);
	}
}
