
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.opengl.GLCanvas;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class playing extends JFrame implements MouseListener , ActionListener
    {
        
                GLCanvas glcanvas;
                AnimGLEventListener listener;
                Animator animator;
                JButton b = new JButton("BACK");
                JPanel jp =new JPanel();
    public playing() 
        {
                //b.setSize(1, 1);
                setUndecorated(true);
                listener = new AnimGLEventListener();
                glcanvas = new GLCanvas();
                glcanvas.addGLEventListener(listener);
                glcanvas.addMouseListener(this);
                jp.add(b);
                
                getContentPane().add(glcanvas, BorderLayout.CENTER);
                getContentPane().add(jp, BorderLayout.NORTH);
                b.addActionListener(this);
                animator = new FPSAnimator(150);
                animator.add(glcanvas);
                animator.start();
                

                setVisible(true);
                setTitle("CONNECT4");
                setSize(800, 700);
                setLocationRelativeTo(null);
                //setFocusable(true);
                glcanvas.requestFocus();
        }

    
    
    @Override
    public void mouseClicked(MouseEvent e) 
        {
                listener.xm = (100 * e.getX() / glcanvas.getWidth()) ;
                listener.clicked = true ;
        }

    @Override
    public void mousePressed(MouseEvent e) 
        {
        }

    @Override
    public void mouseReleased(MouseEvent e) 
        {
        }

    @Override
    public void mouseEntered(MouseEvent e) 
        {
        }

    @Override
    public void mouseExited(MouseEvent e)
        {
        }

    @Override
    public void actionPerformed(ActionEvent e) 
        {
            dispose();
            Start s = new Start();
        }
    
    }
