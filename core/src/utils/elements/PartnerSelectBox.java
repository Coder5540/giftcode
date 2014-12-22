package utils.elements;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PartnerSelectBox extends SelectBox<ItemPartnerSelectBox> {

	public static int ALL = -1;
	ArrayList<ItemPartnerSelectBox> list;

	public PartnerSelectBox(SelectBoxStyle style) {
		super(style);
		list = new ArrayList<ItemPartnerSelectBox>();
	}

	public PartnerSelectBox(Skin skin) {
		super(skin);
		list = new ArrayList<ItemPartnerSelectBox>();
	}

	public void addPartner(ItemPartnerSelectBox partner) {
		list.add(partner);
		addPartner(list);
	}

	public void addPartner(ArrayList<ItemPartnerSelectBox> partners) {
		ItemPartnerSelectBox ipartner[] = new ItemPartnerSelectBox[partners
				.size()];
		for (int i = 0; i < partners.size(); i++) {
			ipartner[i] = partners.get(i);
		}
		setItems(ipartner);
	}
}
