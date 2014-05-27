/**
 * Copyright (C) 2013 Orbeon, Inc.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
 */
package org.orbeon.oxf.fr.persistence.db

import org.orbeon.oxf.resources.URLFactory
import java.io.{StringWriter, InputStreamReader}
import org.orbeon.oxf.util.ScalaUtils._
import scala.collection.mutable.ArrayBuffer

private[persistence] object SQL {

    private val Base = "oxf:/apps/fr/persistence/relational/ddl/"

    // Reads a sequence semicolon-separated of statements from a text file
    def read(file: String): Seq[String] = {

        // Read file content
        val fileContentAsString = {
            val url = Base ++ file
            val inputStream = URLFactory.createURL(url).openStream()
            val reader = new InputStreamReader(inputStream)
            val writer = new StringWriter
            copyReader(reader, writer)
            writer.toString
        }

        // Group each line with its next line
        val linesWithNext = {
            val lines = fileContentAsString.split("\n").toList
            val next = lines.tail.map(Some(_)) :+ None
            lines.zip(next)
        }

        // Mark lines that are end-of-statement
        val linesWithMarkers = linesWithNext.map{case (line, next) ⇒ (line,
            // To be the end of a statement, we must end with a `;`
            line.endsWith(";") &&
            // But also we don't start with space, or we're at the end of the file, or the next line is empty
            (! line.startsWith(" ") || next.map(_.length == 0).getOrElse(true))
        )}

        // Group lines in statements
        var allStatements    = ArrayBuffer[String]()
        var currentStatement = ArrayBuffer[String]()
        linesWithMarkers.foreach { case (line, isLastLineOfStatement) ⇒
            // Remove the `;` separator if this is the last line
            val partToKeep = if (isLastLineOfStatement) line.substring(0, line.length - 1) else line
            currentStatement += partToKeep
            if (isLastLineOfStatement) {
                allStatements += currentStatement.mkString("\n")
                currentStatement.clear()
            }
        }
        allStatements.toList
    }

}
