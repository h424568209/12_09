import sun.reflect.generics.tree.Tree;

import javax.swing.plaf.nimbus.State;
import java.util.*;

public class LeeCode {
    class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;
        public TreeNode(int val){
            this.val = val;
        }
    }

    /**
     * 给定一个二叉树，根节点为第1层，深度为 1。在其第 d 层追加一行值为 v 的节点
     * 递归
     * 非递归（深度优先遍历） -》栈
     * 非递归（广度优先遍历） -》队列
     * @param t 树的根节点
     * @param v 追加的值
     * @param d 深度
     * @return 新的树
     */
    public TreeNode addOneRow(TreeNode t, int v, int d) {
        if(d == 1){
            TreeNode node = new TreeNode(v);
            node.left = t;
            return node;
        }
        insert(t,v,d,1);
        return t;
    }

    private void insert(TreeNode node, int val, int n, int depth) {
        if(node == null){
            return;
        }
        if(depth == n-1){
            TreeNode left = node.left ;
            node.left = new TreeNode(val);
            node.left.left = left;
            TreeNode right= node.right;
            node.right = new TreeNode(val);
            node.right.right = right;
        }else{
            insert(node.left,val,n,depth+1);
            insert(node.right,val,n,depth+1);
        }
    }

    /**
     * 递归转非递归
     * 将TreeNode转为Node，Node中包含当前的树节点和当前饿深度
     * 栈中存放的是Node类型的节点
     */
    class Node{
        TreeNode node;
        int depth;
        Node(TreeNode n , int d){
            node = n;
            depth = d;
        }
    }
    public TreeNode addOneRowss(TreeNode t, int v, int d) {
        if(d == 1){
            TreeNode node = new TreeNode(v);
            node.left = t;
            return node;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(new Node(t,1));
        while(!stack.isEmpty()){
            Node n =stack.pop();
            if(n.node == null){
                continue;
            }
            if(n.depth == d-1){
                TreeNode temp = n.node.left;
                n.node.left = new TreeNode(v);
                n.node.left.left = temp;
                temp = n.node.right;
                n.node.right = new TreeNode(v);
                n.node.right.right = temp;
            }else{
                stack.push(new Node(n.node.left,n.depth+1));
                stack.push(new Node(n.node.right,n.depth+1));
            }
        }
        return t;
    }
    /**
     * 使用广度优先搜索进行插入元素
     * 维护两个队列，queue中始终存放的是同一层的树节点
     */
    public TreeNode addOneRows(TreeNode t, int v, int d) {
        if(d==1){
            TreeNode node = new TreeNode(v);
            node.left = t;
            return node;
        }
        Queue <TreeNode> queue = new LinkedList<>();
        queue.add(t);
        int depth = 1;
        while(depth < d-1){
            Queue<TreeNode> temp = new LinkedList<>();
            while(!queue.isEmpty()){
                TreeNode node = queue.remove();
                if(node.left !=null)
                temp.add(node.left);
                if(node.right !=null)
                temp.add(node.right);
            }
            queue = temp;
            depth++;
        }
        while(!queue.isEmpty()){
            TreeNode node = queue.remove();
            TreeNode temp = node.left;
            node.left = new TreeNode(v);
            node.left.left = temp;
            temp = node.right;
            node.right = new TreeNode(v);
            node.right.right = temp;
        }
        return t;
    }
    /**
     * 存在重复元素
     * 给定一个整数数组，判断数组中是否有两个不同的索引 i 和 j，
     * 使得 nums [i] 和 nums [j] 的差的绝对值最大为 t，并且 i 和 j 之间的差的绝对值最大为 ķ。
     *
     * 利用自平衡二叉树可以在对数的时间内通过插入和删除来对滑动窗口内元素进行排序
     * 思路： 构建一个空的二叉搜索树set
     *          对于每个元素x 遍历数组， 在set上查找大于等于x的最小的数 若 s - x >=t ->true
     *                                在set上查找小于x的最大的数，若  x - g <= t -> true
     *      如果树的大小超过了K，则移除最早加入树的那个节点
     * @param nums 数组
     * @param k 索引的绝对值的差的最大值
     * @param t 元素的绝对值的差的最大值
     * @return true/false
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        Set<Integer> set = new TreeSet<>();
        for(int i = 0 ; i < nums.length ; i++){
            //大于给定元素的最小的数
            Integer s = ((TreeSet<Integer>) set).ceiling(nums[i]);
            if(s!=null && s<=nums[i]+t) {
                return true;
            }
            //小于给定元素的最大的数
            Integer m = ((TreeSet<Integer>) set).floor(nums[i]);
                if(m!=null && t+m>= nums[i]){
                return true;
            }
            set.add(nums[i]);
                if(set.size()>k){
                    set.remove(nums[i-k]);
                }
        }
        return false;
    }
    /**
     * 给定一个字符串数组，将字母异位词组合在一起。字母异位词指字母相同，但排列不同的字符串。
     * 使用HashMap ：  数组中元素的字母的顺序排列做键，数组的元素做值
     * 如果map中有这个键，则将这个字符串放这个键对应的链表中
     * 否则重新申请一个链表，加入这个键
     * @param strs 字符串数组
     * @return 链表
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<String,ArrayList<String>> hashMap = new HashMap<>();
        for(String s : strs){
            char[] ch = s.toCharArray();
            Arrays.sort(ch);
            //将其中的元素进行排序 若含有的元素相同，则其排序后是一个相同的字符串
            String key = String.valueOf(ch);
            //将字符串放到对应的key下面
            if(!hashMap.containsKey(key))
                hashMap.put(key,new ArrayList<>());
            hashMap.get(key).add(s);
        }
        return new ArrayList<>(hashMap.values());
    }
    /**
     * 跳跃游戏2
     *给定一个非负整数数组，你最初位于数组的第一个位置。
     * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
     * 你的目标是使用最少的跳跃次数到达数组的最后一个位置。
     * 已知终点的位置，可以不断进行更新终点，直到终点是0完成跳跃。
     * @param nums 给定数组
     * @return 最小的跳跃次数
     */
    public int jump(int[] nums) {
        int steps = 0 ;
        int position = nums.length-1;
        while(position!=0){
            for(int i = 0 ; i < position ; i++){
                if(nums[i] + i >= position)  { //当前位置可以跳到终点
                    position = i ;  //更新当前位置是终点位置
                    steps ++;
                    break;
                }
            }
        }
        return steps;
    }
    public static void main(String[] args) {
        LeeCode l = new LeeCode();
        System.out.println(l.jump(new int[]{2,3,1,1,2,2,2}));
        System.out.println(l.groupAnagrams(new String[]{"eat","ate","tea","qwe","ewq","ww"}));
        System.out.println(l.containsNearbyAlmostDuplicate(new int[]{1,5,9,1,5,9},2,3));
    }
}
