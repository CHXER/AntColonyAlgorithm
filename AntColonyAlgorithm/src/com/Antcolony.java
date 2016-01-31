package com;
/*
 * @(#)Antcolony.java 1.0 03/05/22
 *
 * You can modify the template of this file in the
 * directory ..\JCreator\Templates\Template_2\Project_Name.java
 *
 * You can also create your own project template by making a new
 * folder in the directory ..\JCreator\Template\. Use the other
 * templates as examples.
 *
 */

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.Vector;

public class Antcolony extends Applet implements Runnable {
	//��Щ����������Ҫ�õ��ı���
	//��ͼ
	static int width = 300, height = 300;	// �����ĳ��Ϳ�
	//����ʳ���λ��
	static Point originPt[];				// �ѵ���Ϣ
	static int originCount = 1;				// �ѵĸ��������Ϊ100
	static Point endPt[];					// ʳ������飬ֵΪʳ������ꡣ
	static int endCount = 1;					// ʳ���ĸ�������ʼ��ʱ��Ϊ1�������Ϊ100
	//�ϰ���
	static int obsGrid[][];					// �ϰ����������飬���Ǹ�width*height�ľ��������д洢�����ϰ������飨obsP[]����ָ�꣬���������Լӿ��������ٶȡ�����õ�û���ϰ����Ϊ-1
	static Point obsP[];					// �ϰ������飬�洢���ǵ����Ϣ��ָ�����ϰ��������
	static int obsCount;					// �ϰ�������������Ϊwidth*height
	//��Ϣ��
	static int pheromoneGrid[][][];			// ��Ϣ���������飬2*width*height����ά���󣬵�һά����Ϣ�����ࣨ�ѵ���Ϣ��Ϊ0��ʳ���Ϊ1�������洢������Ϣ�ص������ֵ
	static Vector<Pheromone> phe;			// ��Ϣ���������൱��һ�����飩��������������Ϣ�ص�ʱ��ֻ��Ҫ������������Ϳ����ˣ�������������width*height��ô���pheromoneGrid�����
	static int maxPheromone = 500000;		// �����Ϣ����ֵ��Ӧ�ø��ݵ�ͼ�ĸ��ӳ̶�������Խ����Խ��
	static int delimiter = 5;				// ��Ϣ����ɢ�����ʣ�Ϊ������Խ������ɢ��Խ��
	static int foodR = 20;					// ʳ����Ѳ����ݶȵ���Ϣ�صİ뾶
	
	
	static Ant ants[];						// ��������
	static int antCount;					// ���ϵ�����
	private static final long serialVersionUID = 1L;
	static int drawPhe = 2;					// ����Ϣ�ص�ģʽ��0Ϊ������1Ϊ�����е���Ϣ��,2Ϊ��ʳ�����Ϣ�أ�3Ϊ���ѵ���Ϣ��
	AntCanvas canvas = new AntCanvas();		// ��ͼ�õĻ���
	boolean isStandalone = false;			// ϵͳ�Ĳ������Ƿ�������У����ù�
	Thread runner;							// ����һ���̣߳��ö���ƽ��������
	boolean running;						// �Ƿ��ö�������
	boolean reset = false;					// �Ƿ��������ð�ť
	static Color OBS_COLOR = Color.red;		// �ϰ������ɫ
	static Color ORIGIN_COLOR = Color.yellow;// �ѵ���ɫ
	static Color BACK_COLOR = Color.black;	// ����ɫ
	static Color ANT_COLOR = Color.white;	// ���ϵ���ɫ
	static Color End_COLOR = Color.cyan;	// ʳ������ɫ
	static int delay = 10;					// ÿ�����еļ�����ʣ�ԽС��������Խ�죨�����������û�ã���Ϊ�����϶����Ժ󣬴�����̺ܺ�ʱ�䣩

	// ������һЩ�ؼ���Ϣ
	Button btnStart = new Button("��ʼ");
	Button btnReset = new Button("����");
	Button btnMap = new Button("�༭��ͼ");
	Button btnConfig = new Button("����");
	Choice choPDraw = new Choice();

