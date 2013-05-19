/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semanticweb.owlapi.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/** Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 15-Jan-2009
 * <p/>
 * Represents the Node ID for anonymous individuals */
public abstract class NodeID implements Comparable<NodeID>, Serializable {
    private static final long serialVersionUID = 40000L;
    private static final AtomicLong counter = new AtomicLong();
    private static final String NODE_ID_PREFIX = "genid";
    private static final String SHARED_NODE_ID_PREFIX = "genid-nodeid-";
    private static final String PREFIX = "_:";

    /** @param id
     * @return string version of id */
    public static String nodeString(int id) {
        return NodeID.NODE_ID_PREFIX + Integer.toString(id);
    }

    /** Returns an absolute IRI from a nodeID attribute.
     * 
     * @param nodeID
     * @return absolute IRI */
    public static String getIRIFromNodeID(String nodeID) {
        return PREFIX + SHARED_NODE_ID_PREFIX + nodeID.replace("genid", "");
    }

    /** Generates next anonymous IRI.
     * 
     * @return absolute IRI */
    public static String nextAnonymousIRI() {
        return PREFIX + NODE_ID_PREFIX + counter.incrementAndGet();
    }

    /** Tests whether supplied IRI was generated by this parser in order to label
     * an anonymous node.
     * 
     * @param uri
     *            the IRI
     * @return <code>true</code> if the IRI was generated by this parser to
     *         label an anonymous node */
    public static boolean isAnonymousNodeIRI(String uri) {
        return uri != null && (uri.startsWith(PREFIX) || uri.indexOf(NodeID.NODE_ID_PREFIX) != -1);
    }

    /** Tests whether supplied IRI was generated by this parser in order to label
     * an anonymous node.
     * 
     * @param iri
     *            the IRI
     * @return <code>true</code> if the IRI was generated by this parser to
     *         label an anonymous node */
    public static boolean isAnonymousNodeIRI(IRI iri) {
        return iri.getNamespace() != null && iri.getNamespace().contains(NodeID.NODE_ID_PREFIX);
    }

    /** @param iri
     * @return true if the iri is an anonymous label */
    public static boolean isAnonymousNodeID(String iri) {
        return iri != null && iri.indexOf(NodeID.SHARED_NODE_ID_PREFIX) > -1;
    }

    /** Gets the string representation of the node ID. This will begin with _:
     * 
     * @return The string representation of the node ID. */
    public abstract String getID();

    /** Gets a NodeID with a specific identifier string
     * 
     * @param id
     *            The String that identifies the node. If the String doesn't
     *            start with "_:" then this will be concatenated to the front of
     *            the specified id String; if the string is empty or null, an
     *            autogenerated id will be used.
     * @return A NodeID */
    public static NodeID getNodeID(String id) {
        String _id = id == null || id.length() == 0 ? PREFIX + NODE_ID_PREFIX
                + Long.toString(counter.incrementAndGet()) : id;
        return new NodeIDImpl(_id);
    }

    static class NodeIDImpl extends NodeID {
        private static final long serialVersionUID = 40000L;
        private final String id;

        public NodeIDImpl(String id) {
            if (id.startsWith(PREFIX)) {
                this.id = id;
            } else {
                this.id = PREFIX + id;
            }
        }

        @Override
        public String toString() {
            return id;
        }

        @Override
        public int compareTo(NodeID o) {
            return id.compareTo(o.toString());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof NodeID)) {
                return false;
            }
            NodeID other = (NodeID) obj;
            return id.equals(other.getID());
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String getID() {
            return id;
        }
    }
}
