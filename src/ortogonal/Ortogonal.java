package ortogonal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.Timer;
/**
 *
 * @author seb_c
 */
public class Ortogonal extends JFrame implements ActionListener{
    private BufferedImage buffer;
    private Image fondo;
    private Timer timer;
    boolean baja=true;
    boolean shoot=false;
    String scorest = "Score: ";
    int score = 0;
    final int a=0;
    final int b=1;
    final int c=2;
    final int d=3;
    final int e=4;
    final int f=5;
    final int g=6;
    final int h=7;
    final int i=8;
    final int j=9;
    final int k=10;
    final int l=11;
    final int m=12;
    final int n=13;
    final int ñ=14;
    final int o=15;
    final int x=0;
    final int y=1;
    final int z=2;
    int xoff=0;
    int yoff=300;
    int zoff=0;
    
    //Puntos de la figura 9
    private int[][] figuraT ={
                            {400 , 600 ,  600, 400 , 200 , 400, 200, 400, 200, 400, 200, 400, 400, 600, 400, 600},
                            {1000, 1000, 1000, 1000, 800 , 800, 800, 800, 600, 600, 600, 600, 400, 400, 400, 400},
                            {100 , 100 , 300 , 300 , 100 , 100, 300, 300, 100, 100, 300, 300, 100, 100, 300, 300}
                           };
    
    //Cubo enemigo
    private int[][] cubo ={
                            {500+xoff , 700+xoff, 500+xoff, 700+xoff, 500+xoff, 700+xoff, 500+xoff, 700+xoff},
                            {600+yoff , 600+yoff, 600+yoff, 600+yoff, 400+yoff, 400+yoff, 400+yoff, 400+yoff},
                            {200+zoff , 200+zoff, 400+zoff, 400+zoff, 200+zoff, 200+zoff, 400+zoff, 400+zoff}
                          };
      
    //Coordenadas de la bala
    private int[][] tiro ={
                            {0},
                            {0},
                            {1}
                           };
    
    //Triangulo
    private int[][] triangle ={
                            {200,400,300,300},
                            {800,800,800,600},
                            {200,200,400,300}
                           };
    