	public void init() {
		// ��ʼ���������Ȼ����ֿؼ�
		setLayout(new BorderLayout());
		Panel pan = new Panel();
		add("South", pan);
		this.add("Center", canvas);
		pan.add(btnStart);
		pan.add(btnReset);
		pan.add(btnConfig);
		pan.add(btnMap);
		pan.add(choPDraw);
		choPDraw.addItem("������Ϣ��");
		choPDraw.addItem("��������Ϣ��");
		choPDraw.addItem("��ʳ����Ϣ��");
		choPDraw.addItem("���ѵ���Ϣ��");
		choPDraw.select(2);

		// ��ʼ����������
		obsGrid = new int[width][height];
		phe = new Vector<Pheromone>();
		pheromoneGrid = new int[2][width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				obsGrid[i][j] = -1;
				for (int k = 0; k < 2; k++) {
					pheromoneGrid[k][i][j] = 0;
				}
			}
		}

		antCount = 50;// ���ϸ���ȱʡΪ50
		// ��ʼ�����ϣ���Щ���Զ������ϵ���ԭʼ������
		ants = new Ant[antCount];
		for (int i = 0; i < antCount; i++) {
			ants[i] = new Ant(new Point(0, 0), 3, i, this, ANT_COLOR, 0.001, 50);
		}

		// ����װ��ȱʡ�ĵ�ͼ�������ϰ��ʳ��㡢�ѵ��λ�ã����ŵ�����grid[][]��Ȼ�󽻸�init_map����ͳһ����
		int grid[][] = new int[width][height];

		// ����ӵ�ͼ���м��ڵ�ͼ
		Maps.loadMap(grid, 0);
		// ��ʼ����ͼ
		reinit_map(grid);

