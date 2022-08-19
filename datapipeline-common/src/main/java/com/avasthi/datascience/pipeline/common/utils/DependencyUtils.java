package com.avasthi.datascience.pipeline.common.utils;

import java.util.UUID;

public class DependencyUtils {

    public static class Node {
        private final UUID id;
        public Node(UUID id) {this.id = id; }
        @Override public String toString() {return id.toString(); }
    }

}
