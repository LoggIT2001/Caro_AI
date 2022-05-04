import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;

import java.io.*;

import java.util.*;

import java.awt.image.*;
import java.awt.print.*;

import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

public class JCanvas extends JComponent implements Printable {

	static final int addRenderingHints = 1, clip1 = 2, draw1 = 3, draw3DRect = 4, drawGlyphVector = 5, drawImage1 = 6,
			drawImage2 = 7, drawRenderableImage = 8, drawRenderedImage = 9, drawString1 = 10, drawString2 = 11,
			drawString3 = 12, drawString4 = 13, fill = 14, fill3DRect = 15, rotate1 = 16, rotate2 = 17, scale1 = 18,
			setBackground = 19, setComposite = 20, setPaint = 21, setRenderingHint = 22, setRenderingHints = 23,
			setStroke = 24, setTransform = 25, shear = 26, transform1 = 27, translate1 = 28, translate2 = 29,
			clearRect = 30,
			// clipRect=31,
			copyArea = 32, drawArc = 33, drawBytes = 34, drawChars = 35, drawImage4 = 36, drawImage5 = 37,
			drawImage6 = 38, drawImage7 = 39, drawImage8 = 40, drawImage9 = 41, drawLine = 42, drawOval = 43,
			drawPolygon1 = 44, drawPolygon2 = 45, drawPolyline = 46, drawRect = 47, drawRoundRect = 48, fillArc = 49,
			fillOval = 50, fillPolygon1 = 51, fillPolygon2 = 52, fillRect = 53, fillRoundRect = 54, setColor = 57,
			setFont = 58, setPaintMode = 59, setClip1 = 55, // setClip2=56,
			setXORMode = 60, clear = 61, opaque = 62, drawOutline = 63;

	// Tạo thực thể Canvas
	public JCanvas() {
		setFocusable(true);
	}

	public void draw(Shape s) {
		toBuffer(draw1, s);
	}

	public void fill(Shape s) {
		toBuffer(fill, s);
	}

	public Color getBackground() {
		return bufferBackground();
	}

	public GraphicsConfiguration getDeviceConfiguration() {
		Graphics2D g = getG();
		return g == null ? null : g.getDeviceConfiguration();
	}

	public FontRenderContext getFontRenderContext() {
		Graphics2D g = getG();
		return g == null ? null : g.getFontRenderContext();
	}

	public Paint getPaint() {
		return bufferPaint();
	}

	public Stroke getStroke() {
		return bufferStroke();
	}

	public AffineTransform getTransform() {
		return bufferTransform();
	}

	public void setBackground(Color color) {
		toBuffer(setBackground, color);
		toBuffer(clear, null);
	}

	public void setComposite(Composite comp) {
		toBuffer(setComposite, comp);
	}

	public void setPaint(Paint paint) {
		toBuffer(setPaint, paint);
	}

	public void setStroke(Stroke s) {
		toBuffer(setStroke, s);
	}

	public void setTransform(AffineTransform Tx) {
		toBuffer(setTransform, Tx);
	}

	public void transform(AffineTransform Tx) {
		toBuffer(transform1, Tx);
	}

	public void translate(double tx, double ty) {
		toBuffer(translate1, mkArg(tx, ty));
	}

	public void translate(int x, int y) {
		toBuffer(translate2, mkArg(x, y));
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		toBuffer(drawLine, mkArg(x1, y1, x2, y2));
	}

	public void drawOval(int x, int y, int width, int height) {
		toBuffer(drawOval, mkArg(x, y, width, height));
	}

	public void fillRect(int x, int y, int width, int height) {
		toBuffer(fillRect, mkArg(x, y, width, height));
	}

	public void setColor(Color c) {
		setPaint(c);
	}
    // thiết kế giao diện bảng 
	private Boolean theOpaque = null;
	private Color theBackground = null;
	private static Paint origPaint = Color.BLACK;
	private static Font origFont = new Font("Dialog", Font.PLAIN, 12);
	private static Stroke origStroke = new BasicStroke(1);

