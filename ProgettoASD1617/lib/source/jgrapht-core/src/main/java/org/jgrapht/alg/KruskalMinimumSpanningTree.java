/*
 * (C) Copyright 2010-2017, by Tom Conerly and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.util.*;

/**
 * An implementation of <a href="http://en.wikipedia.org/wiki/Kruskal's_algorithm">Kruskal's minimum
 * spanning tree algorithm</a>. If the given graph is connected it computes the minimum spanning
 * tree, otherwise it computes the minimum spanning forest. The algorithm runs in time O(E log E).
 * This implementation uses the hashCode and equals method of the vertices.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @author Tom Conerly
 * @since Feb 10, 2010
 * @deprecated Use {@link org.jgrapht.alg.spanning.KruskalMinimumSpanningTree} instead.
 */
@Deprecated
public class KruskalMinimumSpanningTree<V, E>
    implements MinimumSpanningTree<V, E>
{
    private double spanningTreeCost;
    private Set<E> edgeList;

    /**
     * Creates and executes a new KruskalMinimumSpanningTree algorithm instance. An instance is only
     * good for a single spanning tree; after construction, it can be accessed to retrieve
     * information about the spanning tree found.
     *
     * @param graph the graph to be searched
     */
    public KruskalMinimumSpanningTree(final Graph<V, E> graph)
    {
        UnionFind<V> forest = new UnionFind<>(graph.vertexSet());
        ArrayList<E> allEdges = new ArrayList<>(graph.edgeSet());
        Collections.sort(
            allEdges, (edge1, edge2) -> Double
                .valueOf(graph.getEdgeWeight(edge1)).compareTo(graph.getEdgeWeight(edge2)));

        spanningTreeCost = 0;
        edgeList = new HashSet<>();

        for (E edge : allEdges) {
            V source = graph.getEdgeSource(edge);
            V target = graph.getEdgeTarget(edge);
            if (forest.find(source).equals(forest.find(target))) {
                continue;
            }

            forest.union(source, target);
            edgeList.add(edge);
            spanningTreeCost += graph.getEdgeWeight(edge);
        }
    }

    @Override
    public Set<E> getMinimumSpanningTreeEdgeSet()
    {
        return edgeList;
    }

    @Override
    public double getMinimumSpanningTreeTotalWeight()
    {
        return spanningTreeCost;
    }

}

// End KruskalMinimumSpanningTree.java
