package com.p3k.magictale.engine.algorithms;

import java.awt.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Find path algorithm A* realisation
 *
 * Created by artem96 on 12.12.16.
 */
public class AStarFindAlgorithm {

    public static ArrayList<Point> findPath(
            int[][] field, int fieldWidth, int fieldHeight,
            Point start, Point goal) {

        // step 1. Initialization

        // viewed nodes
        ArrayList<PathNode> closedSet = new ArrayList<>();

        // fresed unvieved nodes
        ArrayList<PathNode> openSet = new ArrayList<>();

        // step 2. Add start point and start algo loop

        PathNode startNode = new PathNode();
        startNode.setPosition(start);
        startNode.setCameFrom(null);
        startNode.setPathLengthFromStart(0);
        startNode.setHeuristicEstimatePathLength(
                getHeuristicPathLength(start, goal)
        );
        openSet.add(startNode);

        while (openSet.size() > 0) {

            // step 3. select nearest node
            PathNode currentNode = orderByFullEstimateLength(openSet).get(0);

            // step 4.
            if (currentNode.getPosition().equals(goal))
                return getPathForNode(currentNode);

            // step 5.
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            // step 6.
            ArrayList<PathNode> neighbours = getNeighbours(
                    currentNode, goal, field, fieldWidth, fieldHeight);

            for (PathNode neighbourNode : neighbours) {

                // step 7. if this node alredy viewed, go next
                if (countOfEqualNodes(closedSet, neighbourNode) > 0)
                    continue;

                PathNode openNode = findFirstWithEqualPosition(openSet, neighbourNode);

                // step 8. if there are no such node in open
                // list, then add it
                if (openNode == null) {
                    openSet.add(neighbourNode);
                } else if (openNode.getPathLengthFromStart() >
                        neighbourNode.pathLengthFromStart) {
                    // this node is nearest so override it in list
                    openNode.setCameFrom(currentNode);
                    openNode.setPathLengthFromStart(neighbourNode.getPathLengthFromStart());
                }


            }

        }

        // there are no path to goal
        return null;
    }

    // estimate length from one point to another
    private static int getHeuristicPathLength(Point from, Point to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    /**
     * Sort array by estimate full length parameter
     * @param list
     * @return
     */
    private static ArrayList<PathNode> orderByFullEstimateLength(ArrayList<PathNode> list) {

        // bubble sort =( TODO maybe something more powerfull

        boolean swapped = true;
        int j = 0;
        PathNode temp;

        int firstLength;
        int secondLength;

        PathNode firstNode;
        PathNode secondNode;

        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < list.size()-1; i++) {

                firstNode = list.get(i);
                secondNode = list.get(i+1);

                firstLength = firstNode.getEstimateFullPathLength();
                secondLength = secondNode.getEstimateFullPathLength();

                if (firstLength > secondLength) {
                    temp = firstNode;
                    list.set(i, secondNode);
                    list.set(i+1, temp);
                    swapped = true;
                }

            }
        }

        return list;

    }

    /**
     * Reverse ArrayList
     *
     */
    private static ArrayList<Point> reverse(ArrayList<Point> list) {

        ArrayList<Point> result = new ArrayList<>();

        for (int i = list.size()-1; i >= 0; i--) {
            result.add(list.get(i));
        }

        return result;
    }

    /**
     * return path to given node
     */

    private static ArrayList<Point> getPathForNode(PathNode node) {
        ArrayList<Point> result = new ArrayList<>();

        PathNode currentNode = node;

        while (currentNode != null) {
            result.add(currentNode.getPosition());
            currentNode = currentNode.cameFrom;
        }

        return reverse(result);
    }

    private static ArrayList<PathNode> getNeighbours(PathNode node,
                                                     Point goal, int[][] field,
                                                     int fieldWidth, int fieldHeight) {

        ArrayList<PathNode> result = new ArrayList<>();

        //init all neighbours
        Point[] neighboursPoints = new Point[4];

        //right neighbour
        neighboursPoints[0] = new Point(
                node.getPosition().x + 1,
                node.getPosition().y);

        //left neighbour
        neighboursPoints[1] = new Point(
                node.getPosition().x - 1,
                node.getPosition().y);

        //upper neighbour
        neighboursPoints[2] = new Point(
                node.getPosition().x,
                node.getPosition().y + 1);

        //down neighbour
        neighboursPoints[3] = new Point(
                node.getPosition().x,
                node.getPosition().y - 1);

        for (Point point : neighboursPoints) {

            // check for map boundaries
            if (point.x < 0 || point.x >= fieldWidth)
                continue;
            if (point.y < 0 || point.y >= fieldHeight)
                continue;

            // check if we can move in to this point
            if ((field[point.x][point.y] == -1)) {
                // can't move here
                continue;
            }

            PathNode neighbourNode = new PathNode();
            neighbourNode.setPosition(point);
            neighbourNode.setCameFrom(node);
            neighbourNode.setPathLengthFromStart(
                    node.getPathLengthFromStart() + getDistanceBetweenNeighbours());
            neighbourNode.setHeuristicEstimatePathLength(
                    getHeuristicPathLength(point, goal)
            );

            result.add(neighbourNode);
        }

        return result;
    }

    /**
     * return distance from one cell to another
     *
     * @return distance from one cell to another
     */
    private static int getDistanceBetweenNeighbours() {
        return 1;
    }

    /**
     * return count of nodes with equal position
     * other attributes may be different
     *
     * @return count of object in array, with
     * equal position attribute
     */
    private static int countOfEqualNodes(ArrayList<PathNode> list, PathNode node) {

        int result = 0;

        for (PathNode nextNode : list) {
            if (node.getPosition().equals(nextNode.getPosition())) {
                result++;
            }
        }

        return result;
    }

    /**
     * returns first object that has
     * position equals to the given node's position
     *
     */

    private static PathNode findFirstWithEqualPosition(ArrayList<PathNode> list, PathNode node) {

        for (PathNode nextNode : list) {
            if (node.getPosition().equals(nextNode.getPosition())) {
                return nextNode;
            }
        }

        return null;
    }
}
