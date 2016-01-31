package com;

import java.awt.*;
import java.util.Random;
import java.util.Vector;
import java.lang.System;

public class Ant {
	Point nowPt; // ��ǰ������
	int vr; // �ٶȣ�ÿ���������߶�����󳤶�
	int id; // ��ʶ
	Point lastPt; // ��һ������
	Color color; // ���ϵ���ɫ
	Color backColor; // ��������ɫ
	int height, width; // ����ĳߴ�
	int phe; // ÿ���ͷ���Ϣ�ص���ֵ
	Vector<Point> historyPoint; // ��¼һ����ʳ������ʷ�ϵ����е�
	double mainDirect; // ������
	Point foodPt; // ��¼��ʳ��㣬�Ƿ��ҵ�ʱ���ж���
	Point originPt; // �ѵ�����
	Point aimPt; // Ŀ����λ�ã����ѻ���ʳ������꣩
	Point startPt; // ��ʼ���λ�ã����ѻ���ʳ������꣩
	int foundTimes; // �ҵ�ʳ������ѵĴ��������Ϊż����������Ѱ��ʳ������������������Ѱ���ѣ���
	int maxPheromone; // ����ܹ��ͷŵ���Ϣ��
	int pheromoneCount; // ��ǰ��ӵ�е���Ϣ�ص�����
	int judged = 0; // �ж�Ѱ��Ŀ���Ĺ����Ƿ��Ѿ�������
	double mistake; // ������ĸ���
	int memory; // �����߹������Ŀ
	double countDistance; // �߹�����·��,Ϊ���̵�·�̣�Ҳ����˵�ҵ�ʳ������Ѿʹ��¼����ˡ�
	double minDistance; // ��ǰ��ֻ������û��������ʱ�����С�ܾ���
	Antcolony localColony; // �����������

	/**
	 * ���Ϲ��캯��
	 * 
	 * @param nowpt
	 *            ����������λ��
	 * @param vr
	 *            �ٶ�
	 * @param idd
	 *            ��ֻ���ϵı�ʶ
	 * @param colony
	 *            ����������
	 * @param c
	 *            ��ɫ
	 * @param mist
	 *            ������ĸ���
	 * @param mem
	 *            �ɼ����߹��ĵ���
	 */

	public Ant(Point nowpt, int vr, int idd, Antcolony colony, Color c,
			double mist, int mem) {
		nowPt = new Point(nowpt.x, nowpt.y);
		originPt = new Point(nowpt.x, nowpt.y);
		foodPt = new Point(nowpt.x, nowpt.y);
		startPt = new Point(nowpt);
		aimPt = new Point(nowpt);
		lastPt = nowPt;
		this.vr = 5;
		id = idd;
		color = c;
		backColor = Antcolony.BACK_COLOR;
		height = Antcolony.height;
		width = Antcolony.width;
		localColony = colony;
		phe = 2000;
		mistake = mist;
		historyPoint = new Vector<Point>();
		mainDirect = -1;
		foundTimes = 0;
		maxPheromone = Antcolony.maxPheromone;
		pheromoneCount = maxPheromone;
		memory = mem;
		countDistance = 0;
		minDistance = Double.MAX_VALUE;
	}

	/**
	 * ��ʼ������
	 */
	public void init() {
		nowPt = new Point(originPt);
		lastPt = new Point(originPt);
		foodPt = new Point(originPt);
		aimPt = new Point(originPt);
		startPt = new Point(originPt);
		historyPoint.removeAllElements();
		mainDirect = -1;
		foundTimes = 0;
		pheromoneCount = maxPheromone;
		countDistance = 0;
		minDistance = Double.MAX_VALUE;
	}

	/**
	 * �Ѹ�ֻ���ϻ��ڵ�ͼ�����
	 * 
	 * @param g
	 *            ����Ļ���
	 */
	public void draw(Graphics g) {
		// ����������Ļ�ϻ��������Ȳ����ϴλ��ĵ㣬Ȼ���ٻ��������ڵĵ㡣
		g.setColor(backColor);
		g.fillOval((int) lastPt.x, (int) lastPt.y, 1, 1);
		g.setColor(color);
		g.fillOval((int) nowPt.x, (int) nowPt.y, 1, 1);
	}

