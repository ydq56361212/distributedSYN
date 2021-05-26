import java.util.LinkedList;

public class Graph {
    int v;
    LinkedList<Integer>[] adjListArray;

    //用户定义代表图表的类。
    //图是邻接表的数组。
    //数组的大小将为V（图形中的顶点数）
    public Graph(int v) {
        this.v = v;
        //将数组的大小定义为顶点数
        adjListArray = new LinkedList[v];
        ////为每个顶点创建一个新列表，以便可以存储相邻节点
        for (int i = 0; i < v; i++) {
            adjListArray[i] = new LinkedList<Integer>();
        }
    }

    //将边缘添加到无向图
    public void addEdge(int src, int dest) {
        //在src中将一条边添加到dest。
        adjListArray[src].add(dest);
        //由于图是无向的，因此也要从dest到src添加一条边
        //adjListArray[dest].add(src);
    }

    public void DFSUtil(int v, boolean[] visited) {
        // 将当前节点标记为已访问并打印
        visited[v] = true;
        System.out.print(v + " ");
        // 递归所有顶点
        // 邻近此顶点
        for (int x : adjListArray[v]) {
            if (!visited[x]) DFSUtil(x, visited);
        }
    }

    public void connectedComponents() {
        // 将所有顶点标记为未访问
        boolean[] visited = new boolean[v];
        for (int i = 0; i < v; ++i) {
            if (!visited[i]) {
                // 从v打印所有可到达的顶点
                DFSUtil(i, visited);
                System.out.println();
            }
        }

    }
}
