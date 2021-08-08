package uk.co.hexillium.maze

import groovy.transform.CompileStatic
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

import java.awt.image.BufferedImage

@CompileStatic
class MazeImpl {

    List<MazeNode> nodes

    MazeImpl(BufferedImage bim) {

        MazeNode[][] image = new MazeNode[bim.getWidth()][bim.getHeight()]
        nodes = new ArrayList<>()

        println "The first colour is ${bim.getRGB(0, 0)} which yields ${testRGBIsWall(bim.getRGB(0, 0))}"

        // first make a black-and-white binary of where our walls and where our spaces are

        // initially this will just be a simple black/white
        for (x in 0..<bim.getWidth()) {
            for (y in 0..<bim.getHeight()) {
                if (!testRGBIsWall(bim.getRGB(x, y))) {
                    //a white pixel
                    MazeNode current = new MazeNode(x, y)
                    image[x][y] = current
                    nodes.add(current)
                }
            }
        }

        // pass over the list, assigning neighbours
        for (x in 0..<bim.getWidth()) {
            for (y in 0..<bim.getHeight()) {
                var current = image[x][y]
                if (current != null) {
                    if (x > 0 && image[x - 1][y] != null) {
                        current.left = image[x - 1][y]
                    }
                    if (x < bim.getWidth() - 1 && image[x + 1][y] != null) {
                        current.right = image[x + 1][y]
                    }
                    if (y > 0 && image[x][y - 1] != null) {
                        current.up = image[x][y - 1]
                    }
                    if (y < bim.getHeight() - 1 && image[x][y + 1] != null) {
                        current.down = image[x][y + 1]
                    }
                }
            }
        }


        // shrink the graph if we find the following:
        //
        //     A--B       A--B--C       A|
        //     |  |                     B|
        //     C--D                     C|
        //
        boolean cont = true
        while (cont) {
            cont = false
            for (Iterator iter = nodes.iterator(); iter.hasNext();) {
                MazeNode current = iter.next()
                if (current.down?.left?.up?.right == current
                        || current.down?.right?.up?.left == current
                        || current.up?.left?.down?.right == current
                        || current.up?.right?.down?.left == current
                        || current.left?.up?.right?.down == current
                        || current.left?.down?.right?.up == current
                        || current.right?.up?.left?.down == current
                        || current.right?.down?.left?.up == current) {
                    current.redundant = true
                    iter.remove()
                    current.down?.up = current.up
                    current.up?.down = current.down
                    current.left?.right = current.right
                    current.right?.left = current.left
                    cont = true
                    continue
                }
                if (current.verticallyRedundant()) {
                    iter.remove()
                    cont = true
                    current.down.up = current.up
                    current.up.down = current.down
                    continue
                }
                if (current.horizontallyRedundant()) {
                    cont = true
                    current.left.right = current.right
                    current.right.left = current.left
                    iter.remove()
                    continue
                }
            }
        }


    }

    void drawMaze(GraphicsContext g){
        int scale = 14
        int shift = 7
        nodes.each { MazeNode node ->
            g.setFill(node.redundant ? Color.AQUA : Color.GREEN)
            g.fillOval(node.x * scale - shift, node.y * scale - shift, scale, scale)
        }
        nodes.each {MazeNode node ->
            if (node.up != null){
                g.strokeLine(node.x * scale, node.y * scale, node.up.x * scale, node.up.y * scale)
            }
            if (node.down != null){
                g.strokeLine(node.x * scale, node.y * scale, node.down.x * scale, node.down.y * scale)
            }
            if (node.left != null){
                g.strokeLine(node.x * scale, node.y * scale, node.left.x * scale, node.left.y * scale)
            }
            if (node.right != null){
                g.strokeLine(node.x * scale, node.y * scale, node.right.x * scale, node.right.y * scale)
            }

        }
    }

    static boolean testRGBIsWall(int rgb) {
        // I'm doing the same thing(s) to r, g and b so it doesn't really matter what order they're in
        int red = (rgb & 0xFF0000) >> 16
        int green = (rgb & 0x00FF00) >> 8
        int blue = rgb & 0x0000FF
//        println "colour $rgb has components red: $red, green $green, blue: $blue"
        (red + green + blue) < 300
    }

}
