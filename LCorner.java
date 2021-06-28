package hufs.cse.grimpan.strategy;


import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class LCorner extends JLabel {

	public LCorner(String s){
		this.setText(s);
		this.setBounds(0, 0, getWidth(), getHeight());
		initialize();
	}
	public LCorner(double ratio){
		DecimalFormat df = new DecimalFormat("0.0");
		
		this.setText("x "+df.format(ratio));
		this.setBounds(0, 0, getWidth(), getHeight());
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setBackground(TileModel.TILE_COLOR);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setOpaque(true);
		this.setBorder(new EtchedBorder());
			
	}

}
