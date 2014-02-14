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

package se.lth.cs.koshik.analysis.wikipedia;

import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import se.lth.cs.koshik.model.Document;
import se.lth.cs.koshik.model.wikipedia.InternalLink;
import se.lth.cs.koshik.model.wikipedia.Section;

import de.fau.cs.osr.ptk.common.AstVisitor;

public class TextConverter extends AstVisitor<WtNode>
{
	private final WikiConfig config;

	private StringBuilder sb;

	private String currentSectionTitle;

	private boolean filterOutput;
	private boolean expectSectionTitle;
	private boolean hasNotReadAbstract;

	/**
	 * Becomes true if we are no long at the Beginning Of the whole Document.
	 */
	private Document document;
	
	// =========================================================================

	public TextConverter(WikiConfig config, Document document)
	{
		this.config = config;
		this.document = document;
	}

	@Override
	protected boolean before(WtNode node)
	{
		// This method is called by go() before visitation starts
		sb = new StringBuilder();
		currentSectionTitle = "";
		expectSectionTitle = false;
		hasNotReadAbstract = true;
		return super.before(node);
	}

	@Override
	protected Object after(WtNode node, Object result)
	{
		this.document.setContent(sb.toString());
		return sb.toString();
	}

	// =========================================================================

	public void visit(WtNode n)
	{
	}

	public void visit(WtNodeList n)
	{
		iterate(n);
	}

	public void visit(WtUnorderedList e)
	{
		iterate(e);
	}

	public void visit(WtOrderedList e)
	{
		iterate(e);
	}

	public void visit(WtListItem item)
	{
		iterate(item);
	}

	public void visit(EngPage p)
	{
		iterate(p);
	}

	public void visit(WtText text)
	{
		if(expectSectionTitle) {
			if(!text.getContent().trim().equalsIgnoreCase("")) {
				currentSectionTitle = text.getContent().trim();
			}
			expectSectionTitle = false;
		}

		if(!isInsideFilteredSection() && !text.getContent().replaceAll("[\n\r]", "").equalsIgnoreCase("")) { 
			sb.append(text.getContent());
		}
	}

	public void visit(WtWhitespace w)
	{
		if(!isInsideFilteredSection()) {
			sb.append(" ");
		}
	}

	public void visit(WtBold b)
	{
		iterate(b);
	}

	public void visit(WtItalics i)
	{
		iterate(i);
	}

	public void visit(WtXmlCharRef cr)
	{
		if(!isInsideFilteredSection()) {
			sb.append(Character.toChars(cr.getCodePoint()));
		}
	}

	public void visit(WtXmlEntityRef er)
	{
		String ch = er.getResolved();
		if (ch == null)
		{
			if(!isInsideFilteredSection()) {
				sb.append('&' + er.getName() + ';');
			}
		}
		else
		{
			if(!isInsideFilteredSection()) {
				sb.append(ch);
			}
		}

	}

	public void visit(WtUrl wtUrl)
	{
		if (!wtUrl.getProtocol().isEmpty())
		{
			if(!isInsideFilteredSection()) {
				sb.append(wtUrl.getProtocol() + ':');
			}
		}
		if(!isInsideFilteredSection()) {
			sb.append(wtUrl.getPath());
		}
	}

	public void visit(WtExternalLink link)
	{
	}

	public void visit(WtInternalLink link)
	{
		try
		{
			if (link.getTarget().isResolved())
			{
				PageTitle page = PageTitle.make(config, link.getTarget().getAsString());
				if (page.getNamespace().equals(config.getNamespace("Category")))
					return;
			}
		}
		catch (LinkTargetException e)
		{
		}

		InternalLink internalLink = new InternalLink(document);
		internalLink.setBegin(sb.length());
		internalLink.setTarget(link.getTarget().getAsString());
		
		if(!isInsideFilteredSection()) {
			sb.append(link.getPrefix());
		}
		
		if (!link.hasTitle())
		{
			iterate(link.getTarget());
		}
		else
		{
			iterate(link.getTitle());
		}

		if(!isInsideFilteredSection()) {
			sb.append(link.getPostfix());
		}
		
		internalLink.setEnd(sb.length());
	}

