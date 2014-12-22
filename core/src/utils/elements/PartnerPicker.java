package utils.elements;

import java.util.ArrayList;

import utils.elements.PartnerPicker.MyPartner;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class PartnerPicker extends SelectBox<MyPartner> {

	MyPartner select;
	ArrayList<MyPartner> list;

	public static int ALL = -1;

	public PartnerPicker(SelectBoxStyle style) {
		super(style);
		init();
	}

	public PartnerPicker(Skin skin) {
		super(skin);
		init();
	}

	public void init() {
		list = new ArrayList<MyPartner>();
	}

	public void addPartner(MyPartner partner) {
		list.add(partner);
		setItems(getListData());
	}

	public void addPartner(int id, String title, String code) {
		addPartner(new MyPartner(id, title, code));
	}

	public Array<MyPartner> getListData() {
		Array<MyPartner> data = new Array<MyPartner>();
		for (MyPartner part : list) {
			data.add(part);
		}
		return data;
	}

	public String getPartnerCode() {
		return getSelected().code;
	}
	
	public int getPartnerId(){
		return getSelected().id;
	}

	public class MyPartner {
		public int id;
		public String name;
		public String code;

		public MyPartner(int id, String name, String code) {
			this.id = id;
			this.name = name;
			this.code = code;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public int getSize() {
		return list.size();
	}
}
