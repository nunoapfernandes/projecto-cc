package back_end_pool;


public class MonitorUDP {

    private static front_end_server.Menu mainMenu;

    public static void main(String[] args)  {

        MonitorUDP_Register register = new MonitorUDP_Register();
        MonitorUDP_Answer answer = new MonitorUDP_Answer();

        register.start();
        answer.start();

        loadMenu();
        do{
            mainMenu.executeMenu();
            switch (mainMenu.getOption()){
                default: break;
            }
        } while (mainMenu.getOption()!=0);

        register.interrupt();
        answer.interrupt();
    }


    public static void loadMenu(){
        String[] main_menu = {
                "Quit",
        };

        mainMenu = new front_end_server.Menu(main_menu);
    }
}