	private void doPaint(Graphics2D g, int s, Object o) {
		Object o1 = null, o2 = null, o3 = null, o4 = null, o5 = null, o6 = null, o7 = null, o8 = null, o9 = null,
				o10 = null, o11 = null;
		if (o instanceof Object[]) {
			Object[] a = (Object[]) o;
			if (a.length > 0)
				o1 = a[0];
			if (a.length > 1)
				o2 = a[1];
			if (a.length > 2)
				o3 = a[2];
			if (a.length > 3)
				o4 = a[3];
			if (a.length > 4)
				o5 = a[4];
			if (a.length > 5)
				o6 = a[5];
			if (a.length > 6)
				o7 = a[6];
			if (a.length > 7)
				o8 = a[7];
			if (a.length > 8)
				o9 = a[8];
			if (a.length > 9)
				o10 = a[9];
			if (a.length > 10)
				o11 = a[10];
		}
		switch (s) {
		case clear:
			paintBackground(g, theBackground);
			break;
		case setBackground:
			g.setBackground((Color) o);
			break;
		case setPaint:
			g.setPaint((Paint) o);
			break;
		case setStroke:
			g.setStroke((Stroke) o);
			break;
		case drawLine:
			g.drawLine((Integer) o1, (Integer) o2, (Integer) o3, (Integer) o4);
			break;
		case drawOval:
			g.drawOval((Integer) o1, (Integer) o2, (Integer) o3, (Integer) o4);
			break;
		case fillRect:
			g.fillRect((Integer) o1, (Integer) o2, (Integer) o3, (Integer) o4);
			break;
		case setClip1:
			g.setClip((Shape) o);
			break;
		case setColor:
			g.setColor((Color) o);
			break;
		case setFont:
			g.setFont((Font) o);
			break;
		case setPaintMode:
			g.setPaintMode();
			break;
		case setXORMode:
			g.setXORMode((Color) o);
			break;
		case opaque:
			super.setOpaque((Boolean) o);
			break;
		default:
			System.out.println("Unknown image operation " + s);
		}
	}


	private Object mkArg(Object... args) {
		return args;
	}

