package back_end_pool;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private List<String> options;
    private int op;

    public Menu(String[] options){
        this.options = new ArrayList<>();

        for(String op: options){
            this.options.add(op);
        }
        this.op = 0;
    }

    public void executeMenu(){
        do{
            showMenu();
            this.op = readOption();
        }
        while (this.op == -1);
    }

    public void showMenu(){
        System.out.println(" ********** MENU FOR DEBUGGING ********** ");

        for(int i=0;i< this.options.size(); i++){
            System.out.print(i+1);
            System.out.print(" - ");
            System.out.print(this.options.get(i) + "\n");
        }

        System.out.println("0 - Quit");
    }

    private int readOption(){
        int op;

        Scanner in = new Scanner(System.in);

        System.out.print("Option: ");

        op = in.nextInt();

        if(op < 0 || op>this.options.size()){
            System.out.println("Invalid option");
            op = -1;
        }
        return op;
    }

    public int getOption(){
        return this.op;
    }
}
