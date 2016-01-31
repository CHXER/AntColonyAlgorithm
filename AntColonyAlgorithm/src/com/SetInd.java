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

public class SetInd extends Frame {
	// �����������Ե���壬û��������⣺�ı���������ɫ���á�
	Configer local;
	boolean Confirmed = false;
	Choice ch_Id;
	Choice Mistake_ch;
	Choice VR_ch;
	Choice Memory_ch;
	Choice Color_ch;
	Panel pancenter;
	double Mistake[];
	int VR[];
	int Memory[];
	Color colors[] = { Color.black, Color.blue, Color.red, Color.green, Color.yellow, Color.magenta, Color.white,
			Color.cyan, Color.orange };
	String colortxt[] = { "��", "��", "��", "��", "��", "��", "��", "��", "��", "�Զ���.." };
	int index;
	TextField Color_txt;
	TextField ant_txt;
	TextField Max_txt;
	TextField Times_txt;
	TextField Mind_txt;
	Button btnDefault;
	Button btnAll;
	Button btnConfirm;
	Button btnCancel;

	public SetInd(Configer lc) {
		super("�������ϸ������");
		try {
			local = lc;
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
		btnAll = new Button("Ӧ����ȫ������");
		this.add("South", pan);
		pan.add(btnConfirm);
		pan.add(btnCancel);
		// pan.add(btnDefault);
		Panel pancenter = new Panel();
		this.add("Center", pancenter);
		pancenter.setLayout(new GridLayout(8, 2, 0, 0));
		this.add("North", new Label("�������ϵĸ�������"));
		pancenter.add(new Label("���ϱ�ţ�"));
		ch_Id = new Choice();
		pancenter.add(ch_Id);
		for (int i = 0; i < local.AntCount; i++) {
			ch_Id.addItem(Integer.toString(i));
		}
		pancenter.add(new Label("������ʣ�"));
		Mistake_ch = new Choice();
		Mistake = new double[200];
		for (int i = 0; i <= 100; i++) {
			Mistake[i] = 0.001 * i;
			Mistake_ch.addItem(Double.toString(Mistake[i]));
		}
		pancenter.add(Mistake_ch);
		pancenter.add(new Label("�ٶȰ뾶��"));
		VR_ch = new Choice();
		VR = new int[20];
		pancenter.add(VR_ch);
		for (int i = 0; i <= 8; i++) {
			VR[i] = i + 2;
			VR_ch.addItem(Integer.toString(VR[i]));
		}
		pancenter.add(new Label("����������"));
		Memory_ch = new Choice();
		Memory = new int[20];
		pancenter.add(Memory_ch);
		for (int i = 0; i <= 9; i++) {
			Memory[i] = 10 + i * 10;
			Memory_ch.addItem(Integer.toString(Memory[i]));
		}
		pancenter.add(new Label("���ϵ���ɫ��"));
		Color_ch = new Choice();
		Color_txt = new TextField(50);
		pancenter.add(Color_ch);
		for (int i = 0; i < colors.length + 1; i++) {
			Color_ch.addItem(colortxt[i]);
		}
		pancenter.add(new Label("��ɫ��16���Ʊ�ʾ��"));
		pancenter.add(Color_txt);
		pancenter.add(new Label("��ǰ�����ҵ���ʳ��Σ���"));
		Times_txt = new TextField(50);
		Times_txt.setEditable(false);
		pancenter.add(Times_txt);
		pancenter.add(new Label("��ǰ�����ҵ������·���ȣ�"));
		Mind_txt = new TextField(50);
		Mind_txt.setEditable(false);
		pancenter.add(Mind_txt);
		pan.add(btnAll);
		setDefault();

		Color_txt.addTextListener(new java.awt.event.TextListener() {
			public void textValueChanged(TextEvent e) {
				Color_txt_textValueChanged(e);
			}
		});
	}

	void this_windowClosing(WindowEvent e) {
		if (!Confirmed) {
			local.copy_ants();
		}
		this.setVisible(false);
		this.dispose();
	}

	public boolean action(Event evt, Object o) {
		if (evt.target == btnConfirm) {
			Confirmed = true;
			this.setVisible(false);
			this.dispose();
			return true;
		} else if (evt.target == btnCancel) {
			if (!Confirmed) {
				local.copy_ants();
			}
			this.setVisible(false);
			this.dispose();
			return true;
		} else if (evt.target == btnAll) {
			// setDefault();
			for (int j = 0; j < local.AntCount; j++) {
				int i = VR_ch.getSelectedIndex();
				local.ants[j].vr = VR[i];
				i = Mistake_ch.getSelectedIndex();
				local.ants[j].mistake = Mistake[i];
				i = Memory_ch.getSelectedIndex();
				local.ants[j].memory = Memory[i];
				i = Color_ch.getSelectedIndex();
				if (i < colors.length)
					local.ants[index].color = colors[i];
				else {
					String s = Color_txt.getText();
					try {
						int t;
						t = Integer.valueOf(s, 16).intValue();
						local.ants[j].color = new Color(t);
					} catch (Exception a) {
						return true;
					}
				}
			}
			return true;
		} else if (evt.target == VR_ch) {
			int i = VR_ch.getSelectedIndex();
			local.ants[index].vr = VR[i];
		} else if (evt.target == Mistake_ch) {
			int i = Mistake_ch.getSelectedIndex();
			local.ants[index].mistake = Mistake[i];
		} else if (evt.target == Memory_ch) {
			int i = Memory_ch.getSelectedIndex();
			local.ants[index].memory = Memory[i];
		} else if (evt.target == Color_ch) {
			int i = Color_ch.getSelectedIndex();
			local.ants[index].color = colors[i];
			Color_txt.setText(Integer.toHexString(local.ants[index].color.getRGB()));
		} else if (evt.target == ch_Id) {
			index = ch_Id.getSelectedIndex();
			setDefault();
		}
		return false;
	}

	private void setDefault() {
		for (int i = 0; i < Mistake.length; i++) {
			if (local.ants[index].mistake == Mistake[i]) {
				Mistake_ch.select(i);
				break;
			}
		}
		for (int i = 0; i < VR.length; i++) {
			if (local.ants[index].vr == VR[i]) {
				VR_ch.select(i);
				break;
			}
		}
		for (int i = 0; i < Memory.length; i++) {
			if (local.ants[index].memory == Memory[i]) {
				Memory_ch.select(i);
				break;
			}
		}
		boolean found = false;
		Color_txt.setText(Integer.toHexString(local.ants[index].color.getRGB()));

		for (int i = 0; i < colors.length; i++) {
			if (local.ants[index].color == colors[i]) {
				Color_ch.select(i);
				found = true;
				break;
			}
		}
		if (!found) {
			Color_ch.select(colors.length);
		}

		int times = (int) ((local.ants[index].foundTimes + 1) / 2);
		Times_txt.setText(Integer.toString(times));
		if (times > 0) {
			double mind = local.ants[index].minDistance;
			Mind_txt.setText(Double.toString(mind));
		}
	}

	void Color_txt_textValueChanged(TextEvent e) {
		String s = Color_txt.getText();
		try {
			int t;
			// if(s.charAt(0)=='f')s=s.substring(1,s.length());
			t = Integer.valueOf(s, 16).intValue();
			local.ants[index].color = new Color(t);
			boolean found = false;
			for (int i = 0; i < colors.length; i++) {
				if (local.ants[index].color == colors[i]) {
					Color_ch.select(i);
					found = true;
					break;
				}
				if (!found)
					Color_ch.select(colors.length);

			}
		} catch (Exception a) {
			return;
		}
	}
}