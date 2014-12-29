package com.aia.appsreport.component.table;

import utils.factory.FontFactory.FontType;
import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class ItemLog extends ItemTable {
	LabelStyle style;

	public ItemLog(AbstractTable table, String stt, String sdt, String ten,
			String loaigd, String sotientruocgd, String sotiensaugd,
			String sotiengd, String donvi, String nguoigd, String sdtnguoidg,
			String note, String thoigian) {
		super(table);
		style = Style.ins.getLabelStyle(20, FontType.Regular, Color.GRAY);
		Label lb[] = { getLabel(stt, 0), getLabel(sdt, 1), getLabel(ten, 2),
				getLabel(loaigd, 3), getLabel(sotientruocgd, 4),
				getLabel(sotiensaugd, 5), getLabel(sotiengd, 6),
				getLabel(donvi, 7), getLabel(nguoigd, 8),
				getLabel(sdtnguoidg, 9), getLabel(note, 10),
				getLabel(thoigian, 11) };
		setComponentItem(lb);
		init();
	}

	public ItemLog(AbstractTable table, String stt, String sdt, String ten,
			String loaigd, String sotientruocgd, String sotiensaugd,
			String sotiengd, String donvi, String thoigian) {
		super(table);
		style = Style.ins.getLabelStyle(20, FontType.Regular, Color.GRAY);
		Label lb[] = { getLabel(stt, 0), getLabel(sdt, 1), getLabel(ten, 2),
				getLabel(loaigd, 3), getLabel(sotientruocgd, 4),
				getLabel(sotiensaugd, 5), getLabel(sotiengd, 6),
				getLabel(donvi, 7), getLabel(thoigian, 8) };
		setComponentItem(lb);
		init();
	}

	Label getLabel(String text, int index) {
		Label lb = new Label(text, style);
		lb.setWrap(true);
		lb.setWidth(table.widthCol[index]);
		lb.setHeight(lb.getTextBounds().height);
		lb.setAlignment(Align.center);
		return lb;
	}
}