	/**
	 * ������������Ͻ��о��ߵ������������ж������Ƿ��Ѿ��ҵ���Ŀ��� ��Ŀ�����û�ҵ�ʳ���ʱ����ʳ��㣬�ҵ��Ժ����Լ����ѣ�
	 * Ȼ��������ϵ�������Ҳ���������ϵ�������һ�����ԣ���û����Ϣ����ָ����ʱ�����ϰ����������˶�
	 * ��ʼ�����Լ���Χ�Ŀռ���Ϣ�������ж�����Ϣ�أ��Ƿ����ϰ����������Ϣ�صĴ�С�����ƶ����Ǹ���
	 * ���ݾ��ߵ�Ŀ�������ʵ���ƶ������а����˱��ϵ���Ϊ��������Ϣ�ء�
	 */
	public void update() {
		// TODO:��������λ���Լ�����״̬
		// ������Ҫ������������������������Ҫ�����Ը��ĸ����е�˽�к�����
		if (historyPoint.size() != 0) {
			int px = historyPoint.lastElement().x;
			int py = historyPoint.lastElement().y;
			if (px == nowPt.x && py == nowPt.y) {
				lastPt = nowPt;
				if (nowPt.x > 250)
					nowPt.x -= 5;
				else
					nowPt.x += 5;
				if (nowPt.y < 50)
					nowPt.y += 5;
				else
					nowPt.y -= 5;

				return;
			}
		}
		if (judged < 2) {
			if (judgeEnd()) {
				judged++;
				return;
			}
		}
		judged = 0;
		double direct = selectDirect();

		int deltx = 0, delty = 0;
		deltx = (int) (vr * Math.cos(direct));
		delty = (int) (vr * Math.sin(direct));

		int kind = foundTimes % 2;
		int here = Antcolony.pheromoneGrid[1 - kind][nowPt.x][nowPt.y];

		int maxphe = here;

		int deltx1, delty1;
		deltx1 = deltx;
		delty1 = delty;

		for (int x = -vr; x <= vr; x++) {
			for (int y = -vr; y <= vr; y++) {
				int xx = nowPt.x + x;
				int yy = nowPt.y + y;
				if (xx >= width || yy >= height || xx <= 0 || yy <= 0)
					continue;
				if (x != 0 || y != 0) {
					int phe = Antcolony.pheromoneGrid[1 - kind][xx][yy];

					if (maxphe < phe) {

						double ra = Math.random();
						if (here == 0 || ra > mistake) {
							boolean found = false;
							int size = historyPoint.size();
							int minsize = memory;
							if (size < memory)
								minsize = size;
							for (int i = size - 1; i >= size - minsize; i--) {
								Point pt = (Point) (historyPoint.elementAt(i));
								if (pt.x == xx && pt.y == yy) {
									found = true;
									break;
								}
							}
							if (!found) {
								maxphe = Antcolony.pheromoneGrid[1 - kind][xx][yy];
								deltx = x;
								delty = y;
							}
						}
					}
				}
			}
		}
		Point pt;
		pt = evadeObs(deltx, delty);

		if (pt.x == nowPt.x && pt.y == nowPt.y) {
			pt = evadeObs(deltx1, delty1);
		}
		boolean flag = true;
		for (int i = 0; i < historyPoint.size(); i++)
			if (pt.x == historyPoint.get(i).x && pt.y == historyPoint.get(i).y) {
				mainDirect = Math.random() * 2 * Math.PI;
				int dx = (int) (Math.sin(mainDirect) * vr);
				int dy = (int) (Math.cos(mainDirect) * vr);
				// System.out.println(dx+" "+dy);
				pt = evadeObs(dx, dy);
				flag = false;
				break;
			}
		if (flag)
			scatter();

		countDistance += distance(lastPt, nowPt);

		lastPt = new Point(nowPt.x, nowPt.y);

		historyPoint.insertElementAt(lastPt, historyPoint.size());
		if (historyPoint.size() > memory) {
			historyPoint.removeElementAt(0);
		}
		nowPt = new Point(pt.x, pt.y);
	}

