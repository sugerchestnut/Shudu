package game_shudu;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.*;
import java.util.*;

/**
 * Created by pl on 15-4-19.
 */
class self_info{
    private int x;
    private int y;
    private char flag;
    private char value;
    private int backSigal;//用来标记是否是回退元素的一个标记,初始为0

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getFlag() {
        return flag;
    }

    public void setFlag(char flag) {
        this.flag = flag;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public int getBackSigal(){  return backSigal; }

    public void setBackSigal(int backSigal){    this.backSigal=backSigal;  }

    public self_info(){}

    public self_info(int x,int y,char flag,char value,int backSigal){
        this.x=x;
        this.y=y;
        this.flag=flag;
        this.value=value;
        this.backSigal=backSigal;
    }
}

class shudu{              //数独类
    private self_info[][] self_twodemin;//创建一个二维数组将字符数组包装成二维数组对象保存起来
    private char[][] array_Shudu1;
    //Set <self_info>  noreptarray = new HashSet<self_info>();//创建一个没有重复的集合
    LinkedList<self_info> self_Stack = new LinkedList<self_info>();//创建一个栈
    private char[] s1 ={'1','2','3','4','5','6','7','8','9'};
    private int[] s4;

    public shudu(){}
    public shudu(char[][] t){
        int i;
        s4 = new int[3];//保存退栈的元素的对应信息
        array_Shudu1 = new char[9][9];
        self_twodemin = new self_info[9][9];
        array_Shudu1 = t;
        for (int e = 0; e < array_Shudu1.length; e++) {        //将array_Shudu数组里出现的元素添加进去，不会重复
            for (int f = 0; f < array_Shudu1[e].length; f++) {
                self_twodemin[e][f] = new self_info(e, f, array_Shudu1[e][f], array_Shudu1[e][f], 0);
            }
        }
    }

    public void array_Show(){
        for (int r = 0; r < array_Shudu1.length; r++){
            for (int c = 0;c < array_Shudu1[r].length; c++) {
                System.out.print(array_Shudu1[r][c]);
            }
            System.out.println();
        }
    }

    public boolean checkLine(char key,int r,int c){//判断行
        int i;
        for (i = 0; i < 9; i++) {
            //if (i == c)//如果遇到准备填写的那一列的话直接跳过，只用检查其他的格子
            //continue;
            if (self_twodemin[r][i].getValue() == key)
                return false;
        }
        return true;
    }

    public boolean checkRow(char key,int r,int c){//判断列
        int i;
        for (i = 0; i < 9; i++) {
            //if (i == r)//如果遇到准备填写的那一列的话直接跳过，只用检查其他的格子
            //continue;
            if (self_twodemin[i][c].getValue() == key)
                return false;
        }
        return true;
    }

    public boolean checkNine(char key,int r,int c){//判断九格
        int i=(r/3)*3;
        int j=(c/3)*3;
        int m=i;
        int n=j;

        for (; m < i+3; m++) {         //在for循环里面定义的变量在for语句执行完后就会被撤销
            for (; n < j+3; n++) {
                if (self_twodemin[m][n].getValue() == key)
                    return false;
            }
        }
        return true;
    }

    public boolean game_KeyMethod(char[] temp,int p1,int p2){//t 代表要进行检查的字符
        int m = 0;
        boolean tag = true;
        if (self_twodemin[p1][p2].getBackSigal() == 1) {//如果这个字符是经过退栈后再次检查的话，则需要从temp的下一个位置开始检查
            m = s4[2] + 1;
            self_twodemin[p1][p2].setBackSigal(0);//出过一次栈之后让他的退栈标志重返0
        }

        for (; m < 9; m++) {
            if (checkLine(temp[m], p1, p2) && checkRow(temp[m], p1, p2) && checkNine(temp[m], p1, p2)) {

                self_twodemin[p1][p2].setValue(temp[m]);
                if (p1==1 && p2==0)
                    System.out.println("第二行第一列的值"+self_twodemin[p1][p2].getValue());

                self_Stack.addLast(self_twodemin[p1][p2]);

                System.out.println(self_Stack.getLast().getValue());
                break;
            }
        }
        show_data();
        //跑完了数组还没找到合适的数能添加进去，此时需要找到上一个刚刚添加进去的数，退栈，重新试
        if (m == 9 && self_twodemin[p1][p2].getValue() == self_twodemin[p1][p2].getFlag()){
            tag = false;
        }
        return tag;
    }

    public void backPop(char[] temp){
        self_info last_elem;
        last_elem = new self_info();

        last_elem = self_Stack.getLast();//获取栈中最后一个元素
        s4[0] = last_elem.getX();
        s4[1] = last_elem.getY();
        System.out.println("退栈元素坐标:"+last_elem.getX()+" "+last_elem.getY());
        if (last_elem == null) {
            throw new NoSuchElementException();
        }
        self_Stack.removeLast();//元素出栈
        System.out.print("退栈元素:");
        System.out.println(last_elem.getValue()+" ");
        System.out.println("退栈后剩余:"+self_Stack.size());

        for (int u = 0; u < s1.length; u++){
            if (s1[u] == last_elem.getValue())
                s4[2] = u;
        }
    }

    public void shudu_Game(){
        int e=0;
        int f=0;
        for ( e = 0; e < 9; e++){//整体循环遍历一遍进行操作
            for ( f = 0; f < 9; f++){
                if (self_twodemin[e][f].getValue() == '0' ){
                    if((game_KeyMethod(s1, e, f)) == false){
                        backPop(s1);
                        e=s4[0];
                        f=s4[1];
                        self_twodemin[e][f].setBackSigal(1);//将二维数组里面该元素的退栈标志设为1
                        self_twodemin[e][f].setValue('0');//将二维数组里该元素的值重置为0
                        show_data();//显示退栈后的元素
                        f--;
                        //if( f==8 ){ e--; }

                    }
                }
            }
        }
    }

    public void show_data(){
        for (int t = 0;t < self_twodemin.length; t++){
            for (int p = 0;p < self_twodemin[t].length; p++){
                System.out.print(self_twodemin[t][p].getValue());
            }
            System.out.println();
        }
    }
}

public class MainMethod {
    public static void main(String[] args) {
        FileReader fr = null;
        BufferedReader br = null;
        char[][] array_Shudu;//从文件里面读出来的数据保存在该二维数组里
        array_Shudu = new char[9][9];
        int sign = 0;

        try {
            fr = new FileReader("/home/pl/编程数据/input.txt");
            br = new BufferedReader(fr);

            String strLine;
            while ((++sign) % 11 != 0 && (strLine = br.readLine()) != null) {
                //strLine = br.readLine();
                //System.out.println(strLine);
                if (sign == 1 || (strLine == "---"))
                    continue;
                for (int c = 0; c < strLine.length(); c++)
                    array_Shudu[sign - 2][c] = strLine.charAt(c);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件数据如下:");
        for (int r = 0; r < array_Shudu.length; r++) {
            for (int c = 0; c < array_Shudu[r].length; c++) {
                System.out.print(array_Shudu[r][c]);
            }
            System.out.println();
        }
        shudu sudoKu = new shudu(array_Shudu);
        sudoKu.shudu_Game();
        System.out.println();
        sudoKu.show_data();
    }
}
