import java.awt.*;
import java.util.HashSet;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class PacMan extends JPanel implements ActionListener, KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startx;
        int starty;
        char direction = 'U';
        int velocityX =0;
        int velocityY =0;


        Block(Image image , int x, int y, int width, int height){
           this.image =  image;
           this.x = x;
           this.y = y;
           this.width = width;
           this.height = height;
           this.startx = x;
           this.starty = y;
        }

        void updateDirection(char direction){
            char prevdirection = this.direction;
            this.direction = direction;
            updatevelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for(Block wall :walls){
                if(collison(this,wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevdirection ;
                    updatevelocity();
                }
            }
        }

        void updatevelocity(){
           if(this.direction =='U'){
            this.velocityX = 0;
            this.velocityY = -tilesize/4;
           } 
           else if (this.direction =='D'){
            this.velocityX = 0;
            this.velocityY = tilesize/4;
           }
           else if(this.direction == 'L'){
            this.velocityX = -tilesize/4;;
            this.velocityY = 0;
           }
           else if(this.direction =='R'){
            this.velocityX = tilesize/4;;
            this.velocityY = 0;
           }
        }

        void reset(){
            this.x = this.startx;
            this.y = this.starty;
        }
    }
private int rowcount = 21;
private int columncount = 19;
private int tilesize = 32;
private int boardwidth = columncount * tilesize;
private int boardheight = rowcount * tilesize;

private Image wallimg;
private Image blueghostimg;
private Image orangeghostimg;
private Image pinkghostimg;
private Image redghostimg;

private Image pacmanupimg;
private Image pacmandownimg;
private Image pacmanleftimg;
private Image pacmanrightimg;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X  bpo  X XOOO",
        "XXXX X XXrXX X XXXX",
        "O                 O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

HashSet<Block> walls;
HashSet<Block> foods;
HashSet<Block> ghosts;
Block pacman;

Timer gameloop;
char [] directions = {'U' , 'D' , 'L' ,'R'};
Random random = new Random();
int score = 0;
int lives = 3;
boolean gameover = false;



