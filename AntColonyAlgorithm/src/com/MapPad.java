package com;

import java.awt.event.*;
import java.awt.*;

/**
 * Title: ant Description: Copyright: Copyright (c) 2003 Company:
 * agents.yeah.net
 * 
 * @author jake
 * @version 1.0
 */

// ����ļ�����༭��ͼ
class MapCanvas extends Canvas {
	// ��ͼ�Ļ�����
	MapPad localpad;// ���ͼ�༭�����ؽ��潻������
	Graphics pang;// ��Ҫ��ͼ����ͼ
	int width, height;// ��ͼ�ĳ��ȺͿ��

	// ��¼��ͼʱ��Ҫ��һЩ�����Ϣ
	Point startPt = new Point(-1, -1);// �����Ϊ���հ���ʱ�������
	Point firstPt = new Point(0, 0);// �������ʵ���ϸ���һ����ֻ�����������������ͼ��ʱ��������
	Point lastPt = new Point(0, 0);// ����¼��������������ʱ��ո��Ϲ��ĵ��λ�ã�Ϊ���ܹ�Ĩȥ����Ҫ��ͼ

	MapCanvas(MapPad pad) {
		// ��ʼ��һЩ����
		localpad = pad;
		width = localpad.local.width;
		height = localpad.local.height;
		this.setBackground(localpad.local.BACK_COLOR);
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				this_mouseMoved(e);
			}

