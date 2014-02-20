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

package se.lth.cs.koshik.input.wikipedia.language;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import se.lth.cs.koshik.analysis.wikipedia.TextConverter;
import se.lth.cs.koshik.model.Document;


public abstract class WikipediaPage {
	protected Integer namespace;
	protected Long id;
	protected Long revision;
	protected String title;
	protected String wikiText;
	protected boolean isDisambiguationPage;
	protected boolean isRedirectionPage;
	protected boolean isStubPage;

	public abstract boolean isDisambiguationPage();

	public abstract boolean isRedirectionPage();

	public abstract boolean isStubPage();
	
	protected abstract void parsePageType();
	
	public boolean isArticle() {
		return (this.namespace == 0) && !this.isDisambiguationPage && !this.isRedirectionPage && !this.isStubPage;
	}
	
	public void readFromXMLPage(String xmlPage, String charset) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder(); 
			org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream(xmlPage.getBytes(charset)));
			doc.getDocumentElement().normalize();

			this.namespace = Integer.parseInt(doc.getElementsByTagName("ns").item(0).getTextContent());

			NodeList revisionNodeList = doc.getElementsByTagName("revision");
			if(revisionNodeList.getLength() == 1) {
				this.wikiText = ((Element) revisionNodeList.item(0)).getElementsByTagName("text").item(0).getTextContent();
				this.id = Long.parseLong(doc.getElementsByTagName("id").item(0).getTextContent());
				this.title = doc.getElementsByTagName("title").item(0).getTextContent();
				this.revision = Long.parseLong(((Element) revisionNodeList.item(0)).getElementsByTagName("id").item(0).getTextContent());
				parsePageType();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Document getDocument() {
		Document document = new Document();
		document.setIdentifier(this.id.toString());
		document.setRevision(this.revision.toString());
		document.setTitle(this.title);
		
		try {
			WikiConfig config = DefaultConfigEnWp.generate();
			WtEngineImpl engine = new WtEngineImpl(config);
			
			PageTitle pageTitle = PageTitle.make(config, this.title);
			PageId pageId = new PageId(pageTitle, this.revision);
			
			EngProcessedPage cp = engine.postprocess(pageId, this.wikiText, null);
			TextConverter p = new TextConverter(config, document);
			p.go(cp.getPage());
		} catch (Exception e) {
			document = null;
			e.printStackTrace();
		}
		
		return document;
	}
}
