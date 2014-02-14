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

public class SwedishWikipediaPage extends WikipediaPage {
	/**
	 * Language dependent identifiers of disambiguation, redirection, and stub pages.
	 */
	private static final String IDENTIFIER_DISAMBIGUATION_UPPERCASE_SV = "{{F\u00F6rgrening}}";
	private static final String IDENTIFIER_DISAMBIGUATION_LOWERCASE_SV = "{{f\u00F6rgrening}}";
	private static final String IDENTIFIER_REDIRECTION_UPPERCASE = "#REDIRECT";
	private static final String IDENTIFIER_REDIRECTION_LOWERCASE = "#redirect";
	private static final String IDENTIFIER_REDIRECTION_UPPERCASE_SV = "#OMDIRIGERING";
	private static final String IDENTIFIER_REDIRECTION_LOWERCASE_SV = "#omdirigering";
	private static final String IDENTIFIER_REDIRECTION_CAPITALIZED_SV = "#Omdirigering";
	private static final String IDENTIFIER_STUB_TEMPLATE = "stub}}";
	private static final String IDENTIFIER_STUB_WIKIPEDIA_NAMESPACE = "Wikipedia:Stub";
	
	@Override
	public boolean isDisambiguationPage() {
		return this.isDisambiguationPage;
	}
	
	@Override
	public boolean isRedirectionPage() {
		return this.isRedirectionPage;
	}

	@Override
	public boolean isStubPage() {
		return this.isStubPage;
	}

	@Override
	protected void parsePageType() {
		this.isDisambiguationPage = this.wikiText.contains(IDENTIFIER_DISAMBIGUATION_UPPERCASE_SV) || this.wikiText.contains(IDENTIFIER_DISAMBIGUATION_LOWERCASE_SV);
		this.isRedirectionPage = this.wikiText.contains(IDENTIFIER_REDIRECTION_UPPERCASE) || 
								 this.wikiText.contains(IDENTIFIER_REDIRECTION_LOWERCASE) ||
								 this.wikiText.contains(IDENTIFIER_REDIRECTION_UPPERCASE_SV) ||
								 this.wikiText.contains(IDENTIFIER_REDIRECTION_LOWERCASE_SV) ||
								 this.wikiText.contains(IDENTIFIER_REDIRECTION_CAPITALIZED_SV);
		this.isStubPage = this.wikiText.contains(IDENTIFIER_STUB_TEMPLATE) || this.wikiText.contains(IDENTIFIER_STUB_WIKIPEDIA_NAMESPACE);
	}
}
