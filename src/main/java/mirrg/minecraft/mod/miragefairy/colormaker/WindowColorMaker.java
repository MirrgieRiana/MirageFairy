package mirrg.minecraft.mod.miragefairy.colormaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class WindowColorMaker extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4797756676830405359L;
	private JSplitPane splitPane;
	private JPanel panel;
	private PanelColorSlider panelColorSlider0;
	private PanelColorSlider panelColorSlider1;
	private PanelColorSlider panelColorSlider2;
	private JPanel panelImage;
	private JLabel labelFairy;
	private PanelColorSlider panelColorSliderBG;
	private JLabel labelFairySpirit;
	private JLabel labelMagicSphere;

	private boolean isInProcessing = false;
	private boolean leadyUpdate = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				try {
					WindowColorMaker frame = new WindowColorMaker();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WindowColorMaker()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 754);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setContinuousLayout(true);

		panel = new JPanel();
		panel.setBorder(new EmptyBorder(4, 4, 4, 4));
		splitPane.setRightComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {
			0
		};
		gbl_panel.rowHeights = new int[] {
			0, 0, 0, 0, 0, 0
		};
		gbl_panel.columnWeights = new double[] {
			1.0
		};
		gbl_panel.rowWeights = new double[] {
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0
		};
		panel.setLayout(gbl_panel);

		panelColorSliderBG = new PanelColorSlider();
		GridBagLayout gbl_panelColorSliderBG = (GridBagLayout) panelColorSliderBG.getLayout();
		gbl_panelColorSliderBG.rowWeights = new double[] {
			1.0, 1.0, 1.0, 1.0
		};
		gbl_panelColorSliderBG.rowHeights = new int[] {
			0, 0, 0, 0
		};
		gbl_panelColorSliderBG.columnWeights = new double[] {
			1.0
		};
		gbl_panelColorSliderBG.columnWidths = new int[] {
			0
		};
		GridBagConstraints gbc_panelColorSliderBG = new GridBagConstraints();
		gbc_panelColorSliderBG.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelColorSliderBG.insets = new Insets(0, 0, 5, 0);
		gbc_panelColorSliderBG.gridx = 0;
		gbc_panelColorSliderBG.gridy = 0;
		panel.add(panelColorSliderBG, gbc_panelColorSliderBG);

		panelColorSlider0 = new PanelColorSlider();
		GridBagConstraints gbc_panelColorSlider0 = new GridBagConstraints();
		gbc_panelColorSlider0.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelColorSlider0.insets = new Insets(0, 0, 5, 0);
		gbc_panelColorSlider0.gridx = 0;
		gbc_panelColorSlider0.gridy = 1;
		panel.add(panelColorSlider0, gbc_panelColorSlider0);

		panelColorSlider1 = new PanelColorSlider();
		GridBagConstraints gbc_panelColorSlider1 = new GridBagConstraints();
		gbc_panelColorSlider1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelColorSlider1.insets = new Insets(0, 0, 5, 0);
		gbc_panelColorSlider1.gridx = 0;
		gbc_panelColorSlider1.gridy = 2;
		panel.add(panelColorSlider1, gbc_panelColorSlider1);

		panelColorSlider2 = new PanelColorSlider();
		GridBagConstraints gbc_panelColorSlider2 = new GridBagConstraints();
		gbc_panelColorSlider2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelColorSlider2.insets = new Insets(0, 0, 5, 0);
		gbc_panelColorSlider2.gridx = 0;
		gbc_panelColorSlider2.gridy = 3;
		panel.add(panelColorSlider2, gbc_panelColorSlider2);

		panelColorSlider3 = new PanelColorSlider();
		GridBagConstraints gbc_panelColorSlider3 = new GridBagConstraints();
		gbc_panelColorSlider3.insets = new Insets(0, 0, 5, 0);
		gbc_panelColorSlider3.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelColorSlider3.gridx = 0;
		gbc_panelColorSlider3.gridy = 4;
		panel.add(panelColorSlider3, gbc_panelColorSlider3);

		panelImage = new JPanel();
		splitPane.setLeftComponent(panelImage);
		GridBagLayout gbl_panelImage = new GridBagLayout();
		gbl_panelImage.columnWidths = new int[] {
			100
		};
		gbl_panelImage.rowHeights = new int[] {
			0, 0, 0
		};
		gbl_panelImage.columnWeights = new double[] {
			0.0
		};
		gbl_panelImage.rowWeights = new double[] {
			0.0, 0.0, 0.0
		};
		panelImage.setLayout(gbl_panelImage);

		labelFairy = new JLabel("");
		labelFairy.setPreferredSize(new Dimension(64, 64));
		GridBagConstraints gbc_labelFairy = new GridBagConstraints();
		gbc_labelFairy.insets = new Insets(0, 0, 5, 0);
		gbc_labelFairy.gridx = 0;
		gbc_labelFairy.gridy = 0;
		panelImage.add(labelFairy, gbc_labelFairy);

		labelFairySpirit = new JLabel("");
		labelFairySpirit.setPreferredSize(new Dimension(64, 64));
		GridBagConstraints gbc_labelFairySpirit = new GridBagConstraints();
		gbc_labelFairySpirit.insets = new Insets(0, 0, 5, 0);
		gbc_labelFairySpirit.gridx = 0;
		gbc_labelFairySpirit.gridy = 1;
		panelImage.add(labelFairySpirit, gbc_labelFairySpirit);

		labelMagicSphere = new JLabel("");
		labelMagicSphere.setPreferredSize(new Dimension(64, 64));
		GridBagConstraints gbc_labelMagicSphere = new GridBagConstraints();
		gbc_labelMagicSphere.gridx = 0;
		gbc_labelMagicSphere.gridy = 2;
		panelImage.add(labelMagicSphere, gbc_labelMagicSphere);

		textFieldColors = new JTextField();
		GridBagConstraints gbc_textFieldColors = new GridBagConstraints();
		gbc_textFieldColors.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldColors.gridx = 0;
		gbc_textFieldColors.gridy = 5;
		panel.add(textFieldColors, gbc_textFieldColors);
		textFieldColors.setColumns(10);

		panelColorSliderBG.listeners.add(color -> {
			if (isInProcessing) return;
			setBackgroundColor(color, panelColorSliderBG);
		});
		panelColorSlider0.listeners.add(color -> {
			if (isInProcessing) return;
			setValue(new Color[] {
				panelColorSlider0.getValue(),
				panelColorSlider1.getValue(),
				panelColorSlider2.getValue(),
				panelColorSlider3.getValue(),
			}, panelColorSlider0);
		});
		panelColorSlider1.listeners.add(color -> {
			if (isInProcessing) return;
			setValue(new Color[] {
				panelColorSlider0.getValue(),
				panelColorSlider1.getValue(),
				panelColorSlider2.getValue(),
				panelColorSlider3.getValue(),
			}, panelColorSlider1);
		});
		panelColorSlider2.listeners.add(color -> {
			if (isInProcessing) return;
			setValue(new Color[] {
				panelColorSlider0.getValue(),
				panelColorSlider1.getValue(),
				panelColorSlider2.getValue(),
				panelColorSlider3.getValue(),
			}, panelColorSlider2);
		});
		panelColorSlider3.listeners.add(color -> {
			if (isInProcessing) return;
			setValue(new Color[] {
				panelColorSlider0.getValue(),
				panelColorSlider1.getValue(),
				panelColorSlider2.getValue(),
				panelColorSlider3.getValue(),
			}, panelColorSlider3);
		});
		textFieldColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (isInProcessing) return;
				setValue(textFieldColors.getText(), textFieldColors);
			}

		});
		textFieldColors.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if (isInProcessing) return;
				setValue(textFieldColors.getText(), textFieldColors);
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				if (isInProcessing) return;
				setValue(textFieldColors.getText(), textFieldColors);
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				if (isInProcessing) return;
				setValue(textFieldColors.getText(), textFieldColors);
			}
		});

		setBackgroundColor(Color.gray);
		setValue(new Color[] {
			Color.white,
			Color.white,
			Color.white,
			Color.white,
		});

		leadyUpdate = true;
		updateImage();

		pack();
	}

	private void setValue(String text, Object source)
	{
		try {
			String[] colors = text.split(",");
			setValue(new Color[] {
				Color.decode(colors[0].trim()),
				Color.decode(colors[1].trim()),
				Color.decode(colors[2].trim()),
				Color.decode(colors[3].trim()),
			}, source);
		} catch (RuntimeException e2) {}
	}

	private Color[] value;
	private PanelColorSlider panelColorSlider3;

	private void setValue(Color[] value)
	{
		setValue(value, null);
	}

	private void setValue(Color[] value, Object source)
	{
		isInProcessing = true;

		this.value = value;
		if (source != panelColorSlider0) panelColorSlider0.setValue(value[0]);
		if (source != panelColorSlider1) panelColorSlider1.setValue(value[1]);
		if (source != panelColorSlider2) panelColorSlider2.setValue(value[2]);
		if (source != panelColorSlider3) panelColorSlider3.setValue(value[3]);
		if (source != textFieldColors) textFieldColors.setText(String.format("0x%06X, 0x%06X, 0x%06X, 0x%06X",
			value[0].getRGB() & 0xffffff,
			value[1].getRGB() & 0xffffff,
			value[2].getRGB() & 0xffffff,
			value[3].getRGB() & 0xffffff));
		updateImage();

		isInProcessing = false;
	}

	private ArrayList<ArrayList<SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>>>> images;
	private JTextField textFieldColors;
	{
		try {
			images = ImageLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateImage()
	{
		if (!leadyUpdate) return;
		labelFairy.setIcon(new ImageIcon(createImage(images.get(0))));
		labelFairySpirit.setIcon(new ImageIcon(createImage(images.get(1))));
		labelMagicSphere.setIcon(new ImageIcon(createImage(images.get(2))));
	}

	private BufferedImage createImage(ArrayList<SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>>> entries)
	{
		BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 64; y++) {
				int r1 = panelImage.getBackground().getRed();
				int g1 = panelImage.getBackground().getGreen();
				int b1 = panelImage.getBackground().getBlue();

				for (SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>> entry : entries) {
					Color colorMul;
					if (!entry.getValue().getKey()) {
						if ("skin".equals(entry.getValue().getValue())) colorMul = value[0];
						else if ("darker".equals(entry.getValue().getValue())) colorMul = value[1];
						else if ("brighter".equals(entry.getValue().getValue())) colorMul = value[2];
						else if ("hair".equals(entry.getValue().getValue())) colorMul = value[3];
						else colorMul = Color.white;
					} else {
						colorMul = Color.decode(entry.getValue().getValue());
					}

					int argbOver = entry.getKey().getRGB(x / 4, y / 4);
					int a2 = (argbOver >> 24) & 0xff;
					int r2 = (argbOver >> 16) & 0xff;
					int g2 = (argbOver >> 8) & 0xff;
					int b2 = (argbOver >> 0) & 0xff;

					r2 = r2 * colorMul.getRed() / 255;
					g2 = g2 * colorMul.getGreen() / 255;
					b2 = b2 * colorMul.getBlue() / 255;

					r1 = (r1 * (255 - a2) + r2 * a2) / 255;
					g1 = (g1 * (255 - a2) + g2 * a2) / 255;
					b1 = (b1 * (255 - a2) + b2 * a2) / 255;
				}

				image.setRGB(x, y, ((r1 & 0xff) << 16) | ((g1 & 0xff) << 8) | ((b1 & 0xff) << 0));
			}
		}

		return image;
	}

	private void setBackgroundColor(Color backgroundColor)
	{
		setBackgroundColor(backgroundColor, null);
	}

	private void setBackgroundColor(Color backgroundColor, Object source)
	{
		isInProcessing = true;

		panelImage.setBackground(backgroundColor);
		if (source != panelColorSliderBG) panelColorSliderBG.setValue(backgroundColor);
		updateImage();

		isInProcessing = false;
	}

}
