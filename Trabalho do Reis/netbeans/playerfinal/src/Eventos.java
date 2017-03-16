
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nmore_000
 */
public class Eventos extends javax.swing.JFrame implements ActionListener{
    int segundos = 60;
    Timer timer;

    public Eventos() {
        super("Eventos");
        this.timer = new Timer(1000,taskPerformer);
        this.timer.setCoalesce(true);
        this.timer.setRepeats(true);
        JButton botao = new JButton("Clique");
        botao.addActionListener(this);
        getContentPane().add(botao);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this.timer.start();
    }

    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            jLabel7.(segundos);
            if (segundos == 0) {
                timer.stop();
            } else {
                segundos--;
            }
        }
    };

    public static void main(String args[]) {
        new Eventos();
    }

   
}
