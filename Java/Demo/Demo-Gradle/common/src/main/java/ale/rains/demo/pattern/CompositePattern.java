package ale.rains.demo.pattern;

/**
 * 组合模式
 * 组合模式是一种结构型设计模式，它允许你将对象组合成树形结构以表示“部分-整体”的层次结构。
 * 这种模式使得客户端可以统一对待单个对象和对象组合。
 * 在组合模式中，有两种基本类型的对象：叶节点和组合节点。
 * 叶节点表示树形结构中的单个对象，而组合节点则表示树形结构中的对象组合。
 * 组合节点可以包含其他组合节点和/或叶节点，形成一个递归的树形结构。
 */
public class CompositePattern {
    /*
    public interface IComponent {
        void display();
    }
    // 树形结构中的节点，包括叶节点和组合节点。
    // 可以通过add()和remove()方法来添加和删除子节点。
    public abstract class Component implements IComponent {
        protected String name;
        public Component(String name) {
            this.name = name;
        }
        public abstract void add(IComponent component);
        public abstract void remove(IComponent component);
    }
    // 具体的组合节点
    public class Composite extends Component {
        private List<IComponent> children = new ArrayList<>();
        public Composite(String name) {
            super(name);
        }
        @Override
        public void add(IComponent component) {
            children.add(component);
        }
        @Override
        public void remove(IComponent component) {
            children.remove(component);
        }
        @Override
        public void display() {
            System.out.println("Composite: " + name);
            for (IComponent component : children) {
                component.display();
            }
        }
    }
    // 叶节点没有子节点，但可以实现共同的操作方法
    public class Leaf implements IComponent {
        private String name;
        public Leaf(String name) {
            this.name = name;
        }
        @Override
        public void display() {
            System.out.println("Leaf: " + name);
        }
    }
    // 演示代码
    public static void main(String[] args) {
        Component root = new Composite("root");
        Component branch1 = new Composite("branch1");
        Component branch2 = new Composite("branch2");
        Component leaf1 = new Leaf("leaf1");
        Component leaf2 = new Leaf("leaf2");
        Component leaf3 = new Leaf("leaf3");
        root.add(branch1);
        root.add(branch2);
        branch1.add(leaf1);
        branch2.add(leaf2);
        branch2.add(leaf3);
        root.display();
    }
    */
}