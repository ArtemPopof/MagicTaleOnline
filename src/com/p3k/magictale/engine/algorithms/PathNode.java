package com.p3k.magictale.engine.algorithms;

import java.awt.*;

/**
 * Path node for A* algorithm
 *
 * Created by artem96 on 12.12.16.
 */
public class PathNode {

    // Point coords on the map.
    public Point position;

    // Path length from start
    public int pathLengthFromStart;

    // Node from where we come here
    public PathNode cameFrom;

    // estimate length to goal
    public int heuristicEstimatePathLength;

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getPathLengthFromStart() {
        return pathLengthFromStart;
    }

    public void setPathLengthFromStart(int pathLengthFromStart) {
        this.pathLengthFromStart = pathLengthFromStart;
    }

    public PathNode getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(PathNode cameFrom) {
        this.cameFrom = cameFrom;
    }

    public int getHeuristicEstimatePathLength() {
        return heuristicEstimatePathLength;
    }

    public void setHeuristicEstimatePathLength(int heuristicEstimatePathLength) {
        this.heuristicEstimatePathLength = heuristicEstimatePathLength;
    }

    public int getEstimateFullPathLength() {
        return pathLengthFromStart + heuristicEstimatePathLength;
    }


}
