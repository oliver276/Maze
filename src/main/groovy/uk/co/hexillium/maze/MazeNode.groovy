package uk.co.hexillium.maze

class MazeNode {

    MazeNode up
    MazeNode down
    MazeNode left
    MazeNode right

    boolean redundant

    int x
    int y

    MazeNode(int x, int y) {
        this.up = up
        this.down = down
        this.left = left
        this.right = right
        this.x = x
        this.y = y
    }

    MazeNode getUp() { up }

    MazeNode getDown() {
        return down
    }

    MazeNode getLeft() {
        return left
    }

    MazeNode getRight() {
        return right
    }

    int getX() {
        return x
    }

    int getY() {
        return y
    }

    boolean horizontallyRedundant() {
        return up == null && down == null && left != null && right != null;
    }
    boolean verticallyRedundant() {
        return up != null && down != null && left == null && right == null;
    }
}
