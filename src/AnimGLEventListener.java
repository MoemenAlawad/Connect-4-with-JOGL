import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.BitSet;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

class AnimGLEventListener implements GLEventListener ,  MouseListener {

    int d ; 
    int xr[] = new int[21];
    int connect4[][] = new int[6][7];
    int yr[][] = new int[6][7];
    boolean connect4_d[][] = new boolean[6][7];
    int sol[][] = new int[6][7];
    int connect4_x[] = {2, 16, 31, 45, 59, 73, 88};
    int connect4_y[] = {2, 16, 31, 45, 59, 74};
    int xm;
    int index;
    int col;
    int row;
    boolean finish ;
    int player = 2;
    int i, j;
    int col_selector[] = {1,11,18,27,31,39,44,54,59,68,74,83,87,97} ;
    boolean clicked;
    boolean draw ; 
    
    int maxWidth = 100, maxHeight = 100 , k;
    String textureNames[] = { "bg.png","r.png","y.png","turn.png","ywin.png","gwin.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                //                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                yr[i][j] = 88;
            }
        }
    }

    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        DrawBackground(gl);
        handleKeyPress();
        if (d(sol))
            {
                finish = true ;  
            }
        for (i = 0; i < 6; i++)
            for (j = 0; j < 7; j++) 
                  if (connect4_d[i][j]) 
                        {
                            DrawSprite(gl, connect4_x[j], connect4_y[i], connect4[i][j] , 1);
                        }
        if ( player == 1 )
            {
               // DrawSprite(gl, 2,88 ,3 , 1);
                DrawSprite(gl,45,88 ,2 , 1);
            } 
        if (player == 2 )
            {
                DrawSprite(gl, 45,88 ,1 , 1);
                //DrawSprite(gl,88,88 ,3 , 1);
            } 
        if (finish)
            {
                if (d(sol))
                    {
                            DrawSprite(gl, 45,45 ,3 , 3);
                    
                    }    
              else  if (player == 1 )
                    {
                        DrawSprite(gl, 45,45 ,5 , 3);
                    }
              else    if (player == 2 )
                    {     
                        DrawSprite(gl, 45 , 45 , 4 , 3);
                    }       
                
            }
        if (clicked && !draw && !finish) {
            if(player == 1)
                player = 2 ; 
            else player = 1 ;
            
            for (int i  = 0 ; i < 13 ; i+=2)
                if (xm >= col_selector[i] && xm <= col_selector[i+1])
                        {   
                            col = i/2 ; 
                            break ;
                        }
            for (row = 0 ; row < 6 ; row++) 
                {
                    if (connect4[row][col] == 0)
                        {
                            connect4[row][col] = player;
                            sol[5-row][col] = player;
                            break;
                        }
                }
            if(row == 6 )row-- ;
            draw = true ; 
            clicked = false ;
            
                      
        }
        
        if (draw && !finish)    
            {
                if (yr[row][col] != connect4_y[row] ) 
                       DrawSprite(gl, connect4_x[col], yr[row][col]--, player, 1);
                else 
                    {
                        DrawSprite(gl, connect4_x[col], connect4_y[row], player, 1);
                        
                        connect4_d[row][col] = true ;
                        draw = false  ;
                        if (who_win(sol) == 1 ) 
                            {
                                finish = true ;
                            }
                  else  if (who_win(sol) == 2 ) 
                            {
                                finish = true ;
                            }
                    }
            }
        }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    /*
     * KeyListener
     */
    public void handleKeyPress() {

    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    public static int who_win(int [][] m)
        {
            int i ; 
            for ( i = 0 ; i < 7 ; i++ )
                {
                    if (m[0][i] * m[1][i] * m[2][i] * m[3][i] == 1  )
                        {
                            return 1 ;
                        }
                    if (m[0][i] * m[1][i] * m[2][i] * m[3][i] == 16)
                        return 2 ;
                    
                    if (m[1][i] * m[2][i] * m[3][i] * m[4][i] == 1 )
                        {
                            return 1 ;
                        }
                    if (m[1][i] * m[2][i] * m[3][i] * m[4][i] == 16)
                        return 2 ;
                   
                    
                    if (m[2][i] * m[3][i] * m[4][i] * m[5][i] == 1 )
                        {
                            return 1 ;
                        }
                    if (m[2][i] * m[3][i] * m[4][i] * m[5][i] == 16)
                        return 2 ;
                    
                }
            for ( i = 0 ; i < 6 ; i++ )
                {
                    if (m[i][0] * m[i][1] * m[i][2] * m[i][3] == 1  )
                        {
                            return 1 ;
                        }
                    if (m[i][0] * m[i][1] * m[i][2] * m[i][3] == 16)
                        return 2 ;
                    
                    if (m[i][1] * m[i][2] * m[i][3] * m[i][4] == 1 )
                        {
                            return 1 ;
                        }
                    if (m[i][1] * m[i][2] * m[i][3] * m[i][4] == 16)
                        return 2 ;
                   
                    
                    if (m[i][2] * m[i][3] * m[i][4] * m[i][5] == 1 )
                        {
                            return 1 ;
                        }
                    if (m[i][2] * m[i][3] * m[i][4] * m[i][5] == 16)
                        return 2 ;
                    
                    if (m[i][3] * m[i][4] * m[i][5] * m[i][6] == 1 )
                        {
                            return 1 ;
                        }
                    if (m[i][3] * m[i][4] * m[i][5] * m[i][6] == 16)
                        return 2 ;
                    
                    
                }
            if (m[3][0]*m[2][1]*m[1][2]*m[0][3] == 1 )
                return 1;
            if (m[3][0]*m[2][1]*m[1][2]*m[0][3] == 16 )
                return 2;
            
            if (m[5][3]*m[4][4]*m[3][5]*m[2][6] == 1 )
                return 1;
            if (m[5][3]*m[4][4]*m[3][5]*m[2][6] == 16 )
                return 2;
            
            
            if (m[4][0]*m[3][1]*m[2][2]*m[1][3] == 1 )
                return 1;
            if (m[4][0]*m[3][1]*m[2][2]*m[1][3] == 16 )
                return 2;
            if (m[3][1]*m[2][2]*m[1][3]*m[0][4] == 1 )
                return 1;
            if (m[3][1]*m[2][2]*m[1][3]*m[0][4] == 16 )
                return 2;
            
            if (m[5][2]*m[4][3]*m[3][4]*m[2][5] == 1 )
                return 1;
            if (m[5][2]*m[4][3]*m[3][4]*m[2][5] == 16 )
                return 2;
            if (m[4][3]*m[3][4]*m[2][5]*m[1][6] == 1 )
                return 1;
            if (m[4][3]*m[3][4]*m[2][5]*m[1][6] == 16 )
                return 2;
            
            
            if (m[5][1]*m[4][2]*m[3][3]*m[2][4] == 1 )
                return 1;
            if (m[5][1]*m[4][2]*m[3][3]*m[2][4] == 16 )
                return 2;
            if (m[4][2]*m[3][3]*m[2][4]*m[1][5] == 1 )
                return 1;
            if (m[4][2]*m[3][3]*m[2][4]*m[1][5] == 16 )
                return 2;
            if (m[3][3]*m[2][4]*m[1][5]*m[0][6] == 1 )
                return 1;
            if (m[3][3]*m[2][4]*m[1][5]*m[0][6] == 16 )
                return 2;
            
            if (m[5][0]*m[4][1]*m[3][2]*m[2][3] == 1 )
                return 1;
            if (m[5][0]*m[4][1]*m[3][2]*m[2][3] == 16 )
                return 2;
            if (m[4][1]*m[3][2]*m[2][3]*m[1][4] == 1 )
                return 1;
            if (m[4][1]*m[3][2]*m[2][3]*m[1][4] == 16 )
                return 2;
            if (m[3][2]*m[2][3]*m[1][4]*m[0][5] == 1 )
                return 1;
            if (m[3][2]*m[2][3]*m[1][4]*m[0][5] == 16 )
                return 2;
            /*
            */
            if (m[2][0]*m[3][1]*m[4][2]*m[5][3] == 1 )
                return 1;
            if (m[2][0]*m[3][1]*m[4][2]*m[5][3] == 16 )
                return 2;
            
            if (m[0][3]*m[1][4]*m[2][5]*m[3][6] == 1 )
                return 1;
            if (m[0][3]*m[1][4]*m[2][5]*m[3][6] == 16 )
                return 2;
            
            
            if (m[1][0]*m[2][1]*m[3][2]*m[4][3] == 1 )
                return 1;
            if (m[1][0]*m[2][1]*m[3][2]*m[4][3] == 16 )
                return 2;
            if (m[2][1]*m[3][2]*m[4][3]*m[5][4] == 1 )
                return 1;
            if (m[2][1]*m[3][2]*m[4][3]*m[5][4] == 16 )
                return 2;
            
            if (m[0][2]*m[1][3]*m[2][4]*m[3][5] == 1 )
                return 1;
            if (m[0][2]*m[1][3]*m[2][4]*m[3][5] == 16 )
                return 2;
            if (m[1][3]*m[2][4]*m[3][5]*m[4][6] == 1 )
                return 1;
            if (m[1][3]*m[2][4]*m[3][5]*m[4][6] == 16 )
                return 2;
            
            
            if (m[0][1]*m[1][2]*m[2][3]*m[3][4] == 1 )
                return 1;
            if (m[0][1]*m[1][2]*m[2][3]*m[3][4] == 16 )
                return 2;
            if (m[1][2]*m[2][3]*m[3][4]*m[4][5] == 1 )
                return 1;
            if (m[1][2]*m[2][3]*m[3][4]*m[4][5] == 16 )
                return 2;
            if (m[2][3]*m[3][4]*m[4][5]*m[5][6] == 1 )
                return 1;
            if (m[2][3]*m[3][4]*m[4][5]*m[5][6] == 16 )
                return 2;
            
            if (m[0][0]*m[1][1]*m[2][2]*m[3][3] == 1 )
                return 1;
            if (m[0][0]*m[1][1]*m[2][2]*m[3][3] == 16 )
                return 2;
            if (m[1][1]*m[2][2]*m[3][3]*m[4][4] == 1 )
                return 1;
            if (m[1][1]*m[2][2]*m[3][3]*m[4][4] == 16 )
                return 2;
            if (m[2][2]*m[3][3]*m[4][4]*m[5][5] == 1 )
                return 1;
            if (m[2][2]*m[3][3]*m[4][4]*m[5][5] == 16 )
                return 2;
            
            
            
            
            
            return 0 ;
        }
    public static boolean d (int [][] m )
        {
            for (int i = 0 ; i < 6  ; i++)
                for (int j = 0 ; j < 7 ;j++ )
                    if (m [i][j] == 0 )
                        return false ; 
            
            return true ; 
        }
    
}
