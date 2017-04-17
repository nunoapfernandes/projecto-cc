package gui;


import ccclient.Pergunta;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nmore_000
 */
public class Jogo extends javax.swing.JFrame {
    private char resposta;
    
    
    int segundos = 60;
    Timer t;
    private File musicFile;
    private SourceDataLine sDLine;
    private AudioInputStream aIOStream;
    private Thread aux;
    private boolean flag;
    Pergunta pergunta;
    
    public Jogo() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Windows".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
                }
            }
        } catch (Exception e) {}
        
        initComponents();
        
        // Get the screen size  
        GraphicsConfiguration gc = getGraphicsConfiguration();  
        Rectangle bounds = gc.getBounds();  
        
        // Create and pack the Elements  
        //.... code to create the panels etc  
        pack();  
   
        // Set the Location and Activate  
        Dimension size = getPreferredSize();  
        setLocation((int) ((bounds.width / 2) - (size.getWidth() / 2)),  
                    (int) ((bounds.height / 2) - (size.getHeight() / 2))); 
        
        contador();
        t.start();
        
        aux = new Thread(new Runnable() {
			public void run() {
			}
	});
        }
    
    private void create() throws UnsupportedAudioFileException, IOException,
			LineUnavailableException {
		AudioInputStream in = AudioSystem.getAudioInputStream(musicFile);
		AudioInputStream din = null;
		if (in != null) {
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			sDLine = getLine(decodedFormat);
			aIOStream = din;
		}
	}


    public void play() throws IOException, LineUnavailableException {
		start();
		aux.interrupt();
		aux = new Thread(new Runnable() {
			public void run() {
				rawplay();
			}
		});
		aux.start();
	}

	/**
	 * Play same part of the music
	 */
	private void play(byte[] data) throws IOException {
		try {
			// Read a stream
			int length = aIOStream.read(data, 0, data.length);
			// Play the stream
			sDLine.write(data, 0, length);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Start the SourceDataLine for play
	 */
	private void start() {
		sDLine.start();
		flag = true;
	}

	/**
	 * The engine of the player
	 */
	private void rawplay() {
		byte[] data = new byte[4096];
		try {
			while (flag)
				play(data);
		} catch (IOException e) {
			System.out.println("Music end");
		}
	}

	/**
	 * Close all I/O
	 */
	public void close() {
		aux.interrupt();
		sDLine.drain();
		sDLine.stop();
		sDLine.close();
		try {
			aIOStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop the SourceDataLine, if started again only can continue.
	 */
	public void stop() {
		aux.interrupt();
		sDLine.stop();
		sDLine.drain();
		flag = false;
	}

	/**
	 * If the SourceDataLine is already in stop continue playing.
	 */
	public void resume() {
		sDLine.start();
		flag = true;
		try {
			play();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the SourceDataLine for Read and Write the music
	 * 
	 * @param audioFormat
	 *            The music as Stream
	 */
	private SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	public String getNomeMusica() throws UnsupportedAudioFileException,
			IOException {

		AudioFileFormat baseFileFormat = AudioSystem
				.getAudioFileFormat(musicFile);
		Map<String, Object> propriedade = ((AudioFileFormat) baseFileFormat)
				.properties();

		String nome = (String) propriedade.get("author");
		nome += " - " + propriedade.get("title");

		return nome;
	}

	public String getExtensao() {
		StringBuilder sb = new StringBuilder();
		for (int i = (pergunta.getPath().length() - 3); i < pergunta.getPath().length(); i++) {
			sb.append(pergunta.getPath().charAt(i));
		}
		return sb.toString().toUpperCase();
	}

	private boolean checarExtensao() {
		String a = "MP3";
		String b = "OGG";
		return (a.contentEquals(getExtensao()) || b.contentEquals(getExtensao()));
	}
        
        public void mudaPergunta(Pergunta in) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            this.pergunta = in;
                
                ImageIcon i = new ImageIcon("C:\\Users\\nmore_000\\Desktop\\aObNp72_700b_v1.jpg");
                //ImageIcon i = (this.pergunta.getImage());
                
                Image image = i.getImage(); // transform it 
                Image newimg = image.getScaledInstance(jInternalFrame1.getWidth(),jInternalFrame1.getHeight() ,  java.awt.Image.SCALE_SMOOTH);  
                i = new ImageIcon(newimg);
                jLabel2.setIcon(i);
                
                
                jLabel2.setText(new String());
           
               
                jInternalFrame1.add(jLabel2);
                jInternalFrame1.setLocation(0,0);
              
                
               
                jLabel5.setText(pergunta.getAnswers().get(0));
                jLabel10.setText(pergunta.getAnswers().get(1));
                jLabel11.setText(pergunta.getAnswers().get(2));
                jLabel12.setText(pergunta.getAnswers().get(3));
                
                
                musicFile = pergunta.getMusic();
                create();
	}

	public String getQualidade() throws UnsupportedAudioFileException,
			IOException {

		AudioFileFormat baseFileFormat = AudioSystem
				.getAudioFileFormat(musicFile);
		Map<String, Object> propriedade = ((AudioFileFormat) baseFileFormat)
				.properties();

		if (checarExtensao() == true) {
			String buffer = "";
			String key = getExtensao().toLowerCase();

			key += ".bitrate.nominal.bps";
			buffer = (String) propriedade.get(key).toString();
			int aux = Integer.parseInt(buffer) / 1000;
			buffer = Integer.toString(aux) + " Kbps | ";

			key = getExtensao().toLowerCase();
			key += ".frequency.hz";
			key = (String) propriedade.get(key).toString();
			aux = Integer.parseInt(key) / 1000;
			buffer += Integer.toString(aux) + " Khz";

			return buffer;

		} else
			return "??? Kbps | ?? Khz";
	}

    public void contador() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        t = new Timer(1000,taskPerformer);
        t.setCoalesce(true);
        t.setRepeats(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        setVisible(true);
    }
    
    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            jLabel7.setText(Integer.toString(segundos));
            if (segundos == 0) {
                t.stop();
            } else {
                segundos--;
            }
        }
    };
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabel2 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel9.setText("jLabel9");

        jButton2.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1024, 600));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setForeground(new java.awt.Color(204, 204, 255));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setDoubleBuffered(false);
        jPanel1.setMaximumSize(new java.awt.Dimension(1058, 626));
        jPanel1.setMinimumSize(new java.awt.Dimension(1058, 626));
        jPanel1.setName("JOGO - CC"); // NOI18N
        jPanel1.setOpaque(false);

        jLabel1.setText("Time Left:");

        jLabel7.setText("Contador");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("jLabel5");

        jLabel10.setText("jLabel10");

        jLabel11.setText("jLabel11");

        jLabel12.setText("jLabel12");

        jButton6.setText("B");

        jButton7.setText("D");

        jButton8.setText("A");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("C");

        jInternalFrame1.setMaximumSize(new java.awt.Dimension(535, 349));
        jInternalFrame1.setMinimumSize(new java.awt.Dimension(535, 349));
        jInternalFrame1.setName("Imagem"); // NOI18N
        jInternalFrame1.setPreferredSize(new java.awt.Dimension(200, 250));
        jInternalFrame1.setVisible(true);
        jInternalFrame1.getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("jLabel2");
        jLabel2.setMaximumSize(new java.awt.Dimension(535, 349));
        jLabel2.setPreferredSize(new java.awt.Dimension(535, 349));
        jInternalFrame1.getContentPane().add(jLabel2, new java.awt.GridBagConstraints());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton8)
                            .addComponent(jButton9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(325, 325, 325)))
                .addGap(26, 26, 26))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 482, Short.MAX_VALUE)
                .addGap(301, 301, 301))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jLabel11)
                    .addComponent(jButton8)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jLabel12)
                    .addComponent(jButton9)
                    .addComponent(jLabel10))
                .addGap(33, 33, 33))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        
    }//GEN-LAST:event_jButton8ActionPerformed

  /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Jogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Jogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Jogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Jogo().setVisible(true);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

