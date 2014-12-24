package utils.elements;

import utils.factory.MyClassReflection;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class CustomTable extends Table {
	public boolean	customUpdate	= true;

	@Override
	public void layout() {
		if (!customUpdate) {
			super.layout();
			return;
		}
		float width = getWidth();
		float height = getHeight();

		Method layout_method = MyClassReflection.getMethod(Table.class,
				"layout", float.class, float.class, float.class, float.class);
		layout_method.setAccessible(true);
		try {
			layout_method.invoke(this, 0, 0, getWidth(), getHeight());
		} catch (ReflectionException e) {
			e.printStackTrace();
		}

		Array<Cell> cells = MyClassReflection.getValue(Table.class, "cells",
				this, Array.class);

		boolean round = MyClassReflection.getValue(Table.class, "round", this,
				Boolean.class);

		if (round) {
			for (Cell c : cells) {
				if (c != null) {
					final float actorWidth = Math.round(c.getActorWidth());
					final float actorHeight = Math.round(c.getActorHeight());
					final float actorX = Math.round(c.getActorX());
					final float actorY = height - Math.round(c.getActorY())
							- actorHeight;
					c.setActorBounds(actorX, actorY, actorWidth, actorHeight);
					final Actor actor = c.getActor();
					actor.setOrigin(Align.center);
					if (actor != null) {
						actor.addAction(Actions.sequence(Actions.parallel(
								Actions.sizeTo(actorWidth, actorHeight, 0.2f,
										Interpolation.sineOut), Actions.moveTo(
										actorX, actorY, 0.2f,
										Interpolation.sineOut)), Actions
								.run(new Runnable() {
									@Override
									public void run() {
										actor.setBounds(actorX, actorY,
												actorWidth, actorHeight);
									}
								})));
					}
				}
			}
		} else {
			for (Cell c : cells) {
				if (c != null) {
					final float actorWidth = MyClassReflection.getValue(
							Cell.class, "actorWidth", this, Float.class);
					final float actorHeight = MyClassReflection.getValue(
							Cell.class, "actorHeight", this, Float.class);
					final float actorX = MyClassReflection.getValue(Cell.class,
							"actorX", this, Float.class);
					final float y = MyClassReflection.getValue(Cell.class,
							"actorY", this, Float.class);
					final float actorY = height - y - actorHeight;
					c.setActorY(actorY);
					final Actor actor = MyClassReflection.getValue(Cell.class,
							"actor", this, Actor.class);
					actor.setOrigin(Align.center);
					if (actor != null) {
						actor.addAction(Actions.sequence(Actions.parallel(
								Actions.sizeTo(actorWidth, actorHeight, 0.2f,
										Interpolation.sineOut), Actions.moveTo(
										actorX, actorY, 0.2f,
										Interpolation.sineOut)), Actions
								.run(new Runnable() {
									@Override
									public void run() {
										actor.setBounds(actorX, actorY,
												actorWidth, actorHeight);
									}
								})));
					}
				}
			}
		}
		// Validate children separately from sizing actors to ensure actors
		// without a cell are validated.
		Array<Actor> children = getChildren();
		for (int i = 0, n = children.size; i < n; i++) {
			Actor child = children.get(i);
			if (child instanceof Layout)
				((Layout) child).validate();
		}
	}
}