	/**
	 * ����������ݾ��ߵ�λ��ֵ���б��ϵ��жϣ������ʵ�����ƶ����ĵ�
	 * Ҫ�ƶ�����Ŀ�����(nowPt+delt),��ǰ����nowPt����ô����nowPt��(nowPt+delt)������ֱ���ϵ����е㣬����û���ϰ��
	 * 
	 * @param deltx
	 *            Ŀ����뵱ǰ���xƫ��
	 * @param delty
	 *            Ŀ����뵱ǰ���yƫ��
	 * @return ���ƶ����ĵ㡣
	 */
	/*
	 * private Point evadeObs(int deltx, int delty) {
	 * 
	 * // ����ֱ�ߵĲ������̣� // x=p1x+(p2x-p1x)*t,y=p1y+(p2y-p1y)*t; //
	 * ����t�ǲ�����ȡֵ[0,1]������Ϊabs(max{p2x-p1x,p2y-p1y})�� //
	 * p1,p2������ֱ���nowPt��nowPt+delt
	 * 
	 * // TODO: ��ɱ��Ϲ��� // ���ѣ����Antcolony.obsGrid[x][y] >= 0����ʾ��x��yλ�ô����ϰ��� Point
	 * to = new Point(nowPt.x + deltx, nowPt.y + delty); int k =
	 * Math.max(Math.abs(deltx), Math.abs(delty)); for (int i = 0; i <= k; i++)
	 * { int dx = i * deltx / k; int dy = i * delty / k; if
	 * (Antcolony.obsGrid[nowPt.x + dx][nowPt.y + dy] >= 0) { dx = (i - 1) *
	 * deltx / k; dy = (i - 1) * delty / k; mainDirect = reverse(mainDirect);
	 * return new Point(nowPt.x + dx, nowPt.y + dy); } } return to; }
	 */

	private Point evadeObs(int deltx, int delty) {

		// ����ֱ�ߵĲ������̣�
		// x=p1x+(p2x-p1x)*t,y=p1y+(p2y-p1y)*t;
		// ����t�ǲ�����ȡֵ[0,1]������Ϊabs(max{p2x-p1x,p2y-p1y})��
		// p1,p2������ֱ���nowPt��nowPt+delt

		// TODO: ��ɱ��Ϲ���
		// ���ѣ����Antcolony.obsGrid[x][y] >= 0����ʾ��x��yλ�ô����ϰ���
		Point to = new Point(nowPt.x + deltx, nowPt.y + delty);
		int k = Math.max(Math.abs(deltx), Math.abs(delty));
		if (k != 0)
			for (int i = 0; i <= k; i++) {
				int dx = i * deltx / k;
				int dy = i * delty / k;
				if (Antcolony.obsGrid[nowPt.x + dx][nowPt.y + dy] >= 0) {
					dx = (i - 1) * deltx / k;
					dy = (i - 1) * delty / k;
					double re = Math.random();
					double re1 = Math.random();
					mainDirect += Math.PI * (re * re - re1 * re1) / 2 + Math.PI;
					if (mainDirect >= Math.PI * 2)
						mainDirect -= Math.PI * 2;
					return new Point(nowPt.x + dx, nowPt.y + dy);
				}
			}
		return to;
	}

