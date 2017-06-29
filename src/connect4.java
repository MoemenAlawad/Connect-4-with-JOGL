
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class connect4 
    {
     

        public static void main(String[] args) 
            {
                try 
                    {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) 
                            {
                                if ("Nimbus".equals(info.getName())) 
                                    {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                    }
                            }
                    } 
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(connect4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
                new Start();
               // Media hit = new Media("C:\\Users\\Public\\Music\\Sample Music\\Sleep Away.mp3");
               // MediaPlayer mediaPlayer = new MediaPlayer(hit);
               // mediaPlayer.play();
            }       
    
    }
