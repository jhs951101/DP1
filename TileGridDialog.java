package hufs.cse.grimpan.strategy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class TileGridDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel jContentPane = null;
	private JButton jbtnConfirm = null;
	private JButton jbtnCancel = null;
	private JPanel jpApproveButtons = null;
	private JPanel jpEditFields = null;
	private JLabel lblRows;
	private JTextField jtfRow;
	private JLabel lblColumns;
	private JTextField jtfCol;
	private JLabel lblXMargin;
	private JTextField jtfXMargin;
	private JLabel lblYMargin;
	private JTextField jtfYMargin;
	private JLabel lblHGap;
	private JTextField jtfHGap;
	private JLabel lblVGap;
	private JTextField jtfVGap;
	private JLabel lblMm1;
	private JLabel lblMm2;
	private JLabel lblMm3;
	private JLabel lblMm4;

	private static TileModel model;
	private BaseTileFrame frame = null;

	/**
	 * Create the dialog.
	 */
	public TileGridDialog(BaseTileFrame frame, TileModel model) {
		super(frame);
		
		this.model = model;
		this.frame = frame;
		initialize();
	}
	
	private void initialize(){
		this.setSize(319, 259);
		this.setResizable(false);
		this.setModal(true);
		//this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

		this.setTitle("Tile Grid");
		this.setContentPane(getJContentPane());

		this.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				jbtnConfirm.requestFocusInWindow();
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJpEditFields(), BorderLayout.CENTER);
			jContentPane.add(getJpApproveButtons(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	private JPanel getJpEditFields() {
		if (jpEditFields == null) {
			jpEditFields = new JPanel();
			jpEditFields.setBorder(new EmptyBorder(0, 20, 10, 10));
			jpEditFields.setLayout(null);
			jpEditFields.add(getLblRows());
			jpEditFields.add(getJtfRow());
			jpEditFields.add(getLblColumns());
			jpEditFields.add(getJtfCol());
			jpEditFields.add(getLblXMargin());
			jpEditFields.add(getJtfXMargin());
			jpEditFields.add(getLblYMargin());
			jpEditFields.add(getJtfYMargin());
			jpEditFields.add(getLblHGap());
			jpEditFields.add(getJtfHGap());
			jpEditFields.add(getLblVGap());
			jpEditFields.add(getJtfVGap());
			jpEditFields.add(getLblMm1());
			jpEditFields.add(getLblMm2());
			jpEditFields.add(getLblMm3());
			jpEditFields.add(getLblMm4());
		}
		return jpEditFields;
	}
	private JPanel getJpApproveButtons() {
		if (jpApproveButtons == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setHgap(30);
			gridLayout.setVgap(0);
			gridLayout.setColumns(2);
			jpApproveButtons = new JPanel();
			jpApproveButtons.setPreferredSize(new Dimension(168, 60));
			jpApproveButtons.setBorder(new EmptyBorder(10, 30, 10, 30));
			jpApproveButtons.setLayout(gridLayout);
			jpApproveButtons.add(getJbtnConfirm(), null);
			jpApproveButtons.add(getJbtnCancel(), null);
		}
		return jpApproveButtons;
	}
	private JButton getJbtnConfirm() {
		if (jbtnConfirm == null) {
			jbtnConfirm = new JButton();
			jbtnConfirm.setText("확인");
			jbtnConfirm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					confirmAction();
				}
			});
		}
		return jbtnConfirm;
	}
	private void confirmAction() {

		int row = 1;
		int col = 1;
		double xmar = 10;
		double ymar = 10;
		double hgap = 10;
		double vgap = 10;
		try {
			row = Integer.parseInt(jtfRow.getText());
		} catch (NumberFormatException e) {
			// do nothing
		}
		try {
			col = Integer.parseInt(jtfCol.getText());
		} catch (NumberFormatException e) {
			// do nothing
		}
		try {
			xmar = Integer.parseInt(jtfXMargin.getText());
		} catch (NumberFormatException e) {
			// do nothing
		}
		try {
			ymar = Integer.parseInt(jtfYMargin.getText());
		} catch (NumberFormatException e) {
			// do nothing
		}
		try {
			hgap = Integer.parseInt(jtfHGap.getText());
		} catch (NumberFormatException e) {
			// do nothing
		}
		try {
			vgap = Integer.parseInt(jtfVGap.getText());
		} catch (NumberFormatException e) {
			// do nothing
		}

		row = Math.max(row, 1);
		col = Math.max(col, 1);
		xmar = Math.max(xmar, 0);
		ymar = Math.max(ymar, 0);
		hgap = Math.max(hgap, 0);
		vgap = Math.max(vgap, 0);
		//System.out.println("row="+row+" col="+col+" xmar="+xmar+" ymar="+ymar+" hgap="+hgap+" vgap="+vgap);
		
		xmar = model.getMilli2Pixel(xmar);
		ymar = model.getMilli2Pixel(ymar);
		hgap = model.getMilli2Pixel(hgap);
		vgap = model.getMilli2Pixel(vgap);

		double tileWidth = (model.getBaseWidth()-2*xmar-(col-1)*hgap)/col;
		double tileHeight = (model.getBaseHeight()-2*ymar-(row-1)*vgap)/row;
		//System.out.println("basew="+model.getBaseWidth()+" baseh="+model.getBaseHeight());
		
		
		model.tileRectList = new ArrayList<RectShape>();
		model.selMarkList = null;
		
		for (int r=0; r<row; ++r){
			double xstart = xmar;
			double ystart = ymar + r*(tileHeight+vgap);
			for (int c=0; c<col; ++c){
				xstart = xmar + c*(tileWidth+hgap);
				Rectangle2D tileShape = new Rectangle2D.Double(
						model.getPixel2Milli(xstart), model.getPixel2Milli(ystart), 
						model.getPixel2Milli(tileWidth), model.getPixel2Milli(tileHeight));
				//model.tileRectList.add(new TileRect(tileShape));
				model.addTileRect(new RectShape(tileShape, model));// will add with pixel to milli convert
			}
		}
		
		setVisible(false);
		this.frame.drawPanelView.repaint();
	}

	/**
	 * This method initializes jbtnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnCancel() {
		if (jbtnCancel == null) {
			jbtnCancel = new JButton();
			jbtnCancel.setText("취소");
			jbtnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					cancelAction();
				}
			});
		}
		return jbtnCancel;
	}
	private void cancelAction() {
		// do nothing
		
		super.setVisible(false);
	}


	private JLabel getLblRows() {
		if (lblRows == null) {
			lblRows = new JLabel("Rows");
			lblRows.setBounds(24, 10, 125, 21);
			lblRows.setHorizontalAlignment(SwingConstants.TRAILING);
		}
		return lblRows;
	}
	private JTextField getJtfRow() {
		if (jtfRow == null) {
			jtfRow = new JTextField();
			jtfRow.setText("1");
			jtfRow.setBounds(175, 10, 66, 21);
			jtfRow.setColumns(10);
		}
		return jtfRow;
	}
	private JLabel getLblColumns() {
		if (lblColumns == null) {
			lblColumns = new JLabel("Columns");
			lblColumns.setBounds(24, 35, 125, 21);
			lblColumns.setHorizontalAlignment(SwingConstants.TRAILING);
		}
		return lblColumns;
	}
	private JTextField getJtfCol() {
		if (jtfCol == null) {
			jtfCol = new JTextField();
			jtfCol.setText("1");
			jtfCol.setBounds(175, 35, 66, 21);
			jtfCol.setColumns(10);
		}
		return jtfCol;
	}
	private JLabel getLblXMargin() {
		if (lblXMargin == null) {
			lblXMargin = new JLabel("X Margin");
			lblXMargin.setBounds(24, 60, 125, 21);
			lblXMargin.setHorizontalAlignment(SwingConstants.TRAILING);
		}
		return lblXMargin;
	}
	private JTextField getJtfXMargin() {
		if (jtfXMargin == null) {
			jtfXMargin = new JTextField();
			jtfXMargin.setText("10");
			jtfXMargin.setBounds(175, 60, 66, 21);
			jtfXMargin.setColumns(10);
		}
		return jtfXMargin;
	}
	private JLabel getLblYMargin() {
		if (lblYMargin == null) {
			lblYMargin = new JLabel("Y Margin");
			lblYMargin.setBounds(24, 85, 125, 21);
			lblYMargin.setHorizontalAlignment(SwingConstants.TRAILING);
		}
		return lblYMargin;
	}
	private JTextField getJtfYMargin() {
		if (jtfYMargin == null) {
			jtfYMargin = new JTextField();
			jtfYMargin.setText("10");
			jtfYMargin.setBounds(175, 85, 66, 21);
			jtfYMargin.setColumns(10);
		}
		return jtfYMargin;
	}
	private JLabel getLblHGap() {
		if (lblHGap == null) {
			lblHGap = new JLabel("H Gap");
			lblHGap.setBounds(24, 110, 125, 21);
			lblHGap.setHorizontalAlignment(SwingConstants.TRAILING);
		}
		return lblHGap;
	}
	private JTextField getJtfHGap() {
		if (jtfHGap == null) {
			jtfHGap = new JTextField();
			jtfHGap.setText("10");
			jtfHGap.setBounds(175, 110, 66, 21);
			jtfHGap.setColumns(10);
		}
		return jtfHGap;
	}
	private JLabel getLblVGap() {
		if (lblVGap == null) {
			lblVGap = new JLabel("V Gap");
			lblVGap.setBounds(24, 135, 125, 21);
			lblVGap.setHorizontalAlignment(SwingConstants.TRAILING);
		}
		return lblVGap;
	}
	private JTextField getJtfVGap() {
		if (jtfVGap == null) {
			jtfVGap = new JTextField();
			jtfVGap.setText("10");
			jtfVGap.setBounds(175, 135, 66, 21);
			jtfVGap.setColumns(10);
		}
		return jtfVGap;
	}
	private JLabel getLblMm1() {
		if (lblMm1 == null) {
			lblMm1 = new JLabel("mm");
			lblMm1.setBounds(252, 66, 49, 15);
		}
		return lblMm1;
	}
	private JLabel getLblMm2() {
		if (lblMm2 == null) {
			lblMm2 = new JLabel("mm");
			lblMm2.setBounds(252, 91, 49, 15);
		}
		return lblMm2;
	}
	private JLabel getLblMm3() {
		if (lblMm3 == null) {
			lblMm3 = new JLabel("mm");
			lblMm3.setBounds(252, 116, 49, 15);
		}
		return lblMm3;
	}
	private JLabel getLblMm4() {
		if (lblMm4 == null) {
			lblMm4 = new JLabel("mm");
			lblMm4.setBounds(252, 141, 49, 15);
		}
		return lblMm4;
	}

}
