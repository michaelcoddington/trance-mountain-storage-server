/*
 * Copyright (C) 2019 melliFORAY contributors (https://github.com/orgs/melliforay/teams/melliforay-contributors)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.melliforay.storageservice.repository.internal.adapter.metadata.support.mongo.converter

import org.bson.Document
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.melliforay.storageservice.RevisionNumber
import org.melliforay.storageservice.repository.BinaryReference
import org.melliforay.storageservice.repository.NodeReference
import org.melliforay.storageservice.repository.NodeRepresentation
import java.util.Date
import org.mockito.Mockito.`when` as mockwhen

@DisplayName("a node representation read converter")
class NodeRepresentationReadConverterTest {

    private val converter = NodeRepresentationReadConverter()

    private val testDate = Date()

    private fun constructMockDocument(): Document {
        val d = Document()
        d["name"] = "node name"
        d["path"] = "/node/path"
        d["revision"] = "3fe"

        val props = Document()
        d["properties"] = props
        props["intProp"] = 2
        props["intListProp"] = listOf(1, 2, 3)
        props["longProp"] = Long.MAX_VALUE
        props["longListProp"] = listOf(Long.MAX_VALUE, Long.MAX_VALUE)
        props["stringProp"] = "stringval"
        props["stringListProp"] = listOf("a", "b", "c")
        props["doubleProp"] = Double.MAX_VALUE
        props["doubleListProp"] = listOf(Double.MAX_VALUE, Double.MAX_VALUE)
        props["booleanProp"] = false
        props["dateProp"] = testDate

        val binaryRefDoc = Document()
        props["binaryRefProp"] = binaryRefDoc
        binaryRefDoc["type"] = "BinaryReference"
        binaryRefDoc["path"] = "/some/binary/path"

        val nodeRefLatestDoc = Document()
        props["nodeRefLatestProp"] = nodeRefLatestDoc
        nodeRefLatestDoc["type"] = "NodeReference"
        nodeRefLatestDoc["path"] = "/some/node"
        nodeRefLatestDoc["snapshot"] = null

        val nodeRefSnapshotDoc = Document()
        props["nodeRefSnapshotProp"] = nodeRefSnapshotDoc
        nodeRefSnapshotDoc["type"] = "NodeReference"
        nodeRefSnapshotDoc["path"] = "/some/node"
        nodeRefSnapshotDoc["snapshot"] = "ab4ea"

        return d
    }

    private fun getRepresentation(): NodeRepresentation? = converter.convert(constructMockDocument())

    @Test
    @DisplayName("should read a representation's name")
    fun readName() {
        val repr = getRepresentation()
        assertEquals("node name", repr!!.name)
    }

    @Test
    @DisplayName("should read a representation's path")
    fun readPath() {
        val repr = getRepresentation()
        assertEquals("/node/path", repr!!.path)
    }

    @Test
    @DisplayName("should read a representation's name")
    fun readRevision() {
        val repr = getRepresentation()
        assertEquals(RevisionNumber("3fe"), repr!!.revision)
    }

    @Test
    @DisplayName("should read a representation's int property")
    fun readIntProp() {
        val repr = getRepresentation()
        assertEquals(2, repr!!.properties["intProp"])
    }

    @Test
    @DisplayName("should read a representation's int list property")
    fun readIntListProp() {
        val repr = getRepresentation()
        assertEquals(listOf(1, 2, 3), repr!!.properties["intListProp"])
    }

    @Test
    @DisplayName("should read a representation's long property")
    fun readLongProp() {
        val repr = getRepresentation()
        assertEquals(Long.MAX_VALUE, repr!!.properties["longProp"])
    }

    @Test
    @DisplayName("should read a representation's long list property")
    fun readLongListProp() {
        val repr = getRepresentation()
        assertEquals(listOf(Long.MAX_VALUE, Long.MAX_VALUE), repr!!.properties["longListProp"])
    }

    @Test
    @DisplayName("should read a representation's string property")
    fun readStringProp() {
        val repr = getRepresentation()
        assertEquals("stringval", repr!!.properties["stringProp"])
    }

    @Test
    @DisplayName("should read a representation's string list property")
    fun readStringListProp() {
        val repr = getRepresentation()
        assertEquals(listOf("a", "b", "c"), repr!!.properties["stringListProp"])
    }

    @Test
    @DisplayName("should read a representation's double property")
    fun readDoubleProp() {
        val repr = getRepresentation()
        assertEquals(Double.MAX_VALUE, repr!!.properties["doubleProp"])
    }

    @Test
    @DisplayName("should read a representation's double list property")
    fun readDoubleListProp() {
        val repr = getRepresentation()
        assertEquals(listOf(Double.MAX_VALUE, Double.MAX_VALUE), repr!!.properties["doubleListProp"])
    }

    @Test
    @DisplayName("should read a representation's date property")
    fun readDateProp() {
        val repr = getRepresentation()
        assertEquals(testDate, repr!!.properties["dateProp"])
    }

    @Test
    @DisplayName("should read a representation's boolean property")
    fun readBooleanProp() {
        val repr = getRepresentation()
        assertEquals(false, repr!!.properties["booleanProp"])
    }

    @Test
    @DisplayName("should read a representation's binary reference property")
    fun readBinaryRefProp() {
        val repr = getRepresentation()
        assertEquals(BinaryReference("/some/binary/path"), repr!!.properties["binaryRefProp"])
    }

    @Test
    @DisplayName("should read a representation's node reference follow-latest property")
    fun reaNodeRefLatestProp() {
        val repr = getRepresentation()
        assertEquals(NodeReference("/some/node", null), repr!!.properties["nodeRefLatestProp"])
    }

    @Test
    @DisplayName("should read a representation's node reference follow-snapshot property")
    fun reaNodeRefSnapshotProp() {
        val repr = getRepresentation()
        assertEquals(NodeReference("/some/node", null), repr!!.properties["nodeRefSnapshotProp"])
    }

}