	/**
	 * �ͷ���Ϣ�� �ͷ���Ϣ�غ�����ÿֻ������һ����Ϣ�ص������maxPheromone��
	 * ���ң�ÿ�����϶��ͷ�phe��λ��Ϣ�أ����Ҵ�����pheromoneCount�м�ȥPhe��ֱ���������е���Ϣ��
	 */
	private void scatter() {
		if (pheromoneCount <= 0)
			return;
		// �����ͷ���Ϣ�ص�����
		int kind = foundTimes % 2;

		// ��õ�ǰ�㻷��������Ϣ�ص�ֵ
		int Phec = Antcolony.pheromoneGrid[kind][lastPt.x][lastPt.y];
		if (Phec != 0) {
			// �����ǰ���Ѿ�����Ϣ����
			// TODO: ��������ͷ���Ϣ�ع���
			// ���ѣ������ڴﵽ�һ�����ʱ�������ͷŵ���Ϣ������Ϊ���������ͷ��������˸�ֵ�������ͷ�
			// ��Antcolony.phe�Ҳ��Ҹõ����Ϣ�أ������¡�
			for (int i = 0; i < Antcolony.phe.size(); i++) {
				Pheromone ph = (Pheromone) (Antcolony.phe.elementAt(i));
				if (lastPt.x == ph.x && lastPt.y == ph.y && ph.kind == kind) {
					ph.ant_id = id;
					if (pheromoneCount < phe) {
						Antcolony.pheromoneGrid[kind][lastPt.x][lastPt.y] += pheromoneCount;
						ph.capability += pheromoneCount;
						pheromoneCount = 0;
					} else {
						Antcolony.pheromoneGrid[kind][lastPt.x][lastPt.y] += phe;
						ph.capability += phe;
						pheromoneCount -= phe;
					}
					Antcolony.phe.addElement(ph);
					Antcolony.phe.removeElementAt(i);
					break;
				}
			}
		} else {
			if (pheromoneCount < phe) {
				Antcolony.pheromoneGrid[kind][lastPt.x][lastPt.y] = pheromoneCount;
				Pheromone ph = new Pheromone(lastPt.x, lastPt.y, 100, 1, id,
						null, 0, kind);
				ph.capability = pheromoneCount;
				pheromoneCount = 0;
				Antcolony.phe.addElement(ph);
			} else {
				Antcolony.pheromoneGrid[kind][lastPt.x][lastPt.y] = phe;
				Pheromone ph = new Pheromone(lastPt.x, lastPt.y, 100, 1, id,
						null, 0, kind);
				ph.capability = phe;
				pheromoneCount -= phe;
				Antcolony.phe.addElement(ph);
			}

		}

		phe = (int) (pheromoneCount * 0.005);
		if (phe <= 10)
			phe = 10;

	}

	/**
	 * �ж������Ƿ��ҵ�Ŀ��㣬������ҵ����������ϵ����ֱ꣬�ӽ�����õ�Ŀ���
	 * 
	 * @return �Ƿ��ҵ�Ŀ���
	 */
	private double reverse(double a) {
		a = a + Math.PI;
		if (a > 2 * Math.PI)
			a -= 2 * Math.PI;
		return a;
	}