	private synchronized void toBuffer(int s, Object a) {
		a1.add(s);
		a2.add(a);
		if (s == clear) {
			clearBuffer();
		}
		if (s == opaque)
			theOpaque = (Boolean) a;
		if (s == setBackground)
			theBackground = (Color) a;
		if (inBuffer)
			return;
		if (isSetter(s))
			return;
		Graphics g = getGraphics();
		if (g == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(origPaint);
		g2.setFont(origFont);
		g2.setStroke(origStroke);
		for (int i = 0; i < a1.size() - 1; i++) {
			int s1 = a1.get(i);
			Object s2 = a2.get(i);
			if (isSetter(s1))
				doPaint(g2, s1, s2);
		}
		doPaint((Graphics2D) g, s, a);
	}

	private Graphics2D getG() {
		return (Graphics2D) getGraphics();
	}

	private List<Integer> a1 = new ArrayList<Integer>();
	private List<Object> a2 = new ArrayList<Object>();
	private List<Integer> a1x = new ArrayList<Integer>();
	private List<Object> a2x = new ArrayList<Object>();
	private boolean inBuffer = false;

	public synchronized void startBuffer() {
		inBuffer = true;
		a1x.clear();
		a2x.clear();
		List<Integer> h1 = a1x;
		List<Object> h2 = a2x;
		a1x = a1;
		a2x = a2;
		a1 = h1;
		a2 = h2;
	}

	public synchronized void endBuffer() {
		inBuffer = false;
		a1x.clear();
		a2x.clear();
		repaint();
	}

	private synchronized void clearBuffer() {
		Font f = bufferFont();
		Paint p = bufferPaint();
		Stroke s = bufferStroke();
		a1.clear();
		a2.clear();
		if (f != origFont) {
			a1.add(setFont);
			a2.add(f);
		}
		if (p != origPaint) {
			a1.add(setPaint);
			a2.add(p);
		}
		if (s != origStroke) {
			a1.add(setStroke);
			a2.add(s);
		}
		a1.add(clear);
		a2.add(null);
	}

	private synchronized void doBuffer(Graphics2D g2, boolean opq, Rectangle rt) {
		origTransform = g2.getTransform();
		if (opq && rt != null)
			g2.clearRect(rt.x, rt.y, rt.width, rt.height);
		g2.setPaint(origPaint);
		g2.setFont(origFont);
		g2.setStroke(origStroke);
		if (inBuffer) {
			for (int i = 0; i < a1x.size(); i++)
				doPaint(g2, a1x.get(i), a2x.get(i));
			origTransform = null;
			return;
		}
		for (int i = 0; i < a1.size(); i++)
			doPaint(g2, a1.get(i), a2.get(i));
		origTransform = null;
	}

	private synchronized Object lookBuffer(int s) {
		for (int i = a1.size() - 1; i >= 0; i--) {
			if (a1.get(i) == s)
				return a2.get(i);
		}
		return null;
	}

	private AffineTransform origTransform = null;
	private double printScale = 1;

	/** [Internal] */
	public void paintComponent(Graphics g) {
		boolean opq = true;
		if (theOpaque != null)
			opq = theOpaque;
		super.setOpaque(opq);
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Rectangle rt = getBounds();
		rt.x = 0;
		rt.y = 0;
		doBuffer(g2, opq, rt);
		chkFPS();
	}

	
	public void print() {
		print(1.0);
	}

	public void print(double scale) {
		printScale = scale;
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (printJob.printDialog()) {
			try {
				printJob.print();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void paintBackground(Graphics2D g2, Color theBackground) {
		Color color1 = g2.getColor();
		if (theBackground == null)
			theBackground = Color.white;
		g2.setColor(theBackground);
		g2.fillRect(0, 0, 30000, 30000);
		g2.setColor(color1);
	}

	public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
		if (pi >= 1) {
			return Printable.NO_SUCH_PAGE;
		}
		RepaintManager currentManager = RepaintManager.currentManager(this);
		currentManager.setDoubleBufferingEnabled(false);
		Graphics2D g2 = (Graphics2D) g;
		initState(g2, true);
		g2.translate((int) (pf.getImageableX() + 1), (int) (pf.getImageableY() + 1));
		g2.scale(printScale, printScale);
		doBuffer(g2, true, null);
		currentManager.setDoubleBufferingEnabled(true);
		return Printable.PAGE_EXISTS;
	}

//	public void writeToImage(String s, int w, int h) {
//		String ext;
//		File f;
//		try {
//			ext = s.substring(s.lastIndexOf(".") + 1);
//			f = new File(s);
//		} catch (Exception e) {
//			System.out.println(e);
//			return;
//		}
		 //code nay khong co tac dung gi trong san pham
//		if (!ext.equals("jpg") && !ext.equals("png")) {
//		System.out.println("Cannot write to file: Illegal extension " + ext);
//		return;
//	    }
//	 	boolean opq = true;
//		if (theOpaque != null)
//			opq = theOpaque;
//
//		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//		Graphics2D g2 = image.createGraphics();
//		g2.setBackground(Color.white);
//		g2.setPaint(Color.black);
//		g2.setStroke(new BasicStroke(1));
//		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//		doBuffer(g2, true, new Rectangle(0, 0, w, h));
//		try {
//			ImageIO.write(image, ext, f);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}

	private void initState(Graphics2D g2, boolean opq) {
	}

	private static int[] setOp = { addRenderingHints, clip1, rotate1, rotate2, scale1, setBackground, setComposite,
			setPaint, setRenderingHint, setRenderingHints, setStroke, setTransform, shear, transform1, translate1,
			translate2, 
			copyArea, setColor, setFont, setPaintMode, setClip1,
			setXORMode };

	private boolean isSetter(int s) {
		for (int s1 : setOp)
			if (s == s1)
				return true;
		return false;
	}

	public synchronized void dumpBuffer() {
		System.out.println("Opaque " + theOpaque);
		System.out.println("Background " + obj2str(theBackground));
		for (int i = 0; i < a1.size(); i++) {
			String s1 = totxt(a1.get(i));
			String s2 = obj2str(a2.get(i));
			System.out.println(i + " " + s1 + " " + s2);
		}
	}

	public String obj2str(Object o) {
		if (o == null)
			return "-null-";
		String s = "";
		if (o instanceof Object[]) {
			Object[] a = (Object[]) o;
			for (Object ox : a)
				s = s + " " + ox;
		} else if (o instanceof BasicStroke) {
			BasicStroke o1 = (BasicStroke) o;
			s = "BasicStroke(" + o1.getLineWidth() + "," + o1.getDashPhase() + "," + o1.getLineJoin() + ","
					+ o1.getMiterLimit() + "," + o1.getEndCap();

		} else
			s = "" + o;
		return s;
	}

	private String totxt(int i) {
		switch (i) {
		case 19:
			return "setBackground";
		case 20:
			return "setComposite";
		case 21:
			return "setPaint";
		case 22:
			return "setRenderingHint";
		case 23:
			return "setRenderingHints";
		case 24:
			return "setStroke";
		case 25:
			return "setTransform";
		case 42:
			return "drawLine";
		case 53:
			return "fillRect";
		case 57:
			return "setColor";
		case 55:
			return "setClip";
		case 58:
			return "setFont";
		case 59:
			return "setPaintMode";
		case 60:
			return "setXORMode";
		case 61:
			return "clear";
		case 62:
			return "clear";
		case 63:
			return "drawOutline";
		default:
			return "unknown: " + i;
		}
	}

	// ---------------------------------------------------------
	private int fpsCount = 0;
	private long fpsTime = 0;
	private int lastFPS = 10;

	private void chkFPS() {
		if (fpsCount == 0) {
			fpsTime = System.currentTimeMillis() / 1000;
			fpsCount++;
			return;
		}
		fpsCount++;
		long time = System.currentTimeMillis() / 1000;
		if (time != fpsTime) {
			lastFPS = fpsCount;
			fpsCount = 1;
			fpsTime = time;
		}
	}

	private synchronized AffineTransform bufferTransform() {
		AffineTransform r = new AffineTransform();
		for (int i = 0; i < a1.size(); i++) {
			int s1 = a1.get(i);
			Object s2 = a2.get(i);
			Object s3[] = null;
			if (s2 instanceof Object[])
				s3 = (Object[]) s2;

			if (s1 == setTransform) {
				r = makeTransform(s2);
			}
			if (s1 == shear)
				r.shear((Double) s3[0], (Double) s3[1]);
			if (s1 == rotate1)
				r.rotate((Double) s2);
			if (s1 == rotate2)
				r.rotate((Double) s3[0], (Double) s3[1], (Double) s3[2]);
			if (s1 == scale1)
				r.scale((Double) s3[0], (Double) s3[1]);
			if (s1 == translate1)
				r.translate((Double) s3[0], (Double) s3[1]);
			if (s1 == translate2)
				r.translate((Integer) s3[0], (Integer) s3[1]);
		}
		return r;
	}

	private AffineTransform makeTransform(Object o) {
		AffineTransform r = (AffineTransform) ((AffineTransform) o).clone();

		if (origTransform != null) {
			AffineTransform r2 = (AffineTransform) origTransform.clone();

			r2.concatenate(r);
			r = r2;
		}
		return r;
	}

	private Composite bufferComposite() {
		Composite c = (Composite) lookBuffer(setComposite);
		if (c == null)
			return AlphaComposite.SrcOver;
		return c;
	}

	private Stroke bufferStroke() {
		Stroke c = (Stroke) lookBuffer(setStroke);
		if (c == null)
			return origStroke;
		return c;
	}

	private Font bufferFont() {
		Font c = (Font) lookBuffer(setFont);
		if (c == null)
			return origFont;
		return c;
	}

	private Paint bufferPaint() {
		Paint c = (Paint) lookBuffer(setPaint);
		if (c == null)
			return origPaint;
		return c;
	}

	private Color bufferBackground() {
		Color c = (Color) lookBuffer(setBackground);
		if (c == null && theBackground != null)
			return theBackground;
		if (c == null)
			return null;
		return c;
	}

	public void draw(Shape s, int x, int y) {
		AffineTransform af = getTransform();
		translate(x, y);
		draw(s);
		setTransform(af);
	}

	public void fill(Shape s, int x, int y) {
		AffineTransform af = getTransform();
		translate(x, y);
		fill(s);
		setTransform(af);
	}

	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
		}
	}

	public static final long serialVersionUID = 42L;
}
