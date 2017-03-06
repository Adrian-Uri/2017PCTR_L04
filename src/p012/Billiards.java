package p012;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("serial")
public class Billiards extends JFrame {

	private static ExecutorService e;
	
	private int contaBolas=0;
	
	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;

	private final int N_BALL = 4 + 3;
	private Ball[] balls;

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
		e=Executors.newCachedThreadPool();
	}

	private void initBalls() {
		balls=new Ball[N_BALL];
		for (int i = 0; i < N_BALL; i++) {
			balls[i]=new Ball();
		}
	}

	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when start button is pushed
			if(contaBolas < N_BALL){
			e.submit(new Hilo(balls[contaBolas]));
			contaBolas ++;
			}else{
				System.out.println("no se pueden lanzar mas bolas");
			}
		}
	}

	private class Hilo implements Runnable{
		private Ball mibola;
		
		public Hilo(Ball bola){
			mibola=bola;
		}
		
		@Override
		public void run() {
			while(true){
				mibola.move();
				mibola.reflect();
				board.setBalls(balls);
				board.paint(getGraphics());
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when stop button is pushed
			e.shutdown();
		}
	}

	public static void main(String[] args) {
		new Billiards();
	}
}