		// ��ʼ�����е�����
		reinit();
	}

	public void reinit_map(int grid[][]) {
		// ������grid[][]�д洢����Ϣת������ǰ�Ļ������ݽṹ��
		// �൱�ڰ�һ��λͼ��Ϣwidth*height����ת�����ѡ�ʳ��ϰ���

		// ��ֹͣ���������
		running = false;
		btnStart.setLabel("��ʼ");

		obsCount = 0;
		endCount = 0;
		originCount = 0;
		obsP = new Point[width * height];
		originPt = new Point[100];
		endPt = new Point[100];

		// ���obs_grid��Pheromone���������е�ֵ
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				obsGrid[i][j] = -1;
				for (int k = 0; k < 2; k++) {
					pheromoneGrid[k][i][j] = 0;
				}
			}
		}

		// ��grid�����ж�ȡ��Ϣ
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				switch (grid[i][j]) {
				case 1:
					// ���grid[][]������ϰ���
					obsGrid[i][j] = obsCount;
					obsP[obsCount] = new Point(i, j);
					obsCount++;
					break;
				case 2:
					// ���grid[][]����ѵ���Ϣ��������ѵ���Ϣʡȥ��
					if (originCount < 100) {
						originPt[originCount] = new Point(i, j);
						originCount++;
					}
					break;
				case 3:
					// ���grid[][]���ʳ�����Ϣ�������ʳ�����Ϣʡȥ��
					if (endCount < 100) {
						endPt[endCount] = new Point(i, j);
						endCount++;
					}
					break;
				}
			}
		}
		// ���û��ָ���ѣ��������ѡ��һ��
		if (originCount == 0) {
			for (int i = 0; i < width; i++) {
				int j;
				for (j = 0; j < height; j++) {
					if (obsGrid[i][j] < 0) {
						originPt[originCount] = new Point(i, j);
						originCount++;
						break;
					}
				}
				if (j < height - 1) {
					break;
				}
			}
		}
	}

	public void reinit() {
		// ���³�ʼ����������

		// ��ֹͣ���������
		running = false;
		btnStart.setLabel("��ʼ");

		// ���������Ϣ��Pheromone�����е�ֵ
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < 2; k++) {
					pheromoneGrid[k][i][j] = 0;
				}
			}
		}

		// ��ʼ���������飬antCountֻ�����ڲ�ͬ���ѵ�֮���������ķ���
		for (int i = 0; i < antCount; i++) {
			int index = (int) (originCount * Math.random());
			ants[i].originPt = new Point(originPt[index]);
			ants[i].init();
		}

		// �����Ϣ������
		phe.removeAllElements();

		// ��ÿ��ʳ�����ѵ���Χ�ֲ�һ�����İ����ݶȵݼ�����Ϣ�أ��������һ����Ϊ���ĵİ뾶ΪFoodR��Բ��������Ϣ�ذ��հ뾶�ݼ�
		for (int i = 0; i < endCount; i++) {
			for (int x = -foodR; x <= foodR; x++) {
				int y = (int) (Math.sqrt(foodR * foodR - x * x));
				for (int yy = -y; yy <= y; yy++) {
					pheromoneGrid[1][(endPt[i].x + x + width) % width][(endPt[i].y + yy + height)
							% height] = (int) (1000 * (1 - Math.sqrt(x * x + yy * yy) / foodR));
				}
			}
		}
		for (int i = 0; i < originCount; i++) {
			for (int x = -foodR; x <= foodR; x++) {
				int y = (int) (Math.sqrt(foodR * foodR - x * x));
				for (int yy = -y; yy <= y; yy++) {
					pheromoneGrid[0][(originPt[i].x + x + width) % width][(originPt[i].y + yy + height)
							% height] = (int) (1000 * (1 - Math.sqrt(x * x + yy * yy) / foodR));
				}
			}
		}

		// �ػ�
		canvas.repaint();

		// �ó���ʼ����
		// running=true;
	}

	public void paint(Graphics g) {
		canvas.repaint();
	}

	public void start()
	// �������������ǿ����̵߳�
	{
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
			// running = true;
		}
	}

	public void stop() {
		if (runner != null) {
			runner.interrupt();
			runner.stop();
			runner = null;
			running = false;
		}
	}

	public void run() {
		// �߳�һֱ������ȥ
		while (true) {
			if (running) {
				// �����ʼ�������ͽ���canvas�Ĵ���
				canvas.process();
			}
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}

	}

	public boolean action(Event evt, Object o) {
		if (evt.target == btnMap) {
			// ��ʼ�༭��ͼ���
			running = false;
			btnStart.setLabel("��ʼ");
			MapPad ctl = new MapPad(this);
			ctl.setSize(500, 500);
			ctl.setVisible(true);
			return true;
		} else if (evt.target == btnStart) {
			if (!running) {
				// ����ոհ��������ð�ť�����³�ʼ��һ��
				if (reset)
					reinit();
				btnStart.setLabel("ֹͣ");
				reset = false;
				running = true;
			} else {
				btnStart.setLabel("��ʼ");
				running = false;
			}
			return true;
		} else if (evt.target == btnReset) {
			running = false;
			// ��ʾ�Ѿ����������ð�ť���Ա��´ο�ʼ��ʱ��������³�ʼ��
			reset = true;
			repaint();
			btnStart.setLabel("��ʼ");
			return true;
		} else if (evt.target == btnConfig) {
			running = false;
			btnStart.setLabel("��ʼ");
			Configer ctl = new Configer(this);
			ctl.setSize(300, 300);
			ctl.setVisible(true);
			return true;
		} else if (evt.target == choPDraw) {
			// ѡ����Ϣ�ص�ģʽ
			drawPhe = choPDraw.getSelectedIndex();
			if (drawPhe != 1) {
				canvas.repaint();
			}
			return true;
		}
		return false;

	}

	/** Destroy the applet */
	public void destroy() {
		// �����������ʱ�򣬰��߳�Ҳ����
		if (runner != null) {
			running = false;
			runner.stop();
			runner = null;
		}
	}
	
	public static void main(String[] args) {
		Antcolony applet = new Antcolony();
		applet.isStandalone = true;
		Frame frame;
		frame = new Frame() {
			private static final long serialVersionUID = 1L;
			protected void processWindowEvent(WindowEvent e) {
				super.processWindowEvent(e);
				if (e.getID() == WindowEvent.WINDOW_CLOSING) {
					System.exit(0);
				}
			}

			public synchronized void setTitle(String title) {
				super.setTitle(title);
				enableEvents(AWTEvent.WINDOW_EVENT_MASK);
			}
		};
		frame.setTitle("Applet Frame");
		frame.add(applet, BorderLayout.CENTER);
		applet.init();
		applet.start();
		frame.setSize(500, 500);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
		frame.setVisible(true);
	}

}
