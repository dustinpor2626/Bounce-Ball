import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
import java.util.Random;
import java.net.URL;
class CatchBall extends JFrame
{
 JLabel la=new JLabel(new ImageIcon(getClass().getResource("images/background.jpg")));
 AudioClip clip1,clip2;
 JLabel bar=new JLabel();
 int sw,sh,nb=0,sb,wb=0,eb,count1=0,count2=0,barx,bary,ballx=100,bally=0,totalball,barw=150,barh=30,x,y,remainingball,th;
 JLabel []ball;
 JLabel hit=new JLabel("Hit : "+ count1);
 JLabel miss=new JLabel("Miss : "+ count2);
 Random ran=new Random();
 boolean started=false,escape=false; 
 JLabel activeball; 
 JLabel startpanel=new JLabel();
 JLabel panelbackground;
 JButton startgame;
 BarListener barlistener=new BarListener();
 final JCheckBox cb;
 public CatchBall()
 {
  	super("bouncing ball");
  	loadSound();
  	Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
  	sw=d.width;sh=d.height;sb=sh-38;eb=sw-18;
  	setSize(sw,sh);
  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  	barx=(sw-barw)/2;
  	bary=sb-110;
  	la.setLayout(null);
	cb=new JCheckBox("MUTE");
  	addStartPanel();
  	addScore();
  	addfire();	
  	addBar();
  	addBall();
  	addKeyListener(new MoveBarListener());
  	setVisible(true);
 }
 private void addStartPanel()
 {
   	int px=(sw-700)/2;
   	int py=(sh-550)/2;
   	startpanel.setBounds(px,py,700,550);
   	startpanel.setBackground(new Color(100,250,98));
   	startpanel.setLayout(new BorderLayout());
   	startpanel.add(panelbackground=new JLabel(new ImageIcon(getClass().getResource("images/start3.jfif"))));
   	la.add(startpanel);
   	addPanelButton();	
   	addMute();
 }
 void addPanelButton()
 {
	JPanel p1=new JPanel();
	startpanel.add(p1,"North");
	startgame=new JButton("START GMAE");	
	startgame.setFocusable(false);
	startgame.setForeground(Color.red);
	startgame.setBackground(Color.yellow);
	startgame.setFont(new Font("arial",1,23));
	p1.add(startgame);
	startgame.addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent evt)
	  {
		startpanel.setVisible(false);
		escape=false;
		addMouseMotionListener(barlistener);
		new ThrowBall().start();
    	  }
	});
 }
 private void addMute()
 {
	JPanel p2=new JPanel();
	startpanel.add(p2,"South");
	cb.setFocusable(false);
	cb.setFont(new Font("cooper",1,25));
	cb.setForeground(Color.blue);
        p2.add(cb);
	cb.setSelected(true);
        cb.addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent evt)
	  {
		if(cb.isSelected())
		  clip2.stop();
		else
		  clip2.loop();	
	  }
	});
 }
 private void addBar()
 {
  	bar.setBounds(barx,bary,barw,barh);
  	bar.setBorder(BorderFactory.createLineBorder(Color.yellow));
  	bar.setOpaque(true);
  	bar.setBackground(Color.red);
  	la.add(bar);
 }
 private void addScore()
 {
  	int sx=(sw-600)/2;	
  	hit.setBounds(sx,250,300,100);
  	miss.setBounds(sx+300,250,300,100);
  	hit.setForeground(Color.green);
  	miss.setForeground(Color.red);
  	hit.setFont(new Font("jokerman",1,50));
  	miss.setFont(new Font("jokerman",1,50));
  	this.add(la);
  	la.add(hit);la.add(miss);
 }
 private void addfire()
 {
    for(int c=0;c<=sw;c+=45) 
    {	
	JLabel fire=new JLabel(new ImageIcon(getClass().getResource("images/fire.gif")));
	fire.setBounds(c,sb-78,45,78);
	la.add(fire);
    }
 }
 private void loadSound()
 {
   try
   {
       clip1=Applet.newAudioClip(getClass().getResource("/sound/SOUND136.wav"));
       clip2=Applet.newAudioClip(getClass().getResource("/sound/intro.mid"));
   }catch(Exception ex){}
 } 
 private void addBall()
 {
  totalBall();
  ball=new JLabel[totalball];
  for(int i=0;i<ball.length;i++)
  {
	ball[i]=new JLabel(new ImageIcon(getClass().getResource("images/ball.png")));
	ball[i].setBounds(ballx+=30,bally,30,30);
	la.add(ball[i]);
  }	
 } 
 private void totalBall()
 {
	int x=eb-200;
	totalball=x/30;
	remainingball=totalball;
	th=totalball/2;
 } 
 class MoveBarListener extends KeyAdapter
 {
  public void keyPressed(KeyEvent evt) 
  {
        int kc=evt.getKeyCode();
	if(kc==KeyEvent.VK_ESCAPE && !escape)
	{
	  escape=true;
	  resetGame();
	  startpanel.setVisible(true);
	}
  }
 }
 void resetGame()
 {
	count1=count2=0;
	ballx=100;
	for(int i=0;i<ball.length;i++)
	{
	  if(ball[i]==null)
	  {
	    ball[i]=new JLabel(new ImageIcon(getClass().getResource("images/ball.png")));
	    la.add(ball[i]);
	  }
	  ball[i].setBounds(ballx+=30,0,30,30);
	}
  	clip2.stop();
	cb.setSelected(true);
	hit.setText("Hit : "+ count1);
	miss.setText("Miss : "+ count1);
	removeMouseMotionListener(barlistener);
	barx=(sw-barw)/2;
  	bary=sb-110;
	remainingball=totalball;
 }
 class BarListener extends MouseMotionAdapter
 {
   public void mouseMoved(MouseEvent evt)
   {
	barx=evt.getX();
	bar.setBounds(barx,bary,barw,barh);
   }
 }
 class ThrowBall extends Thread
 {
  public void run()
  {
    boolean dirright=false;
    if(remainingball==0)
    {
     JOptionPane.showMessageDialog(CatchBall.this,"Gave Over");	
     return;
    }
    int balli=0;
    do
    {
      balli=new java.util.Random().nextInt(totalball);
    }while(ball[balli]==null);
    activeball=ball[balli];
    ball[balli]=null;
    x=activeball.getX();	
    y=activeball.getY();
    remainingball--;
    dirright=(balli<=th)?true:false;
    while(true)
    {
	if(escape)
        {
	  la.remove(activeball);
	  break;
	}
	if(dirright)
        {
	  x+=2;
	  if(x>=eb-30)
	  {
		dirright=false;
          }
        }
	if(!dirright)
        {
	  x-=2;
	  if(x<=wb)
	  {
		dirright=true;
          }
        }
	y+=3;
	activeball.setBounds(x,y,30,30);
	try{
	  Thread.sleep(3);
	}catch(Exception ex){}
	if(isCollision())
        {
	  clip1.play();
	  hit.setText("Hit : "+ (++count1));
	  activeball.setVisible(false);
	  new ThrowBall().start();
	  return;
        } 
	else if(y>=sb)	
	{
	 activeball.setVisible(false);
	 miss.setText("Miss : "+ (++count2));
	 new ThrowBall().start();
	 return;
	}
    }   	
  }   	
 } 
 boolean isCollision()
 {
   int c=x-barx;
   int d=bary-y;
   if((d>=25 && d<=29) && (c>=-31 && c<=barw))
    return true;
   return false;
 } 
 public static void main(String args[])
 {
	JFrame.setDefaultLookAndFeelDecorated(true);
	SwingUtilities.invokeLater(new Runnable() {
        public void run() 
	{
            new CatchBall();
        }
	});
 } 
} 