	public void visit(WtSection s)
	{
		if(hasNotReadAbstract && sb.length() > 0) {
			Section section = new Section(document);
			section.setTitle("Abstract");
			section.setBegin(0);
			section.setEnd(sb.length());
			hasNotReadAbstract = false;
		}
		
		Section section = new Section(document);
		section.setBegin(sb.length());
		
		filterOutput = true;
		expectSectionTitle = true;
		iterate(s.getHeading());
		section.setTitle(currentSectionTitle);
		expectSectionTitle = false;
		filterOutput = false;

		if(!isInsideFilteredSection()) {
			if(sb.length() > 0) {
				if(sb.charAt(sb.length() -1) != '\n') {
					sb.append("\n\n");
				}
			}
		}
		
		iterate(s.getBody());
		
		section.setEnd(sb.length());
	}

	public void visit(WtParagraph p)
	{
		iterate(p);
		if(!isInsideFilteredSection()) {
			if(sb.length() > 0) {
				if(sb.charAt(sb.length() -1) != '\n') {
					sb.append("\n\n");
				}
			}
		}
	}

	public void visit(WtHorizontalRule hr)
	{
	}

	public void visit(WtXmlElement e)
	{
		if (e.getName().equalsIgnoreCase("br"))
		{
		}
		else
		{
			iterate(e.getBody());
		}
	}

	// =========================================================================
	// Stuff we want to hide

	public void visit(WtImageLink n)
	{
	}

	public void visit(WtIllegalCodePoint n)
	{
	}

	public void visit(WtXmlComment n)
	{
	}

	public void visit(WtTemplate n)
	{
		if(!isInsideFilteredSection()) {
			if(n.toString().equalsIgnoreCase("WtTemplate([0] = WtName[WtText(\"spaced ndash\")], [1] = WtTemplateArguments[])")) {
				sb.append(" - ");
			}
		}
	}

	public void visit(WtTemplateArgument n)
	{
	}

	public void visit(WtTemplateParameter n)
	{
	}

	public void visit(WtTagExtension n)
	{
	}

	public void visit(WtPageSwitch n)
	{
	}

	// =========================================================================

	private boolean isInsideFilteredSection() {
		if(filterOutput) {
			return true;
		}

		if(this.currentSectionTitle.equalsIgnoreCase("See also") ||
				this.currentSectionTitle.equalsIgnoreCase("Notes") ||
				this.currentSectionTitle.equalsIgnoreCase("Writings") ||
				this.currentSectionTitle.equalsIgnoreCase("References") ||
				this.currentSectionTitle.equalsIgnoreCase("Publications") ||
				this.currentSectionTitle.equalsIgnoreCase("Bibliography") ||
				this.currentSectionTitle.equalsIgnoreCase("Further reading") ||
				this.currentSectionTitle.equalsIgnoreCase("External links") ||
				this.currentSectionTitle.equalsIgnoreCase("Se även") ||
				this.currentSectionTitle.equalsIgnoreCase("Källor") ||
				this.currentSectionTitle.equalsIgnoreCase("Externa länkar") ||
				this.currentSectionTitle.equalsIgnoreCase("Referenser") ||
				this.currentSectionTitle.equalsIgnoreCase("Biografi") ||
				this.currentSectionTitle.equalsIgnoreCase("Litteratur") ||
				this.currentSectionTitle.equalsIgnoreCase("参见") ||
				this.currentSectionTitle.equalsIgnoreCase("相关条目") ||
				this.currentSectionTitle.equalsIgnoreCase("注释") ||
				this.currentSectionTitle.equalsIgnoreCase("参考资料") ||
				this.currentSectionTitle.equalsIgnoreCase("外部链接") ||
				this.currentSectionTitle.equalsIgnoreCase("参看")) {
			return true;
		} else {
			return false;
		}

	}
}
