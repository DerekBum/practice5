interface Visitor <T> {
    fun calculate(Node: Sum): T
    fun calculate(Node: Multiply): T
    fun calculate(Node: Number): T
}

class printVisitor: Visitor<Unit> {
    override fun calculate(node: Sum) {
        node.left.operate(this)
        node.right.operate(this)
        print("+")
    }
    override fun calculate(node: Multiply) {
        node.left.operate(this)
        node.right.operate(this)
        print("*")
    }
    override fun calculate(node: Number) {
        print("${node.element}")
    }
}

class expandVisitor: Visitor<Node> {
    override fun calculate(node: Sum): Node {
        node.left = node.left.operate(this)
        node.right = node.right.operate(this)
        return node
    }
    override fun calculate(node: Multiply): Node {
        var left = node.left.getChildren()
        var right = node.right.getChildren()
        if (left.size > right.size)
            left = right.also { right = left }
        return if (left.size == 1 &&  right.size == 1)
            Multiply(left[0], right[0]).operate(this)
        else if (left.size == 1 && right.size == 2)
            Sum(Multiply(left[0], right[0]), Multiply(left[0], right[1])).operate(this)
        else
            Sum(Multiply(Multiply(left[0], right[0]), Multiply(left[0], right[1])), Sum(Multiply(left[1], right[0]), Multiply(left[1], right[1]))).operate(this)
    }
    override fun calculate(node: Number): Node {
        return node
    }
}

class calculateVisitor: Visitor<Int> {
    override fun calculate(node: Sum): Int {
        return node.left.operate(this) + node.right.operate(this)
    }
    override fun calculate(node: Multiply): Int {
        return node.left.operate(this) * node.right.operate(this)
    }
    override fun calculate(node: Number): Int {
        return node.element
    }
}

interface Node {
    fun <T> operate(visitor: Visitor<T>): T
    fun getChildren(): List<Node>
}

class Sum(var left: Node, var right: Node): Node {
    override fun <T> operate(visitor: Visitor<T>): T {
        return visitor.calculate(this)
    }
    override fun getChildren(): List<Node> {
        return listOf(left, right)
    }
}

class Multiply(val left: Node, val right: Node): Node {
    override fun <T> operate(visitor: Visitor<T>): T {
        return visitor.calculate(this)
    }
    override fun getChildren(): List<Node> {
        return listOf(left, right)
    }
}

class Number(val element: Int): Node {
    override fun <T> operate(visitor: Visitor<T>): T {
        return visitor.calculate(this)
    }
    override fun getChildren(): List<Node> {
        return listOf(this)
    }
}

fun main(args: Array<String>) {

}