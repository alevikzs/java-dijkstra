package org.alevikzs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    private static class Graph {
        protected final Set<Node> nodes = new HashSet<>();
        public void addNode(Node node) {
            nodes.add(node);
        }
    }
    private static class Node {
        protected String name;
        protected int distance = Integer.MAX_VALUE;
        protected List<Node> shortestPath = new LinkedList<>();
        protected Map<Node, Integer> adjacentNodes = new HashMap<>();
        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }
        public Node(String name) {
            this.name = name;
        }
    }
    private static Graph calculateMinPathFromSource(Graph graph, Node source) {
        source.distance = 0;

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getMinDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            currentNode.adjacentNodes.forEach((adjacentNode, edgeWeight) -> {
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            });

            settledNodes.add(currentNode);
        }
        return graph;
    }
    private static Node getMinDistanceNode(Set<Node> unsettledNodes) {
        return unsettledNodes.stream()
                .min(Comparator.comparingInt(o -> o.distance))
                .orElseThrow(IllegalArgumentException::new);
    }
    private static void calculateMinDistance(Node evaluation, int edgeWeigh, Node source) {
        int sourceDistance = source.distance;
        if (sourceDistance + edgeWeigh < evaluation.distance) {
            evaluation.distance = sourceDistance + edgeWeigh;
            LinkedList<Node> shortestPath = new LinkedList<>(source.shortestPath);
            shortestPath.add(source);
            evaluation.shortestPath = shortestPath;
        }
    }
    public static void main(String[] args) {
        Node node1 = new Node("1");
        Node node2 = new Node("2");
        Node node3 = new Node("3");
        Node node4 = new Node("4");
        Node node5 = new Node("5");
        Node node6 = new Node("6");
        node1.addDestination(node2, 3);
        node1.addDestination(node3, 1);
        node1.addDestination(node4, 3);
        node3.addDestination(node2, 4);
        node3.addDestination(node5, 7);
        node3.addDestination(node6, 5);
        node4.addDestination(node6, 2);
        node6.addDestination(node5, 4);
        Graph graph = new Graph();
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);
        graph.addNode(node5);
        graph.addNode(node6);
        calculateMinPathFromSource(graph, node1)
                .nodes
                .forEach(node -> System.out.println(node.name + ": " + node.distance));
    }
}