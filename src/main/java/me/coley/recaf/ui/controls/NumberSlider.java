package me.coley.recaf.ui.controls;

import javafx.scene.control.Slider;
import me.coley.recaf.config.FieldWrapper;
import me.coley.recaf.control.gui.GuiController;

import java.util.Objects;

/**
 * Generic number scaling control &amp; utility.
 *
 * @author Matt
 */
public class NumberSlider<N extends Number> extends Slider {
	/**
	 * @param controller Controller to act on.
	 * @param wrapper Numeric field wrapper.
	 */
	public NumberSlider(GuiController controller, FieldWrapper wrapper, int min, int max, int majorTick) {
		setMin(min);
		setMax(max);
		setMajorTickUnit(majorTick);
		setShowTickMarks(true);
		setShowTickLabels(true);
		setSnapToTicks(true);
		setValue(toDouble(wrapper.get()));
		valueProperty().addListener(((observable, oldValue, newValue) -> {
			setValue((Double) newValue);
			if(!oldValue.equals(newValue)) {
				wrapper.set(toLong(newValue));
			}
		}));
	}

	private static long toLong(Number value) {
		return value.longValue();
	}

	private static double toDouble(Object value) {
		return ((Number) value).doubleValue();
	}
}
