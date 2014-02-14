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


public class WikipediaPageFactory {

  /**
   * Returns a {@code WikipediaPage} for this {@code language}.
   */
  public static WikipediaPage createWikipediaPage(String language) {
    if (language == null) {
      return new EnglishWikipediaPage();
    }

    if (language.equalsIgnoreCase("eng")) {
      return new EnglishWikipediaPage();
    } else if (language.equalsIgnoreCase("swe")) {
      return new SwedishWikipediaPage();
    } else {
      return new EnglishWikipediaPage();
    }
  }

  public static Class<? extends WikipediaPage> getWikipediaPageClass(String language) {
    if (language == null) {
      return EnglishWikipediaPage.class;
    }

    if (language.equalsIgnoreCase("eng")) {
      return EnglishWikipediaPage.class;
    } else if (language.equalsIgnoreCase("swe")) {
      return SwedishWikipediaPage.class;
    } else {
      return EnglishWikipediaPage.class;
    }
  }
}
