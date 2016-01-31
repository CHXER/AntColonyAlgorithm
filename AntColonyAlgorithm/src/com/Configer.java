package com;

import java.awt.Frame;
import java.awt.*;
import java.awt.event.*;

/**
 * Title: ant Description: Copyright: Copyright (c) 2003 Company:
 * agents.yeah.net
 * 
 * @author jake
 * @version 1.0
 */
public class Configer extends Frame {
	// ������壬��Ҫ�Ǵ��������а����ݶ�������Ȼ����ļ�¼����
	Antcolony local;
	TextField ant_txt;
	TextField Max_txt;
	TextField Mind_txt;
	TextField Minid_txt;
	TextField Food_txt;
	Choice del_ch;
	Button btnDefault;
	Button btnConfirm;
	Button btnCancel;
	Button btnSetInd;
	double min_dis = -1;
	int minid;
	public int AntCount;
	public Ant ants[];

	public Configer(Antcolony ants) {
		super("���ò���");
		try {
			local = ants;
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		setLayout(new BorderLayout());
		Panel pan = new Panel();
		btnConfirm = new Button("ȷ��");
		btnCancel = new Button("ȡ��");
		btnDefault = new Button("����");
		btnSetInd = new Button("���ø�������");
		this.add("South", pan);
		pan.add(btnConfirm);
		pan.add(btnCancel);
		pan.add(btnDefault);
		Panel pancenter = new Panel();
		this.add("Center", pancenter);
		pancenter.setLayout(new GridLayout(7, 2, 0, 0));
		this.add("North", new Label("���û����������Լ����ϵĸ�������"));
		pancenter.add(new Label("���ϵ�����"));
		ant_txt = new TextField(10);
		pancenter.add(ant_txt);
		pancenter.add(new Label("�����Ϣ��"));
		Max_txt = new TextField(20);
		pancenter.add(Max_txt);
		pancenter.add(new Label("ʳ���ͷ���Ϣ�İ뾶��"));
		Food_txt = new TextField(20);
		pancenter.add(Food_txt);
		pancenter.add(new Label("��Ϣ�������ٶȣ�"));
		del_ch = new Choice();
		pancenter.add(del_ch);
		for (int i = 1; i <= 10; i++) {
			del_ch.addItem(Integer.toString(i));
		}
		pancenter.add(new Label("�ҵ�����С·������"));
		Mind_txt = new TextField(20);
		Mind_txt.setEditable(false);
		pancenter.add(Mind_txt);
		pancenter.add(new Label("�ҵ�����С·�����ϣ�"));
		Minid_txt = new TextField(20);
		Minid_txt.setEditable(false);
		pancenter.add(Minid_txt);

		pancenter.add(btnSetInd);
		setDefault();
		ant_txt.addTextListener(new java.awt.event.TextListener() {
			public void textValueChanged(TextEvent e) {
				ant_txt_textValueChanged(e);
			}
		});
	}

	void this_windowClosing(WindowEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	public boolean action(Event evt, Object o) {
		if (evt.target == btnDefault) {
			setDefault();
			return true;
		} else if (evt.target == btnSetInd) {
			SetInd ctl = new SetInd(this);
			ctl.setSize(300, 300);
			ctl.setVisible(true);
		} else if (evt.target == btnConfirm) {
			int t;
			if (AntCount > 0) {
				local.antCount = AntCount;
			}
			t = Integer.valueOf(Max_txt.getText()).intValue();
			if (t >= 0) {
				local.maxPheromone = t;
			}
			t = Integer.valueOf(Food_txt.getText()).intValue();
			if (t >= 0) {
				local.foodR = t;
			}
			local.delimiter = del_ch.getSelectedIndex();
			// �������ϵ�����
			local.ants = new Ant[AntCount];
			for (int i = 0; i < AntCount; i++) {
				local.ants[i] = new Ant(new Point(0, 0), 0, i, local, local.ANT_COLOR, 0, 0);
				local.ants[i].clone(ants[i]);
			}

			// ��ʼ������
			local.reinit();
			this.setVisible(false);
			this.dispose();
			return true;
		} else if (evt.target == btnCancel) {
			this.setVisible(false);
			this.dispose();
			return true;
		}
		return false;
	}

	private void setDefault() {
		ant_txt.setText(Integer.toString(local.antCount));
		AntCount = Integer.valueOf(ant_txt.getText()).intValue();
		Max_txt.setText(Integer.toString(local.maxPheromone));
		Food_txt.setText(Integer.toString(local.foodR));
		del_ch.select(local.delimiter - 1);
		copy_ants();
		String s;
		if (min_dis >= 0) {
			s = Double.toString(min_dis);
			Mind_txt.setText(s);
			Minid_txt.setText(Integer.toString(minid));
		}
	}

	void ant_txt_textValueChanged(TextEvent e) {
		int tmp = AntCount;
		AntCount = Integer.valueOf(ant_txt.getText()).intValue();
		if (AntCount < 0) {
			AntCount = tmp;
			setDefault();
			return;
		}
		if (AntCount > local.antCount) {
			copy_ants();
		}
	}

	public void copy_ants() {
		// ���¿������ϵ�״̬�������µ�������Ϊ��ֵ
		ants = new Ant[AntCount];
		for (int i = 0; i < AntCount; i++) {
			ants[i] = new Ant(new Point(0, 0), 0, i, local, local.ANT_COLOR, 0, 0);
			if (i < local.antCount) {
				ants[i].clone(local.ants[i]);
				// ����·����̵����ϡ�
				if (min_dis < 0 || (ants[i].minDistance < min_dis && ants[i].minDistance >= 0)) {
					min_dis = ants[i].minDistance;
					minid = i;
				}
			}
		}
	}
}