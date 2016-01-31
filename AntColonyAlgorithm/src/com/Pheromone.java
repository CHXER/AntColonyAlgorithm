/**
 * Title: ant Description: Copyright: Copyright (c) 2003 Company:
 * agents.yeah.net
 * 
 * @author jake
 * @version 1.0
 */
package com;
import java.awt.*;


/**
 * ��Ϣ��
 */
public class Pheromone {
	int capability;		// ��Ϣ�صĺ���
	int id;				// ��Ϣ�صı�ʶ
	int x;				// x����
	int y;				// y����
	int delimiter;		// ����Ϣ�ؼ��ٵ�ʱ����ɢ����ֵ
	int ant_id;			// ��¼����Ϣ�������ĸ������ͷŵ�
	int kind;			// ��Ϣ�ص����࣬0Ϊ���ѵ���Ϣ�أ����մ������������������ͷŵģ���1Ϊʳ���
	int origin_capcity;	// ��Ϣ�ص�ԭʼ��С����Ϊ�ڻ����У�ʳ����ѵ���Χ�������ͷ���һ��������Ϣ��
	Antcolony local;	// ��ǰ�����ָ��

	public Pheromone(int x1, int y1, int idd, int deli, int ant, Antcolony colony, int oc, int k) {
		x = x1;
		y = y1;
		id = idd;
		delimiter = deli;
		ant_id = ant;
		local = colony;
		origin_capcity = oc;
		capability = origin_capcity;
		kind = k;
	}

	public void add(int cap) {
		// ���Ӹõ����Ϣ��
		capability += cap;
	}

	public void delimit(Graphics g) {
		// ��ɢ��Ϣ��
		if (capability > 0) {
			capability -= delimiter;
			local.pheromoneGrid[kind][x][y] -= delimiter;
		} else {
			// �����Ϣ�ؼ���0�ˣ��ӻ�����Ϣ��������ע��
			local.pheromoneGrid[kind][x][y] = 0;
			local.phe.removeElement(this);
			// ����Ϣ�ش���Ļ��Ĩȥ
			g.setColor(local.BACK_COLOR);
			g.fillOval(x, y, 1, 1);
		}
	}

	public void draw(Graphics g) {
		// ����Ϣ��
		Color color;
		int cap = (int) (255 * (double) (200 * capability) / (double) (local.maxPheromone));
		if (cap >= 255)
			cap = 255;
		if (cap <= 2) {
			color = local.BACK_COLOR;
		} else {
			if (kind == 0) {
				color = new Color(cap, (int) (cap / 2), 0);
			} else {
				color = new Color(0, (int) (cap / 2), cap);
			}
		}
		g.setColor(color);
		g.fillOval(x, y, 1, 1);
	}
}