    //Control de acciones del jugador
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            //System.out.println(e.getKeyCode());
            switch(e.getKeyCode()){
                case 32:    //shoot
                    tiro[x][a]= figuraT[x][i]+20;
                    tiro[y][a]= figuraT[y][i]-20;
                    shoot=true;
                    break;  
                case 38:    //up
                    Translation3D(figuraT,0,-10,0);
                    break;  
                case 40:    //down
                    Translation3D(figuraT,0,10,0);
                    break;  
                case 37:    //left
                    Translation3D(figuraT,-10,0,0);
                    break;  
                case 39:    //right
                    Translation3D(figuraT,10,0,0);
                    break;  
            }
        }
        public void keyReleased(){
            
        } 
    }
    
    public Ortogonal(){
        int[] vectorC = {0,0,10000};
        int[] vectorD = {960,540,5000};
        addKeyListener(new AL());
        setTitle("Geometry Wars");
        setSize(1920,1080);
        setBackground(Color.BLACK);
        setLayout(null);
        
        buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        
        timer = new Timer(16, this);
        timer.start();
           
        
       
        //Preparacion de navesita
        parallelPJ(figuraT,vectorD);    //Proyeccion paralela
        Rotate3D(figuraT,-3,-5,180);    //Rotar para perspectiva
        Scale3D(figuraT,0.2f);          //Achicar el modelo
        Translation3D(figuraT,200,700,300); //Reposicionar
        
        //Triangulo
        parallelPJ(triangle, vectorD);
        Rotate3D(triangle,-10,3,2);
        //Translation3D(triangle,400,700,0);
        
        //Creacion de la perspectiva de los enemigos
        perspectivePJ(cubo,vectorC);    //Proyeccion en perspectiva
        Rotate3D(cubo,10,10,50);        //Rotar para perspectiva
        Scale3D(cubo,0.5f);             //Achicar el modelo
        Translation3D(cubo,2000,0,0);   //Reposicion
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void putPixel(int x, int y, Color c){
        if(x<getWidth() && y<getHeight() && x>0 && y>0){
            buffer.setRGB(x,y,c.getRGB());
        }
    }
    
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    
    public int[][] Translation2D(int[][] coord, int dx, int dy){ //Algoritmo de traslacion
        int[][] homogen = { 
                            {1,0,dx},
                            {0,1,dy},
                            {0,0,1}
                          };
        
        for(int i=0; i<coord[0].length; i++){
            coord[0][i]+= homogen[0][2]; //Eje x
            coord[1][i]+= homogen[1][2]; //Eje y
        }   
        return coord;
    }    
    
    public void dda(int x1,int y1,int x2,int y2, Color c)
    {
        float ax,ay,x,y,res;
        int i;
        
        if(abs(x2-x1)>=abs(y2-y1)){
            //si la variacion en x es mayor o igual que la variacion en y
            res=abs(x2-x1);
        }
        else{
            //si la variacion en y es mayor que la variacion en x
            res=abs(y2-y1);
        }
        
        ax=(x2-x1)/res;//el valor a aumentar en x
        ay=(y2-y1)/res;//el valor a aumentar en y

        //se realiza casteo a float porque los resultados de la división es un real
        x=(float)x1;
        y=(float)y1;

        i=0;
        while(i<=res){
            putPixel(round(x),round(y),c);
            x=x+ax;
            y=y+ay;
            i++;
        }
    }
    
    public void pintarFiguraT(int [][] puntos){
        dda(puntos[x][a], puntos[y][a], puntos[x][b], puntos[y][b], Color.white); //De A a B
        dda(puntos[x][a], puntos[y][a], puntos[x][d], puntos[y][d], Color.white); //De A a D
        dda(puntos[x][a], puntos[y][a], puntos[x][f], puntos[y][f], Color.white); //De A a F
        dda(puntos[x][c], puntos[y][c], puntos[x][b], puntos[y][b], Color.white); //De C a B
        dda(puntos[x][c], puntos[y][c], puntos[x][d], puntos[y][d], Color.white); //De C a D
        dda(puntos[x][c], puntos[y][c], puntos[x][o], puntos[y][o], Color.white); //De C a O
        dda(puntos[x][b], puntos[y][b], puntos[x][n], puntos[y][n], Color.white); //De B a N
        dda(puntos[x][d], puntos[y][d], puntos[x][h], puntos[y][h], Color.white); //De D a H
        dda(puntos[x][f], puntos[y][f], puntos[x][h], puntos[y][h], Color.white); //De F a H
        dda(puntos[x][f], puntos[y][f], puntos[x][e], puntos[y][e], Color.white); //De F a E
        dda(puntos[x][e], puntos[y][e], puntos[x][g], puntos[y][g], Color.white); //De E a G
        dda(puntos[x][e], puntos[y][e], puntos[x][i], puntos[y][i], Color.white); //De E a I
        dda(puntos[x][g], puntos[y][g], puntos[x][h], puntos[y][h], Color.white); //De G a H
        dda(puntos[x][g], puntos[y][g], puntos[x][k], puntos[y][k], Color.white); //De G a K
        dda(puntos[x][i], puntos[y][i], puntos[x][k], puntos[y][k], Color.white); //De I a K
        dda(puntos[x][i], puntos[y][i], puntos[x][j], puntos[y][j], Color.white); //De I a J
        dda(puntos[x][k], puntos[y][k], puntos[x][l], puntos[y][l], Color.white); //De K a L
        dda(puntos[x][j], puntos[y][j], puntos[x][l], puntos[y][l], Color.white); //De J a L
        dda(puntos[x][j], puntos[y][j], puntos[x][m], puntos[y][m], Color.white); //De J a M
        dda(puntos[x][l], puntos[y][l], puntos[x][ñ], puntos[y][ñ], Color.white); //De L a Ñ
        dda(puntos[x][m], puntos[y][m], puntos[x][ñ], puntos[y][ñ], Color.white); //De M a Ñ
        dda(puntos[x][m], puntos[y][m], puntos[x][n], puntos[y][n], Color.white); //De M a n
        dda(puntos[x][ñ], puntos[y][ñ], puntos[x][o], puntos[y][o], Color.white); //De Ñ a O
        dda(puntos[x][o], puntos[y][o], puntos[x][n], puntos[y][n], Color.white); //De O a N
        //flood(puntos[x][a]-1, puntos[y][a]+2, Color.red, Color.black);
    }
    
    public void pintarCubo(int[][] puntos){
        dda(puntos[x][a], puntos[y][a], puntos[x][b], puntos[y][b], Color.white); //De A a B
        dda(puntos[x][a], puntos[y][a], puntos[x][c], puntos[y][c], Color.white); //De A a C
        dda(puntos[x][a], puntos[y][a], puntos[x][e], puntos[y][e], Color.white); //De A a E
        dda(puntos[x][b], puntos[y][b], puntos[x][f], puntos[y][f], Color.white); //De B a F
        dda(puntos[x][b], puntos[y][b], puntos[x][d], puntos[y][d], Color.white); //De B a D
        dda(puntos[x][c], puntos[y][c], puntos[x][d], puntos[y][d], Color.white); //De C a D
        dda(puntos[x][c], puntos[y][c], puntos[x][g], puntos[y][g], Color.white); //De C a G
        dda(puntos[x][d], puntos[y][d], puntos[x][h], puntos[y][h], Color.white); //De D a H
        dda(puntos[x][e], puntos[y][e], puntos[x][f], puntos[y][f], Color.white); //De E a F
        dda(puntos[x][e], puntos[y][e], puntos[x][g], puntos[y][g], Color.white); //De E a G
        dda(puntos[x][f], puntos[y][f], puntos[x][h], puntos[y][h], Color.white); //De F a H
        dda(puntos[x][g], puntos[y][g], puntos[x][h], puntos[y][h], Color.white); //De G a H
        //flood(puntos[x][e]+1, puntos[y][e]+1, Color.RED, Color.WHITE);
    }
    
    public void pintarTriangulo(int[][] puntos){
        dda(puntos[x][a], puntos[y][a], puntos[x][b], puntos[y][b], Color.white); //De A a B
        dda(puntos[x][a], puntos[y][a], puntos[x][c], puntos[y][c], Color.white); //De A a C
        dda(puntos[x][a], puntos[y][a], puntos[x][d], puntos[y][d], Color.white); //De A a D
        dda(puntos[x][b], puntos[y][b], puntos[x][c], puntos[y][c], Color.white); //De B a C
        dda(puntos[x][b], puntos[y][b], puntos[x][d], puntos[y][d], Color.white); //De B a D
        dda(puntos[x][c], puntos[y][c], puntos[x][d], puntos[y][d], Color.white); //De C a D
        //flood(puntos[x][e]+1, puntos[y][e]+1, Color.RED, Color.WHITE);
    }
            
    public void parallelPJ(int[][] puntos, int[] d){
        for(int i=0; i<puntos[0].length; i++){
            for(int j=0; j<puntos.length; j++){
                switch(j){
                    case 0: //Caso x
                        puntos[j][i] = (int) puntos[j][i] - ((d[0]/d[2]) * puntos[j+2][i]);
                        break;
                    case 1: //Caso y
                        puntos[j][i] = (int) puntos[j][i] - ((d[1]/d[2]) * puntos[j+1][i]);
                        break; 
                }
            }
        }
    }
    
    public void perspectivePJ(int[][] puntos, int[] c){
            for(int i=0; i<puntos[0].length; i++){
                for(int j=0; j<puntos.length; j++){
                    switch(j){
                        case 0: //Caso x
                            puntos[j][i] = (int) (c[2]*puntos[j][i] - c[0]*puntos[j+2][i])/(c[2] - puntos[2][i]);
                            break;
                        case 1: //Caso y
                            puntos[j][i] = (int) ( (c[2]*puntos[j][i]) - (c[1]*puntos[j+1][i])) / (c[2] - puntos[2][i]);
                            break; 
                    }
                }
            }
    }
    
    public int[][] Translation3D(int[][] coord, int dx, int dy, int dz){ //Algoritmo de traslacion
        int[][] homogen = {{1,0,0,dx},
                           {0,1,0,dy},
                           {0,0,1,dz},
                           {0,0,0,1}
                           };
        
        for(int i=0; i<coord[0].length; i++){
            coord[0][i]+= homogen[0][3]; //Eje x
            coord[1][i]+= homogen[1][3]; //Eje y
            coord[2][i]+= homogen[2][3]; //Eje z
        }   
        return coord;
    }
    
    public int[][] Scale3D(int[][] coord, float factor){  //Algoritmo de escala
        float auxx, auxy, auxz;
        float[][] homogen = {
                                {factor,0,0,0},
                                {0,factor,0,0},
                                {0,0,factor,0},
                                {0,0,0,1},
                            };

        for(int i=0; i<coord[0].length; i++){
            auxx = coord[x][i];
            auxy = coord[y][i];
            auxz = coord[z][i];
            coord[0][i]= (int)(homogen[0][0]*auxx); //Eje x
            coord[1][i]= (int)(homogen[1][1]*auxy); //Eje y
            coord[2][i]= (int)(homogen[2][2]*auxz); //Eje z
        }   
        return coord;
    }
    
    public int[][] Rotate3D(int[][] coord, float anglex, float angley, float anglez){  //Algoritmo de escala
        double auxx, auxy, auxz;
        double[][] homogenx = {
                                {1,0,0,0},
                                {0,Math.cos(Math.toRadians(anglex)),Math.sin(Math.toRadians(anglex)),0},
                                {0,-Math.sin(Math.toRadians(anglex)),Math.cos(Math.toRadians(anglex)),0},
                                {0,0,0,1},
                            };

        double[][] homogeny = {
                                {Math.cos(Math.toRadians(angley)),0,-Math.sin(Math.toRadians(angley)),0},
                                {0,1,0,0},
                                {Math.sin(Math.toRadians(angley)),0,Math.cos(Math.toRadians(angley)),0},
                                {0,0,0,1},
                            };
        
        double[][] homogenz = {
                                {Math.cos(Math.toRadians(anglez)),Math.sin(Math.toRadians(anglez)),0,0},
                                {-Math.sin(Math.toRadians(anglez)),Math.cos(Math.toRadians(anglez)),0,0},
                                {0,0,1,0},
                                {0,0,0,1},
                            };

        if(anglex != 0){
            for(int i=0; i<coord[0].length; i++){
                auxx = coord[x][i];
                auxy = coord[y][i];
                auxz = coord[z][i];

                //coord[x][i]= (int)(homogenx[0][0]*coord[0][i]); //Eje x
                coord[y][i]= (int)( ( (auxy * homogenx[1][1]) + (auxz * homogenx[2][1]) ) ); //Eje y
                coord[z][i]= (int)( ( (auxy * homogenx[1][2]) + (auxz * homogenx[2][2]) ) ); //Eje z
            }  
        }
        if(angley != 0){
            for(int i=0; i<coord[0].length; i++){
                auxx = coord[x][i];
                //auxy = coord[y][i];
                auxz = coord[z][i];
                
                coord[x][i]= (int)( ( (auxx * homogeny[0][0]) + (auxz * homogeny[2][0]) ) ); //Eje x
                coord[z][i]= (int)( ( (auxx * homogeny[0][2]) + (auxz * homogeny[2][2]) ) ); //Eje z
                if(coord[x][i] != auxx){
                    coord[x][i]-= auxx - coord[x][i];
                    coord[z][i]-= auxz - coord[z][i];
                }
            }
        }
        if(anglez != 0){
            for(int i=0; i<coord[0].length; i++){
            auxx = coord[x][i];
            auxy = coord[y][i];
            auxz = coord[z][i];   
             
            coord[x][i]= (int)( ( (auxx * homogenz[0][0]) + (auxy * homogenz[1][0]) ) ); //Eje x
            coord[y][i]= (int)( ( (auxx * homogenz[0][1]) + (auxy * homogenz[1][1]) ) ); //Eje y
            //coord[2][i]= (int)(homogenx[2][2]*coord[2][i]); //Eje z
            }
        }

        return coord;
    }
    
    void flood(int x, int y, Color nc, Color vc){   //Algoritmo de relleno
        if(x<getWidth()&& y<getHeight() && x>0 && y>0){ 
            int bvc=vc.getRGB(), bnc=nc.getRGB(), bf=buffer.getRGB(x, y);
            if(x >= 0 && x < getWidth() && y >= 0 && y < getHeight() && bf == bvc && bf != bnc){       
                putPixel(x,y,nc);
                flood(x+1, y, nc, vc);
                //flood(x-1, y, nc, vc);
                flood(x, y+1, nc, vc);
                //flood(x, y-1, nc, vc);    
            }
        }
    }
    
    public void param36(double coordx, double coordy){
        double x=0,y=0,xsav=0, ysav=0, t=0, step=Math.PI/100;
        for (t=0; t<PI*2; t+=step){
            x = cos(t) + (cos(7*t)/2) + (sin(17*t)/3);
            y = sin(t) + (sin(7*t)/2) + (cos(17*t)/3);
            
            //putPixel((int) round(x*100),(int) round(y*100), Color.BLACK);
            dda((int)round(x*20+coordx),(int)round(y*20+coordy),(int) round(xsav*20+coordx),(int)round(ysav*20+coordy), Color.white);
            ysav = y;
            xsav = x;
        } 
    }
    
    public void mesh(){
        int[] x = new int[16];
        int[] y = new int[16];
        
        x[0] = 65;
        for(int i=1; i<16; i++){
            x[i] = x[i-1] + 1920/16;
        }
        
        y[0] = 1080/9;
        for(int i=1; i<16; i++){
            y[i] = y[i-1] + 1080/16;
        }

        
        int ysav=y[0],xsav=x[0];
        
        for(int i=0; i<x.length; i++){ //Pinta verticales
            ysav = y[i];
            xsav = x[i];
            for(int j=0; j<x.length; j++){
                //putPixel(x[i-1],y[j-1], Color.CYAN);
                //System.out.println("Coordenadas: "+ x[i]+" "+y[j]);
                dda(x[i],y[j],xsav,ysav, Color.DARK_GRAY);
                ysav = y[j];
                xsav = x[i];
            }
        }
        
        for(int i=0; i<y.length; i++){ //Pinta horizontales
            dda(x[0],y[i],x[x.length-1],y[i], Color.DARK_GRAY);
        }
    }
    
    public void paintComponent(Graphics g){   
        //Graphics solo se usa para pintar las strings NADAMAS, lo demas usa el metodo putpixel
        g.setColor(Color.LIGHT_GRAY);   //Color de fuente de la puntuacion
        g.setFont(new Font("Liberation Sans", Font.PLAIN, 40)); //Fuente
        g.drawString(scorest+score, 930, 100); //Pintar puntuacion
        mesh(); //Tablero decorativo
        
        //Pinta figuras recalculadas
        pintarFiguraT(figuraT);
        pintarCubo(cubo);
        pintarTriangulo(triangle);

        if(shoot==true){ //Si se dispara
            param36(tiro[x][a],tiro[y][a]); //Pinta bala con coordenadas calculadas
        }
        
        if(score==100){ //Si ganas
            g.setFont(new Font("Liberation Sans", Font.PLAIN, 100));
            g.drawString("¡Ganaste!", 800, 550); //Pintar puntuacion
            timer.stop();
        }
        
        if(cubo[x][a] < 0 || cubo[x][a] <= figuraT[x][i] && (cubo[y][a] >= figuraT[y][a]  && cubo[y][a] <= figuraT[y][a]-100)) //Si pierdes
        {
            g.setFont(new Font("Liberation Sans", Font.PLAIN, 100));
            g.drawString("Perdiste :(", 800, 550); //Pintar puntuacion
            timer.stop();
        }
    }
    
    public void paint(Graphics g){
        fondo = createImage(getWidth(),getHeight());    //Inicia IMAGEN
        Graphics gbuffer = fondo.getGraphics(); //Asigna graficos de la imagen a gbuffer
        buffer = (BufferedImage) createImage(getWidth(),getHeight()); //Crea imagen de buffer en buffer
        gbuffer = buffer.getGraphics(); // Doble buffer 
 	gbuffer.setClip(0, 0,getWidth(),getHeight()); // Inicia el clip

        paintComponent(gbuffer); //Manda componente a pintar

        this.getGraphics().drawImage(buffer, 0, 0, this); //Pinta todo junto      
    }
    
    public static void main(String[] args) {
        new Ortogonal();
    }

    @Override
    public void actionPerformed(ActionEvent e) {      
        //Calculo de trayectoria de enemigo
        if(cubo[x][a]>0){   
            Translation3D(cubo,-10,0,0); //Calcula siguiente posicion
            if( (tiro[x][a] + 50 >= cubo[x][a]) && (tiro[y][a] <= cubo[y][a]+100  && tiro[y][a] >= cubo[y][a]-100)){    //Colision con disparo 
                score+=10; //Suma puntuacion
                Translation3D(cubo,1000,getRandomNumber(-100,100),0); //Randomiza siguiente posicion
                Rotate3D(cubo,getRandomNumber(-10,10),0,getRandomNumber(-10,10)); //Rota de forma random
                
                if(cubo[y][a]-50 < 0){  //Si excede el limite superior se reajusta la posicion
                      Translation3D(cubo,0,getRandomNumber(200,500),0);
                }    
                if(cubo[y][a]+50 > 1080){  //Si excede el limite inferior se reajusta la posicion
                      Translation3D(cubo,0,getRandomNumber(-200,-500),0);
                } 
                
            }
        }
        
        //Calulo de trayectoria del disparo
        if(shoot==true){    
            Translation2D(tiro,20,0);   //Movimiento a la izquierda 
            if(tiro[x][a]>=1920 || tiro[x][a] >= cubo[x][a]-10 && (tiro[y][a] >= cubo[y][a]+100  && tiro[y][a] <= cubo[y][a]-100)){ //Detecta colision
                tiro[x][a]=0;   //Resetea
                tiro[y][a]=0;   //Resetea
                shoot=false;    //Manda decir que no hay disparo para que no se pinte
            }
        }
        
        repaint();
    }
}