PacMan() {
    setPreferredSize(new Dimension(boardwidth,boardheight) );
    setBackground(Color.BLACK);
    addKeyListener(this);
    
    //load images
    wallimg = new ImageIcon(getClass().getResource("./wall.png")).getImage();
    blueghostimg = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
    orangeghostimg = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
    pinkghostimg = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
    redghostimg = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

    pacmanupimg = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
    pacmandownimg = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
    pacmanleftimg = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
    pacmanrightimg = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

    loadMap();
    for(Block ghost : ghosts){
        char newdirections = directions[random.nextInt(4)];
        ghost.updateDirection(newdirections);
    }
    // how long it takes to start timer, millisecond gone between frames
    gameloop = new Timer(50,this);// 20 frames per second (1000/50)
    gameloop.start();
    }
    public void loadMap(){
       walls = new HashSet<Block>();
       foods = new HashSet<Block>();
       ghosts = new HashSet<Block>();

       for(int r=0; r < rowcount;r++){
           for(int c = 0; c<columncount; c++){
            String row = tileMap[r];
            char tileMapChar = row.charAt(c);

            int x = c*tilesize;
            int y = r*tilesize;

            if(tileMapChar == 'X'){
                Block wall = new Block(wallimg , x, y, tilesize, tilesize);
                walls.add(wall);
            }
            else if(tileMapChar == 'b'){ //blueghost
                Block ghost =  new Block(blueghostimg , x, y,tilesize,tilesize);
                ghosts.add(ghost);
            }
            else if(tileMapChar == 'o'){ //orangeghost
                Block ghost =  new Block(orangeghostimg , x, y,tilesize,tilesize);
                ghosts.add(ghost);
            }
            else if(tileMapChar == 'p'){ //pinkghost
                Block ghost =  new Block(pinkghostimg , x, y,tilesize,tilesize);
                ghosts.add(ghost);
            }
            else if(tileMapChar == 'r'){ //redghost
                Block ghost =  new Block(redghostimg , x, y,tilesize,tilesize);
                ghosts.add(ghost);
            }
            else if(tileMapChar == 'P'){ //blueghost
                pacman =  new Block(pacmanrightimg , x, y,tilesize,tilesize);
            }
            else if(tileMapChar == ' '){ //food
                Block food = new Block(null,x+14,y+14,4,4);
                foods.add(food);
            }
            }
           }
       }
       public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
       }

       public void draw(Graphics g){
        g.drawImage(pacman.image,pacman.x,pacman.y,pacman.width,pacman.height,null);

        for(Block ghost : ghosts){
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
         }

         for(Block wall : walls){
            g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);
         }
         g.setColor(Color.WHITE);
         for(Block food : foods){
            g.fillRect(food.x,food.y,food.width,food.height);

         }

         //score
         g.setFont(new Font("Arial", Font.PLAIN,18));
         if(gameover){
            g.drawString("Game Over : "+ String.valueOf(score),tilesize/2,tilesize/2);  
         }
         else{
            g.drawString("x "+String.valueOf(lives)+ "score: "+ String.valueOf(score),tilesize/2,tilesize/2);
         }
       }

       public void move(){
        pacman.x +=pacman.velocityX;
        pacman.y +=pacman.velocityY;

        // check wall collison 
        for(Block wall :walls){
            if(collison(pacman,wall)){
                pacman.x -=pacman.velocityX;
                pacman.y -=pacman.velocityY;
                break;
            }
        }

       for (Block ghost : ghosts) {

    // collision with pacman
    if (collison(ghost, pacman)) {
        lives--;
        if(lives == 0){
            gameover = true;
            return;
        }
        resetpositions();
        return;
    }

    // move ghost
    ghost.x += ghost.velocityX;
    ghost.y += ghost.velocityY;

    // wall collision
    boolean hitWall = false;
    for (Block wall : walls) {
        if (collison(ghost, wall)) {
            hitWall = true;
            break;
        }
    }

    if (hitWall ||
        ghost.x < 0 ||
        ghost.x + ghost.width > boardwidth ||
        ghost.y < 0 ||
        ghost.y + ghost.height > boardheight) {

        // undo move
        ghost.x -= ghost.velocityX;
        ghost.y -= ghost.velocityY;

        // pick new direction
        char newDir = directions[random.nextInt(4)];
        ghost.direction = newDir;
        ghost.updatevelocity();
    }
}



         // check food collison
         Block foodeaten = null;
         for(Block food :foods){
            if (collison(pacman,food)){
                foodeaten = food;
                score +=10;
            }
         }
         foods.remove(foodeaten);

         if(foods.isEmpty()){
            loadMap();
            resetpositions();
         }
       }

       public boolean collison(Block a,Block b){
         return a.x<b.x + b.width &&
         a.x+ a.width >b.x &&
         a.y < b.y +b.height &&
         a.y + a.height> b.y;
       }

       public void resetpositions(){
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;
        for(Block ghost :ghosts){
            ghost.reset();
            char newdirections = directions[random.nextInt(4)];
            ghost.updateDirection(newdirections);
        }
       }

       @Override
       public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover){
            gameloop.stop();
        }
       }

       @Override
       public void keyTyped(KeyEvent e){}

       @Override
       public void keyPressed(KeyEvent e){}

       @Override
       public void keyReleased(KeyEvent e){
        if(gameover){
           loadMap();
           resetpositions();
           lives = 3;
           score = 0;
           gameover = false;
           gameloop.start();
        }
    
        //System.out.println("Key event :"+e.getKeyCode());
       if(e.getKeyCode() == KeyEvent.VK_UP){
        pacman.updateDirection('U');
       }
       else if(e.getKeyCode() == KeyEvent.VK_DOWN){
        pacman.updateDirection('D');
       }
       else if(e.getKeyCode() == KeyEvent.VK_LEFT){
        pacman.updateDirection('L');
       }
       else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
        pacman.updateDirection('R');
       }

       if(pacman.direction =='U'){
        pacman.image = pacmanupimg;
       }
       else if(pacman.direction =='D'){
        pacman.image = pacmandownimg;
       }
       else if(pacman.direction =='L'){
        pacman.image = pacmanleftimg;
       }
       else if(pacman.direction =='R'){
        pacman.image = pacmanrightimg;
       }
       }
}
