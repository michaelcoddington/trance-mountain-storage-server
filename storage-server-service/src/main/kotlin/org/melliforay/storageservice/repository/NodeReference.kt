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

package org.melliforay.storageservice.repository

import org.melliforay.storageservice.RevisionNumber

/**
 * A reference that points to another [Node].
 * @param path the path of the node to be referenced
 * @param snapshotRevision the snapshot revision of the node to reference, or null if the reference is to the latest revision of the node
 */
class NodeReference(val path: String, val snapshotRevision: RevisionNumber?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NodeReference) return false

        if (path != other.path) return false
        if (snapshotRevision != other.snapshotRevision) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + (snapshotRevision?.hashCode() ?: 0)
        return result
    }

}