	private double dist(Point a, Point b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	private boolean judgeEnd() {
		// ���Ȼ�õ�ǰ�������������ѻ�������ʳ�
		int kind = foundTimes % 2; // �����0����������ʳ���֮������������
		if (kind == 0) { // ����ʳ��
			// TODO: �ж��Ƿ��ҵ�ʳ��
			// �������ѣ�
			// 1.�ж��Ƿ��ҵ�(���е�ʳ��������뵱ǰ��Ƚϣ�����С��vr����Ϊ���ҵ�)������ҵ�����ֱ���ƶ���ʳ��㣬�����������Ϣ������ֱ�ӷ���false
			// 2.���´˴�·;���ܾ��룬�����±����ֻ�����ҵ��ġ�����ʳ�֮�����̾��롣
			// 3.Ϊ�´�·;��׼��������ܾ����¼(countDistance)����ʷ��¼(historyPoint)������Ŀ���(aimPt)�Լ���ʼ��(startPt)�������ҵ�Ŀ�����(foundTimes)
			// 4.�ı�������(mainDirect)����¼�ҵ���ʳ��λ��(foodPt)�����ÿɲ�����Ϣ��(pheromoneCount)�����ֵ(maxPheromone)
			for (int i = 0; i < localColony.endCount; i++) {
				double dis = dist(nowPt, localColony.endPt[i]);
				if (dis <= (Math.random() / 2 + 1) * (Math.random() + 3) * vr) {
					historyPoint.removeAllElements();
					lastPt = nowPt;
					historyPoint.addElement(localColony.endPt[i]);
					countDistance += dis;
					minDistance = Math.min(minDistance, countDistance);
					countDistance = 0.0;
					aimPt = localColony.originPt[i];
					startPt = localColony.endPt[i];
					foundTimes = 1;
					mainDirect = reverse(mainDirect);
					pheromoneCount = maxPheromone;
					return true;
				}
			}
			// ����û�ҵ�
			return false;
		} else { // ������
					// TODO: �ж��Ƿ��ҵ���
					// �������ѣ�
					// 1.�ж��Ƿ��ҵ�(�������뵱ǰ��Ƚϣ�����С��vr����Ϊ���ҵ�)������ҵ�����ֱ���ƶ����ѣ������������Ϣ������ֱ�ӷ���false
					// 2.���´˴�·;���ܾ��룬�����±����ֻ�����ҵ��ġ�����ʳ�֮�����̾��롣
					// 3.Ϊ�´�·;��׼��������ܾ����¼(countDistance)����ʷ��¼(historyPoint)������Ŀ���(aimPt)�Լ���ʼ��(startPt)�������ҵ�Ŀ�����(foundTimes)
					// 4.�ı�������(mainDirect)�����ÿɲ�����Ϣ��(pheromoneCount)�����ֵ(maxPheromone)

			for (int i = 0; i < localColony.originCount; i++) {
				double dis = dist(nowPt, localColony.endPt[i]);
				if (dis <= (Math.random() + 2) * vr) {
					lastPt = nowPt;
					historyPoint.addElement(localColony.originPt[i]);
					countDistance += dis;
					minDistance = Math.min(minDistance, countDistance);
					countDistance = 0.0;
					historyPoint.removeAllElements();
					historyPoint.addElement(localColony.originPt[i]);
					aimPt = localColony.endPt[i];
					startPt = localColony.originPt[i];
					foundTimes = 0;
					mainDirect = reverse(mainDirect);
					pheromoneCount = maxPheromone;
					return true;
				}
			}
			// ����û�ҵ�
			return false;
		}
	}

	/**
	 * ѡ����
	 * 
	 * @return ����
	 */
	private double selectDirect() {
		// ѡ�������ѡ��ķ���Ϊ�������һ������Ŷ�
		double direct, e = 0;
		if (mainDirect < 0) {
			// ���Ŀǰ��û��������ǣ��������ѡ��һ��
			e = 2 * Math.PI * Math.random();
			mainDirect = e;
		}
		// ѡ���������
		direct = mainDirect;
		// ��һ�����ģ�ͣ����������������x,y����[0,1]�ڵģ�����x^2-y^2����һ��
		// [-1��1]���������������0�㸽���ĸ��ʴ�����С
		double re = Math.random();
		double re1 = Math.random();

		if (Math.random() < 0.02) {
			// ��С����0.02�ı��������ֵ���������ѡȡΪ�����ϼ�ס�ĵ������ѡһ���㣬���㵱ǰ��������֮��ķ���ǡ�
			// int size = (int) (re1 * memory) + 1;
			// if (historyPoint.size() > size) {
			// Point pt = (Point) (historyPoint.elementAt(historyPoint.size() -
			// size));
			// if (pt.x != nowPt.x || pt.y != nowPt.y) {
			// mainDirect = getDirection(pt, nowPt);
			// }
			// }
			direct = mainDirect + 3.14 / 18 * ((int) Math.random()) % 100 / 100;
			direct += Math.PI * (re * re - re1 * re1) / 2;
		}
		return direct;
	}

	/**
	 * ���pt1-->pt2�ķ����
	 * 
	 * @param pt1
	 *            ���
	 * @param pt2
	 *            �յ�
	 * @return �����ֵ����λΪ�����ƣ�
	 */
	private double getDirection(Point pt1, Point pt2) {
		// �������Ϊָ��������pt1��pt2������pt1-->pt2�ķ����
		// �˺������Ѷ���Ҫ���ڣ����ǵ����������棬�����Ҫ�Ӷ��������㷽��ǣ�
		// ���з���������п��ܵĽ���ʹ���������߾�����̵Ľǡ�
		double e;
		int deltx1 = pt2.x - pt1.x;
		int deltx2;
		if (pt2.x > pt1.x)
			deltx2 = pt2.x - pt1.x - width;
		else
			deltx2 = pt2.x + width - pt1.x;
		int delty1 = pt2.y - pt1.y;
		int delty2;
		if (pt2.y > pt1.y)
			delty2 = pt2.y - pt1.y - height;
		else
			delty2 = pt2.y + height - pt1.y;
		int deltx = deltx1, delty = delty1;
		if (deltx == 0 && delty == 0)
			return -1;
		if (Math.abs(deltx2) < Math.abs(deltx1)) {
			deltx = deltx2;
		}
		if (Math.abs(delty2) < Math.abs(delty1)) {
			delty = delty2;
		}
		if (deltx != 0) {
			e = Math.atan((double) (delty) / (double) (deltx));
			if (deltx < 0) {
				if (e < 0)
					e = e - Math.PI;
				else
					e = e + Math.PI;
			}
		} else {
			if (delty > 0)
				e = Math.PI / 2;
			else
				e = -Math.PI / 2;
		}
		e = (e + Math.PI * 2) % (2 * Math.PI);
		return e;
		// return (Math.atan((double)(pt2.y-pt2.y)/(double)(pt2.x-pt1.x)) +
		// Math.PI * 2) % (2 * Math.PI);
	}

	/**
	 * ��������ľ���
	 * 
	 * @param pt1
	 *            ��һ�����λ��
	 * @param pt2
	 *            �ڶ������λ��
	 * @return �������
	 */
	private double distance(Point pt1, Point pt2) {
		// ��������pt1,pt2����������֮��ľ��룬�ѵ��������������棬����������ѭ���������
		// �������������п��ܾ�������С��
		int dx1 = pt1.x - pt2.x;
		int dx2;
		int dx, dy;
		if (pt1.x > pt2.x)
			dx2 = pt1.x + width - pt2.x;
		else
			dx2 = pt2.x + width - pt1.x;
		int dy1 = pt1.y - pt2.y;
		int dy2;
		if (pt1.y > pt2.y)
			dy2 = pt1.y + height - pt2.y;
		dy2 = pt2.y + height - pt1.y;
		if (Math.abs(dx1) < Math.abs(dx2))
			dx = dx1;
		else
			dx = dx2;
		if (Math.abs(dy1) < Math.abs(dy2))
			dy = dy1;
		else
			dy = dy2;
		return Math.sqrt(dx * dx + dy * dy);
		// return Math.sqrt((pt1.x - pt2.x) * (pt1.x - pt2.x) + (pt1.y - pt2.y)
		// * (pt1.y - pt2.y));
	}

	public void clone(Ant ant1) {
		// ������ant1�����Կ�����������
		nowPt = new Point(ant1.nowPt);
		originPt = new Point(ant1.originPt);
		foodPt = new Point(ant1.foodPt);
		startPt = new Point(ant1.startPt);
		aimPt = new Point(ant1.aimPt);
		lastPt = new Point(ant1.lastPt);
		vr = ant1.vr;
		id = ant1.id;
		color = ant1.color;
		backColor = ant1.backColor;
		height = ant1.height;
		width = ant1.width;
		localColony = ant1.localColony;
		phe = ant1.phe;
		mistake = ant1.mistake;
		historyPoint = ant1.historyPoint;
		mainDirect = ant1.mainDirect;
		foundTimes = ant1.foundTimes;
		maxPheromone = ant1.maxPheromone;
		pheromoneCount = ant1.pheromoneCount;
		memory = ant1.memory;
		countDistance = ant1.countDistance;
		minDistance = ant1.minDistance;
	}

}