KOSHIK
======

An NLP framework for large scale processing using Hadoop. KOSHIK supports parsing of text in multiple languages including English, Swedish, and Chinese.

USAGE
=====

Before processing a corpus, the corpus must be imported into Koshik. Koshik supports import from plain text, CoNLL2006/2009, and Wikipedia XML dumps.
To import from a Wikipedia XML dump file, run:

hadoop jar Koshik-1.0.1.jar se.lth.cs.koshik.util.Import -input /enwiki-20140102-pages-articles.xml -inputformat wikipedia -language eng -charset utf-8 -output /enwiki_avro

The imported documents can then be parsed using the analysis tools in Koshik.
To parse using an English semantic role labeler, run:
hadoop jar Koshik-1.0.1.jar se.lth.cs.koshik.util.EnglishPipeline -D mapred.reduce.tasks=12 -D mapred.child.java.opts=-Xmx8G -archives model.zip -input /enwiki_avro -output /enwiki_semantic

Querying data through HIVE
==========================
CREATE EXTERNAL TABLE koshikdocs ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe' STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat' LOCATION '/hivetablekoshik' TBLPROPERTIES('avro.schema.url'='hdfs:///AvroDocument.avsc');
LOAD DATA INPATH '/enwiki_semantic/*.avro' INTO TABLE koshikdocs;

Number of articles:
select count(identifier) from koshikdocs;

Number of sentences:
SELECT count(ann) FROM koshikdocs LATERAL VIEW explode(annotations.layer) annTable as ann WHERE ann LIKE '%Sentence';

Number of tokens:
SELECT count(ann) FROM koshikdocs LATERAL VIEW explode(annotations.layer) annTable as ann WHERE ann LIKE '%Token';

Number of nouns:
SELECT count(key) FROM (SELECT explode(ann) AS (key,value) FROM (SELECT ann FROM koshikdocs LATERAL VIEW explode(annotations.features) annTable as ann) annmap) decmap WHERE key='POSTAG' AND value LIKE 'NN%';

NLP Model files
==========================
The language model files for the tools used in KOSHIK can be downloaded from the following sites:
- https://code.google.com/p/mate-tools/downloads/list
- http://opennlp.sourceforge.net/models-1.5/
- http://nlp.stanford.edu/software/corenlp.shtml

References
==========
Please cite the following paper, if you use KOSHIK:
- Peter Exner, Pierre Nugues, 2014. [*KOSHIK: A Large-scale Distributed Computing Framework for NLP*](http://semantica.cs.lth.se/koshik.pdf). To appear at ICPRAM 2014, Angers, France. [PDF](http://semantica.cs.lth.se/koshik.pdf)