package com;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 * ����
 */
public class AntCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	// ������һ�л�ͼ�������ɸ������
	Color obs_color;		// �ϰ�����ɫ
	Color origin_color;		// �ѵ���ɫ
	Color back_color;		// ����ɫ
	Color end_color;		// ʳ������ɫ
	boolean reset;

	public AntCanvas() {
		super();
		back_color = Antcolony.BACK_COLOR;
		setBackground(back_color);
		setForeground(Color.white);
		obs_color = Antcolony.OBS_COLOR;
		origin_color = Antcolony.ORIGIN_COLOR;
		end_color = Antcolony.End_COLOR;
		reset = true;
	}

	public void Clear() {
		// ��ջ���
		reset = true;
		repaint();
	}

	public void paint(Graphics g) {
		int i;
		// �ػ���ʱ��������ϰ���
		g.setColor(Color.black);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(obs_color);
		for (i = 0; i < Antcolony.obsCount; i++) {
			g.fillRect(Antcolony.obsP[i].x, Antcolony.obsP[i].y, 1, 1);
		}

	}

	public void process() {
		// �������Ĺ���
		Graphics g = this.getGraphics();
		g.setColor(end_color);
		for (int j = 0; j < Antcolony.endCount; j++) {
			// �����е�ʳ���
			g.fillRect(Antcolony.endPt[j].x, Antcolony.endPt[j].y, 2, 2);
		}
		for (int i = 0; i < Antcolony.antCount; i++) {
			// ÿֻ���Ͽ�ʼ���ߣ���������
			Antcolony.ants[i].update();
			Antcolony.ants[i].draw(g);
		}
		
		for (int i = 0; i < Antcolony.phe.size(); i++) {
			Pheromone v = (Pheromone) (Antcolony.phe.elementAt(i));
			// Antcolony��drawPhe������־�Ƿ���Ϣ��
			switch (Antcolony.drawPhe) {
			case (1):
				v.draw(g);
				break;
			case (2):
				if (v.kind == 1)
					v.draw(g);
				break;
			case (3):
				if (v.kind == 0)
					v.draw(g);
				break;
			}
			v.delimit(g);
		}
		g.setColor(origin_color);
		for (int i = 0; i < Antcolony.originCount; i++) {
			// �����е���
			g.fillRect(Antcolony.originPt[i].x, Antcolony.originPt[i].y, 2, 2);
		}

	}
}