package mirrg.minecraft.mod.miragefairy.colormaker;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PanelSliderField extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1318287131881935822L;

	public static final Pattern PATTERN = Pattern.compile("[0-9]+");

	public ArrayList<IntConsumer> listeners = new ArrayList<>();

	private JSlider slider;
	private ParsingTextField<Integer> textField;
	private boolean isInProcessing = false;

	//

	/**
	 * Create the panel.
	 */
	public PanelSliderField()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {
			300, 50
		};
		gridBagLayout.rowHeights = new int[] {
			0
		};
		gridBagLayout.columnWeights = new double[] {
			0.0, 0.0
		};
		gridBagLayout.rowWeights = new double[] {
			0.0
		};
		setLayout(gridBagLayout);

		slider = new JSlider();
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.insets = new Insets(0, 0, 0, 5);
		gbc_slider.gridx = 0;
		gbc_slider.gridy = 0;
		add(slider, gbc_slider);
		slider.setMajorTickSpacing(8);
		slider.setPaintTicks(true);
		slider.setMaximum(255);

		textField = new ParsingTextField<>();
		textField.parser = s -> {
			int i;
			if (PATTERN.matcher(s.trim()).matches()) {
				try {
					i = Integer.parseInt(s.trim(), 10);
				} catch (Exception e) {
					return Optional.empty();
				}
				if (i < slider.getMinimum()) return Optional.empty();
				if (i > slider.getMaximum()) return Optional.empty();
				return Optional.of(i);
			} else {
				return Optional.empty();
			}
		};
		textField.builder = v -> "" + v;
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(5);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e)
			{
				if (isInProcessing) return;
				setValue(slider.getValue(), slider);
			}
		});
		textField.listener.add(i -> {
			if (isInProcessing) return;
			setValue(i, textField);
		});

		setValue(0);
	}

	//

	private int value;

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		setValue(value, null);
	}

	//

	private void setValue(int value, Object source)
	{
		isInProcessing = true;

		this.value = value;
		if (source != slider) slider.setValue(value);
		if (source != textField) textField.setValue(value);
		listeners.forEach(l -> {
			try {
				l.accept(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		isInProcessing = false;
	}
}
