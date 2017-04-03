package mirrg.minecraft.mod.miragefairy.colormaker;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

import javax.swing.JPanel;

public class PanelColorSlider extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4787374535556960163L;

	public static final Pattern PATTERN = Pattern.compile("[0-9a-fA-F]{6}");

	public ArrayList<Consumer<Color>> listeners = new ArrayList<>();

	private PanelSliderField sliderG;
	private PanelSliderField sliderB;
	private PanelSliderField sliderR;
	private ParsingTextField<Integer> textField;

	private boolean isInProcessing = false;

	//

	/**
	 * Create the panel.
	 */
	public PanelColorSlider()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {
			0, 0
		};
		gridBagLayout.rowHeights = new int[] {
			0, 0, 0, 0
		};
		gridBagLayout.columnWeights = new double[] {
			1.0, Double.MIN_VALUE
		};
		gridBagLayout.rowWeights = new double[] {
			0.0, 0.0, 0.0, 0.0
		};
		setLayout(gridBagLayout);

		sliderR = new PanelSliderField();
		GridBagConstraints gbc_sliderR = new GridBagConstraints();
		gbc_sliderR.insets = new Insets(0, 0, 5, 0);
		gbc_sliderR.fill = GridBagConstraints.BOTH;
		gbc_sliderR.gridx = 0;
		gbc_sliderR.gridy = 0;
		add(sliderR, gbc_sliderR);

		sliderG = new PanelSliderField();
		GridBagConstraints gbc_sliderG = new GridBagConstraints();
		gbc_sliderG.insets = new Insets(0, 0, 5, 0);
		gbc_sliderG.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderG.gridx = 0;
		gbc_sliderG.gridy = 1;
		add(sliderG, gbc_sliderG);

		sliderB = new PanelSliderField();
		GridBagConstraints gbc_sliderB = new GridBagConstraints();
		gbc_sliderB.insets = new Insets(0, 0, 5, 0);
		gbc_sliderB.fill = GridBagConstraints.BOTH;
		gbc_sliderB.gridx = 0;
		gbc_sliderB.gridy = 2;
		add(sliderB, gbc_sliderB);

		textField = new ParsingTextField<>();
		textField.parser = s -> {
			if (PATTERN.matcher(s.trim()).matches()) {
				return Optional.of(Integer.parseInt(s.trim(), 16));
			} else {
				return Optional.empty();
			}
		};
		textField.builder = v -> String.format("%06X", v & 0xffffff);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 3;
		add(textField, gbc_textField);
		textField.setColumns(10);

		sliderR.listeners.add(new IntConsumer() {
			@Override
			public void accept(int value)
			{
				if (isInProcessing) return;
				setValue(new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), sliderR);
			}
		});
		sliderG.listeners.add(new IntConsumer() {
			@Override
			public void accept(int value)
			{
				if (isInProcessing) return;
				setValue(new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), sliderG);
			}
		});
		sliderB.listeners.add(new IntConsumer() {
			@Override
			public void accept(int value)
			{
				if (isInProcessing) return;
				setValue(new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), sliderB);
			}
		});
		textField.listener.add(i -> {
			if (isInProcessing) return;
			setValue(new Color(textField.getValue()), textField);
		});

		setValue(Color.white);
	}

	//

	private Color value;

	public void setValue(Color value)
	{
		setValue(value, null);
	}

	public Color getValue()
	{
		return value;
	}

	//

	private void setValue(Color value, Object source)
	{
		isInProcessing = true;

		this.value = value;
		if (source != textField) textField.setValue(value.getRGB());
		if (source != sliderR) sliderR.setValue(value.getRed());
		if (source != sliderG) sliderG.setValue(value.getGreen());
		if (source != sliderB) sliderB.setValue(value.getBlue());
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