			public void mouseDragged(MouseEvent e) {
				this_mouseDragged(e);
			}
		});
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				this_mousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				this_mouseReleased(e);
			}

			public void mouseDragged(MouseEvent e) {
				this_mouseDragged(e);
			}

			public void mouseClicked(MouseEvent e) {
				this_mouseClicked(e);
			}

		});

	}

	void this_mousePressed(MouseEvent e) {
		// ���հ��¾ͼ�¼��ʼ�㣬���ж�������Ƿ񳬽�
		startPt = new Point(e.getX(), e.getY());
		int x = startPt.x, y = startPt.y;
		if (startPt.x < 0)
			x = 0;
		if (startPt.y < 0)
			y = 0;
		if (startPt.x >= width)
			x = width - 1;
		if (startPt.y >= height)
			y = height - 1;
		startPt = new Point(x, y);
	}

	void this_mouseDragged(MouseEvent e) {
		// ��������¼���ֻ�е����ϰ��������ֱ�ߡ����Ρ�Բ��ʱ����з����˳�
		int x2 = e.getX(), y2 = e.getY();
		if (x2 < 0 || x2 >= width || y2 < 0 || y2 >= height)
			return;

		if (localpad.Kind == 0 && localpad.Shape == 3) {
			// ����ǻ��ϰ���ĵ㣬������㣬ֻ���������2
			pang.setColor(localpad.KindColor[localpad.Kind + 1]);
			pang.fillRect(x2, y2, 2, 2);

			localpad.grid[x2][y2] = localpad.Kind + 1;
			if (x2 + 1 >= width)
				x2 = x2 - 1;
			if (y2 + 1 >= height)
				y2 = y2 - 1;
			localpad.grid[x2 + 1][y2] = localpad.Kind + 1;
			localpad.grid[x2][y2 + 1] = localpad.Kind + 1;
			localpad.grid[x2 + 1][y2 + 1] = localpad.Kind + 1;
			return;
		}
		if (localpad.Kind != 0 || localpad.Shape == 3)
			return;
		if (startPt.x < 0 || startPt.y < 0)
			return;
		int x1 = startPt.x, y1 = startPt.y;
		if (x2 < x1) {
			x1 = x2;
			x2 = startPt.x;
		}
		if (y2 < y1) {
			y1 = y2;
			y2 = startPt.y;
		}
		int xx1, yy1, xx, yy;
		xx = lastPt.x;
		yy = lastPt.y;
		xx1 = firstPt.x;
		yy1 = firstPt.y;
		if (xx < xx1) {
			xx1 = xx;
			xx = firstPt.x;
		}
		if (yy < yy1) {
			yy1 = yy;
			yy = firstPt.y;
		}

		// ����ǻ�ֱ�ߣ��Ȱ���һ�㻭����ֱ�߲�����Ҳ�����ñ���ɫ�ػ�һ��ղŵ�ֱ��
		// ������ǻ�ֱ�ߣ�������ղŻ����ľ��Σ�������£��������Բ��Ҳ������һ����ľ�����ס��
		if (localpad.Shape == 2) {
			pang.setColor(localpad.local.BACK_COLOR);
			pang.drawLine(firstPt.x, firstPt.y, lastPt.x, lastPt.y);
		} else
			pang.clearRect(xx1, yy1, Math.abs(xx - xx1), Math.abs(yy - yy1));

		// �������Ǹ������Ժ���Ҫ��ԭ���е�ͼ����Ϣ���»���������Ϊ���������ʱ�򲢲���������ͼ
		for (int i = xx1; i <= xx; i++) {
			for (int j = yy1; j <= yy; j++) {
				if (localpad.grid[i][j] > 0) {
					pang.setColor(localpad.KindColor[localpad.grid[i][j]]);
					if (localpad.grid[i][j] > 1)
						pang.fillRect(i, j, 2, 2);
					pang.fillRect(i, j, 1, 1);
				}
			}
		}
		// ������״�Ĳ�ͬ��ѡ��ͼ����
		if (localpad.Shape == 4)
			pang.setColor(localpad.local.BACK_COLOR);
		else
			pang.setColor(localpad.local.OBS_COLOR);
		switch (localpad.Shape) {
		case 0:
			// ���������
			pang.fillRect(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
			break;
		case 1:
			// ��һ����Բ
			pang.fillOval(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
			break;
		case 2:
			// ��ֱ��
			pang.drawLine(startPt.x, startPt.y, e.getX(), e.getY());
			break;
		case 4:
			// ��Ƥ���ñ���ɫ������
			pang.fillRect(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
			break;
		}
		// �õ����������ʼ����յ������
		lastPt = new Point(e.getX(), e.getY());
		if (lastPt.x >= width)
			lastPt.x = width - 1;
		if (lastPt.x < 0)
			lastPt.x = 0;
		if (lastPt.y >= height)
			lastPt.y = height - 1;
		if (lastPt.y < 0)
			lastPt.y = 0;

		firstPt = new Point(startPt.x, startPt.y);

	}

	void this_mouseReleased(MouseEvent e) {
		// ����ͷſ�ʼ�����Ļ�ͼ��Ҳ���ǰ�ͼ���ϵ�Ԫ��ת��������grid�У�
		// ��������൱��һ��λͼ����¼��ÿ��������ࣨ�ѡ�ʳ��㡢�ϰ��
		if (localpad.Kind != 0 || localpad.Shape == 3)
			return;
		int x2 = e.getX(), y2 = e.getY();
		if (x2 < 0)
			x2 = 0;
		if (x2 >= width)
			x2 = width - 1;
		if (y2 < 0)
			y2 = 0;
		if (y2 >= height)
			y2 = height - 1;
		int x1 = startPt.x, y1 = startPt.y;
		if (x2 < x1) {
			x1 = x2;
			x2 = startPt.x;
		}
		if (y2 < y1) {
			y1 = y2;
			y2 = startPt.y;
		}

		// ѡ����ͼ����ɫ
		if (localpad.Shape != 4)
			pang.setColor(localpad.local.OBS_COLOR);
		else
			pang.setColor(localpad.local.BACK_COLOR);

		// ���ݻ�ͼ���಻ͬ���в�ͬ�Ĵ���
		switch (localpad.Shape % 4) {
		case 0:
			// �Ǿ��ε�ʱ���ѭ�����꣬���һ������
			pang.fillRect(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
			for (int i = x1; i < x2; i++) {
				for (int j = y1; j < y2; j++) {
					if (localpad.Shape == 0)
						localpad.grid[i][j] = 1;
					if (localpad.Shape == 4)
						localpad.grid[i][j] = 0;
				}
			}
			break;
		case 1:
			// ����Բ��ѭ������һ����Բ
			pang.fillOval(x1, y1, x2 - x1, y2 - y1);
			double a = (double) (x2 - x1) / 2;
			double b = (double) (y2 - y1) / 2;
			for (double x = -a; x <= a; x++) {
				double yy1 = b * Math.sqrt(a * a - x * x) / a;
				for (double y = -yy1; y <= yy1; y++) {
					int xx = (int) (x + (x1 + x2) / 2);
					int yy = (int) (y + (y1 + y2) / 2);
					localpad.grid[xx][yy] = 1;
				}
			}
			break;
		case 2:
			// ��ֱ�ߵ�ʱ�򣬱Ƚ��鷳����Ҫ��ֱ�ߵĲ�������x=x1+(x2-x1)*t,y=y1+(y2-y1)*t,t��[0,1]��
			repaint(x1 - 1, y1 - 1, x2 - x1 + 2, y2 - y1 + 2);
			x1 = startPt.x;
			y1 = startPt.y;
			x2 = e.getX();
			y2 = e.getY();
			if (x2 < 0)
				x2 = 0;
			if (x2 >= width)
				x2 = width - 1;
			if (y2 < 0)
				y2 = 0;
			if (y2 >= height)
				y2 = height - 1;
			double step;
			if (x2 != x1 || y2 != y1) {
				if (Math.abs(x2 - x1) > Math.abs(y2 - y1))
					step = (double) (1 / (double) (Math.abs(x2 - x1)));
				else
					step = (double) (1 / (double) (Math.abs(y2 - y1)));
				for (double t = 0; t <= 1; t += step) {
					int x = (int) ((x2 - x1) * t + x1);
					int y = (int) ((y2 - y1) * t + y1);
					// Ϊ��ֱ�߲����������Ի����Ϊ2����
					pang.fillRect(x, y, 1, 1);
					pang.fillRect(x + 1, y, 1, 1);
					pang.fillRect(x, y + 1, 1, 1);
					localpad.grid[x][y] = 1;
					localpad.grid[x + 1][y] = 1;
					pang.fillRect(x, y + 1, 1, 1);
				}
			}
		}
		startPt = new Point(-1, -1);

	}

	void this_mouseMoved(MouseEvent e) {
		// ������ƶ���ʱ�������ǰģʽΪ��Ƥ��������ƶ����ĵ�ᱻһ��С�����ڵ�
		if (localpad.Shape == 4) {
			for (int x = lastPt.x - 2; x < lastPt.x + 2; x++) {
				for (int y = lastPt.y - 2; y < lastPt.y + 2; y++) {
					if (x >= 0 && x < width && y >= 0 && y < height) {
						if (localpad.grid[x][y] > 0) {
							pang.setColor(localpad.KindColor[localpad.grid[x][y]]);
							if (localpad.grid[x][y] > 1)
								pang.fillRect(x, y, 2, 2);
							pang.fillRect(x, y, 1, 1);
						}
					}
				}
			}
			pang.setColor(localpad.local.BACK_COLOR);
			pang.fillRect(e.getX() - 2, e.getY() - 2, 4, 4);
			lastPt = new Point(e.getX(), e.getY());
			if (lastPt.x >= width)
				lastPt.x = width - 1;
			if (lastPt.x < 0)
				lastPt.x = 0;
			if (lastPt.y >= height)
				lastPt.y = height - 1;
			if (lastPt.y < 0)
				lastPt.y = 0;

		}
	}

	void this_mouseClicked(MouseEvent e) {
		// ��굥���¼�����������¼���ÿ����Ŀ�ȶ���2
		if (e.getX() < 0 || e.getX() > width || e.getY() < 0 || e.getY() > width)
			return;
		if (localpad.Kind == 1 || localpad.Kind == 2 || (localpad.Kind == 0 && localpad.Shape == 3)) {
			pang.setColor(localpad.KindColor[localpad.Kind + 1]);
			int x = e.getX();
			int y = e.getY();
			pang.fillRect(x, y, 2, 2);
			localpad.grid[x][y] = localpad.Kind + 1;
			if (localpad.Kind == 0) {
				if (x + 1 >= width)
					x = x - 1;
				if (y + 1 >= height)
					y = y - 1;
				localpad.grid[x + 1][y] = localpad.Kind + 1;
				localpad.grid[x][y + 1] = localpad.Kind + 1;
				localpad.grid[x + 1][y + 1] = localpad.Kind + 1;
			}
			return;
		}
		// ��Ƥ���������еĶ���
		if (localpad.Shape == 4) {
			pang.setColor(localpad.KindColor[0]);
			int x1 = e.getX() - 2;
			int y1 = e.getY() - 2;
			int x2 = x1 + 4;
			int y2 = y1 + 4;
			pang.fillRect(x1, y1, 4, 4);
			if (x1 < 0)
				x1 = 0;
			if (x2 >= width)
				x2 = width - 1;
			if (y1 < 0)
				y1 = 0;
			if (y2 >= height)
				y2 = height - 1;
			for (int x = x1; x < x2; x++) {
				for (int y = y1; y < y2; y++) {
					localpad.grid[x][y] = 0;
				}
			}
		}
	}

	public void paint(Graphics g) {
		// �ػ�������ʱ�򣬰������е���Ϣ����ȡ����������Ļ��
		g.clearRect(0, 0, width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (localpad.grid[i][j] != 0) {
					g.setColor(localpad.KindColor[localpad.grid[i][j]]);
					if (localpad.grid[i][j] > 1)
						g.fillRect(i, j, 2, 2);
					else
						g.fillRect(i, j, 1, 1);
				}
			}
		}

	}
}


public class MapPad extends Frame {
	// ������ͼ�༭�����������
	Panel pan = new Panel();
	MapCanvas Canvas;
	Choice shapeCh = new Choice();
	Choice KindCh = new Choice();
	Choice MapCh = new Choice();
	Button btnConfirm = new Button("ȷ��");
	int Shape = 0;						// ���ǻ�ͼ����״��0���Σ�1��Բ��2ֱ�ߣ�3�㣬4��Ƥ
	int Kind = 0;						// ��ͼ�����ࣺ0�ϰ��1�ѵ㣬2��ʳ���
	Color KindColor[] = new Color[4];	// ��ͼ�������ɫ��Ϣ��0Ϊ����ɫ��1Ϊ�ϰ�����ɫ��2Ϊ�ѵ���ɫ��3Ϊʳ�����ɫ
	int grid[][];						// ���е�ͼ��Ԫ����Ϣ��������
	Antcolony local;					// �������ָ��

	public MapPad(Antcolony colony) {
		local = colony;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setLayout(new BorderLayout());
		// �����ʼ��
		grid = new int[local.width][local.height];
		for (int i = 0; i < local.width; i++) {
			for (int j = 0; j < local.height; j++) {
				grid[i][j] = local.obsGrid[i][j] + 1;
				if (grid[i][j] > 1)
					grid[i][j] = 1;
			}
		}
		for (int i = 0; i < local.endCount; i++) {
			grid[local.endPt[i].x][local.endPt[i].y] = 3;
		}
		for (int i = 0; i < local.originCount; i++) {
			grid[local.originPt[i].x][local.originPt[i].y] = 2;
		}

		// ����ɫ���鸺ֵ
		KindColor[0] = local.BACK_COLOR;
		KindColor[1] = local.OBS_COLOR;
		KindColor[2] = local.ORIGIN_COLOR;
		KindColor[3] = local.End_COLOR;

		// ���ؼ�
		this.setBackground(Color.black);
		Canvas = new MapCanvas(this);
		pan.setBackground(Color.lightGray);
		this.add("South", pan);
		this.add("Center", Canvas);
		pan.add(new Label("��״:"));
		pan.add(shapeCh);
		pan.add(KindCh);
		pan.add(btnConfirm);
		pan.add(MapCh);
		shapeCh.addItem("����");
		shapeCh.addItem("Բ");
		shapeCh.addItem("ֱ��");
		shapeCh.addItem("��");
		shapeCh.addItem("��Ƥ");
		KindCh.addItem("�ϰ���");
		KindCh.addItem("��");
		KindCh.addItem("ʳ��");
		MapCh.addItem("���ص�ͼ");
		MapCh.addItem("ɽ������");
		MapCh.addItem("����Ҷ");
		MapCh.addItem("�Թ�");
		// �¼���������jb�Լ�����ȥ�ģ������ҵ��¶���
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}

			public void windowOpened(WindowEvent e) {
				this_windowOpened(e);
			}
		});
	}

	void this_windowClosing(WindowEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	public boolean action(Event evt, Object o) {
		// ѡ��ͼ���࣬�Լ���״��������ѡ��֮���й���
		if (evt.target == shapeCh) {
			Shape = shapeCh.getSelectedIndex();
			if (Shape != 3) {
				KindCh.select(0);
				Kind = 0;
			}
			return true;
		}
		if (evt.target == KindCh) {
			Kind = KindCh.getSelectedIndex();
			if (Kind != 0) {
				shapeCh.select(3);
				Shape = 3;
			} else {
				shapeCh.select(0);
				Shape = 0;
			}
			return true;
		}
		if (evt.target == MapCh) {
			int index = MapCh.getSelectedIndex();
			if (index <= 0)
				return true;
			// ������飺
			for (int i = 0; i < local.width; i++) {
				for (int j = 0; j < local.height; j++) {
					grid[i][j] = 0;
				}
			}
			// ���ص�ͼ
			Maps.loadMap(grid, index - 1);
			Canvas.repaint();
			return true;
		}
		if (evt.target == btnConfirm) {
			// ���ѡ����ȷ������ô�ȳ�ʼ����ͼ���ٳ�ʼ���������ϣ�������������
			local.reinit_map(grid);
			local.reinit();
			this.setVisible(false);
			this.dispose();
			return true;
		}
		return false;

	}

	void this_windowOpened(WindowEvent e) {
		Canvas.pang = Canvas.getGraphics();
	}

}